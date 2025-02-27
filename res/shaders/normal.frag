#define HIGHP

uniform sampler2D u_normal;
uniform sampler2D u_texture;
//uniform sampler2D u_textures;
uniform vec2 u_texsize;
uniform vec2 u_offset;
uniform vec2 u_lightPos;
uniform vec4 u_lightColor;

varying vec2 v_texCoords;
varying lowp vec4 v_color;
varying lowp vec4 v_mix_color;

//void main(){
//    gl_FragColor = v_color * mix(c, vec4(v_mix_color.rgb, c.a), v_mix_color.a);
//    gl_FragColor.r = 1.;
//}


void main() {
    vec2 T = v_texCoords.xy;
    vec2 coords = (T * u_texsize) + u_offset;
    vec3 normal = vec3(texture2D(u_normal, T).r*2.-1.,(texture2D(u_normal, T).b*2.-1.),texture2D(u_normal, T).g);


    vec3 lightPos = vec3(u_lightPos.x, u_lightPos.y, 24.);
    vec3 surfacePos = vec3(coords.x, coords.y, 8.);


    vec3 N = normalize(normal);
    vec3 LightDir = lightPos.xyz-surfacePos.xyz;
    float D = length(LightDir)/1024.;
    vec3 L = normalize(LightDir);

    vec3 Diffuse = (u_lightColor.rgb * u_lightColor.a) * max(dot(N, L), 0.0);
    vec3 Ambient = vec3(0.22, 0.22, 0.3);
    float Attenuation = 1.0 / (.4 + 3.*D + 20.*D*D);

    vec3 Intensity = Ambient + Diffuse * Attenuation;

    vec4 c = texture2D(u_texture, v_texCoords);

    vec4 rc = v_color * mix(c, vec4(v_mix_color.rgb, c.a), v_mix_color.a);

    vec3 FinalColor = rc.rgb * Intensity;
    gl_FragColor = texture2D(u_normal, T);
//    gl_FragColor = vec4(texture2D(u_normal, T).rgb, rc.a);
}
//