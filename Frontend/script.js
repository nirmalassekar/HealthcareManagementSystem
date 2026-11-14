
const form = document.getElementById("appointmentForm");

form.addEventListener("submit", function(event) {
    event.preventDefault();

    const name = document.getElementById("name").value;
    const doctor = document.getElementById("doctor").value;
    const date = document.getElementById("date").value;

    document.getElementById("message").textContent =
        `Thank you ${name}! Your appointment request with ${doctor} for ${date} has been received.`;

    form.reset();
});
