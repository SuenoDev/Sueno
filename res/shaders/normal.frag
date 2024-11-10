#define HIGHP
#define ALPHA 0.92

uniform sampler2D u_normal;
uniform sampler2D u_texture;
uniform vec2 u_texsize;
uniform vec2 u_offset;
uniform vec2 u_lightPos;
uniform vec4 u_lightColor;
uniform vec2 u_surefacePos;

varying vec2 v_texCoords;

void main() {
    vec2 T = v_texCoords.xy;
    vec2 coords = (T * u_texsize) + u_offset;
    vec3 normal = vec3(texture2D(u_texture, T).r*2.-1.,(texture2D(u_texture, T).b*2.-1.),texture2D(u_texture, T).g);


    vec3 lightPos = vec3(u_lightPos.x, u_lightPos.y, 24);
    vec3 surfacePos = vec3(u_surefacePos.x, u_surefacePos.y, 8);


    vec3 N = normalize(normal);
    vec3 LightDir = lightPos.xyz-surfacePos.xyz;
    float D = length(LightDir);
    vec3 L = normalize(LightDir);

    vec3 Diffuse = (u_lightColor.rgb * u_lightColor.a) * max(dot(N, L), 0.0);
    vec3 Ambient = vec3(0.12, 0.12, 0.2);
    float Attenuation = 1.0 / (.4 + 3*D + 20*D*D);

    vec3 Intensity = Ambient + Diffuse * Attenuation;
    vec3 FinalColor = texture2D(u_normal, T).rgb * Intensity;
    gl_FragColor = vec4(FinalColor,  texture2D(u_normal, T).a);
}
