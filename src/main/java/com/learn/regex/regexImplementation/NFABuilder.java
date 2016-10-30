package com.learn.regex.regexImplementation;

import com.learn.regex.regexImplementation.ob.*;

import java.util.*;

/**
 * Created by bruce.ge on 2016/10/28.
 */
public class NFABuilder {
    public static NFAUnit createNFA(String regex) {
        String backRegex = RegexMatchToBack.getBackRegex(regex);
        Stack<NFAUnit> unitStack = new Stack<NFAUnit>();
        int[] startState = new int[1];
        startState[0] = 1;
        for (int i = 0; i < backRegex.length(); i++) {
            char c = backRegex.charAt(i);
            if (!RegexOperator.isOperator(c)) {
                //create with default NFAUNIT.
                NFAUnit unit = new NFAUnit();
                unit.setStartState(startState[0]++);
                unit.setEndState(startState[0]++);
                unit.addPathToMap(unit.getStartState(), c, unit.getEndState());
                unitStack.push(unit);
            } else {
                if (RegexOperator.isLinkOperator(c)) {
                    NFAUnit a = unitStack.pop();
                    NFAUnit b = unitStack.pop();
                    NFAUnit with = buildLinkNFA(a, b, c, startState);
                    unitStack.push(with);
                } else if (RegexOperator.isSingleOperator(c)) {
                    NFAUnit a = unitStack.pop();
                    NFAUnit single = buildSingleNFA(a, c, startState);
                    unitStack.push(single);
                }
            }
        }
        return unitStack.pop();
    }

    public static NFAUnit createNFAByContainer(String regex) {
        List<Container> containers = RegexMatchToBack.getBackContainer(regex);
        Stack<NFAUnit> unitStack = new Stack<NFAUnit>();
        int[] startState = new int[1];
        startState[0] = 1;
        for (int i = 0; i < containers.size(); i++) {
            Container container = containers.get(i);
            switch (container.getType()) {
                case SIMPLEWORD:
                    WordContainer cc = (WordContainer) container;
                    NFAUnit unit = buildSimpleWordNFA(startState,cc.getValue());
                    unitStack.push(unit);
                    break;
                case ESCAPEWORD:
                    WordContainer c2 = (WordContainer) container;
                    NFAUnit unit1 = buildEscapeNFA(startState,c2.getValue());
                    unitStack.push(unit1);
                    break;
                case ALLMATCHWORD:
                    NFAUnit unit2 = buildAllMatch(startState);
                    unitStack.push(unit2);
                    break;
                case MIDMATCHWORD:
                    //todo get it done need test.
                    MidWordContainer midWord=(MidWordContainer) container;
                    NFAUnit unit3 = buildMidNFA(startState,midWord.getValue());
                    unitStack.push(unit3);
                    break;
                case OPERATOR:
                    OperatorContainer op = (OperatorContainer) container;
                    if (RegexOperator.isLinkOperator(op.getValue())) {
                        NFAUnit a = unitStack.pop();
                        NFAUnit b = unitStack.pop();
                        NFAUnit with = buildLinkNFA(a, b, op.getValue(), startState);
                        unitStack.push(with);
                    } else if (RegexOperator.isSingleOperator(op.getValue())) {
                        NFAUnit a = unitStack.pop();
                        NFAUnit single = buildSingleNFA(a, op.getValue(), startState);
                        unitStack.push(single);
                    }
                    break;
                //todo  need to build. not easy to build with.
                case BIG_OPERATOR:
                    BigOperatorContainer dd = (BigOperatorContainer) container;
                    NFAUnit pop = unitStack.pop();
                    NFAUnit unit4 = buildByBigOperator(dd.getBigValue(),pop,startState);
                    unitStack.push(unit4);
                    break;
            }
        }
        return unitStack.pop();
    }

