//vert
uniform mat4 u_proj;
uniform mat4 u_view;
uniform mat4 u_worldTrans;

attribute vec4 a_position;
attribute vec3 a_normal;
attribute vec2 a_texCoord0;

varying vec2 v_texcoord;
varying vec3 v_normal;
varying vec3 v_worldPos;

void main() {
    vec4 worldPos4 = u_worldTrans * a_position;
    v_worldPos = worldPos4.xyz;
    v_normal = normalize(mat3(u_worldTrans) * a_normal);


    v_texcoord = a_texCoord0;
    gl_Position = u_proj * u_view * worldPos4;
}