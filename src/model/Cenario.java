package model;

import javax.media.opengl.GL;

import objmodel.object.OBJModel;

public class Cenario {

	private OBJModel model;
	private Transformacao4D matrizObjeto = new Transformacao4D();
	
	public Cenario(GL gl) {
		model = new OBJModel("data/scene", 1.5f, gl, true);
	}
	
	public void draw(GL gl) {
		gl.glPushMatrix();
		gl.glMultMatrixd(matrizObjeto.obterDados(), 0);
		model.draw(gl);
		gl.glPopMatrix();
	}
	
}
