package imgui.api;

import imgui.api.fn.ImDrawCallback;

import java.util.ArrayList;
import java.util.List;

import static imgui.api.ImGuiMacros.IM_COL32_WHITE;

public final class ImDrawList {
    public final List<ImDrawCmd> cmdBuffer = new ArrayList<>();
    public final List<Integer> idxBuffer = new ArrayList<>();
    public final List<ImDrawVert> vtxBuffer = new ArrayList<>();
    public int flags;

    public int vtxCurrentIdx;
    public ImDrawListSharedData/*ptr*/ data;
    public String ownerName;
    public int vtxWritePtr;
    public int idxWritePtr;
    public final List<ImVec4> clipRectStack = new ArrayList<>();
    public final List<Integer> textureIdStack = new ArrayList<>();
    public final List<ImVec2> path = new ArrayList<>();
    public final ImDrawCmdHeader cmdHeader = new ImDrawCmdHeader();
    public final ImDrawListSplitter splitter = new ImDrawListSplitter();
    public float fringeScale;

    public ImDrawList(ImDrawListSharedData/*ptr*/ sharedData) {
        data = sharedData;
    }

    public void pushClipRect(ImVec2 clipRectMin, ImVec2 clipRectMax) { pushClipRect(clipRectMin, clipRectMax, false); }
    public void pushClipRect(ImVec2/*ref*/ clipRectMin, ImVec2/*ref*/ clipRectMax, boolean intersectWithCurrentClipRect) {

    }

    public void pushClipRectFullScreen() {

    }

    public void popClipRect() {

    }

    public void pushTextureID(int textureId) {

    }

    public void popTextureID() {

    }

    public ImVec2 getClipRectMin() {
        ImVec4 cr = clipRectStack.get(clipRectStack.size() - 1);
        return new ImVec2(cr.x, cr.y);
    }

    public ImVec2 getClipRectMax() {
        ImVec4 cr = clipRectStack.get(clipRectStack.size() - 1);
        return new ImVec2(cr.z, cr.w);
    }

    public void addLine(ImVec2 p1, ImVec2 p2, int col) { addLine(p1, p2, col, 1.0f); }
    public void addLine(ImVec2/*ref*/ p1, ImVec2/*ref*/ p2, int col, float thickness) {

    }

    public void addRect(ImVec2 pMin, ImVec2 pMax, int col) { addRect(pMin, pMax, col, 0.0f); }
    public void addRect(ImVec2 pMin, ImVec2 pMax, int col, float rounding) { addRect(pMin, pMax, col, rounding, 0); }
    public void addRect(ImVec2 pMin, ImVec2 pMax, int col, float rounding, int flags) { addRect(pMin, pMax, col, rounding, flags, 1.0f); }
    public void addRect(ImVec2/*ref*/ pMin, ImVec2/*ref*/ pMax, int col, float rounding, int flags, float thickness) {

    }

    public void addRectFilled(ImVec2 pMin, ImVec2 pMax, int col) { addRectFilled(pMin, pMax, col, 0.0f); }
    public void addRectFilled(ImVec2 pMin, ImVec2 pMax, int col, float rounding) { addRectFilled(pMin, pMax, col, rounding, 0); }
    public void addRectFilled(ImVec2/*ref*/ pMin, ImVec2/*ref*/ pMax, int col, float rounding, int flags) {

    }

    public void addRectFilledMultiColor(ImVec2/*ref*/ pMin, ImVec2/*ref*/ pMax, int colUprLeft, int colUprRight, int colBotRight, int colBotLeft) {

    }

    public void addQuad(ImVec2 p1, ImVec2 p2, ImVec2 p3, ImVec2 p4, int col) { addQuad(p1, p2, p3, p4, col, 1.0f); }
    public void addQuad(ImVec2/*ref*/ p1, ImVec2/*ref*/ p2, ImVec2/*ref*/ p3, ImVec2/*ref*/ p4, int col, float thickness) {

    }

    public void addQuadFilled(ImVec2/*ref*/ p1, ImVec2/*ref*/ p2, ImVec2/*ref*/ p3, ImVec2/*ref*/ p4, int col) {

    }

    public void addTriangle(ImVec2 p1, ImVec2 p2, ImVec2 p3, int col) { addTriangle(p1, p2, p3, col, 1.0f); }
    public void addTriangle(ImVec2/*ref*/ p1, ImVec2/*ref*/ p2, ImVec2/*ref*/ p3, int col, float thickness) {

    }

    public void addTriangleFilled(ImVec2/*ref*/ p1, ImVec2/*ref*/ p2, ImVec2/*ref*/ p3, int col) {

    }

    public void addCircle(ImVec2 center, float radius, int col) { addCircle(center, radius, col, 0); }
    public void addCircle(ImVec2 center, float radius, int col, int numSegments) { addCircle(center, radius, col, numSegments, 1.0f); }
    public void addCircle(ImVec2/*ref*/ center, float radius, int col, int numSegments, float thickness) {

    }

    public void addCircleFilled(ImVec2 center, float radius, int col) { addCircleFilled(center, radius, col, 0); }
    public void addCircleFilled(ImVec2/*ref*/ center, float radius, int col, int numSegments) {

    }

