#version 330 core

layout(location = 0) in vec3 position;
layout(location = 1) in vec4 colour;
layout(location = 2) in vec3 normal;

out vec4 vertexColour;
out vec3 Normal;
out vec3 FragPos;

uniform mat4 modelMatrix;
uniform mat4 viewMatrix;
uniform mat4 projectionMatrix;
uniform mat4 normalMatrix;

void main() {
	gl_Position = projectionMatrix*viewMatrix*modelMatrix*vec4(position, 1.0);
	
    vertexColour = colour;
    //Normal = mat3(transpose(inverse(modelMatrix))) * normal;
    Normal = (normalMatrix*vec4(normal, 1.0)).xyz; 
    FragPos = vec3(modelMatrix * vec4(position, 1.0f));
}