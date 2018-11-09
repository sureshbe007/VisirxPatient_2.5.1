package com.visirx.patient.visirxav.view;
import android.content.Context;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import com.quickblox.videochat.webrtc.QBRTCClient;
import org.webrtc.EglBase;
import org.webrtc.SurfaceViewRenderer;
public class RTCSurfaceView extends SurfaceViewRenderer {

    private boolean inited;

    public RTCSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RTCSurfaceView(Context context) {
        super(context);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        super.surfaceCreated(holder);
        init();
    }

    protected void init(){
        if (inited) {
            return;
        }
        EglBase eglContext = QBRTCClient.getInstance(getContext()).getEglContext();
        if (eglContext == null) {
            return;
        }
        init(eglContext.getEglBaseContext(), null);
        inited = true;
    }
}
