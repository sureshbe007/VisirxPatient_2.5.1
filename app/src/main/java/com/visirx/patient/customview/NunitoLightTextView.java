package com.visirx.patient.customview;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by aa on 20.1.16.
 */
public class NunitoLightTextView extends TextView {

    public NunitoLightTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        if (!isInEditMode())
            init();
    }

    public NunitoLightTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode())
            init();
    }

    public NunitoLightTextView(Context context) {
        super(context);
        if (!isInEditMode())
            init();
    }

    private void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(),
                "Nunito-Light.ttf");
        setTypeface(tf);
    }
}
