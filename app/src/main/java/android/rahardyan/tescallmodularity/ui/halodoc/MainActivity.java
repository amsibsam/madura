package android.rahardyan.tescallmodularity.ui.halodoc;

import android.content.Intent;
import android.rahardyan.tescallmodularity.AgoraSampleReferences.model.ConstantApp;
import android.rahardyan.tescallmodularity.R;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.test_agora).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SampleCallActivity.class)
                        .putExtra(ConstantApp.ACTION_KEY_CHANNEL_NAME, "channelDap"));
            }
        });
    }

}
