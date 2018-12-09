package listener;

/** 事件监听器
 * @Auther: zonglin_wu
 * @Date: 2018/12/3 14:02
 * @Description:
 */
public interface EventListener {
    /**
     * 添加事件
     * @param eventSource
     */
    public void addEvent(EventSource eventSource);

    /**
     * 删除事件
     * @param eventSource
     */
    public void deleteEvent(EventSource eventSource);

    /**
     * 开始
     */
    public void start();

    /**
     * 暂停
     * @param millisecond
     */
    public void suspend(long millisecond);

    /**
     * 关闭
     */
    public void cancel();

    /**
     * 监听观察者对象
     * @return
     */
    public boolean listener();
}
