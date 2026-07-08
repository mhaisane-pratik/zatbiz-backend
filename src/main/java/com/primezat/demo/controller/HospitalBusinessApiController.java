package com.primezat.demo.controller;

import com.primezat.demo.model.*;
import com.primezat.demo.repository.*;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/hospital")
@CrossOrigin(origins = "*")
public class HospitalBusinessApiController {

    @Autowired
    private HospitalDoctorRepository doctorRepository;

    @Autowired
    private HospitalAppointmentRepository appointmentRepository;

    @Autowired
    private HospitalPatientRepository patientRepository;

    @Autowired
    private HospitalVitalLogRepository vitalLogRepository;

    @Autowired
    private HospitalLabTestRepository labTestRepository;

    @Autowired
    private HospitalMedicineRepository medicineRepository;

    @Autowired
    private HospitalInvoiceRepository invoiceRepository;

    // ==========================================
    // 1. Hospital Doctors Directory Mappings
    // ==========================================
    @GetMapping("/doctors")
    public List<HospitalDoctor> getDoctors(@RequestParam Long projectId) {
        return doctorRepository.findByProjectId(projectId);
    }

    @PostMapping("/doctors")
    public HospitalDoctor createDoctor(@RequestBody HospitalDoctor doctor) {
        return doctorRepository.save(doctor);
    }

    @PutMapping("/doctors/{id}")
    public ResponseEntity<HospitalDoctor> updateDoctor(@PathVariable Long id, @RequestBody HospitalDoctor updated) {
        Optional<HospitalDoctor> opt = doctorRepository.findById(id);
        if (opt.isEmpty()) return ResponseEntity.notFound().build();
        HospitalDoctor doctor = opt.get();
        if (updated.getName() != null) doctor.setName(updated.getName());
        if (updated.getImageUrl() != null) doctor.setImageUrl(updated.getImageUrl());
        if (updated.getDepartment() != null) doctor.setDepartment(updated.getDepartment());
        if (updated.getSpecialization() != null) doctor.setSpecialization(updated.getSpecialization());
        if (updated.getConsultationFee() != null) doctor.setConsultationFee(updated.getConsultationFee());
        if (updated.getExperience() != null) doctor.setExperience(updated.getExperience());
        if (updated.getQualification() != null) doctor.setQualification(updated.getQualification());
        if (updated.getAvailability() != null) doctor.setAvailability(updated.getAvailability());
        if (updated.getRoomNumber() != null) doctor.setRoomNumber(updated.getRoomNumber());
        if (updated.getBio() != null) doctor.setBio(updated.getBio());
        if (updated.getAvailable() != null) doctor.setAvailable(updated.getAvailable());
        return ResponseEntity.ok(doctorRepository.save(doctor));
    }

    @DeleteMapping("/doctors/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteDoctor(@PathVariable Long id) {
        if (doctorRepository.existsById(id)) {
            doctorRepository.deleteById(id);
            Map<String, Boolean> res = new HashMap<>();
            res.put("success", true);
            return ResponseEntity.ok(res);
        }
        return ResponseEntity.notFound().build();
    }

    // ==========================================
    // 2. Hospital Appointments Mappings
    // ==========================================
    @GetMapping("/appointments")
    public List<HospitalAppointment> getAppointments(@RequestParam Long projectId) {
        return appointmentRepository.findByProjectId(projectId);
    }

    @GetMapping("/appointments/customer")
    public List<HospitalAppointment> getAppointmentsByCustomer(@RequestParam Long projectId, @RequestParam String email) {
        return appointmentRepository.findByProjectIdAndPatientEmail(projectId, email.trim().toLowerCase());
    }

    @PostMapping("/appointments")
    public HospitalAppointment createAppointment(@RequestBody HospitalAppointment appointment) {
        return appointmentRepository.save(appointment);
    }

