package com.visirx.patient.model;

/**
 * Created by Suresh on 03-03-2016.
 */
public class AppointmentPatientModel {
    // For list all Appointmnet in patient app by suresh

    private static final long serialVersionUID = 1L;
    private String perfomerid;
    private String firstName;
    private String lastName;
    private String dobString;
    private String gender;
    private String mobileNumber;
    private int age;
    private String date;
    private String time;
    private String symptoms;
    private int reservationNumber;
    private String address;
    private String status;
    private int doctorfee;
    private int visirxfee;
    private String Comments;
    private String appointmentType;
    private String customerZipcode;
    private String customerImageThumbnailPath;
    private String customerImagePath;
    private String baseDataLastUpdated;

    public String getDoctorSpecialization() {
        return doctorSpecialization;
    }

    public void setDoctorSpecialization(String doctorSpecialization) {
        this.doctorSpecialization = doctorSpecialization;
    }

    public String getDoctorDescription() {
        return doctorDescription;
    }

    public void setDoctorDescription(String doctorDescription) {
        this.doctorDescription = doctorDescription;
    }

    private String doctorSpecialization;
    private String doctorDescription;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDobString() {
        return dobString;
    }

    public void setDobString(String dobString) {
        this.dobString = dobString;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getCustomerZipcode() {
        return customerZipcode;
    }

    public void setCustomerZipcode(String customerZipcode) {
        this.customerZipcode = customerZipcode;
    }

    public String getCustomerImageThumbnailPath() {
        return customerImageThumbnailPath;
    }

    public void setCustomerImageThumbnailPath(String customerImageThumbnailPath) {
        this.customerImageThumbnailPath = customerImageThumbnailPath;
    }

    public String getCustomerImagePath() {
        return customerImagePath;
    }

    public void setCustomerImagePath(String customerImagePath) {
        this.customerImagePath = customerImagePath;
    }

    public String getBaseDataLastUpdated() {
        return baseDataLastUpdated;
    }

    public void setBaseDataLastUpdated(String baseDataLastUpdated) {
        this.baseDataLastUpdated = baseDataLastUpdated;
    }


    public String getAppointmentType() {
        return appointmentType;
    }

    public void setAppointmentType(String appointmentType) {
        this.appointmentType = appointmentType;
    }

    public String getPerfomerid() {
        return perfomerid;
    }

    public void setPerfomerid(String perfomerid) {
        this.perfomerid = perfomerid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(String symptoms) {
        this.symptoms = symptoms;
    }

    public int getReservationNumber() {
        return reservationNumber;
    }

    public void setReservationNumber(int reservationNumber) {
        this.reservationNumber = reservationNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getDoctorfee() {
        return doctorfee;
    }

    public void setDoctorfee(int doctorfee) {
        this.doctorfee = doctorfee;
    }

    public int getVisirxfee() {
        return visirxfee;
    }

    public void setVisirxfee(int visirxfee) {
        this.visirxfee = visirxfee;
    }

    public String getComments() {
        return Comments;
    }

    public void setComments(String comments) {
        Comments = comments;
    }
}
