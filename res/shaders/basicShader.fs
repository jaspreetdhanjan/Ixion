#version 330 core

in vec4 vColour;
in vec2 vTexCoords;

out vec4 fragColour;

uniform sampler2D tex;

void main() {
	fragColour=vColour*texture(tex, vTexCoords);
}