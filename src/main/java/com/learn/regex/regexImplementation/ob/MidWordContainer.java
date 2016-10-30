package com.learn.regex.regexImplementation.ob;

/**
 * Created by bruce.ge on 2016/10/30.
 */
//match with []
public class MidWordContainer extends Container {
    private String value;

    public MidWordContainer(String value) {
        this.type = ContainerEnum.MIDMATCHWORD;
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
