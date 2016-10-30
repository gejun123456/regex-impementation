package com.learn.regex.regexImplementation.handler;

import com.learn.regex.regexImplementation.NFAUnit;
import com.learn.regex.regexImplementation.ob.Container;
import com.learn.regex.regexImplementation.ob.OperatorContainer;

import java.util.Stack;

/**
 * Created by bruce.ge on 2016/10/30.
 */
//operator此处包含 * + | 有不同的处理
public class OperatorHandler implements NFAHandler{
    public void handle(Container co, Stack<NFAUnit> stack) {
        OperatorContainer op = (OperatorContainer) co;
        if(op.getValue()=='*'){
            NFAUnit nfa = stack.pop();

        }

    }
}
