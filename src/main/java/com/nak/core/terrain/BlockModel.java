package com.nak.core.terrain;

import org.joml.Vector2f;
import org.joml.Vector3f;

public class BlockModel {
    public static Vector3f[] PX_POS = {
            new Vector3f(0.5f,0.5f,-0.5f),
            new Vector3f(0.5f,-0.5f,-0.5f),
            new Vector3f(0.5f,-0.5f,0.5f),
            new Vector3f(0.5f,-0.5f,0.5f),
            new Vector3f(0.5f,0.5f,0.5f),
            new Vector3f(0.5f,0.5f,-0.5f)
    };

    public static Vector3f[] NX_POS = {
            new Vector3f(-0.5f,0.5f,-0.5f),
            new Vector3f(-0.5f,-0.5f,-0.5f),
            new Vector3f(-0.5f,-0.5f,0.5f),
            new Vector3f(-0.5f,-0.5f,0.5f),
            new Vector3f(-0.5f,0.5f,0.5f),
            new Vector3f(-0.5f,0.5f,-0.5f)
    };

    public static Vector3f[] PY_POS = {
            new Vector3f(-0.5f,0.5f,0.5f),
            new Vector3f(-0.5f,0.5f,-0.5f),
            new Vector3f(0.5f,0.5f,-0.5f),
            new Vector3f(0.5f,0.5f,-0.5f),
            new Vector3f(0.5f,0.5f,0.5f),
            new Vector3f(-0.5f,0.5f,0.5f)
    };

    public static Vector3f[] NY_POS = {
            new Vector3f(-0.5f,-0.5f,0.5f),
            new Vector3f(-0.5f,-0.5f,-0.5f),
            new Vector3f(0.5f,-0.5f,-0.5f),
            new Vector3f(0.5f,-0.5f,-0.5f),
            new Vector3f(0.5f,-0.5f,0.5f),
            new Vector3f(-0.5f,-0.5f,0.5f)
    };

    public static Vector3f[] PZ_POS = {
            new Vector3f(-0.5f,0.5f,0.5f),
            new Vector3f(-0.5f,-0.5f,0.5f),
            new Vector3f(0.5f,-0.5f,0.5f),
            new Vector3f(0.5f,-0.5f,0.5f),
            new Vector3f(0.5f,0.5f,0.5f),
            new Vector3f(-0.5f,0.5f,0.5f)
    };

    public static Vector3f[] NZ_POS = {
            new Vector3f(-0.5f,0.5f,-0.5f),
            new Vector3f(-0.5f,-0.5f,-0.5f),
            new Vector3f(0.5f,-0.5f,-0.5f),
            new Vector3f(0.5f,-0.5f,-0.5f),
            new Vector3f(0.5f,0.5f,-0.5f),
            new Vector3f(-0.5f,0.5f,-0.5f)
    };


    public static Vector2f[] TEXTURE_POS = {
            new Vector2f(0.f, 0.f),
            new Vector2f(0.f, 1.f),
            new Vector2f(1.f, 1.f),
            new Vector2f(1.f, 1.f),
            new Vector2f(1.f, 0.f),
            new Vector2f(0.f, 0.f)
    };

