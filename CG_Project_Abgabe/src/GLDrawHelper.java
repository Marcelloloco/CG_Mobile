

// original code from http://code.google.com/p/oglsuperbible5/source/browse/trunk/Src/GLTools/src/GLTools.cpp
// Ported to Java by D.Strippgen 2012

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

public class GLDrawHelper {
	
	
/////////////////////////////////////////////////////////////////////////////////////////////////
	// Make a sphere
	public static void drawSphere(float fRadius, int iSlices, int iStacks)
	        {
	    float drho = (float)(3.141592653589) / (float) iStacks;
	    float dtheta = 2.0f * (float)(3.141592653589) / (float) iSlices;
	        float ds = 1.0f / (float) iSlices;
	        float dt = 1.0f / (float) iStacks;
	        float t = 1.0f;       
	        float s = 0.0f;
	    int j, k;     // Looping variables

	    GL11.glBegin(GL11.GL_TRIANGLES);
	        for (k = 0; k < iStacks; k++) 
	                {
	                float rho = (float)k * drho;
	                float srho = (float)(Math.sin(rho));
	                float crho = (float)(Math.cos(rho));
	                float srhodrho = (float)(Math.sin(rho + drho));
	                float crhodrho = (float)(Math.cos(rho + drho));
	                
	        // Many sources of OpenGL sphere drawing code uses a triangle fan
	        // for the caps of the sphere. This however introduces texturing 
	        // artifacts at the poles on some OpenGL implementations
	        s = 0.0f;
            float[][] vVertex = new float [4][3] ;
            float[][] vNormal = new float [4][3] ;
            float[][] vTexture = new float [4][3] ;

	                for ( j = 0; j < iSlices; j++) 
	                        {
	                        float theta = (j == iSlices) ? 0.0f : j * dtheta;
	                        float stheta = (float)(-Math.sin(theta));
	                        float ctheta = (float)(Math.cos(theta));
	                        
	                        float x = stheta * srho;
	                        float z = ctheta * srho;
	                        float y = crho;
	        
	                        vTexture[0][0] = s;
	                        vTexture[0][1] = t;
	                        vNormal[0][0] = x;
	                        vNormal[0][1] = y;
	                        vNormal[0][2] = z;
	                        vVertex[0][0] = x * fRadius;
	                        vVertex[0][1] = y * fRadius;
	                        vVertex[0][2] = z * fRadius;
	                        
	            x = stheta * srhodrho;
	                        z = ctheta * srhodrho;
	                        y = crhodrho;

	                        vTexture[1][0] = s;
	                        vTexture[1][1] = t - dt;
	                        vNormal[1][0] = x;
	                        vNormal[1][1] = y;
	                        vNormal[1][2] = z;
	                        vVertex[1][0] = x * fRadius;
	                        vVertex[1][1] = y * fRadius;
	                        vVertex[1][2] = z * fRadius;
	                        

	                        theta = ((j+1) == iSlices) ? 0.0f : (j+1) * dtheta;
	                        stheta = (float)(-Math.sin(theta));
	                        ctheta = (float)(Math.cos(theta));
	                        
	                        x = stheta * srho;
	                        z = ctheta * srho;
	                        y = crho;
	        
	            s += ds;
	                        vTexture[2][0] = s;
	                        vTexture[2][1] = t;
	                        vNormal[2][0] = x;
	                        vNormal[2][1] = y;
	                        vNormal[2][2] = z;
	                        vVertex[2][0] = x * fRadius;
	                        vVertex[2][1] = y * fRadius;
	                        vVertex[2][2] = z * fRadius;
	                        
	            x = stheta * srhodrho;
	                        z = ctheta * srhodrho;
	                        y = crhodrho;

	                        vTexture[3][0] = s;
	                        vTexture[3][1] = t - dt;
	                        vNormal[3][0] = x;
	                        vNormal[3][1] = y;
	                        vNormal[3][2] = z;
	                        vVertex[3][0] = x * fRadius;
	                        vVertex[3][1] = y * fRadius;
	                        vVertex[3][2] = z * fRadius;
	                
	                        GL11.glTexCoord2f(vTexture[0][0], vTexture[0][1]);
	                        GL11.glNormal3f(vNormal[0][0], vNormal[0][1],vNormal[0][2]);
	                        GL11.glVertex3f(vVertex[0][0], vVertex[0][1],vVertex[0][2]);

	                        GL11.glTexCoord2f(vTexture[1][0], vTexture[1][1]);
	                        GL11.glNormal3f(vNormal[1][0], vNormal[1][1],vNormal[1][2]);
	                		GL11.glVertex3f(vVertex[1][0], vVertex[1][1],vVertex[1][2]);
	                        
	                		GL11.glTexCoord2f(vTexture[2][0], vTexture[2][1]);
	                        GL11.glNormal3f(vNormal[2][0], vNormal[2][1],vNormal[2][2]);
	                		GL11.glVertex3f(vVertex[2][0], vVertex[2][1],vVertex[2][2]);

	                		for(int i=0;i<3;i++){vVertex[0][i] = vVertex[1][i];
	                							vNormal[0][i] = vNormal[1][i];
	                							vTexture[0][i] = vTexture[1][i];}
	                		for( int   i=0;i<3;i++){vVertex[1][i] = vVertex[3][i];
	                								vNormal[1][i] = vNormal[3][i];
	                								vTexture[1][i] = vTexture[3][i];}

	                        GL11.glTexCoord2f(vTexture[0][0], vTexture[0][1]);
	                        GL11.glNormal3f(vNormal[0][0], vNormal[0][1],vNormal[0][2]);
	                        GL11.glVertex3f(vVertex[0][0], vVertex[0][1],vVertex[0][2]);

	                        GL11.glTexCoord2f(vTexture[1][0], vTexture[1][1]);
	                        GL11.glNormal3f(vNormal[1][0], vNormal[1][1],vNormal[1][2]);
	                		GL11.glVertex3f(vVertex[1][0], vVertex[1][1],vVertex[1][2]);

	                        GL11.glTexCoord2f(vTexture[2][0], vTexture[2][1]);
	                		GL11.glNormal3f(vNormal[2][0], vNormal[2][1],vNormal[2][2]);
	                		GL11.glVertex3f(vVertex[2][0], vVertex[2][1],vVertex[2][2]);
	                        }
	        t -= dt;
	        }

	        GL11.glEnd();
	    }
	    


