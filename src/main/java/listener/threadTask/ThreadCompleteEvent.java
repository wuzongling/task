package listener.threadTask;

import constant.TaskStatus;
import interf.ITask;

/**
 * @Auther: zonglin_wu
 * @Date: 2018/12/4 19:41
 * @Description:
 */
public class ThreadCompleteEvent extends ThreadTaskAbtractEvent{

    public ThreadCompleteEvent(ITask task) {
        super(task);
    }

    @Override
    public String getName() {
        return "ThreadCompleteEvent";
    }

    @Override
    public boolean isTrigger() {
        ITask task = this.getEventObject();
        int eventStatus = task.getStatus();
        return eventStatus == TaskStatus.NORMAL;
    }
}
