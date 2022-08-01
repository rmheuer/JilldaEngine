#version 330 core

in vec3 v_TexDir;

uniform samplerCube m_Texture;

layout(location = 0) out vec4 o_Color;

void main(void) {
    o_Color = texture(m_Texture, v_TexDir);
}