	public static FloatBuffer directFloatBuffer(float[]buffer){
		ByteBuffer bb = ByteBuffer.allocateDirect(4*buffer.length);
		bb.order(ByteOrder.nativeOrder());
		FloatBuffer fb = bb.asFloatBuffer();
		fb.put(buffer).flip();
		return fb;
	}

	public static IntBuffer directIntBuffer(int[]buffer){
		ByteBuffer bb = ByteBuffer.allocateDirect(4*buffer.length);
		bb.order(ByteOrder.nativeOrder());
		IntBuffer ib = bb.asIntBuffer();
		ib.put(buffer).flip();
		return ib;
	}
	
//////////////////////////////////////////////////////////////////////
	public static int compileShader(String filename, int type){
		int f = GL20.glCreateShader(type);

		BufferedReader brv;
		try {
			brv = new BufferedReader(new FileReader(filename));
			String vsrc = "";
			String line;
			while ((line=brv.readLine()) != null) {
			  vsrc += line + "\n";
			}
			GL20.glShaderSource(f, vsrc);
			GL20.glCompileShader(f);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return f;
	}
	
	public static String getShaderInfoLog(int shaderID)
	{
		return GL20.glGetShaderInfoLog(shaderID, GL20.glGetShader(shaderID, GL20.GL_INFO_LOG_LENGTH));
	}
	
	public static int compileAndLink(String vertfile, String fragfile, Map<Integer, String> myAttribLocation) {
		int shaderprogram = -1;
		int v = compileShader(vertfile, GL20.GL_VERTEX_SHADER);
		int f = compileShader(fragfile, GL20.GL_FRAGMENT_SHADER);

		shaderprogram = GL20.glCreateProgram();
		GL20.glAttachShader(shaderprogram, v);
		GL20.glAttachShader(shaderprogram, f);

		for(Integer i : myAttribLocation.keySet()){
			GL20.glBindAttribLocation(shaderprogram, i, myAttribLocation.get(i));
		}
		
		GL20.glLinkProgram(shaderprogram);
		GL20.glValidateProgram(shaderprogram);
		int error = GL11.glGetError();
		String info = getShaderInfoLog(f);
		System.out.println("Error GL: " + error +": " + info);
		info = getShaderInfoLog(v);
		System.out.println("Error GL: " + error +": " + info);
		return shaderprogram;
	}
	
	public static String getRandomTexturePath(String path){
		String fullpath = null;
		File pathfile = new File(path);
		if(pathfile.isDirectory()){
			File[] textures = pathfile.listFiles(new FilenameFilter() {
				
				@Override
				public boolean accept(File dir, String name) {
					if(name.endsWith(".jpg") || name.endsWith(".png"))return true;
					return false;
				}
			});
			if(textures.length >0){
				return textures[(int)(Math.random()*textures.length)].getAbsolutePath();
			}
		}
		return fullpath;
	}

}
