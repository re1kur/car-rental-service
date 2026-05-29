(function () {
    'use strict';

    const ROOMS = [
        {id: 'general', label: 'General'},
        {id: 'support', label: 'Support'},
        {id: 'cars', label: 'Cars'},
        {id: 'random', label: 'Random'}
    ];

    const messagesEl = document.querySelector('[data-messages]');
    const onlineEl = document.querySelector('[data-online]');
    const onlineCountEl = document.querySelector('[data-online-count]');
    const roomsEl = document.querySelector('[data-rooms]');
    const typingEl = document.querySelector('[data-typing]');
    const statusEl = document.querySelector('[data-conn-status]');
    const meNameEl = document.querySelector('[data-me-name]');
    const form = document.querySelector('[data-send-form]');
    const input = document.querySelector('[data-input]');

    let ws = null;
    let me = null;
    let currentRoom = 'general';
    let reconnectDelay = 1000;
    let typingSent = false;
    let typingTimer = null;
    const typingUsers = new Map();

    function esc(v) {
        if (v == null) {
            return '';
        }
        const d = document.createElement('div');
        d.textContent = String(v);
        return d.innerHTML;
    }

    function initials(name) {
        const parts = String(name || '?').trim().split(/\s+/);
        return (parts.length > 1 ? parts[0][0] + parts[1][0] : parts[0].slice(0, 2)).toUpperCase();
    }

    function time(iso) {
        const d = new Date(iso);
        return isNaN(d) ? '' : d.toLocaleTimeString([], {hour: '2-digit', minute: '2-digit'});
    }

    function send(type, data) {
        if (ws && ws.readyState === WebSocket.OPEN) {
            ws.send(JSON.stringify({type: type, data: data}));
        }
    }

    function renderRooms() {
        roomsEl.innerHTML = '';
        ROOMS.forEach(function (room) {
            const li = document.createElement('li');
            li.textContent = room.label;
            li.className = 'chat-room' + (room.id === currentRoom ? ' active' : '');
            li.addEventListener('click', function () {
                switchRoom(room.id);
            });
            roomsEl.appendChild(li);
        });
    }

    function switchRoom(roomId) {
        if (roomId === currentRoom) {
            return;
        }
        send('leave_room', {room: currentRoom});
        currentRoom = roomId;
        messagesEl.innerHTML = '';
        onlineEl.innerHTML = '';
        onlineCountEl.textContent = '0';
        typingUsers.clear();
        renderTyping();
        renderRooms();
        send('join_room', {room: currentRoom});
    }

    function addMessage(msg) {
        const mine = me && msg.sender && msg.sender.userId === me.userId;
        const el = document.createElement('div');
        el.className = 'msg' + (mine ? ' mine' : '');
        el.innerHTML =
            '<span class="msg-avatar">' + esc(initials(msg.sender.displayName)) + '</span>' +
            '<div class="msg-body">' +
            '<div class="msg-head"><span class="msg-author">' + esc(msg.sender.displayName) +
            (msg.sender.guest ? ' <span class="msg-guest">guest</span>' : '') +
            '</span><span class="msg-time">' + time(msg.timestamp) + '</span></div>' +
            '<div class="msg-text">' + esc(msg.text) + '</div>' +
            '</div>';
        messagesEl.appendChild(el);
        messagesEl.scrollTop = messagesEl.scrollHeight;
    }

    function addSystem(text) {
        const el = document.createElement('div');
        el.className = 'msg-system';
        el.textContent = text;
        messagesEl.appendChild(el);
        messagesEl.scrollTop = messagesEl.scrollHeight;
    }

    function renderOnline(users) {
        onlineCountEl.textContent = users.length;
        onlineEl.innerHTML = '';
        users.forEach(function (u) {
            const li = document.createElement('li');
            li.innerHTML = '<span class="msg-avatar">' + esc(initials(u.displayName)) + '</span>' +
                '<span>' + esc(u.displayName) + (u.guest ? ' <span class="msg-guest">guest</span>' : '') + '</span>';
            onlineEl.appendChild(li);
        });
    }

    function renderTyping() {
        const names = Array.from(typingUsers.values());
        if (names.length === 0) {
            typingEl.textContent = '';
        } else if (names.length === 1) {
            typingEl.textContent = names[0] + ' is typing…';
        } else {
            typingEl.textContent = names.slice(0, 2).join(', ') + ' are typing…';
        }
    }

    function handle(event) {
        const data = event.data;
        switch (event.type) {
            case 'connected':
                me = data.user;
                meNameEl.textContent = me.guest ? 'You are a guest (' + me.displayName + ')' : 'Signed in as ' + me.displayName;
                send('join_room', {room: currentRoom});
                break;
            case 'history':
                if (data.room === currentRoom) {
                    messagesEl.innerHTML = '';
                    data.messages.forEach(addMessage);
                }
                break;
            case 'message':
                if (data.room === currentRoom) {
                    addMessage(data);
                }
                break;
            case 'user_joined':
                if (data.room === currentRoom) {
                    addSystem(data.user.displayName + ' joined');
                }
                break;
            case 'user_left':
                if (data.room === currentRoom) {
                    addSystem(data.user.displayName + ' left');
                    typingUsers.delete(data.user.userId);
                    renderTyping();
                }
                break;
            case 'typing_status':
                if (data.room === currentRoom) {
                    if (data.isTyping) {
                        typingUsers.set(data.user.userId, data.user.displayName);
                    } else {
                        typingUsers.delete(data.user.userId);
                    }
                    renderTyping();
                }
                break;
            case 'online_users':
                if (data.room === currentRoom) {
                    renderOnline(data.users);
                }
                break;
            case 'error':
                window.toast(data.message || 'Chat error', 'error');
                break;
            default:
                break;
        }
    }

    function connect() {
        const proto = location.protocol === 'https:' ? 'wss' : 'ws';
        ws = new WebSocket(proto + '://' + location.host + '/ws/chat');

        ws.onopen = function () {
            reconnectDelay = 1000;
            statusEl.textContent = 'online';
            statusEl.className = 'chat-status online';
        };

        ws.onmessage = function (frame) {
            let event;
            try {
                event = JSON.parse(frame.data);
            } catch (e) {
                return;
            }
            handle(event);
        };

        ws.onclose = function () {
            statusEl.textContent = 'reconnecting…';
            statusEl.className = 'chat-status offline';
            setTimeout(connect, reconnectDelay);
            reconnectDelay = Math.min(reconnectDelay * 2, 10000);
        };

        ws.onerror = function () {
            ws.close();
        };
    }

    form.addEventListener('submit', function (e) {
        e.preventDefault();
        const text = input.value.trim();
        if (!text) {
            return;
        }
        send('send_message', {room: currentRoom, text: text});
        input.value = '';
        stopTyping();
    });

    input.addEventListener('input', function () {
        if (!typingSent) {
            typingSent = true;
            send('typing', {room: currentRoom, isTyping: true});
        }
        clearTimeout(typingTimer);
        typingTimer = setTimeout(stopTyping, 1500);
    });

    function stopTyping() {
        clearTimeout(typingTimer);
        if (typingSent) {
            typingSent = false;
            send('typing', {room: currentRoom, isTyping: false});
        }
    }

    renderRooms();
    connect();
})();
