#version 330 core

layout(location = 0) in vec3 a_Position;

uniform mat4 u_ProjViewMat;
uniform mat4 u_ModlMat;

void main(void) {
	gl_Position = u_ProjViewMat * u_ModlMat * vec4(a_Position, 1.0);
}
