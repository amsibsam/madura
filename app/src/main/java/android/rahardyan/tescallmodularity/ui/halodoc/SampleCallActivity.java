package android.rahardyan.tescallmodularity.ui.halodoc;

import android.rahardyan.tescallmodularity.Madura;
import android.rahardyan.tescallmodularity.AgoraSampleReferences.model.ConstantApp;
import android.rahardyan.tescallmodularity.R;
import android.rahardyan.tescallmodularity.event.CallEvent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

public class SampleCallActivity extends AppCompatActivity implements CallEvent {
    private RelativeLayout rootContainer, smallVideoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_call);

        String target = getIntent().getStringExtra(ConstantApp.ACTION_KEY_CHANNEL_NAME);
        final String encryptionKey = getIntent().getStringExtra(ConstantApp.ACTION_KEY_ENCRYPTION_KEY);
        final String encryptionMode = getIntent().getStringExtra(ConstantApp.ACTION_KEY_ENCRYPTION_MODE);

        rootContainer = (RelativeLayout) findViewById(R.id.root_container);
        smallVideoView = (RelativeLayout) findViewById(R.id.small_video_container);
        Madura.setListener(this);
        Madura.setRootLayout(this, rootContainer, smallVideoView, encryptionKey, encryptionMode);
        Madura.startCall(target);

        findViewById(R.id.bottom_action_end_call).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Madura.endCall();
            }
        });

        findViewById(R.id.btn_mute).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Madura.muteAudio();
            }
        });

        findViewById(R.id.customized_function_id).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Madura.swithcCamera();
            }
        });
    }

    @Override
    public void onAcceptCall(String lib) {

    }

    @Override
    public void onEndCall(String lib) {
        finish();
    }

    @Override
    public void onConversationStart() {
//        TODO: start timer
    }

    @Override
    public void onConversationEnd() {
        finish();
//        TODO: stop timer (callback not implemented yet)
    }

    @Override
    public void onCallFailed() {
//        TODO: notify user (callback not implemented yet)
    }
}
