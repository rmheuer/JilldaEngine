#version 330 core
#type fragment

in vec2 v_TexCoord;
in vec3 v_Normal;

layout(location = 0) out vec4 o_Color;

void main(void) {
    // Basic shading
    const vec3 lightDir = normalize(vec3(0.5, -0.75, 1.0));
    float normDotDir = dot(v_Normal, lightDir);
    float lightAmt = max(0.25, normDotDir);

    o_Color = vec4(lightAmt, lightAmt, lightAmt, 1.0);
}
