package com.example.rps;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
    /*
        This class represent the home page of the application.
        It contains buttons and music.
     */
    EditText playerName;
    Button btnStart, btnSound, info_dialog, btn_settings, no_button, yes_button, exit_button;
    MediaPlayer backsound;
    AudioManager am;
    Dialog d_settings, d_info;
    boolean isMute = true;
    boolean is_exposed = false;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playerName = (EditText) findViewById(R.id.playername);

        btnStart = (Button) findViewById(R.id.btnstart);
        btnStart.setOnClickListener(this);

        btnSound = (Button) findViewById(R.id.btnmute);
        btnSound.setOnClickListener(this);

        info_dialog = (Button) findViewById(R.id.info_dialog);
        info_dialog.setOnClickListener(this);

        btn_settings = (Button)findViewById(R.id.btn_settings);
        btn_settings.setOnClickListener(this);

        am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int max = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        am.setStreamVolume(AudioManager.STREAM_MUSIC, max / 2, 0);

        backsound = MediaPlayer.create(this, R.raw.start);
        backsound.setLooping(true);
        backsound.start();



    }

    @Override
    public void onClick(View v)
    {
        /*
            This function checks which button was pressed and operate accordingly.
            This function receives the view that pressed.
         */
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
                //intent.putExtra("userName", playerName.getText().toString());
                if (is_exposed)
                    intent.putExtra("is_exposed","1");
                else
                    intent.putExtra("is_exposed","0");
                startActivity(intent);
            }
            else
            {
                Toast.makeText(this, "You have to enter your name first ", Toast.LENGTH_LONG).show();
            }
        }
        else if (v == info_dialog)
        {
            create_info_dialog();
        }
    }

    private void create_info_dialog()
    {
        /*
            This function creates the info dialog.
         */
        d_info = new Dialog(this);
        d_info.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d_info.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        d_info.setContentView(R.layout.dialog_info);
        exit_button = (Button) d_info.findViewById(R.id.exit_button);
        exit_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                d_info.cancel();
            }
        });
        d_info.setCancelable(true);
        d_info.show();
    }

    private void create_settings_dialog()
        /*
            This function creates the settings dialog.
         */
    {
        d_settings = new Dialog(this);
        d_settings.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d_settings.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        d_settings.setContentView(R.layout.dialog_gamesettings);
        no_button = (Button) d_settings.findViewById(R.id.no_button);
        no_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                is_exposed = false;
                d_settings.cancel();
            }
        });
        yes_button = (Button) d_settings.findViewById(R.id.yes_button);
        yes_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                is_exposed = true;
                d_settings.cancel();
            }
        });
        d_settings.setCancelable(true);
        d_settings.show();
    }
}
