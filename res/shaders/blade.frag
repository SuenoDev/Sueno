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



void main() {
    vec2 T = v_texCoords.xy;
    vec2 coords = (T * u_texsize);

    T.x += sin(coords.y / 3.0 + u_time / 20.0) / u_texsize.x;
    T.y += cos(coords.x / 3.0 + u_time / 20.0) / u_texsize.y;

    float blurAmount = 2.0;
    vec4 color = vec4(0.0);
    float totalWeight = 0.0;


    for (float i = -blurAmount; i <= blurAmount; i++) {
        vec2 offset = vec2(0.0, i * u_invsize.y * 0.4);
        vec4 sampleColor = texture2D(u_texture, T + offset);
        float weight = 0.2;

        color += sampleColor * weight;
        totalWeight += weight;
    }

    color /= totalWeight;

    gl_FragColor = color;

    vec4 maxed = texture2D(u_texture, T);

    if(texture2D(u_texture, T).a < 0.9 && maxed.a > 0.9){
        gl_FragColor = vec4(maxed.rgb, maxed.a * 100.0);
    }
}