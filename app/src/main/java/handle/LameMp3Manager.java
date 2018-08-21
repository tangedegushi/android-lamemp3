package handle;

import android.util.Log;

import java.io.File;
import java.io.IOException;

import handle.recorder.AudioRecorderListener;
import handle.recorder.Mp3Recorder;


/**
 * Created by clam314 on 2016/8/24
 *
 * 对Mp3Recorder进行多一次封装，增加录音取消相应的逻辑
 */
public enum LameMp3Manager implements Mp3Recorder.OnFinishListener {
    instance;

    private static final String TAG = LameMp3Manager.class.getSimpleName();
    private Mp3Recorder mp3Recorder;
    private boolean cancel = false;
    private boolean stop = false;
    private AudioRecorderListener mediaRecorderListener;


    LameMp3Manager(){
        mp3Recorder = new Mp3Recorder();
        mp3Recorder.setFinishListener(this);
    }

    public void setMediaRecorderListener(AudioRecorderListener listener){
        mediaRecorderListener = listener;
    }

    public void startRecorder(String saveMp3FullName){
        cancel = stop = false;
        try {
            mp3Recorder.startRecording(createMp3SaveFile(saveMp3FullName));
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void stopRecording(){
        try {
            mp3Recorder.stopRecording();
            stop = true;
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void pauseRecording() {
        mp3Recorder.pauseRecorder();
    }

    public void continueRecording() {
        mp3Recorder.continueRecording();
    }

    private File createMp3SaveFile(String saveMp3FullName){
        File mp3 = new File(saveMp3FullName);
        Log.d(TAG,"create mp3 file for the recorder:"+saveMp3FullName);
        return mp3;
    }


    @Override
    public void onFinish(String mp3FilePath) {
        if(cancel){
            //录音取消的话，将之前的录音数据清掉
            File mp3 = new File(mp3FilePath);
            if(mp3.exists()){
//                mp3.delete();
            }
            cancel = false;
        }else if(stop){
            stop = false;
            if(mediaRecorderListener != null){
                mediaRecorderListener.onRecorderFinish(209,mp3FilePath);
            }
        }
    }
}