    public static Vector2f[] UV_PX = {
            // GRASS
            new Vector2f(1.f / 16.f, 0.f),
            new Vector2f(1.f / 16.f, 1.f / 16.f),
            new Vector2f(2.f / 16.f, 1.f / 16.f),
            new Vector2f(2.f / 16.f, 1.f / 16.f),
            new Vector2f(2.f / 16.f, 0.f / 16.f),
            new Vector2f(1.f / 16.f, 0.f),

            // DIRT
            new Vector2f(2.f / 16.f, 0.f),
            new Vector2f(2.f / 16.f, 1.f / 16.f),
            new Vector2f(3.f / 16.f, 1.f / 16.f),
            new Vector2f(3.f / 16.f, 1.f / 16.f),
            new Vector2f(3.f / 16.f, 0.f / 16.f),
            new Vector2f(2.f / 16.f, 0.f),

            // STONE
            new Vector2f(3.f / 16.f, 0.f),
            new Vector2f(3.f / 16.f, 1.f / 16.f),
            new Vector2f(4.f / 16.f, 1.f / 16.f),
            new Vector2f(4.f / 16.f, 1.f / 16.f),
            new Vector2f(4.f / 16.f, 0.f / 16.f),
            new Vector2f(3.f / 16.f, 0.f),

            // TREEBARK
            new Vector2f(4.f / 16.f, 0.f),
            new Vector2f(4.f / 16.f, 1.f / 16.f),
            new Vector2f(5.f / 16.f, 1.f / 16.f),
            new Vector2f(5.f / 16.f, 1.f / 16.f),
            new Vector2f(5.f / 16.f, 0.f / 16.f),
            new Vector2f(4.f / 16.f, 0.f),

            // TREELEAF
            new Vector2f(6.f / 16.f, 0.f),
            new Vector2f(6.f / 16.f, 1.f / 16.f),
            new Vector2f(7.f / 16.f, 1.f / 16.f),
            new Vector2f(7.f / 16.f, 1.f / 16.f),
            new Vector2f(7.f / 16.f, 0.f / 16.f),
            new Vector2f(6.f / 16.f, 0.f)
    };

    public static Vector2f[] UV_NX = {
            // GRASS
            new Vector2f(1.f / 16.f, 0.f),
            new Vector2f(1.f / 16.f, 1.f / 16.f),
            new Vector2f(2.f / 16.f, 1.f / 16.f),
            new Vector2f(2.f / 16.f, 1.f / 16.f),
            new Vector2f(2.f / 16.f, 0.f / 16.f),
            new Vector2f(1.f / 16.f, 0.f),

            // DIRT
            new Vector2f(2.f / 16.f, 0.f),
            new Vector2f(2.f / 16.f, 1.f / 16.f),
            new Vector2f(3.f / 16.f, 1.f / 16.f),
            new Vector2f(3.f / 16.f, 1.f / 16.f),
            new Vector2f(3.f / 16.f, 0.f / 16.f),
            new Vector2f(2.f / 16.f, 0.f),

            // STONE
            new Vector2f(3.f / 16.f, 0.f),
            new Vector2f(3.f / 16.f, 1.f / 16.f),
            new Vector2f(4.f / 16.f, 1.f / 16.f),
            new Vector2f(4.f / 16.f, 1.f / 16.f),
            new Vector2f(4.f / 16.f, 0.f / 16.f),
            new Vector2f(3.f / 16.f, 0.f),

            // TREEBARK
            new Vector2f(4.f / 16.f, 0.f),
            new Vector2f(4.f / 16.f, 1.f / 16.f),
            new Vector2f(5.f / 16.f, 1.f / 16.f),
            new Vector2f(5.f / 16.f, 1.f / 16.f),
            new Vector2f(5.f / 16.f, 0.f / 16.f),
            new Vector2f(4.f / 16.f, 0.f),

            // TREELEAF
            new Vector2f(6.f / 16.f, 0.f),
            new Vector2f(6.f / 16.f, 1.f / 16.f),
            new Vector2f(7.f / 16.f, 1.f / 16.f),
            new Vector2f(7.f / 16.f, 1.f / 16.f),
            new Vector2f(7.f / 16.f, 0.f / 16.f),
            new Vector2f(6.f / 16.f, 0.f)
    };


