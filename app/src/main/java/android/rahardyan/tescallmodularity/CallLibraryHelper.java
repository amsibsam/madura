package android.rahardyan.tescallmodularity;

import android.app.Activity;
import android.content.Context;
import android.rahardyan.tescallmodularity.CallLibrary.AgoraExample;
import android.rahardyan.tescallmodularity.CallLibrary.CallLibrary;
import android.rahardyan.tescallmodularity.CallLibrary.TokboxExample;
import android.rahardyan.tescallmodularity.event.CallEvent;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

/**
 * Created by rahardyan on 17/12/16.
 */

public class CallLibraryHelper{
    private static AgoraExample callLibrary;

    public CallLibraryHelper() {
    }

    public static void init(Context context) {
        callLibrary = new AgoraExample(context);
    }

    public static void setListener(CallEvent listener){
        callLibrary.setListener(listener);
    }

    public static void setRootLayout(Activity activity, RelativeLayout rootLayout, String key, String mode){
        callLibrary.setRootContainer(activity, rootLayout, key, mode);
    }

    public static void startCall(String target){
        callLibrary.startCall(target);
    }

    public static void endCall(){
        callLibrary.endCall();
    }

    public enum CallAs{
        CALLER,
        CALLEE
    }
}
