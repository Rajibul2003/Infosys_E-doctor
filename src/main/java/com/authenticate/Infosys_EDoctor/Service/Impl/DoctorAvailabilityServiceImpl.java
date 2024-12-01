package com.authenticate.Infosys_EDoctor.Service.Impl;

import com.authenticate.Infosys_EDoctor.Entity.DoctorAvailability;
import com.authenticate.Infosys_EDoctor.Repository.DoctorAvailabilityRepository;
import com.authenticate.Infosys_EDoctor.Service.DoctorAvailabilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorAvailabilityServiceImpl implements DoctorAvailabilityService {

    @Autowired
    private DoctorAvailabilityRepository availabilityRepository;

    public DoctorAvailability addAvailability(DoctorAvailability availability) {
        return availabilityRepository.save(availability);
    }

    public List<DoctorAvailability> getAvailabilityByDoctorId(String doctorId) {
        return availabilityRepository.findByDoctorId(doctorId);
    }

    public DoctorAvailability updateAvailability(int id, DoctorAvailability updatedAvailability) {
        DoctorAvailability existingAvailability = availabilityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Availability not found"));
        existingAvailability.setFromDate(updatedAvailability.getFromDate());
        existingAvailability.setEndDate(updatedAvailability.getEndDate());
        return availabilityRepository.save(existingAvailability);
    }

    public boolean deleteAvailability(int id) {
        DoctorAvailability existingAvailability = availabilityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Availability not found"));
        availabilityRepository.deleteById(id);
        return true;
    }
}
