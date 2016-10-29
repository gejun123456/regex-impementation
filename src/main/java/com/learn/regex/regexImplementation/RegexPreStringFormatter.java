package com.learn.regex.regexImplementation;

import org.assertj.core.api.Assertions;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by bruce.ge on 2016/10/29.
 */
/*
* 正则表达式的预处理
* 将[] {}  符号处理成| * 结合符
* 方便后续处理
* */
public class RegexPreStringFormatter {
    public static String prehandle(String regex){
        StringBuilder hadleRex = new StringBuilder();

        return "";
    }


    //写好case的控制 //只处理.号的生成
    //need to support with [] and {}.
    public static void main(String[] args) {
        Assertions.assertThat("(a|b|c|d|e|f|g|h|i|j|k|l|m|n|o|p|q|r|s|t|u|v|w|x|y|z)").isEqualTo(prehandle("."));

    }
}
