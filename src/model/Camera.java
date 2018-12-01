package model;

import javax.media.opengl.glu.GLU;

public class Camera {

	private float xEye, yEye, zEye, xCenter, yCenter, zCenter;

	private GLU glu;

	public Camera(GLU glu) {
		this.glu = glu;
	}

	public void lookAt() {
		glu.gluLookAt(xEye, yEye, zEye, xCenter, yCenter, zCenter, 0.0f, 1.0f, 0.0f);
	}

	public float getxEye() {
		return xEye;
	}

	public void setxEye(float xEye) {
		this.xEye = xEye;
	}

	public float getyEye() {
		return yEye;
	}

	public void setyEye(float yEye) {
		this.yEye = yEye;
	}

	public float getzEye() {
		return zEye;
	}

	public void setzEye(float zEye) {
		this.zEye = zEye;
	}

	public float getxCenter() {
		return xCenter;
	}

	public void setxCenter(float xCenter) {
		this.xCenter = xCenter;
	}

	public float getyCenter() {
		return yCenter;
	}

	public void setyCenter(float yCenter) {
		this.yCenter = yCenter;
	}

	public float getzCenter() {
		return zCenter;
	}

	public void setzCenter(float zCenter) {
		this.zCenter = zCenter;
	}

}
