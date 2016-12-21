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
    public void endCall() {
        listener.onEndCall("tokBox");
    }
}
