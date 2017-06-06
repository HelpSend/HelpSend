package com.heu.cs.generalmethod;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;

/**
 * Created by memgq on 2017/6/6.
 */
public interface SMSApiInterface {

    String getUserInfo() throws IOException, URISyntaxException;

    String sendSms(String text, String mobile) throws IOException;

    String sendVoice(String apikey, String mobile, String code);

    String bindCall(String apikey, String from, String to, Integer duration);

    String unbindCall(String apikey, String from, String to);

    String post(String url, Map<String, String> paramsMap);
}
