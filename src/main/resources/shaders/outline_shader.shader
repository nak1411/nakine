#SHADER_VERT
#version 400 core
layout (location = 0) in vec3 aPos;
layout (location = 1) in vec2 aTexCoord;
layout (location = 2) in vec3 normal;

out vec3 outNormal;
out vec3 outPos;

uniform mat4 transformationMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform float outlineScale;

void main()
{
    vec4 worldPos = transformationMatrix * vec4(aPos * outlineScale, 1.0);
    gl_Position = projectionMatrix * viewMatrix * worldPos;
    outNormal = normalize(transformationMatrix * vec4(normal * outlineScale, 0.0)).xyz;
    outPos = worldPos.xyz;
}

#SHADER_FRAG
#version 400 core

out vec4 color;

uniform vec4 outlineColor;

void main(){
    color = outlineColor;
}