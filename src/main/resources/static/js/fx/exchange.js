document.addEventListener('DOMContentLoaded', () => {

    let chart = null;
    let currentPeriod = '1w';

    // 날짜 포맷 (yyyyMMdd)
    function formatDate(date) {
        const y = date.getFullYear();
        const m = String(date.getMonth() + 1).padStart(2, '0');
        const d = String(date.getDate()).padStart(2, '0');
        return `${y}${m}${d}`;
    }

    // 기간별 날짜 배열 생성 (주말 제외)
    function getDateRange(period) {
        const dates = [];
        const today = new Date();
        const days = period === '1w' ? 7 : period === '1m' ? 30 : 90;

        for (let i = days - 1; i >= 0; i--) {
            const d = new Date(today);
            d.setDate(today.getDate() - i);
            if (d.getDay() !== 0 && d.getDay() !== 6) {
                dates.push(formatDate(d));
            }
        }
        return dates;
    }

    // 날짜 하나 fetch
    async function fetchOneDay(dateStr, curUnit, rateType) {
        const res = await fetch(`/api/exchange?searchdate=${dateStr}`);
        if (!res.ok) return null;
        const data = await res.json();
        if (!data || data.length === 0) return null;

        const target = data.find(item => item.cur_unit === curUnit);
        if (!target) return null;

        const val = parseFloat(target[rateType].replace(/,/g, ''));
        return isNaN(val) || val === 0 ? null : val;
    }

    // 기간 전체 fetch
    async function fetchPeriodData(period) {
        const curUnit  = document.getElementById('currency').value;
        const rateType = document.getElementById('rateType').value;
        const dates    = getDateRange(period);

        const results = await Promise.all(
            dates.map(async (dateStr) => {
                const value = await fetchOneDay(dateStr, curUnit, rateType);
                return { date: dateStr, value };
            })
        );
        return results.filter(r => r.value !== null);
    }

    // 차트 + 환율 표시
    function renderChart(dataList) {
        const labels = dataList.map(d => d.date.slice(4, 6) + '/' + d.date.slice(6));
        const values = dataList.map(d => d.value);

        const max    = Math.max(...values);
        const min    = Math.min(...values);
        const latest = values[values.length - 1];
        const prev   = values[values.length - 2] ?? latest;
        const diff   = (latest - prev).toFixed(2);
        const pct    = ((diff / prev) * 100).toFixed(2);

        // 환율 값 표시
        document.querySelector('.rate-value').textContent =
            latest.toLocaleString('ko-KR', { minimumFractionDigits: 2 });

        // 증감 표시
        const changeEl = document.querySelector('.rate-change');
        if (diff >= 0) {
            changeEl.textContent = `▲ ${diff} (+${pct}%)`;
            changeEl.className   = 'rate-change up';
        } else {
            changeEl.textContent = `▼ ${Math.abs(diff)} (${pct}%)`;
            changeEl.className   = 'rate-change down';
        }

        if (chart) chart.destroy();

        chart = new Chart(document.getElementById('rateChart'), {
            type: 'line',
            data: {
                labels,
                datasets: [{
                    data: values,
                    borderColor: '#e74c3c',
                    borderWidth: 1.5,
                    pointRadius: 0,
                    fill: false,
                    tension: 0.3
                }]
            },
            plugins: [{
                id: 'minMaxLabel',
                afterDatasetsDraw(chart) {
                    const ctx    = chart.ctx;
                    const meta   = chart.getDatasetMeta(0);
                    const points = meta.data;

                    points.forEach((pt, i) => {
                        const val = values[i];
                        if (val === max || val === min) {
                            ctx.save();
                            ctx.font      = '11px sans-serif';
                            ctx.fillStyle = val === max ? '#e74c3c' : '#3498db';
                            const label   = (val === max ? '최고: ' : '최저: ')
                                          + val.toLocaleString();
                            const x = Math.min(Math.max(pt.x, 40), chart.width - 70);
                            // 최고값은 아래, 최저값은 위로 표시해서 잘림 방지
                            const y = val === max ? pt.y + 18 : pt.y - 8;
                            ctx.fillText(label, x, y);
                            ctx.restore();
                        }
                    });
                }
            }],
            options: {
                responsive: true,
                maintainAspectRatio: false,
                layout: {
                    padding: {
                        top: 10,
                        bottom: 10,
                        left: 5,
                        right: 5
                    }
                },
                plugins: { legend: { display: false }, tooltip: { enabled: true } },
                scales: {
                    x: { display: false },
                    y: {
                        display: false,
                        // 위아래 여백 추가로 잘림 방지
                        grace: '10%'
                    }
                }
            }
        });
    }

    // 로드 함수
    async function loadChart(period) {
        const dataList = await fetchPeriodData(period);
        if (!dataList.length) {
            alert('조회된 데이터가 없습니다.');
            return;
        }
        renderChart(dataList);
    }

    // 조회 버튼
    document.querySelector('.sidebar-btn').addEventListener('click', () => {
        loadChart(currentPeriod);
    });

    // 기간 탭
    document.querySelectorAll('.tab-btn').forEach(btn => {
        btn.addEventListener('click', function () {
            document.querySelectorAll('.tab-btn').forEach(b => b.classList.remove('active'));
            this.classList.add('active');
            currentPeriod = this.dataset.period;
            loadChart(currentPeriod);
        });
    });

    // 페이지 진입 시 기본 조회
    loadChart('1w');
});