varying vec2 v_texCoords;

uniform sampler2D u_effects;
uniform sampler2D u_screen;

const vec3 COLOR = vec3(0.08, 0.1, 0.6);
const vec3 COLOR_SCALE = vec3(0.3, 0.3, 0.7);

void main(){
    vec4 effect_color = texture2D(u_effects, v_texCoords);

    vec4 sc = texture2D(u_screen, v_texCoords);

    sc.rgb = mix(sc.rgb, (vec3(1.0) - sc.rgb) * COLOR_SCALE, effect_color.a * 3.0);
    sc.rgb = mix(sc.rgb, COLOR, effect_color.a * 0.8);

    vec4 screen_color = sc;

    gl_FragColor = screen_color;
}