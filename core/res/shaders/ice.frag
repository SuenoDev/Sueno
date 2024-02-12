//varying vec2 v_texCoords;
//uniform sampler2D u_texture;
//uniform vec2 u_campos;
//uniform vec2 u_resolution;
//uniform float u_time;

//float random(vec2 st) {
//    return fract(sin(dot(st.xy, vec2(12.9898,78.233))) * 43758.5453);
//}
//
//float noise(vec2 st) {
//    vec2 i = floor(st);
//    vec2 f = fract(st);
//
//    float a = random(i);
//    float b = random(i + vec2(1.0, 0.0));
//    float c = random(i + vec2(0.0, 1.0));
//    float d = random(i + vec2(1.0, 1.0));
//
//    vec2 u = f * f * (3.0 - 2.0 * f);
//
//    return mix(a, b, u.x) + (c - a) * u.y * (1.0 - u.x) + (d - b) * u.x * u.y;
//}

void main() {
    gl_FragColor = vec4(1.0, 1.0, 1.0, 1.0);
//    vec2 st = v_texCoords;
//    vec2 c = v_texCoords;
//    vec2 v = vec2(1.0/u_resolution.x, 1.0/u_resolution.y);
//    vec2 coords = vec2(c.x / v.x + u_campos.x, c.y / v.y + u_campos.y);
//
//    float stime = u_time / 5.0;
//
//    vec4 sampled = texture2D(u_texture, st);
//    vec3 color = sampled.rgb * vec3(0.9, 0.9, 1);
//
//    float tester = mod(coords.x + coords.y * 1.1 + sin(stime / 8.0 + coords.x / 5.0 - coords.y / 100.0) * 2.0, 40.0);
//
//    if (tester < 7.0) {
//        float iceNoise = noise(coords * 10.0);
//        color *= iceNoise;
//    }
//
//    gl_FragColor = vec4(color.rgb, min(sampled.a * 100.0, 1.0));
}