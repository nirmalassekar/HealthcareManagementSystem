
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.*;

public class PatientApiServer {

    private static final int PORT = 8080;

    private static final Path FRONTEND =
            Paths.get("Frontend").toAbsolutePath().normalize();

    private static final PatientService patientService =
            new PatientService();

    private static final DoctorService doctorService =
            new DoctorService();

    private static final AppointmentService appointmentService =
            new AppointmentService();

    public static void main(String[] args) throws IOException {

        HttpServer server = HttpServer.create(
                new InetSocketAddress("0.0.0.0", PORT), 0);

        server.createContext("/api/patients",
                PatientApiServer::handlePatientRequest);

        server.createContext("/api/doctors",
                PatientApiServer::handleDoctorRequest);

        server.createContext("/api/appointments",
                PatientApiServer::handleAppointmentRequest);

        server.createContext("/",
                PatientApiServer::serveFrontend);

        server.start();

        System.out.println("Healthcare Server Started!");
        System.out.println("Open: http://localhost:8080/");
    }

    // ================= PATIENT API =================

    private static void handlePatientRequest(HttpExchange exchange)
            throws IOException {

        try {
            String method = exchange.getRequestMethod();

            if (method.equalsIgnoreCase("GET")) {

                List<Patient> patients =
                        patientService.getAllPatients();

                StringBuilder json = new StringBuilder("[");

                for (int i = 0; i < patients.size(); i++) {

                    Patient p = patients.get(i);

                    json.append("{")
                        .append("\"id\":").append(p.getPatientId()).append(",")
                        .append("\"name\":\"").append(escapeJson(p.getPatientName())).append("\",")
                        .append("\"age\":").append(p.getAge()).append(",")
                        .append("\"gender\":\"").append(escapeJson(p.getGender())).append("\",")
                        .append("\"phone\":\"").append(escapeJson(p.getPhoneNumber())).append("\",")
                        .append("\"disease\":\"").append(escapeJson(p.getDisease())).append("\"")
                        .append("}");

                    if (i < patients.size() - 1) {
                        json.append(",");
                    }
                }

                json.append("]");
                sendJson(exchange, 200, json.toString());
                return;
            }

            if (!method.equalsIgnoreCase("POST")) {
                sendResponse(exchange, 405, "Method not allowed.");
                return;
            }

            Map<String, String> data = readFormData(exchange);

            int id = Integer.parseInt(data.get("id"));
            String name = data.get("name");
            int age = Integer.parseInt(data.get("age"));
            String gender = data.get("gender");
            String phone = data.get("phone");
            String disease = data.get("disease");

            if (id <= 0 || age <= 0 ||
                    isBlank(name) || isBlank(gender) ||
                    isBlank(phone) || isBlank(disease)) {

                sendResponse(exchange, 400, "Invalid patient details.");
                return;
            }

            Patient patient = new Patient(
                    id, name.trim(), age, gender.trim(),
                    phone.trim(), disease.trim());

            boolean saved = patientService.addPatient(patient);

            if (saved) {
                sendResponse(exchange, 201,
                        "Patient registered successfully!");
            } else {
                sendResponse(exchange, 409,
                        "Patient could not be saved. Check ID or database.");
            }

        } catch (NumberFormatException e) {
            sendResponse(exchange, 400,
                    "Patient ID and age must be valid numbers.");
        } catch (Exception e) {
            e.printStackTrace();
            sendResponse(exchange, 500, "Patient server error.");
        }
    }

    // ================= DOCTOR API =================

