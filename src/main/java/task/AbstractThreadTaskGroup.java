package task;

import constant.EventType;
import constant.TaskStatus;
import factory.ThreadTaskEventFactory;
import interf.ITask;
import interf.ITaskGroup;
import listener.*;
import listener.threadTask.TaskAbstractObserver;
import listener.threadTask.ThreadCompleteEvent;
import listener.threadTask.ThreadTaskAbtractListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * 线程任务组基类
 * @Auther: zonglin_wu
 * @Date: 2018/12/1 16:51
 * @Description:
 */
public abstract class AbstractThreadTaskGroup extends AbstractThreadTask implements ITaskGroup {

    private ArrayList<AbstractThreadTask> taskList = new ArrayList();

    private ThreadPoolExecutor threadPoolExecutor;

    private boolean synResult = false;

    private List resultList = new ArrayList();

    EventListener eventListener;
    public AbstractThreadTaskGroup(ThreadPoolExecutor threadPoolExecutor){
        this.threadPoolExecutor = threadPoolExecutor;
    }

    public AbstractThreadTaskGroup(ThreadPoolExecutor threadPoolExecutor,boolean synResult){
        this.threadPoolExecutor = threadPoolExecutor;
        this.synResult = synResult;
    }

    public AbstractThreadTaskGroup(){

    }
    EventObserver exceptionObserver;
    EventObserver completeObserver;
    {
        exceptionObserver = new TaskAbstractObserver(this) {
            @Override
            public void update(Object param) {
                this.taskObserver.cancel(true);
            }
        };
        completeObserver = new TaskAbstractObserver(this) {
            @Override
            public void update(Object param) {
                synchronized(this){
                    AbstractThreadTaskGroup taskGroup = (AbstractThreadTaskGroup) this.taskObserver;
                    ITask task = (ITask)param;
                    Object o = task.getResult();
                    System.out.println("t:"+task);
                    System.out.println("o:"+o);
                    taskGroup.resultList.add(o);
                    if(resultList.size() == taskList.size()){
                        status = TaskStatus.NORMAL;
                        collectCalculate(resultList);
                        try {
                            postHandle(resultList,null);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        };
    }
    private void initListener(){
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

    public abstract Object collectCalculate(List params);

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

    public Object postHandle(Object result, ArrayList params) throws Exception {
//        eventListener.cancel();
        return null;
    }

    @Override
    public Object getResult() {
        if (!synResult){
            //同步
            while (true){
                if(status == TaskStatus.NORMAL){
                    return resultList;
                }
            }
        }
        return resultList;
    }

    public void errorHandle(Exception e, List params) {
        cancel(true);
        errorCall(e,params);
    }

    public void start() {
        status = TaskStatus.COMPLETING;
        try {
//            initListener();
            excute(null);
        } catch (Exception e) {
            status = TaskStatus.EXCEPTIONAL;
            errorHandle(e,null);
        }
    }

    public void suspend(long millisecond) throws Exception {
        for(ITask task : taskList){
            task.suspend(millisecond);
        }
    }

    public void cancel(boolean flag) {
        if(status != TaskStatus.CANCELLED){
            status = TaskStatus.CANCELLED;
            int ctaskStatus;
            for(ITask task : taskList){
                ctaskStatus = task.getStatus();
                if(ctaskStatus != TaskStatus.EXCEPTIONAL){
                    task.cancel(true);
                    task.errorHandle(null,null);
                }
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
