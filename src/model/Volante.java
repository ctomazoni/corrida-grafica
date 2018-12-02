package model;

import javax.media.opengl.GL;

import objmodel.object.OBJModel;

public class Volante {
	
	private OBJModel model;
	private Transformacao4D matrizObjeto = new Transformacao4D();
	
	public Volante(GL gl) {
		model = new OBJModel("data/volante", 1.5f, gl, true);
		matrizObjeto.atribuirRotacaoX(10);
		matrizObjeto.atribuirTranslacao(4, 0, 0);
	}
	
	public void draw(GL gl) {
		gl.glPushMatrix();
		gl.glMultMatrixd(matrizObjeto.obterDados(), 0);
			model.draw(gl);
		gl.glPopMatrix();
	}
	
}
