#define HIGHP

uniform sampler2D u_texture;
uniform vec2 u_resolution;
uniform float u_time;

varying vec4 v_position;
varying vec2 v_texCoords;

vec2 random2( vec2 p ) {
    return fract(sin(vec2(dot(p,vec2(127.1,311.7)),dot(p,vec2(269.5,183.3))))*43758.5453);
}

void main() {
    vec2 st = gl_FragCoord.xy/u_resolution.xy;
    st.x *= u_resolution.x/u_resolution.y;
    vec3 color = vec3(.0);
    st *= 48.;

    vec2 i_st = floor(st);
    vec2 f_st = fract(st);
    float l = 0.;
    float m_dist = 1.;
    for (int y = -1; y <= 1; y++) {
        for (int x = -1; x <= 1; x++) {

            vec2 neighbor = vec2(float(x),float(y));
            vec2 point = random2(i_st + neighbor);
            vec2 xx = neighbor - vec2(1.3,0.7);
            l=sqrt(xx.x*xx.x+xx.y*xx.y)/2.0;
            l=max(l,0.4);
            vec2 diff = neighbor + point - f_st;
            float dist = length(diff);
            m_dist = min(m_dist, dist);
        }
    }

    color +=vec3(0.621, 0.879, 0.934)*l;
    color += texture2D(u_texture, v_texCoords).rgb;

    gl_FragColor = vec4(color,1.0);
}