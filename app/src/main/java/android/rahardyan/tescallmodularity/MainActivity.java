package android.rahardyan.tescallmodularity;

import android.rahardyan.tescallmodularity.ui.CallActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import io.agora.rtc.IRtcEngineEventHandler;
import io.agora.rtc.RtcEngine;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final RtcEngine rtcEngine = RtcEngine.create(this, "12ee6202b79f4b17962c4d819d43bb60", new IRtcEngineEventHandler() {
            @Override
            public void onJoinChannelSuccess(String channel, int uid, int elapsed) {
                super.onJoinChannelSuccess(channel, uid, elapsed);
            }

            @Override
            public void onError(int err) {
                super.onError(err);
            }

            @Override
            public void onLeaveChannel(RtcStats stats) {
                super.onLeaveChannel(stats);
            }

            @Override
            public void onUserJoined(int uid, int elapsed) {
                super.onUserJoined(uid, elapsed);
            }

            @Override
            public void onRemoteVideoStats(RemoteVideoStats stats) {
                super.onRemoteVideoStats(stats);
            }

            @Override
            public void onLocalVideoStats(LocalVideoStats stats) {
                super.onLocalVideoStats(stats);
            }

            @Override
            public void onStreamMessage(int uid, int streamId, byte[] data) {
                super.onStreamMessage(uid, streamId, data);
            }
        });

        findViewById(R.id.btn_call).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(CallActivity.generateIntent(MainActivity.this, CallLibraryHelper.CallAs.CALLER));
            }
        });

        findViewById(R.id.test_agora).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rtcEngine.joinChannel(null, "axaxaxa", "tes call", 1);
            }
        });
    }
}
