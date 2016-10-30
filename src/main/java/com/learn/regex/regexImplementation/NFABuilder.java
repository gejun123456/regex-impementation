package com.learn.regex.regexImplementation;

import com.learn.regex.regexImplementation.ob.Container;
import com.learn.regex.regexImplementation.ob.OperatorContainer;
import com.learn.regex.regexImplementation.ob.WordContainer;

import java.util.List;
import java.util.Stack;

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
                    NFAUnit unit = new NFAUnit();
                    unit.setStartState(startState[0]++);
                    unit.setEndState(startState[0]++);
                    unit.addPathToMap(unit.getStartState(), cc.getValue(), unit.getEndState());
                    unitStack.push(unit);
                    break;
                case ESCAPEWORD:

                    break;
                case ALLMATCHWORD:
                    break;
                case MIDMATCHWORD:
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
                case BIG_OPERATOR:
                    break;
            }
        }
        return unitStack.pop();
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
            return buildLinkNFA(a, buildSingleNFA(a, '*', startState), '-', startState);
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
        } else if (c == '-') {
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
