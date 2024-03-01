#define MEDIUMP

uniform sampler2D u_texture;
uniform vec2 u_resolution;
uniform float u_time;
uniform float u_zoom;
uniform vec2 u_campos;

varying vec2 v_position;
varying vec2 v_texCoords;

vec2 random2(vec2 p) {
    return fract(sin(vec2(dot(p,vec2(127.1,311.7)),dot(p,vec2(269.5,183.3))))*43758.5453);
}

vec4 blackHoleEffect(vec2 uv) {
    vec2 center = vec2(0.5); // Центр черной дыры
    float radius = 0.3; // Радиус черной дыры
    float strength = 1.7; // Сила эффекта черной дыры
    //center.x *= u_resolution.x / u_resolution.y;
    vec2 delta = uv - center;

    vec2 dost = vec2(delta);
    dost.x *= u_resolution.x / u_resolution.y;


    float distance = length(delta);

    vec2 direction = normalize(delta);

    // Расчет силы черной дыры
    float intensity = 1.0 / (distance * strength + 1.0);

    // Расчет передвижения текстурных координат
    vec2 offset = direction * intensity * radius;


    // Применение эффекта смещения текстуры
    vec2 texCoords = uv - offset;
    vec4 texColor = texture2D(u_texture, texCoords);

    if (length(delta)+0.07 < radius) {
        texColor = vec4(0.0, 0.0, 0.0, 1.0);
        texColor = min(texColor, vec4(1.0));
    }


    return texColor;
}

void main() {
//    // Получаем цвет пикселя из текстуры
//    vec4 color = texture2D(u_texture, v_texCoords);

    // Применяем эффект черной дыры к цвету пикселя
    vec4 blackHoleColor = blackHoleEffect(v_texCoords);

    // Устанавливаем окончательный цвет пикселя
    gl_FragColor = blackHoleColor;
//    vec2 st = gl_FragCoord.xy/u_resolution.xy;
//    st.x *= u_resolution.x/u_resolution.y;
//    vec3 color = vec3(.0);
//    st *= 180.0/u_zoom;
//
//    vec2 i_st = floor(st);
//    vec2 f_st = fract(st);
//    float m_dist = 1.;
//    for (float y = -1; y <= 1; y+=1.0) {
//        for (float x = -1; x <= 1; x+=1.0) {
//
//            vec2 neighbor = vec2(x, y);
//            vec2 a = i_st + neighbor;
//            vec2 point = random2(a);
//            vec2 diff = neighbor + point - f_st;
//            float dist = length(diff);
//            m_dist = min(m_dist, dist);
//        }
//    }
//    color += vec3(1.0)-m_dist;
//    color *= vec3(0.621, 0.879, 0.934);
//    color += texture2D(u_texture, v_texCoords).rgb;

//    gl_FragColor = vec4(color,1.0);
}