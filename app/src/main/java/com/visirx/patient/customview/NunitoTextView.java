package com.visirx.patient.customview;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by aa on 19.1.16.
 */
public class NunitoTextView extends TextView {

    public NunitoTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        if (!isInEditMode())
            init();
    }

    public NunitoTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode())
            init();
    }

    public NunitoTextView(Context context) {
        super(context);
        if (!isInEditMode())
            init();
    }

    private void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(),
                "Nunito-Regular.ttf");
        setTypeface(tf);
    }
}
