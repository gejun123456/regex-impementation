package com.learn.regex.regexImplementation.ob;

/**
 * Created by bruce.ge on 2016/10/30.
 */
public class Container {
    //当前是否是操作服 如* | { -等
    protected ContainerEnum type;

    public ContainerEnum getType() {
        return type;
    }

    public void setType(ContainerEnum type) {
        this.type = type;
    }
}
