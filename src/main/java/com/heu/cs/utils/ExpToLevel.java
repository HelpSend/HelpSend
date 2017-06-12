package com.heu.cs.utils;

import java.math.BigDecimal;
import java.text.NumberFormat;

/**
 * Created by memgq on 2017/6/12.
 */
public class ExpToLevel {
    public String[] expToLevel(String exp){
        String[] result=new String[2];
        String percent="0";
        int level=1;
        for(int i=1;i>0;i++){
            int total=levelToExp(i);
            if(Integer.parseInt(exp)>total){

            }else {
                level=i-1;
                NumberFormat numberFormat = NumberFormat.getInstance();
                // 设置精确到小数点后2位
                numberFormat.setMaximumFractionDigits(1);
                percent = numberFormat.format((( Double.parseDouble(exp) - Double.parseDouble(String.valueOf(levelToExp(i - 1)))) / Double.parseDouble(String.valueOf(total-levelToExp(i-1) ))) * 100);

                break;
            }
         }
        result[0]=String.valueOf(level);
        result[1]=percent;
        return result;
    }




    public int levelToExp(int level){
        int total=0;
        for(int i=1;i<level;i++){
            total+=((20 * (Math.pow(i, 3) + (5 * i))) - 80);
        }
        return total;
    }




}
