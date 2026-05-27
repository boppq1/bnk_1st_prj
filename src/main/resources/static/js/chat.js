document.addEventListener("DOMContentLoaded", function () {
    // 1. JS 안에서 HTML 버튼 요소를 직접 만듭니다.
    const linkBtn = document.createElement("button");
    linkBtn.id = "customChatbotBtn";
    
    // 부산은행 로고와 챗봇 아이콘, "챗봇 상담" 텍스트를 시각적으로 구성합니다.
    linkBtn.innerHTML = `
        <div style="display: flex; flex-direction: column; align-items: center; gap: 4px;">
            <div style="font-size: 11px; font-weight: 700; color: rgba(255, 255, 255, 0.9);">BNK</div>
            
            <i class="fa-solid fa-comments-dollar" style="font-size: 26px; color: white; margin-bottom: 2px;"></i>
            <span style="font-size: 13px; font-weight: 700; color: white;">챗봇상담</span>
        </div>
    `;
    linkBtn.title = "부산은행 챗봇 상담 열기";
    linkBtn.setAttribute("aria-label", "부산은행 챗봇 상담 열기");

    // 2. [변경] 위치 및 형태 스타일링
    Object.assign(linkBtn.style, {
        position: "fixed",
        
        // [변경] 높은 위치는 유지하되, 오른쪽 테두리에서 30px 떼어냅니다.
        bottom: "35%",          
        right: "15%",          /* 테두리에서 시원하게 띄움 */
        transform: "translateY(50%)", 
        
        zIndex: "9999",
        
        // 🎨 디자인: 부산은행 상징 빨간색 (BNK Red)
        width: "90px",          /* 텍스트와 로고를 담기 위해 폭 확대 */
        height: "110px",        /* 수직으로 정보를 담기 위해 높이 확대 */
        backgroundColor: "#D7191F", /* 공식 BNK Red */
        color: "white",
        
        border: "none",
        
        // [변경] 모든 모서리를 둥글게 처리하여 안정감 있는 디자인 구현
        borderRadius: "20px",    
        cursor: "pointer",
        
        // [변경] 띄워진 위치에 맞춘 부드러운 그림자 효과
        boxShadow: "0 8px 25px rgba(215, 25, 31, 0.35)", 
        
        display: "flex",
        alignItems: "center",
        justifyContent: "center",
        
        transition: "all 0.3s cubic-bezier(0.25, 0.8, 0.25, 1)"
    });

    // 3. 마우스 인터랙션 (Hover 효과)
    linkBtn.addEventListener("mouseenter", function() {
        // 🔥 마우스를 올리면 더 진한 빨간색으로 바뀌고 위로 살짝 더 튀어나옴
        linkBtn.style.backgroundColor = "#b01419"; 
        linkBtn.style.transform = "translateY(50%) translateY(-5px)"; /* 위로 5px 이동 */
        linkBtn.style.boxShadow = "0 12px 30px rgba(215, 25, 31, 0.5)";
    });
    
    linkBtn.addEventListener("mouseleave", function() {
        // 마우스가 나가면 다시 기본 상태로 부드럽게 원복
        linkBtn.style.backgroundColor = "#D7191F";
        linkBtn.style.transform = "translateY(50%) translateY(0)";
        linkBtn.style.boxShadow = "0 8px 25px rgba(215, 25, 31, 0.35)";
    });

    // 4. 버튼 클릭 시 정중앙 팝업창(챗봇 화면) 띄우기
    const targetUrl = "/admin/chatPage";
    
    linkBtn.addEventListener("click", function () {
        const popupWidth = 480;
        const popupHeight = 700;
        
        const leftPosition = (window.screen.width - popupWidth) / 2;
        const topPosition = (window.screen.height - popupHeight) / 2;
        
        const windowName = "BusanBankChatbotPopup";
        const options = `width=${popupWidth},height=${popupHeight},left=${leftPosition},top=${topPosition},scrollbars=yes,resizable=yes`;
        
        window.open(targetUrl, windowName, options);
    });

    // 5. <body> 태그 내부 최하단에 완성된 버튼 강제 주입
    document.body.appendChild(linkBtn);
});