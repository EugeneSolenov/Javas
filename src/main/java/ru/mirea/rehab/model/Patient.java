package ru.mirea.rehab.model;

public class Patient {
    private Long id;
    private String fullName;
    private String phone;
    private String email;
    private String diagnosis;

    public Patient(Long id, String fullName, String phone, String email, String diagnosis) {
        this.id = id; this.fullName = fullName; this.phone = phone; this.email = email; this.diagnosis = diagnosis;
    }
    public Long getId() { return id; }
    public String getFullName() { return fullName; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }
    public String getDiagnosis() { return diagnosis; }
    public void setFullName(String value) { fullName = value; }
    public void setPhone(String value) { phone = value; }
    public void setEmail(String value) { email = value; }
    public void setDiagnosis(String value) { diagnosis = value; }
    @Override public String toString() { return "%d | %s | %s | %s | диагноз: %s".formatted(id, fullName, phone, email, diagnosis); }
}
