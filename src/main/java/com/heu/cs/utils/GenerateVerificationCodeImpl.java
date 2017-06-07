package com.heu.cs.utils;

import java.util.Random;

/**
 * Created by memgq on 2017/6/3.
 */
public class GenerateVerificationCodeImpl implements GenerateVerificationCode {


    /**
     * 生成length位验证码(4-8)
     * @param length
     * @return
     */
    @Override
    public String generateCode(int length){

        String max_i="9";
        String min_i="0";
        String MAX="";
        String MIN="1";
        for(int i=0;i<length;i++){
             MAX+=max_i;
         }
         for(int i=0;i<length-1;i++){
            MIN+=min_i;
         }
        int MAXINT=Integer.parseInt(MAX);
        int MININT=Integer.parseInt(MIN);
        Random random=new Random();
        int code=random.nextInt(MAXINT)%(MAXINT-MININT+1)+MININT;
        return String.valueOf(code);
    }
}
