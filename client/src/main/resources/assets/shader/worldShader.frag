#version 330 core

layout(location = 0) out vec4 a_FragColor;

in vec2 v_TexCoords;
in vec4 v_Color;
in float v_Lightness;
in float v_Fog;

uniform sampler2D u_TexSampler;
uniform vec3 u_FogColor;

void main(void) {
	vec4 texColor = v_Color * texture(u_TexSampler, v_TexCoords);

	if (texColor.a < 0.5)
		discard;
	
	vec4 color = vec4(texColor.rgb * v_Lightness, texColor.a);
	a_FragColor = mix(color, vec4(u_FogColor, 1.0), v_Fog);
}
