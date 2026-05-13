document.addEventListener("DOMContentLoaded", function () {
    const compareBtn = document.getElementById("compareBtn");
    const modal = document.getElementById("compareModal");
    const closeBtn = document.getElementById("compareCloseBtn");
    const resultBox = document.getElementById("compareResultBox");

    compareBtn.addEventListener("click", function () {
        const checkedItems = document.querySelectorAll(".compare-check:checked");

        if (checkedItems.length < 2) {
            alert("비교할 상품을 2개 이상 선택해주세요.");
            return;
        }

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
            let score = 0;
            let reasons = [];

            score += product.rate * 10;
            reasons.push(`최고금리 연 ${product.rate}%`);

            if (product.target === target || product.target === "all") {
                score += 20;
                reasons.push("가입대상이 적합함");
            }

            if (period > 0 && period >= product.minPeriod && period <= product.maxPeriod) {
                score += 25;
                reasons.push("희망 가입기간에 적합함");
            }

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

        scored.sort((a, b) => b.score - a.score);
        const best = scored[0];

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

    function convertTarget(target) {
        if (target === "all") return "전체";
        if (target === "personal") return "개인";
        if (target === "company") return "기업";
        return target;
    }
});