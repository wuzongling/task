package listener.threadtask;

import interf.ITask;
import listener.EventObserver;

/**
 * @Auther: zonglin_wu
 * @Date: 2018/12/9 16:27
 * @Description:
 */
public abstract class TaskAbstractObserver implements EventObserver{
    private ITask taskObserver;

    public TaskAbstractObserver(ITask task){
        this.taskObserver = task;
    }

    public ITask getTaskObserver() {
        return taskObserver;
    }

    public void setTaskObserver(ITask taskObserver) {
        this.taskObserver = taskObserver;
    }
}
