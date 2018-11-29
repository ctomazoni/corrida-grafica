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

public class Main implements GLEventListener, KeyListener {
	private GL gl;
	private GLU glu;
	private GLUT glut;
	private GLAutoDrawable glDrawable;
	private double xEye, yEye, zEye;
	private double xCenter, yCenter, zCenter;
	private float xTr, yTr, zTr;
	private float escalaCubo[] = { 9.0f, 9.0f, 9.0f };

    private float corRed[] = { 1.0f, 0.0f, 0.0f, 1.0f };
//  private float corGreen[] = { 0.0f, 1.0f, 0.0f, 1.0f };
//  private float corBlue[] = { 0.0f, 0.0f, 1.0f, 1.0f };
//  private float corWhite[] = { 1.0f, 1.0f, 1.0f, 1.0f };
//  private float corBlack[] = { 0.0f, 0.0f, 0.0f, 1.0f };
    
    private boolean eHMaterial = true;

	public void init(GLAutoDrawable drawable) {
		glDrawable = drawable;
		gl = drawable.getGL();
		glu = new GLU();
		glut = new GLUT();
		glDrawable.setGL(new DebugGL(gl));

		gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		xEye = 40.0f; 		yEye = 40.0f; 		zEye = 40.0f;
		xCenter = 0.0f;		yCenter = 0.0f;		zCenter = 0.0f;
		
		ligarLuz();
		
	    gl.glEnable(GL.GL_CULL_FACE);
//	    gl.glDisable(GL.GL_CULL_FACE);
		
	    gl.glEnable(GL.GL_DEPTH_TEST);
//	    gl.glDisable(GL.GL_DEPTH_TEST);
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

	public void display(GLAutoDrawable drawable) {
		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

        gl.glMatrixMode(GL.GL_MODELVIEW);
        gl.glLoadIdentity();
		glu.gluLookAt(xEye, yEye, zEye, xCenter, yCenter, zCenter, 0.0f, 1.0f, 0.0f);

		//drawAxis();
		
		xTr = 0.0f;
		yTr = 0.0f;
		zTr = 0.0f;
		
		gl.glColor3f(1.0f, 0.0f, 0.0f);
		drawCube(getTranslacaoCubo(),escalaCubo);
		zTr++;
		drawCube(getTranslacaoCubo(),escalaCubo);
		zTr++;
		drawCube(getTranslacaoCubo(),escalaCubo);
		xTr++;
		drawCube(getTranslacaoCubo(),escalaCubo);
		xTr++;
		drawCube(getTranslacaoCubo(),escalaCubo);
		zTr--;
		drawCube(getTranslacaoCubo(),escalaCubo);
		zTr--;
		drawCube(getTranslacaoCubo(),escalaCubo);
		zTr--;
		drawCube(getTranslacaoCubo(),escalaCubo);
		zTr--;
		drawCube(getTranslacaoCubo(),escalaCubo);
		xTr--;
		drawCube(getTranslacaoCubo(),escalaCubo);
		xTr--;
		drawCube(getTranslacaoCubo(),escalaCubo);
		xTr--;
		drawCube(getTranslacaoCubo(),escalaCubo);
		xTr--;
		drawCube(getTranslacaoCubo(),escalaCubo);
		xTr--;
		drawCube(getTranslacaoCubo(),escalaCubo);
		zTr--;
		drawCube(getTranslacaoCubo(),escalaCubo);
		zTr--;
		drawCube(getTranslacaoCubo(),escalaCubo);
		zTr--;
		drawCube(getTranslacaoCubo(),escalaCubo);
		
		gl.glFlush();
	}
	
	private float[] getTranslacaoCubo() {
		float translacaoCubo[] = { xTr, yTr, zTr };
		return translacaoCubo;
	}
	
	private void drawCube(float translacao[], float escala[]) {
		if (eHMaterial) {
			gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE, corRed, 0);
			gl.glEnable(GL.GL_LIGHTING);
		}

		gl.glPushMatrix();
			gl.glScalef(escala[0],escala[1],escala[2]);
			gl.glTranslated(translacao[0], translacao[1], translacao[2]);
			glut.glutSolidCube(1.0f);
		gl.glPopMatrix();
		
		if (eHMaterial) {
			gl.glDisable(GL.GL_LIGHTING);
		}
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

	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {

		case KeyEvent.VK_ESCAPE:
			System.exit(1);
		break;
		case KeyEvent.VK_1:
			xEye = 20.0f;
			yEye = 20.0f;
			zEye = 20.0f;
		break;
		case KeyEvent.VK_2:
			xEye = 0.0f;
			yEye = 0.0f;
			zEye = 20.0f;
			break;
		case KeyEvent.VK_3:
			xEye = 0.0f;
			yEye = 0.0f;
			zEye = -20.0f;
			break;
		case KeyEvent.VK_4:
			xEye = 1.0f;
			yEye = 0.0f;
			zEye = 0.0f;
			break;

		case KeyEvent.VK_M:
			eHMaterial = !eHMaterial;
			ligarLuz();
			break;
		}

		glDrawable.display();	
	}

	public void drawAxis() {
		// eixo X - Red
		gl.glColor3f(1.0f, 0.0f, 0.0f);
		gl.glBegin(GL.GL_LINES);
			gl.glVertex3f(0.0f, 0.0f, 0.0f);
			gl.glVertex3f(10.0f, 0.0f, 0.0f);
		gl.glEnd();
		// eixo Y - Green
		gl.glColor3f(0.0f, 1.0f, 0.0f);
		gl.glBegin(GL.GL_LINES);
			gl.glVertex3f(0.0f, 0.0f, 0.0f);
			gl.glVertex3f(0.0f, 10.0f, 0.0f);
		gl.glEnd();
		// eixo Z - Blue
		gl.glColor3f(0.0f, 0.0f, 1.0f);
		gl.glBegin(GL.GL_LINES);
			gl.glVertex3f(0.0f, 0.0f, 0.0f);
			gl.glVertex3f(0.0f, 0.0f, 10.0f);
		gl.glEnd();
	}

	public void displayChanged(GLAutoDrawable arg0, boolean arg1, boolean arg2) {
	}

	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void Debug() {
	}

}
