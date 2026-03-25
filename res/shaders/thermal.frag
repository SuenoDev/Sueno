uniform sampler2D u_texture;
uniform sampler2D u_colormap;
varying vec2 v_texCoords;

void main() {
    float temp = texture2D(u_texture, v_texCoords).r;

    float normalized = (temp + 1.0) * 0.5;

    vec4 color = texture2D(u_colormap, vec2(normalized, 0.5));

    float alpha = abs(temp);
    gl_FragColor = vec4(color.rgb, alpha * 0.6);
}