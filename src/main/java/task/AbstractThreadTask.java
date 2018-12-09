package task;

import constant.TaskStatus;
import interf.ITask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.ParamsUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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

    private List params;

    private FutureTask futureTask = new FutureTask(this);

    private Object result;

    private String name = getName();

    volatile int status = TaskStatus.NEW;

    private boolean isErrorCall = true;

    public AbstractThreadTask(List params){
        this.params = params;
    }

    public AbstractThreadTask(List params,boolean synResult){
        this.params = params;
        this.synResult = synResult;
    }

    public AbstractThreadTask(){
    }

    public String getName(){
        return Thread.currentThread().getName()+"_AbstractThreadTask";
    }

    public void preHandle(List params) throws Exception{
        log.info(name+"开始执行 params:"+ ParamsUtil.toParamsString(params));
    }

    public abstract Object excute(List params)throws Exception;

    public Object postHandle(Object result, List params)throws Exception {
        log.info(name+"执行完成，执行结果："+ParamsUtil.toResultString(result));
        return result;
    }

    public Object getResult(){
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
        }catch (Exception e){
            log.error(name+"发生异常,params:"+params,e);
            status = TaskStatus.EXCEPTIONAL;
            errorHandle(e,params);
        }
        return result;
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
