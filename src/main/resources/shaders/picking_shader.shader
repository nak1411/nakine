#SHADER_VERT_PICKING
#version 400 core
layout (location = 0) in vec3 aPos;

out vec3 outPos;

uniform mat4 transformationMatrixPicking;
uniform mat4 projectionMatrixPicking;
uniform mat4 viewMatrixPicking;

void main()
{
    vec4 worldPos = transformationMatrixPicking * vec4(aPos, 1.0);
    gl_Position = projectionMatrixPicking * viewMatrixPicking * worldPos;
    outPos = worldPos.xyz;
}

#SHADER_FRAG_PICKING
#version 400 core

out vec4 color;

uniform vec4 pickingColor;

void main()
{
    color = pickingColor;
}