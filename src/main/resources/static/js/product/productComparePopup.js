document.addEventListener("DOMContentLoaded", function () {
    const compareBtn = document.getElementById("compareBtn");
    const modal = document.getElementById("compareModal");
    const closeBtn = document.getElementById("compareCloseBtn");
    const resultBox = document.getElementById("compareResultBox");

    compareBtn.addEventListener("click", function () {
		// 체크 된 상품만 가져옴
        const checkedItems = document.querySelectorAll(".compare-check:checked");
		// 상품 1개만 선택하면 비교 의미가 없어서 막음
        if (checkedItems.length < 2) {
            alert("비교할 상품을 2개 이상 선택해주세요.");
            return;
        }
		// 체크박스에 숨겨둔 data-* 값을 JS 객체로 바꾸는 부분, html에서는 data-name="BNK 스타 정기예금" 인데 js에서는 item.dataset.name 으로 꺼냄
        const products = Array.from(checkedItems).map(item => ({
            id: item.dataset.id,
            name: item.dataset.name,
            type: item.dataset.type,
            target: item.dataset.target,
            rate: Number(item.dataset.rate || 0),
            desc: item.dataset.desc,
            minPeriod: Number(item.dataset.minPeriod || 0),
            maxPeriod: Number(item.dataset.maxPeriod || 0),
            minAmount: Number(item.dataset.minAmount || 0),
            maxAmount: Number(item.dataset.maxAmount || 0)
        }));

        renderCompare(products);
        modal.classList.add("active");
    });

    closeBtn.addEventListener("click", function () {
        modal.classList.remove("active");
    });

    modal.addEventListener("click", function (event) {
        if (event.target === modal) {
            modal.classList.remove("active");
        }
    });

    function renderCompare(products) {
        const target = document.getElementById("compareTarget").value;
        const amount = Number(document.getElementById("compareAmount").value || 0);
        const period = Number(document.getElementById("comparePeriod").value || 0);

        const scored = products.map(product => {
			// 각 상품마다 점수랑 추천이유를 만든다.
            let score = 0;
            let reasons = [];
			// 금리가 높을수록 점수 높게 줌.
            score += product.rate * 10;
            reasons.push(`최고금리 연 ${product.rate}%`);
			//사용자가 개인인데 상품도 개인이거나 전체면 점수 추가.
			// 상품 가입 대상이 개인이고 사용자가 개인이면 점수 추가, 상품 가입대상이 전체여도 점수 추가.
            if (product.target === target || product.target === "all") {
                score += 20;
                reasons.push("가입대상이 적합함");
            }
			// 희망 가입기간이 상품 가입 가능 기간 안에 있으면 점수 추가.
			// 상품의 최대, 최소 가입 금액과 비교하는 로직, 범위 안에 있으면 점수 추가  
            if (period > 0 && period >= product.minPeriod && period <= product.maxPeriod) {
                score += 25;
                reasons.push("희망 가입기간에 적합함");
            }
			//예치금이 상품 가입금액 범위 안에 있으면 점수 추가.
			//상품의 최소/최대 가입기간과 비교, 범위 안이면 점수 추가
            if (
                amount > 0 &&
                amount >= product.minAmount &&
                (product.maxAmount === 0 || amount <= product.maxAmount)
            ) {
                score += 25;
                reasons.push("예치 예정 금액이 가입 가능 범위에 포함됨");
            }

            return {
                ...product,
                score,
                reasons
            };
        });
		// 점수 노ㅍ은 순서대로 정렬하고, 첫번째 상품을 추천 상품으로 선택.
        scored.sort((a, b) => b.score - a.score);
        const best = scored[0];
		// JS가 HTML을 직접 만들어서 팝업 안에 넣음.
        resultBox.innerHTML = `
            <div class="best-product-box">
                <p class="best-label">추천 상품</p>
                <h3>${best.name}</h3>
                <p>${best.reasons.join(" · ")}</p>
            </div>

            <table class="compare-table">
                <thead>
                    <tr>
                        <th>상품명</th>
                        <th>상품분류</th>
                        <th>가입대상</th>
                        <th>최고금리</th>
                        <th>가입기간</th>
                        <th>점수</th>
                    </tr>
                </thead>
                <tbody>
                    ${scored.map(product => `
                        <tr class="${product.id === best.id ? "best-row" : ""}">
                            <td>${product.name}</td>
                            <td>${product.type}</td>
                            <td>${convertTarget(product.target)}</td>
                            <td>연 ${product.rate}%</td>
                            <td>${product.minPeriod} ~ ${product.maxPeriod}개월</td>
                            <td>${product.score.toFixed(1)}</td>
                        </tr>
                    `).join("")}
                </tbody>
            </table>
        `;
    }
// DB 값ㅇ은 영어라서 화면에 한글로 보여주려고 만든 함수
    function convertTarget(target) {
        if (target === "all") return "전체";
        if (target === "personal") return "개인";
        if (target === "company") return "기업";
        return target;
    }
});