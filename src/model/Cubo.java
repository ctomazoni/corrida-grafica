package model;

import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;

import com.sun.opengl.util.GLUT;

public class Cubo {

	private GL gl;
	private GLUT glut;

	private float velocidade = 0.0f;
	
	private float[] cor = { 1.0f, 1.0f, 1.0f };
	
	private float tempoAnterior;
	private float posicaoAnterior;
	private float xVeiculo = 1.0f;
	private float zVeiculo = 2.0f;
	
	private float translacao[] = { xVeiculo, 5.0f, zVeiculo };
	private float escalaVeiculo[] = { 3.0f, 3.0f, 3.0f };
	private float rotacao[] = { 0.0f, 0.0f, 0.0f };
	
	private Camera camera;
	
	public Cubo(GL gl, GLU glu, GLUT glut) {
		super();
		this.gl = gl;
		this.glut = glut;
		camera = new Camera(glu);
		camera.setxCenter(0.f);
		camera.setyCenter(0.f);
		camera.setzCenter(0.f);
		
		Thread desacelaracao = new Thread(() -> {
			while (true) {
				if (velocidade > 0) {
					velocidade -= 0.01f;
				}
				try {
					Thread.sleep(600);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		desacelaracao.start();
	}

	public void desenhar() {
		gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE, cor, 0);
			gl.glEnable(GL.GL_LIGHTING);
			gl.glPushMatrix();
			gl.glScalef(escalaVeiculo[0], escalaVeiculo[1], escalaVeiculo[2]);
			gl.glRotatef(rotacao[0], rotacao[1], rotacao[2], 0.0f);
			gl.glTranslated(xVeiculo, translacao[1], zVeiculo);
			
			glut.glutSolidCube(1.0f);
		gl.glPopMatrix();
	}

	public void aumentarVelocidade() {
		
	}

	public void diminuirVelocidade() {
		if (velocidade > 0)
			velocidade -= 0.04f;
		
	}
	
	public void moverVeiculoDireita() {
		xVeiculo++;
		atualizarCamera();
	}
	
	public void moverVeiculoEsquerda() {
		xVeiculo--;
		atualizarCamera();
	}
	
	public void acelerar() {
		zVeiculo--;
		atualizarCamera();
	}
	
	public void moverVeiculoRe() {
		zVeiculo++;
		atualizarCamera();
	}

	public Camera getCamera() {
		atualizarCamera();
		return camera;
	}

	private void atualizarCamera() {
		camera.setxEye(xVeiculo);
		camera.setyEye(25.0f);
		camera.setzEye(zVeiculo+10);
	}
	
}
