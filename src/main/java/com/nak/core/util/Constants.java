package com.nak.core.util;

import com.nak.core.entities.Entity;
import imgui.type.ImFloat;
import imgui.type.ImInt;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class Constants {

    public static final String TITLE = "NAKINE ";

    public static final float CAMERA_MOVE_SPEED = 0.1f;
    public static final float MOUSE_MOVE_SPEED = 0.2f;
    public static final float SPECULAR_POWER = 10f;

    public static ImInt NUM_ENTITIES = new ImInt(10);

    public static Vector4f CLEAR_COLOR = new Vector4f(0.1f, 0.09f, 0.1f, 1.0f);
    public static Vector4f OUTLINE_COLOR = new Vector4f(255.0f, 255.0f, 255.0f, 1.0f);
    public static ImFloat OUTLINE_SCALE = new ImFloat(1.03f);

    public static final Vector4f DEFAULT_COLOR = new Vector4f(1.0f, 1.0f, 1.0f, 1.0f);
    public static final Vector3f AMBIENT_COLOR = new Vector3f(0.3f, 0.3f, 0.3f);

}
