attribute vec4 a_position;
attribute vec4 a_color;
attribute vec2 a_texCoord0;
attribute vec2 a_texCoord1;
attribute vec2 a_spriteAngle;

uniform mat4 u_proj;
uniform mat4 u_trans;
uniform mat4 u_projTrans;
uniform vec3 dirVector;

varying vec4 v_color;
varying vec2 v_texCoords;
varying vec2 v_normalCoords;
varying vec2 v_spriteAngle;
varying vec2 v_lightCoord;
varying vec2 v_lightDirCoord;
varying vec3 v_dirLight;

void main()
{
    v_color = a_color;
    v_texCoords = a_texCoord0;
    v_normalCoords = a_texCoord1;
    gl_Position = u_projTrans * a_position;
    v_spriteAngle = a_spriteAngle;

    //optimizations - calculate stuff which doesnt change
    v_lightCoord.x = 0.25 + gl_Position.x * 0.25;
    v_lightCoord.y = 0.5 + gl_Position.y * 0.5;

    v_lightDirCoord.x = 0.75 + gl_Position.x * 0.25;
    v_lightDirCoord.y = 0.5 + gl_Position.y * 0.5;

    float cos = float (v_spriteAngle.x );
    float sin = float (v_spriteAngle.y );

    vec3 dirL = vec3(0.0);
    dirL.x = dirVector.x * cos - dirVector.y * sin;
    dirL.y = dirVector.x * sin + dirVector.y * cos;
    dirL.z = dirVector.z;
    v_dirLight = normalize(dirL);
}



