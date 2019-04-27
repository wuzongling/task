package task;

import interf.ParamSeparate;

import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: zonglin_wu
 * @Date: 2018/12/2 16:40
 * @Description:
 */
public class IntCountSeparate implements ParamSeparate{

    private int segament;

    public IntCountSeparate(int segament){
        this.segament = segament;
    }

    public List<List> separate(Object separate) {
        long count = (Long)separate;
        ArrayList<List> rlist = new ArrayList<>();
        for (int a = 1;a <= count; a=+segament){
            ArrayList plist = new ArrayList();
            plist.add(a);
            rlist.add(plist);
        }
        return rlist;
    }

}
