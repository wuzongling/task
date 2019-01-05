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
        log.info("开始监听");
        status = ListenerStatus.START;
        Thread thread = new Thread(this);
        thread.setDaemon(true);
        thread.start();
    }

    public void suspend(long millisecond) {
        try {
            log.info("监听器暂停");
            status = ListenerStatus.SUSPEND;
            Thread.sleep(millisecond);
        }catch (InterruptedException e){
            log.info("监听器重新监听");
            Thread.currentThread().interrupt();
        }
        finally {
            status = ListenerStatus.START;
        }
    }

    public void cancel() {
        log.info("监听器关闭");
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
