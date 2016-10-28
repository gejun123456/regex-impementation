package com.learn.regex.regexImplementation;

import java.util.*;

/**
 * Created by bruce.ge on 2016/10/28.
 */

//need check with the code.
public class DFAbuilder {

    public DFAUnit createDFA(String regex){
        NFAUnit nfa = NFABuilder.createNFA(regex);
        //use with nfa to build the dfa.
        int startState = nfa.getStartState();
        int[] dfastart = new int[1];
        dfastart[0]=1;
        Map<Integer,Set<Integer>> dfsMappingStates = new HashMap<Integer, Set<Integer>>();
        Map<Integer,Set<Integer>> equalStates = new HashMap<Integer, Set<Integer>>();
        Set<Integer> starts = collect(equalStates,startState,nfa);
        //starts为第一个状态。
        dfsMappingStates.put(dfastart[0],starts);
        DFAUnit dfaUnit = new DFAUnit();
        dfaUnit.setStartState(dfastart[0]);
        Queue<NFAforDFAStateInfo> stateInfos = new LinkedList<NFAforDFAStateInfo>();
        NFAforDFAStateInfo info = new NFAforDFAStateInfo();
        info.setDfsState(dfastart[0]);
        info.setNfastates(starts);
        dfastart[0]++;
        while(!stateInfos.isEmpty()){
            NFAforDFAStateInfo poll = stateInfos.poll();
            Set<Integer> nfastates = poll.getNfastates();
            //查看总共有哪些可用的字符
            Set<Character> usedChar = getCharFrom(nfastates,nfa);
            if(usedChar.size()==0){
                continue;
            }
            //for every character let do the thing.
            for(Character c : usedChar){
                //try find the end states.
                Set<Integer> states = new HashSet<Integer>();
                for (Integer state : nfastates){
                    StateRoute endRoute = nfa.getEndStateByStateAndC(state, c);
                    if(endRoute !=null){
                        states.addAll(endRoute.getRoutes());
                    }
                }
                if(states.size()>0){
                    Set<Integer> finalStates = buildFinal(equalStates,states,nfa);
                    // check if map contain such value.
                    //if not have such value.
                    int check = checkMapContain(finalStates,dfsMappingStates);
                    //return it's mapping value.
                    if(check==-1) {
                        dfsMappingStates.put(dfastart[0], finalStates);
                        NFAforDFAStateInfo curInfo = new NFAforDFAStateInfo();
                        curInfo.setNfastates(finalStates);
                        curInfo.setDfsState(dfastart[0]);
                        stateInfos.add(curInfo);
                        dfaUnit.addPath(poll.getDfsState(), c, curInfo.getDfsState());
                        if (finalStates.contains(nfa.getEndState())) {
                            dfaUnit.addEndState(curInfo.getDfsState());
                        }
                        dfastart[0]++;
                    } else {
                        //说明已经有了
                        dfaUnit.addPath(poll.getDfsState(),c,check);
                    }
                }
            }
        }
        return dfaUnit;
    }

    //this will check wether the new states is in past lists.
    private int checkMapContain(Set<Integer> finalStates, Map<Integer, Set<Integer>> dfsMappingStates) {
        int i = -1;
        for(Integer u : dfsMappingStates.keySet()){
            i = u;
            Set<Integer> ff = dfsMappingStates.get(u);
            for(Integer m : finalStates){
                if(!ff.contains(m)){
                    i = -1;
                    break;
                }
            }
        }
        return i;
    }

    private Set<Integer> buildFinal(Map<Integer, Set<Integer>> equalStates, Set<Integer> states, NFAUnit nfa) {
        Set<Integer> ss = new HashSet<Integer>();
        for (Integer state : states){
            ss.addAll(collect(equalStates,state,nfa));
        }
        return ss;
    }

    private Set<Character> getCharFrom(Set<Integer> nfastates, NFAUnit nfa) {
        Set<Character> useChars = new HashSet<Character>();
        if(nfa.getJumpMap()==null){
            return useChars;
        }
        for(Integer state : nfastates){
            Map<Character, List<Integer>> characterListMap = nfa.getJumpMap().get(state);
            if(characterListMap!=null){
                for(Character c:characterListMap.keySet()){
                    if(c!='Ø'){
                        useChars.add(c);
                    }
                }
            }
        }
        return useChars;
    }

    //拿到一个state所有可以通过空运算符得到的状态
    private Set<Integer> collect(Map<Integer, Set<Integer>> equalStates, int startState, NFAUnit nfa) {
        if(equalStates.containsKey(startState)){
            return equalStates.get(startState);
        }
        char c = 'Ø';
        StateRoute endStates = nfa.getEndStateByStateAndC(startState, c);
        if(endStates==null){
            //将自己放进去
            Set<Integer> s = new HashSet<Integer>();
            s.add(startState);
            equalStates.put(startState, s);
        } else {
            Set<Integer> allRoutes = new HashSet<Integer>();
            for(Integer states : endStates.getRoutes()){
                allRoutes.addAll(collect(equalStates,states,nfa));
            }
            allRoutes.add(startState);
            equalStates.put(startState,allRoutes);
        }
        return equalStates.get(startState);
    }
}
