#!/usr/bin/env bash
# Demo: trigger a server-side push by creating a car via the API.
# Subscribed clients get an FCM "New car available" push even with the tab closed.
#
# Usage:   ./scripts/push-demo.sh ["Model name"]
# Env:     BASE, ADMIN_USER, ADMIN_PASS (defaults: localhost:8080, admin, admin)
set -u

BASE="${BASE:-http://localhost:8080}"
ADMIN_USER="${ADMIN_USER:-admin}"
ADMIN_PASS="${ADMIN_PASS:-admin}"

CJ="$(mktemp)"
KC="$(mktemp)"
CREATE="$(mktemp)"
trap 'rm -f "$CJ" "$KC" "$CREATE"' EXIT

echo "→ discovering make / type / engine ..."
MAKE_ID=$(curl -s "$BASE/api/v1/makes" | grep -oP '"id":\K[0-9]+' | head -1)
TYPE=$(curl -s "$BASE/api/v1/cars/types" | grep -oP '"id":"\K[^"]+' | head -1)
ENGINE=$(curl -s "$BASE/api/v1/cars/engines" | grep -oP '"id":"\K[^"]+' | head -1)
: "${MAKE_ID:?could not read makes from $BASE}"
: "${TYPE:?could not read car types}"
: "${ENGINE:?could not read engines}"
echo "  make=$MAKE_ID type=$TYPE engine=$ENGINE"

echo "→ logging in as $ADMIN_USER ..."
curl -s -c "$CJ" -b "$CJ" -L "$BASE/oauth2/authorization/keycloak" -o "$KC"
ACTION=$(grep -oP 'id="kc-form-login"[^>]*action="\K[^"]+' "$KC" | head -1 | sed 's/&amp;/\&/g')
: "${ACTION:?could not find Keycloak login form}"
curl -s -c "$CJ" -b "$CJ" -L \
  --data-urlencode "username=$ADMIN_USER" --data-urlencode "password=$ADMIN_PASS" \
  "$ACTION" -o /dev/null

echo "→ fetching CSRF token ..."
curl -s -b "$CJ" "$BASE/cars/create" -o "$CREATE"
CSRF=$(grep -oP 'name="_csrf"[^>]*value="\K[^"]+' "$CREATE" | head -1)
: "${CSRF:?login failed (no CSRF) — check admin credentials}"

PLATE=$(tr -dc 'A-Z0-9' < /dev/urandom | head -c 6)
MODEL="${1:-Demo $(date +%H%M%S)}"

echo "→ creating car: make=$MAKE_ID type=$TYPE engine=$ENGINE model='$MODEL' plate=$PLATE ..."
CODE=$(curl -s -b "$CJ" -X POST "$BASE/cars/create" -o /dev/null -w "%{http_code}" \
  -F "_csrf=$CSRF" \
  -F "makeId=$MAKE_ID" \
  -F "carType=$TYPE" \
  -F "engine=$ENGINE" \
  -F "model=$MODEL" \
  -F "year=2025" \
  -F "licensePlate=$PLATE" \
  -F "cost=199" \
  -F "available=true")

if [ "$CODE" = "302" ] || [ "$CODE" = "200" ]; then
  echo "✓ car created — server is broadcasting an FCM push to all subscribers."
  echo "  Watch the desktop notification (the browser tab can be closed)."
else
  echo "✗ create failed (HTTP $CODE)"
  exit 1
fi
