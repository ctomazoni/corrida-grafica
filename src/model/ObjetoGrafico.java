package model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import javax.media.opengl.GL;

public class ObjetoGrafico {

	private LinkedList<Point4D> pontos = new LinkedList<>();
	private int primitiva = GL.GL_LINE_STRIP;
	private Cor cor = new Cor();
	private BoundingBox boundingBox;

	private Transformacao4D transformacao4D = new Transformacao4D();
	private Transformacao4D matrizTmpTranslacao = new Transformacao4D();
	private Transformacao4D matrizTmpEscala = new Transformacao4D();
	private Transformacao4D matrizTmpTranslacaoInversa = new Transformacao4D();
	private Transformacao4D matrizGlobal = new Transformacao4D();
	
	private List<ObjetoGrafico> objetosFilhos = new ArrayList<>();

	/**
	 * Atualiza a posi��o da Bounding Box.
	 */
	public void atualizarBoundingBox() {
		boolean isFirst = true;
		for (Point4D point4d : pontos) {
			if (isFirst) {
				boundingBox = new BoundingBox(point4d.GetX(), point4d.GetY(), 0, point4d.GetX(), point4d.GetY(), 0);
				isFirst = false;
			} else {
				boundingBox.atualizarBBox(point4d);
			}
		}
	}

	/**
	 * Setar os v�rtices do poligono.
	 * @param pontos lista que armazena as posi��es dos v�rtices do poligono no plano.
	 */
	public void setPontos(LinkedList<Point4D> pontos) {
		this.pontos = pontos;
	}

	/**
	 * Retorna a primitiva do poligono.
	 * @return a primitiva atual do poligono.
	 */
	public int getPrimitiva() {
		return primitiva;
	}

	/**
	 * Altera a primitiva do poligono.
	 * @param primitiva nova primitiva do poligono.
	 */
	public void setPrimitiva(int primitiva) {
		this.primitiva = primitiva;
	}

	/**
	 * Retorna a cor do poligono.
	 * @return a cor atual do poligono em um objeto que armazena os valores de satura��o usando o padr�o RGB.
	 */
	public Cor getCor() {
		return cor;
	}

	/**
	 * Seta a nova cor do poligono.
	 * @param cor objeto que armazena os valores de satura��o usando o padr�o RGB.
	 */
	public void setCor(Cor cor) {
		this.cor = cor;
	}

	/**
	 * Desenha o poligono.
	 * @param gl objeto do tipo GL.
	 * @param selecionado poligono selecionado.
	 */
	public void desenhar(GL gl, ObjetoGrafico selecionado) {
		gl.glColor3f(getCor().getRed(), getCor().getGreen(), getCor().getBlue());

		gl.glPushMatrix();
			gl.glMultMatrixd(transformacao4D.GetDate(), 0);

			gl.glBegin(primitiva);
			for (Point4D point4d : pontos) {
				gl.glVertex2d(point4d.GetX(), point4d.GetY());
			}
	
			gl.glEnd();

			if (selecionado == this) {
				this.boundingBox.desenharOpenGLBBox(gl);
			}
		
			objetosFilhos.forEach(f -> f.desenhar(gl, selecionado));
		gl.glPopMatrix();
		
	}

	/**
	 * Desenha a Bounding Box.
	 * @param gl objeto do tipo GL.
	 */
	public void desenharBoundingBox(GL gl) {
		boundingBox.desenharOpenGLBBox(gl);
	}

	/**
	 * Retorna a Bounding Box.
	 * @return a Bounding Box.
	 */
	public BoundingBox getBoundingBox() {
		return boundingBox;
	}
	
	/**
	 * Retorna os filhos de um poligono.
	 * @return os filhos de um poligono.
	 */
	public List<ObjetoGrafico> getObjetosFilhos() {
		return objetosFilhos;
	}
	
	/**
	 * Retorna se o clique selecinou algum poligono.
	 * @param xClique posi��o x do mouse.
	 * @param yClique posi��o y do mouse.
	 * @return se algum poligono foi selecionado.
	 */
	public ObjetoGrafico isSelecionado(int xClique, int yClique) {
		if (boundingBox.isPonto2DDentro(xClique, yClique)) {

			int paridade = 0;
			for (int i = 0; i < pontos.size(); i++) {
				if (i + 1 < pontos.size()) {
					if (isPontoIntersecciona(xClique, yClique, pontos.get(i), pontos.get(i + 1))) {
						paridade++;
					}
				}
			}

			if (isPontoIntersecciona(xClique, yClique, pontos.getLast(), pontos.getFirst())) {
				paridade++;
			}

			if (paridade % 2 == 1) return this;

		}
		
		Optional<ObjetoGrafico> encontrado = objetosFilhos.stream().filter(f -> f.isSelecionado(xClique, yClique) != null).findFirst();
		return encontrado.isPresent() ? encontrado.get() : null;
	}

