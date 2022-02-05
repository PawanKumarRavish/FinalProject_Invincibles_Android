package com.project.taskmanager.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.ContextWrapper;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.project.taskmanager.R;

import java.io.File;

public class RecordAudioActivity extends AppCompatActivity {
    private static final int REQUEST_CODE = 200;
    ImageButton btnRecord,btnStop,btnPlay;
    MediaRecorder mediaRecorder;
    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_audio);
        btnRecord=findViewById(R.id.btnRcrd);
        btnStop=findViewById(R.id.btnStp);
        btnPlay=findViewById(R.id.btnPly);



        btnPlay.setVisibility(View.INVISIBLE);


        if(isMicroPhonePresent()){
            getMicrophonePermission();
        }

        btnRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnStop.setVisibility(View.VISIBLE);



                try{
                    mediaRecorder=new MediaRecorder();
                    mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                    mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                    mediaRecorder.setOutputFile(getRecordingPath());
                    mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                    mediaRecorder.prepare();
                    mediaRecorder.start();
                    Toast.makeText(RecordAudioActivity.this,"Recording Has Started",Toast.LENGTH_LONG).show();

                    btnRecord.setVisibility(View.INVISIBLE);




                }catch(Exception e){
                    e.printStackTrace();
                }




            }
        });
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnRecord.setVisibility(View.VISIBLE);
                btnStop.setVisibility(View.INVISIBLE);
                btnPlay.setVisibility(View.VISIBLE);
                mediaRecorder.stop();
                mediaRecorder.release();
                mediaRecorder=null;
                Toast.makeText(RecordAudioActivity.this,"Recording has Stopped",Toast.LENGTH_LONG).show();
            }
        });
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnStop.setVisibility(View.INVISIBLE);
                try {
                    mediaPlayer=new MediaPlayer();
                    mediaPlayer.setDataSource(getRecordingPath());
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                    Toast.makeText(RecordAudioActivity.this,"recording is Playing ",Toast.LENGTH_LONG).show();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }
    private  boolean isMicroPhonePresent(){// method to check presence of microphone
        if (this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_MICROPHONE)){
            return  true;
        }else {
            return  false;
        }
    }
    private void getMicrophonePermission(){//code to get microphone access
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)==PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this,new String[]{
                    Manifest.permission.RECORD_AUDIO
            },REQUEST_CODE);
        }
    }
    private String getRecordingPath(){
        ContextWrapper contextWrapper=new ContextWrapper(getApplicationContext());
        File musicDirectory=contextWrapper.getExternalFilesDir(Environment.DIRECTORY_MUSIC);
        File file=new File(musicDirectory,"testRecording"+".mp3");
        return file.getPath();

    }
}