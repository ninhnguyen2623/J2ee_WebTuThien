/**
 * Optimized Admin JavaScript
 * Improved code organization and removed duplications
 */

// Immediately-invoked Function Expression (IIFE) to avoid global scope pollution
(function () {
  "use strict"; // Use strict mode

  // ======== DOM Element Selectors ========
  // Store frequently used DOM selectors for performance
  const DOM = {
    body: document.body,
    sidebar: document.querySelector(".sidebar"),
    sidebarToggle: document.querySelector("#sidebarToggle"),
    backToTop: document.querySelector(".back-to-top"),
    dataTables: document.querySelectorAll(".dataTable"),
    navLinks: document.querySelectorAll(".sidebar .nav-link"),
    tooltipTriggers: document.querySelectorAll('[data-bs-toggle="tooltip"]'),
    forms: document.querySelectorAll(".needs-validation"),
    categoriesTable: document.getElementById("categoriesTable"),
    programsTable: document.getElementById("programsTable"),
    fileInputs: document.querySelectorAll('input[type="file"]'),
    imagePreviewContainers: document.querySelectorAll(
      ".image-preview-container"
    ),
    imagePreviews: document.querySelectorAll(".image-preview"),
    ckEditorFields: document.querySelectorAll(".ckeditor"),
    searchInput: document.getElementById("searchInput"),
    filterForm: document.getElementById("filterForm"),
    programForm: document.getElementById("programForm"),
    categoryForm: document.getElementById("categoryForm"),
  };

  // ======== Configuration Settings ========
  const CONFIG = {
    // DataTables default configuration
    dataTable: {
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
    },
    // Default rows per page for pagination
    rowsPerPage: 10,
    // CKEditor default configuration
    ckEditor: {
      toolbar: {
        items: [
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
          "uploadImage",
          "blockQuote",
          "insertTable",
          "undo",
          "redo",
        ],
      },
      language: "vi",
      image: {
        toolbar: [
          "imageTextAlternative",
          "imageStyle:inline",
          "imageStyle:block",
          "imageStyle:side",
        ],
      },
      table: {
        contentToolbar: ["tableColumn", "tableRow", "mergeTableCells"],
      },
    },
  };

  // ======== Utility Functions ========
  // Reusable utility functions
  const UTILS = {
    // Throttle function for performance optimizations
    throttle: (func, delay) => {
      let inProgress = false;
      return (...args) => {
        if (inProgress) return;
        inProgress = true;
        setTimeout(() => {
          func(...args);
          inProgress = false;
        }, delay);
      };
    },

    // Check if an element exists
    exists: (element) => element !== null && element !== undefined,

    // Format date for display
    formatDate: (date) => {
      if (!date) return "";
      const d = new Date(date);
      return d.toLocaleDateString("vi-VN");
    },

    // Validate date range
    validateDateRange: (startDate, endDate) => {
      if (!startDate || !endDate) return true;
      return new Date(endDate) > new Date(startDate);
    },
  };

  // ======== Core Initialization ========
  // Main initialization function to be called on DOM ready
  function initAdminUI() {
    initSidebar();
    initDataTables();
    initNavLinks();
    initTooltips();
    initFormValidation();
    initBackToTop();
    initDataListings();
    initImagePreviews();
    initCKEditor();
  }

  // ======== Sidebar Functions ========
  // Initialize sidebar functionality
  function initSidebar() {
    if (!UTILS.exists(DOM.sidebarToggle)) return;

    DOM.sidebarToggle.addEventListener("click", (event) => {
      event.preventDefault();
      DOM.body.classList.toggle("sidebar-toggled");
      DOM.sidebar.classList.toggle("toggled");
    });

    // Responsive sidebar handling - close on small screens
    const handleResize = () => {
      if (window.innerWidth < 768) {
        DOM.body.classList.add("sidebar-toggled");
        DOM.sidebar.classList.add("toggled");
      }
    };

    // Call once and add listener
    handleResize();
    window.addEventListener("resize", UTILS.throttle(handleResize, 100));

    // Prevent the content wrapper from scrolling when the fixed side navigation hovered over
    DOM.body.addEventListener("scroll", (event) => {
      const navbar = DOM.body.querySelector(".navbar-nav");
      if (
        navbar &&
        navbar.classList.contains("overflow-auto") &&
        window.getComputedStyle(navbar).overflow === "auto"
      ) {
        event.preventDefault();
      }
    });
  }

  // ======== DataTables Functions ========
  // Initialize DataTables for tables with .dataTable class
  function initDataTables() {
    if (!DOM.dataTables.length || !$.fn.dataTable) return;

    DOM.dataTables.forEach((table) => {
      if (!$.fn.dataTable.isDataTable(table)) {
        $(table).DataTable(CONFIG.dataTable);
      }
    });
  }

  // ======== Navigation Functions ========
  // Add active class to current navigation link
  function initNavLinks() {
    if (!DOM.navLinks.length) return;

    const currentPageUrl = window.location.pathname;
    DOM.navLinks.forEach((link) => {
      if (link.getAttribute("href") === currentPageUrl) {
        link.classList.add("active");
      }
    });
  }

  // ======== UI Components ========
  // Initialize Bootstrap tooltips
  function initTooltips() {
    if (!DOM.tooltipTriggers.length || !bootstrap?.Tooltip) return;
    DOM.tooltipTriggers.forEach((el) => new bootstrap.Tooltip(el));
  }

  // Initialize form validation for forms with .needs-validation class
  function initFormValidation() {
    if (!DOM.forms.length) return;

    DOM.forms.forEach((form) => {
      form.addEventListener("submit", function (event) {
        if (!form.checkValidity()) {
          event.preventDefault();
          event.stopPropagation();
        }

        // Specific validations based on form ID
        if (form.id === "programForm") {
          validateProgramForm(form, event);
        } else if (form.id === "categoryForm") {
          validateCategoryForm(form, event);
        }

        form.classList.add("was-validated");
      });
    });
  }

  // Validate program form
  function validateProgramForm(form, event) {
    const startDate = document.getElementById("startDate")?.value;
    const endDate = document.getElementById("endDate")?.value;

    if (!UTILS.validateDateRange(startDate, endDate)) {
      event.preventDefault();
      const endDateFeedback = document.getElementById("endDateFeedback");
      if (endDateFeedback) {
        endDateFeedback.textContent = "Ngày kết thúc phải sau ngày bắt đầu";
        document.getElementById("endDate").classList.add("is-invalid");
      }
    }
  }

  // Validate category form
  function validateCategoryForm(form, event) {
    const nameInput = form.querySelector("#name");
    if (nameInput && nameInput.dataset.original) {
      const originalName = nameInput.dataset.original;
      const currentName = nameInput.value;

      // Confirm major changes
      if (originalName && currentName !== originalName) {
        if (
          !confirm(
            "Bạn đang thay đổi tên danh mục. Điều này có thể ảnh hưởng đến các chiến dịch liên quan. Bạn có chắc chắn muốn tiếp tục?"
          )
        ) {
          event.preventDefault();
        }
      }
    }
  }

  // Initialize back to top button
  function initBackToTop() {
    if (!UTILS.exists(DOM.backToTop)) return;

    const scrollHandler = () => {
      if (window.scrollY > 100) {
        DOM.backToTop.classList.add("active");
      } else {
        DOM.backToTop.classList.remove("active");
      }
    };

    // Throttle scroll event for performance
    window.addEventListener("scroll", UTILS.throttle(scrollHandler, 100));

    DOM.backToTop.addEventListener("click", (event) => {
      event.preventDefault();
      window.scrollTo({
        top: 0,
        behavior: "smooth",
      });
    });
  }

  // ======== Data Listings Management ========
  // Initialize categories and programs tables functionality
  function initDataListings() {
    if (UTILS.exists(DOM.categoriesTable)) {
      initDataTable("category");
    } else if (UTILS.exists(DOM.programsTable)) {
      initDataTable("program");
    }
  }

  // Generic data table initialization for both categories and programs
  function initDataTable(type) {
    const tableElement = DOM[`${type}sTable`];
    const tableRows = document.querySelectorAll(`.${type}-row`);
    const sortSelect = document.getElementById("sortOrder");

    if (!tableElement || !tableRows.length) return;

    // Shared variables
    window.currentPage = 0;

    // Display rows function for pagination
    window.displayRows = function (page) {
      const rowsPerPage = CONFIG.rowsPerPage;
      const start = page * rowsPerPage;
      const end = start + rowsPerPage;
      let visibleCount = 0;

      // Get all visible (non-filtered) rows
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
      if (DOM.searchInput && DOM.searchInput.value.trim() !== "") {
        updatePagination();
        updateRowCountDisplay(type);
      }

      return visibleCount;
    };

    // Initialize search functionality
    if (DOM.searchInput) {
      DOM.searchInput.addEventListener("input", function () {
        const searchTerm = this.value.toLowerCase().trim();

        // Filter rows based on search term
        tableRows.forEach((row) => {
          const text = row.textContent.toLowerCase();
          const isVisible = text.includes(searchTerm);

          row.dataset.hiddenBySearch = isVisible ? "false" : "true";
        });

        // Reset to first page and redisplay
        window.currentPage = 0;
        window.displayRows(0);
      });
    }

    // Initialize sort functionality
    if (sortSelect) {
      sortSelect.addEventListener("change", function () {
        const sortBy = this.value;
        const rows = Array.from(tableRows);

        // Sort rows based on selected criterion
        rows.sort((a, b) => {
          let aValue, bValue;

          switch (sortBy) {
            case "name-asc":
              aValue = a.querySelector("td:nth-child(2)").textContent;
              bValue = b.querySelector("td:nth-child(2)").textContent;
              return aValue.localeCompare(bValue);
            case "name-desc":
              aValue = a.querySelector("td:nth-child(2)").textContent;
              bValue = b.querySelector("td:nth-child(2)").textContent;
              return bValue.localeCompare(aValue);
            case "newest":
              aValue = a.dataset.created;
              bValue = b.dataset.created;
              return bValue - aValue;
            case "oldest":
              aValue = a.dataset.created;
              bValue = b.dataset.created;
              return aValue - bValue;
            default:
              return 0;
          }
        });

        // Re-append rows in the new order
        const tbody = tableElement.querySelector("tbody");
        rows.forEach((row) => tbody.appendChild(row));

        // Redisplay with current pagination
        window.displayRows(window.currentPage);
      });
    }

    // Initialize pagination
    initPagination();

    // First page display
    window.displayRows(0);
  }

  // Update row count display in the UI
  function updateRowCountDisplay(type) {
    const tableRows = document.querySelectorAll(`.${type}-row`);
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
    if (!DOM.searchInput || DOM.searchInput.value.trim() === "") return;

    const tableRows = document.querySelectorAll(".category-row, .program-row");
    const visibleRows = Array.from(tableRows).filter(
      (row) => row.dataset.hiddenBySearch !== "true"
    );
    const totalPages = Math.ceil(visibleRows.length / CONFIG.rowsPerPage);

    const paginationElement = document.querySelector(".pagination");
    if (!paginationElement) return;

    // Update active state
    const pageItems = paginationElement.querySelectorAll(".page-item");
    pageItems.forEach((item, i) => {
      if (i === 0) {
        // Previous button
        item.classList.toggle("disabled", window.currentPage === 0);
      } else if (i === pageItems.length - 1) {
        // Next button
        item.classList.toggle("disabled", window.currentPage >= totalPages - 1);
      } else {
        // Page number buttons
        const pageNum = i - 1;
        item.style.display = pageNum < totalPages ? "" : "none";
        item.classList.toggle("active", pageNum === window.currentPage);
      }
    });
  }

  // Initialize pagination for tables
  function initPagination() {
    const paginationElement = document.querySelector(".pagination");
    if (!paginationElement) return;

    const pageItems = paginationElement.querySelectorAll(".page-item");
    pageItems.forEach((item, i) => {
      const pageLink = item.querySelector(".page-link");

      if (!pageLink) return;

      pageLink.addEventListener("click", function (e) {
        e.preventDefault();

        // Handle prev/next buttons
        if (i === 0) {
          // Previous button
          if (window.currentPage > 0) {
            window.currentPage--;
          }
        } else if (i === pageItems.length - 1) {
          // Next button
          const tableRows = document.querySelectorAll(
            ".category-row, .program-row"
          );
          const visibleRows = Array.from(tableRows).filter(
            (row) => row.dataset.hiddenBySearch !== "true"
          );
          const totalPages = Math.ceil(visibleRows.length / CONFIG.rowsPerPage);

          if (window.currentPage < totalPages - 1) {
            window.currentPage++;
          }
        } else {
          // Page number button
          window.currentPage = i - 1;
        }

        // Display the new page
        window.displayRows(window.currentPage);
        updatePagination();
      });
    });
  }

  // ======== Image Preview Functions ========
  // Handle file input change for image previews
  function initImagePreviews() {
    if (!DOM.fileInputs.length) return;

    DOM.fileInputs.forEach((fileInput) => {
      fileInput.addEventListener("change", function () {
        const previewTarget = this.dataset.preview;
        const preview = previewTarget
          ? document.getElementById(previewTarget)
          : this.closest(".form-group")?.querySelector(".image-preview");

        if (!preview) return;

        if (this.files && this.files[0]) {
          const reader = new FileReader();

          reader.onload = function (e) {
            preview.src = e.target.result;
            preview.classList.remove("d-none");
            preview.style.display = "block";

            // Đặt lại trạng thái lỗi nếu có
            const previewContainer = preview.closest(
              ".image-preview-container"
            );
            if (previewContainer) {
              previewContainer.classList.remove("image-error");
              const textElement =
                previewContainer.querySelector(".mt-2 .text-muted");
              if (textElement) {
                textElement.classList.remove("d-none");
                textElement.textContent = "Ảnh xem trước";
              }
            }
          };

          // Xử lý lỗi khi đọc file
          reader.onerror = function () {
            // Thay thế img bằng icon
            const noImageIcon = document.createElement("div");
            noImageIcon.className = "no-image-icon";
            noImageIcon.innerHTML =
              '<i class="fas fa-file-image text-warning fa-3x"></i>';

            if (preview.parentNode) {
              preview.parentNode.insertBefore(noImageIcon, preview);
              preview.style.display = "none";
            }

            // Hiển thị thông báo lỗi
            const previewContainer = preview.closest(
              ".image-preview-container"
            );
            if (previewContainer) {
              previewContainer.classList.add("image-error");
              const textElement =
                previewContainer.querySelector(".mt-2 .text-muted");
              if (textElement) {
                textElement.classList.remove("d-none");
                textElement.innerHTML =
                  '<i class="fas fa-exclamation-triangle text-warning me-1"></i>Không thể đọc file ảnh';
              }
            }
          };

          reader.readAsDataURL(this.files[0]);
        } else {
          preview.src = "#";
          preview.classList.add("d-none");
        }
      });
    });

    // Xử lý lỗi cho các hình ảnh hiện có
    document.querySelectorAll(".image-preview").forEach((img) => {
      if (!img.getAttribute("onerror")) {
        img.onerror = function () {
          this.onerror = null;

          // Tạo icon thay thế
          const noImageIcon = document.createElement("div");
          noImageIcon.className = "no-image-icon";
          noImageIcon.innerHTML =
            '<i class="fas fa-file-image text-warning fa-3x"></i>';

          // Thêm icon vào DOM
          if (this.parentNode) {
            this.parentNode.insertBefore(noImageIcon, this);
            this.style.display = "none";
          }

          // Thêm class error và thông báo
          const container = this.closest(".image-preview-container");
          if (container) {
            container.classList.add("image-error");
            const textElement = container.querySelector(".mt-2 .text-muted");
            if (textElement) {
              textElement.classList.remove("d-none");
              textElement.innerHTML =
                '<i class="fas fa-exclamation-triangle text-warning me-1"></i>Hình ảnh bị lỗi hoặc không tồn tại';
            }
          }
        };
      }
    });
  }

  // ======== Rich Text Editor ========
  // Initialize CKEditor for rich text fields
  function initCKEditor() {
    if (!DOM.ckEditorFields.length || !window.ClassicEditor) return;

    DOM.ckEditorFields.forEach((editorField) => {
      ClassicEditor.create(editorField, CONFIG.ckEditor)
        .then((editor) => {
          console.log("CKEditor initialized successfully");

          // Store the editor instance if needed for later reference
          editorField.ckeditorInstance = editor;

          // Update preview if there's a preview container
          const previewContainer = document.querySelector(
            ".ckeditor-preview-container"
          );
          if (previewContainer) {
            editor.model.document.on("change:data", () => {
              previewContainer.innerHTML = editor.getData();
            });
          }
        })
        .catch((error) => {
          console.error("CKEditor initialization error:", error);
        });
    });
  }

  // ======== Initialize on DOM Ready ========
  document.addEventListener("DOMContentLoaded", initAdminUI);

  // ======== Public Methods ========
  // Expose functions that need to be accessible globally
  window.resetFilters = function () {
    // Reset all form inputs for filters
    const filterForm = document.getElementById("filterForm");
    if (!filterForm) return;

    // Reset all inputs
    const inputs = filterForm.querySelectorAll("input, select");
    inputs.forEach((input) => {
      if (input.type === "text" || input.tagName === "SELECT") {
        input.value = "";
      } else if (input.type === "checkbox" || input.type === "radio") {
        input.checked = false;
      }
    });

    // Submit the form
    filterForm.submit();
  };
})();

