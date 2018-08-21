package handle;

import android.media.MediaPlayer;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;


public class MediaPlayManager {

    private static MediaPlayManager instance;

    private MediaPlayer mediaPlayer;

    private OnPlayListener onStartListener;
    //MP3 路径
    private String currentPlayPath;
    //播放状态记录
    private MediaPlayerState state;

    private enum MediaPlayerState{
        PLAYING,PAUSE,STOP,PREPARE,ERROR
    }

    public static MediaPlayManager getInstance() {
        if (instance == null) {
            instance = new MediaPlayManager();
        }
        return instance;
    }

    private MediaPlayManager() {
        mediaPlayer = new MediaPlayer();
        state = MediaPlayerState.PREPARE;
    }

    public void setOnPlayListener(OnPlayListener listener) {
        onStartListener = listener;
    }

    /**
     * 设置播放文件路径
     * @param playSource
     */
    public void setPlaySource(String playSource) {
        this.currentPlayPath = playSource;
    }

    /**
     * 开始加载MP3资源
     * @param seekPercent
     */
    public void playSource(final float seekPercent) {
        if (mediaPlayer == null || currentPlayPath == null) {
            if (onStartListener != null) {
                onStartListener.onError("mediaplayer is unstart");
            }
            return;
        }
        state = MediaPlayerState.PREPARE;
        mediaPlayer.reset();
        mediaPlayer.setAudioStreamType(android.media.AudioManager.STREAM_MUSIC);
        File file = new File(currentPlayPath);
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            mediaPlayer.setDataSource(fis.getFD());
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    Log.i("MediaPlayManager","onPrepare success...."+mp.getDuration()+" seek:"+seekPercent);
                    if (seekPercent != -1) {
                        mediaPlayer.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
                            @Override
                            public void onSeekComplete(MediaPlayer mp) {
                                mediaPlayer.start();
                                state = MediaPlayerState.PLAYING;
                                if (onStartListener != null) {
                                    onStartListener.onStart();
                                    Log.i("MediaPlayManager","mp3 play onPrepare success..setOnSeekCompleteListener..");
                                }
                            }
                        });
                        mediaPlayer.seekTo((int) (seekPercent*mp.getDuration()));
                    } else {
                        mediaPlayer.start();
                        state = MediaPlayerState.PLAYING;
                        if (onStartListener != null) {
                            onStartListener.onStart();
                            Log.i("MediaPlayManager","mp3 play onPrepare success....");
                        }
                    }
                }
            });
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    reset();
                    state = MediaPlayerState.STOP;
                    if (onStartListener != null) {
                        onStartListener.onCompletion();
                    }
                    Log.i("MediaPlayManager","mp3 play onCompletion....");
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            state = MediaPlayerState.ERROR;
            if (onStartListener != null) {
                onStartListener.onError("音频加载失败，请重试！");
                Log.e("MediaPlayManager","init mp3 resource error...."+e.toString());
            }
            mediaPlayer.reset();
        }
    }

    /**
     * 暂停播放
     */
    public void pause() {
        state = MediaPlayerState.PAUSE;
        mediaPlayer.pause();
    }

    /**
     * 跳过某一段再播放
     * @param percent
     */
    public void seek(float percent) {
        state = MediaPlayerState.STOP;
        playSource(percent);

    }



    /**
     * 继续播放
     */
    public void continuePlay() {
        state = MediaPlayerState.PLAYING;
        mediaPlayer.start();
    }

    /**
     * 停止播放
     */
    public void reset() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            state = MediaPlayerState.STOP;
        }
    }

    //播放完毕需要释放MediaPlayer的资源
    public void release(){
        if(mediaPlayer != null){
            state = MediaPlayerState.STOP;
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    public interface OnPlayListener{
        void onStart();
        void onCompletion();
        void onError(String errorMsg);
    }


}
