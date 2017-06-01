package com.heu.cs.timertask;



import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by memgq on 2017/6/1.
 */
public class ContextListener implements ServletContextListener{
    public ContextListener() {
    }
    private java.util.Timer timer = null;
    /**
     * 初始化定时器
     * web 程序运行时候自动加载
     */
    @Override
    public void contextInitialized(ServletContextEvent arg0) {

        /**
         * 设置一个定时器
         */
        timer = new java.util.Timer(true);

      //  arg0.getServletContext().log("定时器已启动");

        /**
         * 定时器到指定的时间时,执行某个操作(如某个类,或方法)
         */


//        //设置执行时间
//        Calendar calendar = Calendar.getInstance();
//        int year = calendar.get(Calendar.YEAR);
//        int month = calendar.get(Calendar.MONTH);
//        int day =calendar.get(Calendar.DAY_OF_MONTH);//每天
//        //定制每天的1:00:00执行，
//        calendar.set(year, month, day, 01, 00, 00);
//        Date date = calendar.getTime();
//        System.out.println(date);

        int period = 1300;
        //每天的date时刻执行task，每隔persion 时间重复执行
        timer.schedule(new MyTask(arg0.getServletContext()), 0, period);
//        在 指定的date时刻执行task, 仅执行一次
//        timer.schedule(new DelFileTask(arg0.getServletContext()), date);


        //arg0.getServletContext().log("已经添加任务调度表");

    }

    /**
     * 销毁
     */
    @Override
    public void contextDestroyed(ServletContextEvent arg0){

        timer.cancel();
        arg0.getServletContext().log("定时器销毁");
    }
}
