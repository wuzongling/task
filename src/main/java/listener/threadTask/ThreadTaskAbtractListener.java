package listener.threadTask;

import constant.ListenerStatus;
import listener.EventListener;
import listener.EventObserver;
import listener.EventSource;

import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: zonglin_wu
 * @Date: 2018/12/3 19:05
 * @Description:
 */
public abstract class ThreadTaskAbtractListener implements EventListener,Runnable{

    List<EventSource> eventSourceList = new ArrayList<EventSource>();

    List<EventObserver> observerList = new ArrayList<EventObserver>();

    private int status = ListenerStatus.NEW;

    public void addEvent(EventSource eventSource) {
        eventSourceList.add(eventSource);
    }

    public void deleteEvent(EventSource eventSource) {
        eventSourceList.remove(eventSource);
    }

    public void addObserver(EventObserver observer) {
        observerList.add(observer);
    }

    public void deleteObserver(EventObserver observer) {
        observerList.remove(observer);
    }

    public void start() {
        status = ListenerStatus.START;
    }

    public void suspend(long millisecond) {
        try {
            status = ListenerStatus.SUSPEND;
            Thread.sleep(millisecond);

        }catch (InterruptedException e){

        }
        finally {
            status = ListenerStatus.START;
        }
    }

    public void cancel() {
        status = ListenerStatus.CANCEL;
    }

    public abstract boolean listener();

    public void run() {
       try {
           boolean flag = false;
           while (status == ListenerStatus.START ){
               flag = listener();
               if(flag){
                   for(EventObserver observer : observerList){
                       observer.update(null);
                   }
               }
           }
       }catch (Exception e){
           e.printStackTrace();
           status = ListenerStatus.EXCEPTION;
       }
    }
}
