document.addEventListener('DOMContentLoaded', function () {
 
    const openBtn = document.getElementById('openCouponModal');
    const closeBtn = document.getElementById('closeCouponModal');
    const cancelBtn = document.getElementById('cancelCouponBtn');
    const printBtn = document.getElementById('printCouponBtn');
    const modal = document.getElementById('couponModal');
    const nameInput = document.getElementById('couponName');
    const nameDisplay = document.getElementById('couponNameDisplay');
 
    // 모달 열기
    openBtn.addEventListener('click', function () {
        const name = nameInput.value.trim();
        if (!name) {
            alert('성명을 입력해 주십시오.');
            nameInput.focus();
            return;
        }
        nameDisplay.textContent = name;
        modal.style.display = 'flex';
    });
 
    // 모달 닫기
    function closeModal() {
        modal.style.display = 'none';
    }
 
    closeBtn.addEventListener('click', closeModal);
    cancelBtn.addEventListener('click', closeModal);
 
    // 오버레이 배경 클릭 시 닫기
    modal.addEventListener('click', function (e) {
        if (e.target === modal) closeModal();
    });
 
    // 인쇄
    printBtn.addEventListener('click', function () {
        const printArea = document.getElementById('couponPrintArea').innerHTML;
        const printWindow = window.open('', '_blank', 'width=640,height=440');
        printWindow.document.write(`
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <title>환율우대쿠폰</title>
                <style>
                    * { box-sizing: border-box; margin: 0; padding: 0; }
                    body { margin: 20px; font-family: 'Malgun Gothic', sans-serif; }
                    .coupon-preview {
                        position: relative;
                        display: inline-block;
                        width: 560px;
                    }
                    .coupon-bg-img {
                        width: 100%;
                        display: block;
                    }
                    .coupon-name-overlay {
                        position: absolute;
                        top: 10px;
                        left: 14px;
                        font-size: 13px;
                        font-weight: 700;
                        color: #333;
                        background: transparent;
                        white-space: nowrap;
                    }
                </style>
            </head>
            <body>${printArea}</body>
            </html>
        `);
        printWindow.document.close();
        printWindow.focus();
        printWindow.print();
        printWindow.close();
    });
 
});
 