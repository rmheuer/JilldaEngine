package com.github.rmheuer.engine.editor.window;

import com.github.rmheuer.engine.core.input.mouse.MouseButton;
import com.github.rmheuer.engine.core.main.Game;
import com.github.rmheuer.engine.core.resource.Resource;
import com.github.rmheuer.engine.core.resource.ResourceFile;
import com.github.rmheuer.engine.core.resource.ResourceGroup;
import com.github.rmheuer.engine.core.resource.file.FileResourceGroup;
import com.github.rmheuer.engine.editor.event.FileDeleteEvent;
import com.github.rmheuer.engine.editor.event.FileTreeSelectionChangeEvent;
import com.github.rmheuer.engine.gui.GuiRenderable;
import com.github.rmheuer.engine.gui.GuiRenderer;
import com.github.rmheuer.engine.gui.GuiTextEditFlags;
import com.github.rmheuer.engine.gui.GuiTreeFlags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class FileTreeWindow implements GuiRenderable {
    private final ResourceGroup root;
    private Resource selection;
    private Resource context;

    private Resource prevRenaming;
    private Resource renaming;
    private final StringBuilder renameStr;

    public FileTreeWindow() {
        root = new FileResourceGroup(System.getProperty("user.dir")).getSubgroup("Engine-Editor/Edit sandbox");
        selection = null;
        prevRenaming = null;
        renaming = null;
        renameStr = new StringBuilder();
    }

    private void select(Resource res, boolean save) {
	selection = res;
	Game.get().postGlobalImmediateEvent(new FileTreeSelectionChangeEvent(res, save));
    }

    private boolean showNode(GuiRenderer g, Resource res, String label, int flags) {
        if (res.equals(selection))
            flags |= GuiTreeFlags.Selected;

        boolean isRenaming = res.equals(renaming);

        boolean open = g.pushTree(isRenaming ? "" : label, res.getAbsolutePath(), flags);
        if (isRenaming) {
            int editFlags = GuiTextEditFlags.ReturnFocused | GuiTextEditFlags.AlignToTreeSize;
            if (!renaming.equals(prevRenaming))
                editFlags |= GuiTextEditFlags.Focus | GuiTextEditFlags.SelectAll;
            g.sameLine();
            prevRenaming = renaming;
            if (!g.editString(renameStr, editFlags)) {
                try {
                    String name = renameStr.toString();
                    if (!name.equals(renaming.getName())) {
                        renaming.rename(name);
                        Resource newRes = res.getSibling(name);
                        select(newRes, !renaming.equals(selection));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                renaming = null;
                prevRenaming = null;
            }
        }

        if (g.isWidgetClicked(MouseButton.LEFT))
            select(res, true);
        return open;
    }

    private void showFile(GuiRenderer g, ResourceFile res) {
        int flags = GuiTreeFlags.Leaf | GuiTreeFlags.BackgroundFillsAvailX | GuiTreeFlags.ToggleRequiresSelection;
        showNode(g, res, res.getName(), flags);
        if (g.addContextMenu("file-ctx"))
            context = res;
    }

    private void showGroup(GuiRenderer g, ResourceGroup group, boolean isRoot) {
        StringBuilder label = new StringBuilder(group.getName());
        while (group.getSubgroups().size() == 1 && group.getResources().isEmpty()) {
            group = group.getSubgroups().iterator().next();
            label.append(ResourceFile.SEPARATOR);
            label.append(group.getName());
        }

        int flags = GuiTreeFlags.BackgroundFillsAvailX | GuiTreeFlags.ToggleRequiresSelection;
        if (isRoot)
            flags |= GuiTreeFlags.DefaultOpen;

        boolean open = showNode(g, group, label.toString(), flags);
        if (g.addContextMenu("group-ctx"))
            context = group;

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

    private void scanForDeletingSelection(ResourceGroup g) {
        for (ResourceFile file : g.getResources()) {
            Game.get().postGlobalImmediateEvent(new FileDeleteEvent(file));
        }

        for (ResourceGroup group : g.getSubgroups()) {
            Game.get().postGlobalImmediateEvent(new FileDeleteEvent(g));
            scanForDeletingSelection(group);
        }
    }

    private void showCommonContextMenu(GuiRenderer g) {
        if (g.button("Rename")) {
            renaming = context;
            renameStr.replace(0, renameStr.length(), context.getName());
            g.closeContextMenu();
        }
        if (g.button("Delete")) {
            if (context.isGroup())
                scanForDeletingSelection((ResourceGroup) context);
            Game.get().postGlobalImmediateEvent(new FileDeleteEvent(context));
            context.delete();

            g.closeContextMenu();
        }
    }

    private void showGroupContextMenu(GuiRenderer g) {
        ResourceGroup group = (ResourceGroup) context;

        if (g.button("Create new file")) {
            ResourceFile newFile = group.getResource("New File");
            int i = 1;
            while (newFile.exists()) {
                newFile = group.getResource("New File (" + i++ + ")");
            }
            try {
                newFile.create();
            } catch (IOException e) {
                e.printStackTrace();
            }

            g.closeContextMenu();
        }

        if (g.button("Create new folder")) {
            ResourceGroup newFile = group.getSubgroup("New Folder");
            int i = 1;
            while (newFile.exists()) {
                newFile = group.getSubgroup("New Folder (" + i++ + ")");
            }
            try {
                newFile.create();
            } catch (IOException e) {
                e.printStackTrace();
            }

            g.closeContextMenu();
        }
    }

    @Override
    public void drawGui(GuiRenderer g) {
        g.setLineSpacingEnabled(false);
        showGroup(g, root, true);
        g.setLineSpacingEnabled(true);

        if (g.beginContextMenu("file-ctx")) {
            showCommonContextMenu(g);
            g.endContextMenu();
        }

        if (g.beginContextMenu("group-ctx")) {
            showCommonContextMenu(g);
            g.separator();
            showGroupContextMenu(g);
            g.endContextMenu();
        }
    }
}
