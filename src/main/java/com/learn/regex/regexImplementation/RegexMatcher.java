package com.learn.regex.regexImplementation;

import org.assertj.core.api.Assertions;

import java.util.*;

/**
 * Created by bruce.ge on 2016/10/28.
 */
public class RegexMatcher {
    public static boolean match(String word,String pattern){
        NFAUnit nfa = NFABuilder.createNFAByContainer(pattern);
        //shall to match with things.
        //first find all started state.
        int startState = nfa.getStartState();
        //find $ startStates.
        // if contain end state that cool.
        //find start all E path. find every thing.
        Map<Integer,List<Integer>> equalMap = new HashMap<Integer, List<Integer>>();
        List<Integer> startStates = collect(equalMap,startState,nfa);
        List<Integer> midsates = startStates;
        for (int i = 0; i < word.length();i++){
            List<Integer> gotoStates = new ArrayList<Integer>();
            for (int j = 0; j < midsates.size(); j++) {
                StateRoute endStates = nfa.getEndStateByStateAndC(midsates.get(j), word.charAt(i));
                if(endStates!=null){
                    gotoStates.addAll(endStates.getRoutes());
                }
            }
            //说明没有匹配到
            if(gotoStates.size()==0){
                return false;
            }
            List<Integer> gotoEquals = new ArrayList<Integer>();
            for (int j = 0; j < gotoStates.size(); j++) {
                gotoEquals.addAll(collect(equalMap,gotoStates.get(j),nfa));
            }
            midsates=gotoEquals;
        }
        //最后判断midstate中是否有对应的最终状态
        if(midsates.contains(nfa.getEndState())){
            return true;
        }
        return false;
    }

    private static List<Integer> collect(Map<Integer, List<Integer>> equalMap, int startState,NFAUnit nfa) {
        if(equalMap.containsKey(startState)){
            return equalMap.get(startState);
        }
        StateRoute routes = nfa.getEndStateByStateAndC(startState, OpConstants.EMPTY);
        if(routes==null){
            equalMap.put(startState,Arrays.asList(startState));
        } else {
            List<Integer> allRoutes= new ArrayList<Integer>();
            allRoutes.add(startState);
            for (int i = 0; i < routes.getRoutes().size(); i++) {
                allRoutes.addAll(collect(equalMap,routes.getRoutes().get(i),nfa));
            }
            equalMap.put(startState,allRoutes);
        }
        return equalMap.get(startState);
    }

    //test passed.
    //however need to create more case.
    public static void main(String[] args) {
        Assertions.assertThat(true).isEqualTo(match("a","a"));
        Assertions.assertThat(true).isEqualTo(match("ab","ab"));
        Assertions.assertThat(true).isEqualTo(match("abcd","abcd"));
        Assertions.assertThat(true).isEqualTo(match("aa","(a|b)*"));
        Assertions.assertThat(true).isEqualTo(match("ab","a*b"));
        Assertions.assertThat(true).isEqualTo(match("bba","b*a*"));
        Assertions.assertThat(false).isEqualTo(match("ab","(a|b)(c|d)"));
        Assertions.assertThat(false).isEqualTo(match("abbb","abcd"));
        Assertions.assertThat(false).isEqualTo(match("abcd","ab|cd"));
        Assertions.assertThat(true).isEqualTo(match("ab","ab|cd"));
        Assertions.assertThat(true).isEqualTo(match("a","a?"));
        Assertions.assertThat(true).isEqualTo(match("a","aa?"));
        Assertions.assertThat(true).isEqualTo(match("aab","a+b"));
        Assertions.assertThat(false).isEqualTo(match("b","a+b"));
        Assertions.assertThat(false).isEqualTo(match("aaaaba","a+b"));
        Assertions.assertThat(true).isEqualTo(match("a*","a\\*"));
        Assertions.assertThat(false).isEqualTo(match("a*","\\*"));

        Assertions.assertThat(true).isEqualTo(match("c","[a-k]"));

        Assertions.assertThat(false).isEqualTo(match("l","[a-k]"));

        Assertions.assertThat(true).isEqualTo(match("aaa","a{3}"));
        Assertions.assertThat(false).isEqualTo(match("aaa","a{4}"));
        Assertions.assertThat(true).isEqualTo(match("aaaa","a{4}"));
        Assertions.assertThat(true).isEqualTo(match("aa","a{1,2}"));
        Assertions.assertThat(true).isEqualTo(match("aaaa","a{3,4}"));

        Assertions.assertThat(true).isEqualTo(match("aaa","a{3,4}"));
        Assertions.assertThat(false).isEqualTo(match("aaaaa","a{3,4}"));
        Assertions.assertThat(false).isEqualTo(match("aaa","a{4}"));

    }
}
