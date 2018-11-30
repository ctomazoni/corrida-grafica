package model;

import java.util.LinkedList;
import java.util.List;

import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;

public class Mundo {

	private List<ObjetoGrafico> objetosGraficos;
    private Point4D pontoMaisProximo;
    
	private Camera camera;
	private ObjetoGrafico poligonoSelecionado;
	
	private static Mundo instance;
	
	private GL gl;
	private GLU glu;
	
	private boolean isDesenhando = false;
    private boolean isPontoSelecionado = false;

	/**
	 * Retorna a inst�ncia do Mundo.
	 * @return a inst�ncia do Mundo.
	 */
	public static Mundo getInstance() {
		if (instance == null) {
			instance = new Mundo();
		}
		
		return instance;
	}
	
	/**
	 * Inicia o Mundo.
	 * @param gl objeto do tipo GL.
	 * @param glu objeto do tipo GLU.
	 */
	public void initMundo(GL gl, GLU glu) {
		this.gl = gl;
		this.glu = glu;
		objetosGraficos = new LinkedList<>();
		camera = new Camera(glu);
	}
	
	/**
	 * Lista os objetos gr�ficos.
	 */
	public void listarObjetosGraficos() {
		for (ObjetoGrafico objetoGrafico : objetosGraficos) {
			objetoGrafico.desenhar(gl, poligonoSelecionado);
		}
	}

	/**
	 * Altera o poligono selecionado no Mundo.
	 * @param objetoGrafico poligono selecionado.
	 */
	public void atualizarPoligonoSelecionado(ObjetoGrafico objetoGrafico) {
		this.poligonoSelecionado = objetoGrafico;
	}

	/**
	 * Retorna o poligono selecionado.
	 * @return o poligono selecionado.
	 */
	public ObjetoGrafico getPoligonoSelecionado() {
		return poligonoSelecionado;
	}

	/**
	 * Adiciona novo poligono no Mundo.
	 * @param objetoGrafico novo poligono.
	 */
	public void adicionarObjetoGrafico(ObjetoGrafico objetoGrafico) {
		this.objetosGraficos.add(objetoGrafico);
	}

	/**
	 * Retorna o poligono clicado.<br>
	 * Se o clique n�o atingir nenhum poligono, retorna null.
	 * @param x posi��o x do mouse no Mundo.
	 * @param y posi��o y do mouse no Mundo.
	 * @return o poligono clicado.
	 */
	public ObjetoGrafico getObjetoClicado(int x, int y) {
		for (ObjetoGrafico objetoGrafico : objetosGraficos) {
			
			ObjetoGrafico selecionado = objetoGrafico.isSelecionado(x, y);
			if (selecionado != null) {
				return selecionado;
			}
			
		}
		return null;
	}

	/**
	 * Informa que o programa entrou em modo desenho.
	 */
	public void marcarDesenhando() {
		this.isDesenhando = true;
	}

	/**
	 * Informa que o programa saiu do modo desenho.
	 */
	public void desmarcarDesenhando() {
		this.isDesenhando = false;
	}
	
	/**
	 * Verifica se o usu�rio est� desenhando.
	 * @return se o programa est� em modo desenho.
	 */
	public boolean isDesenhando() {
		return isDesenhando;
	}
        
    /**
     * Informa o v�rtice que foi selecionado.
     * @param ponto v�rtice selecionado.
     */
    public void marcarPontoSelecionado(Point4D ponto) {
        pontoMaisProximo = ponto;
		this.isPontoSelecionado = true;
	}

	/**
	 * Informa que n�o h� nenhum v�rtice selecionado.
	 */
	public void desmarcarPontoSelecionado() {
		pontoMaisProximo = null;
		this.isPontoSelecionado = false;
	}
	
	/**
	 * Verifica se h� algum v�rtice selecionado no Mundo.
	 * @return se existe algum v�rtice selecionado.
	 */
	public boolean isPontoSelecionado() {
		return isPontoSelecionado;
	}
	
    /**
     * Retorna todos os poligonos do Mundo.
     * @return todos os poligonos do Mundo.
     */
    public List<ObjetoGrafico> getObjetosGraficos() {
        return objetosGraficos;
    }

    /**
     * Seta os novos poligonos do Mundo.
     * @param objetosGraficos novos poligonos do Mundo.
     */
    public void setObjetosGraficos(List<ObjetoGrafico> objetosGraficos) {
        this.objetosGraficos = objetosGraficos;
    } 
    
    /**
     * Retorna o v�rtice mais pr�ximo.
     * @return o v�rtice mais pr�ximo.
     */
    public Point4D getPontoMaisProximo() {
        return pontoMaisProximo;
    }
    
    /**
     * Calcula e retorna a dist�ncia entre dois v�rtices.
     * @param x1 valor x do primeiro v�rtice.
     * @param x2 valor x do segundo v�rtice.
     * @param y1 valor y do primeiro v�rtice.
     * @param y2 valor y do segundo v�rtice.
     * @return a dist�ncia entre dois v�rtices.
     */
    public double calcularDistanciaEntrePontos(double x1, double x2, double y1, double y2) {
        return Math.pow((x2 - x1),2) + Math.pow((y2 - y1),2);
    }
    
}
