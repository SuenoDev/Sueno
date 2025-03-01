attribute vec4 a_position;
attribute vec4 a_color;
attribute vec2 a_texCoord0;
attribute vec2 a_texCoord1;
attribute float a_rotation;
attribute vec4 a_mix_color;
uniform mat4 u_projTrans;

varying vec4 v_color;
varying vec4 v_mix_color;
varying vec2 v_texCoords;
varying vec2 v_texCoords1;
varying float v_rotation;

void main(){
    v_color = a_color;
    v_color.a = v_color.a * (255.0/254.0);
    v_mix_color = a_mix_color;
    v_mix_color.a *= (255.0/254.0);
    v_texCoords = a_texCoord0;
    v_texCoords1 = a_texCoord1;
    v_rotation = a_rotation;
    gl_Position = u_projTrans * a_position;
}