    private static void handleDoctorRequest(HttpExchange exchange)
            throws IOException {

        try {
            String method = exchange.getRequestMethod();

            if (method.equalsIgnoreCase("GET")) {

                List<Doctor> doctors =
                        doctorService.getAllDoctors();

                StringBuilder json = new StringBuilder("[");

                for (int i = 0; i < doctors.size(); i++) {

                    Doctor d = doctors.get(i);

                    json.append("{")
                        .append("\"id\":").append(d.getDoctorId()).append(",")
                        .append("\"name\":\"").append(escapeJson(d.getDoctorName())).append("\",")
                        .append("\"specialization\":\"").append(escapeJson(d.getSpecialization())).append("\",")
                        .append("\"experience\":").append(d.getExperience()).append(",")
                        .append("\"phone\":\"").append(escapeJson(d.getPhoneNumber())).append("\"")
                        .append("}");

                    if (i < doctors.size() - 1) {
                        json.append(",");
                    }
                }

                json.append("]");
                sendJson(exchange, 200, json.toString());
                return;
            }

            if (!method.equalsIgnoreCase("POST")) {
                sendResponse(exchange, 405, "Method not allowed.");
                return;
            }

            Map<String, String> data = readFormData(exchange);

            int id = Integer.parseInt(data.get("id"));
            String name = data.get("name");
            String specialization = data.get("specialization");
            int experience = Integer.parseInt(data.get("experience"));
            String phone = data.get("phone");

            if (id <= 0 || experience < 0 ||
                    isBlank(name) || isBlank(specialization) ||
                    isBlank(phone)) {

                sendResponse(exchange, 400, "Invalid doctor details.");
                return;
            }

            Doctor doctor = new Doctor(
                    id, name.trim(), specialization.trim(),
                    experience, phone.trim());

            boolean saved = doctorService.addDoctor(doctor);

            if (saved) {
                sendResponse(exchange, 201,
                        "Doctor registered successfully!");
            } else {
                sendResponse(exchange, 409,
                        "Doctor could not be saved. Check Doctor ID or database.");
            }

        } catch (NumberFormatException e) {
            sendResponse(exchange, 400,
                    "Doctor ID and experience must be valid numbers.");
        } catch (Exception e) {
            e.printStackTrace();
            sendResponse(exchange, 500, "Doctor server error.");
        }
    }

    // ================= APPOINTMENT API =================

    private static void handleAppointmentRequest(HttpExchange exchange)
            throws IOException {

        try {
            String method = exchange.getRequestMethod();

            // GET ALL APPOINTMENTS
            if (method.equalsIgnoreCase("GET")) {

                List<Appointment> appointments =
                        appointmentService.getAllAppointments();

                StringBuilder json = new StringBuilder("[");

                for (int i = 0; i < appointments.size(); i++) {

                    Appointment a = appointments.get(i);

                    json.append("{")
                        .append("\"appointmentId\":").append(a.getAppointmentId()).append(",")
                        .append("\"patientId\":").append(a.getPatientId()).append(",")
                        .append("\"doctorId\":").append(a.getDoctorId()).append(",")
                        .append("\"appointmentDate\":\"").append(a.getAppointmentDate()).append("\",")
                        .append("\"appointmentTime\":\"").append(a.getAppointmentTime()).append("\",")
                        .append("\"reason\":\"").append(escapeJson(a.getReason())).append("\"")
                        .append("}");

                    if (i < appointments.size() - 1) {
                        json.append(",");
                    }
                }

                json.append("]");
                sendJson(exchange, 200, json.toString());
                return;
            }

            // POST, PUT AND DELETE
            if (!method.equalsIgnoreCase("POST") &&
                    !method.equalsIgnoreCase("PUT") &&
                    !method.equalsIgnoreCase("DELETE")) {

                sendResponse(exchange, 405, "Method not allowed.");
                return;
            }

            Map<String, String> data = readFormData(exchange);

            // DELETE APPOINTMENT
            if (method.equalsIgnoreCase("DELETE")) {

                int appointmentId =
                        Integer.parseInt(data.get("appointmentId"));

                if (appointmentId <= 0) {
                    sendResponse(exchange, 400,
                            "Invalid appointment ID.");
                    return;
                }

                boolean deleted =
                        appointmentService.deleteAppointment(appointmentId);

                if (deleted) {
                    sendResponse(exchange, 200,
                            "Appointment deleted successfully!");
                } else {
                    sendResponse(exchange, 404,
                            "Appointment not found or could not be deleted.");
                }

                return;
            }

            // COMMON DATA FOR POST AND PUT
            int appointmentId =
                    Integer.parseInt(data.get("appointmentId"));

            int patientId =
                    Integer.parseInt(data.get("patientId"));

            int doctorId =
                    Integer.parseInt(data.get("doctorId"));

            String dateValue = data.get("appointmentDate");
            String timeValue = data.get("appointmentTime");
            String reason = data.get("reason");

            if (appointmentId <= 0 || patientId <= 0 ||
                    doctorId <= 0 ||
                    isBlank(dateValue) ||
                    isBlank(timeValue) ||
                    isBlank(reason)) {

                sendResponse(exchange, 400,
                        "Invalid appointment details.");
                return;
            }

            // DATE AND TIME CONVERSION
            Date appointmentDate =
                    Date.valueOf(LocalDate.parse(dateValue.trim()));

            Time appointmentTime =
                    Time.valueOf(LocalTime.parse(timeValue.trim()));

            Appointment appointment = new Appointment(
                    appointmentId,
                    patientId,
                    doctorId,
                    appointmentDate,
                    appointmentTime,
                    reason.trim()
            );

            // POST - ADD APPOINTMENT
            if (method.equalsIgnoreCase("POST")) {

                boolean saved =
                        appointmentService.addAppointment(appointment);

                if (saved) {
                    sendResponse(exchange, 201,
                            "Appointment registered successfully!");
                } else {
                    sendResponse(exchange, 409,
                            "Appointment could not be saved. Check IDs or database.");
                }

                return;
            }

            // PUT - UPDATE APPOINTMENT
            if (method.equalsIgnoreCase("PUT")) {

                boolean updated =
                        appointmentService.updateAppointment(appointment);

                if (updated) {
                    sendResponse(exchange, 200,
                            "Appointment updated successfully!");
                } else {
                    sendResponse(exchange, 404,
                            "Appointment not found or could not be updated.");
                }

                return;
            }

        } catch (NumberFormatException e) {
            sendResponse(exchange, 400,
                    "Appointment, Patient and Doctor IDs must be numbers.");
        } catch (DateTimeParseException e) {
            sendResponse(exchange, 400,
                    "Invalid date or time. Please select valid values.");
        } catch (IllegalArgumentException e) {
            sendResponse(exchange, 400,
                    "Invalid appointment date or time.");
        } catch (Exception e) {
            e.printStackTrace();
            sendResponse(exchange, 500,
                    "Appointment server error.");
        }
    }

