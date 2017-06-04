package com.heu.cs.dao.userdao;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.heu.cs.pojo.UserPojo;

import java.util.List;
import java.util.Random;

/**
 * Created by memgq on 2017/5/21.
 */
public class FormatUserData {

    private  final String PROJECT_URL="http://mengqipoet.cn:8080/userimage/";
    private final String preName="defaultavatar";
    private final String IMAGE_TYPE=".jpg";
    private final String[] NICKNAME={"2017爱你要去","早茶月光","、凭凑不齐","nI、唯一","爱与你同在",
            "傻的天真、","季节温暖眼瞳。","、 荼靡","丶失恋的感觉","゛抹去最后一丝自尊","失心疯,怎么了",
            "半夏微凉。","伱在我心里╭ァ","潮流凯子钓裸女；","偏执怪人","°彼此共存°","眼睛想旅行",
            "囚我心虐我身","?情归于尽","守候的裂痕","敷衍”彡","爱情才是奢侈品（man）。","我可以忘记你",
            "-我爱你的奋不顾身","把你藏心里","时光浮夸，乱了遍地流年","我不愿让你一个人。","随梦而飞",
            "所有的深爱都是秘密","喂；丫头你是我的了","人前显贵人后受罪。","夏日倾情","乏味的人生ソ",
            "痴人痴心终是一场梦","让寂寞别走","离开你并非我愿意","回不去的时光","情绪疯子.","陪着烟消遣",
            "繁华年间〃谁许我一生|","睡衣男孩","你要懂得欺骗自己","◆失心虐-Ⅱ/pz","都扔了知道就好",
            "我给你的爱情难道不够、","一闪一闪亮光头?","MC’日月星辰","子弟的化身丶","你说过,我信过",
            "嗯，那又如何","い遥远了清晰的爱","リ丶丶灬尛坏坏＂","恨你你兴奋了是吗","唯我独尊","男人酒女人泪",
            "抽烟ゞ只为麻痹我自己","花花世界，何必当真","中毒的爱情","相濡以沫","带着春心找荡漾@",
            "梦想的天空格外蓝","爱原本应该能和被爱对等","?痞子时代","风的季节^^","这一刻，Love吧！",
            "挥别错的，才能和对的相逢","胖子就是矯情","彼岸回忆","无奈的选择","__、凄凉′美","你有情我无意 #",
            "幼稚丶演绎了莪们的青春","╯仅冇旳姿态","矜持的小女人。","半樽寒月敬浮生","淡雅暮","盈盈眉眼惜流年",
            "流光秋思半年华","离别的车站","深秋的黎明","侵他城丿夺他人","曾经的我们·太天真",
            "伱旳心里没有莪旳位置、","相逢在雨中","我怎能不爱你","时光吹老了好少年//*","花花世界、浪荡人",
            "留住的要珍惜","你、猜卟透╮","阜城悲伤回忆","ミ天使づ守护者ミ","害怕听到答案",
            "繁华过后々曲终人散丶","本人单身，从不吭声。","乐此不疲baby·","经得住诱惑丶","是我给你自由过了火╮",
            "爷╮控霸一切ㄎ","无名份的浪漫","心如荒岛","不要说你可怜","．．°你就是哥的女人","像从未出现过那样@",
            "囚我到老","男Ren哭泣，不代表软弱","我放不过我自己","铅笔的彩色天空-","青春就得二着过~","海哭的声音",
            "毫无保留。","人在旅途","我恨我痴心","-占据你妈的女儿。","一分情九分性ゴ","└半边心┐","夜夜梦中见",
            "如果你不再回来","ζ??、魚忘七秒℡","爱我你怕了吗;","。戏丶丨子、?","相思都愿化成灰",
            "我們的約定丶還記得麽","如何如何又如何","我大嫂是我前女友","放棄是最好的解脫","我记住得你",
            "讨厌的雨天~","霸道小男人ゞ","公车上de慢嗨","╭今夜离你很近。","你要等等我","｀武林萌主、",
            "さ｀扑梦成空丶","时光浮夸，乱了遍地流年"};


    public UserPojo format(String telNumber) {
        UserPojo userPojo = new UserPojo();
        JsonObject jsonObject=new JsonObject();
        List<String> attrsList = userPojo.getAttributes();
        for (String s:attrsList){
            if(s.equals("telNumber")){
                jsonObject.addProperty(s,telNumber);
            }else if(s.equals("avatarPath")){
                jsonObject.addProperty(s,PROJECT_URL+getAvatarName());
            }else if(s.equals("role")){
                jsonObject.addProperty(s,"0");
            }else if(s.equals("nickName")){
                jsonObject.addProperty(s,getNickName());
            }else if(s.equals("gender")){
                jsonObject.addProperty(s,"0");
            }
            else {
                jsonObject.addProperty(s,"");
            }
        }
        Gson gson=new Gson();
        userPojo=gson.fromJson(jsonObject,UserPojo.class);
        return userPojo;
    }

    private String getNickName(){
        int length=NICKNAME.length;
        int  randomInt=Integer.parseInt(generateRandomInt(length-1,0));
        String nick=NICKNAME[randomInt];
        return nick;
    }

    public String generateRandomInt(int MAX,int MIN){
        Random random=new Random();
        int randomInt=random.nextInt(MAX)%(MAX-MIN+1)+MIN;
        return String.valueOf(randomInt);
    }

    private String getAvatarName(){
        String randomInt=generateRandomInt(75,1);
        String avatarName=preName+randomInt+IMAGE_TYPE;
        return avatarName;
    }

}
