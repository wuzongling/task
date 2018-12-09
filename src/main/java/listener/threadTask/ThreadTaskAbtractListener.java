package listener.threadTask;

import constant.ListenerStatus;
import listener.EventListener;
import listener.EventSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;

/**
 * @Auther: zonglin_wu
 * @Date: 2018/12/3 19:05
 * @Description:
 */
public class ThreadTaskAbtractListener implements EventListener,Runnable{

    private static final Logger log = LoggerFactory.getLogger(ThreadTaskAbtractListener.class);

    List<EventSource> eventSourceList = new LinkedList<>();

    private int status = ListenerStatus.NEW;

    public ThreadTaskAbtractListener(){

    }

    public void addEvent(EventSource eventSource) {
        eventSourceList.add(eventSource);
    }

    public void deleteEvent(EventSource eventSource) {
        eventSourceList.remove(eventSource);
    }

    public void start() {
        status = ListenerStatus.START;
        new Thread(this).start();
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

    /**
     * 监听观察者对象
     * @return
     */
    public boolean listener(){
        for (EventSource eventSource : eventSourceList){
            boolean isTrigger = eventSource.isTrigger();
            if(isTrigger){
                System.out.println("ee:"+eventSource.getEventObject());
                eventSource.change();
            }
        }
        return false;
    }

    public void run() {
       try {
           while (status == ListenerStatus.START ){
               Thread.sleep(2000);
               listener();
           }
       }catch (Exception e){
           e.printStackTrace();
           status = ListenerStatus.EXCEPTION;
       }
    }


}
