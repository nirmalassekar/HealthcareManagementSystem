
const doctorForm = document.getElementById("doctorForm");
const doctorMessage = document.getElementById("doctorMessage");
const doctorTableBody = document.getElementById("doctorTableBody");

async function fetchDoctors() {
    try {
        const response = await fetch("/api/doctors");

        if (!response.ok) {
            throw new Error("Failed to fetch doctors");
        }

        const doctors = await response.json();

        doctorTableBody.innerHTML = "";

        doctors.forEach(doctor => {
            const row = document.createElement("tr");

            const values = [
                doctor.id,
                doctor.name,
                doctor.specialization,
                doctor.experience + " years",
                doctor.phone
            ];

            values.forEach(value => {
                const cell = document.createElement("td");
                cell.textContent = value ?? "";
                row.appendChild(cell);
            });

            doctorTableBody.appendChild(row);
        });

    } catch (error) {
        console.error(error);
        doctorMessage.textContent = "Unable to load doctors.";
    }
}

doctorForm.addEventListener("submit", async function(event) {
    event.preventDefault();

    doctorMessage.textContent = "Registering doctor...";

    const doctor = {
        id: document.getElementById("doctorId").value.trim(),
        name: document.getElementById("doctorName").value.trim(),
        specialization: document.getElementById("specialization").value.trim(),
        experience: document.getElementById("experience").value.trim(),
        phone: document.getElementById("phone").value.trim()
    };

    if (Object.values(doctor).some(value => value === "")) {
        doctorMessage.textContent = "Please fill all fields.";
        return;
    }

    try {
        const response = await fetch("/api/doctors", {
            method: "POST",
            headers: {
                "Content-Type": "application/x-www-form-urlencoded"
            },
            body: new URLSearchParams(doctor)
        });

        const message = await response.text();
        doctorMessage.textContent = message;

        if (response.ok) {
            doctorForm.reset();
            await fetchDoctors();
        }

    } catch (error) {
        console.error(error);
        doctorMessage.textContent = "Server connection failed.";
    }
});

fetchDoctors();