    public static Vector2f[] UV_PY = {
            // GRASS
            new Vector2f(0.f, 0.f),
            new Vector2f(0.f, 1.f / 16.f),
            new Vector2f(1.f / 16.f, 1.f / 16.f),
            new Vector2f(1.f / 16.f, 1.f / 16.f),
            new Vector2f(1.f / 16.f, 0.f),
            new Vector2f(0.f, 0.f),

            // DIRT
            new Vector2f(2.f / 16.f, 0.f),
            new Vector2f(2.f / 16.f, 1.f / 16.f),
            new Vector2f(3.f / 16.f, 1.f / 16.f),
            new Vector2f(3.f / 16.f, 1.f / 16.f),
            new Vector2f(3.f / 16.f, 0.f / 16.f),
            new Vector2f(2.f / 16.f, 0.f),

            // STONE
            new Vector2f(3.f / 16.f, 0.f),
            new Vector2f(3.f / 16.f, 1.f / 16.f),
            new Vector2f(4.f / 16.f, 1.f / 16.f),
            new Vector2f(4.f / 16.f, 1.f / 16.f),
            new Vector2f(4.f / 16.f, 0.f / 16.f),
            new Vector2f(3.f / 16.f, 0.f),

            // TREEBARK
            new Vector2f(5.f / 16.f, 0.f),
            new Vector2f(5.f / 16.f, 1.f / 16.f),
            new Vector2f(6.f / 16.f, 1.f / 16.f),
            new Vector2f(6.f / 16.f, 1.f / 16.f),
            new Vector2f(6.f / 16.f, 0.f / 16.f),
            new Vector2f(5.f / 16.f, 0.f),

            // TREELEAF
            new Vector2f(6.f / 16.f, 0.f),
            new Vector2f(6.f / 16.f, 1.f / 16.f),
            new Vector2f(7.f / 16.f, 1.f / 16.f),
            new Vector2f(7.f / 16.f, 1.f / 16.f),
            new Vector2f(7.f / 16.f, 0.f / 16.f),
            new Vector2f(6.f / 16.f, 0.f)

    };

    public static Vector2f[] UV_NY = {
            // GRASS
            new Vector2f(2.f / 16.f, 0.f),
            new Vector2f(2.f / 16.f, 1.f / 16.f),
            new Vector2f(3.f / 16.f, 1.f / 16.f),
            new Vector2f(3.f / 16.f, 1.f / 16.f),
            new Vector2f(3.f / 16.f, 0.f / 16.f),
            new Vector2f(2.f / 16.f, 0.f),

            // DIRT
            new Vector2f(2.f / 16.f, 0.f),
            new Vector2f(2.f / 16.f, 1.f / 16.f),
            new Vector2f(3.f / 16.f, 1.f / 16.f),
            new Vector2f(3.f / 16.f, 1.f / 16.f),
            new Vector2f(3.f / 16.f, 0.f / 16.f),
            new Vector2f(2.f / 16.f, 0.f),

            // STONE
            new Vector2f(3.f / 16.f, 0.f),
            new Vector2f(3.f / 16.f, 1.f / 16.f),
            new Vector2f(4.f / 16.f, 1.f / 16.f),
            new Vector2f(4.f / 16.f, 1.f / 16.f),
            new Vector2f(4.f / 16.f, 0.f / 16.f),
            new Vector2f(3.f / 16.f, 0.f),

            // TREEBARK
            new Vector2f(5.f / 16.f, 0.f),
            new Vector2f(5.f / 16.f, 1.f / 16.f),
            new Vector2f(6.f / 16.f, 1.f / 16.f),
            new Vector2f(6.f / 16.f, 1.f / 16.f),
            new Vector2f(6.f / 16.f, 0.f / 16.f),
            new Vector2f(5.f / 16.f, 0.f),

            // TREELEAF
            new Vector2f(6.f / 16.f, 0.f),
            new Vector2f(6.f / 16.f, 1.f / 16.f),
            new Vector2f(7.f / 16.f, 1.f / 16.f),
            new Vector2f(7.f / 16.f, 1.f / 16.f),
            new Vector2f(7.f / 16.f, 0.f / 16.f),
            new Vector2f(6.f / 16.f, 0.f)
    };

