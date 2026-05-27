document.addEventListener("DOMContentLoaded", function () {
    const compareBtn = document.getElementById("compareBtn");
    const modal = document.getElementById("compareModal");
    const closeBtn = document.getElementById("compareCloseBtn");
    const resultBox = document.getElementById("compareResultBox");

    if (!compareBtn || !modal || !closeBtn || !resultBox) {
        return;
    }

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

    const compareTarget = document.getElementById("compareTarget");
    const compareAmount = document.getElementById("compareAmount");
    const comparePeriod = document.getElementById("comparePeriod");
    const compareAge = document.getElementById("compareAge");
    const compareCurrencyCode = document.getElementById("compareCurrencyCode");

    if (compareTarget) {
        compareTarget.addEventListener("change", rerenderCompare);
    }

    if (compareAmount) {
        compareAmount.addEventListener("input", rerenderCompare);
    }

    if (comparePeriod) {
        comparePeriod.addEventListener("input", rerenderCompare);
    }

    if (compareAge) {
        compareAge.addEventListener("input", rerenderCompare);
    }

    if (compareCurrencyCode) {
        compareCurrencyCode.addEventListener("change", rerenderCompare);
    }

    function rerenderCompare() {
        const checkedItems = document.querySelectorAll(".compare-check:checked");

        if (checkedItems.length < 2) {
            return;
        }

        const products = getCheckedProducts(checkedItems);
        renderCompare(products);
    }

    function getCheckedProducts(checkedItems) {
        return Array.from(checkedItems).map(function (item) {
            return {
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
                maxAmount: Number(item.dataset.maxAmount || 0),

                /*
                 * 대표 통화코드
                 * 예: USD
                 */
                currencyCode: item.dataset.currencyCode || "",

                /*
                 * 지원 통화코드 목록
                 * 예: USD,JPY,EUR
                 */
                currencyCodes: item.dataset.currencyCodes || ""
            };
        });
    }

    async function renderCompare(products) {
        const target = document.getElementById("compareTarget")?.value || "PERSONAL";
        const amount = Number(document.getElementById("compareAmount")?.value || 0);
        const period = Number(document.getElementById("comparePeriod")?.value || 0);
        const age = Number(document.getElementById("compareAge")?.value || 0);

        /*
         * 사용자가 선택한 외화 비교 통화
         * 선택 안 하면 상품 통화 기준으로 계산
         */
        const selectedCurrencyCode =
            document.getElementById("compareCurrencyCode")?.value || "";

        const ageGroup = convertAgeToTargetDetail(age);

        if (amount <= 0 || period <= 0) {
            resultBox.innerHTML = `
                <div class="compare-empty-message">
                    예치 예정 금액과 희망 가입기간을 입력하면 상품별 예상 결과를 비교할 수 있습니다.
                </div>
            `;
            return;
        }

        resultBox.innerHTML = `
            <div class="compare-loading">
                상품 비교 결과를 계산하고 있습니다.
            </div>
        `;

        let comparedProducts = await Promise.all(
            products.map(function (product) {
                return buildComparedProduct(
                    product,
                    target,
                    amount,
                    period,
                    ageGroup,
                    selectedCurrencyCode
                );
            })
        );

        /*
         * 외화 비교 통화를 선택했으면,
         * 그 통화를 지원하지 않는 외화상품은 결과에서 제외
         */
        if (selectedCurrencyCode) {
            comparedProducts = comparedProducts.filter(function (product) {
                return product.statusText !== "선택 통화 미지원";
            });
        }

        if (comparedProducts.length === 0) {
            resultBox.innerHTML = `
                <div class="compare-empty-message">
                    선택한 통화를 지원하는 상품이 없습니다.<br>
                    다른 통화를 선택하거나 비교할 상품을 다시 선택해주세요.
                </div>
            `;
            return;
        }

        comparedProducts.sort(function (a, b) {
            if (a.calcError !== b.calcError) {
                return a.calcError ? 1 : -1;
            }

            if (b.matchCount !== a.matchCount) {
                return b.matchCount - a.matchCount;
            }

            return b.sortInterest - a.sortInterest;
        });

        const best = comparedProducts[0];

        resultBox.innerHTML = `
            <div class="best-product-box clickable-best-product"
                 onclick="location.href='/product/${best.id}'">

                <p class="best-label">
                    입력 조건과 가장 가까운 상품
                </p>

                <h3>${escapeHtml(best.name)}</h3>

                <div class="best-rate">
                    최고금리 연 ${formatPercent(best.rate)}
                </div>

                <div class="compare-status ${best.statusClass}">
                    ${best.statusText}
                </div>

                ${renderBestInterest(best)}

                <ul class="recommend-reason-list">
                    ${best.reasons.map(function (reason) {
                        return `<li>${reason}</li>`;
                    }).join("")}
                </ul>

                ${best.notice ? `
                    <p class="interest-notice">
                        ${best.notice}
                    </p>
                ` : ""}

            </div>

            <table class="compare-table">
                <thead>
                    <tr>
                        <th>상품명</th>
                        <th>상품유형</th>
                        <th>가입대상</th>
                        <th>가입기간</th>
                        <th>최고금리</th>
                        <th>조건 확인</th>
                        <th>세후이자</th>
                        <th>만기 예상금액</th>
                        <th>안내</th>
                    </tr>
                </thead>

                <tbody>
                    ${comparedProducts.map(function (product) {
                        return `
                            <tr class="${product.id === best.id ? "best-row" : ""} clickable-compare-row"
                                onclick="location.href='/product/${product.id}'">

                                <td>
                                    ${product.id === best.id ? "🏅 " : ""}
                                    ${escapeHtml(product.name)}
                                </td>

                                <td>${convertType(product.type)}</td>

                                <td>${convertTarget(product.target)}</td>

                                <td>
                                    ${product.minPeriod}
                                    ~
                                    ${product.maxPeriod}개월
                                </td>

                                <td class="rate-cell">
                                    연 ${formatPercent(product.rate)}
                                </td>

                                <td>
                                    <span class="compare-status ${product.statusClass}">
                                        ${product.statusText}
                                    </span>
                                </td>

                                <td>
                                    ${renderInterestCell(product)}
                                </td>

                                <td>
                                    ${renderMaturityCell(product)}
                                </td>

                                <td>
                                    ${escapeHtml(product.shortNotice)}
                                </td>

                            </tr>
                        `;
                    }).join("")}
                </tbody>
            </table>
        `;
    }

    async function buildComparedProduct(
        product,
        target,
        amount,
        period,
        ageGroup,
        selectedCurrencyCode
    ) {
        /*
         * 외화상품인데 사용자가 특정 통화를 선택했고,
         * 해당 상품이 그 통화를 지원하지 않으면 비교 대상에서 제외 처리
         */
        if (!supportsSelectedCurrency(product, selectedCurrencyCode)) {
            return {
                ...product,
                reasons: ["✘ 선택한 외화 비교 통화를 지원하지 않는 상품입니다."],
                matchCount: -1,
                mismatchCount: 99,
                statusText: "선택 통화 미지원",
                statusClass: "bad",
                calcType: "FOREIGN",
                calcError: true,
                beforeTaxInterest: 0,
                afterTaxInterest: 0,
                maturityAmount: 0,
                foreignPrincipal: 0,
                maturityForeignAmount: 0,
                currencyCode: product.currencyCode,
                exchangeRate: 0,
                notice: "선택한 통화를 지원하지 않아 비교 대상에서 제외되었습니다.",
                shortNotice: "선택 통화 미지원",
                sortInterest: -1
            };
        }

        const reasons = [];
        let matchCount = 0;
        let mismatchCount = 0;

        const targetResult = checkTarget(product, target);
        reasons.push(targetResult.message);

        if (targetResult.matched) {
            matchCount++;
        } else {
            mismatchCount++;
        }

        const periodResult = checkPeriod(product, period);
        reasons.push(periodResult.message);

        if (periodResult.matched) {
            matchCount++;
        } else {
            mismatchCount++;
        }

        const amountResult = checkAmount(product, amount);
        reasons.push(amountResult.message);

        if (amountResult.matched) {
            matchCount++;
        } else {
            mismatchCount++;
        }

        if (ageGroup) {
            const ageResult = checkAge(product, ageGroup);
            reasons.push(ageResult.message);

            if (ageResult.matched) {
                matchCount++;
            } else {
                mismatchCount++;
            }
        }

        reasons.push(`최고금리 연 ${formatPercent(product.rate)} 상품입니다.`);

        let calcResult;

        if (isForeignProduct(product.type)) {
            calcResult = await calculateForeignProduct(
                product,
                amount,
                period,
                selectedCurrencyCode
            );
        } else {
            calcResult = calculateWonProduct(product, amount, period);
        }

        const conditionResult =
            getConditionStatus(matchCount, mismatchCount, calcResult.calcError);

        return {
            ...product,
            reasons,
            matchCount,
            mismatchCount,

            statusText: conditionResult.text,
            statusClass: conditionResult.className,

            calcType: calcResult.calcType,
            calcError: calcResult.calcError || false,

            beforeTaxInterest: calcResult.beforeTaxInterest,
            afterTaxInterest: calcResult.afterTaxInterest,
            maturityAmount: calcResult.maturityAmount,

            foreignPrincipal: calcResult.foreignPrincipal,
            maturityForeignAmount: calcResult.maturityForeignAmount,

            currencyCode: calcResult.currencyCode || product.currencyCode,
            exchangeRate: calcResult.exchangeRate,

            notice: calcResult.notice,
            shortNotice: calcResult.shortNotice,

            sortInterest: calcResult.calcError ? -1 : Number(calcResult.afterTaxInterest || 0)
        };
    }

    function calculateWonProduct(product, amount, period) {
        if (amount <= 0 || product.rate <= 0 || period <= 0) {
            return {
                calcType: "WON",
                calcError: false,
                beforeTaxInterest: 0,
                afterTaxInterest: 0,
                maturityAmount: amount,
                notice: "원화상품 기준 예상 계산 결과입니다.",
                shortNotice: "원화 계산"
            };
        }

        const beforeTaxInterest = amount * (product.rate / 100) * (period / 12);
        const afterTaxInterest = beforeTaxInterest * 0.846;
        const maturityAmount = amount + afterTaxInterest;

        return {
            calcType: "WON",
            calcError: false,
            beforeTaxInterest,
            afterTaxInterest,
            maturityAmount,
            notice: "원화상품 기준 예상 계산 결과입니다.",
            shortNotice: "원화 계산"
        };
    }

    async function calculateForeignProduct(product, amount, period, selectedCurrencyCode) {
        /*
         * 통화 선택값이 있으면 그 통화 사용
         * 선택값이 없으면 상품 대표 통화 사용
         */
        const currencyCode = selectedCurrencyCode || product.currencyCode;

        if (!currencyCode || currencyCode === "KRW") {
            return {
                calcType: "FOREIGN",
                calcError: true,
                currencyCode: currencyCode || "",
                beforeTaxInterest: 0,
                afterTaxInterest: 0,
                maturityAmount: 0,
                foreignPrincipal: 0,
                maturityForeignAmount: 0,
                exchangeRate: 0,
                notice: "외화 비교 통화를 선택하거나 상품 통화 정보를 확인해주세요.",
                shortNotice: "통화정보 오류"
            };
        }

        try {
            const requestBody = {
                productId: Number(product.id),
                krwAmount: amount,
                currencyCode: currencyCode,
                rate: product.rate,
                periodMonth: period,
                expectedRate: null
            };

            const response = await fetch("/api/products/calc/foreign", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(requestBody)
            });

            if (!response.ok) {
                throw new Error("외화 계산 API 오류");
            }

            const data = await response.json();

            return {
                calcType: "FOREIGN",
                calcError: false,
                currencyCode: data.currencyCode,
                exchangeRate: data.exchangeRate,
                foreignPrincipal: data.foreignPrincipal,
                beforeTaxInterest: data.beforeTaxInterest,
                afterTaxInterest: data.afterTaxInterest,
                maturityForeignAmount: data.maturityForeignAmount,
                maturityAmount: data.maturityKrwAmount,
                notice: data.message || "현재 고시 환율 기준 예상 금액입니다. 실제 원화 수령액은 만기 시 환율 변동에 따라 달라질 수 있습니다.",
                shortNotice: "현재 환율 기준"
            };

        } catch (error) {
            return {
                calcType: "FOREIGN",
                calcError: true,
                currencyCode: currencyCode,
                beforeTaxInterest: 0,
                afterTaxInterest: 0,
                maturityAmount: 0,
                foreignPrincipal: 0,
                maturityForeignAmount: 0,
                exchangeRate: 0,
                notice: "외화 계산 중 오류가 발생했습니다. 환율 정보 또는 계산 API를 확인해주세요.",
                shortNotice: "계산 오류"
            };
        }
    }

    function supportsSelectedCurrency(product, selectedCurrencyCode) {
        if (!selectedCurrencyCode) {
            return true;
        }

        if (!isForeignProduct(product.type)) {
            return true;
        }

        const codes = String(product.currencyCodes || product.currencyCode || "")
            .split(",")
            .map(function (code) {
                return code.trim();
            })
            .filter(function (code) {
                return code !== "";
            });

        return codes.includes(selectedCurrencyCode);
    }

    function checkTarget(product, target) {
        if (product.target === target || product.target === "ALL") {
            return {
                matched: true,
                message: "✔ 가입대상 조건에 적합합니다."
            };
        }

        return {
            matched: false,
            message: "✘ 선택한 가입대상과 상품 대상이 다릅니다."
        };
    }

    function checkPeriod(product, period) {
        if (period <= 0 || product.minPeriod <= 0 || product.maxPeriod <= 0) {
            return {
                matched: true,
                message: "✔ 가입기간 제한을 확인할 수 있습니다."
            };
        }

        if (period >= product.minPeriod && period <= product.maxPeriod) {
            return {
                matched: true,
                message: "✔ 희망 가입기간 조건에 적합합니다."
            };
        }

        return {
            matched: false,
            message: "✘ 희망 가입기간이 상품 가입 가능 기간과 다릅니다."
        };
    }

    function checkAmount(product, amount) {
        if (amount <= 0 || product.minAmount <= 0) {
            return {
                matched: true,
                message: "✔ 가입금액 조건을 확인할 수 있습니다."
            };
        }

        const maxAmountMatched =
            product.maxAmount === 0 ||
            amount <= product.maxAmount;

        if (amount >= product.minAmount && maxAmountMatched) {
            return {
                matched: true,
                message: "✔ 예치 예정 금액으로 가입 가능합니다."
            };
        }

        return {
            matched: false,
            message: "✘ 예치 예정 금액이 가입 가능 범위와 다릅니다."
        };
    }

    function checkAge(product, ageGroup) {
        if (!product.targetDetail || product.targetDetail === "ALL") {
            return {
                matched: true,
                message: "✔ 전체 연령 가입 가능 상품입니다."
            };
        }

        if (product.targetDetail === ageGroup) {
            return {
                matched: true,
                message: "✔ 입력한 나이 조건에 적합합니다."
            };
        }

        return {
            matched: false,
            message: "✘ 입력한 나이 조건과 맞지 않습니다."
        };
    }

    function getConditionStatus(matchCount, mismatchCount, calcError) {
        if (calcError) {
            return {
                text: "계산 확인 필요",
                className: "bad"
            };
        }

        if (mismatchCount === 0) {
            return {
                text: "조건 적합",
                className: "good"
            };
        }

        if (matchCount >= mismatchCount) {
            return {
                text: "일부 확인 필요",
                className: "normal"
            };
        }

        return {
            text: "조건 불일치",
            className: "bad"
        };
    }

    function renderBestInterest(product) {
        if (product.calcError) {
            return `
                <div class="interest-summary">
                    <div>
                        <span>계산 결과</span>
                        <strong>-</strong>
                    </div>
                </div>
            `;
        }

        if (product.calcType === "FOREIGN") {
            const krwInterest = getForeignInterestToKrw(product);

            return `
                <div class="interest-summary">
                    <div>
                        <span>통화</span>
                        <strong>${product.currencyCode || "-"}</strong>
                    </div>

                    <div>
                        <span>외화 세후 예상이자</span>
                        <strong>${formatForeign(product.afterTaxInterest, product.currencyCode)}</strong>
                    </div>

                    <div>
                        <span>세후이자 원화 환산</span>
                        <strong>약 ${formatMoney(krwInterest)}</strong>
                    </div>

                    <div>
                        <span>예상 원화 환산금액</span>
                        <strong>${formatMoney(product.maturityAmount)}</strong>
                    </div>
                </div>
            `;
        }

        return `
            <div class="interest-summary">
                <div>
                    <span>세전 예상이자</span>
                    <strong>${formatMoney(product.beforeTaxInterest)}</strong>
                </div>

                <div>
                    <span>세후 예상이자</span>
                    <strong>${formatMoney(product.afterTaxInterest)}</strong>
                </div>

                <div>
                    <span>예상 만기수령액</span>
                    <strong>${formatMoney(product.maturityAmount)}</strong>
                </div>
            </div>
        `;
    }

    function renderInterestCell(product) {
        if (product.calcError) {
            return "-";
        }

        if (product.calcType === "FOREIGN") {
            const krwInterest = getForeignInterestToKrw(product);

            return `
                ${formatForeign(product.afterTaxInterest, product.currencyCode)}
                <br>
                <small>약 ${formatMoney(krwInterest)}</small>
            `;
        }

        return formatMoney(product.afterTaxInterest);
    }

    function renderMaturityCell(product) {
        if (product.calcError) {
            return "-";
        }

        if (product.calcType === "FOREIGN") {
            return `
                ${formatMoney(product.maturityAmount)}
                <br>
                <small>원화 환산</small>
            `;
        }

        return formatMoney(product.maturityAmount);
    }

    function getForeignInterestToKrw(product) {
        const exchangeRate = Number(product.exchangeRate || 0);
        const foreignInterest = Number(product.afterTaxInterest || 0);

        if (exchangeRate <= 0 || foreignInterest <= 0) {
            return 0;
        }

        return foreignInterest * exchangeRate;
    }

    function isForeignProduct(productType) {
        return productType === "FOREIGN_DEP" || productType === "FOREIGN_SAV";
    }

    function formatMoney(value) {
        const numberValue = Number(value || 0);

        return Math.floor(numberValue).toLocaleString("ko-KR") + "원";
    }

    function formatForeign(value, currencyCode) {
        const numberValue = Number(value || 0);

        if (!currencyCode) {
            return "-";
        }

        return numberValue.toLocaleString("ko-KR", {
            minimumFractionDigits: 2,
            maximumFractionDigits: 2
        }) + " " + currencyCode;
    }

    function formatPercent(value) {
        const numberValue = Number(value || 0);

        return numberValue.toLocaleString("ko-KR", {
            minimumFractionDigits: 0,
            maximumFractionDigits: 2
        }) + "%";
    }

    function convertAgeToTargetDetail(age) {
        if (age <= 0) {
            return "";
        }

        if (age <= 18) {
            return "CHILD";
        }

        if (age <= 34) {
            return "YOUTH";
        }

        if (age <= 59) {
            return "ADULT";
        }

        return "SENIOR";
    }

    function convertType(type) {
        if (type === "DEPOSIT") {
            return "예금";
        }

        if (type === "SAVINGS") {
            return "적금";
        }

        if (type === "FOREIGN_DEP") {
            return "외화예금";
        }

        if (type === "FOREIGN_SAV") {
            return "외화적금";
        }

        if (type === "CORP_DEP") {
            return "기업예금";
        }

        if (type === "CORP_SAV") {
            return "기업적금";
        }

        return type || "-";
    }

    function convertTarget(target) {
        if (target === "ALL") {
            return "전체고객";
        }

        if (target === "PERSONAL") {
            return "개인";
        }

        if (target === "COMPANY") {
            return "기업";
        }

        return target || "-";
    }

    function escapeHtml(value) {
        return String(value || "")
            .replaceAll("&", "&amp;")
            .replaceAll("<", "&lt;")
            .replaceAll(">", "&gt;")
            .replaceAll('"', "&quot;")
            .replaceAll("'", "&#039;");
    }
});