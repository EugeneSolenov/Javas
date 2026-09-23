package ru.mirea.rehab.service;

import ru.mirea.rehab.model.Appointment;
import ru.mirea.rehab.model.AppointmentStatus;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class StatisticsService {
    public Map<AppointmentStatus, Long> byStatus(List<Appointment> appointments) {
        Map<AppointmentStatus, Long> result = new EnumMap<>(AppointmentStatus.class);
        for (AppointmentStatus status : AppointmentStatus.values()) result.put(status, 0L);
        appointments.forEach(a -> result.merge(a.getStatus(), 1L, Long::sum));
        return result;
    }
    public long total(List<Appointment> appointments) { return appointments.size(); }
    public long totalMinutes(List<Appointment> appointments) { return appointments.stream().mapToLong(Appointment::getDurationMinutes).sum(); }
    public long distinctTherapists(List<Appointment> appointments) { return appointments.stream().map(Appointment::getTherapist).distinct().count(); }
}
