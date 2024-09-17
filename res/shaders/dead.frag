#define HIGHP

#define ALPHA 0.18
#define step 3.0

uniform sampler2D u_texture;
uniform vec2 u_texsize;
uniform vec2 u_invsize;
uniform float u_time;
uniform float u_dp;
uniform vec2 u_offset;

varying vec2 v_texCoords;

void main(){
    vec2 T = v_texCoords.xy;
    vec2 coords = (T * u_texsize) + u_offset;

    T += vec2(sin(coords.y / 3.0 + u_time / 20.0), sin(coords.x / 3.0 + u_time / 20.0)) / u_texsize;

    vec4 color = texture2D(u_texture, T);
    vec2 v = u_invsize;

//    vec4 maxed = max(max(max(texture2D(u_texture, T + vec2(0, step) * v), texture2D(u_texture, T + vec2(0, -step) * v)), texture2D(u_texture, T + vec2(step, 0) * v)), texture2D(u_texture, T + vec2(-step, 0) * v));
    vec4 maxed = max(texture2D(u_texture, T + vec2(-step, 1) * v), texture2D(u_texture, T + vec2(step, -1) * v));

    if(texture2D(u_texture, T).a < 0.9 && maxed.a > 0.9){
        gl_FragColor = vec4(maxed.rgb, maxed.a * 100.0);
    }


//    if (coords.x/8 > 60.5 && coords.x/8 < 70.5 && coords.y/8 > 60.5 && coords.y/8 < 70.5) {
//        gl_FragColor = texture2D(u_texture, T);
//    } else {
//        gl_FragColor = end;
//    }
}