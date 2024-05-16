#define HIGHP
#define NSCALE 2700.0

uniform sampler2D u_texture;

uniform vec2 u_campos;
uniform vec2 u_ccampos;
uniform vec2 u_resolution;
uniform float u_time;

varying vec2 v_texCoords;



float rand (in vec2 uv) { return fract(sin(dot(uv,vec2(12.4124,48.4124)))*48512.41241); }
const vec2 O = vec2(0.,1.);
float noise2 (in vec2 uv) {
    vec2 b = floor(uv);
    return mix(mix(rand(b),rand(b+O.yx),.5),mix(rand(b+O),rand(b+O.yy),.5),.5);
}

float random (in vec2 st) {
    return fract(sin(dot(st.xy,
                         vec2(12.9898,78.233)))
                 * 43758.5453123);
}

// 2D Noise based on Morgan McGuire @morgan3d
// https://www.shadertoy.com/view/4dS3Wd
float noiser (in vec2 st) {
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

float ave(vec3 v) {
    return (v.r + v.g + v.b) / 3.0;
}

#define NUM_OCTAVES 5

float fbm(float x) {
    float v = 0.0;
    float a = 0.5;
    float shift = float(100);
    for (int i = 0; i < NUM_OCTAVES; ++i) {
        v += a * noiser(x);
        x = x * 2.0 + shift;
        a *= 0.5;
    }
    return v;
}


float fbm(vec2 x) {
    float v = 0.0;
    float a = 0.5;
    vec2 shift = vec2(100);
    // Rotate to reduce axial bias
    mat2 rot = mat2(cos(0.5), sin(0.5), -sin(0.5), cos(0.50));
    for (int i = 0; i < NUM_OCTAVES; ++i) {
        v += a * noiser(x);
        x = rot * x * 2.0 + shift;
        a *= 0.5;
    }
    return v;
}


float fbm(vec3 x) {
    float v = 0.0;
    float a = 0.5;
    vec3 shift = vec3(100);
    for (int i = 0; i < NUM_OCTAVES; ++i) {
        v += a * noiser(x);
        x = x * 2.0 + shift;
        a *= 0.5;
    }
    return v;
}

void main(){
    vec2 T = v_texCoords.xy;
    vec4 color = texture2D(u_texture, T);
    color.rgb = color.rgb * (fbm(T * 14.0 + u_time/60.0) + vec3(1.0)) / 2.0 * vec3(150.0/256.0, 42.0/256.0, 170.0/256.0);

    gl_FragColor = color;
}
