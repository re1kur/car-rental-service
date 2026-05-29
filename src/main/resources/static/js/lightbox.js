(function () {
    'use strict';

    let overlay;

    function build() {
        overlay = document.createElement('div');
        overlay.className = 'lb-overlay';
        overlay.innerHTML = '<button class="lb-close" type="button" aria-label="Close">&times;</button>' +
            '<img class="lb-img" alt="">';
        document.body.appendChild(overlay);
        overlay.addEventListener('click', close);
        document.addEventListener('keydown', function (e) {
            if (e.key === 'Escape') {
                close();
            }
        });
    }

    function close() {
        if (overlay) {
            overlay.classList.remove('open');
        }
    }

    document.addEventListener('click', function (e) {
        const img = e.target.closest('.zoomable');
        if (!img) {
            return;
        }
        if (!overlay) {
            build();
        }
        overlay.querySelector('.lb-img').src = img.dataset.full || img.src;
        overlay.classList.add('open');
    });
})();
