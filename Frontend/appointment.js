
const bookingForm = document.getElementById("bookingForm");
const bookingMessage = document.getElementById("bookingMessage");
const appointmentTableBody =
    document.getElementById("appointmentTableBody");

const bookingButton = document.getElementById("bookingButton");
const cancelEditButton = document.getElementById("cancelEditButton");

let isEditMode = false;

// 1. FETCH APPOINTMENTS
async function fetchAppointments() {
    try {
        const response = await fetch("/api/appointments");

        if (!response.ok) {
            throw new Error("Unable to fetch appointments");
        }

        const appointments = await response.json();

        appointmentTableBody.innerHTML = "";

        appointments.forEach(appointment => {
            const row = document.createElement("tr");

            const values = [
                appointment.appointmentId,
                appointment.patientId,
                appointment.doctorId,
                appointment.appointmentDate,
                appointment.appointmentTime,
                appointment.reason
            ];

            values.forEach(value => {
                const cell = document.createElement("td");
                cell.textContent = value;
                row.appendChild(cell);
            });

            // EDIT BUTTON
            const actionCell = document.createElement("td");

            const editButton = document.createElement("button");
            editButton.textContent = "Edit";
            editButton.type = "button";

            editButton.addEventListener("click", () => {
                editAppointment(appointment);
            });

            // DELETE BUTTON
            const deleteButton = document.createElement("button");
            deleteButton.textContent = "Delete";
            deleteButton.type = "button";

            deleteButton.addEventListener("click", () => {
                deleteAppointment(appointment.appointmentId);
            });

            actionCell.appendChild(editButton);
            actionCell.appendChild(deleteButton);
            row.appendChild(actionCell);

            appointmentTableBody.appendChild(row);
        });

    } catch (error) {
        console.error(error);
        bookingMessage.textContent =
            "Unable to load appointments.";
    }
}

// 2. BOOK OR UPDATE APPOINTMENT
bookingForm.addEventListener("submit", async function(event) {

    event.preventDefault();

    const appointment = {
        appointmentId:
            document.getElementById("appointmentId").value,

        patientId:
            document.getElementById("patientId").value,

        doctorId:
            document.getElementById("doctorId").value,

        appointmentDate:
            document.getElementById("date").value,

        appointmentTime:
            document.getElementById("time").value,

        reason:
            document.getElementById("reason").value.trim()
    };

    const method = isEditMode ? "PUT" : "POST";

    bookingMessage.textContent = isEditMode
        ? "Updating appointment..."
        : "Booking appointment...";

    try {
        const response = await fetch("/api/appointments", {
            method: method,
            headers: {
                "Content-Type": "application/x-www-form-urlencoded"
            },
            body: new URLSearchParams(appointment)
        });

        const message = await response.text();
        bookingMessage.textContent = message;

        if (response.ok) {
            bookingForm.reset();
            resetEditMode();
            await fetchAppointments();
        }

    } catch (error) {
        console.error(error);
        bookingMessage.textContent =
            "Server connection failed.";
    }
});

// 3. EDIT APPOINTMENT
function editAppointment(appointment) {

    document.getElementById("appointmentId").value =
        appointment.appointmentId;

    document.getElementById("patientId").value =
        appointment.patientId;

    document.getElementById("doctorId").value =
        appointment.doctorId;

    document.getElementById("date").value =
        appointment.appointmentDate;

    document.getElementById("time").value =
        appointment.appointmentTime.substring(0, 5);

    document.getElementById("reason").value =
        appointment.reason;

    isEditMode = true;

    bookingButton.textContent = "Update Appointment";
    cancelEditButton.style.display = "inline-block";

    bookingMessage.textContent =
        "Edit the appointment details and click Update.";
    
    bookingForm.scrollIntoView({ behavior: "smooth" });
}

// 4. DELETE APPOINTMENT
async function deleteAppointment(appointmentId) {

    const confirmDelete = confirm(
        "Are you sure you want to delete this appointment?"
    );

    if (!confirmDelete) {
        return;
    }

    bookingMessage.textContent = "Deleting appointment...";

    try {
        const response = await fetch("/api/appointments", {
            method: "DELETE",
            headers: {
                "Content-Type": "application/x-www-form-urlencoded"
            },
            body: new URLSearchParams({
                appointmentId: appointmentId
            })
        });

        const message = await response.text();
        bookingMessage.textContent = message;

        if (response.ok) {
            await fetchAppointments();
        }

    } catch (error) {
        console.error(error);
        bookingMessage.textContent =
            "Unable to delete appointment.";
    }
}

// 5. CANCEL UPDATE
function resetEditMode() {

    isEditMode = false;

    bookingButton.textContent = "Book Appointment";
    cancelEditButton.style.display = "none";
}

cancelEditButton.addEventListener("click", function() {

    bookingForm.reset();
    resetEditMode();

    bookingMessage.textContent = "";
});

// 6. LOAD APPOINTMENTS
fetchAppointments();