    // ================= COMMON HELPERS =================

    private static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    // READ FORM DATA
    private static Map<String, String> readFormData(HttpExchange exchange)
            throws IOException {

        String body = new String(
                exchange.getRequestBody().readAllBytes(),
                StandardCharsets.UTF_8);

        Map<String, String> data = new HashMap<>();

        for (String field : body.split("&")) {

            String[] pair = field.split("=", 2);

            if (pair.length == 2) {

                String key = URLDecoder.decode(
                        pair[0], StandardCharsets.UTF_8);

                String value = URLDecoder.decode(
                        pair[1], StandardCharsets.UTF_8);

                data.put(key, value);
            }
        }

        return data;
    }

    // JSON ESCAPE
    private static String escapeJson(String value) {

        if (value == null) {
            return "";
        }

        return value.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r");
    }

    // SEND JSON
    private static void sendJson(HttpExchange exchange,
                                 int status, String json)
            throws IOException {

        byte[] response = json.getBytes(StandardCharsets.UTF_8);

        exchange.getResponseHeaders().set(
                "Content-Type", "application/json; charset=UTF-8");

        exchange.sendResponseHeaders(status, response.length);

        try (OutputStream output = exchange.getResponseBody()) {
            output.write(response);
        }
    }

    // SEND TEXT RESPONSE
    private static void sendResponse(HttpExchange exchange,
                                     int status, String message)
            throws IOException {

        byte[] response = message.getBytes(StandardCharsets.UTF_8);

        exchange.getResponseHeaders().set(
                "Content-Type", "text/plain; charset=UTF-8");

        exchange.sendResponseHeaders(status, response.length);

        try (OutputStream output = exchange.getResponseBody()) {
            output.write(response);
        }
    }

    // ================= SERVE FRONTEND =================

    private static void serveFrontend(HttpExchange exchange)
            throws IOException {

        try {
            String requestPath =
                    exchange.getRequestURI().getPath();

            if (requestPath.equals("/")) {
                requestPath = "/index.html";
            }

            Path file = FRONTEND.resolve(
                    requestPath.substring(1)).normalize();

            if (!file.startsWith(FRONTEND) ||
                    !Files.exists(file) ||
                    Files.isDirectory(file)) {

                sendResponse(exchange, 404, "File not found.");
                return;
            }

            String filename = file.toString();
            String contentType = "application/octet-stream";

            if (filename.endsWith(".html")) {
                contentType = "text/html; charset=UTF-8";
            } else if (filename.endsWith(".css")) {
                contentType = "text/css; charset=UTF-8";
            } else if (filename.endsWith(".js")) {
                contentType = "application/javascript; charset=UTF-8";
            } else if (filename.endsWith(".png")) {
                contentType = "image/png";
            } else if (filename.endsWith(".jpg") ||
                    filename.endsWith(".jpeg")) {
                contentType = "image/jpeg";
            }

            byte[] content = Files.readAllBytes(file);

            exchange.getResponseHeaders().set(
                    "Content-Type", contentType);

            exchange.sendResponseHeaders(200, content.length);

            try (OutputStream output = exchange.getResponseBody()) {
                output.write(content);
            }

        } catch (Exception e) {
            e.printStackTrace();
            sendResponse(exchange, 500,
                    "Unable to load frontend.");
        }
    }
}
