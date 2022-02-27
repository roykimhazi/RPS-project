package com.example.rps;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView tv_column, tv_row;
    EditText playerName;
    Button btnStart, btnSound, leaderboard, btn_settings, btn_check;
    MediaPlayer backsound, victory;
    SeekBar sb_column, sb_row;
    AudioManager am;
    Dialog d_settings;
    int intent_column, intent_row;
    boolean isMute = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playerName = (EditText) findViewById(R.id.playername);

        btnStart = (Button) findViewById(R.id.btnstart);
        btnStart.setOnClickListener(this);

        btnSound = (Button) findViewById(R.id.btnmute);
        btnSound.setOnClickListener(this);

        leaderboard = (Button) findViewById(R.id.leaderboard);
        leaderboard.setOnClickListener(this);

        btn_settings = (Button)findViewById(R.id.btn_settings);
        btn_settings.setOnClickListener(this);

        am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int max = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        am.setStreamVolume(AudioManager.STREAM_MUSIC, max / 2, 0);

        backsound = MediaPlayer.create(this, R.raw.start);
        backsound.setLooping(true);
        backsound.start();

        victory = MediaPlayer.create(this, R.raw.leaderboardsound);


    }

    @Override
    public void onClick(View v)
    {
        if (v == btn_settings)
        {
            create_settings_dialog();
        }
        if (v == btnSound)
        {
            if (isMute)
            {
                backsound.setVolume(0, 0);
                btnSound.setBackgroundResource(R.drawable.mute);
                isMute = false;
            }
            else
            {
                backsound.setVolume(1, 1);
                btnSound.setBackgroundResource(R.drawable.btn_sound);
                isMute = true;
            }
        }
        else if (v == btnStart)
        {
            if (playerName.getText().toString().length() != 0)
            {
                backsound.setVolume(0, 0);
                Intent intent = new Intent(this, GameActivity.class);
                intent.putExtra("userName", playerName.getText().toString());
                if(tv_column == null )
                {
                    intent.putExtra("column", "5");
                    intent.putExtra("row", "5");
                }
                else
                {
                    intent.putExtra("column", tv_column.getText().toString());
                    intent.putExtra("row", tv_row.getText().toString());
                }
                startActivity(intent);
            }
            else
            {
                Toast.makeText(this, "You have to enter your name first ", Toast.LENGTH_LONG).show();
            }
        }
        else if (v == leaderboard)
        {
            backsound.setVolume(0, 0);
            victory.setVolume(1, 1);
            victory.start();
            Intent intent = new Intent(this, LeaderboardActivity.class);
            startActivity(intent);
        }
    }



    private void create_settings_dialog()
        /*
         *This function create the settings dialog
         */
    {
        d_settings = new Dialog(this);
        d_settings.setContentView(R.layout.dialog_gamesettings);
        btn_check = (Button) d_settings.findViewById(R.id.dialogreturn);
        btn_check.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                d_settings.cancel();
            }
        });
        tv_column = (TextView)d_settings.findViewById(R.id.tv_column);
        tv_row = (TextView)d_settings.findViewById(R.id.tv_row);
        sb_column = (SeekBar) d_settings.findViewById(R.id.sb_column);
        sb_column.setBackgroundColor(getColor(R.color.black));
        sb_column.setProgress(5);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            sb_column.setMin(3);
        }
        sb_column.setMax(15);

        sb_row = (SeekBar) d_settings.findViewById(R.id.sb_row);
        sb_row.setBackgroundColor(getColor(R.color.black));
        sb_row.setProgress(5);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            sb_row.setMin(3);
        }
        sb_row.setMax(15);
        tv_column.setText(String.valueOf(sb_column.getProgress()));
        tv_row.setText(String.valueOf(sb_row.getProgress()));
        sb_column.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b)
            {
                tv_column.setText(String.valueOf(i));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {

            }
        });
        sb_row.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b)
            {
                tv_row.setText(String.valueOf(i));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {

            }
        });
        d_settings.setCancelable(false);
        d_settings.show();
    }
}
