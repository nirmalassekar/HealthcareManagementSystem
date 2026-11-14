
import java.sql.Date;
import java.sql.Time;

public class Appointment {

    private int appointmentId;
    private int patientId;
    private int doctorId;
    private Date appointmentDate;
    private Time appointmentTime;
    private String reason;

    public Appointment(int appointmentId, int patientId,
                       int doctorId, Date appointmentDate,
                       Time appointmentTime, String reason) {

        this.appointmentId = appointmentId;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
        this.reason = reason;
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public int getPatientId() {
        return patientId;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public Date getAppointmentDate() {
        return appointmentDate;
    }

    public Time getAppointmentTime() {
        return appointmentTime;
    }

    public String getReason() {
        return reason;
    }
}
