#version 330 core

in vec2 v_TexCoord;
in vec3 v_Normal;

uniform sampler2D m_Texture;

layout(location = 0) out vec4 o_Color;

void main(void) {
    // Basic shading
    const vec3 lightDir = normalize(vec3(0.5, -0.75, 1.0));
    float normDotDir = dot(v_Normal, lightDir);
    float lightAmt = max(0.25, normDotDir);

    vec4 texColor = texture(m_Texture, v_TexCoord);
    texColor.rgb *= lightAmt;

    o_Color = texColor;
}
