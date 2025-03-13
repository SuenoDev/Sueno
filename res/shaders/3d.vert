uniform mat4 u_proj;
uniform vec3 u_pos;
uniform vec3 u_rot;
uniform mat4 u_trans;
uniform vec3 u_lightdir;
uniform vec3 u_campos;
uniform vec3 u_ambient;
uniform float u_diffuse;
uniform float u_specular;
uniform float u_shininess;
uniform vec3 u_scale;


attribute vec4 a_position;
attribute vec3 a_normal;
attribute vec2 a_texCoord0;


varying vec2 v_texcoord;
varying vec3 v_col;
varying vec3 v_pos;

mat4 rotationMatrix(vec3 eulerAngles) {
    float roll = eulerAngles.x;
    float pitch = eulerAngles.y;
    float yaw = eulerAngles.z;

    float cr = cos(roll);
    float sr = sin(roll);
    float cp = cos(pitch);
    float sp = sin(pitch);
    float cy = cos(yaw);
    float sy = sin(yaw);

    mat4 rotationX = mat4(
    1, 0, 0, 0,
    0, cr, -sr, 0,
    0, sr, cr, 0,
    0, 0, 0, 1
    );

    mat4 rotationY = mat4(
    cp, 0, sp, 0,
    0, 1, 0, 0,
    -sp, 0, cp, 0,
    0, 0, 0, 1
    );

    mat4 rotationZ = mat4(
    cy, -sy, 0, 0,
    sy, cy, 0, 0,
    0, 0, 1, 0,
    0, 0, 0, 1
    );

    return rotationX * rotationY * rotationZ;
}

void main() {
    v_texcoord = a_texCoord0;
    vec4 scaled_position = a_position * vec4(u_scale, 1.0);
    vec4 translated_position = scaled_position;;
    mat4 rotation = rotationMatrix(u_rot);
    vec4 rotated_position = rotation * translated_position + vec4(u_pos, 0.0);
    mat3 normalRotationMatrix = mat3(rotation);
    vec3 rot_norm = normalRotationMatrix * a_normal;
    float ins = max((dot(normalize(rot_norm), normalize(u_lightdir))+1.)/2., 0.);
    v_col = vec3(ins*ins);
    v_pos = vec3(u_trans * rotated_position);
    gl_Position = (u_proj * u_trans) * rotated_position;
}