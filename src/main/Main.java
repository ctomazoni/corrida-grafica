package main;
/**
 * Objetivo: trabalha com conceitos da camera sintetica
 * https://www.opengl.org/sdk/docs/man2/xhtml/
 */

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

import javax.media.opengl.DebugGL;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;

import com.sun.opengl.util.GLUT;

import model.BoundingBox;
import model.Camera;
import model.Carro;
import model.Cenario;
import model.Volante;

public class Main implements GLEventListener, KeyListener {
	private GL gl;
	private GLU glu;
	private GLUT glut;
	private GLAutoDrawable glDrawable;

	private boolean eHMaterial = true;

	private Camera cameraTerceiraPessoa;
	private Camera cameraAtiva;

	private Cenario cenarioModel;
	private Carro carroModel;
	private List<Carro> carrosEstacionados;
	private Volante volanteModel;
	
	private BoundingBox bbEstacionamento1;
	private BoundingBox bbEstacionamento2;
	private BoundingBox bbEstacionamento3;

	private boolean isDesenharBatido;
	private boolean isDesenharOk;
	private boolean isDesenhandoColisao = false;
	
	public void init(GLAutoDrawable drawable) {
		glDrawable = drawable;
		gl = drawable.getGL();
		glu = new GLU();
		glut = new GLUT();
		glDrawable.setGL(new DebugGL(gl));
		
		gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
		
//		gl.glEnable(GL.GL_COLOR_MATERIAL);
		
		cameraTerceiraPessoa = new Camera(glu);
		cameraTerceiraPessoa.setxEye(15.f);
		cameraTerceiraPessoa.setyEye(20.f);
		cameraTerceiraPessoa.setzEye(18.f);
		cameraTerceiraPessoa.setxCenter(11.f);
		cameraTerceiraPessoa.setyCenter(1.f);
		cameraTerceiraPessoa.setzCenter(11.f);

		cameraAtiva = cameraTerceiraPessoa;

		volanteModel = new Volante(gl);
		cenarioModel = new Cenario(gl);
		carroModel = new Carro(glu, gl, true);
		
		bbEstacionamento1 = new BoundingBox(5, 0, 8, 6, 1, 10);
		bbEstacionamento2 = new BoundingBox(12.5, 0, 10, 14, 1, 12);
		bbEstacionamento3 = new BoundingBox(16.0, 0, 4.2, 17.5, 1, 6.5);
		
		criarCarrosEstacionados();

		ligarLuz();

		gl.glEnable(GL.GL_CULL_FACE);

		gl.glEnable(GL.GL_DEPTH_TEST);
	}
	
	private void criarCarrosEstacionados() {
		carrosEstacionados = new ArrayList<>();
		
		Carro carro1 = new Carro(glu,gl);
		Carro carro2 = new Carro(glu,gl);
		Carro carro3 = new Carro(glu,gl);
		Carro carro4 = new Carro(glu,gl);
		Carro carro5 = new Carro(glu,gl);

		carroModel.translacaoXYZ(1, 0, 3.7);
		carroModel.rotacaoY(90);
		
		carro1.translacaoXYZ(5.6, 0, 7);

		carro2.translacaoXYZ(5.6, 0, 11.3);

		carro3.translacaoXYZ(10.4, 0, 4.2);
		carro3.rotacaoY(180);
		
		carro4.translacaoXYZ(10.3, 0, 10.9);
		carro4.rotacaoY(180);
		
		carro5.translacaoXYZ(10.3, 0, 12.9);
		carro5.rotacaoY(180);

		carrosEstacionados.add(carro1);
		carrosEstacionados.add(carro2);
		carrosEstacionados.add(carro3);
		carrosEstacionados.add(carro4);
		carrosEstacionados.add(carro5);
		
	}

	public void display(GLAutoDrawable arg0) {
		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

		gl.glMatrixMode(GL.GL_MODELVIEW);
		gl.glLoadIdentity();
		cameraAtiva.lookAt();

		desenhaSRU3D();
		float[] white = { 1.0f, 1.0f, 1.0f, 1.0f };
		gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE, white, 0);
		gl.glEnable(GL.GL_LIGHTING);
		
		cenarioModel.draw(gl);
		carroModel.draw(gl);
		volanteModel.draw(gl);
		
		carrosEstacionados.forEach(f -> f.draw(gl));
		