    @PutMapping("/appointments/{id}")
    public ResponseEntity<HospitalAppointment> updateAppointment(@PathVariable Long id, @RequestBody HospitalAppointment updated) {
        Optional<HospitalAppointment> opt = appointmentRepository.findById(id);
        if (opt.isEmpty()) return ResponseEntity.notFound().build();
        HospitalAppointment appt = opt.get();
        if (updated.getPatientName() != null) appt.setPatientName(updated.getPatientName());
        if (updated.getPatientPhone() != null) appt.setPatientPhone(updated.getPatientPhone());
        if (updated.getDoctorId() != null) appt.setDoctorId(updated.getDoctorId());
        if (updated.getDoctorName() != null) appt.setDoctorName(updated.getDoctorName());
        if (updated.getBookingDate() != null) appt.setBookingDate(updated.getBookingDate());
        if (updated.getBookingTime() != null) appt.setBookingTime(updated.getBookingTime());
        if (updated.getSymptoms() != null) appt.setSymptoms(updated.getSymptoms());
        if (updated.getStatus() != null) appt.setStatus(updated.getStatus());
        return ResponseEntity.ok(appointmentRepository.save(appt));
    }

    @PutMapping("/appointments/{id}/status")
    public ResponseEntity<HospitalAppointment> updateAppointmentStatus(@PathVariable Long id, @RequestBody Map<String, String> body) {
        Optional<HospitalAppointment> opt = appointmentRepository.findById(id);
        if (opt.isEmpty()) return ResponseEntity.notFound().build();
        HospitalAppointment appt = opt.get();
        String status = body.get("status");
        if (status != null) {
            appt.setStatus(status);
        }
        return ResponseEntity.ok(appointmentRepository.save(appt));
    }

    @DeleteMapping("/appointments/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteAppointment(@PathVariable Long id) {
        if (appointmentRepository.existsById(id)) {
            appointmentRepository.deleteById(id);
            Map<String, Boolean> res = new HashMap<>();
            res.put("success", true);
            return ResponseEntity.ok(res);
        }
        return ResponseEntity.notFound().build();
    }

    // ==========================================
    // 3. Hospital Patient Mappings
    // ==========================================
    @PostMapping("/patients/register")
    public ResponseEntity<?> registerPatient(@RequestBody Map<String, String> request) {
        String name = request.get("name");
        String email = request.get("email");
        String password = request.get("password");
        String phone = request.get("phone");
        String address = request.get("address");
        String projectIdStr = request.get("projectId");

        if (email != null) {
            email = email.trim().toLowerCase();
        }

        if (name == null || name.trim().isEmpty() ||
            email == null || email.trim().isEmpty() ||
            password == null || password.trim().isEmpty() ||
            projectIdStr == null || projectIdStr.trim().isEmpty()) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Required fields: name, email, password, projectId");
            return ResponseEntity.badRequest().body(error);
        }

