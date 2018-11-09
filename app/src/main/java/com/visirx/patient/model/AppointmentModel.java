package com.visirx.patient.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by aa on 20.1.16.
 */
public class AppointmentModel implements Serializable {
    //rony Dashboard GC - starts - whole page------->

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    //master data - fixed data for patient.(chance to get updated is little)
    private String customerId;
    private String firstName;
    private String lastName;
    private String dobString;
    private String gender;
    private String mobileNumber;

    public String getSpecial() {
        return special;
    }

    public void setSpecial(String special) {
        this.special = special;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private String special;
    private String description;

    //no need to send.client should calculate using DOB
    private int age;
    //new variable
    private String baseDataLastUpdated;

    //Appointment data - data related to the specific appointment
    private int reservationNumber;
    private String address;
    private String date;
    private String time;
    private String status;
    private int DocFee;
    private String symptoms;


    //extra data for the app side alone.
    private String customerImageThumbnailPath;
    private String customerImagePath;

    // created by  suresh on 23-2-16
    private String Customer_specialization;
    private String Customer_description;
    private String Customer_address;
    private String Customer_zipcode;

    public String getCustomer_specialization() {
        return Customer_specialization;
    }

    public void setCustomer_specialization(String customer_specialization) {
        Customer_specialization = customer_specialization;
    }

    public String getCustomer_description() {
        return Customer_description;
    }

    public void setCustomer_description(String customer_description) {
        Customer_description = customer_description;
    }

    public String getCustomer_address() {
        return Customer_address;
    }

    public void setCustomer_address(String customer_address) {
        Customer_address = customer_address;
    }

    public String getCustomer_zipcode() {
        return Customer_zipcode;
    }

    public void setCustomer_zipcode(String customer_zipcode) {
        Customer_zipcode = customer_zipcode;
    }
    /*private byte[] photo;
    private String patientPhoto;*/

	/*private String notes;

	private List<AppointmentNoteModel> appointmentNotes;
	private String appointmentNotesLatest;*/
    //MY DEVE
//	private String prescriptionImage;
//
//	public String getPrescriptionImage() {
//		return prescriptionImage;
//	}
//
//	public void setPrescriptionImage(String prescriptionImage) {
//		this.prescriptionImage = prescriptionImage;
//	}
    //paramedic
	/*private boolean isNurseAssigned;
	private String nurseFirstName;
	private String nurseLastName;
	private int nurseAge;
	private String nurseGender;
	private String nurseMobileNumber;
	private String nurseId;
	private String nursePhoto;
	private byte[] nursePhotoByte;*/

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
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

    public String getBaseDataLastUpdated() {
        return baseDataLastUpdated;
    }

    public void setBaseDataLastUpdated(String baseDataLastUpdated) {
        this.baseDataLastUpdated = baseDataLastUpdated;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getDocFee() {
        return DocFee;
    }

    public void setDocFee(int docFee) {
        DocFee = docFee;
    }

    public String getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(String symptoms) {
        this.symptoms = symptoms;
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
}
