package app.random.generator.from_scratch_v_03;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;

import com.random.generator.from_scratch_v_03.R;

public class Welcome extends AppCompatActivity {

    int progress = 0;
    ProgressBar horizontalProgressBar;
    Handler h = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        horizontalProgressBar = (ProgressBar)findViewById(R.id.progressBarHome);

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i=0; i<5; i++){
                    progress+=50;
                    h.post(new Runnable() {
                        @Override
                        public void run() {
                            horizontalProgressBar.setProgress(progress);
                            if (progress == horizontalProgressBar.getMax()){
                                //horizontalProgressBar.setVisibility(View.VISIBLE);
                                //Intent intent = new Intent(getApplicationContext(), Menu_Drawer.class);
                                //startActivity(intent);
                                startActivity(new Intent(Welcome.this, Splash_Menu.class));
                            }
                        }
                    });
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e){

                    }
                }
            }
        }).start();
    }
}
