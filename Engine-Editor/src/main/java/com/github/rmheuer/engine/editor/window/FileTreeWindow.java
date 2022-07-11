package com.github.rmheuer.engine.editor.window;

import com.github.rmheuer.engine.core.input.mouse.MouseButton;
import com.github.rmheuer.engine.core.main.Game;
import com.github.rmheuer.engine.core.resource.Resource;
import com.github.rmheuer.engine.core.resource.ResourceFile;
import com.github.rmheuer.engine.core.resource.ResourceGroup;
import com.github.rmheuer.engine.core.resource.file.FileResourceGroup;
import com.github.rmheuer.engine.editor.event.FileTreeSelectionChangeEvent;
import com.github.rmheuer.engine.gui.GuiRenderable;
import com.github.rmheuer.engine.gui.GuiRenderer;
import com.github.rmheuer.engine.gui.GuiTreeFlags;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class FileTreeWindow implements GuiRenderable {
    private final ResourceGroup root;
    private Resource selection;

    public FileTreeWindow() {
        root = new FileResourceGroup(System.getProperty("user.dir"));
        selection = null;
    }

    private void select(Resource res) {
	selection = res;
	Game.get().postImmediateEvent(new FileTreeSelectionChangeEvent(res));
    }

    private void showFile(GuiRenderer g, ResourceFile res) {
        int flags = GuiTreeFlags.Leaf | GuiTreeFlags.BackgroundFillsAvailX | GuiTreeFlags.ToggleRequiresSelection;
        if (res.equals(selection))
            flags |= GuiTreeFlags.Selected;
        g.pushTree(res.getName(), res.getAbsolutePath(), flags);
        if (g.isWidgetClicked(MouseButton.LEFT))
            select(res);
    }

    private void showGroup(GuiRenderer g, ResourceGroup group, boolean isRoot) {
        StringBuilder label = new StringBuilder(group.getName());
        while (group.getSubgroups().size() == 1 && group.getResources().isEmpty()) {
            group = group.getSubgroups().iterator().next();
            label.append(ResourceFile.SEPARATOR);
            label.append(group.getName());
        }

        int flags = GuiTreeFlags.BackgroundFillsAvailX | GuiTreeFlags.ToggleRequiresSelection;
        if (group.equals(selection))
            flags |= GuiTreeFlags.Selected;
        if (isRoot)
            flags |= GuiTreeFlags.DefaultOpen;

        boolean open = g.pushTree(label.toString(), group.getAbsolutePath(), flags);
        if (g.isWidgetClicked(MouseButton.LEFT))
            select(group);

        if (open) {
            List<ResourceGroup> groups = new ArrayList<>(group.getSubgroups());
            List<ResourceFile> files = new ArrayList<>(group.getResources());
            groups.sort(Comparator.comparing(ResourceGroup::getName, String.CASE_INSENSITIVE_ORDER));
            files.sort(Comparator.comparing(ResourceFile::getName, String.CASE_INSENSITIVE_ORDER));

            for (ResourceGroup subgroup : groups) {
                showGroup(g, subgroup, false);
            }

            for (ResourceFile r : files) {
                showFile(g, r);
            }

            g.popTree();
        }
    }

    @Override
    public void drawGui(GuiRenderer g) {
        g.setLineSpacingEnabled(false);
        showGroup(g, root, true);
        g.setLineSpacingEnabled(true);
    }
}
