import java.io.IOException;
import java.nio.FloatBuffer;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import edu.berlin.htw.ds.cg.helper.TextureReader;

public class Mobile {
	private float len = 400;
	private float wid = 300;
	private Fork fork;
	private int width = 800;
	private int height = 640;
	private int CamSpeed = 1;
	public float yCamAngle = -height;
	public float xCamAngle = 0;
	public long lastTime = getTime();
	private double yaw;
	private double pitch;
	private int x;
	private int y;
	public float[] lookDir;
	private static GLU glu = new GLU();
	
	
	static FloatBuffer noAmbient = GLDrawHelper.directFloatBuffer(new float[] {00.0f, 0.0f, 0.0f, 1.0f});
	static FloatBuffer whiteDiffuse =  GLDrawHelper.directFloatBuffer(new float[]{1.0f, 1.0f, 1.0f, 1.0f});
	static FloatBuffer position = GLDrawHelper.directFloatBuffer(new float[]{0.0f, 0.0f, 0.0f, 1.0f});


	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Mobile mobile = new Mobile();
		mobile.run();
	}

	public Mobile(){
		
	}
	
	public void run(){
		setup();

		while (!Display.isCloseRequested()){
			mouseMoved();
			update();
			draw();
		}

		finish();
	}
	
	public void setup() {
		try
		{
			Display.setDisplayMode(new DisplayMode(width, height));
			Display.create();
			

		} catch (LWJGLException e){
			e.printStackTrace();
			System.exit(0);
		}
		

		fork = new Fork(40,70,len,wid,
				new Fork(40,70,len,wid,new Fork(), new Fork()),
				new Fork(40,70,len,wid,
						new Fork(40,70,len*0.75f,wid*0.75f,new Fork(), new Fork()),
						new Fork(40,70,len*0.75f,wid*0.75f,new Fork(), new Fork())));
				
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		glu.gluPerspective(45.f, width/(float)height , 0.1f, 4000.f);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		
		GL11.glEnable(GL11.GL_LIGHTING); 
		GL11.glLight(GL11.GL_LIGHT0, GL11.GL_AMBIENT,noAmbient);
		GL11.glLight(GL11.GL_LIGHT0, GL11.GL_DIFFUSE,whiteDiffuse);
		GL11.glEnable(GL11.GL_LIGHT0);
		
		GL11.glColorMaterial(GL11.GL_FRONT_AND_BACK, GL11.GL_AMBIENT_AND_DIFFUSE);
		GL11.glEnable(GL11.GL_COLOR_MATERIAL);
	}
	
	public void finish() {
		Display.destroy();	
	}
	
	public void mouseMoved(){
		yaw -= 0.01 * (Mouse.getX() - x);
		pitch -= 0.01 * (Mouse.getY() - y);
		
		pitch = Math.max(-Math.PI * 0.45, pitch);
		pitch = Math.min(Math.PI * 0.45, pitch);
		
		lookDir = new float[] {
				(float) (Math.sin(this.yaw) * Math.cos(this.pitch)),
				(float) -(Math.sin(this.pitch)),
				(float) (Math.cos(this.yaw) * Math.cos(this.pitch))
		};
		x = Mouse.getX();
		y = Mouse.getY();
	}

	public void draw() {
		GL11.glClearColor(0, 0, 0, 0);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
		
		float[] cameraPos = new float[]{xCamAngle, 200,  yCamAngle}; 
		glu.gluLookAt(cameraPos[0], cameraPos[1], cameraPos[2], (cameraPos[0] + lookDir[0]), (cameraPos[1] + lookDir[1]), (cameraPos[2] + lookDir[2]), 0, 1, 0);
		
		GL11.glLight(GL11.GL_LIGHT0, GL11.GL_POSITION,position);

		fork.render(true);
				
		Display.update();
	}

	public void update() {
		long time = getTime();
		float timeDiff = time - lastTime;
		lastTime = time;
		
		if(Keyboard.isKeyDown(Keyboard.KEY_W)) yCamAngle += CamSpeed * timeDiff;
		if(Keyboard.isKeyDown(Keyboard.KEY_S)) yCamAngle -= CamSpeed * timeDiff;
		
		if(Keyboard.isKeyDown(Keyboard.KEY_A)) xCamAngle -= CamSpeed * timeDiff;
		if(Keyboard.isKeyDown(Keyboard.KEY_D)) xCamAngle += CamSpeed * timeDiff;
		
	}
	
	private long getTime(){
		return(Sys.getTime() * 1000) / Sys.getTimerResolution();
	}
}