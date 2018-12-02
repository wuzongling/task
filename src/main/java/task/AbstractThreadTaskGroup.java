package task;

import interf.ITask;
import interf.ITaskGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * @Auther: zonglin_wu
 * @Date: 2018/12/1 16:51
 * @Description:
 */
public abstract class AbstractThreadTaskGroup extends AbstractThreadTask implements ITaskGroup,Runnable{
    private ArrayList<AbstractThreadTask> taskList = new ArrayList();

    private ThreadPoolExecutor threadPoolExecutor;

    private boolean synResult = false;


    public AbstractThreadTaskGroup(ThreadPoolExecutor threadPoolExecutor){
        this.threadPoolExecutor = threadPoolExecutor;
    }

    public AbstractThreadTaskGroup(ThreadPoolExecutor threadPoolExecutor,boolean synResult){
        this.threadPoolExecutor = threadPoolExecutor;
        this.synResult = synResult;
    }

    public AbstractThreadTaskGroup(){

    }
    public boolean addTask(ITask task) {
        taskList.add((AbstractThreadTask)task);
        return true;
    }

    public boolean removeTask(int i) {
        taskList.remove(i);
        return true;
    }

    public abstract Object collectCalculate(ArrayList params);

    @Override
    public String getName() {
        return Thread.currentThread().getName()+"_AbstractThreadTaskGroup";
    }

    @Override
    public Object excute(List params) throws Exception {
        threadPoolExecutor = getThreadPoolExecutor();
        ArrayList result = new ArrayList();
        for(AbstractThreadTask task : taskList){
            threadPoolExecutor.execute(task);
            Object param = task.getResult();
            result.add(param);
        }
        collectCalculate(result);
        return null;
    }

    public Object postHandle(Object result, ArrayList params) throws Exception {
        return null;
    }

    @Override
    public Object getResult() throws Exception {
        ArrayList result = new ArrayList();
        for (ITask task : taskList){
            Object param = task.getResult();
            result.add(param);
        }
        return result;
    }

    public void errorHandle(Exception e, ArrayList params) {
        cancel(true);
    }

    public void start() {
        if(synResult){
            new Thread(this).start();
        }else {
            try {
                excute(null);
            } catch (Exception e) {
                errorHandle(e,null);
            }
        }
    }

    public void suspend(long millisecond) throws Exception {

    }

    public void cancel(boolean flag) {
        for(ITask task : taskList){
            task.cancel(flag);
        }
        taskList = null;
    }

    @Override
    public void cancelHandle(List params) {

    }

    public ThreadPoolExecutor getThreadPoolExecutor(){
        if(threadPoolExecutor == null){
            return new ThreadPoolExecutor(8, 8,
                    0L, TimeUnit.MILLISECONDS,
                    new LinkedBlockingQueue<Runnable>());
        }
        return threadPoolExecutor;
    }

    public void run() {
        try {
            excute(null);
        } catch (Exception e) {
            errorHandle(e,null);
        }
    }
}