    public static Vector2f[] UV_PZ = {
            // GRASS
            new Vector2f(1.f / 16.f, 0.f),
            new Vector2f(1.f / 16.f, 1.f / 16.f),
            new Vector2f(2.f / 16.f, 1.f / 16.f),
            new Vector2f(2.f / 16.f, 1.f / 16.f),
            new Vector2f(2.f / 16.f, 0.f / 16.f),
            new Vector2f(1.f / 16.f, 0.f),

            // DIRT
            new Vector2f(2.f / 16.f, 0.f),
            new Vector2f(2.f / 16.f, 1.f / 16.f),
            new Vector2f(3.f / 16.f, 1.f / 16.f),
            new Vector2f(3.f / 16.f, 1.f / 16.f),
            new Vector2f(3.f / 16.f, 0.f / 16.f),
            new Vector2f(2.f / 16.f, 0.f),

            // STONE
            new Vector2f(3.f / 16.f, 0.f),
            new Vector2f(3.f / 16.f, 1.f / 16.f),
            new Vector2f(4.f / 16.f, 1.f / 16.f),
            new Vector2f(4.f / 16.f, 1.f / 16.f),
            new Vector2f(4.f / 16.f, 0.f / 16.f),
            new Vector2f(3.f / 16.f, 0.f),

            // TREEBARK
            new Vector2f(4.f / 16.f, 0.f),
            new Vector2f(4.f / 16.f, 1.f / 16.f),
            new Vector2f(5.f / 16.f, 1.f / 16.f),
            new Vector2f(5.f / 16.f, 1.f / 16.f),
            new Vector2f(5.f / 16.f, 0.f / 16.f),
            new Vector2f(4.f / 16.f, 0.f),

            // TREELEAF
            new Vector2f(6.f / 16.f, 0.f),
            new Vector2f(6.f / 16.f, 1.f / 16.f),
            new Vector2f(7.f / 16.f, 1.f / 16.f),
            new Vector2f(7.f / 16.f, 1.f / 16.f),
            new Vector2f(7.f / 16.f, 0.f / 16.f),
            new Vector2f(6.f / 16.f, 0.f)
    };


    public static Vector2f[] UV_NZ = {
            // GRASS
            new Vector2f(1.f / 16.f, 0.f),
            new Vector2f(1.f / 16.f, 1.f / 16.f),
            new Vector2f(2.f / 16.f, 1.f / 16.f),
            new Vector2f(2.f / 16.f, 1.f / 16.f),
            new Vector2f(2.f / 16.f, 0.f / 16.f),
            new Vector2f(1.f / 16.f, 0.f),

            // DIRT
            new Vector2f(2.f / 16.f, 0.f),
            new Vector2f(2.f / 16.f, 1.f / 16.f),
            new Vector2f(3.f / 16.f, 1.f / 16.f),
            new Vector2f(3.f / 16.f, 1.f / 16.f),
            new Vector2f(3.f / 16.f, 0.f / 16.f),
            new Vector2f(2.f / 16.f, 0.f),

            // STONE
            new Vector2f(3.f / 16.f, 0.f),
            new Vector2f(3.f / 16.f, 1.f / 16.f),
            new Vector2f(4.f / 16.f, 1.f / 16.f),
            new Vector2f(4.f / 16.f, 1.f / 16.f),
            new Vector2f(4.f / 16.f, 0.f / 16.f),
            new Vector2f(3.f / 16.f, 0.f),

            // TREEBARK
            new Vector2f(4.f / 16.f, 0.f),
            new Vector2f(4.f / 16.f, 1.f / 16.f),
            new Vector2f(5.f / 16.f, 1.f / 16.f),
            new Vector2f(5.f / 16.f, 1.f / 16.f),
            new Vector2f(5.f / 16.f, 0.f / 16.f),
            new Vector2f(4.f / 16.f, 0.f),

            // TREELEAF
            new Vector2f(6.f / 16.f, 0.f),
            new Vector2f(6.f / 16.f, 1.f / 16.f),
            new Vector2f(7.f / 16.f, 1.f / 16.f),
            new Vector2f(7.f / 16.f, 1.f / 16.f),
            new Vector2f(7.f / 16.f, 0.f / 16.f),
            new Vector2f(6.f / 16.f, 0.f)
    };


    public static Vector3f[] NORMALS = {
            new Vector3f(0.f, 0.f, 0.f),
            new Vector3f(0.f, 0.f, 0.f),
            new Vector3f(0.f, 0.f, 0.f),
            new Vector3f(0.f, 0.f, 0.f),
            new Vector3f(0.f, 0.f, 0.f),
            new Vector3f(0.f, 0.f, 0.f)
    };

