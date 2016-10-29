package com.learn.regex.regexImplementation;

import org.assertj.core.api.Assertions;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by bruce.ge on 2016/10/29.
 */
/*
* 正则表达式的预处理
* 将[] {} + 符号处理成| * 结合符
* 方便后续处理
* */
public class RegexPreStringFormatter {
    public static String prehandle(String regex){
        return "";
    }


    //写好case的控制
    public static void main(String[] args) {
        //是否交给nfa来控制比较好。 +号和?号
        assertThat("aa*").isEqualTo(prehandle("a+"));

    }
}
