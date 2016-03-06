#ifdef GL_ES
precision mediump float;
#endif

uniform sampler2D u_sampler0;

uniform float u_displacement;
uniform float u_displacement2;

varying vec4 v_col;
varying vec2 p;


float fbm(vec2 n) {
	float total = 0.0, amplitude = 1.0;
	for (int i = 0; i < 5; i++) {

		total += texture2D( u_sampler0, n/164.0).x * amplitude;
		n += n*1.5;
		amplitude *= 0.55;
	}
	return  total;
}

void main() {
    float fm = fbm(p * 0.9);
    float fm2 = fbm(p.xy + cos(u_displacement2));

   gl_FragColor = vec4(v_col.rgb *
        fbm(p * fm + u_displacement2 + fm2), 1.0)
        * mix (0.7, 0.9, fm + u_displacement);
}