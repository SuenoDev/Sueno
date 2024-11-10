attribute vec4 a_position;
attribute vec2 a_texCoord0;

uniform struct {
    mat4 proj;
    vec3 position;
    mat4 view;
} u_camera;

uniform struct {
    mat4 rotation;
    vec3 scale;
    vec3 translation;
} u_model;

varying vec2 v_texCoords;
varying vec3 v_realPosition;
varying vec2 v_texCoord;
varying vec3 v_normal;

void main(){
    v_realPosition = (u_model.rotation * vec4(a_position * u_model.scale, 1.0)).xyz + u_model.translation;
    v_normal = (u_model.rotation * vec4(a_normal, 0.0)).xyz;
    v_texCoords = a_texCoord0;
    gl_Position = a_position;
}