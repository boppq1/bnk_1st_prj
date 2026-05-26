document.addEventListener("DOMContentLoaded", function () {

    const DROPDOWN_DURATION = 260;
    const SUBMENU_DURATION  = 200;

    // ── max-height 슬라이드 함수 ──
    function slideDown(el, duration) {
        el.style.display = "block";
        el.style.overflow = "hidden";
        el.style.maxHeight = "0";
        el.style.opacity = "0";
        el.style.transition = "max-height " + duration + "ms ease, opacity " + duration + "ms ease";

        requestAnimationFrame(function () {
            requestAnimationFrame(function () {
                el.style.maxHeight = el.scrollHeight + "px";
                el.style.opacity = "1";
            });
        });

        setTimeout(function () {
            el.style.maxHeight = "none";
            el.style.overflow = "";
            el.style.transition = "";
            el.classList.add("active");
        }, duration);
    }

    function slideUp(el, duration) {
        el.style.overflow = "hidden";
        el.style.maxHeight = el.scrollHeight + "px";
        el.style.opacity = "1";
        el.style.transition = "max-height " + duration + "ms ease, opacity " + duration + "ms ease";

        requestAnimationFrame(function () {
            requestAnimationFrame(function () {
                el.style.maxHeight = "0";
                el.style.opacity = "0";
            });
        });

        setTimeout(function () {
            el.style.display = "none";
            el.style.maxHeight = "";
            el.style.overflow = "";
            el.style.opacity = "";
            el.style.transition = "";
            el.classList.remove("active");
        }, duration);
    }

    function closeMenu(el, duration) {
        if (el.classList.contains("active") || el.style.display === "block") {
            slideUp(el, duration);
        }
    }

    function openMenu(el, duration) {
        if (!el.classList.contains("active")) {
            slideDown(el, duration);
        }
    }

    // ── 초기화 ──
    document.querySelectorAll(".foreign-dropdown-menu").forEach(function (m) {
        m.classList.remove("active");
        m.style.display = "none";
    });
    document.querySelectorAll(".foreign-sub-menu").forEach(function (m) {
        m.classList.remove("active");
        m.style.display = "none";
    });
    document.querySelectorAll(".foreign-sub-title").forEach(function (t) {
        t.classList.remove("active");
    });
    document.querySelectorAll(".foreign-dropdown-btn").forEach(function (b) {
        b.classList.remove("active");
    });

    // ── 현재 URL 기준 자동 펼침 (애니메이션 없이 즉시) ──
    const currentPath = window.location.pathname;

    document.querySelectorAll(".foreign-dropdown-menu a").forEach(function (link) {
        const linkPath = new URL(link.href, location.origin).pathname;

        if (currentPath === linkPath || currentPath.startsWith(linkPath)) {
            link.classList.add("active");

            const subMenu = link.closest(".foreign-sub-menu");
            if (subMenu) {
                subMenu.classList.add("active");
                subMenu.style.display = "block";
                subMenu.style.maxHeight = "none";

                const subTitle = subMenu.previousElementSibling;
                if (subTitle && subTitle.classList.contains("foreign-sub-title")) {
                    subTitle.classList.add("active");
                }
            }

            const dropdownMenu = link.closest(".foreign-dropdown-menu");
            if (dropdownMenu) {
                dropdownMenu.classList.add("active");
                dropdownMenu.style.display = "block";
                dropdownMenu.style.maxHeight = "none";

                const dropdownBtn = dropdownMenu.closest(".foreign-dropdown")
                    .querySelector(".foreign-dropdown-btn");
                if (dropdownBtn) {
                    dropdownBtn.classList.add("active");
                }
            }
        }
    });

    // ── 드롭다운 버튼 클릭 ──
    const dropdownBtns = document.querySelectorAll(".foreign-dropdown-btn");

    dropdownBtns.forEach(function (btn) {
        btn.addEventListener("click", function () {

            const clickedDropdown = btn.closest(".foreign-dropdown");
            const clickedMenu = clickedDropdown.querySelector(".foreign-dropdown-menu");
            const isAlreadyOpen = btn.classList.contains("active");

            // 다른 드롭다운 닫기
            dropdownBtns.forEach(function (otherBtn) {
                if (otherBtn === btn) return;
                const otherDropdown = otherBtn.closest(".foreign-dropdown");
                const otherMenu = otherDropdown.querySelector(".foreign-dropdown-menu");

                otherBtn.classList.remove("active");
                otherDropdown.querySelectorAll(".foreign-sub-title").forEach(function (t) {
                    t.classList.remove("active");
                });
                otherDropdown.querySelectorAll(".foreign-sub-menu").forEach(function (m) {
                    closeMenu(m, SUBMENU_DURATION);
                });
                closeMenu(otherMenu, DROPDOWN_DURATION);
            });

            // 클릭한 드롭다운 토글
            if (isAlreadyOpen) {
                btn.classList.remove("active");
                clickedDropdown.querySelectorAll(".foreign-sub-title").forEach(function (t) {
                    t.classList.remove("active");
                });
                clickedDropdown.querySelectorAll(".foreign-sub-menu").forEach(function (m) {
                    closeMenu(m, SUBMENU_DURATION);
                });
                closeMenu(clickedMenu, DROPDOWN_DURATION);
            } else {
                btn.classList.add("active");
                openMenu(clickedMenu, DROPDOWN_DURATION);
            }
        });
    });

    // ── 서브타이틀 클릭 ──
    const subTitles = document.querySelectorAll(".foreign-sub-title");

    subTitles.forEach(function (title) {
        title.addEventListener("click", function () {

            const parentGroup = title.closest(".foreign-dropdown-menu");
            const subMenu = title.nextElementSibling;
            const isAlreadyOpen = title.classList.contains("active");

            // 같은 드롭다운 내 다른 서브메뉴 닫기
            parentGroup.querySelectorAll(".foreign-sub-title").forEach(function (t) {
                if (t === title) return;
                t.classList.remove("active");
            });
            parentGroup.querySelectorAll(".foreign-sub-menu").forEach(function (m) {
                if (m === subMenu) return;
                closeMenu(m, SUBMENU_DURATION);
            });

            // 클릭한 서브메뉴 토글
            if (isAlreadyOpen) {
                title.classList.remove("active");
                if (subMenu) closeMenu(subMenu, SUBMENU_DURATION);
            } else {
                title.classList.add("active");
                if (subMenu) openMenu(subMenu, SUBMENU_DURATION);
            }
        });
    });

});