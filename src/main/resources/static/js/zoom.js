document.addEventListener("DOMContentLoaded", function () {
    const zoomBtn = document.getElementById("zoomToggleBtn");

    if (!zoomBtn) {
        return;
    }

    const savedZoomMode = localStorage.getItem("zoomMode");

    if (savedZoomMode === "on") {
        document.body.classList.add("zoom-mode");
        zoomBtn.textContent = "기본";
        zoomBtn.title = "기본 크기로 보기";
        zoomBtn.setAttribute("aria-label", "기본 크기로 보기");
    } else {
        document.body.classList.remove("zoom-mode");
        zoomBtn.textContent = "크게";
        zoomBtn.title = "화면 크게 보기";
        zoomBtn.setAttribute("aria-label", "화면 크게 보기");
    }

    zoomBtn.addEventListener("click", function () {
        document.body.classList.toggle("zoom-mode");

        const isZoomMode = document.body.classList.contains("zoom-mode");

        if (isZoomMode) {
            zoomBtn.textContent = "기본";
            zoomBtn.title = "기본 크기로 보기";
            zoomBtn.setAttribute("aria-label", "기본 크기로 보기");
            localStorage.setItem("zoomMode", "on");
        } else {
            zoomBtn.textContent = "크게";
            zoomBtn.title = "화면 크게 보기";
            zoomBtn.setAttribute("aria-label", "화면 크게 보기");
            localStorage.setItem("zoomMode", "off");
        }
    });
});