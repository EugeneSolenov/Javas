package ru.mirea.rehab.util;

import ru.mirea.rehab.model.Appointment;
import java.io.BufferedWriter;
import java.nio.file.*;
import java.util.List;

public final class CsvExporter {
    private CsvExporter(){}
    public static void exportAppointments(List<Appointment> items, Path file){
        try(BufferedWriter w=Files.newBufferedWriter(file)){w.write("id;patient;type;start;duration;therapist;status\n");for(Appointment a:items)w.write(String.join(";",String.valueOf(a.getId()),a.getPatientName(),a.getType().name(),a.getStartAt().toString(),String.valueOf(a.getDurationMinutes()),a.getTherapist(),a.getStatus().name())+"\n");}
        catch(Exception e){throw new RuntimeException("Ошибка экспорта: "+e.getMessage(),e);}
    }
}
