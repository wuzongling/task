package task;

import constant.EventType;
import constant.TaskStatus;
import factory.ThreadTaskEventFactory;
import interf.ITask;
import listener.EventObserver;
import listener.EventSource;
import listener.threadTask.ThreadCompleteEvent;
import listener.threadTask.ThreadErrorEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.ParamsUtil;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.FutureTask;

/**
 * 线程任务基类
 * @Auther: zonglin_wu
 * @Date: 2018/12/1 16:22
 * @Description:
 */
public abstract class AbstractThreadTask implements ITask,Callable{
    private static final Logger log = LoggerFactory.getLogger(AbstractThreadTask.class);

    private boolean synResult = false;
    //执行参数
    private List params;

    private FutureTask futureTask = new FutureTask(this);
    //返回结果
    private Object result;

    private String name = getName();

    volatile int status = TaskStatus.NEW;

    private boolean isErrorCall = true;

    private Map<Integer,EventSource> eventMap = new ConcurrentHashMap<>();

    public AbstractThreadTask(String name,List params){
        this(name,params,false);
    }

    public AbstractThreadTask(String name,List params,boolean synResult){
        this.params = params;
        this.synResult = synResult;
    }
    public AbstractThreadTask(String name ){
        this(name,null);
    }

    public AbstractThreadTask(){
    }

    {
        //初始化事件
       /* EventSource completeEvent = new ThreadCompleteEvent();
        EventSource errorEvent = new ThreadErrorEvent();
        completeEvent.addEventObject(this);
        errorEvent.addEventObject(this);*/
        EventSource completeEvent = ThreadTaskEventFactory.buildEvent(EventType.NORMAL_EVENT,this);
        EventSource errorEvent = ThreadTaskEventFactory.buildEvent(EventType.EXCEPTIONAL_EVENT,this);
        eventMap.put(completeEvent.getType(),completeEvent);
        eventMap.put(errorEvent.getType(),errorEvent);
    }

    protected void init(){
    }

    public String getName(){
        if(name == null || name.equals("")){
            return Thread.currentThread().getName()+"_AbstractThreadTask";
        }
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void preHandle(List params) throws Exception{
        init();
        log.info(getName()+"开始执行 params:"+ ParamsUtil.toParamsString(params));
    }

    public abstract Object excute(List params)throws Exception;

    public Object postHandle(Object result, List params)throws Exception {
        log.info(getName()+"执行完成，执行结果："+ParamsUtil.toResultString(result));
        return result;
    }

    public Object getResult(long millisecond){
        Date date = new Date();
        if(millisecond != 0 && date.getTime() > millisecond){
            return result;
        }
        try {
            if(!synResult){
                result = futureTask.get();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    public Integer getStatus() {
        return status;
    }

    public void errorHandle(Exception e, List params){
           if(isErrorCall){
               errorCall(e,params);
           }
    }

    public void suspend(long millisecond)throws Exception {
        Thread.sleep(millisecond);
    }

    public void cancel(boolean flag){
        status = TaskStatus.CANCELLED;
        futureTask.cancel(flag);
    }

    public void register(int type, EventObserver observer){
        EventSource eventSource = eventMap.get(type);
        eventSource.addObserver(observer);
    }

    public abstract void errorCall(Exception e,List params);

    public void start(){
        status = TaskStatus.COMPLETING;
        new Thread(futureTask).start();
    }

    public Object call() {
        try {
            preHandle(params);
            result = excute(params);
            postHandle(result,params);
            status = TaskStatus.NORMAL;
            eventChange(EventType.NORMAL_EVENT);
        }catch (Exception e){
            log.error(getName()+"发生异常,params:"+params,e);
            status = TaskStatus.EXCEPTIONAL;
            errorHandle(e,params);
            eventChange(EventType.EXCEPTIONAL_EVENT);
        }
        return result;
    }

    private void eventChange(final int eventType){
        new Thread(new Runnable() {
            @Override
            public void run() {
                EventSource eventSource = eventMap.get(eventType);
                eventSource.change();
            }
        }).start();
    }

    public void setParams(List params) {
        this.params = params;
    }

    public FutureTask getFutureTask() {
        return futureTask;
    }

    public void setErrorCall(boolean errorCall) {
        isErrorCall = errorCall;
    }
}
