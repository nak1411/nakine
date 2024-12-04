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
    ImFloat entityScale = new ImFloat(1);
    int id = 0;

    public Chunk(SceneManager scene, Loader loader) {
        this.scene = scene;

          cube = loader.loadModel(Block.vertices, Block.texturePos, Block.indices);
          cube.setTexture(new Texture(loader.loadTexture("textures/grassblock.png")), 1.0f);
          cube.getMaterial().setDisableCulling(false);
          outline = loader.loadModel(Block.vertices, Block.texturePos, Block.indices);
          outline.setTexture(new Texture(loader.loadTexture("textures/grassblock.png")), 1.0f);
          outline.getMaterial().setDisableCulling(false);

//        cube = ModelLoader.loadModel(loader, "/models/TestCube.obj");
//        cube.setTexture(new Texture(loader.loadTexture("textures/grassblock.png")), 1.0f);
//        cube.getMaterial().setDisableCulling(false);
//        outline = ModelLoader.loadModel(loader, "/models/TestCube.obj");
//        outline.setTexture(new Texture(loader.loadTexture("textures/grassblock.png")), 1.0f);
//        outline.getMaterial().setDisableCulling(false);
    }

    public void renderThreadOne() {
        // Quad ONE
        for (int i = (int) (Camera.getPosition().x - Constants.NUM_ENTITIES.get()); i < Camera.getPosition().x; i++) {
            id += i;
            for (int j = (int) (Camera.getPosition().z); j < Camera.getPosition().z + Constants.NUM_ENTITIES.get(); j++) {
                id += j / 2;
                if (!usedPos.contains(new Vector3f(i, 0, j))) {
                    scene.addEntity(new Entity(id, cube, new Vector3f(i, 0, j), new Vector3f(0, 0, 0), entityScale));
                    scene.addOutline(new Entity(id, outline, new Vector3f(i, 0, j), new Vector3f(0, 0, 0), entityScale));
                    usedPos.add(new Vector3f(i, 0, j));
                }
            }
        }
        // Quad TWO
        for (int i = (int) Camera.getPosition().x; i < Camera.getPosition().x + Constants.NUM_ENTITIES.get(); i++) {
            id += i;
            for (int j = (int) (Camera.getPosition().z); j < Camera.getPosition().z + Constants.NUM_ENTITIES.get(); j++) {
                id += j / 2;
                if (!usedPos.contains(new Vector3f(i, 0, j))) {
                    scene.addEntity(new Entity(id, cube, new Vector3f(i, 0, j), new Vector3f(0, 0, 0), entityScale));
                    scene.addOutline(new Entity(id, outline, new Vector3f(i, 0, j), new Vector3f(0, 0, 0), entityScale));
                    usedPos.add(new Vector3f(i, 0, j));
                }
            }
        }
    }

    public void renderThreadTwo() {
        // Quad THREE
        for (int i = (int) (Camera.getPosition().x - Constants.NUM_ENTITIES.get()); i < Camera.getPosition().x; i++) {
            id += i;
            for (int j = (int) (Camera.getPosition().z - Constants.NUM_ENTITIES.get()); j < Camera.getPosition().z; j++) {
                id += j / 2;
                if (!usedPos.contains(new Vector3f(i, 0, j))) {
                    scene.addEntity(new Entity(id, cube, new Vector3f(i, 0, j), new Vector3f(0, 0, 0), entityScale));
                    scene.addOutline(new Entity(id, outline, new Vector3f(i, 0, j), new Vector3f(0, 0, 0), entityScale));
                    usedPos.add(new Vector3f(i, 0, j));
                }
            }
        }
        // Quad FOUR
        for (int i = (int) Camera.getPosition().x; i < Camera.getPosition().x + Constants.NUM_ENTITIES.get(); i++) {
            id += i;
            for (int j = (int) (Camera.getPosition().z - Constants.NUM_ENTITIES.get()); j < Camera.getPosition().z; j++) {
                id += j / 2;
                if (!usedPos.contains(new Vector3f(i, 0, j))) {
                    scene.addEntity(new Entity(id, cube, new Vector3f(i, 0, j), new Vector3f(0, 0, 0), entityScale));
                    scene.addOutline(new Entity(id, outline, new Vector3f(i, 0, j), new Vector3f(0, 0, 0), entityScale));
                    usedPos.add(new Vector3f(i, 0, j));
                }
            }
        }
    }
}
