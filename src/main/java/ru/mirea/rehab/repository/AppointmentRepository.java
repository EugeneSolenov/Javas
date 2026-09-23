package ru.mirea.rehab.repository;

import ru.mirea.rehab.model.*;
import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentRepository {
    List<Appointment> findAll();
    Appointment findById(long id);
    long save(Appointment appointment);
    void update(Appointment appointment);
    void delete(long id);
    List<Appointment> search(String text, LocalDateTime from, LocalDateTime to, AppointmentStatus status, RehabilitationType type);
}
