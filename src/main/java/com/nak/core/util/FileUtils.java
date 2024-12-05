package com.nak.core.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Objects;

public class FileUtils {

    /**
     * Create and return vert and frag shaders combined into one file
     **/
    public static StringBuilder[] loadShaderFile(String fileName) {
        StringBuilder[] shaderSource = new StringBuilder[]{new StringBuilder(), new StringBuilder()};
        // Enum used to index the "mode" of the shader type being read
        enum ShaderType {
            NONE(-1), VERTEX(0), FRAGMENT(1);
            int label;

            ShaderType(int label) {
                this.label = label;
            }
        }
        ShaderType type = ShaderType.NONE;

        try {
            // Read file from resource folder
            BufferedReader fileReader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(FileUtils.class.getResourceAsStream(fileName))));
            String line;

            // While the current line is not null(blank), append whatever text it finds on current line to result and then insert(go to) new line("\n")
            while ((line = fileReader.readLine()) != null) {
                if (line.contains("#SHADER")) {
                    if (line.contains("VERT"))
                        type = ShaderType.VERTEX;
                    else if (line.contains("FRAG"))
                        type = ShaderType.FRAGMENT;
                } else {
                    shaderSource[type.label].append(line).append("\n");
                }
            }
            fileReader.close();
        } catch (Exception e) {
            System.err.println("Could not read file.");
            e.printStackTrace();
            System.exit(-1);
        }
        return shaderSource;
    }
}
