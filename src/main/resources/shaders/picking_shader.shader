#SHADER_VERT
#version 400 core
layout (location = 0) in vec3 aPos;

out vec3 outPos;

uniform mat4 transformationMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;

void main()
{
    vec4 worldPos = transformationMatrix * vec4(aPos, 1.0);
    gl_Position = projectionMatrix * viewMatrix * worldPos;
    outPos = worldPos.xyz;
}

#SHADER_FRAG
#version 400 core

out vec4 color;

uniform vec4 pickingColor;

void main()
{
    color = pickingColor;
}