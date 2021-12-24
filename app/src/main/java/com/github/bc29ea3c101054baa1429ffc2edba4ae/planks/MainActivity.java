package com.github.bc29ea3c101054baa1429ffc2edba4ae.planks;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    // declarations
    Timer timer;
    Boolean cmdReset = false;
    Long startTime, endTime;
    RecordDao recordDao;

    // new timer task
    // the task modifies textView's text
    public TimerTask myTask(TextView textView, Long startTime){
        return new TimerTask() {
            @Override
            public void run() {
                Long timeDiff = (System.currentTimeMillis() - startTime);

                runOnUiThread(() -> {
                    // 60 min 60 sec 100 ms

                    if (!cmdReset) {
                        Long millis = (timeDiff / 10) % 100;
                        Long sec = (timeDiff / 1000) % 60;
                        Long min = (timeDiff / 1000) / 60;

                        String text = formatText(min) + ":" + formatText(sec) + "." + formatText(millis);
                        textView.setText(text);
                    } else {
                        textView.setText(R.string.counterDefault);
                    }
                });
            }
        };
    }

    public String formatText(Long time){
        String formattedText;
        if (time < 10){
            formattedText = "0" + time.toString();
        } else {
            formattedText = time.toString();
        }
        return formattedText;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // INIT
        TextView textView = findViewById(R.id.textView);
        Button btnTimer = findViewById(R.id.btnTimer);
        Button btnReset = findViewById(R.id.btnReset);
        Button btnStats = findViewById(R.id.btnStats);

        // DEFAULTS
        btnTimer.setTag(1);

        // DB
        Thread threadBuild = new Thread(() -> {
            AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                    AppDatabase.class, "Record").build();
            recordDao = db.recordDao();
        });
        threadBuild.start();


        // btn STATS
        btnStats.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, StatsActivity.class);
            startActivity(intent);

        });

        // btn RESET
        btnReset.setOnClickListener(v -> {
            cmdReset = true;
            if ( (Integer) btnTimer.getTag() == 0){
                timer.cancel();
                timer.purge();
            }

            btnTimer.setTag(1);
            btnTimer.setText(R.string.button_start);
            textView.setText(R.string.counterDefault);
        });

        // BTN onCLick
        btnTimer.setOnClickListener(v -> {
            int status = (Integer) v.getTag();

            if (status == 1){       // START
                cmdReset = false;
                timer = new Timer();
                // btn stuff
                btnTimer.setTag(0);
                btnTimer.setText(R.string.button_stop);
                // timer stuff
                // write down start time and start timer
                startTime = System.currentTimeMillis();
                timer.schedule(myTask(textView, startTime), 0, 15);
                //;
            } else if(status == 0){                // STOP
                // set btnTimer stuff to default
                btnTimer.setTag(1);
                btnTimer.setText(R.string.button_start);

                // timer stuff
                // cancel and write down end time
                timer.cancel();
                endTime = System.currentTimeMillis();

                // DB stuff
                // write diff of start and end time to database;

                Long score = endTime - startTime;
                Long fDate = startTime;


                Thread threadInsert = new Thread(() -> {
                    Record record = new Record();
                    record.setDate(fDate);
                    record.setScore(score);
                    recordDao.insert(record);
                });
                threadInsert.start();



                // NOTIFICATION
                Toast.makeText(MainActivity.this,"Stats updated...", Toast.LENGTH_SHORT).show();
            }
        });
    }
}