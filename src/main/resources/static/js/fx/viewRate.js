document.getElementById('searchBtn').addEventListener('click', function () {
    const curNm = document.getElementById('cur_nm').value;

    fetch('/viewRate?cur_nm=' + encodeURIComponent(curNm))
        .then(response => {
            if (!response.ok) {
                throw new Error('서버 응답 오류: ' + response.status);
            }
            return response.json();
        })
        .then(data => {
            renderTable(data);
        })
        .catch(error => {
            console.error('조회 실패:', error);
            alert('데이터를 불러오지 못했습니다.');
        });
});

function renderTable(items) {
    const tbody = document.getElementById('rateTableBody');
    tbody.innerHTML = '';  // 기존 행 비우기

    if (!items || items.length === 0) {
        tbody.innerHTML = '<tr><td colspan="6">조회 결과가 없습니다.</td></tr>';
        return;
    }

    items.forEach(item => {
        const tr = document.createElement('tr');
        tr.innerHTML = `
            <td>${item.ntc_dt ?? ''}</td>
            <td>${item.rate_no ?? ''}</td>
            <td>${item.cur_nm ?? ''}</td>
            <td>${item.ttb ?? ''}</td>
            <td>${item.tts ?? ''}</td>
            <td>${item.deal_bas_r ?? ''}</td>
        `;
        tbody.appendChild(tr);
    });
}