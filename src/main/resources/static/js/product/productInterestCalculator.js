document.addEventListener("DOMContentLoaded", function () {
    const calcBtn = document.getElementById("detailCalcBtn");
    const resultBox = document.getElementById("detailCalcResult");

    if (!calcBtn || !resultBox) {
        return;
    }

    calcBtn.addEventListener("click", function () {
        const amount = Number(document.getElementById("detailAmount").value || 0);
        const period = Number(document.getElementById("detailPeriod").value || 0);
        const rate = Number(document.getElementById("detailRate").value || 0);

        const productType = document.getElementById("detailProductType")?.value;
        const productId = Number(document.getElementById("detailProductId")?.value || 0);
        const currencyCode = document.getElementById("detailCurrencyCode")?.value;

        if (amount <= 0 || period <= 0 || rate <= 0) {
            alert("금액, 기간, 금리를 입력해주세요.");
            return;
        }

        if (isForeignProduct(productType)) {
            calculateForeignInterest({
                productId: productId,
                krwAmount: amount,
                currencyCode: currencyCode,
                rate: rate,
                periodMonth: period
            });
            return;
        }

        calculateWonInterest(amount, period, rate);
    });

    function isForeignProduct(productType) {
        return productType === "FOREIGN_DEP" || productType === "FOREIGN_SAV";
    }

    function calculateWonInterest(amount, period, rate) {
        const beforeTaxInterest =
            amount * (rate / 100) * (period / 12);

        const afterTaxInterest =
            beforeTaxInterest * 0.846;

        const maturityAmount =
            amount + afterTaxInterest;

        resultBox.innerHTML = `
            <div class="interest-summary">

                <div>
                    <span>세전 예상이자</span>
                    <strong>${formatMoney(beforeTaxInterest)}</strong>
                </div>

                <div>
                    <span>세후 예상이자</span>
                    <strong>${formatMoney(afterTaxInterest)}</strong>
                </div>

                <div>
                    <span>예상 만기수령액</span>
                    <strong>${formatMoney(maturityAmount)}</strong>
                </div>

                <p class="interest-notice">
                    원화상품 기준 예상 계산 결과입니다.
                </p>

            </div>
        `;
    }

    function calculateForeignInterest(requestData) {
        if (!requestData.currencyCode) {
            alert("외화상품의 통화 정보가 없습니다.");
            return;
        }

        fetch("/api/products/calc/foreign", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(requestData)
        })
            .then(function (response) {
                if (!response.ok) {
                    throw new Error("외화 계산 요청 실패");
                }

                return response.json();
            })
            .then(function (data) {
                resultBox.innerHTML = `
                    <div class="interest-summary">

                        <div>
                            <span>통화</span>
                            <strong>${data.currencyCode}</strong>
                        </div>

                        <div>
                            <span>적용 환율</span>
                            <strong>${formatRate(data.exchangeRate)}</strong>
                        </div>

                        <div>
                            <span>외화 환산 원금</span>
                            <strong>${formatForeign(data.foreignPrincipal, data.currencyCode)}</strong>
                        </div>

                        <div>
                            <span>외화 세전 예상이자</span>
                            <strong>${formatForeign(data.beforeTaxInterest, data.currencyCode)}</strong>
                        </div>

                        <div>
                            <span>외화 세후 예상이자</span>
                            <strong>${formatForeign(data.afterTaxInterest, data.currencyCode)}</strong>
                        </div>

                        <div>
                            <span>만기 예상 외화금액</span>
                            <strong>${formatForeign(data.maturityForeignAmount, data.currencyCode)}</strong>
                        </div>

                        <div>
                            <span>예상 원화 환산금액</span>
                            <strong>${formatMoney(data.maturityKrwAmount)}</strong>
                        </div>

                        <p class="interest-notice">
                            ${data.message}
                        </p>

                    </div>
                `;
            })
            .catch(function () {
                alert("외화 예상 이자 계산 중 오류가 발생했습니다.");
            });
    }

    function formatMoney(value) {
        return Math.floor(Number(value))
            .toLocaleString("ko-KR") + "원";
    }

    function formatRate(value) {
        return Number(value).toLocaleString("ko-KR", {
            minimumFractionDigits: 2,
            maximumFractionDigits: 4
        }) + "원";
    }

    function formatForeign(value, currencyCode) {
        return Number(value).toLocaleString("ko-KR", {
            minimumFractionDigits: 2,
            maximumFractionDigits: 2
        }) + " " + currencyCode;
    }
});