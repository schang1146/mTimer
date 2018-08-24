package schang1146.mtimer;

import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ToggleButton;

public class MainActivity extends AppCompatActivity {
    long counterStart;
    long resetStart = 0;
    EditText textCounterSS;
    EditText textCounterMM;
    EditText textCounterHH;
    ToggleButton toggleStart;
    ToggleButton toggleLoop;
    private boolean isPaused;
    private boolean isLooped;
    String oldText;
    Uri alarm;
    Ringtone r;

    private void ringAlarm () {
        alarm = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alarm == null) {
            alarm = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            if (alarm == null) {
                alarm = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            }
        }
        r = RingtoneManager.getRingtone(getApplicationContext(), alarm);
        r.play();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                r.stop();
                toggleLoop.setChecked(true);
            }
        }, 3000);
    }

    private TextWatcher CounterWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if (textCounterHH.getText().hashCode() == charSequence.hashCode()) {
                oldText = textCounterHH.getText().toString();
            } else if (textCounterMM.getText().hashCode() == charSequence.hashCode()) {
                oldText = textCounterMM.getText().toString();
            } else if (textCounterSS.getText().hashCode() == charSequence.hashCode()) {
                oldText = textCounterSS.getText().toString();
            }
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if (textCounterHH.getText().toString().charAt(0) == '0' && textCounterHH.getText()
                    .hashCode() == charSequence.hashCode() && textCounterHH.getText().length() >
                    2) {
                textCounterHH.removeTextChangedListener(this);
                textCounterHH.setText(textCounterHH.getText().toString().substring(1));
                textCounterHH.setSelection(textCounterHH.getText().length());
                textCounterHH.addTextChangedListener(this);
            } else if (textCounterMM.getText().toString().charAt(0) == '0' && textCounterMM
                    .getText().hashCode() == charSequence.hashCode() && textCounterMM.getText()
                    .length() >
                    2) {
                textCounterMM.removeTextChangedListener(this);
                textCounterMM.setText(textCounterMM.getText().toString().substring(1));
                textCounterMM.setSelection(textCounterMM.getText().length());
                textCounterMM.addTextChangedListener(this);
            } else if (textCounterSS.getText().toString().charAt(0) == '0' && textCounterSS
                    .getText().hashCode() == charSequence.hashCode() && textCounterSS.getText()
                    .length() >
                    2) {
                textCounterSS.removeTextChangedListener(this);
                textCounterSS.setText(textCounterSS.getText().toString().substring(1));
                textCounterSS.setSelection(textCounterSS.getText().length());
                textCounterSS.addTextChangedListener(this);
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (textCounterHH.getText().toString().length() > 2 && textCounterHH.getText()
                    .hashCode() == editable.hashCode()) {
                textCounterHH.removeTextChangedListener(this);
                textCounterHH.setText(oldText);
                textCounterHH.setSelection(textCounterHH.getText().length());
                textCounterHH.addTextChangedListener(this);
            } else if (textCounterMM.getText().toString().length() > 2 && textCounterMM.getText()
                    .hashCode() == editable.hashCode()) {
                textCounterMM.removeTextChangedListener(this);
                textCounterMM.setText(oldText);
                textCounterMM.setSelection(textCounterMM.getText().length());
                textCounterMM.addTextChangedListener(this);
            } else if (textCounterSS.getText().toString().length() > 2 && textCounterSS.getText()
                    .hashCode() == editable.hashCode()) {
                textCounterSS.removeTextChangedListener(this);
                textCounterSS.setText(oldText);
                textCounterSS.setSelection(textCounterSS.getText().length());
                textCounterSS.addTextChangedListener(this);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button buttonReset = findViewById(R.id.buttonReset);
        textCounterHH = findViewById(R.id.textCounterHH);
        textCounterMM = findViewById(R.id.textCounterMM);
        textCounterSS = findViewById(R.id.textCounterSS);
        toggleStart = findViewById(R.id.toggleStart);
        toggleLoop = findViewById(R.id.toggleLoop);

        toggleStart.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                counterStart = Long.parseLong(textCounterSS.getText().toString()) * 1000 + Long
                        .parseLong(textCounterMM.getText().toString()) * 60000 + Long.parseLong
                        (textCounterHH.getText().toString()) * 3600000;

                if (resetStart == 0) {
                    resetStart = counterStart/1000;
                }

                if (b) {
                    isPaused = false;
                } else {
                    isPaused = true;
                }
                CountDownTimer CurrentTimer = new CountDownTimer(counterStart, 50) {
                    public void onTick(long millisuntilFinished) {
                        if (isPaused) {
                            cancel();
                            textCounterHH.setEnabled(true);
                            textCounterMM.setEnabled(true);
                            textCounterSS.setEnabled(true);
                        } else {
                            textCounterHH.setEnabled(false);
                            textCounterMM.setEnabled(false);
                            textCounterSS.setEnabled(false);
                            int timeLeft = (int) (millisuntilFinished / 1000);
                            textCounterHH.setText(String.valueOf(Math.round(Math.floor(timeLeft /
                                    3600))));
//                            textCounterHH.setText(String.format("%02d", Math.round(Math.floor
//                                    (timeLeft / 3600))));
                            textCounterMM.setText(String.valueOf(Math.round(Math.floor((timeLeft
                                    % 3600) / 60))));
//                            textCounterMM.setText(String.format("%02d", Math.round(Math.floor
//                                    ((timeLeft % 3600) / 60))));
                            textCounterSS.setText(String.valueOf(Math.round(timeLeft % 60)));
//                            textCounterSS.setText(String.format("%02d", Math.round(timeLeft % 60)));
                        }
                    }

                    public void onFinish() {
                        ringAlarm();
                        if (isLooped) {
                            start();
                        } else {
                            cancel();
                            textCounterHH.setEnabled(true);
                            textCounterMM.setEnabled(true);
                            textCounterSS.setEnabled(true);
                            toggleStart.setChecked(false);
                        }
                    }
                };
                CurrentTimer.start();
            }
        });

        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleStart.setChecked(false);
                textCounterHH.setText(String.valueOf(Math.round(Math.floor(resetStart /
                        3600))));
                textCounterMM.setText(String.valueOf(Math.round(Math.floor((resetStart
                        % 3600) / 60))));
                textCounterSS.setText(String.valueOf(Math.round(resetStart % 60)));
            }
        });

        toggleLoop.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    isLooped = true;
                    ringAlarm();
                } else {
                    isLooped = false;
                }
            }
        });

        textCounterHH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View viewHH) {
                resetStart = 0;
            }
        });

        textCounterMM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View viewMM) {
                resetStart = 0;
            }
        });

        textCounterSS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View viewSS) {
                resetStart = 0;
            }
        });

        textCounterHH.addTextChangedListener(CounterWatcher);
        textCounterMM.addTextChangedListener(CounterWatcher);
        textCounterSS.addTextChangedListener(CounterWatcher);
    }
}
