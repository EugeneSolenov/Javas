package ru.mirea.rehab.repository;

import ru.mirea.rehab.exception.EntityNotFoundException;
import ru.mirea.rehab.model.*;
import ru.mirea.rehab.util.DatabaseManager;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class JdbcAppointmentRepository implements AppointmentRepository {
    private Appointment map(ResultSet r) throws SQLException { return new Appointment(r.getLong("id"), r.getLong("patient_id"), r.getString("patient_name"), RehabilitationType.valueOf(r.getString("rehabilitation_type")), r.getTimestamp("start_at").toLocalDateTime(), r.getInt("duration_minutes"), r.getString("therapist"), AppointmentStatus.valueOf(r.getString("status")), r.getString("notes")); }
    private static final String SELECT = "SELECT a.id,a.patient_id,p.full_name patient_name,a.rehabilitation_type,a.start_at,a.duration_minutes,a.therapist,a.status,a.notes FROM appointments a JOIN patients p ON p.id=a.patient_id";
    public List<Appointment> findAll(){return search(null,null,null,null,null);}
    public List<Appointment> search(String text, LocalDateTime from, LocalDateTime to, AppointmentStatus status, RehabilitationType type){
        String sql=SELECT+" WHERE (?::text IS NULL OR lower(p.full_name) LIKE lower(?) OR lower(a.therapist) LIKE lower(?)) AND (?::timestamp IS NULL OR a.start_at>=?) AND (?::timestamp IS NULL OR a.start_at<=?) AND (?::text IS NULL OR a.status=?) AND (?::text IS NULL OR a.rehabilitation_type=?) ORDER BY a.start_at";
        List<Appointment> result=new ArrayList<>();
        try(Connection c=DatabaseManager.open();PreparedStatement s=c.prepareStatement(sql)){int i=1;String q=text==null?null:"%"+text+"%";s.setString(i++,q);s.setString(i++,q);s.setString(i++,q);s.setObject(i++,from);s.setObject(i++,from);s.setObject(i++,to);s.setObject(i++,to);s.setString(i++,status==null?null:status.name());s.setString(i++,status==null?null:status.name());s.setString(i++,type==null?null:type.name());s.setString(i++,type==null?null:type.name());try(ResultSet r=s.executeQuery()){while(r.next())result.add(map(r));}return result;}catch(SQLException e){throw new RuntimeException("Ошибка чтения записей: "+e.getMessage(),e);}
    }
    public Appointment findById(long id){for(Appointment a:findAll())if(a.getId()==id)return a;throw new EntityNotFoundException("Запись с ID "+id+" не найдена");}
    public long save(Appointment a){String sql="INSERT INTO appointments(patient_id,rehabilitation_type,start_at,duration_minutes,therapist,status,notes) VALUES(?,?,?,?,?,?,?) RETURNING id";try(Connection c=DatabaseManager.open();PreparedStatement s=c.prepareStatement(sql)){s.setLong(1,a.getPatientId());s.setString(2,a.getType().name());s.setTimestamp(3,Timestamp.valueOf(a.getStartAt()));s.setInt(4,a.getDurationMinutes());s.setString(5,a.getTherapist());s.setString(6,a.getStatus().name());s.setString(7,a.getNotes());try(ResultSet r=s.executeQuery()){r.next();return r.getLong(1);}}catch(SQLException e){throw new RuntimeException("Ошибка сохранения записи: "+e.getMessage(),e);}}
    public void update(Appointment a){try(Connection c=DatabaseManager.open();PreparedStatement s=c.prepareStatement("UPDATE appointments SET rehabilitation_type=?,start_at=?,duration_minutes=?,therapist=?,status=?,notes=? WHERE id=?")){s.setString(1,a.getType().name());s.setTimestamp(2,Timestamp.valueOf(a.getStartAt()));s.setInt(3,a.getDurationMinutes());s.setString(4,a.getTherapist());s.setString(5,a.getStatus().name());s.setString(6,a.getNotes());s.setLong(7,a.getId());if(s.executeUpdate()==0)throw new EntityNotFoundException("Запись не найдена");}catch(SQLException e){throw new RuntimeException("Ошибка обновления записи: "+e.getMessage(),e);}}
    public void delete(long id){try(Connection c=DatabaseManager.open();PreparedStatement s=c.prepareStatement("DELETE FROM appointments WHERE id=?")){s.setLong(1,id);if(s.executeUpdate()==0)throw new EntityNotFoundException("Запись не найдена");}catch(SQLException e){throw new RuntimeException("Ошибка удаления записи: "+e.getMessage(),e);}}
}
