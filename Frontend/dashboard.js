
async function loadDashboard() {
    try {
        // Fetch data from Java backend APIs
        const [patientResponse, doctorResponse, appointmentResponse] =
            await Promise.all([
                fetch("/api/patients"),
                fetch("/api/doctors"),
                fetch("/api/appointments")
            ]);

        if (!patientResponse.ok ||
            !doctorResponse.ok ||
            !appointmentResponse.ok) {
            throw new Error("Unable to fetch dashboard data");
        }

        const patients = await patientResponse.json();
        const doctors = await doctorResponse.json();
        const appointments = await appointmentResponse.json();

        // Update dashboard counts
        document.getElementById("patientCount").textContent =
            patients.length;

        document.getElementById("doctorCount").textContent =
            doctors.length;

        document.getElementById("appointmentCount").textContent =
            appointments.length;

        // Display tables
        displayTable(patients, "patientTable");
        displayTable(doctors, "doctorTable");
        displayTable(appointments, "appointmentTable");

    } catch (error) {
        console.error("Dashboard Error:", error);
    }
}

function displayTable(data, elementId) {
    const container = document.getElementById(elementId);

    if (!container) return;

    if (data.length === 0) {
        container.textContent = "No records found.";
        return;
    }

    const table = document.createElement("table");
    const headers = Object.keys(data[0]);

    const headerRow = table.insertRow();

    headers.forEach(header => {
        const th = document.createElement("th");
        th.textContent = header;
        headerRow.appendChild(th);
    });

    data.forEach(item => {
        const row = table.insertRow();

        headers.forEach(header => {
            const cell = row.insertCell();
            cell.textContent = item[header] ?? "";
        });
    });

    container.replaceChildren(table);
}

// Load dashboard when page opens
document.addEventListener("DOMContentLoaded", loadDashboard);
