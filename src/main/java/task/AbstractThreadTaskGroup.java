package task;

import constant.EventType;
import constant.TaskStatus;
import exception.ThreadTaskException;
import factory.ThreadTaskEventFactory;
import interf.ITask;
import interf.ITaskGroup;
import listener.EventListener;
import listener.EventObserver;
import listener.EventSource;
import listener.threadtask.TaskAbstractObserver;
import listener.threadtask.ThreadTaskAbtractListener;
import org.apache.commons.lang3.time.DateUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程任务组基类
 * @Auther: zonglin_wu
 * @Date: 2018/12/1 16:51
 * @Description:
 */
public abstract class AbstractThreadTaskGroup extends AbstractThreadTask implements ITaskGroup {

    private ArrayList<AbstractThreadTask> taskList = new ArrayList();

    private ThreadPoolExecutor threadPoolExecutor;
    //是否同步
    private boolean synResult = true;

    private List resultList = new ArrayList();
    //等待时长，默认30分
    private int waitMillisecond = 1000*60*30;
    //异常观察者
    EventObserver exceptionObserver;
    //事件完成观察者
    EventObserver completeObserver;

    EventListener eventListener;
    public AbstractThreadTaskGroup(ThreadPoolExecutor threadPoolExecutor){
        this.threadPoolExecutor = threadPoolExecutor;
        init();
    }

    public AbstractThreadTaskGroup(ThreadPoolExecutor threadPoolExecutor,boolean synResult){
        this.threadPoolExecutor = threadPoolExecutor;
        this.synResult = synResult;
        init();
    }

    public AbstractThreadTaskGroup(){
        init();
    }

    @Override
    public void init(){
        initObserver();
    }

    /**
     * 初始化事件
     */
    private void initObserver(){
        if(exceptionObserver == null){
            exceptionObserver = new TaskAbstractObserver(this) {
                @Override
                public void update(Object param) {
                    this.getTaskObserver().cancel(true);
                }
            };
        }

        if(completeObserver == null){
            completeObserver = new TaskAbstractObserver(this) {
                @Override
                public void update(Object param) {
                    synchronized(this){
                        AbstractThreadTaskGroup taskGroup = (AbstractThreadTaskGroup) this.getTaskObserver();
                        //正在执行的任务
                        ITask task = (ITask)param;
                        Object result = task.getResult(waitMillisecond);
                        taskGroup.resultList.add(result);
                        if(resultList.size() == taskList.size()){
                            status = TaskStatus.NORMAL;
                            collectCalculate(resultList);
                            try {
                                postHandle(resultList,null);
                            } catch (Exception e) {
                                throw new ThreadTaskException(e);
                            }
                        }
                    }
                }
            };
        }
    }


    /**
     * 初始化监听
     */
    protected void initListener(){
        eventListener = new ThreadTaskAbtractListener();
        for(ITask task : taskList){
            EventSource exceptionEvent = ThreadTaskEventFactory.buildEvent(EventType.EXCEPTIONAL_EVENT,task,exceptionObserver);
            EventSource completeEvent = ThreadTaskEventFactory.buildEvent(EventType.NORMAL_EVENT,task,completeObserver);
            if(exceptionEvent != null){
                eventListener.addEvent(exceptionEvent);
            }
            if (completeEvent != null){
                eventListener.addEvent(completeEvent);
            }
        }
        eventListener.start();
    }

    public boolean addTask(ITask task) {
        AbstractThreadTask abstractThreadTask = (AbstractThreadTask)task;
        taskList.add(abstractThreadTask);
        registerEvent(task);
        return true;
    }

    private void registerEvent(ITask task){
        task.register(EventType.NORMAL_EVENT,completeObserver);
        task.register(EventType.EXCEPTIONAL_EVENT,exceptionObserver);
    }

    public boolean removeTask(int i) {
        taskList.remove(i);
        return true;
    }

    @Override
    public String getName() {
        return Thread.currentThread().getName()+"_AbstractThreadTaskGroup";
    }

    @Override
    public Object excute(List params) throws Exception {
        threadPoolExecutor = getThreadPoolExecutor();
        for(AbstractThreadTask task : taskList){
            threadPoolExecutor.execute(task.getFutureTask());
        }
        //一定要调用关闭方法
        threadPoolExecutor.shutdown();
        return null;
    }

    @Override
    public Object postHandle(Object result, List params) throws Exception {
        if(eventListener != null){
            eventListener.cancel();
        }
        return super.postHandle(result,params);
    }

    @Override
    public Object getResult(int millisecond) {

        //同步
        if (synResult){
            Date currentDate = new Date();
            Date expirationTime = DateUtils.addMilliseconds(currentDate,millisecond);
            while (true){
                if(currentDate.getTime() > expirationTime.getTime()){
                    return resultList;
                }
                if(status >= TaskStatus.NORMAL){
                    return resultList;
                }
            }
        }
        return resultList;
    }

    public Object getResult() {
        return getResult(waitMillisecond);
    }

    public void start() {
        status = TaskStatus.COMPLETING;
        try {
            excute(null);
            getResult();
        } catch (Exception e) {
            status = TaskStatus.EXCEPTIONAL;
            errorHandle(e,null);
        }
    }
    @Override
    public void suspend(long millisecond) throws Exception {
        for(ITask task : taskList){
            task.suspend(millisecond);
        }
    }

    @Override
    public void errorHandle(Exception e, List params) {
        super.errorHandle(e,params);
    }

    @Override
    public void cancel(boolean flag) {
        if(status != TaskStatus.CANCELLED){
            status = TaskStatus.CANCELLED;
            //是否出现异常以上的级别
            int flagStatus = 0;
            for(ITask task : taskList){
                int ctaskStatus = task.getStatus();
                if(ctaskStatus >= TaskStatus.EXCEPTIONAL){
                    flagStatus = ctaskStatus;
                        break;
                }
            }

            if(flagStatus >= TaskStatus.EXCEPTIONAL){
                taskList.stream().forEach(task ->{
                    int ctaskStatus = task.getStatus();
                    //没有发现异常的任务进行回滚，发生过异常的已经回滚过了
                    if(ctaskStatus < TaskStatus.EXCEPTIONAL){
                        task.errorHandle(null,null);
                    }
                });
            }else {
                taskList.stream().forEach(task-> task.cancel(true));
            }
            taskList = null;
        }
    }


    public ThreadPoolExecutor getThreadPoolExecutor(){
        if(threadPoolExecutor == null){
            return new ThreadPoolExecutor(8, 8,
                    0L, TimeUnit.MILLISECONDS,
                    new LinkedBlockingQueue<Runnable>());
        }
        return threadPoolExecutor;
    }

    @Override
    public void errorCall(Exception e, List params) {
    }

}
