/**
 * Consolidated Admin JavaScript
 * This file combines admin-fixed.js and admin-charts.js
 * for a more efficient admin interface functionality
 */

(function () {
  "use strict"; // Use strict mode

  // Toggle the side navigation
  const sidebarToggle = document.body.querySelector("#sidebarToggle");
  if (sidebarToggle) {
    sidebarToggle.addEventListener("click", (event) => {
      event.preventDefault();
      document.body.classList.toggle("sidebar-toggled");
      document.querySelector(".sidebar").classList.toggle("toggled");
    });
  }

  // Close sidebar when window width is less than 768px
  window.addEventListener("resize", () => {
    if (window.innerWidth < 768) {
      document.body.classList.add("sidebar-toggled");
      document.querySelector(".sidebar").classList.add("toggled");
    }
  });

  // Prevent the content wrapper from scrolling when the fixed side navigation hovered over
  document.body.addEventListener("scroll", (event) => {
    if (
      document.body
        .querySelector(".navbar-nav")
        .classList.contains("overflow-auto") &&
      window.getComputedStyle(document.body.querySelector(".navbar-nav"))
        .overflow === "auto"
    ) {
      event.preventDefault();
    }
  });

  // Initialize dataTables
  const dataTables = document.querySelectorAll(".dataTable");
  if (dataTables.length > 0) {
    dataTables.forEach((table) => {
      if ($.fn.dataTable.isDataTable(table)) return;

      $(table).DataTable({
        responsive: true,
        lengthMenu: [10, 25, 50, 100],
        language: {
          search: "Tìm kiếm:",
          lengthMenu: "Hiển thị _MENU_ mục",
          info: "Hiển thị _START_ đến _END_ của _TOTAL_ mục",
          infoEmpty: "Hiển thị 0 đến 0 của 0 mục",
          infoFiltered: "(được lọc từ tổng số _MAX_ mục)",
          paginate: {
            first: "Đầu",
            last: "Cuối",
            next: "Sau",
            previous: "Trước",
          },
        },
      });
    });
  }

  // Add active class to current link
  const currentPageUrl = window.location.pathname;
  const navLinks = document.querySelectorAll(".sidebar .nav-link");
  if (navLinks.length > 0) {
    navLinks.forEach((link) => {
      if (link.getAttribute("href") === currentPageUrl) {
        link.classList.add("active");
      }
    });
  }

  // Initialize tooltips
  const tooltipTriggerList = [].slice.call(
    document.querySelectorAll('[data-bs-toggle="tooltip"]')
  );
  tooltipTriggerList.map(function (tooltipTriggerEl) {
    return new bootstrap.Tooltip(tooltipTriggerEl);
  });

  // Confirm delete
  const deleteButtons = document.querySelectorAll(".btn-delete");
  if (deleteButtons.length > 0) {
    deleteButtons.forEach((button) => {
      button.addEventListener("click", (event) => {
        event.preventDefault();
        const url = button.getAttribute("href");

        Swal.fire({
          title: "Bạn có chắc chắn?",
          text: "Dữ liệu đã xóa không thể khôi phục!",
          icon: "warning",
          showCancelButton: true,
          confirmButtonColor: "#4e73df",
          cancelButtonColor: "#e74a3b",
          confirmButtonText: "Xác nhận xóa",
          cancelButtonText: "Hủy",
        }).then((result) => {
          if (result.isConfirmed) {
            window.location.href = url;
          }
        });
      });
    });
  }

  // Form validation
  const forms = document.querySelectorAll(".needs-validation");
  if (forms.length > 0) {
    forms.forEach((form) => {
      form.addEventListener(
        "submit",
        (event) => {
          if (!form.checkValidity()) {
            event.preventDefault();
            event.stopPropagation();
          }
          form.classList.add("was-validated");
        },
        false
      );
    });
  }

  // Toast notifications
  const toastElements = document.querySelectorAll(".toast");
  if (toastElements.length > 0) {
    toastElements.forEach((toast) => {
      new bootstrap.Toast(toast).show();
    });
  }

  // Back to top button
  const backToTopButton = document.querySelector(".back-to-top");
  if (backToTopButton) {
    window.addEventListener("scroll", () => {
      if (window.scrollY > 100) {
        backToTopButton.classList.add("active");
      } else {
        backToTopButton.classList.remove("active");
      }
    });

    backToTopButton.addEventListener("click", (event) => {
      event.preventDefault();
      window.scrollTo({
        top: 0,
        behavior: "smooth",
      });
    });
  }

  // Image preview for uploads
  const fileInputs = document.querySelectorAll(".custom-file-input");
  if (fileInputs.length > 0) {
    fileInputs.forEach((input) => {
      input.addEventListener("change", (event) => {
        const fileName = event.target.files[0].name;
        const nextSibling = event.target.nextElementSibling;
        nextSibling.innerText = fileName;

        const previewElement = document.querySelector(input.dataset.preview);
        if (previewElement) {
          const reader = new FileReader();
          reader.onload = (e) => {
            previewElement.src = e.target.result;
          };
          reader.readAsDataURL(event.target.files[0]);
        }
      });
    });
  }
})();

