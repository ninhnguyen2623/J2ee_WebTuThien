/**
 * Optimized Admin JavaScript
 * Enhanced functionality with performance improvements
 */

(function () {
  "use strict";

  // DOM Elements
  const elements = {
    sidebar: document.querySelector(".sidebar"),
    sidebarToggle: document.getElementById("sidebarToggle"),
    contentWrapper: document.getElementById("content-wrapper"),
    topbar: document.querySelector(".topbar"),
    backToTop: document.querySelector(".back-to-top"),
    filterForm: document.querySelector(".filter-form"),
    searchInput: document.querySelector(".search-input"),
    dataTables: document.querySelectorAll(".data-table"),
    tooltips: document.querySelectorAll('[data-bs-toggle="tooltip"]'),
    ckEditors: document.querySelectorAll(".ckeditor"),
    imagePreviews: document.querySelectorAll(".image-preview-container"),
    formValidation: document.querySelectorAll(".needs-validation"),
    navLinks: document.querySelectorAll(".sidebar .nav-link"),
  };

  // Configuration
  const config = {
    dataTable: {
      language: {
        url: "/js/dataTables.vietnamese.json",
      },
      responsive: true,
      pageLength: 10,
      lengthMenu: [
        [10, 25, 50, -1],
        [10, 25, 50, "Tất cả"],
      ],
      dom:
        '<"row"<"col-sm-12 col-md-6"l><"col-sm-12 col-md-6"f>>' +
        '<"row"<"col-sm-12"tr>>' +
        '<"row"<"col-sm-12 col-md-5"i><"col-sm-12 col-md-7"p>>',
      order: [[0, "desc"]],
    },
    ckEditor: {
      language: "vi",
      removePlugins: "elementspath,resize",
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
        "blockQuote",
        "insertTable",
        "undo",
        "redo",
      ],
    },
  };

  // Utility Functions
  const utils = {
    debounce(func, wait) {
      let timeout;
      return function executedFunction(...args) {
        const later = () => {
          clearTimeout(timeout);
          func(...args);
        };
        clearTimeout(timeout);
        timeout = setTimeout(later, wait);
      };
    },

    throttle(func, limit) {
      let inThrottle;
      return function executedFunction(...args) {
        if (!inThrottle) {
          func(...args);
          inThrottle = true;
          setTimeout(() => (inThrottle = false), limit);
        }
      };
    },

    formatDate(date) {
      return new Date(date).toLocaleDateString("vi-VN", {
        year: "numeric",
        month: "2-digit",
        day: "2-digit",
      });
    },

    formatCurrency(amount) {
      return new Intl.NumberFormat("vi-VN", {
        style: "currency",
        currency: "VND",
      }).format(amount);
    },

    validateForm(form) {
      if (!form.checkValidity()) {
        event.preventDefault();
        event.stopPropagation();
      }
      form.classList.add("was-validated");
    },

    // Thêm hàm mới để xử lý active state cho navbar
    setActiveNavItem() {
      const currentPath = window.location.pathname;
      elements.navLinks.forEach((link) => {
        const href = link.getAttribute("href");
        if (href && currentPath.includes(href)) {
          // Xóa active class từ tất cả các items
          elements.navLinks.forEach((l) => l.classList.remove("active"));
          // Thêm active class cho item hiện tại
          link.classList.add("active");

          // Nếu item nằm trong dropdown, mở dropdown
          const parentCollapse = link.closest(".collapse");
          if (parentCollapse) {
            parentCollapse.classList.add("show");
            const parentLink = document.querySelector(
              `[data-bs-target="#${parentCollapse.id}"]`
            );
            if (parentLink) {
              parentLink.classList.add("active");
              parentLink.setAttribute("aria-expanded", "true");
            }
          }
        }
      });
    },
  };

  // Initialize Components
  const initComponents = {
    sidebar() {
      if (elements.sidebarToggle) {
        elements.sidebarToggle.addEventListener("click", () => {
          elements.sidebar.classList.toggle("toggled");
          elements.contentWrapper.classList.toggle("toggled");
          elements.topbar.classList.toggle("toggled");
        });
      }

      // Thêm event listeners cho các nav links
      elements.navLinks.forEach((link) => {
        link.addEventListener("click", function (e) {
          // Xóa active class từ tất cả các items
          elements.navLinks.forEach((l) => l.classList.remove("active"));
          // Thêm active class cho item được click
          this.classList.add("active");
        });
      });

      // Set active state khi trang load
      utils.setActiveNavItem();
    },

    dataTables() {
      elements.dataTables.forEach((table) => {
        if (table) {
          new DataTable(table, config.dataTable);
        }
      });
    },

    tooltips() {
      elements.tooltips.forEach((tooltip) => {
        if (tooltip) {
          new bootstrap.Tooltip(tooltip);
        }
      });
    },

    ckEditors() {
      elements.ckEditors.forEach((editor) => {
        if (editor) {
          ClassicEditor.create(editor, config.ckEditor).catch((error) =>
            console.error(error)
          );
        }
      });
    },

    imagePreviews() {
      elements.imagePreviews.forEach((container) => {
        const input = container.querySelector('input[type="file"]');
        const preview = container.querySelector(".image-preview");
        const noImageIcon = container.querySelector(".no-image-icon");

        if (input && preview) {
          input.addEventListener("change", (e) => {
            const file = e.target.files[0];
            if (file) {
              const reader = new FileReader();
              reader.onload = (e) => {
                preview.src = e.target.result;
                preview.style.display = "block";
                if (noImageIcon) noImageIcon.style.display = "none";
                container.classList.remove("image-error");
              };
              reader.onerror = () => {
                container.classList.add("image-error");
                preview.style.display = "none";
                if (noImageIcon) noImageIcon.style.display = "flex";
              };
              reader.readAsDataURL(file);
            }
          });
        }
      });
    },

    formValidation() {
      elements.formValidation.forEach((form) => {
        if (form) {
          form.addEventListener("submit", (event) => {
            utils.validateForm(form);
          });
        }
      });
    },

    backToTop() {
      if (elements.backToTop) {
        const scrollHandler = utils.throttle(() => {
          if (window.pageYOffset > 100) {
            elements.backToTop.classList.add("active");
          } else {
            elements.backToTop.classList.remove("active");
          }
        }, 100);

        window.addEventListener("scroll", scrollHandler);
        elements.backToTop.addEventListener("click", () => {
          window.scrollTo({ top: 0, behavior: "smooth" });
        });
      }
    },

    searchFilter() {
      if (elements.searchInput) {
        const searchHandler = utils.debounce((e) => {
          const searchTerm = e.target.value.toLowerCase();
          const table = document.querySelector(".data-table");
          if (table) {
            const rows = table.querySelectorAll("tbody tr");
            rows.forEach((row) => {
              const text = row.textContent.toLowerCase();
              row.style.display = text.includes(searchTerm) ? "" : "none";
            });
          }
        }, 300);

        elements.searchInput.addEventListener("input", searchHandler);
      }
    },
  };

  // Initialize Admin UI
  function initAdminUI() {
    // Initialize all components
    Object.values(initComponents).forEach((init) => init());

    // Add scroll shadow to topbar
    if (elements.topbar) {
      const scrollHandler = utils.throttle(() => {
        if (window.pageYOffset > 0) {
          elements.topbar.classList.add("shadow-scroll");
        } else {
          elements.topbar.classList.remove("shadow-scroll");
        }
      }, 100);

      window.addEventListener("scroll", scrollHandler);
    }

    // Handle window resize
    const resizeHandler = utils.debounce(() => {
      if (window.innerWidth <= 768) {
        elements.sidebar.classList.remove("toggled");
        elements.contentWrapper.classList.remove("toggled");
        elements.topbar.classList.remove("toggled");
      }
    }, 250);

    window.addEventListener("resize", resizeHandler);
  }

  // Initialize when DOM is ready
  if (document.readyState === "loading") {
    document.addEventListener("DOMContentLoaded", initAdminUI);
  } else {
    initAdminUI();
  }
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

/**
 * Reusable UI components and functions
 * These functions handle common UI patterns across admin pages
 */
const AdminUI = {
  /**
   * Initialize all detail pages with consistent behavior
   */
  initDetailPage: function () {
    // Add back button behavior
    const backButtons = document.querySelectorAll(".btn-back, .btn-return");
    backButtons.forEach((btn) => {
      btn.addEventListener("click", function (e) {
        if (this.getAttribute("href") === "#" || !this.getAttribute("href")) {
          e.preventDefault();
          window.history.back();
        }
      });
    });

    // Initialize status badges
    this.initStatusBadges();
  },

  /**
   * Initialize all form pages with consistent behavior
   */
  initFormPage: function () {
    // Form validation
    const forms = document.querySelectorAll(".admin-form");
    forms.forEach((form) => {
      form.addEventListener("submit", function (e) {
        if (!this.checkValidity()) {
          e.preventDefault();
          e.stopPropagation();
        }
        this.classList.add("was-validated");
      });
    });

    // Help toggles
    const helpToggles = document.querySelectorAll(".help-toggle");
    helpToggles.forEach((toggle) => {
      toggle.addEventListener("click", function (e) {
        e.preventDefault();
        const helpSection = document.querySelector(
          this.getAttribute("data-target")
        );
        if (helpSection) {
          helpSection.classList.toggle("d-none");

          // Update icon
          const icon = this.querySelector("i");
          if (icon) {
            if (helpSection.classList.contains("d-none")) {
              icon.classList.replace("fa-chevron-up", "fa-chevron-down");
            } else {
              icon.classList.replace("fa-chevron-down", "fa-chevron-up");
            }
          }
        }
      });
    });

    // Toggle switches for status
    const statusSwitches = document.querySelectorAll(".status-switch");
    statusSwitches.forEach((switchEl) => {
      switchEl.addEventListener("change", function () {
        const statusValue = document.querySelector(
          this.getAttribute("data-target")
        );
        if (statusValue) {
          statusValue.value = this.checked ? "1" : "0";
        }

        // Update status label if exists
        const statusLabel = document.querySelector(
          this.getAttribute("data-label")
        );
        if (statusLabel) {
          statusLabel.textContent = this.checked
            ? "Đang hoạt động"
            : "Không hoạt động";
          statusLabel.className = this.checked
            ? "badge bg-success-light text-success"
            : "badge bg-danger-light text-danger";
        }
      });
    });
  },

  /**
   * Initialize list/index pages with consistent behavior
   */
  initListPage: function () {
    // Reset filters
    const resetButtons = document.querySelectorAll(".reset-filters");
    resetButtons.forEach((btn) => {
      btn.addEventListener("click", function (e) {
        e.preventDefault();
        const form = this.closest("form");
        if (form) {
          // Reset all inputs except submit buttons
          const inputs = form.querySelectorAll(
            'input:not([type="submit"]), select, textarea'
          );
          inputs.forEach((input) => {
            if (input.type === "checkbox" || input.type === "radio") {
              input.checked = input.defaultChecked;
            } else {
              input.value = input.defaultValue;
            }
          });

          // Submit the form
          form.submit();
        } else {
          // If no form found, just navigate to the href
          window.location.href = this.getAttribute("href");
        }
      });
    });

    // Initialize enhanced search
    this.initEnhancedSearch();

    // Initialize status badges
    this.initStatusBadges();

    // Initialize confirm dialogs
    this.initConfirmDialogs();
  },

  /**
   * Initialize enhanced search functionality
   */
  initEnhancedSearch: function () {
    // Real-time search feature
    const searchInput = document.getElementById("searchTerm");
    if (searchInput) {
      let typingTimer;
      const doneTypingInterval = 500; // 500ms delay after user stops typing

      searchInput.addEventListener("input", function () {
        clearTimeout(typingTimer);
        if (searchInput.value.length >= 2) {
          // Only search if 2+ characters typed
          searchInput.classList.add("typing");
          typingTimer = setTimeout(function () {
            const form = searchInput.closest("form");
            if (form) form.submit();
          }, doneTypingInterval);
        } else {
          searchInput.classList.remove("typing");
        }
      });
    }
  },

  /**
   * Clear search term
   */
  clearSearchTerm: function () {
    document.getElementById("searchTerm").value = "";
    document.getElementById("filterForm").submit();
  },

  /**
   * Clear program filter
   */
  clearProgramId: function () {
    document.getElementById("programId").value = "";
    document.getElementById("filterForm").submit();
  },

  /**
   * Clear amount range filters
   */
  clearAmountRange: function () {
    document.getElementById("minAmount").value = "";
    document.getElementById("maxAmount").value = "";
    document.getElementById("filterForm").submit();
  },

  /**
   * Initialize status badges with appropriate classes
   */
  initStatusBadges: function () {
    const statusBadges = document.querySelectorAll(".status-badge");
    statusBadges.forEach((badge) => {
      const status = badge.getAttribute("data-status");
      if (status === "true" || status === "1" || status === "active") {
        badge.classList.add("success");
      } else if (
        status === "false" ||
        status === "0" ||
        status === "inactive"
      ) {
        badge.classList.add("danger");
      } else if (status === "pending") {
        badge.classList.add("warning");
      }
    });
  },

  /**
   * Initialize confirmation dialogs for delete actions
   */
  initConfirmDialogs: function () {
    const confirmButtons = document.querySelectorAll("[data-confirm]");
    confirmButtons.forEach((btn) => {
      btn.addEventListener("click", function (e) {
        if (
          !confirm(
            this.getAttribute("data-confirm") ||
              "Bạn có chắc chắn muốn thực hiện thao tác này?"
          )
        ) {
          e.preventDefault();
        }
      });
    });
  },

  /**
   * Initialize image preview functionality
   */
  initImagePreviews: function () {
    const imageInputs = document.querySelectorAll(".image-input");
    imageInputs.forEach((input) => {
      input.addEventListener("change", function () {
        const previewContainer = document.querySelector(
          this.getAttribute("data-preview")
        );
        if (previewContainer && this.files && this.files[0]) {
          const reader = new FileReader();
          reader.onload = function (e) {
            const previewImage = previewContainer.querySelector("img");
            if (previewImage) {
              previewImage.src = e.target.result;
              previewImage.style.display = "block";
            }

            // Hide no-image icon if it exists
            const noImageIcon =
              previewContainer.querySelector(".no-image-icon");
            if (noImageIcon) {
              noImageIcon.style.display = "none";
            }
          };
          reader.readAsDataURL(this.files[0]);
        }
      });
    });
  },
};

// Initialize components when DOM is loaded
document.addEventListener("DOMContentLoaded", function () {
  // Determine page type and initialize appropriate components
  const pageBody = document.body;

  if (pageBody.classList.contains("detail-page")) {
    AdminUI.initDetailPage();
  }

  if (pageBody.classList.contains("form-page")) {
    AdminUI.initFormPage();
  }

  if (pageBody.classList.contains("list-page")) {
    AdminUI.initListPage();
  }

  // Always initialize image previews
  AdminUI.initImagePreviews();
});

// Dashboard Charts Initialization
document.addEventListener("DOMContentLoaded", function () {
  // Debugging data objects
  console.log("Donations by day:", "[(${donationsByDay})]");
  console.log("Donations by category:", "[(${donationsByCategory})]");

  // Format currency in VND
  function formatCurrency(value) {
    return new Intl.NumberFormat("vi-VN", {
      style: "currency",
      currency: "VND",
      maximumFractionDigits: 0,
    }).format(value);
  }

  // Donations over time chart
  if (document.getElementById("donationTimeChart")) {
    const donationTimeData = {
      labels: Object.keys(
        JSON.parse('[(${donationsByDay != null ? donationsByDay : "{}"})]')
      ),
      datasets: [
        {
          label: "Số tiền quyên góp",
          data: Object.values(
            JSON.parse('[(${donationsByDay != null ? donationsByDay : "{}"})]')
          ),
          backgroundColor: "rgba(78, 115, 223, 0.2)",
          borderColor: "rgba(78, 115, 223, 1)",
          borderWidth: 2,
          tension: 0.4,
          fill: true,
        },
      ],
    };

    const donationTimeConfig = {
      type: "line",
      data: donationTimeData,
      options: {
        maintainAspectRatio: false,
        plugins: {
          legend: {
            position: "top",
          },
          tooltip: {
            callbacks: {
              label: function (context) {
                return formatCurrency(context.raw);
              },
            },
          },
        },
        scales: {
          y: {
            beginAtZero: true,
            ticks: {
              callback: function (value) {
                return formatCurrency(value);
              },
            },
          },
        },
      },
    };

    new Chart(
      document.getElementById("donationTimeChart").getContext("2d"),
      donationTimeConfig
    );
  }

  // Category distribution chart
  if (document.getElementById("categoryChart")) {
    const categoryData = {
      labels: Object.keys(
        JSON.parse(
          '[(${donationsByCategory != null ? donationsByCategory : "{}"})]'
        )
      ),
      datasets: [
        {
          data: Object.values(
            JSON.parse(
              '[(${donationsByCategory != null ? donationsByCategory : "{}"})]'
            )
          ),
          backgroundColor: [
            "rgba(78, 115, 223, 0.8)",
            "rgba(28, 200, 138, 0.8)",
            "rgba(246, 194, 62, 0.8)",
            "rgba(231, 74, 59, 0.8)",
            "rgba(54, 185, 204, 0.8)",
          ],
          borderWidth: 1,
        },
      ],
    };

    const categoryConfig = {
      type: "doughnut",
      data: categoryData,
      options: {
        maintainAspectRatio: false,
        plugins: {
          legend: {
            position: "bottom",
            labels: {
              boxWidth: 12,
            },
          },
          tooltip: {
            callbacks: {
              label: function (context) {
                const total = context.dataset.data.reduce(
                  (acc, val) => acc + val,
                  0
                );
                const percentage = Math.round((context.raw / total) * 100);
                return `${context.label}: ${formatCurrency(
                  context.raw
                )} (${percentage}%)`;
              },
            },
          },
        },
        cutout: "60%",
      },
    };

    new Chart(
      document.getElementById("categoryChart").getContext("2d"),
      categoryConfig
    );
  }
});
