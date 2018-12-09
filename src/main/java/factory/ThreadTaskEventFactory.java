package factory;

import constant.EventType;
import interf.ITask;
import listener.EventObserver;
import listener.EventSource;
import listener.threadTask.ThreadCompleteEvent;
import listener.threadTask.ThreadErrorEvent;

import java.util.List;

/**
 * @Auther: zonglin_wu
 * @Date: 2018/12/8 14:16
 * @Description:
 */
public class ThreadTaskEventFactory {

    public static EventSource buildEvent(int eventType, ITask task){
        switch (eventType){
            case EventType.EXCEPTIONAL_EVENT : return new ThreadErrorEvent(task);
            case EventType.NORMAL_EVENT : return new ThreadCompleteEvent(task);
            default:return null;
        }
    }

    public static EventSource buildEvent(int eventType, ITask task, EventObserver observer){
        EventSource eventSource = null;
        switch (eventType){
            case EventType.EXCEPTIONAL_EVENT : eventSource = new ThreadErrorEvent(task);break;
            case EventType.NORMAL_EVENT : eventSource = new ThreadCompleteEvent(task);break;
            default:return eventSource;
        }
        eventSource.addObserver(observer);
        return eventSource;
    }
}
