(function () {
    'use strict';

    const ROOMS = [
        {id: 'general', label: 'General'},
        {id: 'support', label: 'Support'},
        {id: 'cars', label: 'Cars'},
        {id: 'random', label: 'Random'}
    ];
    const label = (id) => (ROOMS.find(r => r.id === id) || {label: id}).label;

    const messagesEl = document.querySelector('[data-messages]');
    const onlineEl = document.querySelector('[data-online]');
    const onlineCountEl = document.querySelector('[data-online-count]');
    const roomsEl = document.querySelector('[data-rooms]');
    const typingEl = document.querySelector('[data-typing]');
    const statusEl = document.querySelector('[data-conn-status]');
    const meNameEl = document.querySelector('[data-me-name]');
    const roomTitleEl = document.querySelector('[data-room-title]');
    const roomCountEl = document.querySelector('[data-room-count]');
    const form = document.querySelector('[data-send-form]');
    const input = document.querySelector('[data-input]');

    let me = null;
    let currentRoom = 'general';
    let lastStatus = null;
    const unread = {};
    const counts = {};
    const typingUsers = new Map();
    let typingSent = false;
    let typingTimer = null;

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

    function renderRooms() {
        roomsEl.innerHTML = '';
        ROOMS.forEach(function (room) {
            const li = document.createElement('li');
            li.className = 'chat-room' + (room.id === currentRoom ? ' active' : '');
            const unreadBadge = unread[room.id]
                ? '<span class="chat-unread">' + unread[room.id] + '</span>' : '';
            li.innerHTML = '<span class="chat-room-name">' + esc(room.label) + '</span>' +
                '<span class="chat-room-meta">' + unreadBadge +
                '<span class="chat-room-count" title="online">' + (counts[room.id] || 0) + '</span></span>';
            li.addEventListener('click', function () {
                switchRoom(room.id);
            });
            roomsEl.appendChild(li);
        });
    }

    function renderHeader() {
        roomTitleEl.textContent = label(currentRoom);
        roomCountEl.textContent = (counts[currentRoom] || 0) + ' online';
    }

    function switchRoom(roomId) {
        if (roomId === currentRoom) {
            return;
        }
        socket.send('leave_room', {room: currentRoom});
        currentRoom = roomId;
        unread[roomId] = 0;
        messagesEl.innerHTML = '';
        onlineEl.innerHTML = '';
        onlineCountEl.textContent = '0';
        typingUsers.clear();
        renderTyping();
        renderRooms();
        renderHeader();
        socket.send('join_room', {room: currentRoom});
    }

    function nearBottom() {
        return messagesEl.scrollHeight - messagesEl.scrollTop - messagesEl.clientHeight < 60;
    }

    function addMessage(msg, forceScroll) {
        const stick = forceScroll || nearBottom();
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
        if (stick) {
            messagesEl.scrollTop = messagesEl.scrollHeight;
        }
    }

    function addSystem(text) {
        const stick = nearBottom();
        const el = document.createElement('div');
        el.className = 'msg-system';
        el.textContent = text;
        messagesEl.appendChild(el);
        if (stick) {
            messagesEl.scrollTop = messagesEl.scrollHeight;
        }
    }

    function renderOnline(users) {
        onlineCountEl.textContent = users.length;
        counts[currentRoom] = users.length;
        renderHeader();
        onlineEl.innerHTML = '';
        users.forEach(function (u) {
            const li = document.createElement('li');
            li.innerHTML = '<span class="online-dot"></span>' +
                '<span class="msg-avatar">' + esc(initials(u.displayName)) + '</span>' +
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
                meNameEl.textContent = me.guest
                    ? 'You are a guest (' + me.displayName + ')'
                    : 'Signed in as ' + me.displayName;
                socket.send('join_room', {room: currentRoom});
                break;
            case 'room_counts':
                Object.assign(counts, data.counts);
                renderRooms();
                renderHeader();
                break;
            case 'room_activity':
                if (data.room !== currentRoom) {
                    unread[data.room] = (unread[data.room] || 0) + 1;
                    renderRooms();
                }
                break;
            case 'history':
                if (data.room === currentRoom) {
                    messagesEl.innerHTML = '';
                    data.messages.forEach(function (m) {
                        addMessage(m, true);
                    });
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

    function onStatus(status) {
        if (status === 'online') {
            statusEl.textContent = 'online';
            statusEl.className = 'chat-status online';
            if (lastStatus === 'offline') {
                window.toast('Reconnected.', 'success');
            }
        } else {
            statusEl.textContent = 'reconnecting…';
            statusEl.className = 'chat-status offline';
            if (lastStatus === 'online') {
                window.toast('Connection lost. Reconnecting…', 'error');
            }
        }
        lastStatus = status;
    }

    function stopTyping() {
        clearTimeout(typingTimer);
        if (typingSent) {
            typingSent = false;
            socket.send('typing', {room: currentRoom, isTyping: false});
        }
    }

    form.addEventListener('submit', function (e) {
        e.preventDefault();
        const text = input.value.trim();
        if (!text) {
            return;
        }
        socket.send('send_message', {room: currentRoom, text: text});
        input.value = '';
        stopTyping();
    });

    input.addEventListener('input', function () {
        if (!typingSent) {
            typingSent = true;
            socket.send('typing', {room: currentRoom, isTyping: true});
        }
        clearTimeout(typingTimer);
        typingTimer = setTimeout(stopTyping, 1500);
    });

    renderRooms();
    renderHeader();
    const socket = window.ChatSocket('/ws/chat', {onEvent: handle, onStatus: onStatus});
})();
