package listener;

import factory.ThreadTaskEventFactory;
import interf.ITask;
import listener.threadtask.ThreadTaskAbtractListener;

/**
 * @Auther: zonglin_wu
 * @Date: 2018/12/8 14:09
 * @Description:
 */
public class ListennerAdapter {

    public EventListener addListenner(ITask task, EventObserver eventObserver,int eventType){
        EventListener listener = new ThreadTaskAbtractListener();
        EventSource eventSource = ThreadTaskEventFactory.buildEvent(eventType,task);
        eventSource.addObserver(eventObserver);
        listener.addEvent(eventSource);
        return listener;
    }
}
