package handle.interf;

import java.util.List;

/**
 * Created by ubt on 2018/1/25.
 */

public interface ContactCallback<E> {
    void fail(String msg);

    void success(List<E> data);
}
