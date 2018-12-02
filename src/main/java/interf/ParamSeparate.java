package interf;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Auther: zonglin_wu
 * @Date: 2018/12/2 16:33
 * @Description:
 */
public interface ParamSeparate<T> {

    public ArrayList<List<T>> separate(Object separate);
}
