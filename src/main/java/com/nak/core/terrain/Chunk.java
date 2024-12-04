package com.nak.core.terrain;

import com.nak.core.entities.Camera;
import com.nak.core.entities.Entity;
import com.nak.core.entities.SceneManager;
import com.nak.core.opengl.Loader;
import com.nak.core.opengl.Model;
import com.nak.core.opengl.ModelLoader;
import com.nak.core.textures.Texture;
import com.nak.core.util.Constants;
import imgui.type.ImFloat;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class Chunk {

    private SceneManager scene;

    private Model cube, outline;
    private List<Vector3f> usedPos = new ArrayList<>();

    public Chunk(SceneManager scene, Loader loader) {
        this.scene = scene;

        cube = ModelLoader.loadModel(loader, "/models/TestCube.obj");
        cube.setTexture(new Texture(loader.loadTexture("textures/grassblock.png")), 1.0f);
        cube.getMaterial().setDisableCulling(false);
        outline = ModelLoader.loadModel(loader, "/models/TestCube.obj");
        outline.setTexture(new Texture(loader.loadTexture("textures/grassblock.png")), 1.0f);
        outline.getMaterial().setDisableCulling(false);
    }

    public void render(){
        int id = 0;
        ImFloat entityScale = new ImFloat(1);
        for (int i = 0; i < Camera.getPosition().x + Constants.NUM_ENTITIES.get(); i++) {
            id += i;
            for (int j = 0; j < Camera.getPosition().x + Constants.NUM_ENTITIES.get(); j++) {
                id += j / 2;
                if (!usedPos.contains(new Vector3f(i * 2, 0, j * 2))) {
                    scene.addEntity(new Entity(id, cube, new Vector3f(i * 2, 0, j * 2), new Vector3f(0, 0, 0), entityScale));
                    scene.addOutline(new Entity(id, outline, new Vector3f(i * 2, 0, j * 2), new Vector3f(0, 0, 0), entityScale));
                    usedPos.add(new Vector3f(i * 2, 0, j * 2));
                }
            }
        }
    }
}
