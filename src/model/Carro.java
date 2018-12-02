package model;

import javax.media.opengl.GL;

import objmodel.object.OBJModel;

public class Carro {

	private OBJModel model;
	private Transformacao4D matrizObjeto = new Transformacao4D();
	private float deslocamento = 0.1f;
	private float angulo;

	public Carro(GL gl) {
		model = new OBJModel("data/porsche", 2.0f, gl, true);
		translacaoXYZ(0, 2, 0);
		
	}

	public void draw(GL gl) {
		gl.glPushMatrix();
		gl.glMultMatrixd(matrizObjeto.obterDados(), 0);
		model.draw(gl);
		gl.glPopMatrix();
	}

	public void virarEsquerda() {
		if (angulo <= 8) {
			angulo += 0.2;
			System.out.println(angulo);
		}
	}

	public void virarDireita() {
		if (angulo >= -8) {
			angulo -= 0.2;
			System.out.println(angulo);
		}
	}

	public void acelerar() {
		rotacaoY(angulo);
		translacaoXYZ(0.0, 0.0, deslocamento);
	}

	public void freiar() {
		rotacaoY(-angulo);
		translacaoXYZ(0.0, 0.0, -deslocamento);
	}

	public void atribuirIdentidade() {
		angulo = 0.0f;
		matrizObjeto.atribuirIdentidade();
	}

	public void translacaoXYZ(double tx, double ty, double tz) {
		Transformacao4D matrizTranslate = new Transformacao4D();
		matrizTranslate.atribuirTranslacao(tx, ty, tz);
		matrizObjeto = matrizObjeto.transformMatrix(matrizTranslate);
	}

	public void rotacaoY(double angulo) {
		Transformacao4D rotacaoY = new Transformacao4D();
		rotacaoY.atribuirRotacaoY(Transformacao4D.DEG_TO_RAD * angulo);
		matrizObjeto = matrizObjeto.transformMatrix(rotacaoY);
	}

}
