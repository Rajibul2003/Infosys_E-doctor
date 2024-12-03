package com.authenticate.Infosys_EDoctor.Repository;

import com.authenticate.Infosys_EDoctor.Entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, String> {

    /**
     * Find a patient by email.
     * @param email the email of the patient
     * @return Optional of Patient
     */
    Optional<Patient> findByEmail(String email);

    /**
     * Find a patient by mobile number.
     * @param mobileNo the mobile number of the patient
     * @return Optional of Patient
     */
    Optional<Patient> findByMobileNo(String mobileNo);
}

