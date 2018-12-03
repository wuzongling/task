package listener;

import interf.ITask;

import java.util.List;

/**
 * 事件源
 * @Auther: zonglin_wu
 * @Date: 2018/12/3 14:04
 * @Description:
 */
public interface EventSource {

    /**
     * 事件名称
     * @return
     */
    public String getName();

    /**
     * 添加被观察者对象
     * @param task
     */
    public void addEventObject(ITask task);

    /**
     * 删除别观察对象
     * @param task
     */
    public void deleteEventObject(ITask task);

    /**
     * 获取被观察者对象
     * @return
     */
    public List<ITask> getEventObject();
}
