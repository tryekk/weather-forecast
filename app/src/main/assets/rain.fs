precision highp float;

varying vec2 v_TexCoord;

uniform float u_Time;
uniform sampler2D u_Texture;

// Pseudo random number generator.
float hash( vec2 a )
{
    return fract( sin( a.x * 3433.8 + a.y * 3843.98 ) * 45933.8 );
}

// Value noise courtesy of BigWingz
// check his youtube channel he has
// a video of this one.
// Succint version by FabriceNeyret
float noise( vec2 U )
{
    vec2 id = floor( U );
          U = fract( U );
    U *= U * ( 3. - 2. * U );

    vec2 A = vec2( hash(id)            , hash(id + vec2(0,1)) ),
         B = vec2( hash(id + vec2(1,0)), hash(id + vec2(1,1)) ),
         C = mix( A, B, U.x);

    return mix( C.x, C.y, U.y );
}

void main( void ) {

	vec2 uv = v_TexCoord;

	vec2 n1 = vec2(noise(20. * uv), noise(20. * uv + .8));

	vec4 colour = texture2D(u_Texture, uv + n1 * 0.01);

        // Blur
        // for( float d= 0.; d < 3.14; d += 3.14 / 16.)
        // {
        //    for(float i = 1. / 3.; i <= 1.0; i += 1. / 3.)
        //    {
        //        colour += texture2D(u_Texture, uv + vec2(cos(d), sin(d)) * 0.005 * i);
        //    }
        // }

    	// gl_FragColor = colour / (3. * 16. - 15.0);

    	gl_FragColor = colour;


	for (float r = 3.; r > 0.; r--) {

        vec2 size = r * 10. * vec2(1080., 2340.) / 2340.;

		vec3 n2 = vec3(noise(10. * floor(size * uv + .25) / size), noise(10. * floor(size * uv + .25) / size + 1.), noise(10. * floor(size * uv + .25) / size + 2.));

		vec2 grid = 6.28 * uv * size + (n1 - .5) * 2.;

		vec2 s = sin(grid);

		float t = (s.x + s.y) * max(0., 1. - fract(u_Time * (n2.r + .1) + n2.g) * 3.);

		if (n2.b < (3. - r) * .15 && t > .5)
		{
		    vec3 v = normalize(-vec3(cos(grid), mix(.2, 2., t - .5)));
            gl_FragColor = texture2D(u_Texture, uv + v.xy * .3);

            // gl_FragColor = vec4(t * .1, t * .4, 0.8 + t * .2, t);
        }
	}

}