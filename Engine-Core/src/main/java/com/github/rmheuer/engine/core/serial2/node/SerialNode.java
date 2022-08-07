package com.github.rmheuer.engine.core.serial2.node;

import java.util.ArrayList;
import java.util.List;

public abstract class SerialNode {
    private TraversableNode parent;
    private String pathToken;

    public TraversableNode getParent() {
        return parent;
    }

    void setParent(TraversableNode parent) {
        this.parent = parent;
    }

    void setPathToken(String tok) {
        pathToken = tok;
    }

    /**
     * Gets a relative path from this node to another node.
     *
     * @param other path destination
     * @return path if exists, else null
     */
    public String getRelativePathTo(SerialNode other) {
        if (other == this)
            throw new IllegalArgumentException("Cannot get relative path to self");

        List<SerialNode> parents = new ArrayList<>();
        List<SerialNode> otherParents = new ArrayList<>();

        SerialNode current = this;
        SerialNode otherCurrent = other;
        while (true) {
            if (current == null && otherCurrent == null)
                return null;

            if (current != null)
                parents.add(current);
            if (otherCurrent != null)
                otherParents.add(otherCurrent);

            if (current != null) {
                if (otherParents.contains(current)) {
                    return makePath(other, current, otherParents);
                }
                current = current.getParent();
            }
            if (otherCurrent != null) {
                if (parents.contains(otherCurrent)) {
                    return makePath(other, otherCurrent, otherParents);
                }
                otherCurrent = otherCurrent.getParent();
            }
        }
    }

    private String makePath(SerialNode dst, SerialNode commonParent, List<SerialNode> otherParents) {
        StringBuilder builder = new StringBuilder();

        // Walk back up the tree to the common parent
        SerialNode n = this;
        while (n != commonParent) {
            n = n.parent;
            builder.append(TraversableNode.PARENT_TOKEN);
            if (n != dst)
                builder.append(TraversableNode.PATH_SEPARATOR);
            else
                return builder.toString();
        }

        // Walk back down the tree to the destination
        int commonIdx = otherParents.lastIndexOf(commonParent);

        for (int i = commonIdx; i >= 0; i--) {
            SerialNode node = otherParents.get(i);
            if (node.pathToken != null) {
                builder.append(node.pathToken);

                if (node != dst)
                    builder.append(TraversableNode.PATH_SEPARATOR);
            }

            if (node == dst)
                return builder.toString();
        }

        throw new IllegalStateException("Did not find destination node in parent list");
    }
}
