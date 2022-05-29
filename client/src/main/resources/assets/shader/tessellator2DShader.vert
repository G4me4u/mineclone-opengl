#version 330 core

layout(location = 0) in vec2 a_Position;
layout(location = 1) in vec4 a_Color;
layout(location = 2) in vec2 a_TexCoord;
layout(location = 3) in float a_TexIndex;

out vec4 v_Color;
out vec2 v_TexCoord;
out float v_TexIndex;

uniform mat4 u_ProjMat;

void main(void) {
	v_Color = a_Color;
	v_TexCoord = a_TexCoord;
	
	// Add 0.5 to ensure we have no rounding errors
	v_TexIndex = a_TexIndex + 0.5;
	
	gl_Position = u_ProjMat * vec4(a_Position, 0.0, 1.0);
}
