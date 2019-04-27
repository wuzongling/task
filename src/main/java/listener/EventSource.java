package listener;

import interf.ITask;

/**
 * 事件源（Observable）
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
     * 获取事件类型
     * @return
     */
    public int getType();

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
    public ITask getEventObject();

    /**
     * 添加观察者
     * @param observer
     */
    public void addObserver(EventObserver observer);

    /**
     * 删除观察者
     * @param observer
     */
    public void deleteObserver(EventObserver observer);

    /**
     * 通知
     */
    public void change();

    /**
     * 是否触发事件
     * @return
     */
    public boolean isTrigger();
}
