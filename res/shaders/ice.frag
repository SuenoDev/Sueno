uniform sampler2D u_texture;
uniform vec2 u_texsize;
uniform vec2 u_offset;
uniform float u_time;



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
    for( int j=-3; j<=3; j++ )
    for( int i=-3; i<=3; i++ )
    {
        vec2 g = vec2( float(i),float(j) );
        vec3 o = hash3( p + g )*vec3(1.,1.,1.0);
        vec2 r = g - f + o.xy;
        float d = dot(r,r);
        float ww = pow( 1.0-smoothstep(0.0,1.414,sqrt(d)), k );
        va += o.z*ww;
        wt += ww;
    }

    return va/wt;
}

vec3 m(vec3 a, vec3 b, float c) {
    float d = 1.-c;
    return vec3((a.x*c+b.x*d)/2., (a.y*c+b.y*d)/2., (a.z*c+b.z*d)/2.);
}

void main(){
    vec2 T = v_texCoords.xy;
    vec2 coords = (T * u_texsize) + u_offset;

    float b = pow(iqnoise(coords/9.)+1.,2.)/4.;
    float time = u_time/180.;

//    float f = clamp((pow(sin(time*2.+2.),2.) + sin(time/62.-155.)+pow(sin(1.2*time-2.),3.) + sin(3.*time+3.))/4., 0, 1);
    vec3 r = vec3(0.6896078431372549,1,0.9907843137254902)*b;

    gl_FragColor = vec4(r.x,r.y,r.z,texture2D(u_texture, T).a);
}