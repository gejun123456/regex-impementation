package com.learn.regex.regexImplementation.ob;

import com.learn.regex.regexImplementation.ob.Container;

/**
 * Created by bruce.ge on 2016/10/30.
 */
//作为操作符的容器  * | {
public class OperatorContainer extends Container {
    private char value;

    private String specialValue;

    public char getValue() {
        return value;
    }

    public void setValue(char value) {
        this.value = value;
    }

    public String getSpecialValue() {
        return specialValue;
    }

    public void setSpecialValue(String specialValue) {
        this.specialValue = specialValue;
    }

    public boolean isSpecialValue(char a){
        return a=='{';
    }
}
