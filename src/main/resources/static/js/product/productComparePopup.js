document.addEventListener("DOMContentLoaded", function () {

    // 비교하기 버튼
    const compareBtn = document.getElementById("compareBtn");

    // 비교 팝업 전체 영역
    const modal = document.getElementById("compareModal");

    // 팝업 닫기 버튼
    const closeBtn = document.getElementById("compareCloseBtn");

    // 추천 결과랑 비교표가 들어갈 영역
    const resultBox = document.getElementById("compareResultBox");

    // 비교하기 버튼을 눌렀을 때 실행
    compareBtn.addEventListener("click", function () {

        // 체크된 상품만 가져옴
        const checkedItems = document.querySelectorAll(".compare-check:checked");

        // 상품 1개만 선택하면 비교 의미가 없어서 막음
        if (checkedItems.length < 2) {
            alert("비교할 상품을 2개 이상 선택해주세요.");
            return;
        }

        // 체크박스에 숨겨둔 data-* 값을 JS 객체로 바꿈
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

        // 비교 결과 그리기
        renderCompare(products);

        // 팝업 열기
        modal.classList.add("active");
    });

    // 닫기 버튼 누르면 팝업 닫기
    closeBtn.addEventListener("click", function () {
        modal.classList.remove("active");
    });

    // 팝업 바깥 어두운 영역 누르면 팝업 닫기
    modal.addEventListener("click", function (event) {
        if (event.target === modal) {
            modal.classList.remove("active");
        }
    });

    // 가입대상, 금액, 기간을 바꾸면 추천 결과가 바로 다시 계산되게 함
    document.getElementById("compareTarget").addEventListener("change", rerenderCompare);
    document.getElementById("compareAmount").addEventListener("input", rerenderCompare);
    document.getElementById("comparePeriod").addEventListener("input", rerenderCompare);

    // 사용자가 입력 조건을 바꿨을 때 현재 체크된 상품 기준으로 다시 비교
    function rerenderCompare() {

        const checkedItems = document.querySelectorAll(".compare-check:checked");

        // 비교 상품이 2개 미만이면 다시 계산하지 않음
        if (checkedItems.length < 2) {
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
    }

    // 실제 비교 점수를 계산하고 화면에 뿌리는 함수
    function renderCompare(products) {

        // 사용자가 선택한 가입대상
        const target = document.getElementById("compareTarget").value;

        // 사용자가 입력한 예치 예정 금액
        const amount = Number(document.getElementById("compareAmount").value || 0);

        // 사용자가 입력한 희망 가입기간
        const period = Number(document.getElementById("comparePeriod").value || 0);

        // 상품마다 점수와 추천 이유를 계산함
        const scored = products.map(product => {

            let score = 0;
            let reasons = [];

            // 금리가 높을수록 점수를 더 줌
            score += product.rate * 10;

            // 추천 이유에는 무조건 최고금리를 보여줌
            reasons.push(`최고금리 연 ${product.rate}%`);

            // DB 값이 PERSONAL, COMPANY, ALL 대문자로 오기 때문에 대문자로 비교해야 함
            // 선택한 가입대상과 상품 가입대상이 같거나, 상품 가입대상이 전체 고객이면 점수 추가
            if (product.target === target || product.target === "ALL") {
                score += 30;
                reasons.push("가입대상이 적합해요");
            }

            // 사용자가 희망 가입기간을 입력했고,
            // 그 기간이 상품의 최소/최대 가입기간 안에 들어오면 점수 추가
            if (
                period > 0 &&
                period >= product.minPeriod &&
                period <= product.maxPeriod
            ) {
                score += 30;
                reasons.push("희망 가입기간에 적합해요");
            }

            // 사용자가 예치 예정 금액을 입력했고,
            // 그 금액이 상품의 최소/최대 가입금액 안에 들어오면 점수 추가
            // maxAmount가 0이면 최대 한도가 없는 상품으로 처리
            if (
                amount > 0 &&
                amount >= product.minAmount &&
                (product.maxAmount === 0 || amount <= product.maxAmount)
            ) {
                score += 30;
                reasons.push("예치 예정 금액으로 가입 가능해요");
            }

            return {
                ...product,
                score,
                reasons
            };
        });

        // 점수가 높은 순서대로 정렬
        scored.sort((a, b) => b.score - a.score);

        // 가장 점수가 높은 첫 번째 상품을 추천 상품으로 선택
        const best = scored[0];

        // JS가 HTML을 직접 만들어서 팝업 안에 넣음
        resultBox.innerHTML = `
            <div class="best-product-box">
                <p class="best-label">고객님께 가장 잘 맞는 상품</p>

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
                        <th>최고금리</th>
                        <th>가입기간</th>
                        <th>적합도</th>
                    </tr>
                </thead>

                <tbody>
                    ${scored.map(product => `
                        <tr class="${product.id === best.id ? "best-row" : ""}">
                            <td>
                                ${product.id === best.id ? "🥇 " : ""}
                                ${product.name}
                            </td>

                            <td>${convertType(product.type)}</td>

                            <td>${convertTarget(product.target)}</td>

                            <td class="rate-cell">연 ${product.rate}%</td>

                            <td>
                                ${product.minPeriod} ~ ${product.maxPeriod}개월
                            </td>

                            <td>
                                ${Math.min(product.score, 100).toFixed(0)}점
                            </td>
                        </tr>
                    `).join("")}
                </tbody>
            </table>
        `;
    }

    // DB에 저장된 상품 유형 코드를 사용자에게 보여줄 한글 문구로 바꿈
    function convertType(type) {
        if (type === "DEPOSIT") return "예금";
        if (type === "SAVINGS") return "적금";
        if (type === "FOREIGN_DEP") return "외화예금";
        if (type === "FOREIGN_SAV") return "외화적금";

        return type;
    }

    // DB에 저장된 가입대상 코드를 사용자에게 보여줄 한글 문구로 바꿈
    function convertTarget(target) {
        if (target === "ALL") return "전체고객";
        if (target === "PERSONAL") return "개인";
        if (target === "COMPANY") return "기업";

        return target;
    }
});