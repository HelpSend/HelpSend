package com.heu.cs.timertask;

import com.heu.cs.pojo.OrderPojo;
import javax.servlet.ServletContext;
import java.util.List;
import java.util.TimerTask;
import static com.heu.cs.timertask.Factory.groupGrabOrder;

/**
 * Created by memgq on 2017/6/1.
 */
public class MyTask extends TimerTask {
    private static boolean isRunning = false;

    public List<OrderPojo> getOrderPojoList() {
        return orderPojoList;
    }

    public void setOrderPojoList(List<OrderPojo> orderPojoList) {
        this.orderPojoList = orderPojoList;
    }

    private List<OrderPojo> orderPojoList;

    private ServletContext context = null;

    public MyTask() {
        super();
    }

    public MyTask(ServletContext context) {
        this.context = context;
    }
    @Override
    public void run() {

        if (!isRunning) {

            context.log("开始执行指定任务");
            /**
             * 自己的业务代码
             */
            GrabOrderTask grabOrderTask=new GrabOrderTask();
            this.setOrderPojoList(grabOrderTask.getOrderList());
            groupGrabOrder.push("firstGrab",this.getOrderPojoList(),2);
            groupGrabOrder.push("secondGrab",this.getOrderPojoList(),3);
            isRunning = false;
            context.log("指定任务执行结束");

        } else {
            context.log("上一次任务执行还未结束");
        }
    }
}
