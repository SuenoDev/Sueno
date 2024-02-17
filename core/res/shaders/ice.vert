attribute vec4 a_position;
attribute vec2 a_texCoord0;


varying vec2 v_texCoords;
varying vec2 v_position;


void main(){
    v_texCoords = a_texCoord0;
    gl_Position = a_position;
    v_position = a_position.xy;
}