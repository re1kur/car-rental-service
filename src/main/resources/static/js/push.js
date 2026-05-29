(function () {
    'use strict';

    const btn = document.querySelector('[data-push-toggle]');
    if (!btn) {
        return;
    }

    const FB_VERSION = '10.12.2';
    const cfg = window.__PUSH_CONFIG__;
    let messaging = null;

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

    async function initMessaging() {
        await ensureFirebase();
        if (!window.firebase.apps.length) {
            window.firebase.initializeApp(cfg.firebase);
        }
        messaging = window.firebase.messaging();
        messaging.onMessage(function (payload) {
            const d = payload.data || {};
            if ('Notification' in window && Notification.permission === 'granted') {
                new Notification(d.title || 'RentCar', {body: d.body || '', icon: '/img/car-placeholder.svg'});
            }
        });
    }

    async function subscribe() {
        try {
            const permission = await Notification.requestPermission();
            if (permission !== 'granted') {
                alert('Notification permission denied.');
                return;
            }
            await initMessaging();
            const token = await messaging.getToken({vapidKey: cfg.vapidKey});
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
})();
