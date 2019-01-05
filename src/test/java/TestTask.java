import factory.TreadTaskFactory;
import interf.ITaskGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: zonglin_wu
 * @Date: 2018/12/2 17:15
 * @Description:
 */
public class TestTask {

    public static void threadTaskTest(){
        List paramList = new ArrayList();
        paramList.add(1);
        List list = new ArrayList();
        //需要继承AbstractThreadTask任务类
        TestTreadTask treadTaskTest1 = new TestTreadTask();
        treadTaskTest1.setParams(paramList);
        treadTaskTest1.setName("task1");
        TestTreadTask treadTaskTest2 = new TestTreadTask();
        treadTaskTest2.setName("task2");
        ErrorTaskTest errorTaskTest = new ErrorTaskTest();
        errorTaskTest.setName("task3");
        //设置重试次数
        errorTaskTest.setRetriesCount(2);
        list.add(treadTaskTest1);
        list.add(treadTaskTest2);
        list.add(errorTaskTest);
//        ITaskGroup taskGroup = TreadTaskFactory.buildTaskGroup(list,true);
        ITaskGroup taskGroup = TreadTaskFactory.buildTaskGroup(list,true,(params)->{
            System.out.println("合并计算");
            return null;
        });
        taskGroup.start();
        System.out.println("最后结果："+taskGroup.getResult(1000000));
    }

    public static void main(String[] args) {
        threadTaskTest();
    }
}
