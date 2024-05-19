uniform sampler2D u_texture;

uniform vec2 u_campos;
uniform vec2 u_ccampos;
uniform vec2 u_resolution;
uniform float u_time;

varying vec2 v_texCoords;

float random(in vec2 st) {
    return fract(sin(dot(st.xy,
                         vec2(12.9898,78.233)))
                 * 43758.5453123);
}

// 2D Noise based on Morgan McGuire @morgan3d
// https://www.shadertoy.com/view/4dS3Wd
float dvnoise (in vec2 st) {
    vec2 i = floor(st);
    vec2 f = fract(st);

    // Four corners in 2D of a tile
    float a = random(i);
    float b = random(i + vec2(1.0, 0.0));
    float c = random(i + vec2(0.0, 1.0));
    float d = random(i + vec2(1.0, 1.0));

    // Smooth Interpolation

    // Cubic Hermine Curve.  Same as SmoothStep()
    vec2 u = f*f*(3.0-2.0*f);
    // u = smoothstep(0.,1.,f);

    // Mix 4 coorners percentages
    return mix(a, b, u.x) +
    (c - a)* u.y * (1.0 - u.x) +
    (d - b) * u.x * u.y;
}

float fbm1(vec2 x) {
    float v = 0.0;
    float a = 0.5;
    vec2 shift = vec2(100);
    for (int i = 0; i < 4; ++i) {
        v += a * dvnoise(x);
        x = x * 2.0 + shift;
        a *= 0.5;
    }
    return v;
}

float fbm2(vec2 x) {
    float v = 0.0;
    float a = 0.5;
    vec2 shift = vec2(100);
    for (int i = 0; i < 8; ++i) {
        v += a * dvnoise(x);
        x = x * 2.0 + shift;
        a *= 0.5;
    }
    return v;
}

void main(){
    vec2 T = v_texCoords.xy;
    vec4 color = texture2D(u_texture, T);
    float noise = (fbm1(vec2(T * 14.0 + vec2(u_time/60.0))) + 1.0) / 2.0;
    float cnoise = (fbm2(vec2(T * 56.0 + vec2(u_time/40.0))) + 1.0) / 2.2;

    vec3 c1 = vec3(44, 93, 170) / 256.0;
    vec3 c2 = vec3(126, 20, 195) / 256.0;

    vec3 ctmp = mix(c1, c2, cnoise);

    color.rgb *= noise * ctmp;

    gl_FragColor = color;
}
