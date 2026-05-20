document.addEventListener("DOMContentLoaded", function () {
    // HTML 문서가 모두 로드된 뒤 JS가 실행되도록 설정한다.

    const compareBtn = document.getElementById("compareBtn");
    // 선택 상품 비교하기 버튼을 가져온다.

    const modal = document.getElementById("compareModal");
    // 비교 결과를 보여줄 모달 창을 가져온다.

    const closeBtn = document.getElementById("compareCloseBtn");
    // 모달 닫기 버튼을 가져온다.

    const resultBox = document.getElementById("compareResultBox");
    // 비교 결과가 출력될 영역을 가져온다.

    compareBtn.addEventListener("click", function () {
        // 비교하기 버튼을 클릭했을 때 실행된다.

        const checkedItems = document.querySelectorAll(".compare-check:checked");
        // 체크된 상품만 가져온다.

        if (checkedItems.length < 2) {
            // 선택한 상품이 2개 미만이면 비교를 막는다.

            alert("비교할 상품을 2개 이상 선택해주세요.");
            // 사용자에게 안내 메시지를 띄운다.

            return;
            // 함수 실행을 중단한다.
        }

        const products = getCheckedProducts(checkedItems);
        // 체크된 상품 정보를 JS 객체 배열로 변환한다.

        renderCompare(products);
        // 상품 비교 결과를 화면에 출력한다.

        modal.classList.add("active");
        // 모달에 active 클래스를 추가해서 팝업을 보이게 한다.
    });

    closeBtn.addEventListener("click", function () {
        // 닫기 버튼을 클릭했을 때 실행된다.

        modal.classList.remove("active");
        // 모달을 숨긴다.
    });

    modal.addEventListener("click", function (event) {
        // 모달 배경을 클릭했을 때 실행된다.

        if (event.target === modal) {
            // 클릭한 대상이 모달 배경이면

            modal.classList.remove("active");
            // 모달을 닫는다.
        }
    });

    document.getElementById("compareTarget").addEventListener("change", rerenderCompare);
    // 가입대상이 바뀌면 비교 결과를 다시 계산한다.

    document.getElementById("compareAmount").addEventListener("input", rerenderCompare);
    // 예치 금액이 입력될 때마다 비교 결과를 다시 계산한다.

    document.getElementById("comparePeriod").addEventListener("input", rerenderCompare);
    // 가입 기간이 입력될 때마다 비교 결과를 다시 계산한다.

    document.getElementById("compareAge").addEventListener("input", rerenderCompare);
    // 나이가 입력될 때마다 비교 결과를 다시 계산한다.

    function rerenderCompare() {
        // 입력값이 바뀔 때 비교 결과를 다시 그리는 함수이다.

        const checkedItems = document.querySelectorAll(".compare-check:checked");
        // 현재 체크된 상품을 다시 가져온다.

        if (checkedItems.length < 2) {
            // 체크된 상품이 2개 미만이면

            return;
            // 다시 계산하지 않는다.
        }

        const products = getCheckedProducts(checkedItems);
        // 체크된 상품 정보를 다시 객체 배열로 변환한다.

        renderCompare(products);
        // 변경된 입력값 기준으로 비교 결과를 다시 출력한다.
    }

    function getCheckedProducts(checkedItems) {
        // 체크된 상품의 data-* 값을 가져와 객체 배열로 변환하는 함수이다.

        return Array.from(checkedItems).map(item => ({
            // 체크박스 목록을 배열로 바꾸고 각 상품 정보를 객체로 만든다.

            id: item.dataset.id,
            // 상품 ID를 가져온다.

            name: item.dataset.name,
            // 상품명을 가져온다.

            type: item.dataset.type,
            // 상품 유형을 가져온다.

            target: item.dataset.target,
            // 가입대상 대분류를 가져온다.

            targetDetail: item.dataset.targetDetail,
            // 가입대상 세부 조건을 가져온다.

            rate: Number(item.dataset.rate || 0),
            // 최고금리를 숫자로 변환해서 가져온다.

            desc: item.dataset.desc,
            // 상품 설명을 가져온다.

            minPeriod: Number(item.dataset.minPeriod || 0),
            // 최소 가입기간을 숫자로 변환한다.

            maxPeriod: Number(item.dataset.maxPeriod || 0),
            // 최대 가입기간을 숫자로 변환한다.

            minAmount: Number(item.dataset.minAmount || 0),
            // 최소 가입금액을 숫자로 변환한다.

            maxAmount: Number(item.dataset.maxAmount || 0)
            // 최대 가입금액을 숫자로 변환한다.
        }));
    }

    function renderCompare(products) {
        // 상품 비교, 추천, 이자 계산 결과를 화면에 출력하는 함수이다.

        const target = document.getElementById("compareTarget").value;
        // 사용자가 선택한 가입대상을 가져온다.

        const amount = Number(document.getElementById("compareAmount").value || 0);
        // 사용자가 입력한 예치 금액을 숫자로 변환한다.

        const period = Number(document.getElementById("comparePeriod").value || 0);
        // 사용자가 입력한 가입기간을 숫자로 변환한다.

        const age = Number(document.getElementById("compareAge").value || 0);
        // 사용자가 입력한 나이를 숫자로 변환한다.

        const ageGroup = convertAgeToTargetDetail(age);
        // 나이를 상품 조건과 비교할 수 있는 코드값으로 변환한다.

        const scored = products.map(product => {
            // 선택된 상품마다 적합도 점수와 이자 계산을 수행한다.

            let score = 0;
            // 상품 적합도 점수를 저장한다.

            let reasons = [];
            // 추천 사유를 저장한다.

            let available = true;
            // 상품 가입 가능 여부를 저장한다.

            score += product.rate * 10;
            // 금리가 높을수록 높은 점수를 부여한다.

            reasons.push(`최고금리 연 ${product.rate}%`);
            // 추천 사유에 최고금리를 추가한다.

            if (product.target === target || product.target === "ALL") {
                // 상품 가입대상이 사용자 조건과 같거나 전체 고객이면

                score += 30;
                // 적합도 점수를 추가한다.

                reasons.push("가입대상이 적합해요");
                // 추천 사유를 추가한다.

            } else {
                // 가입대상이 맞지 않으면

                score -= 100;
                // 큰 감점을 적용한다.

                available = false;
                // 가입 가능하지 않은 상품으로 표시한다.

                reasons.push("가입대상이 맞지 않아요");
                // 부적합 사유를 추가한다.
            }

            if (period > 0 && product.minPeriod > 0 && product.maxPeriod > 0) {
                // 사용자가 가입기간을 입력했고 상품에도 가입기간 조건이 있으면

                if (period >= product.minPeriod && period <= product.maxPeriod) {
                    // 입력한 가입기간이 상품 가입기간 범위 안에 있으면

                    score += 30;
                    // 적합도 점수를 추가한다.

                    reasons.push("희망 가입기간에 적합해요");
                    // 추천 사유를 추가한다.

                } else {
                    // 가입기간이 맞지 않으면

                    score -= 50;
                    // 감점을 적용한다.

                    available = false;
                    // 가입 가능하지 않은 상품으로 표시한다.

                    reasons.push("희망 가입기간이 맞지 않아요");
                    // 부적합 사유를 추가한다.
                }
            }

            if (amount > 0 && product.minAmount > 0) {
                // 사용자가 예치금액을 입력했고 상품에도 최소금액 조건이 있으면

                if (
                    amount >= product.minAmount &&
                    (
                        product.maxAmount === 0 ||
                        amount <= product.maxAmount
                    )
                ) {
                    // 입력한 금액이 최소금액 이상이고 최대금액 이하이면

                    score += 30;
                    // 적합도 점수를 추가한다.

                    reasons.push("예치 예정 금액으로 가입 가능해요");
                    // 추천 사유를 추가한다.

                } else {
                    // 금액 조건이 맞지 않으면

                    score -= 50;
                    // 감점을 적용한다.

                    available = false;
                    // 가입 가능하지 않은 상품으로 표시한다.

                    reasons.push("예치 예정 금액이 가입 가능 범위와 맞지 않아요");
                    // 부적합 사유를 추가한다.
                }
            }

            if (ageGroup) {
                // 사용자가 나이를 입력했다면

                if (!product.targetDetail || product.targetDetail === "ALL") {
                    // 상품이 전체 연령 가입 가능이면

                    score += 10;
                    // 소폭 점수를 추가한다.

                    reasons.push("전체 연령 가입 가능 상품이에요");
                    // 추천 사유를 추가한다.

                } else if (product.targetDetail === ageGroup) {
                    // 상품 연령 조건이 사용자 나이대와 일치하면

                    score += 25;
                    // 적합도 점수를 추가한다.

                    reasons.push("나이 조건에 적합해요");
                    // 추천 사유를 추가한다.

                } else {
                    // 연령 조건이 맞지 않으면

                    score -= 100;
                    // 큰 감점을 적용한다.

                    available = false;
                    // 가입 가능하지 않은 상품으로 표시한다.

                    reasons.push("입력한 나이 조건과 맞지 않아요");
                    // 부적합 사유를 추가한다.
                }
            }

            const interest = calculateInterest(amount, product.rate, period);
            // 예치금액, 금리, 가입기간을 기준으로 예상 이자를 계산한다.

            return {
                // 기존 상품 정보에 계산 결과를 추가해서 반환한다.

                ...product,
                // 기존 상품 정보를 그대로 유지한다.

                score,
                // 계산된 적합도 점수를 저장한다.

                reasons,
                // 추천 사유 목록을 저장한다.

                available,
                // 가입 가능 여부를 저장한다.

                beforeTaxInterest: interest.beforeTaxInterest,
                // 세전 예상 이자를 저장한다.

                afterTaxInterest: interest.afterTaxInterest,
                // 세후 예상 이자를 저장한다.

                maturityAmount: interest.maturityAmount
                // 예상 만기 수령액을 저장한다.
            };
        });

        const availableProducts = scored.filter(product => product.available);
        // 가입 가능한 상품만 따로 걸러낸다.

        const finalScored = availableProducts.length > 0
            ? availableProducts
            : scored;
        // 가입 가능한 상품이 있으면 그것만 사용하고, 없으면 전체 상품을 사용한다.

        finalScored.sort((a, b) => b.score - a.score);
        // 적합도 점수가 높은 순서대로 정렬한다.

        const best = finalScored[0];
        // 가장 점수가 높은 상품을 추천 상품으로 선택한다.

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

                <div class="interest-summary">
                    <div>
                        <span>세전 예상이자</span>
                        <strong>${formatMoney(best.beforeTaxInterest)}</strong>
                    </div>

                    <div>
                        <span>세후 예상이자</span>
                        <strong>${formatMoney(best.afterTaxInterest)}</strong>
                    </div>

                    <div>
                        <span>예상 만기수령액</span>
                        <strong>${formatMoney(best.maturityAmount)}</strong>
                    </div>
                </div>

                <p class="interest-notice">
                    ※ 단리 및 일반과세 15.4% 기준 예상 금액입니다.
                </p>

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
                        <th>세후 예상이자</th>
                        <th>만기수령액</th>
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
                                ${formatMoney(product.afterTaxInterest)}
                            </td>

                            <td>
                                ${formatMoney(product.maturityAmount)}
                            </td>

                            <td>
                                ${Math.max(0, Math.min(product.score, 100)).toFixed(0)}점
                            </td>

                        </tr>

                    `).join("")}

                </tbody>

            </table>
        `;
        // 추천 상품 박스와 비교 테이블을 HTML로 만들어 화면에 출력한다.
    }

    function calculateInterest(amount, rate, period) {
        // 예치금액, 금리, 가입기간을 이용해 예상 이자를 계산하는 함수이다.

        if (amount <= 0 || rate <= 0 || period <= 0) {
            // 금액, 금리, 기간 중 하나라도 0 이하이면 계산할 수 없으므로

            return {
                // 기본값을 반환한다.

                beforeTaxInterest: 0,
                // 세전 이자는 0원으로 설정한다.

                afterTaxInterest: 0,
                // 세후 이자는 0원으로 설정한다.

                maturityAmount: amount
                // 만기수령액은 입력한 원금으로 설정한다.
            };
        }

        const beforeTaxInterest = amount * (rate / 100) * (period / 12);
        // 세전 예상이자 = 예치금액 × 금리 × 가입기간 비율로 계산한다.

        const afterTaxInterest = beforeTaxInterest * 0.846;
        // 세후 예상이자 = 세전 이자에서 일반과세 15.4%를 제외한 금액이다.

        const maturityAmount = amount + afterTaxInterest;
        // 예상 만기수령액 = 원금 + 세후 예상이자이다.

        return {
            // 계산된 결과를 객체로 반환한다.

            beforeTaxInterest,
            // 세전 예상 이자를 반환한다.

            afterTaxInterest,
            // 세후 예상 이자를 반환한다.

            maturityAmount
            // 예상 만기 수령액을 반환한다.
        };
    }

    function formatMoney(value) {
        // 숫자 금액을 원화 표시 형식으로 바꾸는 함수이다.

        return Math.floor(value).toLocaleString("ko-KR") + "원";
        // 소수점을 버리고 천 단위 콤마와 원 단위를 붙인다.
    }

    function convertAgeToTargetDetail(age) {
        // 입력한 나이를 상품의 연령 조건 코드로 변환하는 함수이다.

        if (age <= 0) return "";
        // 나이를 입력하지 않았거나 0 이하이면 빈 값을 반환한다.

        if (age <= 18) return "CHILD";
        // 18세 이하는 어린이 고객으로 분류한다.

        if (age <= 34) return "YOUTH";
        // 19세부터 34세까지는 청년 고객으로 분류한다.

        if (age <= 59) return "ADULT";
        // 35세부터 59세까지는 일반 고객으로 분류한다.

        return "SENIOR";
        // 60세 이상은 시니어 고객으로 분류한다.
    }

    function convertType(type) {
        // 상품 유형 코드를 한글 상품명으로 변환하는 함수이다.

        if (type === "DEPOSIT") return "예금";
        // DEPOSIT이면 예금으로 표시한다.

        if (type === "SAVINGS") return "적금";
        // SAVINGS이면 적금으로 표시한다.

        if (type === "FOREIGN_DEP") return "외화예금";
        // FOREIGN_DEP이면 외화예금으로 표시한다.

        if (type === "FOREIGN_SAV") return "외화적금";
        // FOREIGN_SAV이면 외화적금으로 표시한다.

        return type;
        // 해당하지 않는 값은 원래 코드 그대로 반환한다.
    }

    function convertTarget(target) {
        // 가입대상 코드를 한글로 변환하는 함수이다.

        if (target === "ALL") return "전체고객";
        // ALL이면 전체고객으로 표시한다.

        if (target === "PERSONAL") return "개인";
        // PERSONAL이면 개인으로 표시한다.

        if (target === "COMPANY") return "기업";
        // COMPANY이면 기업으로 표시한다.

        return target;
        // 해당하지 않는 값은 원래 코드 그대로 반환한다.
    }

    function convertTargetDetail(targetDetail) {
        // 세부 가입대상 코드를 한글로 변환하는 함수이다.

        if (!targetDetail) return "전체";
        // 값이 없으면 전체로 표시한다.

        if (targetDetail === "ALL") return "전체";
        // ALL이면 전체로 표시한다.

        if (targetDetail === "CHILD") return "아동";
        // CHILD이면 아동으로 표시한다.

        if (targetDetail === "YOUTH") return "청년";
        // YOUTH이면 청년으로 표시한다.

        if (targetDetail === "ADULT") return "일반";
        // ADULT이면 일반으로 표시한다.

        if (targetDetail === "SENIOR") return "시니어";
        // SENIOR이면 시니어로 표시한다.

        return targetDetail;
        // 해당하지 않는 값은 원래 코드 그대로 반환한다.
    }

});

