package com.livingworld.smartnpc.common.ai;

import java.util.*;

/**
 * GOAP (Goal-Oriented Action Planning).
 * Планировщик: из текущего состояния ищет цепочку действий до цели.
 * Чистая логика, не зависит от Minecraft.
 */
public class GOAPPlanner {

    public static class State {
        public final Map<String, Boolean> facts = new HashMap<>();
        public State copy() {
            State s = new State();
            s.facts.putAll(facts);
            return s;
        }
        public boolean get(String key) { return facts.getOrDefault(key, false); }
        public void set(String key, boolean value) { facts.put(key, value); }
    }

    public static class Action {
        public final String name;
        public final Map<String, Boolean> preconditions;
        public final Map<String, Boolean> effects;
        public final double cost;

        public Action(String name, Map<String, Boolean> pre, Map<String, Boolean> eff, double cost) {
            this.name = name;
            this.preconditions = pre;
            this.effects = eff;
            this.cost = cost;
        }
    }

    public static class Goal {
        public final String name;
        public final Map<String, Boolean> desiredState;
        public Goal(String name, Map<String, Boolean> desired) {
            this.name = name;
            this.desiredState = desired;
        }
    }

    /**
     * Планирование: возвращает список действий или пустой список, если цель недостижима.
     */
    public List<Action> plan(State current, Goal goal, List<Action> actions) {
        // Простой BFS по состояниям (для малого числа действий)
        Queue<State> queue = new LinkedList<>();
        Map<State, List<Action>> cameFrom = new HashMap<>();
        queue.add(current);
        cameFrom.put(current, new ArrayList<>());

        while (!queue.isEmpty()) {
            State state = queue.poll();
            List<Action> path = cameFrom.get(state);

            if (isGoalSatisfied(state, goal)) {
                return path;
            }

            for (Action action : actions) {
                if (!isApplicable(state, action)) continue;
                State next = apply(state, action);
                if (cameFrom.containsKey(next)) continue;

                List<Action> nextPath = new ArrayList<>(path);
                nextPath.add(action);
                cameFrom.put(next, nextPath);
                queue.add(next);
            }
        }
        return Collections.emptyList();
    }

    private boolean isGoalSatisfied(State state, Goal goal) {
        for (Map.Entry<String, Boolean> e : goal.desiredState.entrySet()) {
            if (state.get(e.getKey()) != e.getValue()) return false;
        }
        return true;
    }

    private boolean isApplicable(State state, Action action) {
        for (Map.Entry<String, Boolean> e : action.preconditions.entrySet()) {
            if (state.get(e.getKey()) != e.getValue()) return false;
        }
        return true;
    }

    private State apply(State state, Action action) {
        State next = state.copy();
        for (Map.Entry<String, Boolean> e : action.effects.entrySet()) {
            next.set(e.getKey(), e.getValue());
        }
        return next;
    }
}