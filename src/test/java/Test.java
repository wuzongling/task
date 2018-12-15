import factory.TreadTaskFactory;
import interf.ITaskGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: zonglin_wu
 * @Date: 2018/12/2 17:15
 * @Description:
 */
public class Test {

    public static void threadTaskTest(){
        List list = new ArrayList();
        TreadTaskTest treadTaskTest1 = new TreadTaskTest();
//        treadTaskTest1.setName("task1");
        TreadTaskTest treadTaskTest2 = new TreadTaskTest();
//        treadTaskTest2.setName("task2");
        ErrorTaskTest errorTaskTest = new ErrorTaskTest();
//        errorTaskTest.setName("task3");
        list.add(treadTaskTest1);
        list.add(treadTaskTest2);
        list.add(errorTaskTest);
        ITaskGroup taskGroup = TreadTaskFactory.buildTaskGroup(list,false);
        taskGroup.start();
        System.out.println("结果："+taskGroup.getResult(10000));
    }

    public static void main(String[] args) {
        threadTaskTest();
    }
}
