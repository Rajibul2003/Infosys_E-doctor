package com.authenticate.Infosys_EDoctor.Service.Impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.authenticate.Infosys_EDoctor.Entity.Appointment;
import com.authenticate.Infosys_EDoctor.Entity.Doctor;
import com.authenticate.Infosys_EDoctor.Entity.Patient;
import com.authenticate.Infosys_EDoctor.Repository.PatientRepository;
import com.authenticate.Infosys_EDoctor.Service.AppointmentService;
import com.authenticate.Infosys_EDoctor.Service.DoctorAvailabilityService;
import com.authenticate.Infosys_EDoctor.Service.DoctorService;

import java.time.LocalDate;

import java.util.Optional;

import org.junit.jupiter.api.Disabled;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {PatientServiceImpl.class, PasswordEncoder.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class PatientServiceImplTest {
    @MockBean
    private AppointmentService appointmentService;

    @MockBean
    private DoctorAvailabilityService doctorAvailabilityService;

    @MockBean
    private DoctorService doctorService;

    @MockBean
    private EmailService emailService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private PatientRepository patientRepository;

    @Autowired
    private PatientServiceImpl patientServiceImpl;

    /**
     * Test {@link PatientServiceImpl#addPatient(Patient)}.
     * <ul>
     *   <li>Given {@link EmailService}
     * {@link EmailService#sendPatientIdEmail(String, String)} throw
     * {@link RuntimeException#RuntimeException(String)} with {@code foo}.</li>
     * </ul>
     * <p>
     * Method under test: {@link PatientServiceImpl#addPatient(Patient)}
     */
    @Test
    @DisplayName("Test addPatient(Patient); given EmailService sendPatientIdEmail(String, String) throw RuntimeException(String) with 'foo'")
    void testAddPatient_givenEmailServiceSendPatientIdEmailThrowRuntimeExceptionWithFoo() {
        // Arrange
        when(passwordEncoder.encode(Mockito.<CharSequence>any())).thenReturn("secret");
        doThrow(new RuntimeException("foo")).when(emailService)
                .sendPatientIdEmail(Mockito.<String>any(), Mockito.<String>any());

        Patient patient = new Patient();
        patient.setAddress("42 Main St");
        patient.setAge(1);
        patient.setBloodGroup("Blood Group");
        patient.setEmail("jane.doe@example.org");
        patient.setGender(Patient.Gender.MALE);
        patient.setMobileNo("Mobile No");
        patient.setName("Name");
        patient.setPassword("iloveyou");
        patient.setPatientId("42");
        when(patientRepository.save(Mockito.<Patient>any())).thenReturn(patient);
        Optional<Patient> emptyResult = Optional.empty();
        when(patientRepository.findByEmail(Mockito.<String>any())).thenReturn(emptyResult);

        Patient patient2 = new Patient();
        patient2.setAddress("42 Main St");
        patient2.setAge(1);
        patient2.setBloodGroup("Blood Group");
        patient2.setEmail("jane.doe@example.org");
        patient2.setGender(Patient.Gender.MALE);
        patient2.setMobileNo("Mobile No");
        patient2.setName("Name");
        patient2.setPassword("iloveyou");
        patient2.setPatientId("42");

        // Act and Assert
        assertThrows(RuntimeException.class, () -> patientServiceImpl.addPatient(patient2));
        verify(patientRepository).findByEmail(eq("jane.doe@example.org"));
        verify(emailService).sendPatientIdEmail(eq("jane.doe@example.org"), eq("42"));
        verify(patientRepository).save(isA(Patient.class));
        verify(passwordEncoder).encode(isA(CharSequence.class));
    }

    /**
     * Test {@link PatientServiceImpl#addPatient(Patient)}.
     * <ul>
     *   <li>Given {@link PatientRepository}
     * {@link PatientRepository#findByEmail(String)} return {@link Optional} with
     * {@link Patient} (default constructor).</li>
     * </ul>
     * <p>
     * Method under test: {@link PatientServiceImpl#addPatient(Patient)}
     */
    @Test
    @DisplayName("Test addPatient(Patient); given PatientRepository findByEmail(String) return Optional with Patient (default constructor)")
    void testAddPatient_givenPatientRepositoryFindByEmailReturnOptionalWithPatient() {
        // Arrange
        Patient patient = new Patient();
        patient.setAddress("42 Main St");
        patient.setAge(1);
        patient.setBloodGroup("Blood Group");
        patient.setEmail("jane.doe@example.org");
        patient.setGender(Patient.Gender.MALE);
        patient.setMobileNo("Mobile No");
        patient.setName("Name");
        patient.setPassword("iloveyou");
        patient.setPatientId("42");
        Optional<Patient> ofResult = Optional.of(patient);
        when(patientRepository.findByEmail(Mockito.<String>any())).thenReturn(ofResult);

        Patient patient2 = new Patient();
        patient2.setAddress("42 Main St");
        patient2.setAge(1);
        patient2.setBloodGroup("Blood Group");
        patient2.setEmail("jane.doe@example.org");
        patient2.setGender(Patient.Gender.MALE);
        patient2.setMobileNo("Mobile No");
        patient2.setName("Name");
        patient2.setPassword("iloveyou");
        patient2.setPatientId("42");

        // Act and Assert
        assertThrows(RuntimeException.class, () -> patientServiceImpl.addPatient(patient2));
        verify(patientRepository).findByEmail(eq("jane.doe@example.org"));
    }

    /**
     * Test {@link PatientServiceImpl#addPatient(Patient)}.
     * <ul>
     *   <li>Then {@link Patient} (default constructor) Password is
     * {@code secret}.</li>
     * </ul>
     * <p>
     * Method under test: {@link PatientServiceImpl#addPatient(Patient)}
     */
    @Test
    @DisplayName("Test addPatient(Patient); then Patient (default constructor) Password is 'secret'")
    void testAddPatient_thenPatientPasswordIsSecret() {
        // Arrange
        when(passwordEncoder.encode(Mockito.<CharSequence>any())).thenReturn("secret");
        doNothing().when(emailService).sendPatientIdEmail(Mockito.<String>any(), Mockito.<String>any());

        Patient patient = new Patient();
        patient.setAddress("42 Main St");
        patient.setAge(1);
        patient.setBloodGroup("Blood Group");
        patient.setEmail("jane.doe@example.org");
        patient.setGender(Patient.Gender.MALE);
        patient.setMobileNo("Mobile No");
        patient.setName("Name");
        patient.setPassword("iloveyou");
        patient.setPatientId("42");
        when(patientRepository.save(Mockito.<Patient>any())).thenReturn(patient);
        Optional<Patient> emptyResult = Optional.empty();
        when(patientRepository.findByEmail(Mockito.<String>any())).thenReturn(emptyResult);

        Patient patient2 = new Patient();
        patient2.setAddress("42 Main St");
        patient2.setAge(1);
        patient2.setBloodGroup("Blood Group");
        patient2.setEmail("jane.doe@example.org");
        patient2.setGender(Patient.Gender.MALE);
        patient2.setMobileNo("Mobile No");
        patient2.setName("Name");
        patient2.setPassword("iloveyou");
        patient2.setPatientId("42");

        // Act
        Patient actualAddPatientResult = patientServiceImpl.addPatient(patient2);

        // Assert
        verify(patientRepository).findByEmail(eq("jane.doe@example.org"));
        verify(emailService).sendPatientIdEmail(eq("jane.doe@example.org"), eq("42"));
        verify(patientRepository).save(isA(Patient.class));
        verify(passwordEncoder).encode(isA(CharSequence.class));
        assertEquals("secret", patient2.getPassword());
        assertSame(patient, actualAddPatientResult);
    }

    /**
     * Test {@link PatientServiceImpl#addPatient(Patient)}.
     * <ul>
     *   <li>Then throw {@link IllegalArgumentException}.</li>
     * </ul>
     * <p>
     * Method under test: {@link PatientServiceImpl#addPatient(Patient)}
     */
    @Test
    @DisplayName("Test addPatient(Patient); then throw IllegalArgumentException")
    void testAddPatient_thenThrowIllegalArgumentException() {
        // Arrange
        when(patientRepository.findByEmail(Mockito.<String>any())).thenThrow(new IllegalArgumentException("foo"));

        Patient patient = new Patient();
        patient.setAddress("42 Main St");
        patient.setAge(1);
        patient.setBloodGroup("Blood Group");
        patient.setEmail("jane.doe@example.org");
        patient.setGender(Patient.Gender.MALE);
        patient.setMobileNo("Mobile No");
        patient.setName("Name");
        patient.setPassword("iloveyou");
        patient.setPatientId("42");

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> patientServiceImpl.addPatient(patient));
        verify(patientRepository).findByEmail(eq("jane.doe@example.org"));
    }

    /**
     * Test {@link PatientServiceImpl#updateProfile(String, Patient)}.
     * <p>
     * Method under test: {@link PatientServiceImpl#updateProfile(String, Patient)}
     */
    @Test
    @DisplayName("Test updateProfile(String, Patient)")
    @Disabled("TODO: Complete this test")
    void testUpdateProfile() {
        // TODO: Diffblue Cover was only able to create a partial test for this method:
        //   Reason: Failed to create Spring context.
        //   Attempt to initialize test context failed with
        //   java.lang.IllegalStateException: ApplicationContext failure threshold (1) exceeded: skipping repeated attempt to load context for [MergedContextConfiguration@6af0189 testClass = com.authenticate.Infosys_EDoctor.Service.Impl.DiffblueFakeClass246, locations = [], classes = [com.authenticate.Infosys_EDoctor.Service.Impl.PatientServiceImpl, org.springframework.security.crypto.password.PasswordEncoder], contextInitializerClasses = [], activeProfiles = [], propertySourceDescriptors = [], propertySourceProperties = [], contextCustomizers = [org.springframework.boot.test.context.filter.ExcludeFilterContextCustomizer@2ebc493f, org.springframework.boot.test.json.DuplicateJsonObjectContextCustomizerFactory$DuplicateJsonObjectContextCustomizer@3d1fbba8, org.springframework.boot.test.mock.mockito.MockitoContextCustomizer@c3cbbb58, org.springframework.boot.test.web.reactor.netty.DisableReactorResourceFactoryGlobalResourcesContextCustomizerFactory$DisableReactorResourceFactoryGlobalResourcesContextCustomizerCustomizer@438f7b77, org.springframework.boot.test.autoconfigure.OnFailureConditionReportContextCustomizerFactory$OnFailureConditionReportContextCustomizer@411fe955, org.springframework.boot.test.autoconfigure.actuate.observability.ObservabilityContextCustomizerFactory$DisableObservabilityContextCustomizer@1f, org.springframework.boot.test.autoconfigure.properties.PropertyMappingContextCustomizer@0, org.springframework.boot.test.autoconfigure.web.servlet.WebDriverContextCustomizer@6f84105b], contextLoader = org.springframework.test.context.support.DelegatingSmartContextLoader, parent = null]
        //       at org.springframework.test.context.cache.DefaultCacheAwareContextLoaderDelegate.loadContext(DefaultCacheAwareContextLoaderDelegate.java:145)
        //       at org.springframework.test.context.support.DefaultTestContext.getApplicationContext(DefaultTestContext.java:130)
        //       at java.base/java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:197)
        //       at java.base/java.util.ArrayList$ArrayListSpliterator.forEachRemaining(ArrayList.java:1625)
        //       at java.base/java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:509)
        //       at java.base/java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:499)
        //       at java.base/java.util.stream.ReduceOps$ReduceOp.evaluateSequential(ReduceOps.java:921)
        //       at java.base/java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.base/java.util.stream.ReferencePipeline.collect(ReferencePipeline.java:682)
        //   See https://diff.blue/R026 to resolve this issue.

        // Arrange
        Patient updatedPatient = new Patient();
        updatedPatient.setAddress("42 Main St");
        updatedPatient.setAge(1);
        updatedPatient.setBloodGroup("Blood Group");
        updatedPatient.setEmail("jane.doe@example.org");
        updatedPatient.setGender(Patient.Gender.MALE);
        updatedPatient.setMobileNo("Mobile No");
        updatedPatient.setName("Name");
        updatedPatient.setPassword("iloveyou");
        updatedPatient.setPatientId("42");

        // Act
        patientServiceImpl.updateProfile("42", updatedPatient);
    }

    /**
     * Test {@link PatientServiceImpl#findDoctors()}.
     * <p>
     * Method under test: {@link PatientServiceImpl#findDoctors()}
     */
    @Test
    @DisplayName("Test findDoctors()")
    @Disabled("TODO: Complete this test")
    void testFindDoctors() {
        // TODO: Diffblue Cover was only able to create a partial test for this method:
        //   Reason: Failed to create Spring context.
        //   Attempt to initialize test context failed with
        //   java.lang.IllegalStateException: ApplicationContext failure threshold (1) exceeded: skipping repeated attempt to load context for [MergedContextConfiguration@703c9fc2 testClass = com.authenticate.Infosys_EDoctor.Service.Impl.DiffblueFakeClass241, locations = [], classes = [com.authenticate.Infosys_EDoctor.Service.Impl.PatientServiceImpl, org.springframework.security.crypto.password.PasswordEncoder], contextInitializerClasses = [], activeProfiles = [], propertySourceDescriptors = [], propertySourceProperties = [], contextCustomizers = [org.springframework.boot.test.context.filter.ExcludeFilterContextCustomizer@2ebc493f, org.springframework.boot.test.json.DuplicateJsonObjectContextCustomizerFactory$DuplicateJsonObjectContextCustomizer@3d1fbba8, org.springframework.boot.test.mock.mockito.MockitoContextCustomizer@c3cbbb58, org.springframework.boot.test.web.reactor.netty.DisableReactorResourceFactoryGlobalResourcesContextCustomizerFactory$DisableReactorResourceFactoryGlobalResourcesContextCustomizerCustomizer@438f7b77, org.springframework.boot.test.autoconfigure.OnFailureConditionReportContextCustomizerFactory$OnFailureConditionReportContextCustomizer@411fe955, org.springframework.boot.test.autoconfigure.actuate.observability.ObservabilityContextCustomizerFactory$DisableObservabilityContextCustomizer@1f, org.springframework.boot.test.autoconfigure.properties.PropertyMappingContextCustomizer@0, org.springframework.boot.test.autoconfigure.web.servlet.WebDriverContextCustomizer@6f84105b], contextLoader = org.springframework.test.context.support.DelegatingSmartContextLoader, parent = null]
        //       at org.springframework.test.context.cache.DefaultCacheAwareContextLoaderDelegate.loadContext(DefaultCacheAwareContextLoaderDelegate.java:145)
        //       at org.springframework.test.context.support.DefaultTestContext.getApplicationContext(DefaultTestContext.java:130)
        //       at java.base/java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:197)
        //       at java.base/java.util.ArrayList$ArrayListSpliterator.forEachRemaining(ArrayList.java:1625)
        //       at java.base/java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:509)
        //       at java.base/java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:499)
        //       at java.base/java.util.stream.ReduceOps$ReduceOp.evaluateSequential(ReduceOps.java:921)
        //       at java.base/java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.base/java.util.stream.ReferencePipeline.collect(ReferencePipeline.java:682)
        //   See https://diff.blue/R026 to resolve this issue.

        // Arrange and Act
        patientServiceImpl.findDoctors();
    }

    /**
     * Test {@link PatientServiceImpl#makeAppointment(Appointment)}.
     * <p>
     * Method under test: {@link PatientServiceImpl#makeAppointment(Appointment)}
     */
    @Test
    @DisplayName("Test makeAppointment(Appointment)")
    @Disabled("TODO: Complete this test")
    void testMakeAppointment() {
        // TODO: Diffblue Cover was only able to create a partial test for this method:
        //   Reason: Failed to create Spring context.
        //   Attempt to initialize test context failed with
        //   java.lang.IllegalStateException: ApplicationContext failure threshold (1) exceeded: skipping repeated attempt to load context for [MergedContextConfiguration@5fa8e00a testClass = com.authenticate.Infosys_EDoctor.Service.Impl.DiffblueFakeClass244, locations = [], classes = [com.authenticate.Infosys_EDoctor.Service.Impl.PatientServiceImpl, org.springframework.security.crypto.password.PasswordEncoder], contextInitializerClasses = [], activeProfiles = [], propertySourceDescriptors = [], propertySourceProperties = [], contextCustomizers = [org.springframework.boot.test.context.filter.ExcludeFilterContextCustomizer@2ebc493f, org.springframework.boot.test.json.DuplicateJsonObjectContextCustomizerFactory$DuplicateJsonObjectContextCustomizer@3d1fbba8, org.springframework.boot.test.mock.mockito.MockitoContextCustomizer@c3cbbb58, org.springframework.boot.test.web.reactor.netty.DisableReactorResourceFactoryGlobalResourcesContextCustomizerFactory$DisableReactorResourceFactoryGlobalResourcesContextCustomizerCustomizer@438f7b77, org.springframework.boot.test.autoconfigure.OnFailureConditionReportContextCustomizerFactory$OnFailureConditionReportContextCustomizer@411fe955, org.springframework.boot.test.autoconfigure.actuate.observability.ObservabilityContextCustomizerFactory$DisableObservabilityContextCustomizer@1f, org.springframework.boot.test.autoconfigure.properties.PropertyMappingContextCustomizer@0, org.springframework.boot.test.autoconfigure.web.servlet.WebDriverContextCustomizer@6f84105b], contextLoader = org.springframework.test.context.support.DelegatingSmartContextLoader, parent = null]
        //       at org.springframework.test.context.cache.DefaultCacheAwareContextLoaderDelegate.loadContext(DefaultCacheAwareContextLoaderDelegate.java:145)
        //       at org.springframework.test.context.support.DefaultTestContext.getApplicationContext(DefaultTestContext.java:130)
        //       at java.base/java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:197)
        //       at java.base/java.util.ArrayList$ArrayListSpliterator.forEachRemaining(ArrayList.java:1625)
        //       at java.base/java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:509)
        //       at java.base/java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:499)
        //       at java.base/java.util.stream.ReduceOps$ReduceOp.evaluateSequential(ReduceOps.java:921)
        //       at java.base/java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.base/java.util.stream.ReferencePipeline.collect(ReferencePipeline.java:682)
        //   See https://diff.blue/R026 to resolve this issue.

        // Arrange
        Doctor doctor = new Doctor();
        doctor.setChargedPerVisit(1);
        doctor.setDoctorId("42");
        doctor.setEmail("jane.doe@example.org");
        doctor.setHospitalName("Hospital Name");
        doctor.setLocation("Location");
        doctor.setMobileNo("Mobile No");
        doctor.setName("Name");
        doctor.setPassword("iloveyou");
        doctor.setSpecialization("Specialization");

        Patient patient = new Patient();
        patient.setAddress("42 Main St");
        patient.setAge(1);
        patient.setBloodGroup("Blood Group");
        patient.setEmail("jane.doe@example.org");
        patient.setGender(Patient.Gender.MALE);
        patient.setMobileNo("Mobile No");
        patient.setName("Name");
        patient.setPassword("iloveyou");
        patient.setPatientId("42");

        Appointment appointment = new Appointment();
        appointment.setAppointmentDateTime(LocalDate.of(1970, 1, 1).atStartOfDay());
        appointment.setAppointmentId(1L);
        appointment.setDoctor(doctor);
        appointment.setPatient(patient);
        appointment.setReason("Just cause");
        appointment.setStatus(Appointment.Status.Pending);

        // Act
        patientServiceImpl.makeAppointment(appointment);
    }

    /**
     * Test {@link PatientServiceImpl#updateAppointment(Long, Appointment)}.
     * <p>
     * Method under test:
     * {@link PatientServiceImpl#updateAppointment(Long, Appointment)}
     */
    @Test
    @DisplayName("Test updateAppointment(Long, Appointment)")
    @Disabled("TODO: Complete this test")
    void testUpdateAppointment() {
        // TODO: Diffblue Cover was only able to create a partial test for this method:
        //   Reason: Failed to create Spring context.
        //   Attempt to initialize test context failed with
        //   java.lang.IllegalStateException: ApplicationContext failure threshold (1) exceeded: skipping repeated attempt to load context for [MergedContextConfiguration@866ebb4 testClass = com.authenticate.Infosys_EDoctor.Service.Impl.DiffblueFakeClass245, locations = [], classes = [com.authenticate.Infosys_EDoctor.Service.Impl.PatientServiceImpl, org.springframework.security.crypto.password.PasswordEncoder], contextInitializerClasses = [], activeProfiles = [], propertySourceDescriptors = [], propertySourceProperties = [], contextCustomizers = [org.springframework.boot.test.context.filter.ExcludeFilterContextCustomizer@2ebc493f, org.springframework.boot.test.json.DuplicateJsonObjectContextCustomizerFactory$DuplicateJsonObjectContextCustomizer@3d1fbba8, org.springframework.boot.test.mock.mockito.MockitoContextCustomizer@c3cbbb58, org.springframework.boot.test.web.reactor.netty.DisableReactorResourceFactoryGlobalResourcesContextCustomizerFactory$DisableReactorResourceFactoryGlobalResourcesContextCustomizerCustomizer@438f7b77, org.springframework.boot.test.autoconfigure.OnFailureConditionReportContextCustomizerFactory$OnFailureConditionReportContextCustomizer@411fe955, org.springframework.boot.test.autoconfigure.actuate.observability.ObservabilityContextCustomizerFactory$DisableObservabilityContextCustomizer@1f, org.springframework.boot.test.autoconfigure.properties.PropertyMappingContextCustomizer@0, org.springframework.boot.test.autoconfigure.web.servlet.WebDriverContextCustomizer@6f84105b], contextLoader = org.springframework.test.context.support.DelegatingSmartContextLoader, parent = null]
        //       at org.springframework.test.context.cache.DefaultCacheAwareContextLoaderDelegate.loadContext(DefaultCacheAwareContextLoaderDelegate.java:145)
        //       at org.springframework.test.context.support.DefaultTestContext.getApplicationContext(DefaultTestContext.java:130)
        //       at java.base/java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:197)
        //       at java.base/java.util.ArrayList$ArrayListSpliterator.forEachRemaining(ArrayList.java:1625)
        //       at java.base/java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:509)
        //       at java.base/java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:499)
        //       at java.base/java.util.stream.ReduceOps$ReduceOp.evaluateSequential(ReduceOps.java:921)
        //       at java.base/java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.base/java.util.stream.ReferencePipeline.collect(ReferencePipeline.java:682)
        //   See https://diff.blue/R026 to resolve this issue.

        // Arrange
        Doctor doctor = new Doctor();
        doctor.setChargedPerVisit(1);
        doctor.setDoctorId("42");
        doctor.setEmail("jane.doe@example.org");
        doctor.setHospitalName("Hospital Name");
        doctor.setLocation("Location");
        doctor.setMobileNo("Mobile No");
        doctor.setName("Name");
        doctor.setPassword("iloveyou");
        doctor.setSpecialization("Specialization");

        Patient patient = new Patient();
        patient.setAddress("42 Main St");
        patient.setAge(1);
        patient.setBloodGroup("Blood Group");
        patient.setEmail("jane.doe@example.org");
        patient.setGender(Patient.Gender.MALE);
        patient.setMobileNo("Mobile No");
        patient.setName("Name");
        patient.setPassword("iloveyou");
        patient.setPatientId("42");

        Appointment updatedAppointment = new Appointment();
        updatedAppointment.setAppointmentDateTime(LocalDate.of(1970, 1, 1).atStartOfDay());
        updatedAppointment.setAppointmentId(1L);
        updatedAppointment.setDoctor(doctor);
        updatedAppointment.setPatient(patient);
        updatedAppointment.setReason("Just cause");
        updatedAppointment.setStatus(Appointment.Status.Pending);

        // Act
        patientServiceImpl.updateAppointment(1L, updatedAppointment);
    }

    /**
     * Test {@link PatientServiceImpl#cancelAppointment(Long, String)}.
     * <p>
     * Method under test: {@link PatientServiceImpl#cancelAppointment(Long, String)}
     */
    @Test
    @DisplayName("Test cancelAppointment(Long, String)")
    @Disabled("TODO: Complete this test")
    void testCancelAppointment() {
        // TODO: Diffblue Cover was only able to create a partial test for this method:
        //   Reason: Failed to create Spring context.
        //   Attempt to initialize test context failed with
        //   java.lang.IllegalStateException: ApplicationContext failure threshold (1) exceeded: skipping repeated attempt to load context for [MergedContextConfiguration@1bc7cc2b testClass = com.authenticate.Infosys_EDoctor.Service.Impl.DiffblueFakeClass240, locations = [], classes = [com.authenticate.Infosys_EDoctor.Service.Impl.PatientServiceImpl, org.springframework.security.crypto.password.PasswordEncoder], contextInitializerClasses = [], activeProfiles = [], propertySourceDescriptors = [], propertySourceProperties = [], contextCustomizers = [org.springframework.boot.test.context.filter.ExcludeFilterContextCustomizer@2ebc493f, org.springframework.boot.test.json.DuplicateJsonObjectContextCustomizerFactory$DuplicateJsonObjectContextCustomizer@3d1fbba8, org.springframework.boot.test.mock.mockito.MockitoContextCustomizer@c3cbbb58, org.springframework.boot.test.web.reactor.netty.DisableReactorResourceFactoryGlobalResourcesContextCustomizerFactory$DisableReactorResourceFactoryGlobalResourcesContextCustomizerCustomizer@438f7b77, org.springframework.boot.test.autoconfigure.OnFailureConditionReportContextCustomizerFactory$OnFailureConditionReportContextCustomizer@411fe955, org.springframework.boot.test.autoconfigure.actuate.observability.ObservabilityContextCustomizerFactory$DisableObservabilityContextCustomizer@1f, org.springframework.boot.test.autoconfigure.properties.PropertyMappingContextCustomizer@0, org.springframework.boot.test.autoconfigure.web.servlet.WebDriverContextCustomizer@6f84105b], contextLoader = org.springframework.test.context.support.DelegatingSmartContextLoader, parent = null]
        //       at org.springframework.test.context.cache.DefaultCacheAwareContextLoaderDelegate.loadContext(DefaultCacheAwareContextLoaderDelegate.java:145)
        //       at org.springframework.test.context.support.DefaultTestContext.getApplicationContext(DefaultTestContext.java:130)
        //       at java.base/java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:197)
        //       at java.base/java.util.ArrayList$ArrayListSpliterator.forEachRemaining(ArrayList.java:1625)
        //       at java.base/java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:509)
        //       at java.base/java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:499)
        //       at java.base/java.util.stream.ReduceOps$ReduceOp.evaluateSequential(ReduceOps.java:921)
        //       at java.base/java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.base/java.util.stream.ReferencePipeline.collect(ReferencePipeline.java:682)
        //   See https://diff.blue/R026 to resolve this issue.

        // Arrange and Act
        patientServiceImpl.cancelAppointment(1L, "Just cause");
    }

    /**
     * Test {@link PatientServiceImpl#findDoctorsBySpecialization(String)}.
     * <p>
     * Method under test:
     * {@link PatientServiceImpl#findDoctorsBySpecialization(String)}
     */
    @Test
    @DisplayName("Test findDoctorsBySpecialization(String)")
    @Disabled("TODO: Complete this test")
    void testFindDoctorsBySpecialization() {
        // TODO: Diffblue Cover was only able to create a partial test for this method:
        //   Reason: Failed to create Spring context.
        //   Attempt to initialize test context failed with
        //   java.lang.IllegalStateException: ApplicationContext failure threshold (1) exceeded: skipping repeated attempt to load context for [MergedContextConfiguration@2af8fdf6 testClass = com.authenticate.Infosys_EDoctor.Service.Impl.DiffblueFakeClass242, locations = [], classes = [com.authenticate.Infosys_EDoctor.Service.Impl.PatientServiceImpl, org.springframework.security.crypto.password.PasswordEncoder], contextInitializerClasses = [], activeProfiles = [], propertySourceDescriptors = [], propertySourceProperties = [], contextCustomizers = [org.springframework.boot.test.context.filter.ExcludeFilterContextCustomizer@2ebc493f, org.springframework.boot.test.json.DuplicateJsonObjectContextCustomizerFactory$DuplicateJsonObjectContextCustomizer@3d1fbba8, org.springframework.boot.test.mock.mockito.MockitoContextCustomizer@c3cbbb58, org.springframework.boot.test.web.reactor.netty.DisableReactorResourceFactoryGlobalResourcesContextCustomizerFactory$DisableReactorResourceFactoryGlobalResourcesContextCustomizerCustomizer@438f7b77, org.springframework.boot.test.autoconfigure.OnFailureConditionReportContextCustomizerFactory$OnFailureConditionReportContextCustomizer@411fe955, org.springframework.boot.test.autoconfigure.actuate.observability.ObservabilityContextCustomizerFactory$DisableObservabilityContextCustomizer@1f, org.springframework.boot.test.autoconfigure.properties.PropertyMappingContextCustomizer@0, org.springframework.boot.test.autoconfigure.web.servlet.WebDriverContextCustomizer@6f84105b], contextLoader = org.springframework.test.context.support.DelegatingSmartContextLoader, parent = null]
        //       at org.springframework.test.context.cache.DefaultCacheAwareContextLoaderDelegate.loadContext(DefaultCacheAwareContextLoaderDelegate.java:145)
        //       at org.springframework.test.context.support.DefaultTestContext.getApplicationContext(DefaultTestContext.java:130)
        //       at java.base/java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:197)
        //       at java.base/java.util.ArrayList$ArrayListSpliterator.forEachRemaining(ArrayList.java:1625)
        //       at java.base/java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:509)
        //       at java.base/java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:499)
        //       at java.base/java.util.stream.ReduceOps$ReduceOp.evaluateSequential(ReduceOps.java:921)
        //       at java.base/java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.base/java.util.stream.ReferencePipeline.collect(ReferencePipeline.java:682)
        //   See https://diff.blue/R026 to resolve this issue.

        // Arrange and Act
        patientServiceImpl.findDoctorsBySpecialization("Specialization");
    }

    /**
     * Test {@link PatientServiceImpl#getAvailableDates(String)}.
     * <p>
     * Method under test: {@link PatientServiceImpl#getAvailableDates(String)}
     */
    @Test
    @DisplayName("Test getAvailableDates(String)")
    @Disabled("TODO: Complete this test")
    void testGetAvailableDates() {
        // TODO: Diffblue Cover was only able to create a partial test for this method:
        //   Reason: Failed to create Spring context.
        //   Attempt to initialize test context failed with
        //   java.lang.IllegalStateException: ApplicationContext failure threshold (1) exceeded: skipping repeated attempt to load context for [MergedContextConfiguration@43a41d74 testClass = com.authenticate.Infosys_EDoctor.Service.Impl.DiffblueFakeClass243, locations = [], classes = [com.authenticate.Infosys_EDoctor.Service.Impl.PatientServiceImpl, org.springframework.security.crypto.password.PasswordEncoder], contextInitializerClasses = [], activeProfiles = [], propertySourceDescriptors = [], propertySourceProperties = [], contextCustomizers = [org.springframework.boot.test.context.filter.ExcludeFilterContextCustomizer@2ebc493f, org.springframework.boot.test.json.DuplicateJsonObjectContextCustomizerFactory$DuplicateJsonObjectContextCustomizer@3d1fbba8, org.springframework.boot.test.mock.mockito.MockitoContextCustomizer@c3cbbb58, org.springframework.boot.test.web.reactor.netty.DisableReactorResourceFactoryGlobalResourcesContextCustomizerFactory$DisableReactorResourceFactoryGlobalResourcesContextCustomizerCustomizer@438f7b77, org.springframework.boot.test.autoconfigure.OnFailureConditionReportContextCustomizerFactory$OnFailureConditionReportContextCustomizer@411fe955, org.springframework.boot.test.autoconfigure.actuate.observability.ObservabilityContextCustomizerFactory$DisableObservabilityContextCustomizer@1f, org.springframework.boot.test.autoconfigure.properties.PropertyMappingContextCustomizer@0, org.springframework.boot.test.autoconfigure.web.servlet.WebDriverContextCustomizer@6f84105b], contextLoader = org.springframework.test.context.support.DelegatingSmartContextLoader, parent = null]
        //       at org.springframework.test.context.cache.DefaultCacheAwareContextLoaderDelegate.loadContext(DefaultCacheAwareContextLoaderDelegate.java:145)
        //       at org.springframework.test.context.support.DefaultTestContext.getApplicationContext(DefaultTestContext.java:130)
        //       at java.base/java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:197)
        //       at java.base/java.util.ArrayList$ArrayListSpliterator.forEachRemaining(ArrayList.java:1625)
        //       at java.base/java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:509)
        //       at java.base/java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:499)
        //       at java.base/java.util.stream.ReduceOps$ReduceOp.evaluateSequential(ReduceOps.java:921)
        //       at java.base/java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        //       at java.base/java.util.stream.ReferencePipeline.collect(ReferencePipeline.java:682)
        //   See https://diff.blue/R026 to resolve this issue.

        // Arrange and Act
        patientServiceImpl.getAvailableDates("42");
    }
}
