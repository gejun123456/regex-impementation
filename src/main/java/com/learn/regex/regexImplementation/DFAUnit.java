package com.learn.regex.regexImplementation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by bruce.ge on 2016/10/28.
 */
public class DFAUnit {
    private int startState;

    private List<Integer> endState;

    private Map<Integer,Map<Character,Integer>> dfjump;

    public int getStartState() {
        return startState;
    }

    public void setStartState(int startState) {
        this.startState = startState;
    }

    public List<Integer> getEndState() {
        return endState;
    }

    public void setEndState(List<Integer> endState) {
        this.endState = endState;
    }

    public Map<Integer, Map<Character, Integer>> getDfjump() {
        return dfjump;
    }

    public void setDfjump(Map<Integer, Map<Character, Integer>> dfjump) {
        this.dfjump = dfjump;
    }

    public void addPath(int start,char c ,int end){
        if(dfjump==null){
            dfjump = new HashMap<Integer, Map<Character, Integer>>();
        }
        if(dfjump.get(start)==null){
            dfjump.put(start,new HashMap<Character, Integer>());
        }
        dfjump.get(start).put(c,end);
    }

    public void addEndState(Integer a){
        if(endState==null){
            endState=new ArrayList<Integer>();
        }
        endState.add(a);
    }
}
