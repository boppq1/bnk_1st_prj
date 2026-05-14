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

        const products = getCheckedProducts(checkedItems);

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

    document.getElementById("compareTarget").addEventListener("change", rerenderCompare);
    document.getElementById("compareAmount").addEventListener("input", rerenderCompare);
    document.getElementById("comparePeriod").addEventListener("input", rerenderCompare);
    document.getElementById("compareAge").addEventListener("input", rerenderCompare);

    function rerenderCompare() {

        const checkedItems = document.querySelectorAll(".compare-check:checked");

        if (checkedItems.length < 2) {
            return;
        }

        const products = getCheckedProducts(checkedItems);

        renderCompare(products);
    }

    function getCheckedProducts(checkedItems) {

        return Array.from(checkedItems).map(item => ({
            id: item.dataset.id,
            name: item.dataset.name,
            type: item.dataset.type,
            target: item.dataset.target,
            targetDetail: item.dataset.targetDetail,
            rate: Number(item.dataset.rate || 0),
            desc: item.dataset.desc,
            minPeriod: Number(item.dataset.minPeriod || 0),
            maxPeriod: Number(item.dataset.maxPeriod || 0),
            minAmount: Number(item.dataset.minAmount || 0),
            maxAmount: Number(item.dataset.maxAmount || 0)
        }));
    }

    function renderCompare(products) {

        const target = document.getElementById("compareTarget").value;
        const amount = Number(document.getElementById("compareAmount").value || 0);
        const period = Number(document.getElementById("comparePeriod").value || 0);
        const age = Number(document.getElementById("compareAge").value || 0);

        const ageGroup = convertAgeToTargetDetail(age);

        const scored = products.map(product => {

            let score = 0;
            let reasons = [];
            let available = true;

            score += product.rate * 10;
            reasons.push(`최고금리 연 ${product.rate}%`);

            if (product.target === target || product.target === "ALL") {
                score += 30;
                reasons.push("가입대상이 적합해요");
            } else {
                score -= 100;
                available = false;
                reasons.push("가입대상이 맞지 않아요");
            }

            if (
                period > 0 &&
                product.minPeriod > 0 &&
                product.maxPeriod > 0
            ) {
                if (period >= product.minPeriod && period <= product.maxPeriod) {
                    score += 30;
                    reasons.push("희망 가입기간에 적합해요");
                } else {
                    score -= 50;
                    available = false;
                    reasons.push("희망 가입기간이 맞지 않아요");
                }
            }

            if (
                amount > 0 &&
                product.minAmount > 0
            ) {
                if (
                    amount >= product.minAmount &&
                    (
                        product.maxAmount === 0 ||
                        amount <= product.maxAmount
                    )
                ) {
                    score += 30;
                    reasons.push("예치 예정 금액으로 가입 가능해요");
                } else {
                    score -= 50;
                    available = false;
                    reasons.push("예치 예정 금액이 가입 가능 범위와 맞지 않아요");
                }
            }

            if (ageGroup) {

                if (
                    !product.targetDetail ||
                    product.targetDetail === "ALL"
                ) {
                    score += 10;
                    reasons.push("전체 연령 가입 가능 상품이에요");
                }

                else if (product.targetDetail === ageGroup) {
                    score += 25;
                    reasons.push("나이 조건에 적합해요");
                }

                else {
                    score -= 100;
                    available = false;
                    reasons.push("입력한 나이 조건과 맞지 않아요");
                }
            }

            return {
                ...product,
                score,
                reasons,
                available
            };
        });

        const availableProducts = scored.filter(product => product.available);

        const finalScored = availableProducts.length > 0
            ? availableProducts
            : scored;

        finalScored.sort((a, b) => b.score - a.score);

        const best = finalScored[0];

        resultBox.innerHTML = `
			<div class="best-product-box clickable-best-product"
			     onclick="location.href='/product/${best.id}'">

                <p class="best-label">
                    고객님께 가장 잘 맞는 상품
                </p>

                <h3>${best.name}</h3>

                <div class="best-rate">
                    최고금리 연 ${best.rate}%
                </div>

                <ul class="recommend-reason-list">
                    ${best.reasons.map(reason => `
                        <li>✔ ${reason}</li>
                    `).join("")}
                </ul>

            </div>

            <table class="compare-table">

                <thead>
                    <tr>
                        <th>상품명</th>
                        <th>상품유형</th>
                        <th>가입대상</th>
                        <th>나이대</th>
                        <th>최고금리</th>
                        <th>가입기간</th>
                        <th>적합도</th>
                    </tr>
                </thead>

                <tbody>

                    ${finalScored.map(product => `

						<tr class="${product.id === best.id ? "best-row" : ""} clickable-compare-row"
						    onclick="location.href='/product/${product.id}'">

                            <td>
                                ${product.id === best.id ? "🥇 " : ""}
                                ${product.name}
                            </td>

                            <td>${convertType(product.type)}</td>

                            <td>${convertTarget(product.target)}</td>

                            <td>${convertTargetDetail(product.targetDetail)}</td>

                            <td class="rate-cell">
                                연 ${product.rate}%
                            </td>

                            <td>
                                ${product.minPeriod}
                                ~
                                ${product.maxPeriod}개월
                            </td>

                            <td>
                                ${Math.max(0, Math.min(product.score, 100)).toFixed(0)}점
                            </td>

                        </tr>

                    `).join("")}

                </tbody>

            </table>
        `;
    }

    function convertAgeToTargetDetail(age) {

        if (age <= 0) return "";

        if (age <= 18) return "CHILD";

        if (age <= 34) return "YOUTH";

        if (age <= 59) return "ADULT";

        return "SENIOR";
    }

    function convertType(type) {

        if (type === "DEPOSIT") return "예금";
        if (type === "SAVINGS") return "적금";
        if (type === "FOREIGN_DEP") return "외화예금";
        if (type === "FOREIGN_SAV") return "외화적금";

        return type;
    }

    function convertTarget(target) {

        if (target === "ALL") return "전체고객";
        if (target === "PERSONAL") return "개인";
        if (target === "COMPANY") return "기업";

        return target;
    }

    function convertTargetDetail(targetDetail) {

        if (!targetDetail) return "전체";
        if (targetDetail === "ALL") return "전체";
        if (targetDetail === "CHILD") return "아동";
        if (targetDetail === "YOUTH") return "청년";
        if (targetDetail === "ADULT") return "일반";
        if (targetDetail === "SENIOR") return "시니어";

        return targetDetail;
    }

});