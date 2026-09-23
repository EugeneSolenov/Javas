package ru.mirea.rehab.service;

import ru.mirea.rehab.exception.BusinessException;
import ru.mirea.rehab.model.Patient;
import ru.mirea.rehab.repository.PatientRepository;
import java.util.List;

public class PatientService {
    private final PatientRepository repository;
    public PatientService(PatientRepository repository){this.repository=repository;}
    public List<Patient> all(){return repository.findAll();}
    public Patient byId(long id){return repository.findById(id);}
    public long create(String name,String phone,String email,String diagnosis){validate(name,phone,email); return repository.save(new Patient(null,name,phone,email,diagnosis));}
    public void update(Patient p){validate(p.getFullName(),p.getPhone(),p.getEmail());repository.update(p);}
    public void delete(long id){repository.delete(id);}
    private void validate(String name,String phone,String email){if(name==null||name.isBlank())throw new BusinessException("ФИО пациента обязательно");if(phone==null||phone.isBlank())throw new BusinessException("Телефон обязателен");if(email==null||!email.contains("@"))throw new BusinessException("Некорректный email");}
}
