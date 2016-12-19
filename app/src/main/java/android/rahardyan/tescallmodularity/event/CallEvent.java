package android.rahardyan.tescallmodularity.event;

/**
 * Created by rahardyan on 17/12/16.
 */

public interface CallEvent {
    void onAcceptCall(String lib);
    void onEndCall(String lib);
    void onConversationStart();
    void onConversationEnd();
    void onCallFailed();
}
