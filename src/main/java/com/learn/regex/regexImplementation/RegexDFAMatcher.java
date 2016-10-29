package com.learn.regex.regexImplementation;

import org.assertj.core.api.Assertions;

/**
 * Created by bruce.ge on 2016/10/28.
 */
public class RegexDFAMatcher {
    public static boolean match(String word,String pattern){
        DFAUnit dfa = DFAbuilder.createDFA(pattern);
        return true;
    }

    public static void main(String[] args) {
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
