package listener.threadtask;

import constant.EventType;
import constant.TaskStatus;
import interf.ITask;

/**
 * @Auther: zonglin_wu
 * @Date: 2018/12/8 14:20
 * @Description:
 */
public class ThreadErrorEvent extends ThreadTaskAbtractEvent{

    public ThreadErrorEvent(ITask task) {
        super(task);
    }

    public ThreadErrorEvent(){

    }

    @Override
    public String getName() {
        return "ThreadErrorEvent";
    }

    @Override
    public int getType() {
        return EventType.EXCEPTIONAL_EVENT;
    }

    @Override
    public boolean isTrigger() {
        ITask task = this.getEventObject();
        int eventStatus = task.getStatus();
        return eventStatus == TaskStatus.EXCEPTIONAL;
    }
}
