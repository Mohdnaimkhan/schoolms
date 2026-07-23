// ==========================================
// TOAST.JS
// Auto Close Alerts
// ==========================================

document.addEventListener("DOMContentLoaded", () => {

    initializeAlerts();

});

// ==========================================
// Auto Hide Alerts
// ==========================================

function initializeAlerts() {

    if (!window.bootstrap) {
        return;
    }

    document.querySelectorAll(".alert").forEach(alert => {

        setTimeout(() => {

            let bsAlert = bootstrap.Alert.getOrCreateInstance(alert);

            bsAlert.close();

        }, 4000);

    });

}
