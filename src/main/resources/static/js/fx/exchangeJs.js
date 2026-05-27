/* ============================================================
   BNK 부산은행 외화 환전 시스템  (/js/fx/exchange.js)
============================================================ */
(function () {
  'use strict';

  /* ── 통화 메타 (국기 / 한글명). 백엔드에 없는 표시용 데이터 ── */
  var META = {
    'USD':{f:'🇺🇸',n:'미국 달러'}, 'JPY(100)':{f:'🇯🇵',n:'일본 엔화'},
    'EUR':{f:'🇪🇺',n:'유럽 유로'}, 'CNH':{f:'🇨🇳',n:'중국 위안'},
    'GBP':{f:'🇬🇧',n:'영국 파운드'}, 'AUD':{f:'🇦🇺',n:'호주 달러'},
    'HKD':{f:'🇭🇰',n:'홍콩 달러'}, 'SGD':{f:'🇸🇬',n:'싱가포르 달러'},
    'CHF':{f:'🇨🇭',n:'스위스 프랑'}, 'THB':{f:'🇹🇭',n:'태국 바트'},
    'CAD':{f:'🇨🇦',n:'캐나다 달러'}, 'MYR':{f:'🇲🇾',n:'말레이시아 링기트'},
    'AED':{f:'🇦🇪',n:'아랍에미리트 디르함'}, 'BHD':{f:'🇧🇭',n:'바레인 디나르'},
    'DKK':{f:'🇩🇰',n:'덴마크 크로네'}, 'IDR(100)':{f:'🇮🇩',n:'인도네시아 루피아'},
    'KWD':{f:'🇰🇼',n:'쿠웨이트 디나르'}, 'NOK':{f:'🇳🇴',n:'노르웨이 크로네'},
    'NZD':{f:'🇳🇿',n:'뉴질랜드 달러'}, 'SAR':{f:'🇸🇦',n:'사우디 리얄'},
    'SEK':{f:'🇸🇪',n:'스웨덴 크로나'}, 'BND':{f:'🇧🇳',n:'브루나이 달러'}
  };

  /* ── 상태 ── */
  var RATES = {};       // { curCd: {buyRate, sellRate, baseRate} }
  var dealType = 'BUY'; // BUY | SELL
  var curCd = 'USD';

  /* ── DOM ── */
  var $ = function (id) { return document.getElementById(id); };
  var sel, amountEl, submitEl, modal;

  /* ── 유틸 ── */
  function unitOf(c) { return (c && c.indexOf('(100)') > -1) ? 100 : 1; }
  function meta(c) { return META[c] || { f:'🏳️', n:c }; }
  function nf(n) {
    return Math.round(n).toLocaleString('ko-KR');
  }
  function nf2(n) {
    return (Math.round(n * 100) / 100).toLocaleString('en-US',
      { minimumFractionDigits: 2, maximumFractionDigits: 2 });
  }

  /* ── 환율 로딩 ── */
  function loadRates() {
    fetch('/fx/rates', { credentials: 'same-origin' })
      .then(function (r) {
        if (!r.ok) throw new Error('rate load failed');
        return r.json();
      })
      .then(function (list) {
        if (!list || !list.length) {
          alert('환율 정보가 없습니다. 잠시 후 다시 시도해 주세요.');
          return;
        }
        var latestDate = '';
        list.forEach(function (row) {
          RATES[row.curCd] = {
            buyRate: row.buyRate,
            sellRate: row.sellRate,
            baseRate: row.baseRate
          };
          if (row.ntcDt && row.ntcDt > latestDate) latestDate = row.ntcDt;
        });
        if (latestDate) {
          $('fxRateDate').textContent = '고시일 ' + latestDate.substring(0, 10);
        }
        buildCurrencyOptions();
        buildCurrencyList();
        applyCurrency(RATES['USD'] ? 'USD' : Object.keys(RATES)[0]);
      })
      .catch(function () {
        alert('환율 정보를 불러오지 못했습니다. 잠시 후 다시 시도해 주세요.');
      });
  }

  /* ── 셀렉트 옵션 구성 ── */
  function buildCurrencyOptions() {
    sel.innerHTML = '';
    Object.keys(RATES).forEach(function (c) {
      var o = document.createElement('option');
      o.value = c;
      o.textContent = c + ' — ' + meta(c).n;
      sel.appendChild(o);
    });
  }

  /* ── 우측 통화 리스트 ── */
  function buildCurrencyList() {
    var ul = $('fxCurList');
    ul.innerHTML = '';
    Object.keys(RATES).forEach(function (c) {
      var m = meta(c);
      var li = document.createElement('li');
      li.className = 'fx-cur-item' + (c === curCd ? ' active' : '');
      li.dataset.cur = c;
      li.innerHTML =
        '<span class="ci-flag">' + m.f + '</span>' +
        '<div class="ci-info"><div class="ci-name">' + m.n + '</div>' +
        '<div class="ci-rate">기준 ' + nf(RATES[c].baseRate) + ' KRW</div></div>' +
        '<span class="ci-code">' + c + '</span>';
      li.addEventListener('click', function () { applyCurrency(c); });
      ul.appendChild(li);
    });
  }

  /* ── 통화 적용 ── */
  function applyCurrency(c) {
    if (!RATES[c]) return;
    curCd = c;
    var m = meta(c);
    sel.value = c;
    $('fxFlag').textContent = m.f;
    $('fxCurName').textContent = m.n;
    $('fxUnit').textContent = c;
    // 리스트 active 갱신
    var items = document.querySelectorAll('.fx-cur-item');
    for (var i = 0; i < items.length; i++) {
      items[i].classList.toggle('active', items[i].dataset.cur === c);
    }
    refreshBalance();
    recalc();
  }

  /* ── 거래 구분 전환 ── */
  function applyDeal(type) {
    dealType = type;
    var tabs = document.querySelectorAll('.fx-deal-tab');
    for (var i = 0; i < tabs.length; i++) {
      tabs[i].classList.toggle('active', tabs[i].dataset.deal === type);
    }
    if (type === 'BUY') {
      $('fxDealDesc').textContent = '원화로 외화를 구매합니다. 매수 환율이 적용됩니다.';
      $('fxRateBadge').textContent = '매수 환율';
      $('fxEstLabel').textContent = '필요 원화';
      $('fxAmountLabel').textContent = '환전 금액 (외화 기준)';
    } else {
      $('fxDealDesc').textContent = '보유 외화를 원화로 환전합니다. 매도 환율이 적용됩니다.';
      $('fxRateBadge').textContent = '매도 환율';
      $('fxEstLabel').textContent = '받을 원화';
      $('fxAmountLabel').textContent = '환전 금액 (외화 기준)';
    }
    refreshBalance();
    recalc();
  }

  /* ── 보유 잔액 안내 (매도일 때만 표시) ── */
  function refreshBalance() {
    var wrap = $('fxBalanceWrap');
    if (dealType !== 'SELL') { wrap.hidden = true; return; }
    wrap.hidden = false;
    $('fxBalanceTxt').textContent = curCd + ' 보유 잔액은 환전 신청 시 확인됩니다.';
  }

  /* ── 환율 / 예상금액 계산 ── */
  function curRate() {
    var r = RATES[curCd];
    if (!r) return 0;
    return dealType === 'BUY' ? r.buyRate : r.sellRate;
  }

  function recalc() {
    var rate = curRate();
    var unit = unitOf(curCd);
    // 환율 표시 (단위 통화는 100단위 그대로 표기)
    $('fxRateVal').textContent =
      '1 ' + curCd.replace('(100)', '') +
      (unit === 100 ? ' (100단위)' : '') +
      ' = ' + nf(rate) + ' KRW';

    var amt = parseFloat((amountEl.value || '').replace(/,/g, '')) || 0;
    var krw = amt * rate / unit;
    $('fxEstVal').textContent = amt > 0 ? nf(krw) + ' KRW' : '– KRW';
    submitEl.disabled = (amt <= 0);
  }

  /* ── 확인 모달 ── */
  function openModal() {
    var amt = parseFloat((amountEl.value || '').replace(/,/g, '')) || 0;
    if (amt <= 0) return;
    var rate = curRate();
    var unit = unitOf(curCd);
    var krw = Math.round(amt * rate / unit);

    var msg, detail;
    if (dealType === 'BUY') {
      msg = nf(krw) + ' KRW로 ' + nf2(amt) + ' ' + curCd + '를 구매합니다.';
      detail =
        '<div class="md-row"><span>출금 (원화)</span><b>' + nf(krw) + ' KRW</b></div>' +
        '<div class="md-row"><span>입금 (외화)</span><b>' + nf2(amt) + ' ' + curCd + '</b></div>' +
        '<div class="md-row"><span>적용 환율</span><b>' + nf(rate) + ' KRW</b></div>';
    } else {
      msg = nf2(amt) + ' ' + curCd + '를 ' + nf(krw) + ' KRW로 환전합니다.';
      detail =
        '<div class="md-row"><span>출금 (외화)</span><b>' + nf2(amt) + ' ' + curCd + '</b></div>' +
        '<div class="md-row"><span>입금 (원화)</span><b>' + nf(krw) + ' KRW</b></div>' +
        '<div class="md-row"><span>적용 환율</span><b>' + nf(rate) + ' KRW</b></div>';
    }
    $('fxModalMsg').textContent = msg;
    $('fxModalDetail').innerHTML = detail;
    modal.hidden = false;
  }

  function closeModal() { modal.hidden = true; }

  /* ── 환전 요청 ── */
  function submitExchange() {
    var amt = parseFloat((amountEl.value || '').replace(/,/g, '')) || 0;
    if (amt <= 0) return;
    var confirmBtn = $('fxModalConfirm');
    confirmBtn.disabled = true;
    confirmBtn.textContent = '처리 중…';

    fetch('/fx/exchange', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      credentials: 'same-origin',
      body: JSON.stringify({ dealType: dealType, currency: curCd, amount: amt })
    })
      .then(function (r) { return r.json(); })
      .then(function (res) {
        closeModal();
        if (!res.success) {
          alert(res.message || '환전에 실패했습니다.');
          return;
        }
        showResult(res);
        amountEl.value = '';
        recalc();
      })
      .catch(function () {
        closeModal();
        alert('환전 처리 중 오류가 발생했습니다. 다시 시도해 주세요.');
      })
      .finally(function () {
        confirmBtn.disabled = false;
        confirmBtn.textContent = '환전하기';
      });
  }

  /* ── 결과 카드 ── */
  function showResult(res) {
    var card = $('fxResultCard');
    var isBuy = res.dealType === 'BUY';
    if (isBuy) {
      $('fxResFrom').textContent = '- ' + nf(res.krwAmount) + ' KRW';
      $('fxResTo').textContent = '+ ' + nf2(res.foreignAmount) + ' ' + res.currency;
      $('fxResType').textContent = '원화 → 외화';
    } else {
      $('fxResFrom').textContent = '- ' + nf2(res.foreignAmount) + ' ' + res.currency;
      $('fxResTo').textContent = '+ ' + nf(res.krwAmount) + ' KRW';
      $('fxResType').textContent = '외화 → 원화';
    }
    $('fxResRate').textContent = nf(res.appliedRate) + ' KRW';
    $('fxResultTime').textContent = res.exchCpltDt || '';
    card.hidden = false;
    card.scrollIntoView({ behavior: 'smooth', block: 'nearest' });
  }

  /* ── 초기화 ── */
  function init() {
    sel = $('fxCurrency');
    amountEl = $('fxAmount');
    submitEl = $('fxSubmit');
    modal = $('fxModal');

    // 탭
    var tabs = document.querySelectorAll('.fx-deal-tab');
    for (var i = 0; i < tabs.length; i++) {
      tabs[i].addEventListener('click', function () { applyDeal(this.dataset.deal); });
    }
    // 셀렉트
    sel.addEventListener('change', function () { applyCurrency(this.value); });
    // 금액 입력
    amountEl.addEventListener('input', recalc);
    // 빠른 금액
    var quicks = document.querySelectorAll('.fx-quick button');
    for (var j = 0; j < quicks.length; j++) {
      quicks[j].addEventListener('click', function () {
        amountEl.value = this.dataset.q;
        recalc();
      });
    }
    // 신청 → 모달
    submitEl.addEventListener('click', openModal);
    $('fxModalCancel').addEventListener('click', closeModal);
    $('fxModalConfirm').addEventListener('click', submitExchange);
    modal.addEventListener('click', function (e) {
      if (e.target === modal) closeModal();
    });

    loadRates();
  }

  if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', init);
  } else {
    init();
  }
})();