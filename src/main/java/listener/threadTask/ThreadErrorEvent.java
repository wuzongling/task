package listener.threadTask;

import constant.TaskStatus;
import interf.ITask;
import listener.EventSource;

/**
 * @Auther: zonglin_wu
 * @Date: 2018/12/8 14:20
 * @Description:
 */
public class ThreadErrorEvent extends ThreadTaskAbtractEvent{

    public ThreadErrorEvent(ITask task) {
        super(task);
    }

    @Override
    public String getName() {
        return "ThreadErrorEvent";
    }

    @Override
    public boolean isTrigger() {
        ITask task = this.getEventObject();
        int eventStatus = task.getStatus();
        return eventStatus == TaskStatus.EXCEPTIONAL;
    }
}
