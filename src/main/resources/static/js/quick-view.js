(function () {
    'use strict';

    let overlay;

    function esc(value) {
        if (value == null) {
            return '';
        }
        const div = document.createElement('div');
        div.textContent = String(value);
        return div.innerHTML;
    }

    function buildModal() {
        overlay = document.createElement('div');
        overlay.className = 'qv-overlay';
        overlay.innerHTML =
            '<div class="qv-modal" role="dialog" aria-modal="true">' +
            '<button class="qv-close" type="button" aria-label="Close">&times;</button>' +
            '<div class="qv-body"></div>' +
            '</div>';
        document.body.appendChild(overlay);

        overlay.addEventListener('click', function (e) {
            if (e.target === overlay || e.target.closest('.qv-close')) {
                close();
            }
        });
        document.addEventListener('keydown', function (e) {
            if (e.key === 'Escape') {
                close();
            }
        });
    }

    function open() {
        overlay.classList.add('open');
    }

    function close() {
        if (overlay) {
            overlay.classList.remove('open');
        }
    }

    function row(label, value) {
        return value ? '<li><span class="k">' + label + '</span><span class="v">' + esc(value) + '</span></li>' : '';
    }

    function render(car) {
        const img = car.imageUrl || '/img/car-placeholder.svg';
        const price = car.pricePerDay != null ? '$' + car.pricePerDay : '—';
        overlay.querySelector('.qv-body').innerHTML =
            '<img class="qv-img" src="' + esc(img) + '" alt="">' +
            '<h2>' + esc(car.make) + ' ' + esc(car.model) + '</h2>' +
            '<div class="qv-price">' + price + ' / day</div>' +
            '<ul class="spec-list">' +
            row('Type', car.carType && car.carType.label) +
            row('Engine', car.engine && car.engine.label) +
            row('Year', car.year) +
            row('Color', car.color) +
            row('Seats', car.seats) +
            row('Transmission', car.transmission) +
            '</ul>' +
            '<div class="qv-actions">' +
            '<a class="btn" href="/cars/' + car.id + '">View full details</a>' +
            '</div>';
    }

    document.addEventListener('click', async function (e) {
        if (e.target.closest('.add-cart-btn, [data-toggle-availability], a')) {
            return;
        }
        const btn = e.target.closest('.quick-view-btn');
        if (!btn) {
            return;
        }
        e.preventDefault();
        if (!overlay) {
            buildModal();
        }
        const id = Number(btn.dataset.carId);
        const body = overlay.querySelector('.qv-body');
        body.innerHTML = '<p class="empty">Loading…</p>';
        open();
        try {
            const res = await fetch('/api/v1/cars/' + id, {headers: {'Accept': 'application/json'}});
            if (!res.ok) {
                body.innerHTML = '<p class="empty">Failed to load car.</p>';
                return;
            }
            render(await res.json());
        } catch (err) {
            body.innerHTML = '<p class="empty">Network error.</p>';
        }
    });
})();
