#version 330 core
#type vertex

layout(location = 0) in vec3 a_Position;
layout(location = 1) in vec2 a_TexCoord;
layout(location = 2) in vec3 a_Normal;

uniform mat4 u_Projection;
uniform mat4 u_View;
uniform mat4 u_Transform;

out vec2 v_TexCoord;
out vec3 v_Normal;

void main(void) {
    gl_Position = u_Projection * u_View * u_Transform * vec4(a_Position, 1.0);
    v_TexCoord = a_TexCoord;
    v_Normal = a_Normal;
}