	/**
	 * Verifica se local do clique intersecciona algum poligono.
	 * @param xClique posi��o x do mouse.
	 * @param yClique posi��o y do mouse
	 * @param ponto1 primeiro v�rtice para compara��o.
	 * @param ponto2 segundo v�rtice para compara��o.
	 * @return se algum poligono foi interseccionado.
	 */
	private boolean isPontoIntersecciona(int xClique, int yClique, Point4D ponto1, Point4D ponto2) {
		double ti = (yClique - ponto1.GetY()) / (ponto2.GetY() - ponto1.GetY());
		if (ti >= 0 && ti <= 1) {
			double x = ponto1.GetX() + (ponto2.GetX() - ponto1.GetX()) * ti;
			return x > xClique;
		}
		return false;
	}

	/**
	 * Translada o poligono.
	 * @param x posi��o x para transla��o.
	 * @param y posi��o y para transla��o.
	 */
	public void transladar(double x, double y) {
		Transformacao4D matrizTranslate = new Transformacao4D();
		matrizTranslate.atribuirTranslacao(x, y, 0.0d);
		transformacao4D = matrizTranslate.transformMatrix(transformacao4D);
	}

	/**
	 * Escala o poligono.
	 * @param escala valor da escala.
	 */
	public void escalar(double escala) {
		Point4D ptoFixo = getCentro();
		matrizGlobal.atribuirIdentidade();

		matrizTmpTranslacao.atribuirTranslacao(ptoFixo.GetX(), ptoFixo.GetY(), ptoFixo.GetZ());
		matrizGlobal = matrizTmpTranslacao.transformMatrix(matrizGlobal);

		matrizTmpEscala.atribuirEscala(escala, escala, 1.0);
		matrizGlobal = matrizTmpEscala.transformMatrix(matrizGlobal);

		ptoFixo.inverterSinal(ptoFixo);
		matrizTmpTranslacaoInversa.atribuirTranslacao(ptoFixo.GetX(), ptoFixo.GetY(), ptoFixo.GetZ());
		matrizGlobal = matrizTmpTranslacaoInversa.transformMatrix(matrizGlobal);

		transformacao4D = transformacao4D.transformMatrix(matrizGlobal);
	}

	/**
	 * Rotaciona o poligono.
	 * @param angulo �ngulo para rota��o.
	 */
	public void rotacionar(double angulo) {
		Point4D ptoFixo = getCentro();
		matrizGlobal.atribuirIdentidade();

		matrizTmpTranslacao.atribuirTranslacao(ptoFixo.GetX(), ptoFixo.GetY(), ptoFixo.GetZ());
		matrizGlobal = matrizTmpTranslacao.transformMatrix(matrizGlobal);

		matrizTmpEscala.atribuirRotacaoZ(Transformacao4D.DEG_TO_RAD * angulo);
		matrizGlobal = matrizTmpEscala.transformMatrix(matrizGlobal);

		ptoFixo.inverterSinal(ptoFixo);
		matrizTmpTranslacaoInversa.atribuirTranslacao(ptoFixo.GetX(), ptoFixo.GetY(), ptoFixo.GetZ());
		matrizGlobal = matrizTmpTranslacaoInversa.transformMatrix(matrizGlobal);

		transformacao4D = transformacao4D.transformMatrix(matrizGlobal);
	}

	/**
	 * Calcula e retorna a posi��o do centro do poligono.
	 * @return a posi��o do centro do poligono.
	 */
	public Point4D getCentro() {
		double maiorX = pontos.stream().mapToDouble(Point4D::GetX).max().getAsDouble();
		double menorX = pontos.stream().mapToDouble(Point4D::GetX).min().getAsDouble();
		double maiorY = pontos.stream().mapToDouble(Point4D::GetY).max().getAsDouble();
		double menorY = pontos.stream().mapToDouble(Point4D::GetY).min().getAsDouble();
		return new Point4D(((maiorX + menorX) / 2) * -1, ((maiorY + menorY) / 2) * -1);
	}

}
