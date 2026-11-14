
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        PatientService patientService = new PatientService();
        DoctorService doctorService = new DoctorService();
        AppointmentService appointmentService = new AppointmentService();

        while (true) {

            System.out.println("\n===== HEALTHCARE MANAGEMENT SYSTEM =====");
            System.out.println("1. Patient Management");
            System.out.println("2. Doctor Management");
            System.out.println("3. Appointment Management");
            System.out.println("4. Exit");

            System.out.print("Enter your choice: ");
            int choice = sc.nextInt();

            switch (choice) {

                case 1:
                    patientMenu(sc, patientService);
                    break;

                case 2:
                    doctorMenu(sc, doctorService);
                    break;

                case 3:
                    appointmentMenu(sc, appointmentService);
                    break;

                case 4:
                    System.out.println("Thank you for using our system!");
                    sc.close();
                    return;

                default:
                    System.out.println("Invalid Choice!");
            }
        }
    }

    public static void patientMenu(Scanner sc, PatientService service) {

        while (true) {

            System.out.println("\n===== PATIENT MANAGEMENT =====");
            System.out.println("1. Register Patient");
            System.out.println("2. View All Patients");
            System.out.println("3. Search Patient");
            System.out.println("4. Update Patient");
            System.out.println("5. Delete Patient");
            System.out.println("6. Back");

            System.out.print("Enter your choice: ");
            int choice = sc.nextInt();

            switch (choice) {

                case 1:
                    service.registerPatient(sc);
                    break;

                case 2:
                    service.viewAllPatients();
                    break;

                case 3:
                    System.out.print("Enter Patient ID: ");
                    service.searchPatient(sc.nextInt());
                    break;

                case 4:
                    System.out.print("Enter Patient ID: ");
                    int updateId = sc.nextInt();
                    sc.nextLine();
                    service.updatePatient(updateId, sc);
                    break;

                case 5:
                    System.out.print("Enter Patient ID: ");
                    service.deletePatient(sc.nextInt());
                    break;

                case 6:
                    return;

                default:
                    System.out.println("Invalid Choice!");
            }
        }
    }

    public static void doctorMenu(Scanner sc, DoctorService service) {

        while (true) {

            System.out.println("\n===== DOCTOR MANAGEMENT =====");
            System.out.println("1. Register Doctor");
            System.out.println("2. View All Doctors");
            System.out.println("3. Search Doctor");
            System.out.println("4. Update Doctor");
            System.out.println("5. Delete Doctor");
            System.out.println("6. Back");

            System.out.print("Enter your choice: ");
            int choice = sc.nextInt();

            switch (choice) {

                case 1:
                    service.registerDoctor(sc);
                    break;

                case 2:
                    service.viewAllDoctors();
                    break;

                case 3:
                    System.out.print("Enter Doctor ID: ");
                    service.searchDoctor(sc.nextInt());
                    break;

                case 4:
                    System.out.print("Enter Doctor ID: ");
                    int updateId = sc.nextInt();
                    sc.nextLine();
                    service.updateDoctor(updateId, sc);
                    break;

                case 5:
                    System.out.print("Enter Doctor ID: ");
                    service.deleteDoctor(sc.nextInt());
                    break;

                case 6:
                    return;

                default:
                    System.out.println("Invalid Choice!");
            }
        }
    }


public static void appointmentMenu(Scanner sc, AppointmentService service) {

    while (true) {

        System.out.println("\n===== APPOINTMENT MANAGEMENT =====");
        System.out.println("1. Add Appointment");
        System.out.println("2. View All Appointments");
        System.out.println("3. Search Appointment");
        System.out.println("4. Update Appointment");
        System.out.println("5. Delete Appointment");
        System.out.println("6. Back");

        System.out.print("Enter your choice: ");
        int choice = sc.nextInt();

        switch (choice) {

            case 1:
                System.out.print("Appointment ID: ");
                int id = sc.nextInt();

                System.out.print("Patient ID: ");
                int patientId = sc.nextInt();

                System.out.print("Doctor ID: ");
                int doctorId = sc.nextInt();

                sc.nextLine();

                System.out.print("Appointment Date (YYYY-MM-DD): ");
                java.sql.Date date = java.sql.Date.valueOf(sc.nextLine());

                System.out.print("Appointment Time (HH:MM:SS): ");
                java.sql.Time time = java.sql.Time.valueOf(sc.nextLine());

                System.out.print("Reason: ");
                String reason = sc.nextLine();

                Appointment appointment = new Appointment(
                    id, patientId, doctorId, date, time, reason
                );

                service.addAppointment(appointment);
                break;

            case 2:
                service.viewAppointments();
                break;

            case 3:
                System.out.print("Enter Appointment ID: ");
                service.searchAppointment(sc.nextInt());
                break;

            case 4:
                System.out.print("Appointment ID: ");
                int updateId = sc.nextInt();

                System.out.print("Patient ID: ");
                int newPatientId = sc.nextInt();

                System.out.print("Doctor ID: ");
                int newDoctorId = sc.nextInt();

                sc.nextLine();

                System.out.print("Appointment Date (YYYY-MM-DD): ");
                java.sql.Date newDate = java.sql.Date.valueOf(sc.nextLine());

                System.out.print("Appointment Time (HH:MM:SS): ");
                java.sql.Time newTime = java.sql.Time.valueOf(sc.nextLine());

                System.out.print("Reason: ");
                String newReason = sc.nextLine();

                Appointment updatedAppointment = new Appointment(
                    updateId, newPatientId, newDoctorId,
                    newDate, newTime, newReason
                );

                service.updateAppointment(updatedAppointment);
                break;

            case 5:
                System.out.print("Enter Appointment ID: ");
                service.deleteAppointment(sc.nextInt());
                break;

            case 6:
                return;

            default:
                System.out.println("Invalid Choice!");
        }
    }
}

}
