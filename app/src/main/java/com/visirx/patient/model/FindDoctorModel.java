package com.visirx.patient.model;

import android.graphics.Bitmap;

/**
 * Created by Suresh on 17-02-2016.
 */
public class FindDoctorModel
{

    private String doctorId;
    private String doctorFirstName;
    private String doctorLastName;
    private String doctorGender;
    private String doctorSpecialization;
    private String doctorDescription;
    private int doctorfee;
    private int visirxfee;
    private Bitmap bitMap;

    public String getCustomerImageThumbnailPath() {
        return customerImageThumbnailPath;
    }

    public void setCustomerImageThumbnailPath(String customerImageThumbnailPath) {
        this.customerImageThumbnailPath = customerImageThumbnailPath;
    }

    private String customerImageThumbnailPath;

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public Bitmap getBitMap() {
        return bitMap;
    }

    public void setBitMap(Bitmap bitMap) {
        this.bitMap = bitMap;
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
}
