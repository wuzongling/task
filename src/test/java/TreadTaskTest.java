import task.AbstractThreadTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @Auther: zonglin_wu
 * @Date: 2018/12/2 16:23
 * @Description:
 */
public class TreadTaskTest extends AbstractThreadTask{

    public Object postHandle(Object result, List params) throws Exception {
        System.out.println(getName()+":任务执行后");
        return null;
    }

    @Override
    public void errorCall(Exception e, List params) {
        System.out.println(getName()+":任务执行错误回滚");
    }

    public Object excute(List params) throws Exception {
        System.out.println(getName()+":任务执行");
        int r = new Random().nextInt();
        System.out.println(r);
        return r;
    }

}