/**
 * Admin functionality
 */
$(document).ready(function () {
  // Initialize CKEditor for all textareas with class 'ckeditor'
  const editorElements = document.querySelectorAll(".ckeditor");
  editorElements.forEach((element) => {
    ClassicEditor.create(element, {
      toolbar: [
        "heading",
        "|",
        "bold",
        "italic",
        "link",
        "bulletedList",
        "numberedList",
        "|",
        "outdent",
        "indent",
        "|",
        "imageUpload",
        "blockQuote",
        "insertTable",
        "mediaEmbed",
        "undo",
        "redo",
      ],
    }).catch((error) => {
      console.error("CKEditor initialization error:", error);
    });
  });

  // Image preview functionality
  $('.custom-file-input, input[type="file"]').on("change", function () {
    const file = this.files[0];
    if (file) {
      const reader = new FileReader();
      const previewId = $(this).data("preview") || "imagePreview";

      reader.onload = function (e) {
        $(`#${previewId}`).attr("src", e.target.result).show();
        $(".image-preview-container").show();
      };

      reader.readAsDataURL(file);
    }
  });

  // Date validation - end date must be after start date
  $('#endDate, [name="endDate"]').on("change", function () {
    const startDate = new Date($("#startDate").val());
    const endDate = new Date($(this).val());

    if (endDate <= startDate) {
      $(this).addClass("is-invalid");
      const feedbackId =
        $(this).data("feedback") || `${$(this).attr("id")}Feedback`;
      $(`#${feedbackId}`).text("Ngày kết thúc phải sau ngày bắt đầu");
    } else {
      $(this).removeClass("is-invalid");
    }
  });

  // Reset button functionality
  $('button[type="reset"]').on("click", function () {
    // Reset custom elements that might not be handled by default reset behavior
    setTimeout(function () {
      $(".image-preview").hide();
      $(".invalid-feedback").hide();
      $("input, select, textarea").removeClass("is-invalid");
    }, 10);
  });

  // Set default date to today
  const setDefaultDates = function () {
    const today = new Date();
    const formattedDate = today.toISOString().split("T")[0];

    if ($("#startDate").length && !$("#startDate").val()) {
      $("#startDate").val(formattedDate);
    }
  };

  setDefaultDates();

  // Table search functionality
  $("#tableSearch").on("keyup", function () {
    const value = $(this).val().toLowerCase();
    const table = $(this).data("target");

    $(`${table} tbody tr`).filter(function () {
      $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1);
    });
  });

  // Filter form auto-submit
  $(".auto-submit").on("change", function () {
    const form = $(this).closest("form");
    form.submit();
  });

  // Confirm delete actions
  $(".confirm-delete").on("click", function (e) {
    const message =
      $(this).data("message") || "Bạn có chắc chắn muốn xóa mục này?";
    if (!confirm(message)) {
      e.preventDefault();
    }
  });
});
