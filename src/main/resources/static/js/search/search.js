/*
 * BNKSearch — BNK 부산은행 통합 검색 모듈  |  search.js
 *
 * [사용법]
 *   BNKSearch.init({ triggerSelector: '.search-trigger' });
 *
 * [API 엔드포인트]
 *   GET /api/search/suggested          → SuggestedSearchDto[] (전체, 인기순)
 *   GET /api/search/suggested?q=외환   → SuggestedSearchDto[] (키워드 필터)
 *
 * [SuggestedSearchDto]
 *   suggestedNo  : number
 *   roleType     : "ROLE_COMPANY" | "ROLE_PERSONAL"
 *   keyword      : string
 *   searchVolume : number
 *   pageUrl      : string   (ex. "/exchange/main")
 *   category     : string   (ex. "외환")  — 선택사항, 뱃지 표시용
 */
const BNKSearch = (() => {

    /* ── 설정 ──────────────────────────────────── */
    const API_BASE      = '/api/search/suggested';
    const DEBOUNCE_MS   = 220;
    const MAX_RESULTS   = 8;

    /* 탭 정의: id, label, roleType(null=전체) */
    const TABS = [
        { id: 'all',     label: '전체',   roleType: null },
        { id: 'company', label: '기업',   roleType: 'ROLE_COMPANY' },
        { id: 'personal',label: '개인',   roleType: 'ROLE_PERSONAL' },
    ];

    /* ── 상태 ──────────────────────────────────── */
    let panel, overlay, inputEl, clearBtn, tabBar, body, loading, empty, resultList, sectionLabel;
    let allData      = [];
    let activeTabId  = 'all';
    let isOpen       = false;
    let debounceTimer = null;
    let focusedIndex = -1;

    /* ── 초기화 ────────────────────────────────── */
    function init(opts = {}) {
        _buildDOM();
        _bindEvents(opts.triggerSelector || '.search-trigger');
        _prefetch();
    }

    /* ── DOM 생성 ───────────────────────────────── */
    function _buildDOM() {
        /* 오버레이 */
        overlay = document.createElement('div');
        overlay.className = 'bnk-search-overlay';
        overlay.setAttribute('aria-hidden', 'true');
        document.body.appendChild(overlay);

        /* 패널 */
        panel = document.createElement('div');
        panel.className = 'bnk-search-panel';
        panel.setAttribute('role', 'search');
        panel.setAttribute('aria-label', '통합 검색');

        /* 입력 행 */
        const inputRow = document.createElement('div');
        inputRow.className = 'bnk-search-input-row';
        inputRow.innerHTML = `
			<form action="/search_page" method="get">
			    <div class="bnk-search-input-box">
			        <i class="fa fa-search" aria-hidden="true"></i>
			        <input type="text" name="search_text" id="bnkSearchInput"
			               placeholder="검색어를 입력하세요"
			               autocomplete="off"
			               aria-autocomplete="list"
			               aria-label="검색어 입력" />
			        <button type="button" class="bnk-search-clear" aria-label="입력 지우기">
			            <i class="fa fa-times"></i>
			        </button>
			    </div>
			    <button type="submit" class="bnk-search-submit" aria-label="검색">검색</button>
			</form>
			<button type="button" class="bnk-search-close" aria-label="검색창 닫기">
			    <i class="fa fa-times"></i>
			</button>
        `;

        /* 탭 */
        tabBar = document.createElement('div');
        tabBar.className = 'bnk-search-tabs';
        tabBar.setAttribute('role', 'tablist');
        TABS.forEach(tab => {
            const btn = document.createElement('button');
            btn.className = 'bnk-search-tab' + (tab.id === 'all' ? ' active' : '');
            btn.dataset.tabId = tab.id;
            btn.textContent = tab.label;
            btn.setAttribute('role', 'tab');
            btn.setAttribute('aria-selected', tab.id === 'all' ? 'true' : 'false');
            tabBar.appendChild(btn);
        });

        /* 바디 */
        body = document.createElement('div');
        body.className = 'bnk-search-body';
        body.innerHTML = `
            <div class="bnk-search-loading"><div class="bnk-search-spinner"></div></div>
            <span class="bnk-search-section-label">인기 검색어</span>
            <div class="bnk-hot-chips" role="list"></div>
            <ul class="bnk-search-list" role="listbox" aria-label="검색 결과"></ul>
            <div class="bnk-search-empty" aria-live="polite">
                <i class="fa fa-search" aria-hidden="true"></i>
                <p>검색 결과가 없습니다</p>
            </div>
        `;

        panel.appendChild(inputRow);
        panel.appendChild(tabBar);
        panel.appendChild(body);
        document.body.appendChild(panel);

        /* 참조 저장 */
        inputEl     = panel.querySelector('#bnkSearchInput');
        clearBtn    = panel.querySelector('.bnk-search-clear');
        loading     = body.querySelector('.bnk-search-loading');
        sectionLabel= body.querySelector('.bnk-search-section-label');
        resultList  = body.querySelector('.bnk-search-list');
        empty       = body.querySelector('.bnk-search-empty');
    }

    /* ── 이벤트 바인딩 ─────────────────────────── */
    function _bindEvents(triggerSel) {
        /* 트리거 (검색 아이콘) */
        document.querySelectorAll(triggerSel).forEach(el => {
            el.addEventListener('click', e => { e.preventDefault(); open(); });
        });

        /* 닫기 & 오버레이 */
        panel.querySelector('.bnk-search-close').addEventListener('click', close);
        overlay.addEventListener('click', close);


        /* 지우기 버튼 */
        clearBtn.addEventListener('click', _clearInput);

        /* 입력 */
        inputEl.addEventListener('input', () => {
            const q = inputEl.value.trim();
            clearBtn.classList.toggle('visible', !!q);
            clearTimeout(debounceTimer);
            debounceTimer = setTimeout(() => _query(q), DEBOUNCE_MS);
        });

        /* 키보드 네비게이션 */
        inputEl.addEventListener('keydown', _onKeydown);

        /* 탭 전환 */
        tabBar.addEventListener('click', e => {
            const btn = e.target.closest('.bnk-search-tab');
            if (!btn) return;
            activeTabId = btn.dataset.tabId;
            tabBar.querySelectorAll('.bnk-search-tab').forEach(t => {
                t.classList.toggle('active', t.dataset.tabId === activeTabId);
                t.setAttribute('aria-selected', t.dataset.tabId === activeTabId);
            });
            _query(inputEl.value.trim());
        });

        /* ESC */
        document.addEventListener('keydown', e => {
            if (e.key === 'Escape' && isOpen) close();
        });
    }

    /* ── 열기 / 닫기 ───────────────────────────── */
    function open() {
        if (isOpen) return;
        isOpen = true;

        const header = document.querySelector('header, .header, #header, .site-header');
        panel.style.top = (header ? header.offsetHeight : 70) + 'px';

        overlay.classList.add('is-open');
        panel.classList.add('is-open');
        document.body.style.overflow = 'hidden';

        if (allData.length) _renderInitial();
        else _prefetch();

        setTimeout(() => inputEl.focus(), 260);
    }

    function close() {
        if (!isOpen) return;
        isOpen = false;

        overlay.classList.remove('is-open');
        panel.classList.remove('is-open');
        document.body.style.overflow = '';

        setTimeout(() => {
            inputEl.value = '';
            clearBtn.classList.remove('visible');
            focusedIndex = -1;
        }, 280);
    }

    /* ── 사전 로드 ─────────────────────────────── */
    async function _prefetch() {
        _setLoading(true);
        try {
            const res  = await fetch(API_BASE);
            if (!res.ok) throw new Error('network');
            allData = await res.json();
            if (isOpen) _renderInitial();
        } catch (e) {
            console.error('[BNKSearch] 데이터 로드 실패:', e);
            if (isOpen) _renderEmpty('');
        } finally {
            _setLoading(false);
        }
    }

    /* ── 초기 화면 (칩 형태 인기 검색어) ─────────── */
    function _renderInitial() {
        sectionLabel.textContent = '인기 검색어';
        sectionLabel.style.display = 'block';
        resultList.innerHTML = '';
        resultList.style.display = 'none';
        empty.classList.remove('visible');

        const chipsEl = body.querySelector('.bnk-hot-chips');
        chipsEl.style.display = 'flex';
        chipsEl.innerHTML = '';

        const source = _filterByTab(allData)
            .sort((a, b) => b.searchVolume - a.searchVolume)
            .slice(0, 10);

        source.forEach((item, i) => {
            const chip = document.createElement('a');
            chip.className = 'bnk-hot-chip';
            chip.href = item.pageUrl || _fallbackUrl(item.keyword);
            chip.setAttribute('role', 'listitem');
            chip.innerHTML = `
                <span class="bnk-rank ${i < 3 ? 'top' : ''}">${i + 1}</span>
                ${_escHtml(item.keyword)}
            `;
            chipsEl.appendChild(chip);
        });
    }

    /* ── 입력어 처리 ────────────────────────────── */
    async function _query(q) {
        focusedIndex = -1;
        const chipsEl = body.querySelector('.bnk-hot-chips');

        if (!q) {
            _renderInitial();
            return;
        }

        sectionLabel.textContent = '검색 결과';
        chipsEl.style.display = 'none';
        resultList.style.display = 'block';

        /* 1차: 클라이언트 필터 (즉각 반응) */
        const clientResult = _filterKeywords(_filterByTab(allData), q);
        _renderList(clientResult, q);

        /* 2차: 서버 재검색 */
        try {
            _setLoading(true);
            const res  = await fetch(`${API_BASE}?q=${encodeURIComponent(q)}`);
            if (!res.ok) throw new Error('network');
            const data = await res.json();
            const filtered = _filterByTab(data);
            _renderList(filtered, q);
        } catch (e) {
            console.error('[BNKSearch] 검색 실패:', e);
        } finally {
            _setLoading(false);
        }
    }

    /* ── 탭 필터 ────────────────────────────────── */
    function _filterByTab(items) {
        const tab = TABS.find(t => t.id === activeTabId);
        if (!tab || !tab.roleType) return items;
        return items.filter(item => item.roleType === tab.roleType);
    }

    /* ── 키워드 필터 (부분 일치, 순서 무관) ──────── */
    function _filterKeywords(items, q) {
        const chars = [...new Set(q.split('').filter(c => c.trim()))];
        return items.filter(item =>
            chars.every(ch => item.keyword.includes(ch))
        );
    }

    /* ── 목록 렌더 ─────────────────────────────── */
    function _renderList(items, q) {
        resultList.innerHTML = '';
        focusedIndex = -1;

        const limited = items.slice(0, MAX_RESULTS);

        if (!limited.length) {
            _renderEmpty(q);
            return;
        }

        empty.classList.remove('visible');
        resultList.style.display = 'block';

        limited.forEach((item, idx) => {
            const li = document.createElement('li');
            li.setAttribute('role', 'option');

            const a = document.createElement('a');
            a.href = item.pageUrl || _fallbackUrl(item.keyword);
            a.setAttribute('tabindex', '-1');
            a.dataset.idx = idx;

            const catLabel = item.category || _roleLabel(item.roleType);

            a.innerHTML = `
                <i class="fa fa-search bnk-item-icon" aria-hidden="true"></i>
                <span class="bnk-item-kw">${_highlight(item.keyword, q)}</span>
                ${catLabel ? `<span class="bnk-item-cat">${_escHtml(catLabel)}</span>` : ''}
                <i class="fa fa-chevron-right bnk-item-arrow" aria-hidden="true"></i>
            `;

            li.appendChild(a);
            resultList.appendChild(li);
        });
    }

    /* ── 결과 없음 ─────────────────────────────── */
    function _renderEmpty(q) {
        resultList.innerHTML = '';
        const msg = q
            ? `<strong>"${_escHtml(q)}"</strong>에 대한 검색 결과가 없습니다`
            : '검색어를 입력해 주세요';
        empty.querySelector('p').innerHTML = msg;
        empty.classList.add('visible');
    }

    /* ── 하이라이트 ─────────────────────────────── */
    function _highlight(keyword, q) {
        if (!q) return _escHtml(keyword);
        const chars = [...new Set(q.split('').filter(c => c.trim()))];
        let result = _escHtml(keyword);
        chars.forEach(ch => {
            const esc = ch.replace(/[.*+?^${}()|[\]\\]/g, '\\$&');
            result = result.replace(
                new RegExp(esc, 'g'),
                `<span class="bnk-hl">${ch}</span>`
            );
        });
        return result;
    }

    /* ── 키보드 네비게이션 ──────────────────────── */
	function _onKeydown(e) {
	    const items = [...resultList.querySelectorAll('li a')];
	    
	    if (!items.length) {
	        if (e.key === 'Enter') {
	            panel.querySelector('form').submit();  // ← form submit으로 변경
	        }
	        return;
	    }

	    if (e.key === 'ArrowDown') {
	        e.preventDefault();
	        focusedIndex = Math.min(focusedIndex + 1, items.length - 1);
	        _applyFocus(items);
	    } else if (e.key === 'ArrowUp') {
	        e.preventDefault();
	        focusedIndex = Math.max(focusedIndex - 1, -1);
	        _applyFocus(items);
	    } else if (e.key === 'Enter') {
	        if (focusedIndex >= 0 && items[focusedIndex]) {
	            // 추천 검색어 클릭 시 해당 페이지로 이동
	            window.location.href = items[focusedIndex].href;
	        } else {
	            // 직접 입력 후 Enter → form submit
	            panel.querySelector('form').submit();
	        }
	    }
	}

    function _applyFocus(items) {
        items.forEach((a, i) => a.classList.toggle('is-focused', i === focusedIndex));
        if (focusedIndex >= 0) {
            inputEl.value = items[focusedIndex].querySelector('.bnk-item-kw')?.textContent || inputEl.value;
        }
    }

   

    /* ── 입력 지우기 ────────────────────────────── */
    function _clearInput() {
        inputEl.value = '';
        clearBtn.classList.remove('visible');
        focusedIndex = -1;
        _renderInitial();
        inputEl.focus();
    }

    /* ── 로딩 토글 ─────────────────────────────── */
    function _setLoading(show) {
        loading.classList.toggle('visible', show);
    }

    /* ── 유틸 ───────────────────────────────────── */
    function _roleLabel(roleType) {
        return roleType === 'ROLE_COMPANY' ? '기업'
             : roleType === 'ROLE_PERSONAL' ? '개인'
             : '';
    }

	function _fallbackUrl(keyword) {
	    const urlMap = {
	        '환율'    : '/',
	        '외환'    : '/',
	        '외화송금' : '/',
	        '환전'    : '/',
	        '예금'    : '/',
	        '적금'    : '/',
	        '대출'    : '/',
	        '입금'    : '/',
	        '이체'    : '/',
			'반가우이이이' : '/fx_home',
	    };
	    return urlMap[keyword] || `/search?q=${encodeURIComponent(keyword)}`;
	}

    function _escHtml(str) {
        return String(str)
            .replace(/&/g,'&amp;').replace(/</g,'&lt;')
            .replace(/>/g,'&gt;').replace(/"/g,'&quot;');
    }

    /* ── Public ─────────────────────────────────── */
    return { init, open, close };

})();

/* 자동 초기화 — triggerSelector를 헤더 검색 아이콘 선택자로 변경하세요 */
if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', () => {
        BNKSearch.init({ triggerSelector: '.search-trigger' });
    });
} else {
    BNKSearch.init({ triggerSelector: '.search-trigger' });
}