document.addEventListener("DOMContentLoaded", function () {
    const zoomBtn = document.getElementById("zoomToggleBtn");

    if (!zoomBtn) return;

    zoomBtn.addEventListener("click", function () {
        document.body.classList.toggle("zoom-mode");

        zoomBtn.textContent = document.body.classList.contains("zoom-mode")
            ? "기본 크기로 보기"
            : "화면 크게 보기";
    });
});