#version 330 core

layout(location = 0) out vec4 color;

uniform vec4 u_SelectionColor;

void main(void) {
	color = u_SelectionColor;
}
