package com.visirx.patient.model;

import java.util.List;

/**
 * Created by Suresh on 22-02-2016.
 */
public class AppEmrFilesModel {

    private List<AddEmrFileModel> emrFileModel;
    private List<AddEmrVitalsModel> emrVitalModel;

    public List<AddEmrFileModel> getEmrFileModel() {
        return emrFileModel;
    }

    public void setEmrFileModel(List<AddEmrFileModel> emrFileModel) {
        this.emrFileModel = emrFileModel;
    }

    public List<AddEmrVitalsModel> getEmrVitalModel() {
        return emrVitalModel;
    }

    public void setEmrVitalModel(List<AddEmrVitalsModel> emrVitalModel) {
        this.emrVitalModel = emrVitalModel;
    }
}
