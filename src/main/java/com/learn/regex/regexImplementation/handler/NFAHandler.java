package com.learn.regex.regexImplementation.handler;

import com.learn.regex.regexImplementation.NFAUnit;
import com.learn.regex.regexImplementation.ob.Container;

import java.util.Stack;

/**
 * Created by bruce.ge on 2016/10/30.
 */
/*handler to creat nfa. or fix.*/
public interface NFAHandler {
    void handle(Container co,Stack<NFAUnit> stack);
}
