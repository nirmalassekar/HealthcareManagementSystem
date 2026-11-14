public class Patient {

    private int patientId;
    private String patientName;
    private int age;
    private String gender;
    private String phoneNumber;
    private String disease;



public Patient(int patientId, String patientName, int age,
               String gender, String phoneNumber, String disease) {

    this.patientId = patientId;
    this.patientName = patientName;
    this.age = age;
    this.gender = gender;
    this.phoneNumber = phoneNumber;
    this.disease = disease;

}

// Getters

public int getPatientId() {
    return patientId;
}

public String getPatientName() {
    return patientName;
}

public int getAge() {
    return age;
}

public String getGender() {
    return gender;
}

public String getPhoneNumber() {
    return phoneNumber;
}

public String getDisease() {
    return disease;
}


// Setters

public void setPatientId(int patientId) {
    this.patientId = patientId;
}

public void setPatientName(String patientName) {
    this.patientName = patientName;
}

public void setAge(int age) {
    this.age = age;
}

public void setGender(String gender) {
    this.gender = gender;
}

public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
}

public void setDisease(String disease) {
    this.disease = disease;
}
}