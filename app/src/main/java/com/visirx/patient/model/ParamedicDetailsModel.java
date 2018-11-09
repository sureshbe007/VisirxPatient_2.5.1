package com.visirx.patient.model;

/**
 * Created by Suresh on 22-02-2016.
 */
public class ParamedicDetailsModel {

    private int appointmentId;
    private boolean isNurseAssigned;
    private String nurseFirstName;
    private String nurseLastName;
    private int nurseAge;
    private String nurseGender;
    private String nurseMobileNumber;
    private String nurseId;
    // rony - PrescriptionGC - Starts
    //private byte[] nursePhotoByte;

    private String nurseDob;
    private String lastUpdatedTimestamp;
    private boolean isNurseUpdated;


    private String nurseDataLastUpdated;
    private String nurseThumbImgPath;

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public boolean isNurseAssigned() {
        return isNurseAssigned;
    }

    public void setIsNurseAssigned(boolean isNurseAssigned) {
        this.isNurseAssigned = isNurseAssigned;
    }

    public String getNurseFirstName() {
        return nurseFirstName;
    }

    public void setNurseFirstName(String nurseFirstName) {
        this.nurseFirstName = nurseFirstName;
    }

    public String getNurseLastName() {
        return nurseLastName;
    }

    public void setNurseLastName(String nurseLastName) {
        this.nurseLastName = nurseLastName;
    }

    public int getNurseAge() {
        return nurseAge;
    }

    public void setNurseAge(int nurseAge) {
        this.nurseAge = nurseAge;
    }

    public String getNurseGender() {
        return nurseGender;
    }

    public void setNurseGender(String nurseGender) {
        this.nurseGender = nurseGender;
    }

    public String getNurseMobileNumber() {
        return nurseMobileNumber;
    }

    public void setNurseMobileNumber(String nurseMobileNumber) {
        this.nurseMobileNumber = nurseMobileNumber;
    }

    public String getNurseId() {
        return nurseId;
    }

    public void setNurseId(String nurseId) {
        this.nurseId = nurseId;
    }

    public String getNurseDob() {
        return nurseDob;
    }

    public void setNurseDob(String nurseDob) {
        this.nurseDob = nurseDob;
    }

    public String getLastUpdatedTimestamp() {
        return lastUpdatedTimestamp;
    }

    public void setLastUpdatedTimestamp(String lastUpdatedTimestamp) {
        this.lastUpdatedTimestamp = lastUpdatedTimestamp;
    }

    public boolean isNurseUpdated() {
        return isNurseUpdated;
    }

    public void setIsNurseUpdated(boolean isNurseUpdated) {
        this.isNurseUpdated = isNurseUpdated;
    }

    public String getNurseDataLastUpdated() {
        return nurseDataLastUpdated;
    }

    public void setNurseDataLastUpdated(String nurseDataLastUpdated) {
        this.nurseDataLastUpdated = nurseDataLastUpdated;
    }

    public String getNurseThumbImgPath() {
        return nurseThumbImgPath;
    }

    public void setNurseThumbImgPath(String nurseThumbImgPath) {
        this.nurseThumbImgPath = nurseThumbImgPath;
    }
}
