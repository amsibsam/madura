package android.rahardyan.tescallmodularity.CallLibrary;

import android.os.Handler;
import android.rahardyan.tescallmodularity.event.CallEvent;
import android.util.Log;

public class TokboxExample extends CallLibrary {
    CallEvent listener;

    public TokboxExample(CallEvent listener) {
        this.listener = listener;
    }

    @Override
    public void startCall() {
        Log.d("amsibsam", "start calling using tokbox");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                listener.onAcceptCall("tokBox");
            }
        }, 3000);
    }

    @Override
    public void endCall() {
        listener.onEndCall("tokBox");
    }
}
