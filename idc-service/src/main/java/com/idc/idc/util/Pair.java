package com.idc.idc.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Pair<A extends Comparable<A>, B> implements Comparable<Pair<A, B>> {
    private A first;
    private B second;

    @Override
    public int compareTo(Pair<A, B> o) {
        return first.compareTo(o.first);
    }
}
