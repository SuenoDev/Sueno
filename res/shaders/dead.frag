#define HIGHP

#define ALPHA 0.18
#define step 3.0

uniform sampler2D u_texture;
uniform vec2 u_texsize;
uniform vec2 u_invsize;
uniform float u_time;
uniform float u_dp; // Distance from camera, use this for zoom level
uniform vec2 u_offset;

varying vec2 v_texCoords;

void main() {
    vec2 T = v_texCoords.xy;
    vec2 coords = (T * u_texsize) + u_offset; // Added offset like in your reference shader

    T.x += sin(coords.y / 3.0 + u_time / 20.0) / u_texsize.x;
    T.y += cos(coords.x / 3.0 + u_time / 20.0) / u_texsize.y;

    float blurAmount = 2.0;
    vec4 color = vec4(0.0);
    float totalWeight = 0.0;

    // Adjust blurAmount based on u_dp (camera distance)
    float dynamicBlurAmount = blurAmount * (1.0 + u_dp * 0.5); // Example: more blur as camera moves further

    for (float i = -dynamicBlurAmount; i <= dynamicBlurAmount; i++) {
        vec2 offset = vec2(0.0, i * u_invsize.y * 0.4);
        vec4 sampleColor = texture2D(u_texture, T + offset);
        float weight = 0.2;

        color += sampleColor * weight;
        totalWeight += weight;
    }

    color /= totalWeight;

    gl_FragColor = color;

    // Use step value from your other shader, scaled by camera distance
    float dynamicStep = step * (1.0 + u_dp * 0.5);

    vec4 maxed = max(texture2D(u_texture, T + vec2(-dynamicStep, 1.) * u_invsize), texture2D(u_texture, T + vec2(dynamicStep, -1.) * u_invsize));

    if(texture2D(u_texture, T).a < 0.9 && maxed.a > 0.9){
        gl_FragColor = vec4(maxed.rgb, maxed.a * 100.0);
    }
}
