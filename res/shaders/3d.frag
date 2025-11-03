varying vec2 v_texcoord;
varying vec3 v_normal;
varying vec3 v_worldPos;

uniform sampler2D u_texture;
uniform vec3 u_lightdir; // Направление света norm(light - objpos)
uniform vec3 u_campos;   // Позиция камеры

uniform vec3 u_ambientColor;  // Цвет эмбиентного света
uniform vec3 u_diffuseColor;  // Цвет диффузного света
uniform vec3 u_specularColor; // Цвет отражённого света
uniform float u_shininess;    // Блеск материала
uniform int u_illum;          // Режим освещения: 0 = только текстура/базовый цвет, 1 = эмбиент+диффуз, 2 = эмбиент+диффуз+спекуляр

void main() {
    vec4 texColor = texture2D(u_texture, v_texcoord);
    vec3 normal = normalize(v_normal); // Нормаль интерполируется, поэтому нужно нормализовать ещё раз

    // Базовый цвет (только текстура)
    vec3 finalColor = texColor.rgb;

    if (u_illum == 1 || u_illum == 2) {
        // Эмбиентная компонента
        vec3 ambient = u_ambientColor * texColor.rgb;
        finalColor = ambient;

        // Диффузная компонента
        float diff = max(dot(normal, -u_lightdir), 0.0);
        vec3 diffuse = u_diffuseColor * diff * texColor.rgb;
        finalColor += diffuse;

        if (u_illum == 2) {
            // Спекулярная компонента
            vec3 viewDir = normalize(u_campos - v_worldPos);
            vec3 reflectDir = normalize(reflect(u_lightdir, normal)); // Note: reflect expects incident vector, not light direction
            float spec = pow(max(dot(viewDir, reflectDir), 0.0), u_shininess);
            vec3 specular = u_specularColor * spec;
            finalColor += specular;
        }
    }

    gl_FragColor = vec4(finalColor, texColor.a);
}