package task;

import interf.ParamSeparate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public ArrayList<List> separate(Object separate) {
        long count = (Long)separate;
        ArrayList<List> rlist = new ArrayList<List>();
        for (int a = 1;a <= count; a=+segament){
            ArrayList plist = new ArrayList();
            plist.add(a);
            rlist.add(plist);
        }
        return rlist;
    }

}
