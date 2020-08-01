#version 330 core

layout(location = 0) in vec3 a_Position;
layout(location = 1) in vec2 a_TexCoords;
layout(location = 2) in float a_Lightness;

out vec2 v_TexCoords;
out float v_Lightness;

uniform mat4 u_ProjViewMat;

void main(void) {
	v_TexCoords = a_TexCoords;
	v_Lightness = a_Lightness;
	
	gl_Position = u_ProjViewMat * vec4(a_Position, 1.0);
}
