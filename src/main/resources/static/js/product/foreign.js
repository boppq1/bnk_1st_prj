

document.addEventListener("DOMContentLoaded", function () {

    // ── 초기화: HTML에 하드코딩된 active 클래스 전부 제거 ──
    document.querySelectorAll(".foreign-dropdown-menu").forEach(m => m.classList.remove("active"));
    document.querySelectorAll(".foreign-sub-menu").forEach(m => m.classList.remove("active"));
    document.querySelectorAll(".foreign-sub-title").forEach(t => t.classList.remove("active"));
    document.querySelectorAll(".foreign-dropdown-btn").forEach(b => b.classList.remove("active"));

    // ── 현재 URL에 해당하는 카테고리 자동 펼침 ──
    const currentPath = window.location.pathname;

    document.querySelectorAll(".foreign-dropdown-menu a").forEach(function (link) {
        const linkPath = new URL(link.href, location.origin).pathname;

        if (currentPath === linkPath || currentPath.startsWith(linkPath)) {
            link.classList.add("active");

            const subMenu = link.closest(".foreign-sub-menu");
            if (subMenu) {
                subMenu.classList.add("active");

                const subTitle = subMenu.previousElementSibling;
                if (subTitle && subTitle.classList.contains("foreign-sub-title")) {
                    subTitle.classList.add("active");
                }
            }

            const dropdownMenu = link.closest(".foreign-dropdown-menu");
            if (dropdownMenu) {
                dropdownMenu.classList.add("active");

                const dropdownBtn = dropdownMenu.closest(".foreign-dropdown")
                    .querySelector(".foreign-dropdown-btn");
                if (dropdownBtn) {
                    dropdownBtn.classList.add("active");
                }
            }
        }
    });

    // ── 드롭다운 버튼 클릭 (아코디언) ──
    const dropdownBtns = document.querySelectorAll(".foreign-dropdown-btn");

    dropdownBtns.forEach(function (btn) {

        btn.addEventListener("click", function () {

            const clickedDropdown = btn.closest(".foreign-dropdown");
            const clickedMenu = clickedDropdown.querySelector(".foreign-dropdown-menu");
            const isAlreadyOpen = btn.classList.contains("active");

            dropdownBtns.forEach(function (otherBtn) {
                const otherDropdown = otherBtn.closest(".foreign-dropdown");
                const otherMenu = otherDropdown.querySelector(".foreign-dropdown-menu");
                otherBtn.classList.remove("active");
                otherMenu.classList.remove("active");
                otherDropdown.querySelectorAll(".foreign-sub-title").forEach(t => t.classList.remove("active"));
                otherDropdown.querySelectorAll(".foreign-sub-menu").forEach(m => m.classList.remove("active"));
            });

            if (!isAlreadyOpen) {
                btn.classList.add("active");
                clickedMenu.classList.add("active");
            }

        });

    });

    // ── 서브타이틀 클릭 (아코디언) ──
    const subTitles = document.querySelectorAll(".foreign-sub-title");

    subTitles.forEach(function (title) {

        title.addEventListener("click", function () {

            const parentGroup = title.closest(".foreign-dropdown-menu");
            const subMenu = title.nextElementSibling;
            const isAlreadyOpen = title.classList.contains("active");

            parentGroup.querySelectorAll(".foreign-sub-title").forEach(t => t.classList.remove("active"));
            parentGroup.querySelectorAll(".foreign-sub-menu").forEach(m => m.classList.remove("active"));

            if (!isAlreadyOpen && subMenu) {
                title.classList.add("active");
                subMenu.classList.add("active");
            }

        });

    });

});
