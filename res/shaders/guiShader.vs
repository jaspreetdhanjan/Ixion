#version 330 core

layout(location = 0) in vec3 position;
layout(location = 1) in vec4 colour;
layout(location = 2) in vec3 normal;
layout(location = 3) in vec2 texCoords;

uniform mat4 modelMatrix;
uniform mat4 viewMatrix;
uniform mat4 projectionMatrix;

out vec4 vColour;
out vec2 vTexCoords;

void main() {
    vColour = colour;
    vTexCoords = texCoords;
    
    mat4 mvpMatrix = projectionMatrix*viewMatrix*modelMatrix;
    gl_Position = mvpMatrix*vec4(position, 1.0);
}