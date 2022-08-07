package com.github.rmheuer.engine.core.serial2.node;

public abstract class TraversableNode extends SerialNode {
    public static final String PATH_SEPARATOR = "/";
    public static final String PARENT_TOKEN = "..";

    public abstract SerialNode evalPathToken(String tok);

    public SerialNode getByPath(String path) {
        String[] tokens = path.split(PATH_SEPARATOR);

        TraversableNode node = this;
        for (int i = 0; i < tokens.length; i++) {
            String token = tokens[i];

            if (token.equals(PARENT_TOKEN)) {
                node = node.getParent();
            } else {
                SerialNode nextNode = node.evalPathToken(token);
                if (i == tokens.length - 1)
                    return nextNode;

                if (!(nextNode instanceof TraversableNode))
                    return null;

                node = (TraversableNode) nextNode;
            }
        }

        return node;
    }

    // Traversable nodes must be equal by identity
    @Override
    public final boolean equals(Object o) {
        return o == this;
    }
}
