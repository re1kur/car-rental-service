#!/bin/bash
# Keycloak entrypoint:
#  1) import the realm
#  2) start Keycloak (background)
#  3) wait for the admin API
#  4) if only the bootstrap (temporary) admin exists -> create a PERMANENT admin,
#     seed app users (admin/admin, user/user), then delete the temporary admin
#  5) hand the process back to Keycloak (wait)
cd /opt/keycloak/bin

SERVER=http://localhost:8080
REALM=rental-service-realm
CLIENT=car-rental-app

echo "[init] importing realm..."
./kc.sh import --dir /tmp/realms --override true || echo "[init] import warning (continuing)"

echo "[init] starting Keycloak..."
./kc.sh start-dev &
KC_PID=$!

login_perm() { ./kcadm.sh config credentials --server "$SERVER" --realm master \
    --user "$KEYCLOAK_ADMIN_NAME" --password "$KEYCLOAK_ADMIN_PASSWORD" >/dev/null 2>&1; }
login_boot() { ./kcadm.sh config credentials --server "$SERVER" --realm master \
    --user "$KC_BOOTSTRAP_ADMIN_USERNAME" --password "$KC_BOOTSTRAP_ADMIN_PASSWORD" >/dev/null 2>&1; }

id_of() { grep -o '"id"[^,]*' | head -1 | cut -d'"' -f4; }

echo "[init] waiting for admin API..."
STATE=down
for _ in $(seq 1 60); do
  if login_perm; then STATE=perm; break; fi
  if login_boot; then STATE=boot; break; fi
  sleep 3
done

mark_ready() { touch /tmp/kc-ready; echo "[init] readiness marker set (/tmp/kc-ready)."; }

if [ "$STATE" = "perm" ]; then
  echo "[init] permanent admin already exists — setup skipped."
  mark_ready
elif [ "$STATE" = "boot" ]; then
  echo "[init] bootstrap admin detected — provisioning permanent admin and users..."

  # --- permanent master admin ---
  ./kcadm.sh create users -r master -s username="$KEYCLOAK_ADMIN_NAME" -s enabled=true 2>/dev/null || true
  ./kcadm.sh set-password -r master --username "$KEYCLOAK_ADMIN_NAME" --new-password "$KEYCLOAK_ADMIN_PASSWORD"
  ./kcadm.sh add-roles -r master --uusername "$KEYCLOAK_ADMIN_NAME" --rolename admin

  # --- ensure ROLE_USER client role exists (ROLE_ADMIN comes from the realm import) ---
  CID=$(./kcadm.sh get clients -r "$REALM" -q clientId="$CLIENT" --fields id 2>/dev/null | id_of)
  ./kcadm.sh create clients/"$CID"/roles -r "$REALM" -s name=ROLE_USER 2>/dev/null || true

  # --- seed admin user (admin / admin) ---
  ./kcadm.sh create users -r "$REALM" -s username=admin -s email=admin@rentcar.local \
      -s emailVerified=true -s firstName=App -s lastName=Admin -s enabled=true 2>/dev/null || true
  ./kcadm.sh set-password -r "$REALM" --username admin --new-password admin
  ./kcadm.sh add-roles -r "$REALM" --uusername admin --cclientid "$CLIENT" --rolename ROLE_ADMIN
  ./kcadm.sh add-roles -r "$REALM" --uusername admin --cclientid "$CLIENT" --rolename ROLE_USER 2>/dev/null || true

  # --- seed regular user (user / user) ---
  ./kcadm.sh create users -r "$REALM" -s username=user -s email=user@rentcar.local \
      -s emailVerified=true -s firstName=App -s lastName=User -s enabled=true 2>/dev/null || true
  ./kcadm.sh set-password -r "$REALM" --username user --new-password user
  ./kcadm.sh add-roles -r "$REALM" --uusername user --cclientid "$CLIENT" --rolename ROLE_USER

  # --- delete the temporary bootstrap admin (re-auth as permanent first) ---
  login_perm || true
  BID=$(./kcadm.sh get users -r master -q username="$KC_BOOTSTRAP_ADMIN_USERNAME" --fields id 2>/dev/null | id_of)
  if [ -n "$BID" ]; then
    ./kcadm.sh delete users/"$BID" -r master && echo "[init] removed temporary bootstrap admin."
  fi
  echo "[init] setup complete (permanent admin + admin/admin + user/user)."
  mark_ready
else
  echo "[init] WARNING: admin API not reachable; skipping provisioning."
fi

wait "$KC_PID"
