package listener.threadTask;

import constant.ListenerStatus;
import interf.ITask;
import listener.EventSource;

import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: zonglin_wu
 * @Date: 2018/12/3 15:14
 * @Description:
 */
public class ThreadTaskAbtractEvent implements EventSource{
    private String name;

    private List<ITask> eventObjects = new ArrayList<ITask>();

    public String getName() {
        return null;
    }

    public void addEventObject(ITask task) {
        eventObjects.add(task);
    }

    public void deleteEventObject(ITask task) {
        eventObjects.remove(task);
    }

    public List<ITask> getEventObject() {
        return eventObjects;
    }
}
