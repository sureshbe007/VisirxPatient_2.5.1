package com.visirx.patient.api;

import com.visirx.patient.model.AppointmentNoteModel;

import java.util.ArrayList;

/**
 * Created by aa on 20.1.16.
 */
public class AppointmentNotesReq {

    private RequestHeader requestHeader;
    private ArrayList<AppointmentNoteModel> noteModelList;

    public RequestHeader getRequestHeader() {
        return requestHeader;
    }

    public void setRequestHeader(RequestHeader requestHeader) {
        this.requestHeader = requestHeader;
    }

    public ArrayList<AppointmentNoteModel> getNoteModelList() {
        return noteModelList;
    }

    public void setNoteModelList(ArrayList<AppointmentNoteModel> noteModelList) {
        this.noteModelList = noteModelList;
    }
}
