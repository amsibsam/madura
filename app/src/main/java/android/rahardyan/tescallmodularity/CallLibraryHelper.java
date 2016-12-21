package android.rahardyan.tescallmodularity;

import android.app.Activity;
import android.content.Context;
import android.rahardyan.tescallmodularity.AgoraSampleReferences.threadhelper.WorkerThread;
import android.rahardyan.tescallmodularity.CallLibrary.AgoraExample;
import android.rahardyan.tescallmodularity.event.CallEvent;
import android.widget.RelativeLayout;

/**
 * Created by rahardyan on 17/12/16.
 */

public class CallLibraryHelper{
    private static AgoraExample callLibrary;
    private static WorkerThread mWorkerThread;

    public CallLibraryHelper() {
    }

    public static void init(Context context) {
        callLibrary = new AgoraExample(context);
    }

    public static void setListener(CallEvent listener){
        callLibrary.setListener(listener);
    }

    public static void setRootLayout(Activity activity, RelativeLayout rootLayout, RelativeLayout smallVideo, String key, String mode){
        callLibrary.setRootContainer(activity, rootLayout, smallVideo, key, mode);
    }

    public static void startCall(String target){
        callLibrary.startCall(target);
    }

    public static void endCall(){
        callLibrary.endCall();
    }

    public static synchronized void initWorkerThread(Context context) {
        if (mWorkerThread == null) {
            mWorkerThread = new WorkerThread(context);
            mWorkerThread.start();

            mWorkerThread.waitForReady();
        }
    }

    public static synchronized WorkerThread getWorkerThread() {
        return mWorkerThread;
    }

    public static synchronized void doInitWorkerThread() {
        mWorkerThread.exit();
        try {
            mWorkerThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mWorkerThread = null;
    }

    public enum CallAs{
        CALLER,
        CALLEE
    }
}
