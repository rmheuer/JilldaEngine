#version 330 core
#type vertex

layout(location = 0) in vec3 a_Position;
layout(location = 1) in vec3 a_Color;

uniform mat4 u_Projection;
uniform mat4 u_View;
uniform mat4 u_Transform;

out vec3 v_Color;

void main(void) {
    gl_Position = u_Projection * u_View * u_Transform * vec4(a_Position, 1.0);
    v_Color = a_Color;
}
