package constant;

/**
 * @Auther: zonglin_wu
 * @Date: 2018/12/3 19:31
 * @Description:
 */
public class ListenerStatus {
    private ListenerStatus(){}
    //新建
    public static final int NEW = 0;
    //开始
    public static final int START = 1;
    //暂停
    public static final int SUSPEND = 2;
    //关闭
    public static final int CANCEL = 3;
    //异常
    public static final int EXCEPTION = 4;
}
