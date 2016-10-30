package com.learn.regex.regexImplementation.ob;

/**
 * Created by bruce.ge on 2016/10/30.
 */
//匹配普通字符 \* . a b  [ 等 只有在为[的时候有控制
public class WordContainer extends Container{
    protected char value;

    public WordContainer(char value){
        this.type = ContainerEnum.SIMPLEWORD;
        this.value = value;
    }

    public WordContainer(ContainerEnum type, char value){
        this.type =type;
        this.value= value;
    }

    public WordContainer(){}

    public char getValue() {
        return value;
    }

    public void setValue(char value) {
        this.value = value;
    }

}
