#version 330 core

layout(location = 0) out vec4 a_FragColor;

in vec4 v_Color;
in vec2 v_TexCoord;
in float v_TexIndex;

uniform sampler2D u_TexSamplers[32];

void main(void) {
	vec4 texColor = vec4(1.0);
	
	switch (int(v_TexIndex))
	{
		case  0: texColor = texture(u_TexSamplers[ 0], v_TexCoord); break;
		case  1: texColor = texture(u_TexSamplers[ 1], v_TexCoord); break;
		case  2: texColor = texture(u_TexSamplers[ 2], v_TexCoord); break;
		case  3: texColor = texture(u_TexSamplers[ 3], v_TexCoord); break;
		case  4: texColor = texture(u_TexSamplers[ 4], v_TexCoord); break;
		case  5: texColor = texture(u_TexSamplers[ 5], v_TexCoord); break;
		case  6: texColor = texture(u_TexSamplers[ 6], v_TexCoord); break;
		case  7: texColor = texture(u_TexSamplers[ 7], v_TexCoord); break;
		case  8: texColor = texture(u_TexSamplers[ 8], v_TexCoord); break;
		case  9: texColor = texture(u_TexSamplers[ 9], v_TexCoord); break;
		case 10: texColor = texture(u_TexSamplers[10], v_TexCoord); break;
		case 11: texColor = texture(u_TexSamplers[11], v_TexCoord); break;
		case 12: texColor = texture(u_TexSamplers[12], v_TexCoord); break;
		case 13: texColor = texture(u_TexSamplers[13], v_TexCoord); break;
		case 14: texColor = texture(u_TexSamplers[14], v_TexCoord); break;
		case 15: texColor = texture(u_TexSamplers[15], v_TexCoord); break;
		case 16: texColor = texture(u_TexSamplers[16], v_TexCoord); break;
		case 17: texColor = texture(u_TexSamplers[17], v_TexCoord); break;
		case 18: texColor = texture(u_TexSamplers[18], v_TexCoord); break;
		case 19: texColor = texture(u_TexSamplers[19], v_TexCoord); break;
		case 20: texColor = texture(u_TexSamplers[20], v_TexCoord); break;
		case 21: texColor = texture(u_TexSamplers[21], v_TexCoord); break;
		case 22: texColor = texture(u_TexSamplers[22], v_TexCoord); break;
		case 23: texColor = texture(u_TexSamplers[23], v_TexCoord); break;
		case 24: texColor = texture(u_TexSamplers[24], v_TexCoord); break;
		case 25: texColor = texture(u_TexSamplers[25], v_TexCoord); break;
		case 26: texColor = texture(u_TexSamplers[26], v_TexCoord); break;
		case 27: texColor = texture(u_TexSamplers[27], v_TexCoord); break;
		case 28: texColor = texture(u_TexSamplers[28], v_TexCoord); break;
		case 29: texColor = texture(u_TexSamplers[29], v_TexCoord); break;
		case 30: texColor = texture(u_TexSamplers[30], v_TexCoord); break;
		case 31: texColor = texture(u_TexSamplers[31], v_TexCoord); break;
	}

	a_FragColor = v_Color * texColor;
}
