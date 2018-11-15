package com.idc.idc.util;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayDeque;
import java.util.Deque;

@Getter
@Setter
public class Direction {
    private int totalAmountOfSteps;
    private int currentStepInStep;
    private Deque<Pair<Long, Integer>> steps;

    public Direction() {
        steps = new ArrayDeque<>();
        currentStepInStep = 0;
    }

    public void addPolyline(Pair<Long, Integer> poly) {
        steps.add(poly);
        totalAmountOfSteps += poly.getSecond();
    }

    public Pair<Long, Integer> getCurrent() {
        return steps.getFirst();
    }

    public Pair<Long, Integer> popFirst() {
        return steps.pollFirst();
    }
}
