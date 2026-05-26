/* =========================================================
   환전 계산기 JS
   ---------------------------------------------------------
   - UI 동작(입력 동기화 / 통화 선택 / 빠른금액 / 정정)은 구현됨
   - 실제 환전 "계산 로직"은 calculateExchange() 안에 직접 작성
   - 계산 결과를 화면에 표시하는 부분은 renderResult() 에 매핑됨
   ========================================================= */

(function() {
    "use strict";

    /* ---------- 요소 캐싱 ---------- */
    const el = {
        // 입력
        convStd: () => document.querySelector('input[name="convStd"]:checked'),
        rateStd: () => document.querySelector('input[name="rateStd"]:checked'),
        cashType: () => document.querySelector('input[name="cashType"]:checked'),
        buySell: () => document.querySelector('input[name="buySell"]:checked'),
        baseDate: document.getElementById("baseDate"),
        preferRate: document.getElementById("preferRate"),
        manualRate: document.getElementById("manualRate"),
        manualRateUnit: document.getElementById("manualRateUnit"),
        amountInput: document.getElementById("amountInput"),
        amountRange: document.getElementById("amountRange"),
        amountUnit: document.getElementById("amountUnit"),
        currencyList: document.getElementById("currencyList"),
        searchBtn: document.getElementById("searchBtn"),
        resetBtn: document.getElementById("resetBtn"),
        // 결과
        resultBox: document.getElementById("resultBox"),
    };

    /* ---------- 현재 선택 통화 ---------- */
    let selectedCurrency = { code: "USD", name: "미국 달러" };

    /* =========================================================
       1) UI 이벤트 바인딩
       ========================================================= */

    // 통화 목록 선택
    el.currencyList.addEventListener("click", (e) => {
        const li = e.target.closest(".cur-item");
        if (!li) return;
        el.currencyList.querySelectorAll(".cur-item")
            .forEach((it) => it.classList.remove("selected"));
        li.classList.add("selected");
        selectedCurrency = { code: li.dataset.code, name: li.dataset.name };
        el.amountUnit.textContent = li.dataset.code;
    });

    // 금액 입력 ↔ 슬라이더 동기화
    el.amountInput.addEventListener("input", () => {
        const v = onlyNumber(el.amountInput.value);
        el.amountInput.value = formatNumber(v);
        el.amountRange.value = Math.min(v, Number(el.amountRange.max));
    });
    el.amountRange.addEventListener("input", () => {
        el.amountInput.value = formatNumber(Number(el.amountRange.value));
    });

    // 빠른 금액 버튼 (10/100/500/1000 누적)
    document.querySelectorAll(".qbtn[data-add]").forEach((btn) => {
        btn.addEventListener("click", () => {
            const cur = onlyNumber(el.amountInput.value);
            const next = cur + Number(btn.dataset.add);
            el.amountInput.value = formatNumber(next);
            el.amountRange.value = Math.min(next, Number(el.amountRange.max));
        });
    });

    // 정정(초기화)
    el.resetBtn.addEventListener("click", () => {
        el.amountInput.value = "0";
        el.amountRange.value = "0";
    });

    // 적용환율 직접입력 라디오 활성/비활성
    document.querySelectorAll('input[name="rateStd"]').forEach((r) => {
        r.addEventListener("change", () => {
            const manual = el.rateStd().value === "manual";
            el.manualRate.disabled = !manual;
            el.manualRateUnit.disabled = !manual;
            el.preferRate.disabled = manual;
        });
    });

    // 조회 버튼
    el.searchBtn.addEventListener("click", () => {
        const input = collectInput();
        const result = calculateExchange(input);   // ← 직접 작성할 로직
        renderResult(input, result);
    });

    /* =========================================================
       2) 입력값 수집
       ========================================================= */
    function collectInput() {
        return {
            convStd: el.convStd().value,            // "foreign" | "krw"
            rateStd: el.rateStd().value,            // "notice" | "manual"
            cashType: el.cashType().value,           // "cash"
            buySell: el.buySell().value,            // "buy" | "sell"
            currency: selectedCurrency,              // { code, name }
            baseDate: el.baseDate.value,
            preferRate: Number(el.preferRate.value),  // 0 ~ 90 (%)
            manualRate: onlyNumber(el.manualRate.value),
            amount: onlyNumber(el.amountInput.value),
        };
    }

    /* =========================================================
       3) ★ 환전 계산 로직 (여기를 직접 작성하세요) ★
       ---------------------------------------------------------
       input 으로 위 collectInput() 값이 들어옵니다.
       아래 객체의 각 필드를 채워서 return 하면
       renderResult() 가 화면에 자동으로 표시합니다.
       (지금은 화면 확인용 더미/0 값으로 채워둠)
       ========================================================= */
    function calculateExchange(input) {
        // TODO: 실제 환율/우대율/수수료 계산 구현
        // 예) const marketRate = fetchRate(input.currency.code);
        //     const applied   = input.buySell === "buy" ? ... : ...;

        return {
            marketRate: 0,   // 매매기준율
            noticeRate: 0,   // 고시환율
            appliedRate: 0,   // 적용환율(우대 전)
            preferredRate: 0,   // 우대 적용환율
            total: 0,   // 환전 예상 금액(최종)
            saved: 0,   // 우대 절감액
            payCurrency: "",  // 지급 통화
            recvCurrency: "",  // 수취 통화
            payAmount: 0,   // 지급 금액
            recvAmount: 0,   // 수취 금액
        };
    }

    /* =========================================================
       4) 결과 렌더링 (계산 결과 → 화면)
       ---------------------------------------------------------
       필요 시 표시 형식만 조정하면 됩니다.
       ========================================================= */
    function renderResult(input, r) {
        const cc = input.currency.code;

        setText("r_currency", `${input.currency.name} (${cc})`);
        setText("r_buySell", input.buySell === "buy" ? "외화 살 때" : "외화 팔 때");
        setText("r_total", formatNumber(r.total));

        setText("r_convStd", input.convStd === "foreign" ? "외화기준" : "원화기준");
        setText("r_cashType", "외화현찰");
        setText("r_baseDate", input.baseDate);
        setText("r_prefer", input.preferRate ? `${input.preferRate}%` : "없음");

        setText("r_marketRate", formatNumber(r.marketRate));
        setText("r_noticeRate", formatNumber(r.noticeRate));
        setText("r_appliedRate", formatNumber(r.appliedRate));
        setText("r_preferredRate", formatNumber(r.preferredRate));

        setText("r_inputAmount", `${formatNumber(input.amount)} ${input.convStd === "foreign" ? cc : "KRW"}`);
        setText("r_saved", `${formatNumber(r.saved)} KRW`);

        setText("r_payCurrency", r.payCurrency || "-");
        setText("r_recvCurrency", r.recvCurrency || "-");
        setText("r_payAmount", formatNumber(r.payAmount));
        setText("r_recvAmount", formatNumber(r.recvAmount));

        el.resultBox.hidden = false;
        el.resultBox.scrollIntoView({ behavior: "smooth", block: "start" });
    }

    /* =========================================================
       5) 유틸
       ========================================================= */
    function onlyNumber(str) {
        const n = Number(String(str).replace(/[^0-9.]/g, ""));
        return isNaN(n) ? 0 : n;
    }
    function formatNumber(n) {
        return Number(n).toLocaleString("ko-KR");
    }
    function setText(id, value) {
        const node = document.getElementById(id);
        if (node) node.textContent = value;
    }
})();