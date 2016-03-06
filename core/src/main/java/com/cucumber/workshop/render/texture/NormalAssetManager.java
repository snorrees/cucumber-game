package com.cucumber.workshop.render.texture;

import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.cucumber.workshop.ResourceDirectory;
import net.mostlyoriginal.api.manager.AssetManager;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Snorre E. Brekke - snorre.e.brekke@gmail.com
 */
public class NormalAssetManager extends AssetManager<NormalRef, NormalTextureComponent> {

    @Wire
    ResourceDirectory resources;

    private Map<String, Texture> textures = new HashMap<>();

    public NormalAssetManager() {
        super(NormalRef.class, NormalTextureComponent.class);
    }

    @Override
    public void setup(Entity entity, NormalRef textureRef, NormalTextureComponent textureComponent) {
        if (isEnabled()) {
            textureComponent.texture = textures.computeIfAbsent(textureRef.id,
                                                                this::createTexture);
        }
    }

    private Texture createTexture(String id) {
        return new Texture(Gdx.files.internal(resources.dir + "/img/" + id));
    }
}
