package com.learn.regex.regexImplementation;

import org.assertj.core.api.Assertions;

/**
 * Created by bruce.ge on 2016/10/28.
 */
public class RegexDFAMatcher {
    public static boolean match(String word,String pattern){
        //生成最终的dfa图
        DFAUnit dfa = DFAbuilder.createDFA(pattern);
        int state = dfa.getStartState();
        for (int i = 0; i < word.length(); i++) {
            if(dfa.getDfjump()!=null&&dfa.getDfjump().get(state)!=null&&dfa.getDfjump().get(state).get(word.charAt(i))!=null) {
                state = dfa.getDfjump().get(state).get(word.charAt(i));
            } else {
                return false;
            }
        }
        return dfa.isEndStates(state);
    }
    //先生成dfa 再进行匹配 一个正则需要使用匹配多次的情况
    public static DFAUnit compile(String pattern){
        DFAUnit dfa = DFAbuilder.createDFA(pattern);
        return dfa;
    }

    public static boolean matchByDFA(String word,DFAUnit dfa){
        int state = dfa.getStartState();
        for (int i = 0; i < word.length(); i++) {
            if(dfa.getDfjump()!=null&&dfa.getDfjump().get(state)!=null&&dfa.getDfjump().get(state).get(word.charAt(i))!=null) {
                state = dfa.getDfjump().get(state).get(word.charAt(i));
            } else {
                return false;
            }
        }
        return dfa.isEndStates(state);
    }

    public static void main(String[] args) {
        Assertions.assertThat(true).isEqualTo(match("a","a"));
        System.out.println("haha");
        Assertions.assertThat(true).isEqualTo(match("ab","ab"));
        Assertions.assertThat(true).isEqualTo(match("abcd","abcd"));
        Assertions.assertThat(true).isEqualTo(match("aa","(a|b)*"));
        Assertions.assertThat(true).isEqualTo(match("ab","a*b"));
        Assertions.assertThat(true).isEqualTo(match("bba","b*a*"));
        Assertions.assertThat(false).isEqualTo(match("ab","(a|b)(c|d)"));
        Assertions.assertThat(false).isEqualTo(match("abbb","abcd"));
        Assertions.assertThat(false).isEqualTo(match("abcd","ab|cd"));
        Assertions.assertThat(true).isEqualTo(match("ab","ab|cd"));
    }
}
