package imgui;

import imgui.fn.ImDrawCallback;

import static imgui.ImGuiMacros.*;

public final class ImDrawList {
    public ImVector<ImDrawCmd> cmdBuffer = new ImVector<>();
    public ImVector<Integer> idxBuffer = new ImVector<>();
    public ImVector<ImDrawVert> vtxBuffer = new ImVector<>();
    public int flags;

    public int _VtxCurrentIdx;
    public ImDrawListSharedData _Data = new ImDrawListSharedData();
    public String _ownerName;
    public int _VtxWritePtr;
    public int _IdxWritePtr;
    public ImVector<ImVec4> _ClipRectStack = new ImVector<>();
    public ImVector<Object> _TextureIdStack = new ImVector<>();
    public ImVector<ImVec2> _Path = new ImVector<>();
    public ImDrawCmdHeader _CmdHeader = new ImDrawCmdHeader();
    public ImDrawListSplitter _Splitter = new ImDrawListSplitter();
    public float _FringeScale;

    public ImDrawList(ImDrawListSharedData sharedData) {
        _Data = sharedData;
    }

    public void pushClipRect(ImVec2 clipRectMin, ImVec2 clipRectMax) { pushClipRect(clipRectMin, clipRectMax, false); }
    public void pushClipRect(ImVec2 clipRectMin, ImVec2 clipRectMax, boolean intersectWithCurrentClipRect) {

    }

    public void pushClipRectFullScreen() {

    }

    public void popClipRect() {

    }

    public void pushTextureID(Object textureId) {

    }

    public void popTextureID() {

    }

    public ImVec2 getClipRectMin() {
        ImVec4 cr = _ClipRectStack.back();
        return new ImVec2(cr.x, cr.y);
    }

    public ImVec2 getClipRectMax() {
        ImVec4 cr = _ClipRectStack.back();
        return new ImVec2(cr.z, cr.w);
    }

    public void addLine(ImVec2 p1, ImVec2 p2, int col) { addLine(p1, p2, col, 1.0f); }
    public void addLine(ImVec2 p1, ImVec2 p2, int col, float thickness) {

    }

    public void addRect(ImVec2 pMin, ImVec2 pMax, int col) { addRect(pMin, pMax, col, 0.0f); }
    public void addRect(ImVec2 pMin, ImVec2 pMax, int col, float rounding) { addRect(pMin, pMax, col, rounding, 0); }
    public void addRect(ImVec2 pMin, ImVec2 pMax, int col, float rounding, int flags) { addRect(pMin, pMax, col, rounding, flags, 1.0f); }
    public void addRect(ImVec2 pMin, ImVec2 pMax, int col, float rounding, int flags, float thickness) {

    }

    public void addRectFilled(ImVec2 pMin, ImVec2 pMax, int col) { addRectFilled(pMin, pMax, col, 0.0f); }
    public void addRectFilled(ImVec2 pMin, ImVec2 pMax, int col, float rounding) { addRectFilled(pMin, pMax, col, rounding, 0); }
    public void addRectFilled(ImVec2 pMin, ImVec2 pMax, int col, float rounding, int flags) {

    }

    public void addRectFilledMultiColor(ImVec2 pMin, ImVec2 pMax, int colUprLeft, int colUprRight, int colBotRight, int colBotLeft) {

    }

    public void addQuad(ImVec2 p1, ImVec2 p2, ImVec2 p3, ImVec2 p4, int col) { addQuad(p1, p2, p3, p4, col, 1.0f); }
    public void addQuad(ImVec2 p1, ImVec2 p2, ImVec2 p3, ImVec2 p4, int col, float thickness) {

    }

    public void addQuadFilled(ImVec2 p1, ImVec2 p2, ImVec2 p3, ImVec2 p4, int col) {

    }

    public void addTriangle(ImVec2 p1, ImVec2 p2, ImVec2 p3, int col) { addTriangle(p1, p2, p3, col, 1.0f); }
    public void addTriangle(ImVec2 p1, ImVec2 p2, ImVec2 p3, int col, float thickness) {

    }

    public void addTriangleFilled(ImVec2 p1, ImVec2 p2, ImVec2 p3, int col) {

    }

    public void addCircle(ImVec2 center, float radius, int col) { addCircle(center, radius, col, 0); }
    public void addCircle(ImVec2 center, float radius, int col, int numSegments) { addCircle(center, radius, col, numSegments, 1.0f); }
    public void addCircle(ImVec2 center, float radius, int col, int numSegments, float thickness) {

    }

