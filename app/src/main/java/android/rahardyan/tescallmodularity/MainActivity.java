package android.rahardyan.tescallmodularity;

import android.rahardyan.tescallmodularity.ui.CallActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_call).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(CallActivity.generateIntent(MainActivity.this, CallLibraryHelper.CallAs.CALLER));
            }
        });
    }
}
