document.addEventListener('DOMContentLoaded', () => {
  const bannerWrap = document.querySelector('.banner-wrap');
  const slides = document.querySelectorAll('.slide');
  let current = 0;
  let autoTimer;
 
  if (!slides.length) return;
 
  /* ── 첫 슬라이드 활성화 ── */
  slides[0].classList.add('active');
 
  /* ── 화살표 버튼 생성 ── */
  const prevBtn = document.createElement('button');
  prevBtn.className = 'slider-btn prev';
  prevBtn.innerHTML = '&#8249;';
  prevBtn.setAttribute('aria-label', '이전 슬라이드');
 
  const nextBtn = document.createElement('button');
  nextBtn.className = 'slider-btn next';
  nextBtn.innerHTML = '&#8250;';
  nextBtn.setAttribute('aria-label', '다음 슬라이드');
 
  bannerWrap.appendChild(prevBtn);
  bannerWrap.appendChild(nextBtn);
 
  /* ── 도트 생성 ── */
  const dotsWrap = document.createElement('div');
  dotsWrap.className = 'slider-dots';
 
  slides.forEach((_, i) => {
    const dot = document.createElement('button');
    dot.className = 'dot' + (i === 0 ? ' active' : '');
    dot.setAttribute('aria-label', `슬라이드 ${i + 1}`);
    dot.addEventListener('click', () => {
      goTo(i);
      resetTimer();
    });
    dotsWrap.appendChild(dot);
  });
 
  bannerWrap.appendChild(dotsWrap);
 
  /* ── 슬라이드 이동 ── */
  function goTo(index) {
    slides[current].classList.remove('active');
    dotsWrap.children[current].classList.remove('active');
    current = (index + slides.length) % slides.length;
    slides[current].classList.add('active');
    dotsWrap.children[current].classList.add('active');
  }
 
  /* ── 자동 슬라이드 ── */
  function startTimer() {
    autoTimer = setInterval(() => goTo(current + 1), 3000);
  }
 
  function resetTimer() {
    clearInterval(autoTimer);
    startTimer();
  }
 
  prevBtn.addEventListener('click', () => { goTo(current - 1); resetTimer(); });
  nextBtn.addEventListener('click', () => { goTo(current + 1); resetTimer(); });
 
  startTimer();
});