package ru.mirea.rehab;

import ru.mirea.rehab.exception.BusinessException;
import ru.mirea.rehab.model.*;
import ru.mirea.rehab.repository.*;
import ru.mirea.rehab.service.*;
import ru.mirea.rehab.util.CsvExporter;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final DateTimeFormatter DATE = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    public static void main(String[] args) {
        PatientService patients = new PatientService(new JdbcPatientRepository());
        AppointmentRepository appointmentRepository = new JdbcAppointmentRepository();
        AppointmentService appointments = new AppointmentService(appointmentRepository);
        StatisticsService statistics = new StatisticsService();
        try (Scanner in = new Scanner(System.in)) {
            while (true) {
                System.out.println("\n=== РЕАБИЛИТАЦИОННЫЙ ЦЕНТР ===\n1. Пациенты\n2. Записи на реабилитацию\n3. Поиск и фильтрация\n4. Статистика\n5. Экспорт расписания CSV\n0. Выход");
                String choice = in.nextLine();
                try {
                    switch (choice) {
                        case "1" -> patientMenu(in, patients);
                        case "2" -> appointmentMenu(in, patients, appointments);
                        case "3" -> searchMenu(in, appointments);
                        case "4" -> printStatistics(appointments, statistics);
                        case "5" -> { CsvExporter.exportAppointments(appointments.all(), Path.of("appointments.csv")); System.out.println("Экспортировано в appointments.csv"); }
                        case "0" -> { return; }
                        default -> System.out.println("Неизвестный пункт меню");
                    }
                } catch (RuntimeException e) { System.out.println("Ошибка: " + e.getMessage()); }
            }
        }
    }
    private static void patientMenu(Scanner in, PatientService service) {
        while (true) {
            System.out.println("\n1. Все пациенты\n2. Добавить пациента\n3. Удалить пациента\n0. Назад");
            String c = in.nextLine(); if (c.equals("0")) return;
            try {
                if (c.equals("1")) service.all().forEach(System.out::println);
                else if (c.equals("2")) { System.out.print("ФИО: "); String n=in.nextLine(); System.out.print("Телефон: "); String p=in.nextLine(); System.out.print("Email: "); String e=in.nextLine(); System.out.print("Диагноз: "); String d=in.nextLine(); System.out.println("Создан пациент ID="+service.create(n,p,e,d)); }
                else if (c.equals("3")) { System.out.print("ID: "); service.delete(Long.parseLong(in.nextLine())); System.out.println("Удалено"); }
                else throw new BusinessException("Неизвестный пункт");
            } catch (NumberFormatException e) { System.out.println("ID должен быть числом"); } catch (RuntimeException e) { System.out.println("Ошибка: "+e.getMessage()); }
        }
    }
    private static void appointmentMenu(Scanner in, PatientService patients, AppointmentService service) {
        while (true) {
            System.out.println("\n1. Все записи\n2. Записать пациента\n3. Изменить статус\n4. Удалить запись\n0. Назад"); String c=in.nextLine(); if(c.equals("0"))return;
            try {
                if(c.equals("1")) service.all().forEach(System.out::println);
                else if(c.equals("2")){ patients.all().forEach(System.out::println); System.out.print("ID пациента: "); long pid=Long.parseLong(in.nextLine()); System.out.print("Тип (PHYSICAL/PSYCHOLOGICAL/SPEECH_THERAPY/SOCIAL): "); RehabilitationType type=RehabilitationType.valueOf(in.nextLine().toUpperCase()); System.out.print("Дата (yyyy-MM-dd HH:mm): "); LocalDateTime date=LocalDateTime.parse(in.nextLine(),DATE); System.out.print("Длительность: "); int duration=Integer.parseInt(in.nextLine()); System.out.print("Специалист: "); String therapist=in.nextLine(); System.out.print("Комментарий: "); String notes=in.nextLine(); System.out.println("Создана запись ID="+service.create(pid,type,date,duration,therapist,notes)); }
                else if(c.equals("3")){System.out.print("ID записи: ");long id=Long.parseLong(in.nextLine());System.out.print("Новый статус: ");service.updateStatus(id,AppointmentStatus.valueOf(in.nextLine().toUpperCase()));System.out.println("Статус изменён");}
                else if(c.equals("4")){System.out.print("ID записи: ");service.delete(Long.parseLong(in.nextLine()));System.out.println("Запись удалена");}
                else throw new BusinessException("Неизвестный пункт");
            } catch (NumberFormatException e){System.out.println("Числовое поле введено неверно");} catch(IllegalArgumentException e){System.out.println("Недопустимое значение перечисления или дата");} catch(RuntimeException e){System.out.println("Ошибка: "+e.getMessage());}
        }
    }
    private static void searchMenu(Scanner in, AppointmentService service){System.out.print("Текст пациента/специалиста (пусто — любой): ");String text=in.nextLine();System.out.print("Статус (пусто — любой): ");String status=in.nextLine();AppointmentStatus s=status.isBlank()?null:AppointmentStatus.valueOf(status.toUpperCase());System.out.print("Тип (пусто — любой): ");String type=in.nextLine();RehabilitationType t=type.isBlank()?null:RehabilitationType.valueOf(type.toUpperCase());service.search(text.isBlank()?null:text,null,null,s,t).forEach(System.out::println);}
    private static void printStatistics(AppointmentService service, StatisticsService stats){List<Appointment> all=service.all();System.out.println("Всего записей: "+stats.total(all));System.out.println("Суммарная длительность: "+stats.totalMinutes(all)+" минут");System.out.println("Различных специалистов: "+stats.distinctTherapists(all));stats.byStatus(all).forEach((k,v)->System.out.println(k+": "+v));}
}
