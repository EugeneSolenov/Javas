package ru.mirea.rehab.service;

import ru.mirea.rehab.exception.BusinessException;
import ru.mirea.rehab.model.*;
import ru.mirea.rehab.repository.AppointmentRepository;
import java.time.LocalDateTime;
import java.util.List;

public class AppointmentService {
    private final AppointmentRepository repository;
    public AppointmentService(AppointmentRepository repository) { this.repository = repository; }
    public List<Appointment> all() { return repository.findAll(); }
    public Appointment byId(long id) { return repository.findById(id); }
    public List<Appointment> search(String text, LocalDateTime from, LocalDateTime to, AppointmentStatus status, RehabilitationType type) {
        return repository.search(text, from, to, status, type);
    }
    public long create(long patientId, RehabilitationType type, LocalDateTime startAt, int duration,
                       String therapist, String notes) {
        validate(type, startAt, duration, therapist);
        return repository.save(new Appointment(null, patientId, "", type, startAt, duration, therapist,
                AppointmentStatus.PLANNED, notes));
    }
    public void delete(long id) { repository.delete(id); }

    public void updateStatus(long id, AppointmentStatus target) {
        Appointment current = repository.findById(id);
        if (current.getStatus() == AppointmentStatus.CANCELLED || current.getStatus() == AppointmentStatus.COMPLETED) {
            throw new BusinessException("Завершённую или отменённую запись нельзя изменить");
        }
        if (target == AppointmentStatus.PLANNED && current.getStatus() != AppointmentStatus.PLANNED) {
            throw new BusinessException("Нельзя вернуть запись в статус PLANNED");
        }
        current.setStatus(target);
        repository.update(current);
    }
    private void validate(RehabilitationType type, LocalDateTime startAt, int duration, String therapist) {
        if (type == null) throw new BusinessException("Вид реабилитации обязателен");
        if (startAt == null || startAt.isBefore(LocalDateTime.now())) throw new BusinessException("Дата записи должна быть будущей");
        if (duration < 15 || duration > 240) throw new BusinessException("Длительность должна быть от 15 до 240 минут");
        if (therapist == null || therapist.isBlank()) throw new BusinessException("Специалист обязателен");
    }
}
