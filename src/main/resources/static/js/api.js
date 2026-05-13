    // // =============================
    // // 토큰 관련 유틸 함수
    // // =============================
    //
    // // 토큰 저장
    // function saveTokens(accessToken, refreshToken) {
    //     localStorage.setItem("accessToken", accessToken);
    //     localStorage.setItem("refreshToken", refreshToken);
    // }
    //
    // // 토큰 삭제 (로그아웃)
    // function clearTokens() {
    //     localStorage.removeItem("accessToken");
    //     localStorage.removeItem("refreshToken");
    // }
    //
    // // =============================
    // // 로그인 페이지 전용 함수
    // // =============================
    //
    // // 이미 로그인된 상태면 메인으로 이동 (로그인 페이지에서 호출)
    // function redirectIfLoggedIn() {
    //     const token = localStorage.getItem("accessToken");
    //     if (token) {
    //         window.location.href = "/adminMain";
    //     }
    // }
    //
    // // =============================
    // // 보호된 페이지 전용 함수
    // // =============================
    //
    // // 토큰 없으면 로그인 페이지로 이동 (alert 없이 조용히 리다이렉트)
    // function requireLogin() {
    //     const token = localStorage.getItem("accessToken");
    //     if (!token) {
    //         window.location.href = "/adminLogin";
    //         return false;
    //     }
    //     return true;
    // }
    //
    // // =============================
    // // API 공통 fetch 함수
    // // =============================
    //
    // async function apiFetch(url, options = {}) {
    //     const token = localStorage.getItem("accessToken");
    //
    //     const headers = {
    //         "Content-Type": "application/json",
    //         ...(token ? { "Authorization": "Bearer " + token } : {}),
    //         ...(options.headers || {})
    //     };
    //
    //     const response = await fetch(url, {
    //         ...options,
    //         headers
    //     });
    //
    //     // 401: 토큰 만료 → alert 없이 조용히 로그인 페이지로
    //     if (response.status === 401) {
    //         clearTokens();
    //         window.location.href = "/adminLogin";
    //         return null;
    //     }
    //
    //     return response;
    // }
    //
    // // =============================
    // // 로그인 함수
    // // =============================
    //
    // async function login() {
    //     const adminDto = {
    //         login_id: document.getElementById('login_id').value,   // AdminDto 필드명과 일치
    //         password: document.getElementById('password').value,
    //     };
    //
    //     // 빈 값 체크
    //     if (!adminDto.login_id || !adminDto.password) {
    //         showLoginError("아이디와 비밀번호를 입력해주세요.");
    //         return;
    //     }
    //
    //     try {
    //         const response = await fetch('/admin/login', {
    //             method: 'POST',
    //             headers: {
    //                 'Content-Type': 'application/json',
    //             },
    //             body: JSON.stringify(adminDto),
    //         });
    //
    //         if (!response.ok) {
    //             // 서버 오류 메시지를 alert 없이 화면에 표시
    //             showLoginError("아이디 또는 비밀번호가 올바르지 않습니다.");
    //             return;
    //         }
    //
    //         const data = await response.json();
    //         saveTokens(data.accessToken, data.refreshToken);
    //
    //         window.location.href = "/adminMain"; // 로그인 성공 시 메인 페이지로 이동
    //
    //     } catch (error) {
    //         console.error("로그인 오류:", error);
    //         showLoginError("서버 연결에 실패했습니다. 잠시 후 다시 시도해주세요.");
    //     }
    // }
    //
    // // 로그인 오류 메시지 표시 (alert 대신 화면에 출력)
    // function showLoginError(message) {
    //     const errorDiv = document.getElementById('loginError');
    //     if (errorDiv) {
    //         errorDiv.textContent = message;
    //         errorDiv.style.display = 'block';
    //     }
    // }
    //
    // // =============================
    // // 로그아웃 함수
    // // =============================
    //
    // function logout() {
    //     clearTokens();
    //     window.location.href = "/adminLogin";
    // }