/**
 * Main event handlers for Categories
 */
document.addEventListener("DOMContentLoaded", function () {
  // Handle reset filter button click
  document.addEventListener("click", function (e) {
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
  });

  // Handle form validation
  const forms = document.querySelectorAll(".needs-validation");
  if (forms.length) {
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

  // Confirm before major changes
  const categoryForms = document.querySelectorAll("#categoryForm");
  if (categoryForms.length) {
    categoryForms.forEach((form) => {
      const nameInput = form.querySelector("#name");
      if (nameInput) {
        const originalName = nameInput.value;

        form.addEventListener("submit", function (event) {
          // Only confirm if we're editing an existing category (not creating a new one)
          if (originalName && nameInput.value !== originalName) {
            if (
              !confirm(
                "Bạn đang thay đổi tên danh mục. Điều này có thể ảnh hưởng đến các chiến dịch liên quan. Bạn có chắc chắn muốn tiếp tục?"
              )
            ) {
              event.preventDefault();
            }
          }
        });
      }
    });
  }
});

function resetFilters() {
  document.getElementById("category").value = "";
  document.getElementById("status").value = "";
  document.getElementById("search").value = "";
  document.getElementById("filterForm").submit();
}

/**
 * Initialize form validation for payment methods
 */
function initPaymentMethodForms() {
  const forms = document.querySelectorAll(".needs-validation");

  Array.from(forms).forEach((form) => {
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

// Add initialization for payment methods to our core init function
document.addEventListener("DOMContentLoaded", function () {
  // Initialize existing components
  if (typeof initAdminUI === "function") {
    initAdminUI();
  }

  // Initialize tooltips if not already handled
  const tooltipTriggerList = [].slice.call(
    document.querySelectorAll('[data-bs-toggle="tooltip"]')
  );
  tooltipTriggerList.forEach(function (tooltipTriggerEl) {
    new bootstrap.Tooltip(tooltipTriggerEl);
  });

  // Initialize payment method forms
  initPaymentMethodForms();
});

/**
 * Các hàm quản lý người dùng
 * Xử lý các tương tác trên trang quản lý người dùng
 */
function initUserManagementForms() {
  const userForm = document.getElementById("userForm");
  const phoneInput = document.getElementById("phoneNumber");
  const passwordInput = document.getElementById("password");
  const roleDropdown = document.getElementById("roleId");

  // Xác thực (validation) form người dùng
  if (userForm) {
    userForm.addEventListener("submit", function (event) {
      if (!userForm.checkValidity()) {
        event.preventDefault();
        event.stopPropagation();
      }
      userForm.classList.add("was-validated");
    });
  }

  // Tự động thiết lập mật khẩu bằng số điện thoại (chỉ cho trang tạo mới)
  if (phoneInput && passwordInput && window.location.href.includes("/create")) {
    phoneInput.addEventListener("input", function () {
      // Đặt mật khẩu bằng số điện thoại
      passwordInput.value = this.value;
    });

    // Đảm bảo mật khẩu được thiết lập khi gửi form
    if (userForm) {
      userForm.addEventListener("submit", function (event) {
        if (phoneInput.value && phoneInput.value.match(/^\d{10,11}$/)) {
          passwordInput.value = phoneInput.value;
        } else {
          if (!userForm.checkValidity()) {
            event.preventDefault();
          }
        }
      });
    }
  }

  // Xử lý xác nhận khi xóa người dùng
  const deleteButtons = document.querySelectorAll(".btn-delete");
  if (deleteButtons.length) {
    deleteButtons.forEach((btn) => {
      btn.addEventListener("click", function (e) {
        if (!confirm("Bạn có chắc chắn muốn xóa người dùng này?")) {
          e.preventDefault();
        }
      });
    });
  }

  // Khởi tạo form lọc và tìm kiếm
  const filterForm = document.getElementById("filterForm");
  const clearFiltersBtn = document.getElementById("clearFilters");

  if (filterForm && clearFiltersBtn) {
    clearFiltersBtn.addEventListener("click", function (e) {
      e.preventDefault();

      // Đặt lại tất cả các trường nhập liệu
      const inputs = filterForm.querySelectorAll(
        "input:not([type=hidden]), select"
      );
      inputs.forEach((input) => {
        if (input.type === "text" || input.tagName === "SELECT") {
          input.value = "";
        }
      });

      // Đặt lại số trang về 0
      const pageInput = document.getElementById("pageInput");
      if (pageInput) pageInput.value = 0;

      // Gửi form để thực hiện tìm kiếm với các giá trị đã đặt lại
      filterForm.submit();
    });
  }
}

// Khởi tạo chức năng quản lý người dùng khi trang đã tải xong
document.addEventListener("DOMContentLoaded", function () {
  // Khởi tạo các component có sẵn
  if (typeof initAdminUI === "function") {
    initAdminUI();
  }

  // Khởi tạo các chức năng cụ thể cho quản lý người dùng
  initUserManagementForms();
});
