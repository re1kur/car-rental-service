(function () {
    'use strict';

    const form = document.querySelector('[data-rent-form]');
    if (!form) {
        return;
    }

    const price = Number(form.dataset.dayPrice);
    const start = form.querySelector('input[name="startDate"]');
    const end = form.querySelector('input[name="endDate"]');
    const out = form.querySelector('[data-rent-total]');
    if (!start || !end || !out) {
        return;
    }

    function recalc() {
        if (!start.value || !end.value) {
            out.textContent = '';
            return;
        }
        const s = new Date(start.value);
        const e = new Date(end.value);
        const days = Math.floor((e - s) / 86400000) + 1;
        if (days < 1) {
            out.textContent = 'End date must be on or after start date.';
            return;
        }
        out.textContent = days + ' day(s) × $' + price + ' = $' + (days * price);
    }

    start.addEventListener('change', recalc);
    end.addEventListener('change', recalc);
    recalc();
})();
