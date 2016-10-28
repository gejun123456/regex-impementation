package com.learn.regex.regexImplementation;

import java.util.Set;

/**
 * Created by bruce.ge on 2016/10/28.
 */
public class NFAforDFAStateInfo {
    private int dfsState;

    private Set<Integer> nfastates;

    public int getDfsState() {
        return dfsState;
    }

    public void setDfsState(int dfsState) {
        this.dfsState = dfsState;
    }

    public Set<Integer> getNfastates() {
        return nfastates;
    }

    public void setNfastates(Set<Integer> nfastates) {
        this.nfastates = nfastates;
    }
}
