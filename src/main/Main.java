package main;
/**
 * Objetivo: trabalha com conceitos da camera sintetica
 * https://www.opengl.org/sdk/docs/man2/xhtml/
 */

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.media.opengl.DebugGL;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;
//import javax.media.opengl.glu.GLUquadric;
import javax.swing.JOptionPane;

import com.sun.opengl.util.GLUT;
import com.sun.opengl.util.texture.TextureData;

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

	private Camera cameraPrimeiraPessoa;
	private Camera cameraAtiva;

	private Cenario cenarioModel;
	private Carro carroModel;
	private List<Carro> carrosEstacionados;
	private Volante volanteModel;

	public void init(GLAutoDrawable drawable) {
		glDrawable = drawable;
		gl = drawable.getGL();
		glu = new GLU();
		glut = new GLUT();
		glDrawable.setGL(new DebugGL(gl));
		
		gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
		
//		gl.glEnable(GL.GL_COLOR_MATERIAL);
		
		cameraPrimeiraPessoa = new Camera(glu);
		cameraPrimeiraPessoa.setxEye(1.f);
		cameraPrimeiraPessoa.setyEye(30.f);
		cameraPrimeiraPessoa.setzEye(1.f);
		cameraPrimeiraPessoa.setxCenter(0.f);
		cameraPrimeiraPessoa.setyCenter(0.f);
		cameraPrimeiraPessoa.setzCenter(0.f);

		cameraAtiva = cameraPrimeiraPessoa;

		volanteModel = new Volante(gl);
		cenarioModel = new Cenario(gl);
		carroModel = new Carro(gl);
		
		criarCarrosEstacionados();

		ligarLuz();

		gl.glEnable(GL.GL_CULL_FACE);

		gl.glEnable(GL.GL_DEPTH_TEST);
	}
	
	private void criarCarrosEstacionados() {
		carrosEstacionados = new ArrayList<>();
		
		Carro carro1 = new Carro(gl);
		Carro carro2 = new Carro(gl);
		Carro carro3 = new Carro(gl);
		Carro carro4 = new Carro(gl);
		Carro carro5 = new Carro(gl);
		Carro carro6 = new Carro(gl);

		carroModel.translacaoXYZ(-10, 0, 7);
		carroModel.rotacaoY(90);
		
		// do lado do inicio
		carro1.translacaoXYZ(-10, 0, 5);
		carro1.rotacaoY(90);

		// primeiro da rua lado direito
		carro2.translacaoXYZ(-1, 0, 7);
		carro2.rotacaoY(180);

		// segundo da rua lado direito
		carro3.translacaoXYZ(-1, 0, 4.5);
		carro3.rotacaoY(180);
		
		// terceiro da rua lado esquerdo
		carro4.translacaoXYZ(-6, 0, -0.5);

		// quarto da rua lado esquerdo
		carro5.translacaoXYZ(-6, 0, -6.5);

		// quinto da rua lado direito
		carro6.translacaoXYZ(-1, 0, -6.5);
		carro6.rotacaoY(180);
		
		carrosEstacionados.add(carro1);
		carrosEstacionados.add(carro2);
		carrosEstacionados.add(carro3);
		carrosEstacionados.add(carro4);
		carrosEstacionados.add(carro5);
		carrosEstacionados.add(carro6);
		
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
		
		gl.glDisable(GL.GL_LIGHTING);

		gl.glFlush();
	}


	public void keyPressed(KeyEvent e) {

		switch (e.getKeyCode()) {
		case KeyEvent.VK_ESCAPE:
			System.exit(0);
			break;
		case KeyEvent.VK_D:
			// objetos[0].debug();
			break;
		case KeyEvent.VK_R:
			// objetos[0].atribuirIdentidade();
			break;
		case KeyEvent.VK_SPACE:
			// TODO: ERRO ao reinicar a thread
			// manual = !manual;
			// if (manual) animacao.stop();
			// else animacao.start();
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
		carrosEstacionados.forEach(carro -> {
			if (carroModel.bateuNoCarro(carro)) {
				System.out.println("bateu");
			}
		});
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
