package com.example.zzq.lamemp3demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.io.File;

import handle.LameMp3Manager;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

    }

    private void initView() {
        findViewById(R.id.button).setOnClickListener(this);
        findViewById(R.id.button2).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:
                LameMp3Manager.instance.startRecorder(getSaveMp3FilePath());
                break;
            case R.id.button2:
                LameMp3Manager.instance.stopRecording();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LameMp3Manager.instance.stopRecording();
    }

    public String getSaveMp3FilePath(){
        return getExternalCacheDir().getAbsolutePath()+ File.separator + "record.mp3";
    }
}
