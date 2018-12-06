package model;

import javax.media.opengl.GL;

import objmodel.object.OBJModel;

public class Volante {

	private OBJModel model;
	private Transformacao4D matrizObjeto = new Transformacao4D();
	private float angulo;

	public Volante(GL gl) {
		model = new OBJModel("data/volante2", 1.5f, gl, true);
		
		translacaoXYZ(12, 12, 19);
		rotacaoY(30);
	}

	public void draw(GL gl) {
		gl.glPushMatrix();
		gl.glMultMatrixd(matrizObjeto.GetDados(), 0);
		model.draw(gl);
		gl.glPopMatrix();
	}

	public void virarEsquerda() {
		if (angulo <= 650) {
			angulo += 15;
			rotacaoY(15);
		}
	}

	public void virarDireita() {
		if (angulo >= -650) {
			angulo -= 15;
			rotacaoY(-15);
		}
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
