import task.AbstractThreadTask;

import java.util.List;

/**
 * @Auther: zonglin_wu
 * @Date: 2018/12/15 16:27
 * @Description:
 */
public class ErrorTaskTest extends AbstractThreadTask{
    @Override
    public Object excute(List params) throws Exception {
        Thread.sleep(2000);
        System.out.println(getName()+"错误任务开始执行");
        throw new RuntimeException("测试错误");
    }

    @Override
    public void errorCall(Exception e, List params) {
        System.out.println(getName()+"错误回调");
    }
}
