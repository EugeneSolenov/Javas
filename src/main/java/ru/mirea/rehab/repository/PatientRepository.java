package ru.mirea.rehab.repository;

import ru.mirea.rehab.model.Patient;
import java.util.List;

public interface PatientRepository {
    List<Patient> findAll();
    Patient findById(long id);
    long save(Patient patient);
    void update(Patient patient);
    void delete(long id);
}
