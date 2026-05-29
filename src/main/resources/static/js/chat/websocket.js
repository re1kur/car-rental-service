// Transport module: owns the WebSocket connection, reconnect with exponential backoff,
// JSON {type, data} envelope. Knows nothing about the UI — talks to it through callbacks.
window.ChatSocket = function (path, handlers) {
    'use strict';

    let ws = null;
    let reconnectDelay = 1000;

    function url() {
        const proto = location.protocol === 'https:' ? 'wss' : 'ws';
        return proto + '://' + location.host + path;
    }

    function connect() {
        ws = new WebSocket(url());

        ws.onopen = function () {
            reconnectDelay = 1000;
            if (handlers.onStatus) {
                handlers.onStatus('online');
            }
        };

        ws.onmessage = function (frame) {
            let event;
            try {
                event = JSON.parse(frame.data);
            } catch (e) {
                return;
            }
            if (handlers.onEvent) {
                handlers.onEvent(event);
            }
        };

        ws.onclose = function () {
            if (handlers.onStatus) {
                handlers.onStatus('offline');
            }
            setTimeout(connect, reconnectDelay);
            reconnectDelay = Math.min(reconnectDelay * 2, 10000);
        };

        ws.onerror = function () {
            ws.close();
        };
    }

    connect();

    return {
        send: function (type, data) {
            if (ws && ws.readyState === WebSocket.OPEN) {
                ws.send(JSON.stringify({type: type, data: data}));
            }
        }
    };
};
