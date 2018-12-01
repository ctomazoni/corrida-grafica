package main;
/**
 * Objetivo: trabalha com conceitos da camera sintetica
 * https://www.opengl.org/sdk/docs/man2/xhtml/
 */

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.media.opengl.DebugGL;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;
//import javax.media.opengl.glu.GLUquadric;

import com.sun.opengl.util.GLUT;

import model.Camera;
import model.Carro;
import model.Cubo;
import objmodel.object.OBJModel;

public class Main implements GLEventListener, KeyListener {
	private GL gl;
	private GLU glu;
	private GLUT glut;
	private GLAutoDrawable glDrawable;
    
    private boolean eHMaterial = true;
    
    private Camera cameraPrimeiraPessoa;
    private Camera cameraAtiva;
    
    private Carro carroModel;

	public void init(GLAutoDrawable drawable) {
		glDrawable = drawable;
		gl = drawable.getGL();
		glu = new GLU();
		glut = new GLUT();
		glDrawable.setGL(new DebugGL(gl));

		gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
		
		cameraPrimeiraPessoa = new Camera(glu);
		cameraPrimeiraPessoa.setxEye(2.f);
		cameraPrimeiraPessoa.setyEye(20.f);
		cameraPrimeiraPessoa.setzEye(4.f);
		cameraPrimeiraPessoa.setxCenter(0.f);
		cameraPrimeiraPessoa.setyCenter(0.f);
		cameraPrimeiraPessoa.setzCenter(0.f);
		
		cameraAtiva = cameraPrimeiraPessoa;
		
		 carroModel = new Carro(gl);
		
		ligarLuz();
		
	    gl.glEnable(GL.GL_CULL_FACE);
//	    gl.glDisable(GL.GL_CULL_FACE);
		
	    gl.glEnable(GL.GL_DEPTH_TEST);
//	    gl.glDisable(GL.GL_DEPTH_TEST);
	}
	
	public void display(GLAutoDrawable arg0) {
		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
		
        gl.glMatrixMode(GL.GL_MODELVIEW);
        gl.glLoadIdentity();
		cameraAtiva.lookAt();
		
		desenhaSRU3D();
		float[] white = {1.0f,1.0f,1.0f,1.0f};
		gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE, white, 0);
		gl.glEnable(GL.GL_LIGHTING);
		
		carroModel.draw(gl);
		
		gl.glDisable(GL.GL_LIGHTING);
		
		gl.glFlush();
	}

	public void keyPressed(KeyEvent e) {

		switch (e.getKeyCode()) {
			case KeyEvent.VK_ESCAPE:
				System.exit(0);
			break;
			case KeyEvent.VK_D:
//				objetos[0].debug();
			break;
			case KeyEvent.VK_R:
//				objetos[0].atribuirIdentidade();
			break;
			case KeyEvent.VK_SPACE:
				//TODO: ERRO ao reinicar a thread
//				manual = !manual;
//				if (manual) animacao.stop();
//				else animacao.start();
			break;
			case KeyEvent.VK_RIGHT:
				carroModel.virarDireita();
				break;
			case KeyEvent.VK_LEFT:
				carroModel.virarEsquerda();
				break;
			case KeyEvent.VK_UP:
				carroModel.acelerar();
				break;
			case KeyEvent.VK_DOWN:
				carroModel.freiar();
				break;
//			case KeyEvent.VK_1:
//				xEye = 0.0f; 		yEye = 40.0f; 		zEye = 0.0f;
//				xUp = 0.0f;		yUp = 0.0f;		zUp = -1.0f;
//			break;
//			case KeyEvent.VK_2:
//				xEye = 30.0f; 		yEye = 20.0f; 		zEye = 20.0f;
//				xUp = 0.0f;		yUp = 1.0f;		zUp = 0.0f;
//			break;
//			case KeyEvent.VK_3:
//				xEye = 0.0f; 		yEye = 200.0f; 		zEye = 00.0f;
//				xUp = 0.0f;		yUp = 0.0f;		zUp = -1.0f;
//			break;s
		}

		glDrawable.display();
	}

	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
	    gl.glMatrixMode(GL.GL_PROJECTION);
	    gl.glLoadIdentity();
		gl.glViewport(0, 0, width, height);

//		glu.gluOrtho2D(-30.0f, 30.0f, -30.0f, 30.0f);
	    glu.gluPerspective(60, width/height, 0.1, 100);				// projecao Perpectiva 1 pto fuga 3D    
//		gl.glFrustum (-5.0, 5.0, -5.0, 5.0, 10, 100);			// projecao Perpectiva 1 pto fuga 3D
//	    gl.glOrtho(-30.0f, 30.0f, -30.0f, 30.0f, -30.0f, 30.0f);	// projecao Ortogonal 3D

//		Debug();
	}

	public void desenhaSRU3D() {
		gl.glColor3f(1.0f, 0.0f, 0.0f);
		gl.glBegin(GL.GL_LINES);
			gl.glVertex3f( 0.0f, 0.0f, 0.0f);
			gl.glVertex3f(20.0f, 0.0f, 0.0f);
		gl.glEnd();
		gl.glColor3f(0.0f, 1.0f, 0.0f);
		gl.glBegin(GL.GL_LINES);
			gl.glVertex3f(0.0f,  0.0f, 0.0f);
			gl.glVertex3f(0.0f, 20.0f, 0.0f);
		gl.glEnd();
		gl.glColor3f(0.0f, 0.0f, 1.0f);
		gl.glBegin(GL.GL_LINES);
			gl.glVertex3f(0.0f, 0.0f,  0.0f);
			gl.glVertex3f(0.0f, 0.0f, 20.0f);
		gl.glEnd();
	}

	private void ligarLuz() {
		if (eHMaterial) {
			float posLight[] = { 5.0f, 5.0f, 10.0f, 0.0f };
			gl.glLightfv(GL.GL_LIGHT0, GL.GL_POSITION, posLight, 0);
			gl.glEnable(GL.GL_LIGHT0);
		}
		else
			gl.glDisable(GL.GL_LIGHT0);
	}

	public void displayChanged(GLAutoDrawable arg0, boolean arg1, boolean arg2) {
	}

	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

}
