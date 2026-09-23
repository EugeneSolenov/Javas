CREATE TABLE patients (
    id BIGSERIAL PRIMARY KEY,
    full_name VARCHAR(150) NOT NULL,
    phone VARCHAR(30) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    diagnosis VARCHAR(300) NOT NULL
);

CREATE TABLE appointments (
    id BIGSERIAL PRIMARY KEY,
    patient_id BIGINT NOT NULL REFERENCES patients(id) ON DELETE CASCADE,
    rehabilitation_type VARCHAR(30) NOT NULL CHECK (rehabilitation_type IN ('PHYSICAL','PSYCHOLOGICAL','SPEECH_THERAPY','SOCIAL')),
    start_at TIMESTAMP NOT NULL,
    duration_minutes INTEGER NOT NULL CHECK (duration_minutes BETWEEN 15 AND 240),
    therapist VARCHAR(150) NOT NULL,
    status VARCHAR(30) NOT NULL CHECK (status IN ('PLANNED','CONFIRMED','IN_PROGRESS','COMPLETED','CANCELLED')),
    notes VARCHAR(1000),
    CONSTRAINT unique_therapist_slot UNIQUE (therapist, start_at)
);

CREATE INDEX idx_patients_name ON patients(lower(full_name));
CREATE INDEX idx_appointments_start ON appointments(start_at);
CREATE INDEX idx_appointments_status ON appointments(status);

INSERT INTO patients(full_name, phone, email, diagnosis) VALUES
('Иванов Иван Иванович','+7-900-000-0001','ivanov@example.ru','Последствия травмы колена'),
('Петрова Анна Сергеевна','+7-900-000-0002','petrova@example.ru','Тревожное расстройство'),
('Сидоров Алексей Павлович','+7-900-000-0003','sidorov@example.ru','Нарушение речи'),
('Кузнецова Мария Олеговна','+7-900-000-0004','kuznetsova@example.ru','Инсульт'),
('Смирнов Дмитрий Андреевич','+7-900-000-0005','smirnov@example.ru','Социальная адаптация');

INSERT INTO appointments(patient_id, rehabilitation_type, start_at, duration_minutes, therapist, status, notes)
SELECT id, 'PHYSICAL', '2026-10-01 09:00', 60, 'Орлова Е.В.', 'CONFIRMED', 'Первичная физическая реабилитация' FROM patients WHERE email='ivanov@example.ru';
INSERT INTO appointments(patient_id, rehabilitation_type, start_at, duration_minutes, therapist, status) SELECT id,'PSYCHOLOGICAL','2026-10-01 10:30',50,'Волкова Н.А.','PLANNED' FROM patients WHERE email='petrova@example.ru';
INSERT INTO appointments(patient_id, rehabilitation_type, start_at, duration_minutes, therapist, status) SELECT id,'SPEECH_THERAPY','2026-10-01 12:00',45,'Морозова И.П.','COMPLETED' FROM patients WHERE email='sidorov@example.ru';
INSERT INTO appointments(patient_id, rehabilitation_type, start_at, duration_minutes, therapist, status) SELECT id,'PHYSICAL','2026-10-02 09:00',90,'Орлова Е.В.','IN_PROGRESS' FROM patients WHERE email='kuznetsova@example.ru';
INSERT INTO appointments(patient_id, rehabilitation_type, start_at, duration_minutes, therapist, status) SELECT id,'SOCIAL','2026-10-02 11:00',60,'Федоров Р.С.','CANCELLED' FROM patients WHERE email='smirnov@example.ru';
