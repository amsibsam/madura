package android.rahardyan.tescallmodularity.ui.halodoc;

import android.rahardyan.tescallmodularity.CallLibraryHelper;
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
        CallLibraryHelper.setListener(this);
        CallLibraryHelper.setRootLayout(this, rootContainer, smallVideoView, encryptionKey, encryptionMode);
        CallLibraryHelper.startCall(target);

        findViewById(R.id.bottom_action_end_call).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CallLibraryHelper.endCall();
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

    }

    @Override
    public void onConversationEnd() {

    }

    @Override
    public void onCallFailed() {

    }
}
