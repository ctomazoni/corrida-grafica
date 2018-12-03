package model;

import javax.media.opengl.GL;

import objmodel.object.OBJModel;

public class Carro {

	private OBJModel model;
	private Transformacao4D matrizObjeto = new Transformacao4D();
	private float deslocamento = 0.1f;
	private float angulo;
	
	private BoundingBox bbCarro;
	
	public Carro(GL gl) {
		model = new OBJModel("data/porsche", 5f, gl, true);
		translacaoXYZ(0, 2, 0);
		
		atualizarBoundingBoxMatriz();	
	}

	private void atualizarBoundingBoxMatriz() {
		BoundingBox boundingBox = model.getBoundingBox();
		
		Point4D pmenor = new Point4D(boundingBox.obterMenorX(), boundingBox.obterMenorY(), boundingBox.obterMenorZ(), 1);
		Point4D pmaior = new Point4D(boundingBox.obterMaiorX(), boundingBox.obterMaiorY(), boundingBox.obterMaiorZ(), 1);
		
		pmenor = matrizObjeto.transformPoint(pmenor);
		pmaior = matrizObjeto.transformPoint(pmaior);
		
		bbCarro = new BoundingBox(pmenor.GetX(), pmenor.GetY(), pmenor.GetZ(), pmaior.GetX(), pmaior.GetY(), pmaior.GetZ());
	}

	public void draw(GL gl) {
//		model.getBoundingBox().desenharOpenGLBBox(gl);
		gl.glColor3f(1.0f, 0.0f, 0.0f);
		bbCarro.desenharOpenGLBBox(gl);
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
		atualizarBoundingBoxMatriz();
	}

	public void rotacaoY(double angulo) {
		Transformacao4D rotacaoY = new Transformacao4D();
		rotacaoY.atribuirRotacaoY(Transformacao4D.DEG_TO_RAD * angulo);
		matrizObjeto = matrizObjeto.transformMatrix(rotacaoY);
		atualizarBoundingBoxMatriz();
	}

	public boolean bateuNoCarro(Carro carro) {
		BoundingBox boundingBoxCarro = carro.model.getBoundingBox();
		return bbCarro.isBoundingBoxBatendo(boundingBoxCarro);
	}

}
