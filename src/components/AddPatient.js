import React, { useState } from "react";
import axios from "../services/api"; 
import "../CSS/AddPatient.css";

const AddPatient = () => {
    const [hasPatient, setHasPatient] = useState(false);
    const [patientId, setPatientId] = useState("");
    const [patientProfile, setPatientProfile] = useState(null);
    const [formData, setFormData] = useState({
        name: "",
        mobileNo: "",
        email: "",
        password: "",
        bloodGroup: "",
        gender: "MALE",
        age: "",
        address: ""
    });
    const [loading, setLoading] = useState(false);

    // Fetch Patient Profile by ID
    const fetchPatientProfile = async () => {
        setLoading(true);
        try {
            const response = await axios.get(`/patient/viewProfile?patientId=${patientId}`); 
            setPatientProfile(response.data);
            setHasPatient(true);
        } catch (error) {
            alert("Patient profile not found. Please check the Patient ID.");
            console.error("Error fetching patient profile:", error);
        } finally {
            setLoading(false);
        }
    };

    // Handle Form Input Changes
    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setFormData({ ...formData, [name]: value });
    };

    // Add or Update Patient Profile
    const handleSubmit = async (e) => {
        e.preventDefault();
        setLoading(true);
        try {
            if (patientProfile) {
                // Update existing profile
                const response = await axios.put(`/patient/updateProfile?patientId=${patientProfile.patientId}`, formData);
                alert("Patient profile updated successfully!");
                setPatientProfile(response.data);
            } else {
                // Add new profile
                const response = await axios.post("/patient/addProfile", formData); 
                alert("Patient added successfully! \nCheck email for Patient ID.");
                setPatientProfile(response.data);
                setHasPatient(true);
                setFormData({
                    name: "",
                    mobileNo: "",
                    email: "",
                    password: "",
                    bloodGroup: "",
                    gender: "MALE",
                    age: "",
                    address: ""
                });
                setPatientId(response.data.patientId);
            }
        } catch (error) {
            alert("Error saving profile. Please try again.");
            console.error("Error submitting profile:", error);
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="profile-container">
            <h2>Patient Profile</h2>

            {/* If no profile exists, show Add Profile or Fetch Profile section */}
            {!patientProfile && (
                <>
                    {!hasPatient && (
                        <div className="fetch-profile-section">
                            <h3>Already have a profile?</h3>
                            <div>
                                <label htmlFor="patientId">Enter Patient ID:</label>
                                <input
                                    id="patientId"
                                    type="text"
                                    value={patientId}
                                    onChange={(e) => setPatientId(e.target.value)}
                                />
                                <button onClick={fetchPatientProfile} disabled={loading}>
                                    {loading ? "Fetching..." : "Fetch Profile"}
                                </button>
                            </div>
                        </div>
                    )}

                    <h3>Add Profile</h3>
                    <form onSubmit={handleSubmit}>
                        <div>
                            <label htmlFor="name">Name:</label>
                            <input
                                id="name"
                                name="name"
                                value={formData.name}
                                onChange={handleInputChange}
                                required
                            />
                        </div>
                        <div>
                            <label htmlFor="mobileNo">Mobile Number:</label>
                            <input
                                id="mobileNo"
                                name="mobileNo"
                                type="tel"
                                pattern="\d{10}"
                                title="Mobile number must consist of 10 digits"
                                value={formData.mobileNo}
                                onChange={handleInputChange}
                                required
                            />
                        </div>
                        <div>
                            <label htmlFor="email">Email:</label>
                            <input
                                id="email"
                                name="email"
                                type="email"
                                value={formData.email}
                                onChange={handleInputChange}
                                required
                            />
                        </div>
                        <div>
                            <label htmlFor="password">Password:</label>
                            <input
                                id="password"
                                name="password"
                                type="password"
                                value={formData.password}
                                onChange={handleInputChange}
                                required
                                minLength="8"
                            />
                        </div>
                        <div>
                            <label htmlFor="bloodGroup">Blood Group:</label>
                            <input
                                id="bloodGroup"
                                name="bloodGroup"
                                value={formData.bloodGroup}
                                onChange={handleInputChange}
                            />
                        </div>
                        <div>
                            <label htmlFor="gender">Gender:</label>
                            <select
                                id="gender"
                                name="gender"
                                value={formData.gender}
                                onChange={handleInputChange}
                            >
                                <option value="MALE">Male</option>
                                <option value="FEMALE">Female</option>
                                <option value="OTHERS">Others</option>
                            </select>
                        </div>
                        <div>
                            <label htmlFor="age">Age:</label>
                            <input
                                id="age"
                                name="age"
                                type="number"
                                min="1"
                                value={formData.age}
                                onChange={handleInputChange}
                                required
                            />
                        </div>
                        <div>
                            <label htmlFor="address">Address:</label>
                            <textarea
                                id="address"
                                name="address"
                                value={formData.address}
                                onChange={handleInputChange}
                                required
                            />
                        </div>
                        <button type="submit" disabled={loading}>
                            {loading ? "Saving..." : "Add Patient"}
                        </button>
                    </form>
                </>
            )}

            {/* If profile exists, show the profile details */}
            {patientProfile && (
                <>
                    <h3>Profile Details</h3>
                    <form onSubmit={handleSubmit}>
                        {Object.keys(patientProfile).map((key) => (
                            <div key={key}>
                                <label htmlFor={key}>{key}:</label>
                                <input
                                    id={key}
                                    name={key}
                                    value={patientProfile[key] || ""}
                                    onChange={(e) =>
                                        setPatientProfile({
                                            ...patientProfile,
                                            [key]: e.target.value
                                        })
                                    }
                                    disabled={key === "patientId"}
                                />
                            </div>
                        ))}
                        <button type="submit" disabled={loading}>
                            {loading ? "Updating..." : "Update Profile"}
                        </button>
                    </form>
                </>
            )}
        </div>
    );
};

export default AddPatient;
