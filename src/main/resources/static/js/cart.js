(function () {
    'use strict';

    const JSON_HEADERS = {'Content-Type': 'application/json', 'Accept': 'application/json'};

    document.addEventListener('click', async function (e) {
        const btn = e.target.closest('.add-cart-btn');
        if (!btn) {
            return;
        }
        e.preventDefault();
        const carId = Number(btn.dataset.carId);
        btn.disabled = true;
        try {
            const res = await fetch('/api/v1/cart/items', {
                method: 'POST',
                headers: JSON_HEADERS,
                credentials: 'same-origin',
                body: JSON.stringify({carId: carId, quantity: 1})
            });
            if (res.status === 401) {
                window.toast('Log in to add cars to the cart.', 'error');
                return;
            }
            if (!res.ok) {
                window.toast(await window.readApiError(res), 'error');
                return;
            }
            const cart = await res.json();
            window.updateCartBadge(cart.itemCount);
            window.toast('Added to cart.', 'success');
        } catch (err) {
            window.toast('Network error.', 'error');
        } finally {
            btn.disabled = false;
        }
    });

    const cartRoot = document.querySelector('[data-cart-root]');
    if (!cartRoot) {
        return;
    }

    function applyCart(cart) {
        const byId = {};
        cart.items.forEach(function (i) {
            byId[i.carId] = i;
        });

        cartRoot.querySelectorAll('.cart-item').forEach(function (line) {
            const id = Number(line.dataset.carId);
            const item = byId[id];
            if (!item) {
                line.remove();
                return;
            }
            const qty = line.querySelector('[data-qty]');
            if (qty) {
                qty.textContent = item.quantity;
            }
            const subtotal = line.querySelector('[data-subtotal]');
            if (subtotal) {
                subtotal.textContent = '$' + item.subtotal;
            }
            const start = line.querySelector('input[name="startDate"]');
            const end = line.querySelector('input[name="endDate"]');
            if (start && item.startDate) {
                start.value = item.startDate;
            }
            if (end && item.endDate) {
                end.value = item.endDate;
            }
        });

        const total = document.querySelector('[data-total]');
        if (total) {
            total.textContent = '$' + cart.total;
        }
        window.updateCartBadge(cart.itemCount);

        if (cart.itemCount === 0) {
            window.location.reload();
        }
    }

    async function mutate(request) {
        try {
            const res = await request;
            if (!res.ok) {
                window.toast(await window.readApiError(res), 'error');
                return;
            }
            applyCart(await res.json());
        } catch (e) {
            window.toast('Network error.', 'error');
        }
    }

    cartRoot.addEventListener('click', function (e) {
        const stepBtn = e.target.closest('[data-step]');
        if (stepBtn) {
            const line = stepBtn.closest('.cart-item');
            const carId = Number(line.dataset.carId);
            const current = Number(line.querySelector('[data-qty]').textContent);
            let next = current + Number(stepBtn.dataset.step);
            if (next < 1) {
                next = 1;
            }
            if (next > 365) {
                next = 365;
            }
            if (next === current) {
                return;
            }
            mutate(fetch('/api/v1/cart/items/' + carId, {
                method: 'PUT',
                headers: JSON_HEADERS,
                credentials: 'same-origin',
                body: JSON.stringify({quantity: next})
            }));
            return;
        }

        const removeBtn = e.target.closest('[data-remove]');
        if (removeBtn) {
            const line = removeBtn.closest('.cart-item');
            const carId = Number(line.dataset.carId);
            if (!window.confirm('Remove this car from the cart?')) {
                return;
            }
            mutate(fetch('/api/v1/cart/items/' + carId, {
                method: 'DELETE',
                headers: {'Accept': 'application/json'},
                credentials: 'same-origin'
            }));
        }
    });

    cartRoot.addEventListener('change', function (e) {
        const input = e.target;
        if (input.name !== 'startDate' && input.name !== 'endDate') {
            return;
        }
        const line = input.closest('.cart-item');
        const carId = Number(line.dataset.carId);
        const start = line.querySelector('input[name="startDate"]').value;
        const end = line.querySelector('input[name="endDate"]').value;
        if (!start || !end) {
            return;
        }
        mutate(fetch('/api/v1/cart/items/' + carId, {
            method: 'PUT',
            headers: JSON_HEADERS,
            credentials: 'same-origin',
            body: JSON.stringify({startDate: start, endDate: end})
        }));
    });
})();
