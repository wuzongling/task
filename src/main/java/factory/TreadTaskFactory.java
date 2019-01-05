package factory;

import interf.Calculate;
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
                return null;
            }
        };
        for (ITask task : list){
            taskGroup.addTask(task);
        }
        return taskGroup;
    }

    public static ITaskGroup buildTaskGroup(List<ITask> list,boolean synResult){
        return buildTaskGroup(list,null,synResult);
    }

    public static ITaskGroup buildTaskGroup(List<ITask> list, boolean synResult, Calculate calculate){
        return buildTaskGroup(list,null,synResult,calculate);
    }

    public static ITaskGroup buildTaskGroup(List<ITask> list, ThreadPoolExecutor threadPoolExecutor, boolean synResult, Calculate calculate){
        ITaskGroup taskGroup = new AbstractThreadTaskGroup(threadPoolExecutor,synResult) {
            @Override
            public Object collectCalculate(List params) {
                return calculate.combineCalculate(params);
            }
        };
        for (ITask task : list){
            taskGroup.addTask(task);
        }
        return taskGroup;
    }

}