        Long projectId = Long.parseLong(projectIdStr);
        if (patientRepository.findByProjectIdAndEmail(projectId, email).isPresent()) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Patient Email already registered under this clinic project!");
            return ResponseEntity.badRequest().body(error);
        }

        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        HospitalPatient patient = new HospitalPatient();
        patient.setProjectId(projectId);
        patient.setName(name.trim());
        patient.setEmail(email);
        patient.setPassword(hashedPassword);
        patient.setPhone(phone);
        patient.setAddress(address);

        HospitalPatient saved = patientRepository.save(patient);
        Map<String, Object> res = new HashMap<>();
        res.put("id", saved.getId());
        res.put("name", saved.getName());
        res.put("email", saved.getEmail());
        res.put("phone", saved.getPhone());
        res.put("address", saved.getAddress());
        res.put("projectId", saved.getProjectId());
        return ResponseEntity.ok(res);
    }

    @PostMapping("/patients/login")
    public ResponseEntity<?> loginPatient(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String password = request.get("password");
        String projectIdStr = request.get("projectId");

        if (email != null) {
            email = email.trim().toLowerCase();
        }

        if (email == null || email.trim().isEmpty() ||
            password == null || password.trim().isEmpty() ||
            projectIdStr == null || projectIdStr.trim().isEmpty()) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Required fields: email, password, projectId");
            return ResponseEntity.badRequest().body(error);
        }

        Long projectId = Long.parseLong(projectIdStr);
        Optional<HospitalPatient> opt = patientRepository.findByProjectIdAndEmail(projectId, email);
        if (opt.isEmpty()) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Incorrect patient email or password.");
            return ResponseEntity.badRequest().body(error);
        }

        HospitalPatient patient = opt.get();
        if (!BCrypt.checkpw(password, patient.getPassword())) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Incorrect credentials.");
            return ResponseEntity.badRequest().body(error);
        }

        Map<String, Object> res = new HashMap<>();
        res.put("id", patient.getId());
        res.put("name", patient.getName());
        res.put("email", patient.getEmail());
        res.put("phone", patient.getPhone());
        res.put("address", patient.getAddress());
        res.put("projectId", patient.getProjectId());
        return ResponseEntity.ok(res);
    }

    @PutMapping("/patients/{id}")
    public ResponseEntity<HospitalPatient> updatePatient(@PathVariable Long id, @RequestBody HospitalPatient updated) {
        Optional<HospitalPatient> opt = patientRepository.findById(id);
        if (opt.isEmpty()) return ResponseEntity.notFound().build();
        HospitalPatient patient = opt.get();
        if (updated.getName() != null) patient.setName(updated.getName());
        if (updated.getPhone() != null) patient.setPhone(updated.getPhone());
        if (updated.getAddress() != null) patient.setAddress(updated.getAddress());
        return ResponseEntity.ok(patientRepository.save(patient));
    }

    // ==========================================
    // 4. Hospital Nurse Vitals Log Mappings
    // ==========================================
    @GetMapping("/vitals")
    public List<HospitalVitalLog> getVitals(@RequestParam Long projectId) {
        return vitalLogRepository.findByProjectId(projectId);
    }

    @PostMapping("/vitals")
    public HospitalVitalLog createVital(@RequestBody HospitalVitalLog vitalLog) {
        return vitalLogRepository.save(vitalLog);
    }

    // ==========================================
    // 5. Hospital Lab Tests Mappings
    // ==========================================
    @GetMapping("/lab-tests")
    public List<HospitalLabTest> getLabTests(@RequestParam Long projectId) {
        return labTestRepository.findByProjectId(projectId);
    }

    @PostMapping("/lab-tests")
    public HospitalLabTest createLabTest(@RequestBody HospitalLabTest labTest) {
        return labTestRepository.save(labTest);
    }

    @PutMapping("/lab-tests/{id}")
    public ResponseEntity<HospitalLabTest> updateLabTest(@PathVariable Long id, @RequestBody HospitalLabTest updated) {
        Optional<HospitalLabTest> opt = labTestRepository.findById(id);
        if (opt.isEmpty()) return ResponseEntity.notFound().build();
        HospitalLabTest labTest = opt.get();
        if (updated.getResults() != null) labTest.setResults(updated.getResults());
        if (updated.getRemarks() != null) labTest.setRemarks(updated.getRemarks());
        if (updated.getStatus() != null) labTest.setStatus(updated.getStatus());
        return ResponseEntity.ok(labTestRepository.save(labTest));
    }

    // ==========================================
    // 6. Hospital Medicine Inventory Mappings
    // ==========================================
    @GetMapping("/medicine")
    public List<HospitalMedicine> getMedicines(@RequestParam Long projectId) {
        return medicineRepository.findByProjectId(projectId);
    }

    @PostMapping("/medicine")
    public HospitalMedicine createMedicine(@RequestBody HospitalMedicine medicine) {
        return medicineRepository.save(medicine);
    }

    @DeleteMapping("/medicine/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteMedicine(@PathVariable Long id) {
        if (medicineRepository.existsById(id)) {
            medicineRepository.deleteById(id);
            Map<String, Boolean> res = new HashMap<>();
            res.put("success", true);
            return ResponseEntity.ok(res);
        }
        return ResponseEntity.notFound().build();
    }

    // ==========================================
    // 7. Hospital Invoices Mappings
    // ==========================================
    @GetMapping("/invoices")
    public List<HospitalInvoice> getInvoices(@RequestParam Long projectId) {
        return invoiceRepository.findByProjectId(projectId);
    }

    @PostMapping("/invoices")
    public HospitalInvoice createInvoice(@RequestBody HospitalInvoice invoice) {
        return invoiceRepository.save(invoice);
    }

    @PutMapping("/invoices/{id}")
    public ResponseEntity<HospitalInvoice> updateInvoice(@PathVariable Long id, @RequestBody HospitalInvoice updated) {
        Optional<HospitalInvoice> opt = invoiceRepository.findById(id);
        if (opt.isEmpty()) return ResponseEntity.notFound().build();
        HospitalInvoice invoice = opt.get();
        if (updated.getStatus() != null) invoice.setStatus(updated.getStatus());
        if (updated.getPaymentMethod() != null) invoice.setPaymentMethod(updated.getPaymentMethod());
        return ResponseEntity.ok(invoiceRepository.save(invoice));
    }
}
