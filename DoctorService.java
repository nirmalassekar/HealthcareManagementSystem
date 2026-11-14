
import java.sql.*;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

public class DoctorService {

    public boolean addDoctor(Doctor doctor) {

        String sql = "INSERT INTO doctors VALUES (?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, doctor.getDoctorId());
            ps.setString(2, doctor.getDoctorName());
            ps.setString(3, doctor.getSpecialization());
            ps.setInt(4, doctor.getExperience());
            ps.setString(5, doctor.getPhoneNumber());

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("Doctor added successfully!");
                return true;
            }

            return false;

        } catch (SQLException e) {
            if ("23000".equals(e.getSQLState())) {
                System.out.println("Doctor ID already exists!");
            } else {
                e.printStackTrace();
            }
            return false;
        }
    }

    public void registerDoctor(Scanner sc) {

        System.out.print("Enter Doctor ID: ");
        int id = sc.nextInt();
        sc.nextLine();

        System.out.print("Enter Doctor Name: ");
        String name = sc.nextLine();

        System.out.print("Enter Specialization: ");
        String specialization = sc.nextLine();

        System.out.print("Enter Experience: ");
        int experience = sc.nextInt();
        sc.nextLine();

        System.out.print("Enter Phone Number: ");
        String phone = sc.nextLine();

        Doctor doctor = new Doctor(id, name, specialization, experience, phone);
        addDoctor(doctor);
    }

    public void viewAllDoctors() {

        String sql = "SELECT * FROM doctors";

        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            boolean found = false;

            while (rs.next()) {
                found = true;

                System.out.println("Doctor ID: " + rs.getInt("doctor_id"));
                System.out.println("Name: " + rs.getString("doctor_name"));
                System.out.println("Specialization: " + rs.getString("specialization"));
                System.out.println("Experience: " + rs.getInt("experience") + " years");
                System.out.println("Phone: " + rs.getString("phone_number"));
                System.out.println("------------------------");
            }

            if (!found) {
                System.out.println("No Doctors Available!");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void searchDoctor(int id) {

        String sql = "SELECT * FROM doctors WHERE doctor_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    System.out.println("Doctor Found!");
                    System.out.println("Doctor ID: " + rs.getInt("doctor_id"));
                    System.out.println("Name: " + rs.getString("doctor_name"));
                    System.out.println("Specialization: " + rs.getString("specialization"));
                    System.out.println("Experience: " + rs.getInt("experience"));
                    System.out.println("Phone: " + rs.getString("phone_number"));
                } else {
                    System.out.println("Doctor Not Found!");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateDoctor(int id, Scanner sc) {

        System.out.print("Enter New Name: ");
        String name = sc.nextLine();

        System.out.print("Enter New Specialization: ");
        String specialization = sc.nextLine();

        System.out.print("Enter New Experience: ");
        int experience = sc.nextInt();
        sc.nextLine();

        System.out.print("Enter New Phone Number: ");
        String phone = sc.nextLine();

        String sql = "UPDATE doctors SET doctor_name=?, specialization=?, experience=?, phone_number=? WHERE doctor_id=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.setString(2, specialization);
            ps.setInt(3, experience);
            ps.setString(4, phone);
            ps.setInt(5, id);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("Doctor Updated Successfully!");
            } else {
                System.out.println("Doctor Not Found!");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteDoctor(int id) {

        String sql = "DELETE FROM doctors WHERE doctor_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("Doctor Deleted Successfully!");
            } else {
                System.out.println("Doctor Not Found!");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Doctor> getAllDoctors() {

        List<Doctor> doctors = new ArrayList<>();
        String sql = "SELECT * FROM doctors";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Doctor doctor = new Doctor(
                    rs.getInt("doctor_id"),
                    rs.getString("doctor_name"),
                    rs.getString("specialization"),
                    rs.getInt("experience"),
                    rs.getString("phone_number")
                );

                doctors.add(doctor);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return doctors;
    }
}
