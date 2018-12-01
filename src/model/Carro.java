package model;

import javax.media.opengl.GL;

import objmodel.object.OBJModel;

public class Carro {

	private OBJModel model;
	private Transformacao4D matrizObjeto = new Transformacao4D();
	private double deslocamento = 0.5;
	private double angulo;
	
	public Carro(GL gl) {
		model = new OBJModel("data/porsche", 1.5f, gl, true);
	}

	public void draw(GL gl) {
		gl.glPushMatrix();
		gl.glMultMatrixd(matrizObjeto.obterDados(), 0);
			model.draw(gl);
		gl.glPopMatrix();
	}
	
	public void virarEsquerda() {
		angulo += 2.0;
		System.out.println(angulo);
	}

	public void virarDireita() {
		angulo -= 2.0;
		System.out.println(angulo);
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
		angulo = 0.0;
		matrizObjeto.atribuirIdentidade();
	}
	
	public void translacaoXYZ(double tx, double ty, double tz) {
		Transformacao4D matrizTranslate = new Transformacao4D();
		matrizTranslate.atribuirTranslacao(tx,ty,tz);
		matrizObjeto = matrizObjeto.transformMatrix(matrizTranslate);		
	}
	
	public void rotacaoY(double angulo) {
		Transformacao4D rotacaoY = new Transformacao4D();
		rotacaoY.atribuirRotacaoY(Transformacao4D.DEG_TO_RAD * angulo);
		matrizObjeto = matrizObjeto.transformMatrix(rotacaoY);		
	}
	
}
