
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class AppointmentService {

    // 1. Add Appointment
    public boolean addAppointment(Appointment appointment) {

        String sql = "INSERT INTO appointments "
                + "(appointment_id, patient_id, doctor_id, "
                + "appointment_date, appointment_time, reason) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, appointment.getAppointmentId());
            ps.setInt(2, appointment.getPatientId());
            ps.setInt(3, appointment.getDoctorId());
            ps.setDate(4, appointment.getAppointmentDate());
            ps.setTime(5, appointment.getAppointmentTime());
            ps.setString(6, appointment.getReason());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 2. Get All Appointments
    public List<Appointment> getAllAppointments() {

        List<Appointment> appointments = new ArrayList<>();

        String sql = "SELECT * FROM appointments";

        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {

                Appointment appointment = new Appointment(
                    rs.getInt("appointment_id"),
                    rs.getInt("patient_id"),
                    rs.getInt("doctor_id"),
                    rs.getDate("appointment_date"),
                    rs.getTime("appointment_time"),
                    rs.getString("reason")
                );

                appointments.add(appointment);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return appointments;
    }

    // 3. View Appointments
    public void viewAppointments() {

        List<Appointment> appointments = getAllAppointments();

        for (Appointment a : appointments) {
            System.out.println("-------------------------");
            System.out.println("Appointment ID: " + a.getAppointmentId());
            System.out.println("Patient ID: " + a.getPatientId());
            System.out.println("Doctor ID: " + a.getDoctorId());
            System.out.println("Date: " + a.getAppointmentDate());
            System.out.println("Time: " + a.getAppointmentTime());
            System.out.println("Reason: " + a.getReason());
        }
    }

    // 4. Search Appointment
    public void searchAppointment(int appointmentId) {

        String sql = "SELECT * FROM appointments WHERE appointment_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, appointmentId);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    System.out.println("Appointment ID: " + rs.getInt("appointment_id"));
                    System.out.println("Patient ID: " + rs.getInt("patient_id"));
                    System.out.println("Doctor ID: " + rs.getInt("doctor_id"));
                    System.out.println("Date: " + rs.getDate("appointment_date"));
                    System.out.println("Time: " + rs.getTime("appointment_time"));
                    System.out.println("Reason: " + rs.getString("reason"));
                } else {
                    System.out.println("Appointment Not Found!");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 5. Update Appointment
    public boolean updateAppointment(Appointment appointment) {

        String sql = "UPDATE appointments SET patient_id=?, doctor_id=?, "
                + "appointment_date=?, appointment_time=?, reason=? "
                + "WHERE appointment_id=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, appointment.getPatientId());
            ps.setInt(2, appointment.getDoctorId());
            ps.setDate(3, appointment.getAppointmentDate());
            ps.setTime(4, appointment.getAppointmentTime());
            ps.setString(5, appointment.getReason());
            ps.setInt(6, appointment.getAppointmentId());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 6. Delete Appointment
    public boolean deleteAppointment(int appointmentId) {

        String sql = "DELETE FROM appointments WHERE appointment_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, appointmentId);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