    //todo need test deep with it.
    private static NFAUnit buildByBigOperator(String bigValue, NFAUnit pop,int[] statedState) {
//        return null;
        //need to copy one out.
        String[] split = bigValue.split(",");
        if (split.length==1){
            Integer num = Integer.parseInt(split[0]);
            NFAUnit b;
            NFAUnit a = pop;
            for (int i = 0; i < num-1; i++) {
                b = buildSame(pop,statedState);
                a = buildLinkNFA(a,b,'&',statedState);
            }
            return a;
        } else {
            int start = Integer.parseInt(split[0]);
            int end = Integer.parseInt(split[1]);
            NFAUnit b;
            NFAUnit a = pop;
            for (int i = 0; i < start-1; i++) {
                b = buildSame(pop,statedState);
                a = buildLinkNFA(a,b,'&',statedState);
            }

            //之后要通过活来搞
            NFAUnit g = buildEmpty(statedState);
            for (int i = 0; i < end-start; i++) {
                NFAUnit next = buildSame(pop,statedState);
                //这样即可。
                for (int j =0; j < i; j++) {
                    NFAUnit u  =buildSame(pop,statedState);
                    next = buildLinkNFA(next,u,'&',statedState);
                }
                g = buildLinkNFA(g,next,'|',statedState);
            }
            return buildLinkNFA(a,g,'&',statedState);
        }
    }

    private static NFAUnit buildEmpty(int[] statedState) {
        NFAUnit g = new NFAUnit();
        g.setStartState(statedState[0]++);
        g.setEndState(statedState[0]++);
        g.addPathToMap(g.getStartState(), OpConstants.EMPTY,g.getEndState());
        return g;
    }

    private static NFAUnit buildSame(NFAUnit pop,int[] startedState) {
        Map<Integer,Integer> reflectMap = new HashMap<Integer, Integer>();
        NFAUnit unit = new NFAUnit();
        unit.setStartState(startedState[0]++);
        unit.setEndState(startedState[0]++);
        reflectMap.put(pop.getStartState(),unit.getStartState());
        reflectMap.put(pop.getEndState(),unit.getEndState());
        Map<Integer, Map<Character, List<Integer>>> jumpMap = pop.getJumpMap();
        for(Integer state : jumpMap.keySet()){
            if(!reflectMap.containsKey(state)){
                reflectMap.put(state,startedState[0]++);
            }
            for(Character key : jumpMap.get(state).keySet()){
                for (int i = 0; i < jumpMap.get(state).get(key).size(); i++) {
                    if(!reflectMap.containsKey(jumpMap.get(state).get(key).get(i))){
                        reflectMap.put(jumpMap.get(state).get(key).get(i),startedState[0]++);
                    }
                    unit.addPathToMap(reflectMap.get(state),key,reflectMap.get(jumpMap.get(state).get(key).get(i)));
                }
            }
        }
        return unit;
    }

    private static NFAUnit buildMidNFA(int[] startState, String midWord) {
        Set<Character> charSets = new HashSet<Character>();
        for (int i = 0; i < midWord.length(); i++) {
            char c = midWord.charAt(i);
            if(c!='-'){
                charSets.add(c);
            } else {
                char b = midWord.charAt(i-1);
                char d = midWord.charAt(++i);
                for (char j = b; j < d; j++) {
                    charSets.add(j);
                }
            }
        }
        //get it.
        NFAUnit unit = new NFAUnit();
        unit.setStartState(startState[0]++);
        unit.setEndState(startState[0]++);
        for(Character c: charSets){
            unit.addPathToMap(unit.getStartState(),c,unit.getEndState());
        }
        return unit;
    }

