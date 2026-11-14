
import java.sql.*;
import java.util.Scanner;

public class PatientService {

    // 1. Add Patient
    public boolean addPatient(Patient patient) {

        String sql = "INSERT INTO patients VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, patient.getPatientId());
            ps.setString(2, patient.getPatientName());
            ps.setInt(3, patient.getAge());
            ps.setString(4, patient.getGender());
            ps.setString(5, patient.getPhoneNumber());
            ps.setString(6, patient.getDisease());

            ps.executeUpdate();

            System.out.println("Patient added successfully!");
            return true;

        } catch (SQLException e) {

            if ("23000".equals(e.getSQLState())) {
                System.out.println("Patient ID already exists!");
            } else {
                e.printStackTrace();
            }

            return false;
        }
    }

    // 2. Register Patient
    public void registerPatient(Scanner sc) {

        System.out.print("Enter Patient ID: ");
        int id = sc.nextInt();
        sc.nextLine();

        System.out.print("Enter Patient Name: ");
        String name = sc.nextLine();

        System.out.print("Enter Age: ");
        int age = sc.nextInt();
        sc.nextLine();

        System.out.print("Enter Gender: ");
        String gender = sc.nextLine();

        System.out.print("Enter Phone Number: ");
        String phone = sc.nextLine();

        System.out.print("Enter Disease: ");
        String disease = sc.nextLine();

        Patient patient = new Patient(
            id, name, age, gender, phone, disease
        );

        addPatient(patient);
    }

    // 3. View All Patients
    public void viewAllPatients() {

        String sql = "SELECT * FROM patients";

        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {

                System.out.println("Patient ID: " + rs.getInt("patient_id"));
                System.out.println("Name: " + rs.getString("patient_name"));
                System.out.println("Age: " + rs.getInt("age"));
                System.out.println("Gender: " + rs.getString("gender"));
                System.out.println("Phone: " + rs.getString("phone_number"));
                System.out.println("Disease: " + rs.getString("disease"));

                System.out.println("------------------------");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 4. Search Patient
    public void searchPatient(int id) {

        String sql = "SELECT * FROM patients WHERE patient_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    System.out.println("Patient Found!");
                    System.out.println("Patient ID: " + rs.getInt("patient_id"));
                    System.out.println("Name: " + rs.getString("patient_name"));
                    System.out.println("Age: " + rs.getInt("age"));
                    System.out.println("Gender: " + rs.getString("gender"));
                    System.out.println("Phone: " + rs.getString("phone_number"));
                    System.out.println("Disease: " + rs.getString("disease"));

                } else {
                    System.out.println("Patient Not Found!");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 5. Update Patient
    public void updatePatient(int id, Scanner sc) {

        System.out.print("Enter New Name: ");
        String name = sc.nextLine();

        System.out.print("Enter New Age: ");
        int age = sc.nextInt();
        sc.nextLine();

        System.out.print("Enter New Phone Number: ");
        String phone = sc.nextLine();

        String sql = "UPDATE patients SET patient_name=?, age=?, phone_number=? WHERE patient_id=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.setInt(2, age);
            ps.setString(3, phone);
            ps.setInt(4, id);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("Patient Updated Successfully!");
            } else {
                System.out.println("Patient Not Found!");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 6. Delete Patient
    public void deletePatient(int id) {

        String sql = "DELETE FROM patients WHERE patient_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("Patient Deleted Successfully!");
            } else {
                System.out.println("Patient Not Found!");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


public java.util.List<Patient> getAllPatients() {

    java.util.List<Patient> patients = new java.util.ArrayList<>();

    String sql = "SELECT * FROM patients";

    try (java.sql.Connection con = DBConnection.getConnection();
         java.sql.PreparedStatement ps = con.prepareStatement(sql);
         java.sql.ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {

            Patient patient = new Patient(
                rs.getInt("patient_id"),
                rs.getString("patient_name"),
                rs.getInt("age"),
                rs.getString("gender"),
                rs.getString("phone_number"),
                rs.getString("disease")
            );

            patients.add(patient);
        }

    } catch (java.sql.SQLException e) {
        e.printStackTrace();
    }

    return patients;
}

}
