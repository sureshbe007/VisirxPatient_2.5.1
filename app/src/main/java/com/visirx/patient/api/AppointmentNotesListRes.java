package com.visirx.patient.api;

import com.visirx.patient.model.AppointmentNoteModel;

import java.util.List;

/**
 * Created by aa on 20.1.16.
 */
public class AppointmentNotesListRes {

    private ResponseHeader responseHeader;
    private List<AppointmentNoteModel> noteModelList;

    public ResponseHeader getResponseHeader() {
        return responseHeader;
    }

    public void setResponseHeader(ResponseHeader responseHeader) {
        this.responseHeader = responseHeader;
    }

    public List<AppointmentNoteModel> getNoteModelList() {
        return noteModelList;
    }

    public void setNoteModelList(List<AppointmentNoteModel> noteModelList) {
        this.noteModelList = noteModelList;
    }
}
