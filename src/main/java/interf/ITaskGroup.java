package interf;

import java.util.List;

/**
 * @Auther: zonglin_wu
 * @Date: 2018/12/1 16:09
 * @Description:
 */
public interface ITaskGroup extends ITask{

    public boolean addTask(ITask task);

    public boolean removeTask(int i);

    public Object collectCalculate(List params);
}
