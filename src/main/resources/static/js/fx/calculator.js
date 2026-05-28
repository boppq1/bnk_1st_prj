/* =========================================================
   환전 계산기 JS
   ========================================================= */

(function() {
    "use strict";

    /* ---------- 요소 캐싱 ---------- */
    const el = {
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

    // 조회 버튼 - async
    el.searchBtn.addEventListener("click", async () => {
        const input = collectInput();
        await fetchAndRender(input);
    });

    /* =========================================================
       2) 입력값 수집
       ========================================================= */
    function collectInput() {
        return {
            convStd: el.convStd().value,             // "foreign" | "krw"
            rateStd: el.rateStd().value,             // "notice"  | "manual"
            cashType: el.cashType().value,            // "cash"
            buySell: el.buySell().value,             // "buy"     | "sell"
            currency: selectedCurrency,               // { code, name }
            baseDate: el.baseDate.value,              // "2026.05.06"
            preferRate: Number(el.preferRate.value),    // 0 ~ 90 (%)
            manualRate: onlyNumber(el.manualRate.value),
            amount: onlyNumber(el.amountInput.value),
        };
    }

    /* =========================================================
       3) API 호출 + 계산 + 렌더링
       ========================================================= */
    async function fetchAndRender(input) {

        // 날짜 포맷 변환: "2026.05.06" → "2026-05-06"
        const date = input.baseDate.replace(/\./g, "-");

        // HTML 통화코드 → DB curNm 매핑
        const curNmMap = { JPY: "JPY(100)", IDR: "IDR(100)" };
        const curNm = curNmMap[input.currency.code] || input.currency.code;

        const url = `/api/exchange/rate`
            + `?curNm=${curNm}`
            + `&date=${date}`
            + `&buySell=${input.buySell}`
            + `&prefer=${input.preferRate}`;

        try {
            const res = await fetch(url);
            if (!res.ok) throw new Error(res.status);
            const data = await res.json();

            // data = {
            //   curNm, baseDate,
            //   dealBasR,       매매기준율
            //   noticeRate,     고시환율 (살때 tts / 팔때 ttb)
            //   spread,         스프레드
            //   preferDiscount, 우대 절감액
            //   appliedRate     최종 적용환율
            // }

            const amount = input.amount;
            const isBuy = input.buySell === "buy";
            const appRate = data.appliedRate;

            // 살때: KRW 지급 → 외화 수취
            // 팔때: 외화 지급 → KRW 수취
            const payAmount = isBuy ? Math.round(amount * appRate) : amount;
            const recvAmount = isBuy ? amount : Math.round(amount * appRate);
            const saved = Math.round(data.preferDiscount * amount);

            const result = {
                marketRate: data.dealBasR,
                noticeRate: data.noticeRate,
                appliedRate: data.noticeRate,   // 우대 전 고시환율
                preferredRate: data.appliedRate,  // 우대 후 적용환율
                total: isBuy ? payAmount : recvAmount,
                saved: saved,
                payCurrency: isBuy ? "KRW" : input.currency.code,
                recvCurrency: isBuy ? input.currency.code : "KRW",
                payAmount: payAmount,
                recvAmount: recvAmount,
            };

            renderResult(input, result);

        } catch (err) {
            console.error("환율 조회 실패", err);
            alert("환율 데이터를 불러오지 못했습니다. 잠시 후 다시 시도해주세요.");
        }
    }

    /* =========================================================
       4) 결과 렌더링 (계산 결과 → 화면)
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

    /* =========================================================
       달력 (기준일시 선택)
       ========================================================= */
    (function initCalendar() {

        let viewYear, viewMonth, selectedDate = null;
        const today = new Date();
        viewYear = today.getFullYear();
        viewMonth = today.getMonth();

        const btn = document.getElementById("calBtn");
        const popup = document.getElementById("calPopup");
        const display = document.getElementById("baseDate");  // 기준일시 input에 바로 반영
        const prevBtn = document.getElementById("calPrev");
        const nextBtn = document.getElementById("calNext");
        const mlabel = document.getElementById("calMonthYear");
        const daysEl = document.getElementById("calDays");

        // 팝업 토글
        btn.addEventListener("click", (e) => {
            e.stopPropagation();
            popup.classList.toggle("open");
            if (popup.classList.contains("open")) renderCal();
        });
        document.addEventListener("click", (e) => {
            if (!popup.contains(e.target) && e.target !== btn)
                popup.classList.remove("open");
        });

        // 이전/다음 달
        prevBtn.addEventListener("click", () => {
            viewMonth--;
            if (viewMonth < 0) { viewMonth = 11; viewYear--; }
            renderCal();
        });
        nextBtn.addEventListener("click", () => {
            viewMonth++;
            if (viewMonth > 11) { viewMonth = 0; viewYear++; }
            renderCal();
        });

        function renderCal() {
            const months = ["1월", "2월", "3월", "4월", "5월", "6월",
                "7월", "8월", "9월", "10월", "11월", "12월"];
            mlabel.textContent = `${viewYear}년 ${months[viewMonth]}`;
            daysEl.innerHTML = "";

            const firstDay = new Date(viewYear, viewMonth, 1).getDay();
            const lastDate = new Date(viewYear, viewMonth + 1, 0).getDate();
            const prevLast = new Date(viewYear, viewMonth, 0).getDate();

            for (let i = firstDay - 1;i >= 0;i--)
                daysEl.appendChild(makeDay(prevLast - i, true, false, null));

            for (let d = 1;d <= lastDate;d++) {
                const date = new Date(viewYear, viewMonth, d);
                const isTd = isSameDay(date, today);
                const isSel = selectedDate && isSameDay(date, selectedDate);
                // 오늘 이후 날짜는 선택 불가
                const isFuture = date > today;
                daysEl.appendChild(makeDay(d, false, isTd, date, isSel, isFuture));
            }

            const total = firstDay + lastDate;
            const remain = total % 7 === 0 ? 0 : 7 - (total % 7);
            for (let i = 1;i <= remain;i++)
                daysEl.appendChild(makeDay(i, true, false, null));
        }

        function makeDay(num, otherMonth, isToday, dateObj, isSelected, isFuture) {
            const btn = document.createElement("button");
            btn.type = "button";
            btn.textContent = num;
            btn.className = "cal-day";

            if (otherMonth) btn.classList.add("other-month");
            if (isToday) btn.classList.add("today");
            if (isSelected) btn.classList.add("selected");
            if (isFuture) btn.disabled = true;

            if (!otherMonth && dateObj && !isFuture) {
                const col = dateObj.getDay();
                if (col === 0) btn.classList.add("sun");
                if (col === 6) btn.classList.add("sat");
                btn.addEventListener("click", () => selectDate(dateObj));
            } else {
                btn.disabled = true;
            }
            return btn;
        }

        function selectDate(date) {
            selectedDate = date;
            // "2026.05.27" 형식으로 baseDate input에 반영
            display.value = formatDate(date);
            popup.classList.remove("open");
            renderCal();
        }

        function formatDate(date) {
            const y = date.getFullYear();
            const m = String(date.getMonth() + 1).padStart(2, "0");
            const d = String(date.getDate()).padStart(2, "0");
            return `${y}.${m}.${d}`;
        }

        function isSameDay(a, b) {
            return a.getFullYear() === b.getFullYear()
                && a.getMonth() === b.getMonth()
                && a.getDate() === b.getDate();
        }

        renderCal();
    })();

})();