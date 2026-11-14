const patientForm = document.getElementById("patientForm");
const patientMessage = document.getElementById("patientMessage");
const patientTableBody = document.getElementById("patientTableBody");

// Fetch and display all patients
async function fetchPatients() {
    try {
        const response = await fetch("/api/patients");

        if (!response.ok) {
            throw new Error("Failed to fetch patients");
        }

        const patients = await response.json();

        patientTableBody.innerHTML = "";

        patients.forEach(patient => {
            const row = document.createElement("tr");

            const values = [
                patient.id,
                patient.name,
                patient.age,
                patient.gender,
                patient.phone,
                patient.disease
            ];

            values.forEach(value => {
                const cell = document.createElement("td");
                cell.textContent = value;
                row.appendChild(cell);
            });

            patientTableBody.appendChild(row);
        });

    } catch (error) {
        console.error("Error:", error);
        patientMessage.textContent = "Unable to load patients.";
    }
}

// Register a new patient
patientForm.addEventListener("submit", async function (event) {
    event.preventDefault();

    const patient = {
        id: document.getElementById("patientId").value,
        name: document.getElementById("patientName").value,
        age: document.getElementById("age").value,
        gender: document.getElementById("gender").value,
        phone: document.getElementById("phone").value,
        disease: document.getElementById("disease").value
    };

    try {
        const response = await fetch("/api/patients", {
            method: "POST",
            headers: {
                "Content-Type": "application/x-www-form-urlencoded"
            },
            body: new URLSearchParams(patient)
        });

        const message = await response.text();
        patientMessage.textContent = message;

        if (response.ok) {
            patientForm.reset();
            await fetchPatients();
        }

    } catch (error) {
        patientMessage.textContent = "Server connection failed.";
        console.error(error);
    }
});

// Load patients when page opens
fetchPatients();