package com.github.rmheuer.engine.core.ecs.system.schedule;

import com.github.rmheuer.engine.core.ecs.system.GameSystem;
import com.github.rmheuer.engine.core.ecs.system.SystemNode;
import com.github.rmheuer.engine.core.ecs.system.schedule.info.GroupInfo;
import com.github.rmheuer.engine.core.ecs.system.schedule.info.NodeOrderingInfo;
import com.github.rmheuer.engine.core.ecs.system.schedule.info.RepeatableNodeOrderingInfo;
import com.github.rmheuer.engine.core.util.LazyCache;

import java.util.*;
import java.util.function.Consumer;

public final class StageScheduler<T> {
    private static abstract class Node<T> {
        protected final NodeOrderingInfo orderInfo;

        public Node(NodeOrderingInfo orderInfo) {
            this.orderInfo = orderInfo;
        }

        public abstract void forEach(Consumer<T> fn);
        public abstract void dump(String indent);
    }

    private static final class GroupNode<T> extends Node<T> {
        private final Map<Class<? extends SystemNode>, Node<T>> children;
        private List<Node<T>> cachedOrder;

        public GroupNode(NodeOrderingInfo orderInfo) {
            super(orderInfo);

            children = new LinkedHashMap<>();
            cachedOrder = null;
        }

        public void add(Node<T> node) {
            children.put(node.orderInfo.getSourceType(), node);
            cachedOrder = null;
        }

        private void solve() {
            // Get edges, mapping before -> afters
            LazyCache<Class<? extends SystemNode>, Set<Class<? extends SystemNode>>> edges = new LazyCache<>((n) -> new HashSet<>());
            for (Map.Entry<Class<? extends SystemNode>, Node<T>> entry : children.entrySet()) {
                NodeOrderingInfo order = entry.getValue().orderInfo;
                edges.get(entry.getKey()).addAll(order.getAfter());
                for (Class<? extends SystemNode> before : order.getBefore())
                    edges.get(before).add(entry.getKey());
            }

            // Solve the order constraints
            List<Class<? extends SystemNode>> unmarked = new ArrayList<>(children.keySet());
            Set<Class<? extends SystemNode>> tempMarked = new HashSet<>();
            List<Class<? extends SystemNode>> outputTypes = new ArrayList<>();
            while (!unmarked.isEmpty()) {
                Class<? extends SystemNode> n = unmarked.get(0);
                visit(n, edges, unmarked, tempMarked, outputTypes);
            }

            // Cache the solved order
            cachedOrder = new ArrayList<>();
            for (Class<? extends SystemNode> type : outputTypes) {
                cachedOrder.add(children.get(type));
            }
        }

        private void visit(
                Class<? extends SystemNode> node,
                LazyCache<Class<? extends SystemNode>, Set<Class<? extends SystemNode>>> edges,
                List<Class<? extends SystemNode>> unmarked,
                Set<Class<? extends SystemNode>> tempMarked,
                List<Class<? extends SystemNode>> output
        ) {
            if (!unmarked.contains(node))
                return;
            if (tempMarked.contains(node))
                throw new RuntimeException("Unsolvable schedule constraints");

            if (edges.isCached(node)) {
                tempMarked.add(node);
                for (Class<? extends SystemNode> m : edges.get(node)) {
                    visit(m, edges, unmarked, tempMarked, output);
                }
                tempMarked.remove(node);
            }

            unmarked.remove(node);
            output.add(node);
        }

        @Override
        public void forEach(Consumer<T> fn) {
            if (cachedOrder == null)
                solve();

            for (Node<T> sys : cachedOrder) {
                sys.forEach(fn);
            }
        }

        @Override
        public void dump(String indent) {
            if (orderInfo != null) {
                System.out.println(indent + orderInfo.getSourceType().getSimpleName());
            }
            if (cachedOrder == null)
                solve();
            for (Node<T> node : cachedOrder)
                if (orderInfo != null)
                    node.dump(indent + "  ");
                else
                    node.dump(indent);
        }
    }

    private static final class SysNode<T> extends Node<T> {
        private final T info;

        public SysNode(T info, NodeOrderingInfo orderInfo) {
            super(orderInfo);
            this.info = info;
        }

        @Override
        public void forEach(Consumer<T> fn) {
            fn.accept(info);
        }

        @Override
        public void dump(String indent) {
            String str = info.toString();
            if (info instanceof GameSystem)
                str = info.getClass().getSimpleName();
            else if (info instanceof RepeatableNodeOrderingInfo)
                str = ((RepeatableNodeOrderingInfo) info).getInstance().getClass().getSimpleName();
            System.out.println(indent + str);
        }
    }

    private final GroupNode<T> root;
    private final LazyCache<GroupInfo, GroupNode<T>> groups;

    public StageScheduler() {
        root = new GroupNode<>(null);
        groups = new LazyCache<>(this::getOrAddGroup);
    }

    private GroupNode<T> getOrAddGroup(GroupInfo info) {
        if (info.isRoot())
            return root;

        GroupNode<T> parent = groups.get(info.getGroup());
        GroupNode<T> child = new GroupNode<>(info);
        parent.add(child);

        return child;
    }

    public void add(T system, NodeOrderingInfo order) {
        if (order == null)
            return;

        GroupNode<T> group = groups.get(order.getGroup());
        SysNode<T> node = new SysNode<>(system, order);
        group.add(node);
    }

    public void forEach(Consumer<T> fn) {
        root.forEach(fn);
    }

    public void dump() {
        root.dump("  ");
    }
}
