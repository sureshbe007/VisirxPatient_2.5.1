package com.visirx.patient.model;

/**
 * Created by Suresh on 09-03-2016.
 */
public class CareTeamModel {

    private String customerid;
    private String perfomerid;
    private String doctorFirstName;
    private String doctorLastName;
    private String doctorGender;
    private String doctorSpecialization;
    private String doctorDescription;
    private String lastUpdatedAt;
    private String createdAt;
    private int doctorFee;
    private int visiRxFee;
    private int isDeleted;

    public int getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(int isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getCustomerid() {
        return customerid;
    }

    public void setCustomerid(String customerid) {
        this.customerid = customerid;
    }

    public String getPerfomerid() {
        return perfomerid;
    }

    public void setPerfomerid(String perfomerid) {
        this.perfomerid = perfomerid;
    }

    public String getDoctorFirstName() {
        return doctorFirstName;
    }

    public void setDoctorFirstName(String doctorFirstName) {
        this.doctorFirstName = doctorFirstName;
    }

    public String getDoctorLastName() {
        return doctorLastName;
    }

    public void setDoctorLastName(String doctorLastName) {
        this.doctorLastName = doctorLastName;
    }

    public String getDoctorGender() {
        return doctorGender;
    }

    public void setDoctorGender(String doctorGender) {
        this.doctorGender = doctorGender;
    }

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

    public String getLastUpdatedAt() {
        return lastUpdatedAt;
    }

    public void setLastUpdatedAt(String lastUpdatedAt) {
        this.lastUpdatedAt = lastUpdatedAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public int getDoctorFee() {
        return doctorFee;
    }

    public void setDoctorFee(int doctorFee) {
        this.doctorFee = doctorFee;
    }

    public int getVisiRxFee() {
        return visiRxFee;
    }

    public void setVisiRxFee(int visiRxFee) {
        this.visiRxFee = visiRxFee;
    }
}
