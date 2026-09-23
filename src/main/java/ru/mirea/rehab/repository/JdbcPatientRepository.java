package ru.mirea.rehab.repository;

import ru.mirea.rehab.exception.EntityNotFoundException;
import ru.mirea.rehab.model.Patient;
import ru.mirea.rehab.util.DatabaseManager;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcPatientRepository implements PatientRepository {
    private Patient map(ResultSet rs) throws SQLException {
        return new Patient(rs.getLong("id"), rs.getString("full_name"), rs.getString("phone"), rs.getString("email"), rs.getString("diagnosis"));
    }
    public List<Patient> findAll() {
        String sql = "SELECT id, full_name, phone, email, diagnosis FROM patients ORDER BY id";
        List<Patient> result = new ArrayList<>();
        try (Connection c=DatabaseManager.open(); PreparedStatement s=c.prepareStatement(sql); ResultSet rs=s.executeQuery()) { while(rs.next()) result.add(map(rs)); return result; }
        catch(SQLException e){ throw new RuntimeException("Ошибка чтения пациентов: "+e.getMessage(),e); }
    }
    public Patient findById(long id) {
        try(Connection c=DatabaseManager.open(); PreparedStatement s=c.prepareStatement("SELECT id,full_name,phone,email,diagnosis FROM patients WHERE id=?")){s.setLong(1,id); try(ResultSet rs=s.executeQuery()){if(rs.next())return map(rs);}}
        catch(SQLException e){throw new RuntimeException("Ошибка поиска пациента: "+e.getMessage(),e);} throw new EntityNotFoundException("Пациент с ID "+id+" не найден");
    }
    public long save(Patient p){String sql="INSERT INTO patients(full_name,phone,email,diagnosis) VALUES(?,?,?,?) RETURNING id"; try(Connection c=DatabaseManager.open();PreparedStatement s=c.prepareStatement(sql)){s.setString(1,p.getFullName());s.setString(2,p.getPhone());s.setString(3,p.getEmail());s.setString(4,p.getDiagnosis());try(ResultSet rs=s.executeQuery()){rs.next();return rs.getLong(1);}}catch(SQLException e){throw new RuntimeException("Ошибка сохранения пациента: "+e.getMessage(),e);}}
    public void update(Patient p){try(Connection c=DatabaseManager.open();PreparedStatement s=c.prepareStatement("UPDATE patients SET full_name=?,phone=?,email=?,diagnosis=? WHERE id=?")){s.setString(1,p.getFullName());s.setString(2,p.getPhone());s.setString(3,p.getEmail());s.setString(4,p.getDiagnosis());s.setLong(5,p.getId());if(s.executeUpdate()==0)throw new EntityNotFoundException("Пациент не найден");}catch(SQLException e){throw new RuntimeException("Ошибка обновления пациента: "+e.getMessage(),e);}}
    public void delete(long id){try(Connection c=DatabaseManager.open();PreparedStatement s=c.prepareStatement("DELETE FROM patients WHERE id=?")){s.setLong(1,id);if(s.executeUpdate()==0)throw new EntityNotFoundException("Пациент не найден");}catch(SQLException e){throw new RuntimeException("Ошибка удаления пациента: "+e.getMessage(),e);}}
}