		bbEstacionamento1.desenharOpenGLBBox(gl);
		bbEstacionamento2.desenharOpenGLBBox(gl);
		bbEstacionamento3.desenharOpenGLBBox(gl);

		gl.glDisable(GL.GL_LIGHTING);
		
		if (isDesenharBatido) {
			desenharCuboBatido();
		} 
		if (isDesenharOk) {
			desenharOk();
		}
		
		gl.glFlush();
	}

	private void desenharCuboBatido() {
		gl.glColor3f(1.0f, 0.0f, 0.0f);
		gl.glPushMatrix();
			gl.glTranslatef(0, 2, 0);
			glut.glutSolidCube(0.3f);
		gl.glPopMatrix();
		
		gl.glPushMatrix();
		gl.glTranslatef(0.3f,1.7f, 0);
		glut.glutSolidCube(0.3f);
		gl.glPopMatrix();

		gl.glPushMatrix();
		gl.glTranslatef(0.6f,1.4f, 0);
		glut.glutSolidCube(0.3f);
		gl.glPopMatrix();

		gl.glPushMatrix();
		gl.glTranslatef(0.9f,1.1f, 0);
		glut.glutSolidCube(0.3f);
		gl.glPopMatrix();

		gl.glPushMatrix();
		gl.glTranslatef(1.2f,0.8f, 0);
		glut.glutSolidCube(0.3f);
		gl.glPopMatrix();
		
		//
		gl.glPushMatrix();
		gl.glTranslatef(0.0f,0.8f, 0);
		glut.glutSolidCube(0.3f);
		gl.glPopMatrix();

		gl.glPushMatrix();
		gl.glTranslatef(0.3f, 1.1f, 0);
		glut.glutSolidCube(0.3f);
		gl.glPopMatrix();

		gl.glPushMatrix();
		gl.glTranslatef(0.6f, 1.4f, 0);
		glut.glutSolidCube(0.3f);
		gl.glPopMatrix();

		gl.glPushMatrix();
		gl.glTranslatef(0.9f, 1.7f, 0);
		glut.glutSolidCube(0.3f);
		gl.glPopMatrix();

		gl.glPushMatrix();
		gl.glTranslatef(1.2f, 2.0f, 0);
		glut.glutSolidCube(0.3f);
		gl.glPopMatrix();
		
	}

	private void desenharOk() {
		gl.glColor3f(0.0f, 1.0f, 0.0f);
		
		gl.glPushMatrix();
		gl.glTranslatef(0.4f,1.4f, 0);
		glut.glutSolidCube(0.3f);
		gl.glPopMatrix();
		
		gl.glPushMatrix();
		gl.glTranslatef(0.6f,1.1f, 0);
		glut.glutSolidCube(0.3f);
		gl.glPopMatrix();
		
		gl.glPushMatrix();
		gl.glTranslatef(0.8f,0.8f, 0);
		glut.glutSolidCube(0.3f);
		gl.glPopMatrix();

		gl.glPushMatrix();
		gl.glTranslatef(1.0f,0.5f, 0);
		glut.glutSolidCube(0.3f);
		gl.glPopMatrix();
		
		//
		gl.glPushMatrix();
		gl.glTranslatef(1.0f,0.8f, 0);
		glut.glutSolidCube(0.3f);
		gl.glPopMatrix();
		
		gl.glPushMatrix();
		gl.glTranslatef(1.1f, 1.0f, 0);
		glut.glutSolidCube(0.3f);
		gl.glPopMatrix();
		
		gl.glPushMatrix();
		gl.glTranslatef(1.2f, 1.2f, 0);
		glut.glutSolidCube(0.3f);
		gl.glPopMatrix();
		
		gl.glPushMatrix();
		gl.glTranslatef(1.3f, 1.4f, 0);
		glut.glutSolidCube(0.3f);
		gl.glPopMatrix();
		
		gl.glPushMatrix();
		gl.glTranslatef(1.4f, 1.6f, 0);
		glut.glutSolidCube(0.3f);
		gl.glPopMatrix();

		gl.glPushMatrix();
		gl.glTranslatef(1.5f, 1.8f, 0);
		glut.glutSolidCube(0.3f);
		gl.glPopMatrix();
		
		gl.glPushMatrix();
		gl.glTranslatef(1.6f, 2.0f, 0);
		glut.glutSolidCube(0.3f);
		gl.glPopMatrix();
		
		gl.glPushMatrix();
		gl.glTranslatef(1.8f, 2.2f, 0);
		glut.glutSolidCube(0.3f);
		gl.glPopMatrix();
		
	}

	public void keyPressed(KeyEvent e) {

		switch (e.getKeyCode()) {
		case KeyEvent.VK_ESCAPE:
			System.exit(0);
			break;
		case KeyEvent.VK_1:
			cameraAtiva = carroModel.getCamera();
			break;
		case KeyEvent.VK_2:
			cameraAtiva = cameraTerceiraPessoa;
			break;
		case KeyEvent.VK_RIGHT:
			carroModel.virarDireita();
			volanteModel.virarDireita();
			break;
		case KeyEvent.VK_LEFT:
			carroModel.virarEsquerda();
			volanteModel.virarEsquerda();
			break;
		case KeyEvent.VK_UP:
			carroModel.acelerar();
			break;
		case KeyEvent.VK_DOWN:
			carroModel.freiar();
			break;
		}
		
		verificarColisao();

		glDrawable.display();
	}

	private void verificarColisao() {
		if (isDesenhandoColisao) {
			return;
		}
		
		boolean isEstacionamento1 = bbEstacionamento1.isBoundingBoxDentro(carroModel.getBbCarro());
		boolean isEstacionamento2 = bbEstacionamento2.isBoundingBoxDentro(carroModel.getBbCarro());
		boolean isEstacionamento3 = bbEstacionamento3.isBoundingBoxDentro(carroModel.getBbCarro());
		boolean isBateuCarro = false;
		for (Carro carro : carrosEstacionados) {
			isBateuCarro |= carroModel.bateuNoCarro(carro);
		}
		
		if (isBateuCarro) {
			isDesenhandoColisao = true;
			new Thread(() -> {
				isDesenharBatido = true;
				for (int i = 0; i < 8; i++) {
					try {
						isDesenharBatido = !isDesenharBatido;
						glDrawable.display();
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				isDesenharBatido = false;
				glDrawable.display();
				isDesenhandoColisao = false;
			}).start(); 
			
		} else if (isEstacionamento1 || isEstacionamento2 || isEstacionamento3) {
			isDesenhandoColisao = true;
			new Thread(() -> {
				isDesenharOk = true;
				for (int i = 0; i < 8; i++) {
					try {
						isDesenharOk = !isDesenharOk;
						glDrawable.display();
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				isDesenharOk = false;
				glDrawable.display();
				isDesenhandoColisao = false;
			}).start(); 

		}
		
	}

	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		gl.glMatrixMode(GL.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glViewport(0, 0, width, height);

		// glu.gluOrtho2D(-30.0f, 30.0f, -30.0f, 30.0f);
		glu.gluPerspective(60, width / height, 0.1, 100); // projecao Perpectiva 1 pto fuga 3D
		// gl.glFrustum (-5.0, 5.0, -5.0, 5.0, 10, 100); // projecao Perpectiva 1 pto
		// fuga 3D
		// gl.glOrtho(-30.0f, 30.0f, -30.0f, 30.0f, -30.0f, 30.0f); // projecao
		// Ortogonal 3D

		// Debug();
	}

	public void desenhaSRU3D() {
		gl.glColor3f(1.0f, 0.0f, 0.0f);
		gl.glBegin(GL.GL_LINES);
		gl.glVertex3f(0.0f, 0.0f, 0.0f);
		gl.glVertex3f(20.0f, 0.0f, 0.0f);
		gl.glEnd();
		gl.glColor3f(0.0f, 1.0f, 0.0f);
		gl.glBegin(GL.GL_LINES);
		gl.glVertex3f(0.0f, 0.0f, 0.0f);
		gl.glVertex3f(0.0f, 20.0f, 0.0f);
		gl.glEnd();
		gl.glColor3f(0.0f, 0.0f, 1.0f);
		gl.glBegin(GL.GL_LINES);
		gl.glVertex3f(0.0f, 0.0f, 0.0f);
		gl.glVertex3f(0.0f, 0.0f, 20.0f);
		gl.glEnd();
	}

	private void ligarLuz() {
		if (eHMaterial) {
			float posLight[] = { 5.0f, 5.0f, 10.0f, 0.0f };
			gl.glLightfv(GL.GL_LIGHT0, GL.GL_POSITION, posLight, 0);
			gl.glEnable(GL.GL_LIGHT0);
		} else
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
