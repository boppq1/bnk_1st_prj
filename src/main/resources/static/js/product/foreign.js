document.addEventListener("DOMContentLoaded", function () {
       const dropdownBtn = document.querySelector(".foreign-dropdown-btn");
       const dropdownMenu = document.querySelector(".foreign-dropdown-menu");
       const subTitles = document.querySelectorAll(".foreign-sub-title");

       dropdownBtn.addEventListener("click", function () {
           dropdownBtn.classList.toggle("active");
           dropdownMenu.classList.toggle("active");
       });

       subTitles.forEach(function (title) {
           title.addEventListener("click", function () {
               const subMenu = title.nextElementSibling;

               subTitles.forEach(function (otherTitle) {
                   if (otherTitle !== title) {
                       otherTitle.classList.remove("active");
                       otherTitle.nextElementSibling.classList.remove("active");
                   }
               });

               title.classList.toggle("active");
               subMenu.classList.toggle("active");
           });
       });
   });