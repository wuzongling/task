package listener;

/**
 * 事件观察者
 * @Auther: zonglin_wu
 * @Date: 2018/12/3 15:02
 * @Description:
 */
public interface EventObserver {
    /**
     * 改变
     * @param param
     */
    public void update(Object param);
}
