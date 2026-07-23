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
    document.querySelectorAll('[data-bs-toggle="tooltip"]').forEach((element) => {
      new bootstrap.Tooltip(element);
    });
  }

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
  }
});
