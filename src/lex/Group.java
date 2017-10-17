package lex;

import java.util.Set;

/**
 * 该类是运用于DFA->DFAO的分组数据类
 */
class Group {
    public Set<Integer> states;
    public boolean isStrong;

    public Group(Set<Integer> states, boolean isStrong) {
        this.states = states;
        this.isStrong = isStrong;
    }

    @Override
    public String toString(){
        return states.toString();
    }
}
