package task;

import constant.EventType;
import constant.TaskStatus;
import exception.ThreadTaskException;
import factory.ThreadTaskEventFactory;
import interf.ITask;
import listener.EventObserver;
import listener.EventSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.ParamsUtil;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

/**
 * 线程任务基类
 *
 * @Auther: zonglin_wu
 * @Date: 2018/12/1 16:22
 * @Description:
 */
public abstract class AbstractThreadTask implements ITask, Callable {
    private static final Logger log = LoggerFactory.getLogger(AbstractThreadTask.class);
    //是否同步
    private boolean synResult = true;
    //执行参数
    private List params;

    private FutureTask futureTask = new FutureTask(this);
    //返回结果
    private Object result;

    private String name = getName();

    volatile int status = TaskStatus.NEW;

    //重试次数
    private int retriesCount = 0;

    //重试中间计算次数
    private int retriesModCount = 0;

    private boolean isErrorCall = true;

    private Map<Integer, EventSource> eventMap = null;

    public AbstractThreadTask(String name, List params) {
        this(name, params, false);
    }

    public AbstractThreadTask(String name, List params, boolean synResult) {
        init();
        this.params = params;
        this.synResult = synResult;
        this.name = name;
    }

    public AbstractThreadTask(String name) {
        this(name, null);
    }

    public AbstractThreadTask() {
        init();
    }

    protected void init() {
        if (eventMap == null) {
            eventMap = new ConcurrentHashMap<>();
        }
        if (eventMap.size() == 0) {
            //初始化事件
            EventSource completeEvent = ThreadTaskEventFactory.buildEvent(EventType.NORMAL_EVENT, this);
            EventSource errorEvent = ThreadTaskEventFactory.buildEvent(EventType.EXCEPTIONAL_EVENT, this);
            eventMap.put(completeEvent.getType(), completeEvent);
            eventMap.put(errorEvent.getType(), errorEvent);
        }
    }

    public String getName() {
        if (name == null || name.equals("")) {
            return Thread.currentThread().getName() + "_AbstractThreadTask";
        }
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void preHandle(List params) throws Exception {
        if (log.isInfoEnabled()){
            log.info(" {0}开始执行 params:{1} ",getName(),ParamsUtil.toParamsString(params));
        }
    }

    public Object postHandle(Object result, List params) throws Exception {
        if (log.isDebugEnabled()){
            log.debug(" {0}执行完成，执行结果：{1} ",getName(),ParamsUtil.toResultString(result));
        }
        return result;
    }

    public Object getResult(int millisecond) {
        try {
            if (synResult && result == null) {
                result = futureTask.get(millisecond, TimeUnit.MILLISECONDS);
            }
        } catch (Exception e) {
            throw new ThreadTaskException(e);
        }
        return result;
    }

    public Integer getStatus() {
        return status;
    }

    public void errorHandle(Exception e, List params) {
        cancel(true);
        if (isErrorCall) {
            errorCall(e, params);
        }
        status = TaskStatus.EXCEPTIONAL;
    }

    public void suspend(long millisecond) throws Exception {
        Thread.sleep(millisecond);
    }

    public void cancel(boolean flag) {
        status = TaskStatus.CANCELLED;
        futureTask.cancel(flag);
    }

    public void register(int type, EventObserver observer) {
        EventSource eventSource = eventMap.get(type);
        eventSource.addObserver(observer);
    }

    public abstract void errorCall(Exception e, List params);

    public void start() {
        status = TaskStatus.COMPLETING;
        new Thread(futureTask).start();
    }

    public Object call() {
        try {
            preHandle(params);
            result = excute(params);
            postHandle(result, params);
            status = TaskStatus.NORMAL;
            eventChange(EventType.NORMAL_EVENT);
        } catch (Exception e) {
            if(retriesModCount < retriesCount){
                retriesModCount++;
                log.info("第"+retriesModCount+"次重试");
                call();
            }else {
                log.error(getName() + "发生异常,params:" + params, e);
                errorHandle(e, params);
                status = TaskStatus.EXCEPTIONAL;
                eventChange(EventType.EXCEPTIONAL_EVENT);
            }
        }finally {
            retriesModCount = 0;
        }
        return result;
    }

    /**
     * 事件通知
     * @param eventType
     */
    private void eventChange(final int eventType) {
        new Thread(() -> {
            EventSource eventSource = eventMap.get(eventType);
            eventSource.change();
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

    public int getRetriesCount() {
        return retriesCount;
    }

    public void setRetriesCount(int retriesCount) {
        this.retriesCount = retriesCount;
    }
}
