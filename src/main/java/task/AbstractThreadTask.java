package task;

import constant.TaskStatus;
import interf.ITask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.ParamsUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.FutureTask;

/**
 * @Auther: zonglin_wu
 * @Date: 2018/12/1 16:22
 * @Description:
 */
public abstract class AbstractThreadTask implements ITask,Runnable{
    private static final Logger log = LoggerFactory.getLogger(AbstractThreadTask.class);


    private List params;

    private FutureTask futureTask = new FutureTask(this,0);;

    private Object result;

    private String name = getName();

    private int status = TaskStatus.NEW;

    public AbstractThreadTask(List params){
        this.params = params;
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

    public Object getResult()throws Exception {
        try {
            futureTask.get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    public Integer getStatus()throws Exception {
        return status;
    }

    public void errorHandle(Exception e, List params){
        cancelHandle(params);
    }

    public void suspend(long millisecond)throws Exception {
        Thread.sleep(millisecond);
    }

    public void cancel(boolean flag){
        status = TaskStatus.CANCELLED;
        futureTask.cancel(flag);
        cancelHandle(params);
    }

    public abstract void cancelHandle(List params);

    public void start(){
        status = TaskStatus.COMPLETING;
        new Thread(this).start();
    }

    public void run() {
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
    }

    public void setParams(List params) {
        this.params = params;
    }

    public FutureTask getFutureTask() {
        return futureTask;
    }

    public void setFutureTask(FutureTask futureTask) {
        this.futureTask = futureTask;
    }
}
