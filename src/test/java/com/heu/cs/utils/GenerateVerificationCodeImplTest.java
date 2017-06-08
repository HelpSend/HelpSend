package com.heu.cs.utils;


import org.junit.Test;

import java.net.URLDecoder;

/**
 * Created by memgq on 2017/6/6.
 */

public class GenerateVerificationCodeImplTest {
    @Test
    public void generateCode() throws Exception {
        String s="%7B%22id%22%3A%22b8b4f12ec2f945ba8b3b7bd46d19d7a3%22%2C%22mobile%22%3A%2218045187467%22%2C%22text%22%3A%227288%22%2C%22reply_time%22%3A%222017-06-06+20%3A45%3A56%22%2C%22extend%22%3A%22%22%2C%22base_extend%22%3A%22506675%22%2C%22_sign%22%3A%22de7c5fc0aee943a849ab73260278810a%22%7D";
        String d=URLDecoder.decode(s,"UTF-8");
        System.out.println(d);
        String x="%257B%2522id%2522%253A%2522b8b4f12ec2f945ba8b3b7bd46d19d7a3%2522%252C%2522mobile%2522%253A%252218045187467%2522%252C%2522text%2522%253A%25227288%2522%252C%2522reply_time%2522%253A%25222017-06-06%2B20%253A45%253A56%2522%252C%2522extend%2522%253A%2522%2522%252C%2522base_extend%2522%253A%2522506675%2522%252C%2522_sign%2522%253A%2522de7c5fc0aee943a849ab73260278810a%2522%257D";
        String c=URLDecoder.decode(s,"UTF-8");
        System.out.println(c);
    }



}
