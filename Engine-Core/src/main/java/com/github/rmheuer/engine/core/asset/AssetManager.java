package com.github.rmheuer.engine.core.asset;

import com.github.rmheuer.engine.core.resource.ResourceFile;
import com.github.rmheuer.engine.core.util.InstanceMap;

import java.util.HashMap;
import java.util.Map;

public final class AssetManager {
    private final Map<ResourceFile, InstanceMap<Asset>> assets;
    private final Map<Class<? extends Asset>, AssetSerializer<? extends Asset>> serializers;

    public AssetManager() {
        assets = new HashMap<>();
        serializers = new HashMap<>();
    }

    private <T extends Asset> AssetSerializer<T> getSerializer(Class<T> type) {
        AssetSerializer<? extends Asset> serializer = serializers.get(type);

        @SuppressWarnings("unchecked")
        AssetSerializer<T> cast = (AssetSerializer<T>) serializer;

        return cast;
    }

    private <T extends Asset> T loadAsset(ResourceFile key, Class<T> type) {
        AssetSerializer<T> serializer = getSerializer(type);
        return serializer.load(key);
    }

    public <T extends Asset> T getAsset(ResourceFile key, Class<T> type) {
        InstanceMap<Asset> map = assets.computeIfAbsent(key, (k) -> new InstanceMap<>());
        T asset = map.getInstance(type);
        if (asset != null)
            return asset;

        asset = loadAsset(key, type);
        System.out.println("Asset loaded: " + key + ": " + type);
        asset.addFreeListener(() -> {
            map.removeInstance(type);
            System.out.println("Asset freed: " + key + ": " + type);
        });
        map.setInstance(asset);

        return asset;
    }

    public <T extends Asset> void registerSerializer(Class<T> type, AssetSerializer<T> serializer) {
        serializers.put(type, serializer);
    }
}
