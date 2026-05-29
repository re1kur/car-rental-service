(function () {
    'use strict';

    let container;

    function ensureContainer() {
        if (!container) {
            container = document.createElement('div');
            container.className = 'toast-container';
            document.body.appendChild(container);
        }
        return container;
    }

    window.toast = function (message, type) {
        const el = document.createElement('div');
        el.className = 'toast toast-' + (type || 'info');
        el.textContent = message;
        ensureContainer().appendChild(el);
        requestAnimationFrame(function () {
            el.classList.add('show');
        });
        setTimeout(function () {
            el.classList.remove('show');
            setTimeout(function () {
                el.remove();
            }, 300);
        }, 3000);
    };

    window.updateCartBadge = function (count) {
        const badge = document.querySelector('.cart-badge');
        if (!badge) {
            return;
        }
        if (count > 0) {
            badge.textContent = count;
            badge.classList.remove('hidden');
        } else {
            badge.textContent = '';
            badge.classList.add('hidden');
        }
    };

    window.readApiError = async function (response) {
        try {
            const data = await response.json();
            return data.message || ('Request error (' + response.status + ')');
        } catch (e) {
            return 'Request error (' + response.status + ')';
        }
    };
})();
