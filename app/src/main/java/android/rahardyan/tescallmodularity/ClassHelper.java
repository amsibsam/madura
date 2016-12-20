package android.rahardyan.tescallmodularity;

import android.content.Context;

import static com.opentok.client.DeviceInfo.getApplicationContext;

/**
 * Created by rahardyan on 20/12/16.
 */

public class ClassHelper {
    private static WorkerThread mWorkerThread;

    public ClassHelper() {
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
}
