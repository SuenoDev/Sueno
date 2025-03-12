varying vec2 v_texcoord;
varying vec3 v_col;
varying vec3 v_pos; // Added v_pos

uniform sampler2D u_texture;

uniform vec3 u_lightdir;
uniform vec3 u_campos;
uniform float u_ambient;
uniform float u_diffuse;
uniform float u_specular;
uniform float u_shininess;
uniform vec3 u_ambientColor;
uniform vec3 u_diffuseColor;
uniform vec3 u_specularColor;
uniform int u_illum;

void main() {
//    float ambientStrength = u_ambient;
//    vec3 ambient = ambientStrength * u_ambientColor;

//    vec3 norm = normalize(v_normal);
//    vec3 lightDir = normalize(-u_lightdir);
//    float diff = max(dot(norm, lightDir), 0.0);
//    vec3 diffuse = u_diffuse * diff * u_diffuseColor;
//
//    float specularStrength = u_specular;
//    vec3 viewDir = normalize(u_campos - v_pos); // Corrected viewDir
//    vec3 reflectDir = reflect(-lightDir, norm);
//    float spec = pow(max(dot(viewDir, reflectDir), 0.0), u_shininess);
//    vec3 specular = specularStrength * spec * u_specularColor;
//
    vec4 texColor = texture2D(u_texture, v_texcoord);
//    vec3 color = (ambient + diffuse + specular) * texColor.rgb;
    gl_FragColor = texColor * vec4(v_col, 1.);// * vec4(0.85, 0.85, 0.85, 1.); //vec4(color, texColor.a); // Final color with lighting and texture
}
