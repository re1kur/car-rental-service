(function () {
    'use strict';

    const btn = document.querySelector('[data-push-toggle]');
    if (!btn) {
        return;
    }

    const FB_VERSION = '10.12.2';
    const SW_SCOPE = '/firebase-cloud-messaging-push-scope';
    const cfg = window.__PUSH_CONFIG__;
    let messaging = null;
    let swReg = null;

    if (!cfg || !cfg.enabled) {
        btn.disabled = true;
        btn.textContent = 'Push not configured';
        return;
    }

    let subscribed = !!localStorage.getItem('push-token');
    updateLabel();

    function updateLabel() {
        btn.textContent = subscribed ? 'Disable browser push' : 'Enable browser push';
        btn.classList.toggle('on', subscribed);
    }

    function loadScript(src) {
        return new Promise(function (resolve, reject) {
            const s = document.createElement('script');
            s.src = src;
            s.onload = resolve;
            s.onerror = reject;
            document.head.appendChild(s);
        });
    }

    async function ensureFirebase() {
        if (window.firebase && window.firebase.messaging) {
            return;
        }
        await loadScript('https://www.gstatic.com/firebasejs/' + FB_VERSION + '/firebase-app-compat.js');
        await loadScript('https://www.gstatic.com/firebasejs/' + FB_VERSION + '/firebase-messaging-compat.js');
    }

    function showLocal(title, body, url) {
        if (!('Notification' in window) || Notification.permission !== 'granted') {
            return;
        }
        const opts = {body: body || '', icon: '/img/car-placeholder.svg', data: {url: url || '/'}};
        try {
            if (swReg && swReg.showNotification) {
                swReg.showNotification(title || 'RentCar', opts);
            } else {
                new Notification(title || 'RentCar', opts);
            }
        } catch (e) {
            // ignore rendering errors
        }
    }

    async function initMessaging() {
        await ensureFirebase();
        if (!window.firebase.apps.length) {
            window.firebase.initializeApp(cfg.firebase);
        }
        // Own scope so the FCM worker doesn't clash with the WebSocket-notification SW at '/'.
        swReg = await navigator.serviceWorker.register('/firebase-messaging-sw.js', {scope: SW_SCOPE});
        messaging = window.firebase.messaging();
        // Foreground delivery: render it ourselves (data-only messages don't auto-show).
        messaging.onMessage(function (payload) {
            const d = payload.data || {};
            showLocal(d.title, d.body, d.url);
        });
    }

    async function subscribe() {
        try {
            const permission = await Notification.requestPermission();
            if (permission !== 'granted') {
                alert('Notification permission denied (check the lock icon → Permissions, and the OS notification settings).');
                return;
            }
            await initMessaging();
            const token = await messaging.getToken({vapidKey: cfg.vapidKey, serviceWorkerRegistration: swReg});
            if (!token) {
                alert('Could not obtain a push token.');
                return;
            }
            const res = await fetch('/api/v1/push/subscriptions', {
                method: 'POST',
                headers: {'Content-Type': 'application/json', 'Accept': 'application/json'},
                credentials: 'same-origin',
                body: JSON.stringify({token: token})
            });
            if (!res.ok) {
                alert('Subscription failed (' + res.status + ')');
                return;
            }
            localStorage.setItem('push-token', token);
            subscribed = true;
            updateLabel();
        } catch (e) {
            alert('Push subscription error: ' + e.message);
        }
    }

    async function unsubscribe() {
        const token = localStorage.getItem('push-token');
        try {
            if (messaging) {
                await messaging.deleteToken();
            }
        } catch (e) {
            // ignore
        }
        if (token) {
            try {
                await fetch('/api/v1/push/subscriptions?token=' + encodeURIComponent(token), {
                    method: 'DELETE',
                    credentials: 'same-origin'
                });
            } catch (e) {
                // ignore
            }
        }
        localStorage.removeItem('push-token');
        subscribed = false;
        updateLabel();
    }

    btn.addEventListener('click', function () {
        if (subscribed) {
            unsubscribe();
        } else {
            subscribe();
        }
    });

    // Already subscribed on a fresh page load: re-attach the foreground handler.
    if (subscribed) {
        initMessaging().catch(function () {
        });
    }
})();
