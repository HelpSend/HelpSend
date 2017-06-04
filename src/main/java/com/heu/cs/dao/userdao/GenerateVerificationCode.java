package com.heu.cs.dao.userdao;

import java.util.Random;

/**
 * Created by memgq on 2017/6/3.
 */
public class GenerateVerificationCode {
    private static final int MAX=9999;
    private static final int MIN=1001;
    public String generateCode(){
        Random random=new Random();
        int code=random.nextInt(MAX)%(MAX-MIN+1)+MIN;
        return String.valueOf(code);
    }
}
