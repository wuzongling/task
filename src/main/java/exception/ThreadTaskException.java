package exception;

/**
 * @Auther: zonglin_wu
 * @Date: 2019/4/27 17:17
 * @Description:
 */
public class ThreadTaskException extends RuntimeException{

    private String msg;

    public ThreadTaskException(Exception e){
        super("线程任务执行失败",e);
    }

    public ThreadTaskException(String msg,Exception e){
        super(msg,e);
        setMsg(msg);
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
