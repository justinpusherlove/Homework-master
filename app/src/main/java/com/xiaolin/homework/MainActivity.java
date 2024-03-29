package com.xiaolin.homework;

import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.IOException;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    boolean status=false;
    File soundFile;
    MediaRecorder mediaRecorder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        //实现摇动录音
        SensorManagerHelper sensorHelper = new SensorManagerHelper(this);
        sensorHelper.setOnShakeListener(new SensorManagerHelper.OnShakeListener() {

            @Override
            public void onShake() {
                // TODO Auto-generated method stub
                if (!status) {//如果此时没有进入录音，摇动后将进入录音  此处添加录音代码
                    Toast.makeText(MainActivity.this, "你在摇哦，录音开始", Toast.LENGTH_SHORT).show();
                    //启用录音功能
                    record();
                    //改变状态值
                    status=!status;
                }else{//此时正在录音，摇动后停止录音 此处结束录音
                    Toast.makeText(MainActivity.this, "你又摇了，录音结束", Toast.LENGTH_SHORT).show();
                    //停止录音功能
                    stop();
                    //改变状态值
                    status=!status;
                }
            }
        });

    }

    public void initView() {
        findViewById(R.id.startBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //启动监听服务
                startService(new Intent(MainActivity.this, RecorderService.class));
            }
        });
        findViewById(R.id.stopBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //停止监听服务
                stopService(new Intent(MainActivity.this, RecorderService.class));
            }
        });
    }
    //进行录音函数 代码复用
    public void record(){
        if(!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            Toast.makeText(MainActivity.this,"SD卡不存在，请插入SD卡！",Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            String time =String.valueOf(new Date().getTime());
            soundFile = new File(Environment.getExternalStorageDirectory().getCanonicalFile()+"/"+time+"sound1.amr");
            mediaRecorder = new MediaRecorder();
            //设置录音的声音来源
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            //设置录音的输出格式
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            //设置声音的编码
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mediaRecorder.setOutputFile(soundFile.getAbsolutePath());
            Log.v("record",String.valueOf(soundFile.length()));
            mediaRecorder.prepare();
            mediaRecorder.start();
            Toast.makeText(MainActivity.this,"开始录音",Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //停止录音函数 代码复用
    public  void stop(){
        if(soundFile!=null && soundFile.exists()){
            //停止录音
            mediaRecorder.stop();
            Log.v("record",String.valueOf(soundFile.length()));
            Toast.makeText(MainActivity.this,"录音结束",Toast.LENGTH_SHORT).show();
            //释放资源
            mediaRecorder.release();
            mediaRecorder=null;
        }

    }



}