    public void addNgon(ImVec2 center, float radius, int col, int numSegments) { addNgon(center, radius, col, numSegments, 1.0f); }
    public void addNgon(ImVec2/*ref*/ center, float radius, int col, int numSegments, float thickness) {

    }

    public void addNgonFilled(ImVec2/*ref*/ center, float radius, int col, int numSegments) {

    }

    public void addText(ImVec2/*ref*/ pos, int col, String text) {

    }

    public void addText(ImFont font, float fontSize, ImVec2 pos, int col, String text) { addText(font, fontSize, pos, col, text, 0.0f); }
    public void addText(ImFont font, float fontSize, ImVec2 pos, int col, String text, float wrapWidth) { addText(font, fontSize, pos, col, text, wrapWidth, null); }
    public void addText(ImFont/*ptr*/ font, float fontSize, ImVec2/*ref*/ pos, int col, String text, float wrapWidth, ImVec4/*ptr*/ cpuFineClipRect) {

    }

    public void addPolyline(ImVec2[] points, int col, int flags, float thickness) {

    }

    public void addConvexPolyFilled(ImVec2[] points, int col) {

    }

    public void addBezierCubic(ImVec2 p1, ImVec2 p2, ImVec2 p3, ImVec2 p4, int col, float thickness) { addBezierCubic(p1, p2, p3, p4, col, thickness, 0); }
    public void addBezierCubic(ImVec2/*ref*/ p1, ImVec2/*ref*/ p2, ImVec2/*ref*/ p3, ImVec2/*ref*/ p4, int col, float thickness, int numSegments) {

    }

    public void addBezierQuadratic(ImVec2 p1, ImVec2 p2, ImVec2 p3, int col, float thickness) { addBezierQuadratic(p1, p2, p3, col, thickness, 0); }
    public void addBezierQuadratic(ImVec2/*ref*/ p1, ImVec2/*ref*/ p2, ImVec2/*ref*/ p3, int col, float thickness, int numSegments) {

    }

    public void addImage(int userTextureId, ImVec2 pMin, ImVec2 pMax) { addImage(userTextureId, pMin, pMax, new ImVec2(0, 0)); }
    public void addImage(int userTextureId, ImVec2 pMin, ImVec2 pMax, ImVec2 uvMin) { addImage(userTextureId, pMin, pMax, uvMin, new ImVec2(1, 1)); }
    public void addImage(int userTextureId, ImVec2 pMin, ImVec2 pMax, ImVec2 uvMin, ImVec2 uvMax) { addImage(userTextureId, pMin, pMax, uvMin, uvMax, IM_COL32_WHITE); }
    public void addImage(int userTextureId, ImVec2/*ref*/ pMin, ImVec2/*ref*/ pMax, ImVec2/*ref*/ uvMin, ImVec2/*ref*/ uvMax, int col) {

    }

    public void addImageQuad(int userTextureId, ImVec2 p1, ImVec2 p2, ImVec2 p3, ImVec2 p4) { addImageQuad(userTextureId, p1, p2, p3, p4, new ImVec2(0, 0)); }
    public void addImageQuad(int userTextureId, ImVec2 p1, ImVec2 p2, ImVec2 p3, ImVec2 p4, ImVec2 uv1) { addImageQuad(userTextureId, p1, p2, p3, p4, uv1, new ImVec2(1, 0)); }
    public void addImageQuad(int userTextureId, ImVec2 p1, ImVec2 p2, ImVec2 p3, ImVec2 p4, ImVec2 uv1, ImVec2 uv2) { addImageQuad(userTextureId, p1, p2, p3, p4, uv1, uv2, new ImVec2(1, 1)); }
    public void addImageQuad(int userTextureId, ImVec2 p1, ImVec2 p2, ImVec2 p3, ImVec2 p4, ImVec2 uv1, ImVec2 uv2, ImVec2 uv3) { addImageQuad(userTextureId, p1, p2, p3, p4, uv1, uv2, uv3, new ImVec2(0, 1)); }
    public void addImageQuad(int userTextureId, ImVec2 p1, ImVec2 p2, ImVec2 p3, ImVec2 p4, ImVec2 uv1, ImVec2 uv2, ImVec2 uv3, ImVec2 uv4) { addImageQuad(userTextureId, p1, p2, p3, p4, uv1, uv2, uv3, uv4, IM_COL32_WHITE); }
    public void addImageQuad(int userTextureId, ImVec2/*ref*/ p1, ImVec2/*ref*/ p2, ImVec2/*ref*/ p3, ImVec2/*ref*/ p4, ImVec2/*ref*/ uv1, ImVec2/*ref*/ uv2, ImVec2/*ref*/ uv3, ImVec2/*ref*/ uv4, int col) {

    }

    public void addImageRounded(int userTextureId, ImVec2 pMin, ImVec2 pMax, ImVec2 uvMIn, ImVec2 uvMax, int col, float rounding) { addImageRounded(userTextureId, pMin, pMax, uvMIn, uvMax, col, rounding, 0); }
    public void addImageRounded(int userTextureId, ImVec2/*ref*/ pMin, ImVec2/*ref*/ pMax, ImVec2/*ref*/ uvMin, ImVec2/*ref*/ uvMax, int col, float rounding, int flags) {

    }

