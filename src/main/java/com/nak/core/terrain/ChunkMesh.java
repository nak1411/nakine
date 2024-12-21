package com.nak.core.terrain;

import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class ChunkMesh {

    private static List<BlockVertex> vertices;
    private List<Float> positionsList;
    private List<Float> texturePosList;
    private List<Float> normalsList;

    public float[] positions, texturePos, normals;

    public Chunk chunk;

    public ChunkMesh(Chunk chunk) {
        this.chunk = chunk;
        vertices = new ArrayList<>();
        positionsList = new ArrayList<>();
        texturePosList = new ArrayList<>();
        normalsList = new ArrayList<>();

        buildMesh();
        populateLists();
    }

    public void update(Chunk chunk) {
        this.chunk = chunk;

        buildMesh();
        populateLists();
    }

    private void buildMesh() {

        //***********CALCULATE FACES TO RENDER OR CULL ***********************************//
        for (int i = 0; i < chunk.blocks.size(); i++) {
            Block blockI = chunk.blocks.get(i);
            boolean px = false, nx = false, py = false, ny = false, pz = false, nz = false;

            for (int j = 0; j < chunk.blocks.size(); j++) {
                Block blockJ = chunk.blocks.get(j);

                //PX
                if (((blockI.getPos().x + 1) == (blockJ.getPos().x)) && ((blockI.getPos().y) == (blockJ.getPos().y)) && ((blockI.getPos().z) == (blockJ.getPos().z))) {
                    px = true;
                }
                //NX
                if (((blockI.getPos().x - 1) == (blockJ.getPos().x)) && ((blockI.getPos().y) == (blockJ.getPos().y)) && ((blockI.getPos().z) == (blockJ.getPos().z))) {
                    nx = true;
                }
                //PY
                if (((blockI.getPos().x) == (blockJ.getPos().x)) && ((blockI.getPos().y + 1) == (blockJ.getPos().y)) && ((blockI.getPos().z) == (blockJ.getPos().z))) {
                    py = true;
                }
                //NY
                if (((blockI.getPos().x) == (blockJ.getPos().x)) && ((blockI.getPos().y - 1) == (blockJ.getPos().y)) && ((blockI.getPos().z) == (blockJ.getPos().z))) {
                    ny = true;
                }
                //PZ
                if (((blockI.getPos().x) == (blockJ.getPos().x)) && ((blockI.getPos().y) == (blockJ.getPos().y)) && ((blockI.getPos().z + 1) == (blockJ.getPos().z))) {
                    pz = true;
                }
                //NZ
                if (((blockI.getPos().x) == (blockJ.getPos().x)) && ((blockI.getPos().y) == (blockJ.getPos().y)) && ((blockI.getPos().z - 1) == (blockJ.getPos().z))) {
                    nz = true;
                }
            }

            //********************************* Add faces to visible blocks********************************************//
            if (!px) {
                for (int k = 0; k < 6; k++) {
                    vertices.add(new BlockVertex(new Vector3f(BlockModel.PX_POS[k].x + blockI.getPos().x, BlockModel.PX_POS[k].y + blockI.getPos().y, BlockModel.PX_POS[k].z + blockI.getPos().z),
                            BlockModel.UV_PX[(blockI.type * 6) + k], BlockModel.NORMALS[k]));
                }
            }
            if (!nx) {
                for (int k = 0; k < 6; k++) {
                    vertices.add(new BlockVertex(new Vector3f(BlockModel.NX_POS[k].x + blockI.getPos().x, BlockModel.NX_POS[k].y + blockI.getPos().y, BlockModel.NX_POS[k].z + blockI.getPos().z),
                            BlockModel.UV_NX[(blockI.type * 6) + k], BlockModel.NORMALS[k]));
                }
            }
            if (!py) {
                for (int k = 0; k < 6; k++) {
                    vertices.add(new BlockVertex(new Vector3f(BlockModel.PY_POS[k].x + blockI.getPos().x, BlockModel.PY_POS[k].y + blockI.getPos().y, BlockModel.PY_POS[k].z + blockI.getPos().z),
                            BlockModel.UV_PY[(blockI.type * 6) + k], BlockModel.NORMALS[k]));
                }
            }
            if (!ny) {
                for (int k = 0; k < 6; k++) {
                    vertices.add(new BlockVertex(new Vector3f(BlockModel.NY_POS[k].x + blockI.getPos().x, BlockModel.NY_POS[k].y + blockI.getPos().y, BlockModel.NY_POS[k].z + blockI.getPos().z),
                            BlockModel.UV_NY[(blockI.type * 6) + k], BlockModel.NORMALS[k]));
                }
            }
            if (!pz) {
                for (int k = 0; k < 6; k++) {
                    vertices.add(new BlockVertex(new Vector3f(BlockModel.PZ_POS[k].x + blockI.getPos().x, BlockModel.PZ_POS[k].y + blockI.getPos().y, BlockModel.PZ_POS[k].z + blockI.getPos().z),
                            BlockModel.UV_PZ[(blockI.type * 6) + k], BlockModel.NORMALS[k]));
                }
            }
            if (!nz) {
                for (int k = 0; k < 6; k++) {
                    vertices.add(new BlockVertex(new Vector3f(BlockModel.NZ_POS[k].x + blockI.getPos().x, BlockModel.NZ_POS[k].y + blockI.getPos().y, BlockModel.NZ_POS[k].z + blockI.getPos().z),
                            BlockModel.UV_NZ[(blockI.type * 6) + k], BlockModel.NORMALS[k]));
                }
            }
        }
    }

    private void populateLists() {
        for (int i = 0; i < vertices.size(); i++) {
            positionsList.add(vertices.get(i).positions.x);
            positionsList.add(vertices.get(i).positions.y);
            positionsList.add(vertices.get(i).positions.z);
            texturePosList.add(vertices.get(i).texturePos.x);
            texturePosList.add(vertices.get(i).texturePos.y);
            normalsList.add(vertices.get(i).normals.x);
            normalsList.add(vertices.get(i).normals.y);
            normalsList.add(vertices.get(i).normals.z);
        }

        positions = new float[positionsList.size()];
        texturePos = new float[texturePosList.size()];
        normals = new float[normalsList.size()];

        for (int i = 0; i < positionsList.size(); i++) {
            positions[i] = positionsList.get(i);
        }
        for (int i = 0; i < texturePosList.size(); i++) {
            texturePos[i] = texturePosList.get(i);
        }
        for (int i = 0; i < normalsList.size(); i++) {
            normals[i] = normalsList.get(i);
        }

        positionsList.clear();
        texturePosList.clear();
        normalsList.clear();
    }

    public static List<BlockVertex> getVertices() {
        return vertices;
    }
}
