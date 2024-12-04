package com.nak.core.rendering;

import com.nak.core.entities.Camera;
import com.nak.core.lighting.DirectionalLight;
import com.nak.core.lighting.PointLight;
import com.nak.core.lighting.SpotLight;
import com.nak.core.opengl.Model;

public interface Renderer<T> {

    public void init() throws Exception;

    public void render(int clickedObject, ShaderManager shader, Camera camera, PointLight[] pointLights, SpotLight[] spotLights, DirectionalLight directionalLight);

    abstract void bind(Model model);

    public void unbind();

    public void cleanup();
}