    public void pathClear() {
        path.clear();
    }

    public void pathLineTo(ImVec2/*ref*/ pos) {
        path.add(new ImVec2(pos));
    }

    public void pathLineToMergeDuplicate(ImVec2/*ref*/ pos) {
        if (path.size() == 0 || !path.get(path.size() - 1).equals(pos)) {
            path.add(new ImVec2(pos));
        }
    }

    public void pathFillConvex(int col) {
        addConvexPolyFilled(path.toArray(new ImVec2[0]), col);
        path.clear();
    }

    public void pathStroke(int col) { pathStroke(col, 0); }
    public void pathStroke(int col, int flags) { pathStroke(col, flags, 1.0f); }
    public void pathStroke(int col, int flags, float thickness) {
        addPolyline(path.toArray(new ImVec2[0]), col, flags, thickness);
        path.clear();
    }

    public void pathArcTo(ImVec2 center, float radius, float aMin, float aMax) { pathArcTo(center, radius, aMin, aMax, 0); }
    public void pathArcTo(ImVec2/*ref*/ center, float radius, float aMin, float aMax, int numSegments) {

    }

    public void pathArcToFast(ImVec2/*ref*/ center, float radius, int aMinOf12, int aMaxOf12) {

    }

    public void pathBezierCubicCurveTo(ImVec2 p2, ImVec2 p3, ImVec2 p4) { pathBezierCubicCurveTo(p2, p3, p4, 0); }
    public void pathBezierCubicCurveTo(ImVec2/*ref*/ p2, ImVec2/*ref*/ p3, ImVec2/*ref*/ p4, int numSegments) {

    }

    public void pathBezierQuadraticCurveTo(ImVec2 p2, ImVec2 p3) { pathBezierQuadraticCurveTo(p2, p3, 0); }
    public void pathBezierQuadraticCurveTo(ImVec2/*ref*/ p2, ImVec2/*ref*/ p3, int numSegments) {

    }

    public void pathRect(ImVec2 rectMin, ImVec2 rectMax) { pathRect(rectMin, rectMax, 0.0f); }
    public void pathRect(ImVec2 rectMin, ImVec2 rectMax, float rounding) { pathRect(rectMin, rectMax, rounding, 0); }
    public void pathRect(ImVec2/*ref*/ rectMin, ImVec2/*ref*/ rectMax, float rounding, int flags) {

    }

    public void addCallback(ImDrawCallback callback, Object callbackData) {

    }

    public void addDrawCmd() {

    }

    public ImDrawList/*ptr*/ cloneOutput() {
        return null;
    }

    public void channelsSplit(int count) {
        splitter.split(this, count);
    }

    public void channelsMerge() {
        splitter.merge(this);
    }

    public void channelsSetCurrent(int n) {
        splitter.setCurrentChannel(this, n);
    }

    public void primReserve(int idxCount, int vtxCount) {

    }

    public void primUnreserve(int idxCount, int vtxCount) {

    }

    public void primRect(ImVec2/*ref*/ a, ImVec2/*ref*/ b, int col) {

    }

    public void primRectUV(ImVec2/*ref*/ a, ImVec2/*ref*/ b, ImVec2/*ref*/ uvA, ImVec2/*ref*/ uvB, int col) {

    }

    public void primQuadUV(ImVec2/*ref*/ a, ImVec2/*ref*/ b, ImVec2/*ref*/ c, ImVec2/*ref*/ d, ImVec2/*ref*/ uvA, ImVec2/*ref*/ uvB, ImVec2/*ref*/ uvC, ImVec2/*ref*/ uvD, int col) {

    }

    public void primWriteVtx(ImVec2/*ref*/ pos, ImVec2/*ref*/ uv, int col) {
        ImDrawVert v = vtxBuffer.get(vtxWritePtr);
        v.pos.set(pos);
        v.uv.set(uv);
        v.col = col;
        vtxWritePtr++;
        vtxCurrentIdx++;
    }

    public void primWriteIdx(int idx) {
        idxBuffer.set(idxWritePtr, idx);
        idxWritePtr++;
    }

    public void primVtx(ImVec2/*ref*/ pos, ImVec2/*ref*/ uv, int col) {
        primWriteIdx(vtxCurrentIdx);
        primWriteVtx(pos, uv, col);
    }

    public void resetForNewFrame() {

    }

    public void popUnusedDrawCmd() {

    }

    public void tryMergeDrawCmds() {

    }

    public void onChangedClipRect() {

    }

    public void onChangedTextureID() {

    }

    public void onChangedVtxOffset() {

    }

    public int calcCircleAutoSegmentCount(float radius) {
        return 0;
    }

    public void pathArcToFastEx(ImVec2/*ref*/ center, float radius, int aMinSample, int aMaxSample, int aStep) {

    }

    public void pathArcToN(ImVec2/*ref*/ center, float radius, float aMin, float aMax, int numSegments) {

    }
}
