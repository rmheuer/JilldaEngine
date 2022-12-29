package com.github.rmheuer.engine.debugtools;

import com.github.rmheuer.engine.core.event.Event;
import com.github.rmheuer.engine.core.main.Game;
import com.github.rmheuer.engine.core.profile.FixedProfileStage;
import com.github.rmheuer.engine.core.profile.ProfileNode;
import com.github.rmheuer.engine.gui.GuiRenderable;
import com.github.rmheuer.engine.gui.GuiRenderer;
import com.github.rmheuer.engine.gui.GuiTableFlags;
import com.github.rmheuer.engine.gui.GuiTreeFlags;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class ProfileTool implements GuiRenderable {
    private void showProfileNode(GuiRenderer g, ProfileNode node) {
        List<ProfileNode> children = node.getChildren();

        int flags = children.isEmpty() ? GuiTreeFlags.Leaf : GuiTreeFlags.None;
        g.tableNextColumn();
        boolean open = g.pushTree(node.getName(), node.getName(), flags);
        g.tableNextColumn();
        g.text(String.format("%.3f", node.getSelfTime() / 1_000_000.0));
        g.tableNextColumn();
        g.text(String.format("%.3f", node.getTotalTime() / 1_000_000.0));

        if (open) {
            for (ProfileNode child : children) {
                showProfileNode(g, child);
            }
            g.popTree();
        }
    }

    private void showFixed(GuiRenderer g) {
        Map<FixedProfileStage, ProfileNode> fixedData = Game.get().getStageProfileData();
        for (FixedProfileStage stage : FixedProfileStage.values()) {
            ProfileNode node = fixedData.get(stage);
            if (node != null)
                showProfileNode(g, node);
        }
    }

    private void showGlobalEvents(GuiRenderer g) {
        g.tableNextColumn();
        boolean open = g.pushTree("Global events");
        g.tableNextColumn();
        g.tableNextColumn();

        if (open) {
            // Alphabetize event entries
            List<Map.Entry<Class<? extends Event>, ProfileNode>> eventEntries = new ArrayList<>(Game.get().getEventProfileData().entrySet());
            eventEntries.sort((e1, e2) -> String.CASE_INSENSITIVE_ORDER.compare(e1.getKey().getSimpleName(), e2.getKey().getSimpleName()));

            for (Map.Entry<Class<? extends Event>, ProfileNode> entry : eventEntries) {
                showProfileNode(g, entry.getValue());
            }
            g.popTree();
        }
    }

    @Override
    public void drawGui(GuiRenderer g) {
        g.beginTableFlags(GuiTableFlags.NoPaddingY, 4, 1, 1);
        g.tableNextColumn();
        g.text("Name"); g.tableNextColumn();
        g.text("Self"); g.tableNextColumn();
        g.text("Total");

        showFixed(g);
        showGlobalEvents(g);

        g.endTable();
    }
}
