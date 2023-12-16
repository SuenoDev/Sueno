varying lowp vec4 v_color;
varying lowp vec4 v_mix_color;

varying vec2 v_texCoords;

uniform sampler2D u_texture;
uniform sampler2D u_screen;

void main(){
    /*vec4 c = texture2D(u_screen, v_texCoords);
    vec4 cc = texture2D(u_texture, v_texCoords);

    c = (vec4(1.0) - c) * length(cc);*/

    vec4 c = texture2D(u_screen, v_texCoords);
    c = vec4(0.0, c.g, c.b, c.a);

    gl_FragColor = vec4(.0, .0, .0, 1.);
}