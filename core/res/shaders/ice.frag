#define HIGHP

uniform sampler2D u_texture;

uniform vec2 u_campos;
uniform vec2 u_resolution;
uniform float u_time;

varying vec2 v_texCoords;
float pi = 3.14159;

float dist(vec2 a, vec2 b){
    return sqrt((a.x - b.x) * (a.x - b.x) + (a.y - b.y) * (a.y - b.y));
}

void main(){
    vec4 re;
    re = texture2D(u_texture, v_texCoords);
    float d;
    d = dist(vec2(1.5, 0.5), vec2(v_texCoords));
    if (d < 3) {
        if (d < 0.1) {
            d = 0.1;
        }
        re.rgb += (d+1)/d;
    }
    gl_FragColor = re;
}

