package factory;

import interf.ITask;
import interf.ITaskGroup;
import interf.ParamSeparate;
import task.AbstractThreadTask;
import task.AbstractThreadTaskGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @Auther: zonglin_wu
 * @Date: 2018/12/2 16:27
 * @Description:
 */
public class TreadTaskFactory {

    public static ITaskGroup buildTaskGroup(List<ITask> list, ThreadPoolExecutor threadPoolExecutor,boolean synResult){
        ITaskGroup taskGroup = new AbstractThreadTaskGroup(threadPoolExecutor,synResult) {

            @Override
            public Object collectCalculate(List params) {
                System.out.println(params);
                return null;
            }
        };
        for (ITask task : list){
            taskGroup.addTask(task);
        }
        return taskGroup;
    }

}
