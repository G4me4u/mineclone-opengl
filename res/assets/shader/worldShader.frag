#version 330 core

layout(location = 0) out vec4 color;

in vec2 v_TexCoords;
in float v_Lightness;

uniform sampler2D u_TexSampler;

void main(void) {
	vec4 texColor = texture(u_TexSampler, v_TexCoords);

	if (texColor.a < 0.5)
		discard;
	
	color = texColor * v_Lightness;

//	float fd = vert.loadFloat(FRAG_LOCATION_DEPTH) * 0.01f;
//	float fog = 1.0f / MathUtils.exp(fd * fd * fd * fd * fd);
//	r = r * fog + (1.0f - fog) * 0xFF;
//	g = g * fog + (1.0f - fog) * 0xFF;
//	b = b * fog + (1.0f - fog) * 0xFF;	
}