    public void addCircleFilled(ImVec2 center, float radius, int col) { addCircleFilled(center, radius, col, 0); }
    public void addCircleFilled(ImVec2 center, float radius, int col, int numSegments) {

    }

    public void addNgon(ImVec2 center, float radius, int col, int numSegments) { addNgon(center, radius, col, numSegments, 1.0f); }
    public void addNgon(ImVec2 center, float radius, int col, int numSegments, float thickness) {

    }

    public void addNgonFilled(ImVec2 center, float radius, int col, int numSegments) {

    }

    public void addText(ImVec2 pos, int col, String text) {

    }

    public void addText(ImFont font, float fontSize, ImVec2 pos, int col, String text) { addText(font, fontSize, pos, col, text, 0.0f); }
    public void addText(ImFont font, float fontSize, ImVec2 pos, int col, String text, float wrapWidth) { addText(font, fontSize, pos, col, text, wrapWidth, null); }
    public void addText(ImFont font, float fontSize, ImVec2 pos, int col, String text, float wrapWidth, ImVec4 cpuFineClipRect) {

    }

    public void addPolyline(ImVec2[] points, int col, int flags, float thickness) {

    }

    public void addConvexPolyFilled(ImVec2[] points, int col) {

    }

    public void addBezierCubic(ImVec2 p1, ImVec2 p2, ImVec2 p3, ImVec2 p4, int col, float thickness) { addBezierCubic(p1, p2, p3, p4, col, thickness, 0); }
    public void addBezierCubic(ImVec2 p1, ImVec2 p2, ImVec2 p3, ImVec2 p4, int col, float thickness, int numSegments) {

    }

    public void addBezierQuadratic(ImVec2 p1, ImVec2 p2, ImVec2 p3, int col, float thickness) { addBezierQuadratic(p1, p2, p3, col, thickness, 0); }
    public void addBezierQuadratic(ImVec2 p1, ImVec2 p2, ImVec2 p3, int col, float thickness, int numSegments) {

    }

    public void addImage(Object userTextureId, ImVec2 pMin, ImVec2 pMax) { addImage(userTextureId, pMin, pMax, new ImVec2(0, 0)); }
    public void addImage(Object userTextureId, ImVec2 pMin, ImVec2 pMax, ImVec2 uvMin) { addImage(userTextureId, pMin, pMax, uvMin, new ImVec2(1, 1)); }
    public void addImage(Object userTextureId, ImVec2 pMin, ImVec2 pMax, ImVec2 uvMin, ImVec2 uvMax) { addImage(userTextureId, pMin, pMax, uvMin, uvMax, IM_COL32_WHITE);}
    public void addImage(Object userTextureId, ImVec2 pMin, ImVec2 pMax, ImVec2 uvMin, ImVec2 uvMax, int col) {

    }

    public void addImageQuad(Object userTextureId, ImVec2 p1, ImVec2 p2, ImVec2 p3, ImVec2 p4) { addImageQuad(userTextureId, p1, p2, p3, p4, new ImVec2(0, 0)); }
    public void addImageQuad(Object userTextureId, ImVec2 p1, ImVec2 p2, ImVec2 p3, ImVec2 p4, ImVec2 uv1) { addImageQuad(userTextureId, p1, p2, p3, p4, uv1, new ImVec2(1, 0)); }
    public void addImageQuad(Object userTextureId, ImVec2 p1, ImVec2 p2, ImVec2 p3, ImVec2 p4, ImVec2 uv1, ImVec2 uv2) { addImageQuad(userTextureId, p1, p2, p3, p4, uv1, uv2, new ImVec2(1, 1)); }
    public void addImageQuad(Object userTextureId, ImVec2 p1, ImVec2 p2, ImVec2 p3, ImVec2 p4, ImVec2 uv1, ImVec2 uv2, ImVec2 uv3) { addImageQuad(userTextureId, p1, p2, p3, p4, uv1, uv2, uv3, new ImVec2(0, 1)); }
    public void addImageQuad(Object userTextureId, ImVec2 p1, ImVec2 p2, ImVec2 p3, ImVec2 p4, ImVec2 uv1, ImVec2 uv2, ImVec2 uv3, ImVec2 uv4) { addImageQuad(userTextureId, p1, p2, p3, p4, uv1, uv2, uv3, uv4, IM_COL32_WHITE); }
    public void addImageQuad(Object userTextureId, ImVec2 p1, ImVec2 p2, ImVec2 p3, ImVec2 p4, ImVec2 uv1, ImVec2 uv2, ImVec2 uv3, ImVec2 uv4, int col) {

    }

