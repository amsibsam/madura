package android.rahardyan.tescallmodularity.CallLibrary;

import android.os.Handler;
import android.rahardyan.tescallmodularity.event.CallEvent;
import android.util.Log;

/**
 * Created by rahardyan on 17/12/16.
 */

public class AgoraExample extends CallLibrary{
    CallEvent listener;

    public AgoraExample(CallEvent listener) {
        this.listener = listener;
    }

    @Override
    public void startCall() {
        Log.d("amsibsam", "start calling using agora");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                listener.onAcceptCall("agora");
            }
        }, 3000);
    }

    @Override
    public void endCall() {
        listener.onEndCall("agora");
    }
}
