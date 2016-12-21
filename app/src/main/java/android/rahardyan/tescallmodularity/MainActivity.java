package android.rahardyan.tescallmodularity;

import android.content.Intent;
import android.rahardyan.tescallmodularity.ui.AgoraCallActivity;
import android.rahardyan.tescallmodularity.ui.BaseActivity;
import android.rahardyan.tescallmodularity.ui.SampleCallActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_call).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AgoraCallActivity.class)
                        .putExtra(ConstantApp.ACTION_KEY_CHANNEL_NAME, "channelDap"));
            }
        });

        findViewById(R.id.test_agora).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SampleCallActivity.class)
                        .putExtra(ConstantApp.ACTION_KEY_CHANNEL_NAME, "channelDap"));
            }
        });
    }


    @Override
    protected void initUIandEvent() {

    }

    @Override
    protected void deInitUIandEvent() {

    }
}
