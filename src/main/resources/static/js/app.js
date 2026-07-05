document.addEventListener("DOMContentLoaded", () => {

    // ============================
    // Sidebar Toggle
    // ============================

    const toggle = document.getElementById("sidebarToggle");
    const sidebar = document.getElementById("sidebar");

    if (toggle) {

        toggle.addEventListener("click", () => {

            sidebar.classList.toggle("active");

        });

    }




    // ============================
    // Live Date & Time
    // ============================

    const dateTime = document.getElementById("currentDateTime");

    function updateClock() {

        const now = new Date();

        const options = {

            weekday: "long",
            day: "2-digit",
            month: "long",
            year: "numeric"

        };

        const date = now.toLocaleDateString("en-IN", options);

        const time = now.toLocaleTimeString("en-IN", {

            hour: "2-digit",
            minute: "2-digit",
            second: "2-digit"

        });

        if (dateTime) {

            dateTime.innerHTML = `
                <div>${date}</div>
                <div>${time}</div>
            `;

        }

    }

    updateClock();

    setInterval(updateClock, 1000);

});



// ============================
// Theme Toggle
// ============================

const themeToggle = document.getElementById("themeToggle");
const themeIcon = document.getElementById("themeIcon");

const savedTheme = localStorage.getItem("theme");

if (savedTheme === "dark") {

    document.body.classList.add("dark");

    if (themeIcon) {
        themeIcon.className = "bi bi-sun-fill";
    }

}

if (themeToggle) {

    themeToggle.addEventListener("click", () => {

        document.body.classList.toggle("dark");

        const dark = document.body.classList.contains("dark");

        if (dark) {

            localStorage.setItem("theme", "dark");

            themeIcon.className = "bi bi-sun-fill";

        } else {

            localStorage.setItem("theme", "light");

            themeIcon.className = "bi bi-moon-stars-fill";

        }

    });

}