package com.visirx.patient.common.provider;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.visirx.patient.R;
import com.visirx.patient.activity.DashBoardActivity;
import com.visirx.patient.api.ConfirmAppointmentReq;
import com.visirx.patient.api.ConfirmAppointmentRes;
import com.visirx.patient.api.GetStaticTextsFromDbAppReq;
import com.visirx.patient.api.GetStaticTextsFromDbAppRes;
import com.visirx.patient.api.RequestHeader;
import com.visirx.patient.customview.NormalFont;
import com.visirx.patient.model.ConfirmAppointmentModel;
import com.visirx.patient.utils.HTTPUtils;
import com.visirx.patient.utils.Popup;
import com.visirx.patient.utils.VTConstants;

/**
 * Created by Suresh on 28-03-2016.
 */
public class GetStaticTextsFromDbAppServlet {

    Context context;
    SharedPreferences sharedPreferences;
    String requestAction;

    public GetStaticTextsFromDbAppServlet(Context context) {
        super();
        this.context = context;

    }

    public void GetStaticTextsFromDbAppReq(String action) {

        requestAction = action;

        GetStaticTextsFromDbAppReq getStaticTextsFromDbAppReq = new GetStaticTextsFromDbAppReq();

        RequestHeader requestMessageHeader = new RequestHeader();
        requestMessageHeader.setUserId("00000");

        getStaticTextsFromDbAppReq.setAction(action);

        getStaticTextsFromDbAppReq.setRequestHeader(requestMessageHeader);

        new GetStaticTextsFromDbAppTaskAsyn(context).execute(getStaticTextsFromDbAppReq);

    }

    private class GetStaticTextsFromDbAppTaskAsyn extends AsyncTask<GetStaticTextsFromDbAppReq, Integer, GetStaticTextsFromDbAppRes> {
        Context context;

        public GetStaticTextsFromDbAppTaskAsyn(Context context) {
            this.context = context;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected GetStaticTextsFromDbAppRes doInBackground(GetStaticTextsFromDbAppReq... params) {
            GetStaticTextsFromDbAppRes result = (GetStaticTextsFromDbAppRes) HTTPUtils.getDataFromServer(params[0], GetStaticTextsFromDbAppReq.class,
                    GetStaticTextsFromDbAppRes.class, "GetStaticTextsFromDbAppServlet");
            return result;
        }

        @Override
        protected void onPostExecute(GetStaticTextsFromDbAppRes result) {
            super.onPostExecute(result);
            try {
                if (result == null) {
                    Popup.ShowErrorMessage(context, R.string.error_server_connect, Toast.LENGTH_SHORT);
                    return;
                } else if (result.getResponseHeader().getResponseCode() == -1) {
                    Popup.ShowErrorMessageString(context, result.getResponseHeader().getResponseMessage(), Toast.LENGTH_LONG);
                    return;
                } else {
                    sharedPreferences = context.getSharedPreferences(VTConstants.LOGIN_PREFRENCES_NAME, 0);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(requestAction, result.getResponseString());


                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }


    }

}
