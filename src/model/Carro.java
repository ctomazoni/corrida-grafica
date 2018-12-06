package model;

import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;

import objmodel.object.OBJModel;
import objmodel.object.Tuple3;

public class Carro {

	private OBJModel model;
	private Transformacao4D matrizObjeto = new Transformacao4D();
	private float deslocamento = 0.1f;
	private float angulo;
	private boolean isAtualizarBBox;
	private Camera camera;
	private Volante volante;

	private BoundingBox bbCarro;
	private float zCamera;
	private float xCamera;

	public Carro(GLU glu, GL gl) {
		this(glu, gl, false);
	}

	public Carro(GLU glu, GL gl, boolean isAtualizarBBox) {
		this.isAtualizarBBox = isAtualizarBBox;
		this.camera = new Camera(glu);
		model = new OBJModel("data/porsche", 1.8f, gl, true);
		translacaoXYZ(0, 0.3, 0);

		atualizarBoundingBoxMatriz();
	}

	private void atualizarBoundingBoxMatriz() {
		if (isAtualizarBBox) {
			atualizarPorTodosVertices();
		} else {
			atualizarSimples();
		}
	}

	private void atualizarPorTodosVertices() {
		boolean isFirst = true;
		for (Tuple3 vert : model.getVerts()) {
			Point4D ponto = matrizObjeto.transformPoint(new Point4D(vert.getX(), vert.getY(), vert.getZ(), 1));
			if (isFirst) {
				isFirst = false;
				bbCarro = new BoundingBox(ponto.GetX(), ponto.GetY(), ponto.GetZ(), ponto.GetX(), ponto.GetY(),
						ponto.GetZ());
			} else {
				bbCarro.atualizarBBox(ponto);
			}
		}
	}

	private void atualizarSimples() {
		BoundingBox boundingBox = model.getBoundingBox();

		Point4D pmenor = new Point4D(boundingBox.obterMenorX(), boundingBox.obterMenorY(), boundingBox.obterMenorZ(),
				1);
		Point4D pmaior = new Point4D(boundingBox.obterMaiorX(), boundingBox.obterMaiorY(), boundingBox.obterMaiorZ(),
				1);

		pmenor = matrizObjeto.transformPoint(pmenor);
		pmaior = matrizObjeto.transformPoint(pmaior);

		bbCarro = new BoundingBox(pmenor.GetX(), pmenor.GetY(), pmenor.GetZ(), pmaior.GetX(), pmaior.GetY(),
				pmaior.GetZ());
	}

	public void draw(GL gl) {
		// model.getBoundingBox().desenharOpenGLBBox(gl);
		gl.glColor3f(1.0f, 0.0f, 0.0f);
		// bbCarro.desenharOpenGLBBox(gl);
		gl.glPushMatrix();
		gl.glMultMatrixd(matrizObjeto.GetDados(), 0);
		model.draw(gl);
		gl.glPopMatrix();
	}

	public void virarEsquerda() {
		if (angulo <= 4) {
			angulo += 0.18;
		}
	}

	public void virarDireita() {
		if (angulo >= -4) {
			angulo -= 0.18;
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
		atualizarCamera();
	}

	public void rotacaoY(double angulo) {
		Transformacao4D rotacaoY = new Transformacao4D();
		rotacaoY.atribuirRotacaoY(Transformacao4D.DEG_TO_RAD * angulo);
		matrizObjeto = matrizObjeto.transformMatrix(rotacaoY);
		atualizarBoundingBoxMatriz();
		atualizarCamera();
	}

	public boolean bateuNoCarro(Carro carro) {
		BoundingBox boundingBoxCarro = carro.getBbCarro();
		return bbCarro.isBoundingBoxBatendo(boundingBoxCarro);
	}

	public BoundingBox getBbCarro() {
		return bbCarro;
	}

	public Camera getCamera() {
		atualizarCamera();
		return camera;
	}

	private void atualizarCamera() {
		Point4D ponto = new Point4D();
		ponto = matrizObjeto.transformPoint(ponto);
		camera.setxEye(new Float(ponto.GetX()));
		camera.setyEye(new Float(ponto.GetY()+0.4));
		camera.setzEye(new Float(ponto.GetZ()));
		camera.setxCenter(new Float(ponto.GetX() + 3));
		camera.setyCenter(new Float(ponto.GetY()));
		camera.setzCenter(new Float(ponto.GetZ() + 0.2));
	}

}
