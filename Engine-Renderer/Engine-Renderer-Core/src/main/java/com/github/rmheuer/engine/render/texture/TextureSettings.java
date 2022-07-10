package com.github.rmheuer.engine.render.texture;

public final class TextureSettings {
    private TextureFilter minFilter = TextureFilter.NEAREST;
    private TextureFilter magFilter = TextureFilter.NEAREST;

    public TextureSettings() {}

    public TextureSettings(TextureFilter minFilter, TextureFilter magFilter) {
	this.minFilter = minFilter;
	this.magFilter = magFilter;
    }

    public TextureFilter getMinFilter() {
	return minFilter;
    }

    public TextureSettings setMinFilter(TextureFilter minFilter) {
	this.minFilter = minFilter;
	return this;
    }

    public TextureFilter getMagFilter() {
	return magFilter;
    }

    public TextureSettings setMagFilter(TextureFilter magFilter) {
	this.magFilter = magFilter;
	return this;
    }

    public TextureSettings setFilters(TextureFilter filter) {
	minFilter = filter;
	magFilter = filter;
	return this;
    }
}
