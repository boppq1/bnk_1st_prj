document.addEventListener("DOMContentLoaded", function () {
    const favoriteButtons = document.querySelectorAll(".favorite-btn");

    favoriteButtons.forEach(function (btn) {
        const productId = btn.dataset.productId;

        if (!productId) {
            return;
        }

        /*
         * 화면이 처음 열릴 때
         * 이 상품이 이미 관심상품인지 확인해서
         * ♡ 또는 ♥ 상태를 맞춤
         */
        fetch(`/api/products/favorites/${productId}/status`)
            .then(response => response.json())
            .then(data => {
                if (!data.success) {
                    return;
                }

                updateFavoriteButton(btn, data.favorite);
            })
            .catch(() => {
                console.warn("관심상품 상태 조회 실패");
            });

        /*
         * 하트 클릭 시 관심상품 등록/삭제
         */
        btn.addEventListener("click", function (event) {
            event.preventDefault();
            event.stopPropagation();

            fetch(`/api/products/favorites/${productId}`, {
                method: "POST"
            })
                .then(response => response.json())
                .then(data => {
                    if (!data.success) {
                        alert(data.message);
                        return;
                    }

                    updateFavoriteButton(btn, data.favorite);
                })
                .catch(() => {
                    alert("관심상품 처리 중 오류가 발생했습니다.");
                });
        });
    });

    function updateFavoriteButton(btn, isFavorite) {
        btn.textContent = isFavorite ? "♥" : "♡";
        btn.classList.toggle("active", isFavorite);
        btn.title = isFavorite ? "관심상품 해제" : "관심상품 등록";
    }
});