document.addEventListener("DOMContentLoaded", function () {

    // ── 초기화: HTML에 하드코딩된 active 클래스 전부 제거 ──
    // (CSS에서 JS로 완전히 제어하기 위해 초기 active 상태를 리셋)
    document.querySelectorAll(".foreign-dropdown-menu").forEach(m => m.classList.remove("active"));
    document.querySelectorAll(".foreign-sub-menu").forEach(m => m.classList.remove("active"));
    document.querySelectorAll(".foreign-sub-title").forEach(t => t.classList.remove("active"));
    document.querySelectorAll(".foreign-dropdown-btn").forEach(b => b.classList.remove("active"));

    const dropdownBtns = document.querySelectorAll(".foreign-dropdown-btn");

    dropdownBtns.forEach(function (btn) {

        btn.addEventListener("click", function () {

            const clickedDropdown = btn.closest(".foreign-dropdown");
            const clickedMenu = clickedDropdown.querySelector(".foreign-dropdown-menu");
            const isAlreadyOpen = btn.classList.contains("active");

            // 열려있는 드롭다운 전부 닫기
            dropdownBtns.forEach(function (otherBtn) {
                const otherDropdown = otherBtn.closest(".foreign-dropdown");
                const otherMenu = otherDropdown.querySelector(".foreign-dropdown-menu");
                otherBtn.classList.remove("active");
                otherMenu.classList.remove("active");

                // 하위 서브타이틀/서브메뉴도 닫기
                otherDropdown.querySelectorAll(".foreign-sub-title").forEach(t => t.classList.remove("active"));
                otherDropdown.querySelectorAll(".foreign-sub-menu").forEach(m => m.classList.remove("active"));
            });

            // 클릭한 게 이미 열려있던 거면 그냥 닫힌 채로 종료, 아니면 열기
            if (!isAlreadyOpen) {
                btn.classList.add("active");
                clickedMenu.classList.add("active");
            }

        });

    });

    const subTitles = document.querySelectorAll(".foreign-sub-title");

    subTitles.forEach(function (title) {

        title.addEventListener("click", function () {

            const parentGroup = title.closest(".foreign-dropdown-menu");
            const subMenu = title.nextElementSibling;
            const isAlreadyOpen = title.classList.contains("active");

            // 같은 드롭다운 안의 서브타이틀 전부 닫기
            parentGroup.querySelectorAll(".foreign-sub-title").forEach(t => t.classList.remove("active"));
            parentGroup.querySelectorAll(".foreign-sub-menu").forEach(m => m.classList.remove("active"));

            // 클릭한 게 이미 열려있던 거면 닫힌 채 종료, 아니면 열기
            if (!isAlreadyOpen && subMenu) {
                title.classList.add("active");
                subMenu.classList.add("active");
            }

        });

    });

});