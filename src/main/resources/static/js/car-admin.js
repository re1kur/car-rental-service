(function () {
    'use strict';

    document.addEventListener('click', async function (e) {
        const btn = e.target.closest('[data-toggle-availability]');
        if (!btn) {
            return;
        }
        const id = Number(btn.dataset.carId);
        const next = btn.dataset.available !== 'true';
        btn.disabled = true;
        try {
            const res = await fetch('/api/v1/cars/' + id + '/availability', {
                method: 'PATCH',
                headers: {'Content-Type': 'application/json', 'Accept': 'application/json'},
                credentials: 'same-origin',
                body: JSON.stringify({available: next})
            });
            if (!res.ok) {
                window.toast(await window.readApiError(res), 'error');
                return;
            }
            const car = await res.json();
            btn.dataset.available = String(car.available);
            btn.textContent = car.available ? 'Set unavailable' : 'Set available';

            const scope = btn.closest('.card') || document;
            const badge = scope.querySelector('[data-availability-badge]');
            if (badge) {
                badge.textContent = car.available ? 'Available' : 'Not available';
                badge.className = 'badge ' + (car.available ? 'ok' : 'no');
                badge.setAttribute('data-availability-badge', '');
            }
            window.toast('Now ' + (car.available ? 'available' : 'unavailable') + '.', 'success');
        } catch (err) {
            window.toast('Network error.', 'error');
        } finally {
            btn.disabled = false;
        }
    });
})();
