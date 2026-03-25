uniform sampler2D u_texture;
uniform vec2 u_texsize;
uniform vec2 u_offset;
uniform float u_time;

// u_cscl: диапазон от ~30 (очень близко) до ~0.1 (очень далеко)
uniform float u_cscl;

varying vec2 v_texCoords;

vec3 hash3( vec2 p ){
    vec3 q = vec3( dot(p,vec2(127.1,311.7)),
    dot(p,vec2(269.5,183.3)),
    dot(p,vec2(419.2,371.9)) );
    return fract(sin(q)*43758.5453);
}

float iqnoise(in vec2 x){
    vec2 p = floor(x);
    vec2 f = fract(x);
    float k = 44.;
    float va = 0.0;
    float wt = 0.0;
    for( int j=-3; j<=3; j++ ) {
        for( int i=-3; i<=3; i++ ) {
            vec2 g = vec2( float(i),float(j) );
            vec3 o = hash3( p + g );
            vec2 r = g - f + o.xy;
            float d = dot(r,r);
            float ww = pow( 1.0-smoothstep(0.0,1.414,sqrt(d)), k );
            va += o.z*ww;
            wt += ww;
        }
    }
    return va/wt;
}

float hash12(vec2 p) {
    return fract(sin(dot(p, vec2(127.1, 311.7))) * 43758.5453123);
}

// Принимаем cscl для защиты от мерцания вдалеке
float getStars(vec2 pos, float time, float cscl) {
    vec2 st = pos / 2.5;
    vec2 i_st = floor(st);
    vec2 f_st = fract(st);

    float density = hash12(i_st);
    float mask = smoothstep(0.4, 0.6, density);

    float finalBrightness = 0.0;

    // Если мы сильно отдаляемся (cscl маленький), звезды становятся меньше 1 пикселя и мерцают.
    // Этот лимит искусственно "раздувает" их при сильном отдалении, чтобы они оставались видимыми.
    float minSize = 0.015 / max(cscl, 0.01);

    for (int y = -1; y <= 1; y++) {
        for (int x = -1; x <= 1; x++) {
            vec2 offset = vec2(float(x), float(y));
            vec2 cell = i_st + offset;

            float rnd = hash12(cell);
            float exists = step(0.85, rnd);

            float twinkle = sin(time * 3.0 + hash12(cell * 11.1) * 20.0) * 0.5 + 0.5;

            vec2 p = vec2(hash12(cell * 12.3), hash12(cell * 45.6));

            // Базовый размер + защита от исчезновения
            float size = 0.03 + hash12(cell * 78.9) * 0.08;
            size = max(size, minSize);

            float d = length(f_st - (offset + p));

            finalBrightness += (1.0 - smoothstep(0.0, size, d)) * exists * mask * (0.3 + 0.7 * twinkle);
        }
    }
    return finalBrightness;
}

void main() {
    float ICE_SCALE = 6.0;

    // ВАЖНО: Сила преломления теперь измеряется в МИРОВЫХ единицах (игровых блоках).
    // 2.0 означает, что лед искажает картинку под собой на ~2 блока.
    float REFRACTION_WORLD_STRENGTH = 2.0;

    float REFLECTION_STRENGTH = 80.0;
    float ICE_OPACITY = 0.1;

    float PARALLAX_DEPTH = 0.85;

    vec2 T = v_texCoords.xy;

    float origAlpha = texture2D(u_texture, T).a;

    if (origAlpha < 0.01) {
        gl_FragColor = vec4(0.0);
        return;
    }

    // coords — это чистые мировые координаты, они уже учитывают зум!
    vec2 coords = (T * u_texsize) + u_offset;
    float time = u_time / 60.0;

    float baseNoise = iqnoise(coords / ICE_SCALE);

    float eps = 2.0;
    float nx = iqnoise((coords + vec2(eps, 0.0)) / ICE_SCALE) - baseNoise;
    float ny = iqnoise((coords + vec2(0.0, eps)) / ICE_SCALE) - baseNoise;
    vec2 distortion = vec2(nx, ny);

    distortion *= smoothstep(0.0, 0.5, origAlpha);

    float b = pow(baseNoise + 1.0, 2.0) / 4.0;
    vec3 iceTint = vec3(0.689, 1.0, 0.99) * b;

    // ПРАВИЛЬНАЯ РЕФРАКЦИЯ: Мы делим мировое искажение на размер камеры (u_texsize).
    // Благодаря этому, когда мы приближаем (u_texsize маленький), искажение в UV пропорционально РАСТЁТ (выглядит крупнее).
    vec2 uvOffset = (distortion * REFRACTION_WORLD_STRENGTH) / u_texsize;
    vec2 distortedUV = T + uvOffset;

    vec4 underIceColor = texture2D(u_texture, distortedUV);

    if (underIceColor.a < 0.5) {
        underIceColor = texture2D(u_texture, T);
    }

    // ПАРАЛЛАКС: Только мировые координаты. Я убрал старые умножения на u_cscl, которые ломали звезды.
    vec2 starCoords = coords - (u_offset * PARALLAX_DEPTH);

    starCoords += distortion * REFLECTION_STRENGTH;
    starCoords += vec2(time * 15.0, time * 5.0);

    // Передаем u_cscl чисто для защиты от "исчезновения" при отдалении камеры
    float stars = getStars(starCoords, time, u_cscl);
    vec3 starColor = vec3(stars) * vec3(0.9, 0.95, 1.0);

    vec3 finalColor = mix(underIceColor.rgb, iceTint, ICE_OPACITY * b);
    finalColor += starColor * (1.2 - b * 0.5);

    gl_FragColor = vec4(finalColor, origAlpha);
}