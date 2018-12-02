import factory.TreadTaskFactory;

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
        TreadTaskTest treadTaskTest2 = new TreadTaskTest();
        TreadTaskTest treadTaskTest3 = new TreadTaskTest();
        list.add(treadTaskTest1);
//        list.add(treadTaskTest2);
//        list.add(treadTaskTest3);
        TreadTaskFactory.buildTaskGroup(list,null,false).start();
    }

    public static void main(String[] args) {
        threadTaskTest();
    }
}
