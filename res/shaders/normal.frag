#define HIGHP
#define ALPHA 0.92

uniform sampler2D u_texture;
uniform vec2 u_texsize;
uniform vec2 u_offset;
uniform vec2 u_lightPos;

varying vec2 v_texCoords;

float lenght(vec2 a) {
    return sqrt(a.x*a.x + a.y*a.y);
}

void main() {
    vec2 T = v_texCoords.xy;
    vec2 color = (texture2D(u_texture, T).xy - vec2(0.5)) * vec2(2.);
    if (texture2D(u_texture, T).a == 0.) {
        gl_FragColor = vec4(0.);
        return;
//        color = vec2(0.5);
    }
    vec2 coords = (T * u_texsize) + u_offset;
    vec4 light_color = vec4(0.);
    vec2 bp = u_lightPos - coords;
    vec2 b = u_lightPos - vec2(1040., 256.);

//    light_color.rgb = vec3(max(dot(normalize(color), normalize(b)), 0));

    vec2 nn = normalize(color);
    vec2 nl = normalize(b);
    float r = 120.;
    float d = lenght(bp)/r;
    float df = min((1. - (pow(d, 0.3)))*(r/40.), 1.);
    float ld = dot(nl, nn);
    float l = pow(ld+1., 3.)/8. ;
    float cl = min(max(l * df, 0.), 1.);
    light_color.rgb = vec3(cl);

//    light_color.a = min(texture2D(u_texture, T).a, ALPHA);

    light_color.a = ALPHA;
    gl_FragColor = light_color;
}