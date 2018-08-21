package handle.recorder;

/**
 * Created by clam314 on 2016/7/21
 */
public interface AudioRecorderListener {

    public void onRecorderFinish(int status, String path);
}
