package com.github.rmheuer.engine.render.texture;

import com.github.rmheuer.engine.core.nat.NativeObject;
import com.github.rmheuer.engine.core.nat.NativeObjectManager;
import com.github.rmheuer.engine.render.RenderBackend;

/**
 * Represents a six-sided cube map texture. A common use
 * case for cube maps is a skybox. Since it is a cube, all
 * face images must be square. The texture filters are set
 * for the whole cube map, so the individual faces' filters
 * are ignored.
 *
 * @author rmheuer
 */
public final class CubeMap implements Texture {
    private Image px, py, pz;
    private Image nx, ny, nz;
    private TextureFilter minFilter, magFilter;

    private boolean pxDirty, pyDirty, pzDirty;
    private boolean nxDirty, nyDirty, nzDirty;
    private boolean filtersDirty;

    private Native nat;

    public CubeMap() {
        minFilter = TextureFilter.LINEAR;
        magFilter = TextureFilter.LINEAR;
        filtersDirty = true;
    }

    public CubeMap(Image px, Image nx, Image py, Image ny, Image pz, Image nz) {
        this();
        this.px = px; px.addDataChangeListener(this::pxChange); pxDirty = true;
        this.py = py; py.addDataChangeListener(this::pyChange); pyDirty = true;
        this.pz = pz; pz.addDataChangeListener(this::pzChange); pzDirty = true;
        this.nx = nx; nx.addDataChangeListener(this::nxChange); nxDirty = true;
        this.ny = ny; ny.addDataChangeListener(this::nyChange); nyDirty = true;
        this.nz = nz; nz.addDataChangeListener(this::nzChange); nzDirty = true;
    }

    public Image getPosX() {
        return px;
    }

    public void setPosX(Image px) {
        if (this.px != null)
            this.px.removeDataChangeListener(this::pxChange);
        px.addDataChangeListener(this::pxChange);
        this.px = px;
        pxDirty = true;
    }

    public Image getPosY() {
        return py;
    }

    public void setPosY(Image py) {
        if (this.py != null)
            this.py.removeDataChangeListener(this::pyChange);
        py.addDataChangeListener(this::pyChange);
        this.py = py;
        pyDirty = true;
    }

    public Image getPosZ() {
        return pz;
    }

    public void setPosZ(Image pz) {
        if (this.pz != null)
            this.pz.removeDataChangeListener(this::pzChange);
        pz.addDataChangeListener(this::pzChange);
        this.pz = pz;
        pzDirty = true;
    }

    public Image getNegX() {
        return nx;
    }

    public void setNegX(Image nx) {
        if (this.nx != null)
            this.nx.removeDataChangeListener(this::nxChange);
        nx.addDataChangeListener(this::nxChange);
        this.nx = nx;
        nxDirty = true;
    }

    public Image getNegY() {
        return ny;
    }

    public void setNegY(Image ny) {
        if (this.ny != null)
            this.ny.removeDataChangeListener(this::nyChange);
        ny.addDataChangeListener(this::nyChange);
        this.ny = ny;
        nyDirty = true;
    }

    public Image getNegZ() {
        return nz;
    }

    public void setNegZ(Image nz) {
        if (this.nz != null)
            this.nz.removeDataChangeListener(this::nzChange);
        nz.addDataChangeListener(this::nzChange);
        this.nz = nz;
        nzDirty = true;
    }

    public TextureFilter getMinFilter() {
        return minFilter;
    }

    public void setMinFilter(TextureFilter minFilter) {
        this.minFilter = minFilter;
        filtersDirty = true;
    }

    public TextureFilter getMagFilter() {
        return magFilter;
    }

    public void setMagFilter(TextureFilter magFilter) {
        this.magFilter = magFilter;
        filtersDirty = true;
    }

    private void pxChange(Image nx) { pxDirty = true; }
    private void pyChange(Image nx) { pyDirty = true; }
    private void pzChange(Image nx) { pzDirty = true; }
    private void nxChange(Image nx) { nxDirty = true; }
    private void nyChange(Image nx) { nyDirty = true; }
    private void nzChange(Image nx) { nzDirty = true; }

    public interface Native extends Texture.Native {
        void setPosXImage(Image posX);
        void setNegXImage(Image negX);
        void setPosYImage(Image posY);
        void setNegYImage(Image negY);
        void setPosZImage(Image posZ);
        void setNegZImage(Image negZ);

        void setFilters(TextureFilter minFilter, TextureFilter magFilter);

        void bindToSlot(int slot);
    }

    @Override
    public Native getNative(NativeObjectManager mgr) {
        if (px == null || py == null || pz == null || nx == null || ny == null || nz == null)
            throw new IllegalStateException("Not all faces are set");

        if (nat == null) {
            nat = RenderBackend.get().createCubeMapNative();
            mgr.registerObject(nat);
        }

        if (pxDirty) { nat.setPosXImage(px); pxDirty = false; }
        if (pyDirty) { nat.setPosYImage(py); pyDirty = false; }
        if (pzDirty) { nat.setPosZImage(pz); pzDirty = false; }
        if (nxDirty) { nat.setNegXImage(nx); nxDirty = false; }
        if (nyDirty) { nat.setNegYImage(ny); nyDirty = false; }
        if (nzDirty) { nat.setNegZImage(nz); nzDirty = false; }

        if (filtersDirty) {
            nat.setFilters(minFilter, magFilter);
            filtersDirty = false;
        }

        return nat;
    }
}
