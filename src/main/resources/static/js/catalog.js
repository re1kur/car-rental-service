(function () {
    'use strict';

    const form = document.querySelector('[data-filter-form]');
    const grid = document.querySelector('[data-car-grid]');
    if (!form || !grid) {
        return;
    }

    const pagination = document.querySelector('[data-pagination]');
    const emptyMsg = document.querySelector('[data-empty]');
    const authed = !!document.querySelector('[data-authenticated]');
    const admin = !!document.querySelector('[data-admin]');

    function esc(v) {
        if (v == null) {
            return '';
        }
        const d = document.createElement('div');
        d.textContent = String(v);
        return d.innerHTML;
    }

    function cardHtml(car) {
        const img = car.imageUrl || '/img/car-placeholder.svg';
        const title = esc(car.make) + ' ' + esc(car.model);
        const price = car.pricePerDay != null ? '<span class="card-price">$' + car.pricePerDay + '/day</span>' : '';
        const fab = authed ? '<button type="button" class="btn add-cart-btn add-fab" data-car-id="' + car.id + '" title="Add to cart" aria-label="Add to cart">+</button>' : '';
        const badge = car.available ? '' : '<div class="card-meta"><span class="badge no" data-availability-badge>Not available</span></div>';
        const adminBtn = admin ? '<button type="button" class="btn btn-outline btn-small" data-toggle-availability data-car-id="' + car.id + '" data-available="' + car.available + '">' + (car.available ? 'Set unavailable' : 'Set available') + '</button>' : '';
        return '<div class="card" data-car>' +
            '<div class="card-media quick-view-btn" data-car-id="' + car.id + '" title="Quick view">' +
            '<img src="' + esc(img) + '" alt="' + title + '">' + price +
            '<span class="qv-hint">Quick view</span>' + fab +
            '</div>' +
            '<div class="card-body">' +
            '<h3 class="card-title"><a href="/cars/' + car.id + '">' + title + '</a></h3>' +
            badge +
            '<div class="card-meta">' +
            '<span class="tag">' + esc(car.carType && car.carType.label) + '</span>' +
            '<span class="tag">' + esc(car.year) + '</span>' +
            '<span class="tag">' + esc(car.engine && car.engine.label) + '</span>' +
            '</div>' +
            '<div class="card-actions">' +
            '<a class="btn" href="/cars/' + car.id + '">View details</a>' +
            adminBtn +
            '</div>' +
            '</div></div>';
    }

    async function apply() {
        const fd = new FormData(form);
        const params = new URLSearchParams();
        if (fd.get('model')) params.set('model', fd.get('model'));
        if (fd.get('makeId')) params.set('makeId', fd.get('makeId'));
        if (fd.get('carType')) params.set('type', fd.get('carType'));
        if (fd.get('engine')) params.set('engine', fd.get('engine'));
        if (fd.get('year')) params.set('year', fd.get('year'));
        if (fd.get('available')) params.set('available', fd.get('available'));

        try {
            const res = await fetch('/api/v1/cars?' + params.toString(), {
                headers: {'Accept': 'application/json'},
                credentials: 'same-origin'
            });
            if (!res.ok) {
                window.toast(await window.readApiError(res), 'error');
                return;
            }
            const cars = await res.json();
            if (pagination) {
                pagination.style.display = 'none';
            }
            if (cars.length === 0) {
                grid.innerHTML = '';
                if (emptyMsg) {
                    emptyMsg.style.display = '';
                }
                return;
            }
            if (emptyMsg) {
                emptyMsg.style.display = 'none';
            }
            grid.innerHTML = cars.map(cardHtml).join('');
        } catch (e) {
            window.toast('Network error.', 'error');
        }
    }

    form.addEventListener('submit', function (e) {
        e.preventDefault();
        apply();
    });
    form.querySelectorAll('select').forEach(function (s) {
        s.addEventListener('change', apply);
    });
})();
