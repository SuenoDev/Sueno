attribute vec4 a_position;
attribute vec2 a_texCoord0;

varying vec2 v_texCoords;

varying vec4 v_pos;

void main() {
    gl_Position = a_position;
    v_pos = a_position;
    v_texCoords = a_texCoord0;
}