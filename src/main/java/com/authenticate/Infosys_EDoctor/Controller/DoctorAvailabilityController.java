package com.authenticate.Infosys_EDoctor.Controller;

import com.authenticate.Infosys_EDoctor.Entity.DoctorAvailability;
import com.authenticate.Infosys_EDoctor.Service.DoctorAvailabilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctor/availability")
public class DoctorAvailabilityController {

    @Autowired
    private DoctorAvailabilityService availabilityService;

    @PostMapping("/add")
    public ResponseEntity<?> addAvailability(@RequestBody DoctorAvailability availability) {
        DoctorAvailability savedAvailability = availabilityService.addAvailability(availability);
        return ResponseEntity.ok(savedAvailability);
    }

    @GetMapping("/view")
    public ResponseEntity<?> getAvailability(@RequestParam String doctorId) {
        List<DoctorAvailability> availabilities = availabilityService.getAvailabilityByDoctorId(doctorId);
        return ResponseEntity.ok(availabilities);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateAvailability(@RequestParam int id, @RequestBody DoctorAvailability availability) {
        DoctorAvailability updatedAvailability = availabilityService.updateAvailability(id, availability);
        return ResponseEntity.ok(updatedAvailability);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteAvailability(@RequestParam int id) {
        boolean deleted = availabilityService.deleteAvailability(id);
        return deleted? ResponseEntity.ok("Availability deleted successfully"): ResponseEntity.badRequest().body("Enter valid credentials");
    }
}

