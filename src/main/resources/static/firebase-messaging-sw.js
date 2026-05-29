importScripts('https://www.gstatic.com/firebasejs/10.12.2/firebase-app-compat.js');
importScripts('https://www.gstatic.com/firebasejs/10.12.2/firebase-messaging-compat.js');
importScripts('/push-config.js');

if (self.__PUSH_CONFIG__ && self.__PUSH_CONFIG__.firebase && self.__PUSH_CONFIG__.firebase.apiKey) {
    firebase.initializeApp(self.__PUSH_CONFIG__.firebase);
    const messaging = firebase.messaging();

    messaging.onBackgroundMessage(function (payload) {
        const d = payload.data || {};
        self.registration.showNotification(d.title || 'RentCar', {
            body: d.body || '',
            icon: '/img/car-placeholder.svg',
            data: {url: d.url || '/'}
        });
    });
}

self.addEventListener('notificationclick', function (event) {
    event.notification.close();
    const url = (event.notification.data && event.notification.data.url) || '/';
    event.waitUntil(
        self.clients.matchAll({type: 'window', includeUncontrolled: true}).then(function (windows) {
            for (const client of windows) {
                if ('focus' in client) {
                    client.focus();
                    if ('navigate' in client) {
                        client.navigate(url);
                    }
                    return;
                }
            }
            if (self.clients.openWindow) {
                return self.clients.openWindow(url);
            }
        })
    );
});
