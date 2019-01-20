#version 330 core

in vec4 vColour;
in vec2 vTexCoords;

out vec4 fragColour;

uniform sampler2D tex;

void main() {
	vec4 colour=vColour*texture(tex, vTexCoords);
//	float br = colour.r*colour.g*colour.b;
	fragColour=colour;
}