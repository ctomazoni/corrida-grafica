package model;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;
import javax.media.opengl.GL;
import javax.swing.JOptionPane;

import com.sun.opengl.util.texture.TextureData;

public class Cenario {

	private int[] idTexture;
	private ByteBuffer buffer[];	
	private BufferedImage image;
	private int width, height;
	private TextureData td;
	private Transformacao4D matrizObjeto = new Transformacao4D();
	
	public Cenario(GL gl) {
		gl.glColorMaterial(GL.GL_FRONT_AND_BACK, GL.GL_AMBIENT_AND_DIFFUSE);	
		
		// Habilita o modelo de colorizacao de Gouraud
		gl.glShadeModel(GL.GL_SMOOTH);
		
		//===== Comandos de inicializacao para textura
		idTexture = new int[3]; // lista de identificadores de textura
		gl.glGenTextures(1, idTexture, 2); 		// Gera identificador de textura
		// Define os filtros de magnificacao e minificacao 
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MIN_FILTER,GL.GL_LINEAR);	
		gl.glTexParameteri(GL.GL_TEXTURE_2D,GL.GL_TEXTURE_MAG_FILTER,GL.GL_LINEAR);
		buffer = new ByteBuffer [2]; // buffer da imagem
		loadImage(0,"data/cenario.jpg"); // carrega as texturas		
	}
	
	public void draw(GL gl) {
		gl.glPushMatrix();
		// Desenha um cubo no qual a textura e aplicada
			gl.glTranslatef(0.0f, 0.0f, 0.0f);
			gl.glEnable(GL.GL_TEXTURE_2D);	// Primeiro habilita uso de textura
			gl.glBindTexture(GL.GL_TEXTURE_2D, idTexture[0]); 		// Especifica qual e a textura corrente pelo identificador 
			gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, 3, width, height, 0, GL.GL_BGR,GL.GL_UNSIGNED_BYTE, buffer[0]); 		// Envio da textura para OpenGL
				desenhaCubo(gl);
			gl.glDisable(GL.GL_TEXTURE_2D);	//	Desabilita uso de textura
		gl.glPopMatrix();
	}
	
	public void desenhaCubo(GL gl) {
		gl.glPushMatrix();
		gl.glTranslatef(10.0f, 0.0f, 7.0f);
		gl.glScalef(10.0f, 0.0f, 7.0f);
		gl.glColor3f(1.0f, 1.0f, 1.0f);
		gl.glBegin (GL.GL_QUADS );
			// Especifica a coordenada de textura para cada vertice
			// Face frontal
			gl.glNormal3f(0.0f,0.0f,-1.0f);
			gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-1.0f, -1.0f,  1.0f);
			gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f( 1.0f, -1.0f,  1.0f);
			gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f( 1.0f,  1.0f,  1.0f);
			gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-1.0f,  1.0f,  1.0f);				
			// Face posterior
			gl.glNormal3f(0.0f,0.0f,1.0f);
			gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(-1.0f, -1.0f, -1.0f);
			gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(-1.0f,  1.0f, -1.0f);
			gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f( 1.0f,  1.0f, -1.0f);
			gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f( 1.0f, -1.0f, -1.0f);
			// Face superior
			gl.glNormal3f(0.0f,1.0f,0.0f);
			gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-1.0f,  1.0f, -1.0f);
			gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-1.0f,  1.0f,  1.0f);
			gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f( 1.0f,  1.0f,  1.0f);
			gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f( 1.0f,  1.0f, -1.0f);
			// Face inferior
			gl.glNormal3f(0.0f,-1.0f,0.0f);
			gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(-1.0f, -1.0f, -1.0f);
			gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f( 1.0f, -1.0f, -1.0f);
			gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f( 1.0f, -1.0f,  1.0f);
			gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(-1.0f, -1.0f,  1.0f);
			// Face lateral direita
			gl.glNormal3f(1.0f,0.0f,0.0f);
			gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f( 1.0f, -1.0f, -1.0f);
			gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f( 1.0f,  1.0f, -1.0f);
			gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f( 1.0f,  1.0f,  1.0f);
			gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f( 1.0f, -1.0f,  1.0f);
			// Face lateral esquerda
			gl.glNormal3f(-1.0f,0.0f,0.0f);
			gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-1.0f, -1.0f, -1.0f);
			gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(-1.0f, -1.0f,  1.0f);
			gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(-1.0f,  1.0f,  1.0f);
			gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-1.0f,  1.0f, -1.0f);
		gl.glEnd();
	gl.glPopMatrix();
		
	}
	
	public void rotacaoY(double angulo) {
		Transformacao4D rotacaoY = new Transformacao4D();
		rotacaoY.atribuirRotacaoX(Transformacao4D.DEG_TO_RAD * angulo);
		matrizObjeto = matrizObjeto.transformMatrix(rotacaoY);
	}

	public void loadImage(int ind, String fileName)	{
		// Tenta carregar o arquivo		
		image = null;
		try {
			image = ImageIO.read(new File(fileName));
		}
		catch (IOException e) {
			JOptionPane.showMessageDialog(null,"Erro na leitura do arquivo "+fileName);
		}

		width  = image.getWidth();
		height = image.getHeight();
		// Gera uma nova TextureData...
		td = new TextureData(0,0,false,image);
		// ...e obtem um ByteBuffer a partir dela
		buffer[ind] = (ByteBuffer) td.getBuffer();
	}
	
}
