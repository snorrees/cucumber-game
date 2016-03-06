#ifdef GL_ES
precision mediump float;
#endif

varying vec4 v_color;
varying vec2 v_texCoords;
varying vec2 v_normalCoords;
varying vec2 v_spriteAngle;
varying vec2 v_lightCoord;
varying vec2 v_lightDirCoord;
varying vec3 v_dirLight;

uniform sampler2D u_texture;
uniform sampler2D u_normals;
uniform sampler2D u_lightmap;

uniform vec4 ambientColor;
uniform vec4 dirColor;
uniform vec3 dirVector;

void main() {

    vec4 light = texture2D(u_lightmap, v_lightCoord);
    vec3 lightDirMap = texture2D(u_lightmap, v_lightDirCoord).rgb;
    vec4 diffuseColor = texture2D(u_texture, v_texCoords);
    vec4 normalMap = texture2D(u_normals, v_normalCoords);
    normalMap.g = 1.0 - normalMap.g;

    if(v_color.r < 0.5){
        float gray = dot(diffuseColor.rgb, vec3(0.299, 0.587, 0.114));
        diffuseColor.rgb = vec3(gray);
    }

    //normals need to be converted to [-1.0, 1.0] range and normalized
    vec3 N = normalize(normalMap.rgb * 2.0 - 1.0);

    vec3 lightDir = vec3(0.0);
    lightDir.xy = lightDirMap.xy * -2.0 + 1.0 ;
    lightDir.z = lightDirMap.z * 2.0 - 1.0 ;
    lightDir = normalize(lightDir);

    vec3 sum = vec3(0.0);

    //Ambient light
//    diffuseColor.rgba = vec3(1.0, 1.0, 1.0);
     sum += diffuseColor.rgb * ambientColor.rgb;

    //Directional light
    vec3 dirL = vec3(0.0);
    vec3 dirDiffuse  = vec3(0.0);

    float dirColorIntensity = max(dot(N, v_dirLight), 0.0);
    dirDiffuse = (dirColor.rgb ) * dirColorIntensity;
    sum += diffuseColor.rgb * dirDiffuse ;

    //pointlight

    float cos = float (v_spriteAngle.x );
    float sin = float (v_spriteAngle.y );

    dirL.x = lightDir.x * cos - lightDir.y * sin;
    dirL.y = lightDir.x * sin + lightDir.y * cos;
    dirL.z = lightDir.z;
    dirL = normalize(dirL);
    float colourIntensity = max(dot(N, dirL), 0.0);
    dirDiffuse = (light.rgb) * colourIntensity;
    sum += diffuseColor.rgb * dirDiffuse;
    sum += diffuseColor.rgb * light.rgb * 0.3;

    float specModifier = colourIntensity;
    //smaller pow is more glossy
    float a = normalMap.a ;
    float scaledA = a*8.0;
    float specLevel = floor(scaledA);
    float spec = scaledA - specLevel;
    float specPow = pow(2.0, specLevel  + 2.0);
    sum += pow(specModifier, specPow) * light.rgb * spec;
    sum += pow(dirColorIntensity, specPow) * dirColor.rgb * spec;


   gl_FragColor = /*v_color * */ vec4(sum, diffuseColor.a);

   //Light color debug
   //gl_FragColor = vec4(light.rgb , 1);

   //Light direction debug
//     gl_FragColor = vec4(lightDirMap.rgb  , 1);
     // gl_FragColor = vec4(lightDir.rgb  , 1);

   //Specular power debug
  // gl_FragColor = vec4(vec3(specPow/1024.0) , 1);
  // gl_FragColor = light.rgba;
 }