/**
 * Consolidated Admin JavaScript
 * This file combines admin-fixed.js and admin-charts.js
 * for a more efficient admin interface functionality
 */

(function () {
  "use strict"; // Use strict mode

  // DOM selectors - defined once for performance
  const body = document.body;
  const sidebar = document.querySelector(".sidebar");

  // DOM ready function - handle all initializations
  function initAdminUI() {
    initSidebar();
    initDataTables();
    initNavLinks();
    initTooltips();
    initDeleteButtons();
    initFormValidation();
    initToasts();
    initBackToTop();
    initFileUploads();
    initCategoriesPage();
  }

  // Sidebar management
  function initSidebar() {
    const sidebarToggle = body.querySelector("#sidebarToggle");
    if (sidebarToggle) {
      sidebarToggle.addEventListener("click", (event) => {
        event.preventDefault();
        body.classList.toggle("sidebar-toggled");
        sidebar.classList.toggle("toggled");
      });
    }

    // Close sidebar when window width is less than 768px
    const handleResize = () => {
      if (window.innerWidth < 768) {
        body.classList.add("sidebar-toggled");
        sidebar.classList.add("toggled");
      }
    };

    // Call once and add listener
    handleResize();
    window.addEventListener("resize", handleResize);

    // Prevent the content wrapper from scrolling when the fixed side navigation hovered over
    body.addEventListener("scroll", (event) => {
      const navbar = body.querySelector(".navbar-nav");
      if (
        navbar &&
        navbar.classList.contains("overflow-auto") &&
        window.getComputedStyle(navbar).overflow === "auto"
      ) {
        event.preventDefault();
      }
    });
  }

  // DataTables initialization
  function initDataTables() {
    const dataTables = document.querySelectorAll(".dataTable");
    if (!dataTables.length || !$.fn.dataTable) return;

    const dtConfig = {
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
    };

    dataTables.forEach((table) => {
      if (!$.fn.dataTable.isDataTable(table)) {
        $(table).DataTable(dtConfig);
      }
    });
  }

  // Add active class to current link
  function initNavLinks() {
    const currentPageUrl = window.location.pathname;
    const navLinks = document.querySelectorAll(".sidebar .nav-link");

    if (navLinks.length) {
      navLinks.forEach((link) => {
        if (link.getAttribute("href") === currentPageUrl) {
          link.classList.add("active");
        }
      });
    }
  }

  // Initialize tooltips
  function initTooltips() {
    const tooltipTriggerList = document.querySelectorAll(
      '[data-bs-toggle="tooltip"]'
    );
    if (!tooltipTriggerList.length || !bootstrap?.Tooltip) return;

    tooltipTriggerList.forEach((el) => new bootstrap.Tooltip(el));
  }

  // Form validation for create and edit forms
  function initFormValidation() {
    const forms = document.querySelectorAll(".needs-validation");
    if (!forms.length) return;

    forms.forEach((form) => {
      form.addEventListener("submit", function (event) {
        if (!form.checkValidity()) {
          event.preventDefault();
          event.stopPropagation();
        }
        form.classList.add("was-validated");
      });
    });
  }

  // Toast notifications
  function initToasts() {
    const toastElements = document.querySelectorAll(".toast");
    if (!toastElements.length || !bootstrap?.Toast) return;

    toastElements.forEach((toast) => {
      new bootstrap.Toast(toast).show();
    });
  }

  // Back to top button
  function initBackToTop() {
    const backToTopButton = document.querySelector(".back-to-top");
    if (!backToTopButton) return;

    const scrollHandler = () => {
      if (window.scrollY > 100) {
        backToTopButton.classList.add("active");
      } else {
        backToTopButton.classList.remove("active");
      }
    };

    // Throttle scroll event
    let isScrolling = false;
    window.addEventListener("scroll", () => {
      if (!isScrolling) {
        window.requestAnimationFrame(() => {
          scrollHandler();
          isScrolling = false;
        });
        isScrolling = true;
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
  function initFileUploads() {
    const fileInputs = document.querySelectorAll(".custom-file-input");
    if (!fileInputs.length) return;

    fileInputs.forEach((input) => {
      input.addEventListener("change", (event) => {
        const files = event.target.files;
        if (!files.length) return;

        const fileName = files[0].name;
        const nextSibling = event.target.nextElementSibling;
        if (nextSibling) nextSibling.innerText = fileName;

        const previewElement = document.querySelector(input.dataset.preview);
        if (previewElement) {
          const reader = new FileReader();
          reader.onload = (e) => {
            previewElement.src = e.target.result;
          };
          reader.readAsDataURL(files[0]);
        }
      });
    });
  }

  // Initialize Categories Table functionality if needed
  function initCategoriesPage() {
    if (!document.getElementById("categoriesTable")) return;

    // Shared variables
    const searchInput = document.getElementById("searchInput");
    const tableRows = document.querySelectorAll(".category-row");
    const sortSelect = document.getElementById("sortOrder");
    const rowsPerPage = 10;

    // Make these functions available globally for event handlers
    window.currentPage = 0;

    // Display rows function
    window.displayRows = function (page) {
      const start = page * rowsPerPage;
      const end = start + rowsPerPage;
      let visibleCount = 0;

      // First get all non-filtered rows
      const availableRows = Array.from(tableRows).filter(
        (row) => row.dataset.hiddenBySearch !== "true"
      );

      // Show/hide rows based on pagination
      availableRows.forEach((row, index) => {
        const showOnThisPage = index >= start && index < end;
        row.style.display = showOnThisPage ? "" : "none";
        if (showOnThisPage) visibleCount++;
      });

      // Update pagination UI only if client-side filtering is active
      if (searchInput && searchInput.value.trim() !== "") {
        updatePagination();
        updateRowCountDisplay();
      }

      return visibleCount;
    };

    // Update row count display
    function updateRowCountDisplay() {
      const visibleRows = Array.from(tableRows).filter(
        (row) => row.dataset.hiddenBySearch !== "true"
      );
      const countDisplay = document.querySelector(
        ".text-muted span:nth-child(3)"
      );
      if (countDisplay) countDisplay.textContent = visibleRows.length;
    }

    // Update pagination controls
    function updatePagination() {
      // Only update pagination during client-side filtering
      if (!searchInput || searchInput.value.trim() === "") return;

      const visibleRows = Array.from(tableRows).filter(
        (row) => row.dataset.hiddenBySearch !== "true"
      );
      const totalPages = Math.ceil(visibleRows.length / rowsPerPage);

      const paginationElement = document.querySelector(".pagination");
      if (!paginationElement) return;

      // Update active state
      const pageItems = paginationElement.querySelectorAll(".page-item");
      pageItems.forEach((item) => {
        const link = item.querySelector(".page-link");
        if (!link) return;

        const ariaLabel = link.getAttribute("aria-label");
        if (ariaLabel !== "Previous" && ariaLabel !== "Next") {
          const pageNum = parseInt(link.textContent) - 1;
          item.classList.toggle("active", pageNum === window.currentPage);
        }
      });

      // Update disabled state for prev/next buttons
      const prevButton = paginationElement.querySelector(
        '.page-item .page-link[aria-label="Previous"]'
      );
      const nextButton = paginationElement.querySelector(
        '.page-item .page-link[aria-label="Next"]'
      );

      if (prevButton) {
        const prevItem = prevButton.closest(".page-item");
        prevItem.classList.toggle("disabled", window.currentPage === 0);
      }

      if (nextButton) {
        const nextItem = nextButton.closest(".page-item");
        nextItem.classList.toggle(
          "disabled",
          window.currentPage >= totalPages - 1 || totalPages === 0
        );
      }
    }

    // Handle pagination
    initPagination();

    function initPagination() {
      const pageLinks = document.querySelectorAll(".pagination .page-link");
      if (!pageLinks.length) return;

      pageLinks.forEach((link) => {
        link.addEventListener("click", function (e) {
          const pageItem = this.parentElement;

          // Don't do anything if disabled
          if (pageItem.classList.contains("disabled")) {
            e.preventDefault();
            return;
          }

          // If we're using client-side filtering (search is active)
          if (searchInput && searchInput.value.trim() !== "") {
            e.preventDefault();

            // Handle prev/next buttons
            if (this.getAttribute("aria-label") === "Previous") {
              if (window.currentPage > 0) {
                window.currentPage--;
                window.displayRows(window.currentPage);
              }
            } else if (this.getAttribute("aria-label") === "Next") {
              const visibleRows = Array.from(tableRows).filter(
                (row) => row.dataset.hiddenBySearch !== "true"
              );
              const totalPages = Math.ceil(visibleRows.length / rowsPerPage);

              if (window.currentPage < totalPages - 1) {
                window.currentPage++;
                window.displayRows(window.currentPage);
              }
            } else {
              // Handle specific page numbers
              window.currentPage = parseInt(this.textContent) - 1;
              window.displayRows(window.currentPage);
            }
          } else {
            // For server-side pagination, let the form submit normally
            const pageInput = document.getElementById("pageInput");
            if (pageInput) {
              const url = new URL(
                this.getAttribute("href"),
                window.location.origin
              );
              const pageParam = url.searchParams.get("page");
              if (pageParam !== null) {
                pageInput.value = pageParam;
              }
            }
          }
        });
      });
    }

    // Initialize URL parameters and pagination links
    function initFromUrl() {
      updatePageInputFromUrl();
      updatePaginationLinks();
    }

    // Initialize page input with current URL page if available
    function updatePageInputFromUrl() {
      const urlParams = new URLSearchParams(window.location.search);
      const pageParam = urlParams.get("page");
      const pageInput = document.getElementById("pageInput");
      if (pageInput && pageParam !== null) {
        pageInput.value = pageParam;
      }
    }

    // Update pagination URL to maintain all parameters
    function updatePaginationLinks() {
      const params = collectFormParams();
      const pageLinks = document.querySelectorAll(".pagination .page-link");

      pageLinks.forEach((link) => {
        if (!link.hasAttribute("href")) return;

        const url = new URL(link.getAttribute("href"), window.location.origin);

        // Keep all existing search parameters except page
        for (const [key, value] of Object.entries(params)) {
          if (key !== "page") {
            url.searchParams.set(key, value);
          }
        }

        link.setAttribute("href", url.pathname + url.search);
      });
    }

    // Collect all search form parameters
    function collectFormParams() {
      const form = document.getElementById("searchForm");
      if (!form) return {};

      const formData = new FormData(form);
      const params = {};

      for (const [key, value] of formData.entries()) {
        params[key] = value;
      }

      return params;
    }

    // Initialize with first page
    initFromUrl();
    if (tableRows.length > 0) {
      window.displayRows(window.currentPage);
    }
  }

  // Run initialization when DOM is ready
  if (document.readyState === "loading") {
    document.addEventListener("DOMContentLoaded", initAdminUI);
  } else {
    initAdminUI();
  }
})();

/**
 * Admin functionality
 */
document.addEventListener("DOMContentLoaded", function () {
  // Centralized event delegation
  document.addEventListener("click", handleClick);
  document.addEventListener("change", handleChange);
  document.addEventListener("keyup", handleKeyup);

  // Handle all click events with event delegation
  function handleClick(e) {
    // Reset button functionality
    if (e.target.matches('button[type="reset"]')) {
      setTimeout(function () {
        document
          .querySelectorAll(".image-preview")
          .forEach((el) => (el.style.display = "none"));
        document
          .querySelectorAll(".invalid-feedback")
          .forEach((el) => (el.style.display = "none"));
        document
          .querySelectorAll("input, select, textarea")
          .forEach((el) => el.classList.remove("is-invalid"));
      }, 10);
    }

    // Confirm delete actions
    if (e.target.closest(".confirm-delete")) {
      const button = e.target.closest(".confirm-delete");
      const message =
        button.dataset.message || "Bạn có chắc chắn muốn xóa mục này?";
      if (!confirm(message)) {
        e.preventDefault();
      }
    }

    // Search button
    if (e.target.matches("#searchButton")) {
      if (window.filterTable) window.filterTable();
    }

    // Apply filters button
    if (e.target.matches("#applyFilters")) {
      if (window.applyAllFilters) window.applyAllFilters();
    }

    // Reset filters button
    if (
      e.target.closest(".reset") &&
      document.getElementById("categoriesTable")
    ) {
      const resetButton = e.target.closest(".reset");
      const searchInput = document.getElementById("searchInput");
      const tableRows = document.querySelectorAll(".category-row");
      const sortSelect = document.getElementById("sortOrder");

      if (searchInput) searchInput.value = "";

      // Show all rows
      tableRows.forEach((row) => {
        row.style.display = "";
        row.dataset.hiddenBySearch = "false";
      });

      // Reset other filters
      if (sortSelect) {
        sortSelect.value = "";
      }

      // Reset to first page
      window.currentPage = 0;
      if (window.displayRows) window.displayRows(0);

      // Show visual feedback
      const originalText = resetButton.innerHTML;
      resetButton.innerHTML = '<i class="fas fa-check me-1"></i>Đã làm mới';
      resetButton.classList.remove("btn-outline-primary");
      resetButton.classList.add("btn-success");

      setTimeout(() => {
        resetButton.innerHTML = originalText;
        resetButton.classList.remove("btn-success");
        resetButton.classList.add("btn-outline-primary");
      }, 1500);
    }
  }

  // Handle all change events with event delegation
  function handleChange(e) {
    // Image preview functionality
    if (e.target.matches('.custom-file-input, input[type="file"]')) {
      const file = e.target.files[0];
      if (file) {
        const reader = new FileReader();
        const previewId = e.target.dataset.preview || "imagePreview";

        reader.onload = function (e) {
          const previewElement = document.getElementById(previewId);
          if (previewElement) {
            previewElement.src = e.target.result;
            previewElement.style.display = "block";
          }

          const container = document.querySelector(".image-preview-container");
          if (container) container.style.display = "block";
        };

        reader.readAsDataURL(file);
      }
    }

    // Date validation - end date must be after start date
    if (e.target.matches('#endDate, [name="endDate"]')) {
      const startDateInput = document.getElementById("startDate");
      if (!startDateInput) return;

      const startDate = new Date(startDateInput.value);
      const endDate = new Date(e.target.value);

      if (endDate <= startDate) {
        e.target.classList.add("is-invalid");
        const feedbackId =
          e.target.dataset.feedback || `${e.target.id}Feedback`;
        const feedbackElement = document.getElementById(feedbackId);
        if (feedbackElement) {
          feedbackElement.textContent = "Ngày kết thúc phải sau ngày bắt đầu";
        }
      } else {
        e.target.classList.remove("is-invalid");
      }
    }

    // Filter form auto-submit
    if (e.target.matches(".auto-submit")) {
      const form = e.target.closest("form");
      if (form) form.submit();
    }

    // Sort options change
    if (e.target.matches("#sortBy, #sortDir")) {
      const pageInput = document.getElementById("pageInput");
      const searchForm = document.getElementById("searchForm");

      if (pageInput) pageInput.value = "0"; // Reset to first page on sort change
      if (searchForm) searchForm.submit();
    }
  }

  // Handle all keyup events with event delegation
  function handleKeyup(e) {
    // Table search functionality
    if (e.target.matches("#tableSearch")) {
      const value = e.target.value.toLowerCase();
      const table = e.target.dataset.target;
      if (!table) return;

      document.querySelectorAll(`${table} tbody tr`).forEach((row) => {
        const visible = row.textContent.toLowerCase().indexOf(value) > -1;
        row.style.display = visible ? "" : "none";
      });
    }

    // Search input Enter key
    if (e.target.matches("#searchInput") && e.key === "Enter") {
      if (window.filterTable) window.filterTable();
    }
  }

  // Initialize CKEditor for all textareas with class 'ckeditor'
  function initCKEditor() {
    if (!window.ClassicEditor) return;

    const editorElements = document.querySelectorAll(".ckeditor");
    if (!editorElements.length) return;

    const editorConfig = {
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
    };

    editorElements.forEach((element) => {
      ClassicEditor.create(element, editorConfig).catch((error) => {
        console.error("CKEditor initialization error:", error);
      });
    });
  }

  // Set default date to today for date inputs
  function setDefaultDates() {
    const startDateInput = document.getElementById("startDate");
    if (!startDateInput || startDateInput.value) return;

    const today = new Date();
    const formattedDate = today.toISOString().split("T")[0];
    startDateInput.value = formattedDate;
  }

  // Initialize all admin functionality
  function initAdmin() {
    initSidebar();
    initDataTables();
    initNavLinks();
    initTooltips();
    initDeleteButtons();
    initFormValidation();
    initToasts();
    initBackToTop();
    initFileUploads();
    initCategoriesPage();
  }

  // Run initializations
  initCKEditor();
  setDefaultDates();
  initAdmin();
});
