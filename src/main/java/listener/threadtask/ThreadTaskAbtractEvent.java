package listener.threadtask;

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
public abstract class ThreadTaskAbtractEvent implements EventSource{
    private String name;

    private ITask eventObject;

    List<EventObserver> observerList = new ArrayList<>();

    public ThreadTaskAbtractEvent(){

    }
    public ThreadTaskAbtractEvent(ITask eventObject){
        this.eventObject = eventObject;
    }
    public ThreadTaskAbtractEvent(ITask eventObject,List<EventObserver> observerList){
        this.eventObject = eventObject;
        this.observerList = observerList;
    }
    public ThreadTaskAbtractEvent(ITask eventObject,List<EventObserver> observerList,String name){
        this.eventObject = eventObject;
        this.observerList = observerList;
        this.name = name;
    }

    public String getName() {
        if(name == null || name == ""){
            setName(Thread.currentThread().getName()+"_event");
        }
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addEventObject(ITask task) {
        this.eventObject = task;
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
