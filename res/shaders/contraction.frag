uniform sampler2D u_texture;

uniform vec2 u_campos;
uniform vec2 u_resolution;
uniform float u_time;

uniform vec2 u_texsize;
uniform vec2 u_offset;
uniform float u_per;
//uniform float u_tire;

varying vec2 v_texCoords;

float random(in vec2 st) {
    return fract(sin(dot(st.xy,
    vec2(12.9898,78.233)))
    * 43758.5453123);
}


float len(vec3 a) {
    return sqrt(a.x*a.x + a.y*a.y + a.z*a.z);
}

float dvnoise (in vec2 st) {
    vec2 i = floor(st);
    vec2 f = fract(st);
    float a = random(i);
    float b = random(i + vec2(1.0, 0.0));
    float c = random(i + vec2(0.0, 1.0));
    float d = random(i + vec2(1.0, 1.0));
    vec2 u = f*f*(3.0-2.0*f);
    return mix(a, b, u.x) +
    (c - a)* u.y * (1.0 - u.x) +
    (d - b) * u.x * u.y;
}

float fbm(vec2 x) {
    float v = 0.0;
    float a = 0.5;
    vec2 shift = vec2(300);
    for (int i = 0; i < 8; ++i) {
        v += a * dvnoise(x);
        x = x * 2.0 + shift;
        a *= 0.5;
    }
    return v;
}



//void main(){gl_FragColor = vec4(1,1,1,max(texture2D(u_texture, v_texCoords).a, 0.2));}
void main(){
    vec2 T = v_texCoords.xy;
    vec2 coords = (T * u_texsize) + u_offset;

    vec4 col = texture2D(u_texture, T);
    vec4 color = vec4(col);

    if (col.a > 0) {
        vec2 st = coords/5.2;
        // st += st * abs(sin(u_time*0.1)*3.0);
        color.rgb = vec3(0.0);

        vec2 q = vec2(0.);
        q.x = fbm( st + 0.00*u_time/7.);
        q.y = fbm( st + vec2(1.0));

        vec2 r = vec2(0.);
        r.x = fbm( st + 1.0*q + vec2(1.7,9.2)+ 0.15*u_time/7.);
        r.y = fbm( st + 1.0*q + vec2(8.3,2.8)+ 0.126*u_time/7.);

        float f = fbm(st+r);

        color.rgb = mix(col.rgb/5.6,
        col.rgb/3.3,
        clamp((f*f)*4.0,0.0,1.0));

        color.rgb = mix(color.rgb,
        col.rgb/1.4,
        clamp(length(q),0.0,1.0));

        color.rgb = mix(color.rgb,
        col.rgb,
        clamp(length(r.x),0.0,1.0));

//        color.rgb = mix(vec3(0.610,0.537,0.234),
//        vec3(0.175,0.145,0.050),
//        clamp((f*f)*4.0,0.0,1.0));
//
//        color.rgb = mix(color.rgb,
//        vec3(0.820,0.611,0.204),
//        clamp(length(q),0.0,1.0));
//
//        color.rgb = mix(color.rgb,
//        vec3(1.000,0.726,0.121),
//        clamp(length(r.x),0.0,1.0));

        float noise = (fbm(vec2(coords / 2.2 + vec2(u_time/60.0))) + 1.) / 2.0;

//        color.a = pow(len(color.rgb)/sqrt(2.)+0.2, 32)*512.;
        if (noise < u_per) color.a = 0.;


        color.rgb = vec3((f*f+.6*f*f+.5*f)*color);
//        color.rgb = abs(color.rgb-0.4);
        color.r = pow(color.r, 0.5);
        color.g = pow(color.g, 0.5);
        color.b = pow(color.b, 0.5);

//        color.rgb *= noise;
    }


    gl_FragColor = color;
}
