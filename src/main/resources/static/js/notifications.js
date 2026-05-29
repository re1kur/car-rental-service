(function () {
    'use strict';

    const bell = document.querySelector('[data-notif-bell]');
    if (!bell) {
        return;
    }
    const badge = document.querySelector('[data-notif-badge]');
    const panel = document.querySelector('[data-notif-panel]');
    const listEl = document.querySelector('[data-notif-list]');
    const toggleBtn = document.querySelector('[data-notif-toggle]');
    const adminForm = document.querySelector('[data-notif-form]');

    const LS_SUB = 'notif-subscribed';
    const LS_LOG = 'notif-log';

    let subscribed = localStorage.getItem(LS_SUB) === '1';
    let unread = 0;
    let ws = null;
    let reconnect = 1000;
    let swReg = null;

    function esc(v) {
        const d = document.createElement('div');
        d.textContent = v == null ? '' : String(v);
        return d.innerHTML;
    }

    function fmt(iso) {
        const d = new Date(iso);
        return isNaN(d) ? '' : d.toLocaleString([], {hour: '2-digit', minute: '2-digit'});
    }

    function loadLog() {
        try {
            return JSON.parse(localStorage.getItem(LS_LOG) || '[]');
        } catch (e) {
            return [];
        }
    }

    function saveLog(log) {
        localStorage.setItem(LS_LOG, JSON.stringify(log.slice(-20)));
    }

    function renderList() {
        const log = loadLog().slice().reverse();
        if (!log.length) {
            listEl.innerHTML = '<li class="notif-empty">No notifications yet.</li>';
            return;
        }
        listEl.innerHTML = log.map(function (n) {
            return '<li class="notif-item">' +
                '<div class="notif-title">' + esc(n.title) + '</div>' +
                '<div class="notif-body">' + esc(n.body) + '</div>' +
                '<div class="notif-time">' + fmt(n.timestamp) + '</div></li>';
        }).join('');
    }

    function setBadge() {
        if (unread > 0) {
            badge.textContent = unread;
            badge.classList.remove('hidden');
        } else {
            badge.textContent = '';
            badge.classList.add('hidden');
        }
    }

    function updateToggle() {
        toggleBtn.textContent = subscribed ? 'Unsubscribe' : 'Subscribe';
        toggleBtn.classList.toggle('on', subscribed);
    }

    function showOs(n) {
        if (!('Notification' in window) || Notification.permission !== 'granted') {
            return;
        }
        const opts = {
            body: n.body,
            icon: '/img/car-placeholder.svg',
            tag: 'rentcar-' + (n.timestamp || ''),
            data: {url: n.url || '/'}
        };
        try {
            if (swReg && swReg.showNotification) {
                swReg.showNotification(n.title, opts);
            } else {
                new Notification(n.title, opts);
            }
        } catch (e) {
            // ignore rendering errors
        }
    }

    function onNotification(n) {
        const log = loadLog();
        log.push(n);
        saveLog(log);
        renderList();
        if (panel.hidden) {
            unread++;
            setBadge();
        }
        showOs(n);
    }

    function send(type) {
        if (ws && ws.readyState === WebSocket.OPEN) {
            ws.send(JSON.stringify({type: type}));
        }
    }

    function connect() {
        const proto = location.protocol === 'https:' ? 'wss' : 'ws';
        ws = new WebSocket(proto + '://' + location.host + '/ws/notifications');
        ws.onopen = function () {
            reconnect = 1000;
            if (subscribed) {
                send('subscribe');
            }
        };
        ws.onmessage = function (frame) {
            let ev;
            try {
                ev = JSON.parse(frame.data);
            } catch (e) {
                return;
            }
            if (ev.type === 'notification') {
                onNotification(ev.data);
            }
        };
        ws.onclose = function () {
            setTimeout(connect, reconnect);
            reconnect = Math.min(reconnect * 2, 10000);
        };
        ws.onerror = function () {
            ws.close();
        };
    }

    async function subscribe() {
        if ('Notification' in window && Notification.permission === 'default') {
            try {
                await Notification.requestPermission();
            } catch (e) {
                // ignore
            }
        }
        subscribed = true;
        localStorage.setItem(LS_SUB, '1');
        updateToggle();
        send('subscribe');
    }

    function unsubscribe() {
        subscribed = false;
        localStorage.removeItem(LS_SUB);
        updateToggle();
        send('unsubscribe');
    }

    bell.addEventListener('click', function (e) {
        e.preventDefault();
        panel.hidden = !panel.hidden;
        if (!panel.hidden) {
            unread = 0;
            setBadge();
            renderList();
        }
    });

    document.addEventListener('click', function (e) {
        if (!panel.hidden && !e.target.closest('[data-notif-panel]') && !e.target.closest('[data-notif-bell]')) {
            panel.hidden = true;
        }
    });

    toggleBtn.addEventListener('click', function () {
        if (subscribed) {
            unsubscribe();
        } else {
            subscribe();
        }
    });

    if (adminForm) {
        adminForm.addEventListener('submit', async function (e) {
            e.preventDefault();
            const fd = new FormData(adminForm);
            try {
                const res = await fetch('/api/v1/notifications', {
                    method: 'POST',
                    headers: {'Content-Type': 'application/json', 'Accept': 'application/json'},
                    credentials: 'same-origin',
                    body: JSON.stringify({title: fd.get('title'), body: fd.get('body')})
                });
                if (!res.ok) {
                    alert('Broadcast failed (' + res.status + ')');
                    return;
                }
                const data = await res.json();
                adminForm.reset();
                alert('Sent to ' + data.sent + ' subscriber(s).');
            } catch (err) {
                alert('Network error.');
            }
        });
    }

    if ('serviceWorker' in navigator) {
        navigator.serviceWorker.register('/sw.js').then(function (reg) {
            swReg = reg;
        }).catch(function () {
        });
    }

    updateToggle();
    setBadge();
    connect();
})();
