package ru.mirea.rehab.model;

import java.time.LocalDateTime;

public class Appointment {
    private Long id;
    private Long patientId;
    private String patientName;
    private RehabilitationType type;
    private LocalDateTime startAt;
    private int durationMinutes;
    private String therapist;
    private AppointmentStatus status;
    private String notes;

    public Appointment(Long id, Long patientId, String patientName, RehabilitationType type, LocalDateTime startAt, int durationMinutes, String therapist, AppointmentStatus status, String notes) {
        this.id=id; this.patientId=patientId; this.patientName=patientName; this.type=type; this.startAt=startAt; this.durationMinutes=durationMinutes; this.therapist=therapist; this.status=status; this.notes=notes;
    }
    public Long getId(){return id;} public Long getPatientId(){return patientId;} public String getPatientName(){return patientName;}
    public RehabilitationType getType(){return type;} public LocalDateTime getStartAt(){return startAt;} public int getDurationMinutes(){return durationMinutes;}
    public String getTherapist(){return therapist;} public AppointmentStatus getStatus(){return status;} public String getNotes(){return notes;}
    public void setType(RehabilitationType v){type=v;} public void setStartAt(LocalDateTime v){startAt=v;} public void setDurationMinutes(int v){durationMinutes=v;}
    public void setTherapist(String v){therapist=v;} public void setStatus(AppointmentStatus v){status=v;} public void setNotes(String v){notes=v;}
    @Override public String toString(){return "%d | пациент: %s | %s | %s | %d мин | специалист: %s | %s".formatted(id,patientName,type,startAt,durationMinutes,therapist,status);}
}
