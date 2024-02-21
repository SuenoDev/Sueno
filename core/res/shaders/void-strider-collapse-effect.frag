varying vec2 v_texCoords;

uniform sampler2D u_effects;
uniform sampler2D u_screen;

void main(){
    vec4 screen_color = texture2D(u_screen, v_texCoords);
    vec4 effects_color = texture2D(u_effects, v_texCoords);

    vec3 color = screen_color.rgb;

    color = mix(color, vec3(0.02, 0.02, 0.2), pow(effects_color.a, 2.0) * 0.5);

    gl_FragColor = vec4(color, screen_color.a);
}


