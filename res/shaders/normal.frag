#define HIGHP
#define ALPHA 0.92

uniform sampler2D u_texture;
uniform vec2 u_texsize;
uniform vec2 u_offset;
uniform vec2 u_lightPos;

varying vec2 v_texCoords;

float calculateNormalMapLighting(vec3 lightPos, float lightRadius, vec3 pointPos, vec3 surfacePos, vec2 normal) {
    vec3 lightDir = lightPos - pointPos;
    lightDir = normalize(lightDir);
    normal = normalize(normal);
    float dotProduct = dot(normal, lightDir);
    float distance = length(lightPos - pointPos);
    float intensity = 0.0;
    if (distance <= lightRadius) {
        intensity = max(0.0, 1.0 - distance / lightRadius) * dotProduct;
    }

    return clamp(intensity*2., 0., 1.);
}



void main() {
    vec2 T = v_texCoords.xy;
    vec2 coords = (T * u_texsize) + u_offset;
    vec2 normal = texture2D(u_texture, T).rg * 2.0 - 1.0;
    float intensity = calculateNormalMapLighting(vec3(u_lightPos.x, u_lightPos.y, 40), 80., vec3(coords.x, coords.y, 15), vec3(1040., 256., 0), normal);

    gl_FragColor = vec4(intensity,intensity,intensity,ALPHA*texture2D(u_texture, T).a);
}