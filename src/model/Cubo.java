package model;

import javax.media.opengl.GL;

import com.sun.opengl.util.GLUT;

public class Cubo {

	private float escala;

	private GL gl;
	private GLUT glut;

	private float velocidade = 0.0f;
	
	private float[] cor = { 1.0f, 1.0f, 1.0f };
	
	private float tempoAnterior;
	private float posicaoAnterior;
	
	private float[] translacao = { 3.0f, 20.0f, 2.0f };

	public Cubo(float escala, GL gl, GLUT glut) {
		super();
		this.escala = escala;
		this.gl = gl;
		this.glut = glut;
		
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
			gl.glScalef(escala, escala, escala);
			gl.glTranslated(translacao[0], translacao[1], translacao[2]);
			glut.glutSolidCube(1.0f);
		gl.glPopMatrix();
	}

	public void aumentarVelocidade() {
		
	}

	public void diminuirVelocidade() {
		if (velocidade > 0)
			velocidade -= 0.04f;
		
	}

}
