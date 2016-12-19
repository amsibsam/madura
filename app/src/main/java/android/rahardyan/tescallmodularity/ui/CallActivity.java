package android.rahardyan.tescallmodularity.ui;

import android.content.Context;
import android.content.Intent;
import android.rahardyan.tescallmodularity.CallLibraryHelper;
import android.rahardyan.tescallmodularity.R;
import android.rahardyan.tescallmodularity.event.CallEvent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class CallActivity extends AppCompatActivity implements CallEvent {
    private static final String CALL_AS_KEY = "call_as";

    private CallLibraryHelper callLibraryHelper;
    private Button callButton, endCallButton;
    private CallLibraryHelper.CallAs callAs;

    public static Intent generateIntent(Context context, CallLibraryHelper.CallAs callAs){
        Intent intent = new Intent(context, CallActivity.class);
        intent.putExtra(CALL_AS_KEY, callAs.name());

        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);
        callLibraryHelper = new CallLibraryHelper(this);
        callAs = CallLibraryHelper.CallAs.valueOf(getIntent().getStringExtra(CALL_AS_KEY));

        targetView();
        setButtonResponse();
        initCallRole();
    }

    private void targetView() {
        callButton = (Button) findViewById(R.id.btn_call);
        endCallButton = (Button) findViewById(R.id.btn_end_call);
    }

    private void initCallRole() {
        if (callAs == CallLibraryHelper.CallAs.CALLER) {
            callLibraryHelper.startCall();
        }
    }

    private void setButtonResponse(){
        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callLibraryHelper.startCall();
            }
        });

        endCallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callLibraryHelper.endCall();
            }
        });
    }

    @Override
    public void onAcceptCall(String lib) {
        Toast.makeText(this, "call accepted with "+lib, Toast.LENGTH_SHORT).show();
        Log.d("amsibsam", "call accepted with "+lib);
    }

    @Override
    public void onEndCall(String lib) {
        Toast.makeText(this, "end call with "+lib, Toast.LENGTH_SHORT).show();
        Log.d("amsibsam", "end call with "+lib);
        finish();
    }

    @Override
    public void onConversationStart() {

    }

    @Override
    public void onConversationEnd() {

    }

    @Override
    public void onCallFailed() {

    }
}
