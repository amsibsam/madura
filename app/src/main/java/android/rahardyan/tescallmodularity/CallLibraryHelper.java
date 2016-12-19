package android.rahardyan.tescallmodularity;

import android.rahardyan.tescallmodularity.CallLibrary.AgoraExample;
import android.rahardyan.tescallmodularity.CallLibrary.CallLibrary;
import android.rahardyan.tescallmodularity.CallLibrary.TokboxExample;
import android.rahardyan.tescallmodularity.event.CallEvent;

/**
 * Created by rahardyan on 17/12/16.
 */

public class CallLibraryHelper{
    private CallLibrary callLibrary;

    public CallLibraryHelper(CallEvent callEvent) {
        callLibrary = new TokboxExample(callEvent);
    }

    public void startCall(){
        callLibrary.startCall();
    }

    public void endCall(){
        callLibrary.endCall();
    }

    public enum CallAs{
        CALLER,
        CALLEE
    }
}