    public static float[] vertices = {
            -0.5f,0.5f,-0.5f,
            -0.5f,-0.5f,-0.5f,
            0.5f,-0.5f,-0.5f,
            0.5f,0.5f,-0.5f,

            -0.5f,0.5f,0.5f,
            -0.5f,-0.5f,0.5f,
            0.5f,-0.5f,0.5f,
            0.5f,0.5f,0.5f,

            0.5f,0.5f,-0.5f,
            0.5f,-0.5f,-0.5f,
            0.5f,-0.5f,0.5f,
            0.5f,0.5f,0.5f,

            -0.5f,0.5f,-0.5f,
            -0.5f,-0.5f,-0.5f,
            -0.5f,-0.5f,0.5f,
            -0.5f,0.5f,0.5f,

            -0.5f,0.5f,0.5f,
            -0.5f,0.5f,-0.5f,
            0.5f,0.5f,-0.5f,
            0.5f,0.5f,0.5f,

            -0.5f,-0.5f,0.5f,
            -0.5f,-0.5f,-0.5f,
            0.5f,-0.5f,-0.5f,
            0.5f,-0.5f,0.5f
    };


    public static float[] texturePos = new float[]{
            // Side
            0.01f / 3f, 2.01f / 3f,
            0.01f / 3f, 2.99f / 3f,
            0.99f / 3f, 2.99f / 3f,
            0.99f / 3f, 2.01f / 3f,

            0.01f / 3f, 2.01f / 3f,
            0.01f / 3f, 2.99f / 3f,
            0.99f / 3f, 2.99f / 3f,
            0.99f / 3f, 2.01f / 3f,

            0.01f / 3f, 2.01f / 3f,
            0.01f / 3f, 2.99f / 3f,
            0.99f / 3f, 2.99f / 3f,
            0.99f / 3f, 2.01f / 3f,

            0.01f / 3f, 2.01f / 3f,
            0.01f / 3f, 2.99f / 3f,
            0.99f / 3f, 2.99f / 3f,
            0.99f / 3f, 2.01f / 3f,

            // Top
            2.01f / 3f, 1.01f / 3f,
            2.01f / 3f, 1.99f / 3f,
            2.99f / 3f, 1.99f / 3f,
            2.99f / 3f, 1.01f / 3f,

            // Bottom
            1.01f / 3f, 1.01f / 3f,
            1.01f / 3f, 1.99f / 3f,
            1.99f / 3f, 1.99f / 3f,
            1.99f / 3f, 1.01f / 3f,
    };

    public static int[] indices = new int[]{
            0, 1, 3, 3, 1, 2,
            4, 5, 7, 7, 5, 6,
            8, 9, 11, 11, 9, 10,
            12, 13, 15, 15, 13, 14,
            16, 17, 19, 19, 17, 18,
            20, 21, 23, 23, 21, 22
    };

    public static float[] normals = new float[]{
            -0.5f, 0.5f, -0.5f, // V0
            -0.5f, -0.5f, -0.5f, // V1
            0.5f, -0.5f, -0.5f, // V2
            0.5f, 0.5f, -0.5f, // V3

            -0.5f, 0.5f, 0.5f, // V4
            -0.5f, -0.5f, 0.5f, // V5
            0.5f, -0.5f, 0.5f, // V6
            0.5f, 0.5f, 0.5f, // V7

            0.5f, 0.5f, -0.5f, // V8
            0.5f, -0.5f, -0.5f, // V9
            0.5f, -0.5f, 0.5f, // V10
            0.5f, 0.5f, 0.5f, // V11

            -0.5f, 0.5f, -0.5f, // V12
            -0.5f, -0.5f, -0.5f, // V13
            -0.5f, -0.5f, 0.5f, // V14
            -0.5f, 0.5f, 0.5f, // V15

            -0.5f, 0.5f, 0.5f, // V16
            -0.5f, 0.5f, -0.5f, // V17
            0.5f, 0.5f, -0.5f, // V18
            0.5f, 0.5f, 0.5f, // V19

            -0.5f, -0.5f, 0.5f, // V20
            -0.5f, -0.5f, -0.5f, // V21
            0.5f, -0.5f, -0.5f, // V22
            0.5f, -0.5f, 0.5f, // V23
    };
}
