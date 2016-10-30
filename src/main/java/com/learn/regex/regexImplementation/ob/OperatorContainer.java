package com.learn.regex.regexImplementation.ob;

import com.learn.regex.regexImplementation.ob.Container;

/**
 * Created by bruce.ge on 2016/10/30.
 */
//作为操作符的容器  * | {
public class OperatorContainer extends Container {
    protected char value;

    public OperatorContainer(char value){
        this.type = ContainerEnum.OPERATOR;
        this.value = value;
    }

    public OperatorContainer(){}

    public char getValue() {
        return value;
    }

    public void setValue(char value) {
        this.value = value;
    }
}
