package com.learn.regex.regexImplementation.ob;

/**
 * Created by bruce.ge on 2016/10/30.
 */
//match with {}
public class BigOperatorContainer extends OperatorContainer{
    private String bigValue;

    public BigOperatorContainer(char a,String bigValue) {
        this.type = ContainerEnum.BIG_OPERATOR;
        this.value =a;
        this.bigValue = bigValue;
    }

    public String getBigValue() {
        return bigValue;
    }

    public void setBigValue(String bigValue) {
        this.bigValue = bigValue;
    }
}
