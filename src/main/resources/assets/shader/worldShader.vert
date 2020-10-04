#version 330 core

layout(location = 0) in vec3 a_Position;
layout(location = 1) in vec2 a_TexCoords;
layout(location = 2) in vec4 a_Color;
layout(location = 3) in float a_Lightness;

out vec2 v_TexCoords;
out vec4 v_Color;
out float v_Lightness;
out float v_Fog;

uniform mat4 u_ViewMat;
uniform mat4 u_ProjMat;

uniform float u_FogDensity;
uniform float u_FogGradient;

void main(void) {
	v_TexCoords = a_TexCoords;
	v_Color = a_Color;
	v_Lightness = a_Lightness;
	
	vec4 viewPosition = u_ViewMat * vec4(a_Position, 1.0);
	
	float distToCamera = length(viewPosition.xyz);
	float fog = 1.0 - exp(-pow(distToCamera * u_FogDensity, u_FogGradient));
	v_Fog = clamp(fog, 0.0, 1.0);
	
	gl_Position = u_ProjMat * viewPosition;
}
