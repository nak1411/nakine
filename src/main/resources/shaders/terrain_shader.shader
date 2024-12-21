#SHADER_VERT
#version 400 core
layout (location = 0) in vec3 aPos;
layout (location = 1) in vec2 aTexCoord;
layout (location = 2) in vec3 normal;

out vec2 outTextureCoord;
out vec3 outNormal;
out vec3 outPos;
out float visibility;

uniform mat4 transformationMatrixTerrain;
uniform mat4 projectionMatrixTerrain;
uniform mat4 viewMatrixTerrain;
uniform float fogDensity;
uniform float fogGradient;

void main()
{
    vec4 worldPos = transformationMatrixTerrain * vec4(aPos, 1.0);
    vec4 positionRelativeToCam = viewMatrixTerrain * worldPos;
    gl_Position = projectionMatrixTerrain * positionRelativeToCam;

    outNormal = normalize(transformationMatrixTerrain * vec4(normal, 1.0)).xyz;
    outPos = worldPos.xyz;
    outTextureCoord = aTexCoord;

    float distance = length(positionRelativeToCam.xyz);
    visibility = exp(-pow((distance * fogDensity), fogGradient));
    visibility = clamp(visibility, 0.0, 1.0);
}

#SHADER_FRAG
#version 400 core

const int MAX_POINT_LIGHTS = 5;
const int MAX_SPOT_LIGHTS = 5;

out vec4 color;

in vec2 outTextureCoord;
in vec3 outNormal;
in vec3 outPos;
in float visibility;

struct Material {
    vec4 ambient;
    vec4 diffuse;
    vec4 specular;
    int hasTexture;
    float reflectance;
};

struct DirectionalLight{
    vec3 color;
    vec3 direction;
    float intensity;
};

struct PointLight{
    vec3 color;
    vec3 position;
    float intensity;
    float constant;
    float linear;
    float exponent;
};

struct SpotLight{
    PointLight pl;
    vec3 conedir;
    float cutoff;
};

uniform sampler2D textureSampler;
uniform vec3 ambientLight;
uniform Material material;
uniform float specularPower;
uniform DirectionalLight directionalLight;
uniform PointLight pointLights[MAX_POINT_LIGHTS];
uniform SpotLight spotLights[MAX_SPOT_LIGHTS];
uniform vec4 skyColor;

uniform int depthVisualizer;
uniform int enableFog;

vec4 ambientC;
vec4 diffuseC;
vec4 specularC;

vec4 calcLightColor(vec3 lightColor, float lightIntensity, vec3 position, vec3 toLightDir, vec3 normal){
    vec4 diffuseColor = vec4(0, 0, 0, 0);
    vec4 specColor = vec4(0, 0, 0, 0);

    // Diffuse
    float diffuseFactor = max(dot(normal, toLightDir), 0.0);
    diffuseColor = diffuseC * vec4(lightColor, 1.0) * lightIntensity * diffuseFactor;

    // Specular
    vec3 cameraDirection = normalize(-position);
    vec3 fromLightDir = -toLightDir;
    vec3 reflectedLight = normalize(reflect(fromLightDir, normal));
    float specularFactor = max(dot(cameraDirection, reflectedLight), 0.0);
    specularFactor = pow(specularFactor, specularPower);
    specColor = specularC * lightIntensity * specularFactor * material.reflectance * vec4(lightColor, 1.0);

    return (diffuseColor + specColor);
}

// Point Light
vec4 calcPointLight(PointLight light, vec3 position, vec3 normal){
    vec3 lightDir = light.position - position;
    vec3 toLightDirection = normalize(lightDir);
    vec4 lightColor = calcLightColor(light.color, light.intensity, position, toLightDirection, normal);

    // Attentuation
    float distance = length(lightDir);
    float attenuationInv = light.constant + light.linear * distance + light.exponent * distance * distance;
    return lightColor / attenuationInv;
}

// Spot Light
vec4 calcSpotLight(SpotLight light, vec3 position, vec3 normal){
    vec3 lightDir = light.pl.position - position;
    vec3 toLightDirection = normalize(lightDir);
    vec3 fromLightDirection = -toLightDirection;
    float spotAlpha = dot(fromLightDirection, normalize(light.conedir));

    vec4 color = vec4(0, 0, 0, 0);

    if (spotAlpha > light.cutoff){
        color = calcPointLight(light.pl, position, normal);
        color *= (1.0 - (1.0 - spotAlpha) / (1.0 - light.cutoff));
    }
    return color;
}

// Directional Light
vec4 calcDirectionalLight(DirectionalLight light, vec3 position, vec3 normal){
    return calcLightColor(light.color, light.intensity, position, normalize(light.direction), normal);
}

// Depth buffer calc
float near = 0.01;
float far  = 100.0;

float LinearizeDepth(float depth)
{
    float z = depth * 2.0 - 1.0;// back to NDC
    return (2.0 * near * far) / (far + near - z * (far - near));
}

void main()
{
    if (material.hasTexture == 1){
        ambientC = texture(textureSampler, outTextureCoord);
        diffuseC = ambientC;
        specularC = ambientC;
    } else {
        ambientC = material.ambient;
        diffuseC = material.diffuse;
        specularC = material.specular;
    }

    vec4 diffuseSpecularComp = calcDirectionalLight(directionalLight, outPos, outNormal);

    for (int i = 0; i < MAX_POINT_LIGHTS; i++){
        if (pointLights[i].intensity > 0){
            diffuseSpecularComp += calcPointLight(pointLights[i], outPos, outNormal);
        }
    }
    for (int i = 0; i < MAX_SPOT_LIGHTS; i++){
        if (spotLights[i].pl.intensity > 0){
            diffuseSpecularComp += calcSpotLight(spotLights[i], outPos, outNormal);
        }
    }

    //Transparency
    vec4 textureColor = texture(textureSampler, outTextureCoord);
    if (textureColor.a < 0.5){
        discard;
    }

    // Enable/disable depth visualizer
    if (depthVisualizer == 1){
        float depth = LinearizeDepth(gl_FragCoord.z) / far;
        color = vec4(vec3(depth), 1.0);
    } else {
        color = ambientC * vec4(ambientLight, 1) * textureColor + diffuseSpecularComp;
        if (enableFog == 1){
            // Mix fog visibility
            color = mix(vec4(skyColor.xyz, 1.0), color, visibility);
        }
    }
}