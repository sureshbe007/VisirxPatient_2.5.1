package com.visirx.patient.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Suresh on 22-02-2016.
 */
public class NormalFont extends TextView {

    public NormalFont(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        if (!isInEditMode())
            init();
    }

    public NormalFont(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode())
            init();
    }

    public NormalFont(Context context) {
        super(context);
        if (!isInEditMode())
            init();
    }

    private void init() {
//        Typeface typeface = Typeface.createFromAsset(getContext().getAssets(),"Roboto-Regular.ttf");
//        setTypeface(Typeface.SERIF);
    }

}
