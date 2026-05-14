document.addEventListener("DOMContentLoaded", function () {
    // 상세페이지가 모두 로드된 뒤 실행된다.

    const calcBtn = document.getElementById("detailCalcBtn");
    // 계산하기 버튼 요소를 가져온다.

    const resultBox = document.getElementById("detailCalcResult");
    // 계산 결과를 출력할 영역을 가져온다.

    if (!calcBtn) return;
    // 버튼이 없으면 실행하지 않는다.

    calcBtn.addEventListener("click", function () {
        // 계산하기 버튼 클릭 시 실행된다.

        const amount =
            Number(document.getElementById("detailAmount").value || 0);
        // 사용자가 입력한 예치 금액을 숫자로 변환한다.

        const period =
            Number(document.getElementById("detailPeriod").value || 0);
        // 사용자가 입력한 가입 기간을 숫자로 변환한다.

        const rate =
            Number(document.getElementById("detailRate").value || 0);
        // 적용 금리를 숫자로 변환한다.

        if (amount <= 0 || period <= 0 || rate <= 0) {
            // 입력값이 올바르지 않으면

            alert("금액, 기간, 금리를 입력해주세요.");
            // 안내 메시지를 출력한다.

            return;
            // 함수 실행을 중단한다.
        }

        const beforeTaxInterest =
            amount * (rate / 100) * (period / 12);
        // 세전 예상이자를 계산한다.

        const afterTaxInterest =
            beforeTaxInterest * 0.846;
        // 일반과세 15.4%를 제외한 세후 이자를 계산한다.

        const maturityAmount =
            amount + afterTaxInterest;
        // 원금 + 세후이자를 더해 예상 만기수령액을 계산한다.

        resultBox.innerHTML = `
            <div class="interest-summary">

                <div>
                    <span>세전 예상이자</span>

                    <strong>
                        ${formatMoney(beforeTaxInterest)}
                    </strong>
                </div>

                <div>
                    <span>세후 예상이자</span>

                    <strong>
                        ${formatMoney(afterTaxInterest)}
                    </strong>
                </div>

                <div>
                    <span>예상 만기수령액</span>

                    <strong>
                        ${formatMoney(maturityAmount)}
                    </strong>
                </div>

            </div>
        `;
        // 계산 결과를 HTML 형태로 출력한다.
    });

    function formatMoney(value) {
        // 숫자를 원화 형식으로 변환하는 함수이다.

        return Math.floor(value)
            .toLocaleString("ko-KR") + "원";
        // 천 단위 콤마와 원 단위를 추가한다.
    }

});


