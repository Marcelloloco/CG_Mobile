import java.io.IOException;

import org.lwjgl.opengl.GL11;

import edu.berlin.htw.ds.cg.helper.TextureReader;

public class Fork {
	float pitch = 0;
	float yaw = 0;
	float height;
	float width;
	Fork leftChild;
	Fork rightChild;
	boolean noChild;
	int textureID ;
	int pitchDir;
	int maxAngle = 30;
	TextureReader.Texture texture = null;
	
	
	public Fork(float pitch, float yaw, float height, float width, Fork leftChild, Fork rightChild){
		this.pitch = pitch;
		this.yaw = yaw;
		this.height = height;
		this.width = width;
		this.leftChild = leftChild;
		this.rightChild = rightChild;
		this.noChild = false;
	}

	public Fork(){
		this.noChild = true;
		
		try{
			texture = TextureReader.readTexture(GLDrawHelper.getRandomTexturePath("C:\\Users\\Marcel\\Documents\\Workspace_Backup\\CGSS15Ex3MobileDS\\dataEx3\\Textures"),false);

		}catch(IOException e){
			System.out.println(e);
		}
		
		textureID = GL11.glGenTextures();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGB, texture.getWidth(), texture.getHeight(), 0, GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE, texture.getPixels());
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
	}
	

	public void render(boolean left){
		
		//pitch= movement up + down
		if(pitch >= maxAngle){
			pitchDir = -1;
		}else if(pitch <= -maxAngle){
			pitchDir = 1;
		}
		pitch = pitch + (0.02f * pitchDir); 
		
		if(noChild){
		GL11.glColor3f(1.0f, 1.0f, 1.0f); 
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);

		GLDrawHelper.drawSphere(70, 15, 15);
		
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		
		
		}else{
			if(left ==true){
				GL11.glTranslatef(0, -height - pitch, 0);
			}else if(left == false){
				GL11.glTranslatef(0, -height + pitch, 0);
			}
			GL11.glRotatef(yaw, 0, 1, 0);
			
			GL11.glLineWidth(5);
			GL11.glBegin(GL11.GL_LINES);
			
			GL11.glVertex3f(0, 0, 0);
			GL11.glVertex3f(0, height, 0);
			
			GL11.glVertex3f(0, 0, 0);
			GL11.glVertex3f(width/2, pitch, 0);
			
			
			GL11.glVertex3f(0, 0, 0);
			GL11.glVertex3f(-(width/2), -pitch, 0);
			
			GL11.glEnd();
			
			GL11.glPushMatrix();
			GL11.glTranslatef(-(width/2), 0, 0);
			leftChild.render(true);
			
			GL11.glPopMatrix();
			GL11.glTranslatef(width/2, 0, 0);
		
			rightChild.render(false);
			yaw += 0.02;
		}

	}
}