    public void addImageRounded(Object userTextureId, ImVec2 pMin, ImVec2 pMax, ImVec2 uvMin, ImVec2 uvMax, int col, float rounding) { addImageRounded(userTextureId, pMin, pMax, uvMin, uvMax, col, rounding, 0); }
    public void addImageRounded(Object userTextureId, ImVec2 pMin, ImVec2 pMax, ImVec2 uvMin, ImVec2 uvMax, int col, float rounding, int flags) {

    }

    public void pathClear() {
        _Path.clear();
    }

    public void pathLineTo(ImVec2 pos) {
        _Path.pushBack(pos);
    }

    public void pathLineToMergeDuplicate(ImVec2 pos) {
        if (_Path.size() == 0 || !_Path.get(_Path.size() - 1).equals(pos)) {
            _Path.pushBack(pos);
        }
    }

    public void pathFillConvex(int col) {
        addConvexPolyFilled(_Path.data(new ImVec2[0]), col);
        _Path.clear();
    }

    public void pathStroke(int col) { pathStroke(col, 0); }
    public void pathStroke(int col, int flags) { pathStroke(col, flags, 1.0f); }
    public void pathStroke(int col, int flags, float thickness) {
        addPolyline(_Path.data(new ImVec2[0]), col, flags, thickness);
        _Path.clear();
    }

    public void pathArcTo(ImVec2 center, float radius, float aMin, float aMax) { pathArcTo(center, radius, aMin, aMax, 0); }
    public void pathArcTo(ImVec2 center, float radius, float aMin, float aMax, int numSegments) {

    }

    public void pathArcToFast(ImVec2 center, float radius, int aMinOf12, int aMaxOf12) {

    }

    public void pathBezierCubicCurveTo(ImVec2 p2, ImVec2 p3, ImVec2 p4) { pathBezierCubicCurveTo(p2, p3, p4, 0); }
    public void pathBezierCubicCurveTo(ImVec2 p2, ImVec2 p3, ImVec2 p4, int numSegments) {

    }

    public void pathBezierQuadraticCurveTo(ImVec2 p2, ImVec2 p3) { pathBezierQuadraticCurveTo(p2, p3, 0); }
    public void pathBezierQuadraticCurveTo(ImVec2 p2, ImVec2 p3, int numSegments) {

    }

    public void pathRect(ImVec2 rectMin, ImVec2 rectMax) { pathRect(rectMin, rectMax, 0.0f); }
    public void pathRect(ImVec2 rectMin, ImVec2 rectMax, float rounding) { pathRect(rectMin, rectMax, rounding, 0); }
    public void pathRect(ImVec2 rectMin, ImVec2 rectMax, float rounding, int flags) {

    }

    public void addCallback(ImDrawCallback callback, Object callbackData) {

    }

    public void addDrawCmd() {

    }

    public ImDrawList cloneOutput() {
        return null;
    }

    public void channelsSplit(int count) {
        _Splitter.split(this, count);
    }

    public void channelsMerge() {
        _Splitter.merge(this);
    }

    public void channelsSetCurrent(int n) {
        _Splitter.setCurrentChannel(this, n);
    }

    public void primReserve(int idxCount, int vtxCount) {

    }

    public void primUnreserve(int idxCount, int vtxCount) {

    }

    public void primRect(ImVec2 a, ImVec2 b, int col) {

    }

    public void primRectUV(ImVec2 a, ImVec2 b, ImVec2 uvA, ImVec2 uvB, int col) {

    }

    public void primQuadUV(ImVec2 a, ImVec2 b, ImVec2 c, ImVec2 d, ImVec2 uvA, ImVec2 uvB, ImVec2 uvC, ImVec2 uvD, int col) {

    }

    public void primWriteVtx(ImVec2 pos, ImVec2 uv, int col) {
        ImDrawVert vtx = vtxBuffer.get(_VtxWritePtr);
        vtx.pos = pos;
        vtx.uv = uv;
        vtx.col = col;
        _VtxWritePtr++;
        _VtxCurrentIdx++;
    }

    public void primWriteIdx(int idx) {
        idxBuffer.set(_IdxWritePtr, idx);
        _IdxWritePtr++;
    }
}
