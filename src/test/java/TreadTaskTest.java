import task.AbstractThreadTask;

import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: zonglin_wu
 * @Date: 2018/12/2 16:23
 * @Description:
 */
public class TreadTaskTest extends AbstractThreadTask{

    public Object postHandle(Object result, List params) throws Exception {
        System.out.println("任务执行后");
        return null;
    }

    public void errorHandle(Exception e, List params) {
        System.out.println("任务执行错误");
    }

    public Object excute(List params) throws Exception {
        System.out.println("任务执行");
        return null;
    }

    public void cancelHandle(List params) {
        System.out.println("任务关闭");
    }
}
