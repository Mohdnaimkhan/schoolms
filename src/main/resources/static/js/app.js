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

    setInterval(updateClock,1000);

});