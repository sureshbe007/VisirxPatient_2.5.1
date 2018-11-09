package com.visirx.patient.customview;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Suresh on 22-02-2016.
 */
public class MediumFont extends TextView {

    public MediumFont(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        if (!isInEditMode())
            init();
    }

    public MediumFont(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode())
            init();
    }

    public MediumFont(Context context) {
        super(context);
        if (!isInEditMode())
            init();
    }

    private void init() {
//        setTypeface(Typeface.SANS_SERIF);
        setTypeface(Typeface.DEFAULT_BOLD);
    }
}
