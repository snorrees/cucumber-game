attribute vec4 a_position;
attribute vec4 a_color;
uniform mat4 u_projModelView;
varying vec4 v_col;
varying vec2 p;

uniform vec2 resolution;
uniform sampler2D u_sampler0;


uniform float u_displacement;
uniform float u_displacement2;

float rand(vec2 co){
    return fract(sin(dot(co.xy ,vec2(12.9898,78.233))) * 43758.5453);
}

float fbm(vec2 n) {
	float total = 0.0, amplitude = 1.0;
	for (int i = 0; i < 4; i++) {

		total += texture2D( u_sampler0, n/256.0).x * amplitude;
		n += n*2.5;
		amplitude *= 0.5;
	}
	return  total;
}

void main() {
    p = vec2(a_position.y, a_position.x * 2.0) / resolution.xy * 2.0;

   gl_Position = u_projModelView * a_position;
   v_col = a_color ;
   gl_PointSize = 1.0;
}
