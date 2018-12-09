package interf;

import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: zonglin_wu
 * @Date: 2018/12/1 16:02
 * @Description:
 */
public interface ITask {

    /**
     * 获取名称
     * @return
     */
    public String getName();

    /**
     * 任务执行前处理器
     * @param params
     */
    public void preHandle(List params) throws Exception;

    /**
     * 执行中
     * @param params
     * @return
     */
    public Object excute(List params)throws Exception;

    /**
     * 任务执行后处理器
     * @param result
     * @param params
     * @return
     */
    public Object postHandle(Object result,List params)throws Exception;

    /**
     * 获取任务结果
     * @return
     */
    public Object getResult();

    /**
     * 获取任务状态
     * @return
     */
    public Integer getStatus();

    /**
     * 错误处理器
     * @param e
     * @param params
     */
    public void errorHandle(Exception e,List params);

    /**
     * 开始任务
     */
    public void start();

    /**
     * 暂停
     * @param millisecond
     */
    public void suspend(long millisecond)throws Exception;

    /**
     * 关闭
     * @param flag
     */
    public void cancel(boolean flag);

}