    //实现了 . 符号的匹配
    private static NFAUnit buildAllMatch(int[] startState) {
        NFAUnit unit = new NFAUnit();
        unit.setStartState(startState[0]++);
        unit.setEndState(startState[0]++);
        for (char i = 'a'; i <='z'; i++) {
            unit.addPathToMap(unit.getStartState(),i,unit.getEndState());
        }

        for (char i = 'A'; i <='Z'; i++) {
            unit.addPathToMap(unit.getStartState(),i,unit.getEndState());
        }
        for (char i = '0'; i <='9'; i++) {
            unit.addPathToMap(unit.getStartState(),i,unit.getEndState());
        }
        return unit;
    }

    //实现转义字符的匹配
    private static NFAUnit buildEscapeNFA(int[] startState, char value) {
        if(value=='d'){
            NFAUnit unit = new NFAUnit();
            unit.setStartState(startState[0]++);
            unit.setEndState(startState[0]++);
            for (char i = '0'; i <='9'; i++) {
                unit.addPathToMap(unit.getStartState(),i,unit.getEndState());
            }
            return unit;
        } else if(value=='w'){
            NFAUnit unit = new NFAUnit();
            unit.setStartState(startState[0]++);
            unit.setEndState(startState[0]++);
            for (char i = 'a'; i <='z'; i++) {
                unit.addPathToMap(unit.getStartState(),i,unit.getEndState());
            }

            for (char i = 'A'; i <='Z'; i++) {
                unit.addPathToMap(unit.getStartState(),i,unit.getEndState());
            }
            return unit;
        } else {
            if(value=='t'){
                value = '\t';
            } else if(value=='n'){
                value = '\n';
            }
            return buildSimpleWordNFA(startState,value);
        }

    }

    private static NFAUnit buildSimpleWordNFA(int[] startState, char cc) {
        NFAUnit unit = new NFAUnit();
        unit.setStartState(startState[0]++);
        unit.setEndState(startState[0]++);
        unit.addPathToMap(unit.getStartState(),  cc,unit.getEndState());
        return unit;
    }

    //todo if need to use new instance.
    //if need to create new object for search.
    //实现了?号和+号 每次返回一个实例的好处
    private static NFAUnit buildSingleNFA(NFAUnit a, char c, int[] startState) {
        NFAUnit unit = new NFAUnit();
        if (c == '*') {
            unit.setStartState(startState[0]++);
            unit.setEndState(startState[0]++);
            unit.buildMap(a);
            unit.addPathToMap(unit.getStartState(), OpConstants.EMPTY, a.getStartState());
            unit.addPathToMap(unit.getStartState(), OpConstants.EMPTY, unit.getEndState());
            unit.addPathToMap(a.getEndState(), OpConstants.EMPTY, unit.getEndState());
            unit.addPathToMap(a.getEndState(), OpConstants.EMPTY, a.getStartState());
        } else if (c == '?') {
            a.addPathToMap(a.getStartState(), OpConstants.EMPTY, a.getEndState());
            return a;
        } else if (c == '+') {
            return buildLinkNFA(a, buildSingleNFA(a, '*', startState), '&', startState);
        }
        return unit;
    }

    private static NFAUnit buildLinkNFA(NFAUnit a, NFAUnit b, char c, int[] startState) {
        NFAUnit unit = new NFAUnit();
        if (c == '|') {
            unit.setStartState(startState[0]++);
            unit.setEndState(startState[0]++);
            unit.buildMap(a, b);
            unit.addPathToMap(unit.getStartState(), OpConstants.EMPTY, a.getStartState());
            unit.addPathToMap(unit.getStartState(), OpConstants.EMPTY, b.getStartState());
            unit.addPathToMap(a.getEndState(), OpConstants.EMPTY, unit.getEndState());
            unit.addPathToMap(b.getEndState(), OpConstants.EMPTY, unit.getEndState());
        } else if (c == '&') {
            unit.setStartState(a.getStartState());
            unit.buildMap(a, b);
            unit.setEndState(b.getEndState());
            unit.addPathToMap(a.getEndState(), OpConstants.EMPTY, b.getStartState());
        }
        return unit;
    }

    public static void main(String[] args) {
    }
}
