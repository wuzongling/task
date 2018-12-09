package listener.threadTask;

import interf.ITask;
import listener.EventObserver;
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

    private ITask eventObject;

    List<EventObserver> observerList = new ArrayList<EventObserver>();

    public ThreadTaskAbtractEvent(){

    }
    public ThreadTaskAbtractEvent(ITask eventObject){
        this.eventObject = eventObject;
    }
    public ThreadTaskAbtractEvent(ITask eventObject,List<EventObserver> observerList){
        this.eventObject = eventObject;
        this.observerList = observerList;
    }


    public String getName() {
        return null;
    }

    public void addEventObject(ITask task) {
        this.eventObject = eventObject;
    }

    public void deleteEventObject(ITask task) {
        this.eventObject = null;
    }

    public ITask getEventObject() {
        return eventObject;
    }

    public void addObserver(EventObserver observer) {
        observerList.add(observer);
    }

    public void deleteObserver(EventObserver observer) {
        observerList.remove(observer);
    }

    public void change() {
        for (EventObserver observer : observerList){
            observer.update(eventObject);
        }
    }

    public boolean isTrigger() {
        return false;
    }
}
