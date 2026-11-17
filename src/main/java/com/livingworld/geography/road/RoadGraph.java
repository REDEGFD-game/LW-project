package com.livingworld.geography.road;

import java.util.*;

/**
 * Дорожный граф: узлы — поселения/города, рёбра — дороги.
 * Генерация на основе seed: сначала MST (минимальное остовное дерево),
 * затем добавление торговых маршрутов (рёбра с минимальной стоимостью, образующие циклы).
 * Не зависит от API Minecraft, может быть протестирован отдельно.
 */
public class RoadGraph {

    public static class Node {
        public final int id;
        public final double x, z;

        public Node(int id, double x, double z) {
            this.id = id;
            this.x = x;
            this.z = z;
        }

        public double distanceTo(Node other) {
            double dx = this.x - other.x;
            double dz = this.z - other.z;
            return Math.sqrt(dx * dx + dz * dz);
        }
    }

    public static class Edge {
        public final int from, to;
        public final double cost;
        public final boolean isTradeRoute; // true = торговый маршрут (добавлен для образования цикла)

        public Edge(int from, int to, double cost, boolean isTradeRoute) {
            this.from = from;
            this.to = to;
            this.cost = cost;
            this.isTradeRoute = isTradeRoute;
        }
    }

    private final List<Node> nodes = new ArrayList<>();
    private final List<Edge> edges = new ArrayList<>();

    public void addNode(Node node) {
        nodes.add(node);
    }

    public List<Node> getNodes() { return Collections.unmodifiableList(nodes); }
    public List<Edge> getEdges() { return Collections.unmodifiableList(edges); }

    /**
     * Генерация MST алгоритмом Прима, затем добавление торговых маршрутов.
     * @param tradeRouteCount количество дополнительных торговых маршрутов
     * @param random seed для воспроизводимости
     */
    public void build(int tradeRouteCount, Random random) {
        if (nodes.size() < 2) return;

        // 1. MST (Прим)
        boolean[] inMst = new boolean[nodes.size()];
        inMst[0] = true;
        int added = 1;

        while (added < nodes.size()) {
            double bestCost = Double.MAX_VALUE;
            int bestFrom = -1, bestTo = -1;

            for (int i = 0; i < nodes.size(); i++) {
                if (!inMst[i]) continue;
                for (int j = 0; j < nodes.size(); j++) {
                    if (inMst[j]) continue;
                    double cost = nodes.get(i).distanceTo(nodes.get(j));
                    if (cost < bestCost) {
                        bestCost = cost;
                        bestFrom = i;
                        bestTo = j;
                    }
                }
            }

            if (bestFrom < 0) break; // граф не связный (не должно случиться)
            edges.add(new Edge(bestFrom, bestTo, bestCost, false));
            inMst[bestTo] = true;
            added++;
        }

        // 2. Торговые маршруты: среди рёбер, не входящих в MST,
        // выбираем tradeRouteCount рёбер с минимальной стоимостью и без дублирования
        List<Edge> candidates = new ArrayList<>();
        Set<String> mstPairs = new HashSet<>();
        for (Edge e : edges) {
            mstPairs.add(pairKey(e.from, e.to));
        }

        for (int i = 0; i < nodes.size(); i++) {
            for (int j = i + 1; j < nodes.size(); j++) {
                if (mstPairs.contains(pairKey(i, j))) continue;
                double cost = nodes.get(i).distanceTo(nodes.get(j));
                candidates.add(new Edge(i, j, cost, true));
            }
        }

        candidates.sort(Comparator.comparingDouble(e -> e.cost));
        for (int k = 0; k < Math.min(tradeRouteCount, candidates.size()); k++) {
            edges.add(candidates.get(k));
        }
    }

    private static String pairKey(int a, int b) {
        return Math.min(a, b) + "-" + Math.max(a, b);
    }

    /** Для отладки: выводит список рёбер. */
    public String describe() {
        StringBuilder sb = new StringBuilder();
        for (Edge e : edges) {
            sb.append(String.format("%d -> %d (%.1f)%s%n",
                    e.from, e.to, e.cost, e.isTradeRoute ? " [trade]" : ""));
        }
        return sb.toString();
    }
}