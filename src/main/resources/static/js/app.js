// ==========================================
// APP.JS
// ==========================================

document.addEventListener("DOMContentLoaded", () => {
  // ==========================================
  // Sidebar Toggle
  // ==========================================

  const toggle = document.getElementById("sidebarToggle");
  const sidebar = document.getElementById("sidebar");

  if (toggle && sidebar) {
    toggle.addEventListener("click", () => {
      sidebar.classList.toggle("active");
    });
  }

  // ==========================================
  // Live Date & Time
  // ==========================================

  const dateTime = document.getElementById("currentDateTime");

  function updateClock() {
    if (!dateTime) return;

    const now = new Date();

    const date = now.toLocaleDateString("en-IN", {
      weekday: "long",
      day: "2-digit",
      month: "long",
      year: "numeric",
    });

    const time = now.toLocaleTimeString("en-IN", {
      hour: "2-digit",
      minute: "2-digit",
      second: "2-digit",
    });

    dateTime.innerHTML = `
            <div>${date}</div>
            <div>${time}</div>
        `;
  }

  updateClock();

  setInterval(updateClock, 1000);

  // ==========================================
  // Theme Toggle
  // ==========================================

  const themeToggle = document.getElementById("themeToggle");
  const themeIcon = document.getElementById("themeIcon");

  function setTheme(theme) {
    if (theme === "dark") {
      document.documentElement.classList.add("dark");

      if (themeIcon) {
        themeIcon.className = "bi bi-sun-fill";
      }
    } else {
      document.documentElement.classList.remove("dark");

      if (themeIcon) {
        themeIcon.className = "bi bi-moon-stars-fill";
      }
    }

    localStorage.setItem("theme", theme);
  }

  const savedTheme = localStorage.getItem("theme") || "light";

  setTheme(savedTheme);

  if (themeToggle) {
    themeToggle.addEventListener("click", () => {
      const isDark = document.documentElement.classList.contains("dark");

      setTheme(isDark ? "light" : "dark");
    });
  }

  // ==========================================
  // Bootstrap Tooltip
  // ==========================================

  if (window.bootstrap) {
    document
      .querySelectorAll('[data-bs-toggle="tooltip"]')
      .forEach((element) => {
        new bootstrap.Tooltip(element);
      });
  }

  // ==========================================
  // Global Search
  // ==========================================

  const searchTrigger = document.getElementById("globalSearchTrigger");
  const searchPanel = document.getElementById("globalSearchPanel");
  const searchInput = document.getElementById("globalSearchInput");
  const searchResults = document.getElementById("globalSearchResults");
  const searchClose = document.getElementById("globalSearchClose");
  const searchFooter = document.getElementById("globalSearchFooter");
  let searchTimer = null;
  let searchItems = [];
  let activeSearchIndex = -1;

  function openGlobalSearch() {
    if (!searchPanel || !searchInput) return;
    searchPanel.classList.remove("d-none");
    searchInput.focus();
    searchInput.select();
  }

  function closeGlobalSearch() {
    if (!searchPanel) return;
    searchPanel.classList.add("d-none");
    activeSearchIndex = -1;
    searchItems = [];
  }

  function escapeHtml(value) {
    return String(value ?? "")
      .replaceAll("&", "&amp;")
      .replaceAll("<", "&lt;")
      .replaceAll(">", "&gt;")
      .replaceAll('"', "&quot;")
      .replaceAll("'", "&#039;");
  }

  function renderSearchResults(data, query) {
    if (!searchResults) return;

    const feesOnly = (data.others || []).filter((i) => i.category === "FEES");
    const noticesOnly = (data.others || []).filter((i) => i.category === "NOTICES");

    const groups = [
      ["STUDENTS", data.students],
      ["TEACHERS", data.teachers],
      ["CLASSES", data.classes],
      ["SECTIONS", data.sections],
      ["FEES", feesOnly],
      ["NOTICES", noticesOnly]
    ];

    const total = groups.reduce((sum, [, items]) => sum + (items?.length || 0), 0);
    if (!total) {
      searchResults.innerHTML = `
        <div class="global-search-empty">
          <i class="bi bi-search fs-3 d-block mb-2"></i>
          <strong>No results found</strong>
          <div class="small mt-1">Try name, mobile, admission no., roll no., PEN, APAAR, TEN or receipt no.</div>
        </div>`;
      if (searchFooter) searchFooter.classList.add("d-none");
      searchItems = [];
      activeSearchIndex = -1;
      return;
    }

    let html = "";
    searchItems = [];
    let itemIndex = 0;

    groups.forEach(([category, items]) => {
      if (!items?.length) return;
      html += `
        <div class="global-search-group-title">
          <span>${category}</span>
          <span class="global-search-group-count">${items.length}</span>
        </div>`;
      items.forEach((item) => {
        const index = itemIndex++;
        searchItems.push(item.url);
        const iconClass = `bi bi-${escapeHtml(item.icon || "search")}`;
        const avatar = item.photoUrl
          ? `<img src="${escapeHtml(item.photoUrl)}" alt="" class="global-search-item-photo" data-fallback-icon="${iconClass}">`
          : `<i class="${iconClass}"></i>`;
        html += `
          <a href="${escapeHtml(item.url)}" class="global-search-item" data-search-index="${index}">
            <span class="global-search-item-icon">${avatar}</span>
            <span class="flex-grow-1 min-w-0">
              <span class="global-search-item-title d-block text-truncate">${escapeHtml(item.title)}</span>
              <span class="global-search-item-subtitle d-block">${escapeHtml(item.subtitle)}</span>
            </span>
            <i class="bi bi-chevron-right text-secondary"></i>
          </a>`;
      });
    });

    searchResults.innerHTML = html;

    searchResults.querySelectorAll(".global-search-item-photo").forEach((img) => {
      img.addEventListener("error", () => {
        const icon = document.createElement("i");
        icon.className = img.dataset.fallbackIcon || "bi bi-search";
        img.replaceWith(icon);
      }, { once: true });
    });

    if (searchFooter) searchFooter.classList.remove("d-none");
    activeSearchIndex = -1;
    if (query) localStorage.setItem("sms-last-search", query);
  }

  async function performGlobalSearch(query) {
    if (!searchResults) return;
    if (!query || query.trim().length < 2) {
      searchResults.innerHTML = `
        <div class="global-search-empty">
          <i class="bi bi-search fs-3 d-block mb-2"></i>
          <strong>Search anything</strong>
          <div class="small mt-1">Name, mobile, admission no., roll no., PEN, APAAR, TEN, receipt no.</div>
        </div>`;
      if (searchFooter) searchFooter.classList.add("d-none");
      searchItems = [];
      return;
    }

    searchResults.innerHTML = `
      <div class="global-search-empty">
        <div class="spinner-border spinner-border-sm text-primary mb-2" role="status"></div>
        <div>Searching...</div>
      </div>`;

    try {
      const response = await fetch(`/search/global?q=${encodeURIComponent(query)}`, {
        headers: { "Accept": "application/json" }
      });
      if (!response.ok) throw new Error("Search request failed");
      const data = await response.json();
      renderSearchResults(data, query);
    } catch (error) {
      searchResults.innerHTML = `
        <div class="global-search-empty text-danger">
          <i class="bi bi-exclamation-circle fs-3 d-block mb-2"></i>
          <strong>Search unavailable</strong>
          <div class="small mt-1">Please try again.</div>
        </div>`;
      if (searchFooter) searchFooter.classList.add("d-none");
    }
  }

  if (searchTrigger) searchTrigger.addEventListener("click", openGlobalSearch);
  if (searchClose) searchClose.addEventListener("click", closeGlobalSearch);

  if (searchInput) {
    searchInput.addEventListener("input", () => {
      clearTimeout(searchTimer);
      searchTimer = setTimeout(() => performGlobalSearch(searchInput.value), 300);
    });

    searchInput.addEventListener("keydown", (event) => {
      if (event.key === "Escape") {
        event.preventDefault();
        closeGlobalSearch();
        return;
      }

      if (event.key === "ArrowDown" || event.key === "ArrowUp") {
        event.preventDefault();
        const items = [...document.querySelectorAll(".global-search-item")];
        if (!items.length) return;
        activeSearchIndex = event.key === "ArrowDown"
          ? (activeSearchIndex + 1) % items.length
          : (activeSearchIndex - 1 + items.length) % items.length;
        items.forEach((item, index) => item.classList.toggle("is-active", index === activeSearchIndex));
        items[activeSearchIndex].scrollIntoView({ block: "nearest" });
        return;
      }

      if (event.key === "Enter" && activeSearchIndex >= 0 && searchItems[activeSearchIndex]) {
        event.preventDefault();
        window.location.href = searchItems[activeSearchIndex];
      }
    });
  }

  document.addEventListener("keydown", (event) => {
    if ((event.ctrlKey || event.metaKey) && event.key.toLowerCase() === "k") {
      event.preventDefault();
      openGlobalSearch();
    }
  });

  document.addEventListener("click", (event) => {
    if (!searchPanel || searchPanel.classList.contains("d-none")) return;
    const wrap = document.getElementById("globalSearchWrap");
    if (wrap && !wrap.contains(event.target)) closeGlobalSearch();
  });

  // ==========================================
  // Delete Confirmation
  // ==========================================

  document.querySelectorAll(".btn-delete").forEach((button) => {
    button.addEventListener("click", function (e) {
      if (!confirm("Are you sure you want to delete this record?")) {
        e.preventDefault();
      }
    });
  });

  // ==========================================
  // Common Functions
  // ==========================================

  function show(element) {
    element.classList.remove("d-none");
  }

  function hide(element) {
    element.classList.add("d-none");
  }

  function enable(element) {
    element.disabled = false;
  }

  function disable(element) {
    element.disabled = true;

    // ==========================================
    // Flatpickr Date Picker
    // ==========================================

    if (window.flatpickr) {
      flatpickr(".date-picker", {
        dateFormat: "d-m-Y",

        altInput: false,

        allowInput: true,

        disableMobile: true,
      });
    }
  }
  // ==========================================
  // Module List Search
  // ==========================================
  // Lightweight, reusable client-side search for list pages.
  // It searches visible table data, ignores action buttons, supports
  // spaces/hyphens in identifiers, and provides clear/result feedback.
  function normalizeListSearch(value) {
    return String(value ?? "")
      .toLowerCase()
      .replace(/[\s-]+/g, "")
      .trim();
  }

  function setupModuleListSearch(toolbar) {
    const table = toolbar.parentElement?.querySelector("table[data-module-search-table]");
    const input = toolbar.querySelector(".module-search-input");
    const clear = toolbar.querySelector(".module-search-clear");
    const count = toolbar.querySelector(".module-search-count");

    if (!table || !input) return;

    const tbody = table.querySelector("tbody");
    if (!tbody) return;

    const rows = Array.from(tbody.querySelectorAll("tr")).filter(row => {
      // Thymeleaf empty-state rows are not part of a populated list.
      return !row.hasAttribute("data-search-empty");
    });

    const total = rows.length;

    function applySearch() {
      const query = normalizeListSearch(input.value);
      let visible = 0;

      rows.forEach(row => {
        const cells = Array.from(row.cells || []);
        // Ignore the action column when searching.
        const searchable = cells.length > 1 ? cells.slice(0, -1) : cells;
        const text = normalizeListSearch(
          searchable.map(cell => cell.innerText || cell.textContent || "").join(" ")
        );
        const match = !query || text.includes(query);
        row.classList.toggle("d-none", !match);
        if (match) visible++;
      });

      clear?.classList.toggle("d-none", !query);

      if (count) {
        if (!query) {
          count.textContent = total ? `${total} record${total === 1 ? "" : "s"}` : "";
        } else {
          count.textContent = `${visible} of ${total} record${total === 1 ? "" : "s"}`;
        }
      }

      let empty = tbody.querySelector("[data-search-no-results]");
      if (query && visible === 0 && total > 0) {
        if (!empty) {
          empty = document.createElement("tr");
          empty.setAttribute("data-search-no-results", "true");
          const cell = document.createElement("td");
          cell.colSpan = Math.max(table.tHead?.rows[0]?.cells.length || 1, 1);
          cell.className = "text-center py-4 text-muted";
          cell.innerHTML = '<i class="bi bi-search fs-4 d-block mb-2"></i>No matching records found';
          empty.appendChild(cell);
          tbody.appendChild(empty);
        }
      } else {
        empty?.remove();
      }
    }

    input.addEventListener("input", applySearch);
    toolbar.querySelector(".module-search-focus")?.addEventListener("click", () => input.focus());
    clear?.addEventListener("click", () => {
      input.value = "";
      applySearch();
      input.focus();
    });

    applySearch();
  }

  document.querySelectorAll("[data-module-search]").forEach(setupModuleListSearch);

  // Upgrade the older Teacher and Subject search boxes with the same
  // normalization and clear behavior, without changing their existing UI.
  [
    ["#searchTeacher", "#teacherTable"],
    ["#searchInput", "#subjectTable"]
  ].forEach(([inputSelector, tableSelector]) => {
    const input = document.querySelector(inputSelector);
    const table = document.querySelector(tableSelector);
    if (!input || !table) return;

    input.setAttribute("type", "search");
    input.setAttribute("autocomplete", "off");
    input.setAttribute("spellcheck", "false");

    const parent = input.parentElement;
    if (!parent) return;

    let clear = parent.querySelector(".module-search-clear");
    if (!clear) {
      clear = document.createElement("button");
      clear.type = "button";
      clear.className = "btn btn-outline-secondary module-search-clear";
      clear.title = "Clear search";
      clear.innerHTML = '<i class="bi bi-x-lg"></i>';
      clear.style.display = "none";
      if (parent.classList.contains("input-group")) {
        parent.appendChild(clear);
      } else {
        const group = document.createElement("div");
        group.className = "input-group";
        input.replaceWith(group);
        group.append(input, clear);
      }
    }

    const apply = () => {
      const query = normalizeListSearch(input.value);
      const rows = Array.from(table.querySelectorAll("tr"));
      let visible = 0;
      rows.forEach(row => {
        const text = normalizeListSearch(row.innerText || "");
        const match = !query || text.includes(query);
        row.style.display = match ? "" : "none";
        if (match) visible++;
      });
      clear.style.display = query ? "" : "none";
    };

    input.addEventListener("input", apply);
    clear.addEventListener("click", () => {
      input.value = "";
      apply();
      input.focus();
    });
  });

  // ==========================================
  // Toast Notifications
  // ==========================================

  const TOAST_AUTO_DISMISS_MS = 4000;

  document.querySelectorAll("#smsToastStack .sms-toast").forEach((toast) => {
    const dismiss = () => {
      toast.classList.add("sms-toast-hide");
      toast.addEventListener("animationend", () => toast.remove(), { once: true });
    };

    toast.querySelector(".sms-toast-close")?.addEventListener("click", dismiss);

    const timer = setTimeout(dismiss, TOAST_AUTO_DISMISS_MS);
    toast.addEventListener("mouseenter", () => clearTimeout(timer));
  });

});
