package model;

import javax.media.opengl.GL;

import objmodel.object.OBJModel;

public class Cenario {

	private OBJModel model;
	private Transformacao4D matrizObjeto = new Transformacao4D();
	
	public Cenario(GL gl) {
		model = new OBJModel("data/cenario", 25, gl, true);
		rotacaoY(90);
	}
	
	public void draw(GL gl) {
		gl.glPushMatrix();
		gl.glMultMatrixd(matrizObjeto.obterDados(), 0);
		model.draw(gl);
		gl.glPopMatrix();
	}
	
	public void rotacaoY(double angulo) {
		Transformacao4D rotacaoY = new Transformacao4D();
		rotacaoY.atribuirRotacaoX(Transformacao4D.DEG_TO_RAD * angulo);
		matrizObjeto = matrizObjeto.transformMatrix(rotacaoY);
	}

	
}
