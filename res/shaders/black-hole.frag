uniform sampler2D u_texture;

varying vec2 v_texCoords;
varying vec4 v_pos;

void main() {
    vec2 T = v_texCoords.xy;
    vec4 pos = vec4(T,T);
    gl_FragColor = vec4(pos.x, pos.y, 1, texture2D(u_texture, T).a);
}