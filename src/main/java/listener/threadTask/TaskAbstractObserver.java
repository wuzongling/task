package listener.threadTask;

import interf.ITask;
import listener.EventObserver;

/**
 * @Auther: zonglin_wu
 * @Date: 2018/12/9 16:27
 * @Description:
 */
public abstract class TaskAbstractObserver implements EventObserver{
    public ITask taskObserver;

    public TaskAbstractObserver(ITask task){
        this.taskObserver = task;
    }

    @Override
    public abstract void update(Object param);
}
