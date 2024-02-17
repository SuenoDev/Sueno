#define HIGHP

uniform sampler2D u_texture;
uniform vec2 u_resolution;
uniform float u_time;
uniform float u_zoom;

varying vec4 v_position;
varying vec2 v_texCoords;

vec2 random2( vec2 p ) {
    return fract(sin(vec2(dot(p,vec2(127.1,311.7)),dot(p,vec2(269.5,183.3))))*43758.5453);
}

void main() {
    vec2 st = gl_FragCoord.xy/u_resolution.xy;
    st.x *= u_resolution.x/u_resolution.y;
    vec3 color = vec3(.0);
    st *= 48./u_zoom;

    vec2 i_st = floor(st);
    vec2 f_st = fract(st);
    float m_dist = 1.;
    for (int y = -1; y <= 1; y++) {
        for (int x = -1; x <= 1; x++) {

            vec2 neighbor = vec2(float(x),float(y));
            vec2 point = random2(i_st + neighbor);;
            vec2 diff = neighbor + point - f_st;
            float dist = length(diff);
            m_dist = min(m_dist, dist);
        }
    }

    color += m_dist;
    //color += vec3(0.621, 0.879, 0.934);
    color += texture2D(u_texture, v_texCoords).rgb;

    gl_FragColor = vec4(color,1.0);
}