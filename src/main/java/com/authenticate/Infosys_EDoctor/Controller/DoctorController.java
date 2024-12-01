package com.authenticate.Infosys_EDoctor.Controller;

import com.authenticate.Infosys_EDoctor.Entity.Doctor;
import com.authenticate.Infosys_EDoctor.Entity.DoctorAvailability;
import com.authenticate.Infosys_EDoctor.Service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.print.Doc;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/doctor")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    @PostMapping("/add")
    public ResponseEntity<Doctor> addDoctor(@RequestBody Doctor doctor) {
        Doctor newDoctor = doctorService.addDoctor(doctor);
        return ResponseEntity.ok(newDoctor);
    }

    @GetMapping("/view")
    public ResponseEntity<Doctor> getDoctorById(@RequestParam String doctorId) {
        Doctor doctor = doctorService.getDoctorById(doctorId);
        return ResponseEntity.ok(doctor);
    }

    @GetMapping("/get-all-doctors")
    public ResponseEntity<List<Doctor>> getAllDoctors() {
        List<Doctor> doctors = doctorService.getAllDoctors();
        return ResponseEntity.ok(doctors);
    }

    @PutMapping("/update")
    public ResponseEntity<Doctor> updateDoctor(@RequestParam String doctorId, @RequestBody Doctor doctor) {
        Doctor updatedDoctor = doctorService.updateDoctor(doctorId, doctor);
        return ResponseEntity.ok(updatedDoctor);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteDoctor(@RequestParam String doctorId) {
        doctorService.deleteDoctor(doctorId);
        return ResponseEntity.ok("Doctor " + doctorId + " deleted successfully.");
    }

    @GetMapping("/{doctorId}/available-slots")
    public ResponseEntity<List<DoctorAvailability>> getAvailableSlots(@PathVariable String doctorId) {
        List<DoctorAvailability> availableSlots = doctorService.getAvailableSlots(doctorId);
        return ResponseEntity.ok(availableSlots);
    }

    @GetMapping("/specialized-doctors")
    public ResponseEntity<List<Doctor>> findDoctorBySpecialization(@RequestParam String specialization) {
        List<Doctor> doctors = doctorService.findDoctorsBySpecialization(specialization);
        return ResponseEntity.ok(doctors);
    }
}

