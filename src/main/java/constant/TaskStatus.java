package constant;

/**
 * @Auther: zonglin_wu
 * @Date: 2018/12/2 15:13
 * @Description:
 */
public class TaskStatus {
    private TaskStatus(){}
    //新建
    public static final int NEW          = 0;
    //执行中
    public static final int COMPLETING   = 1;
    //正常完成
    public static final int NORMAL       = 2;
    //异常
    public static final int EXCEPTIONAL  = 3;
    //关闭
    public static final int CANCELLED    = 4;
    public static final int INTERRUPTING = 5;
    public static final int INTERRUPTED  = 6;
}
