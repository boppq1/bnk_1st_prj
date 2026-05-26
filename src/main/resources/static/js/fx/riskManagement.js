document.addEventListener('DOMContentLoaded', function () {
 
    const tabBtns = document.querySelectorAll('.tab-btn');
    const tabContents = document.querySelectorAll('.tab-content');
 
    tabBtns.forEach(function (btn) {
        btn.addEventListener('click', function () {
            const target = btn.getAttribute('data-tab');
 
            tabBtns.forEach(function (b) { b.classList.remove('active'); });
            tabContents.forEach(function (c) { c.classList.remove('active'); });
 
            btn.classList.add('active');
            document.getElementById(target).classList.add('active');
        });
    });
 
});