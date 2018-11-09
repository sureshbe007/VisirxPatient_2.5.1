package com.visirx.patient.utils;

import android.content.Context;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by suresh on 2/2/2016.
 */
public class Validation {

    private Context context;
    TextView txtEmail;

    private boolean isValidMailS(String email2) {
        boolean check;
        Pattern p;
        Matcher m;
        txtEmail = new TextView(context);
        String EMAIL_STRING = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        p = Pattern.compile(EMAIL_STRING);
        m = p.matcher(email2);
        check = m.matches();
        if (!check) {
            txtEmail.setError("Not Valid Email");
        }
        return check;
    }


}
