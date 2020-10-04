#version 330 core

layout(location = 0) out vec4 a_FragColor;

uniform vec4 u_SelectionColor;

void main(void) {
	a_FragColor = u_SelectionColor;
}
