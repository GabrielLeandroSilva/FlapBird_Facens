package com.myflapbird.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.Random;

public class jogo extends ApplicationAdapter {

	//Variaveis para obter as texturas do game
	private SpriteBatch batch;
	private Texture[] passaros;
	private Texture fundo;
	private Texture canoTopo;
	private Texture canoBaixo;

	//Movimentação
	private int movimentaY = 0;
	private int movimentaX = 0;

	private int pontos = 0;

	//Variaveis para obter o tamanho do celular/emulador
	private float larguraDispositivo;
	private float alturaDispositivo;

	//Variavel para obter a mudamça de sprite do passaro
	private float variacao = 0;
	//Variavel para simular a gravidade no jogo
	private int gravidade = 0;
	//Variavel de posicionamento inicial do passaro
	private float posicaoInicialVerticalPassaro = 0;
	//Posição do cano no jogo
	private float posicaoCanohorizontal;
	//Variavel para posicionamento de um cano e outro (separar)
	private float espacoEntreCanos;
	//Variavel para a posicao do cano vertical
	private float posicaoCanoVertical;

	//Variavel para geração de valor aleatorio
	private Random random;
	//Variavel para o texto de pontuação
	BitmapFont textoPontuacao;

	//variavel para indicar se o passaro passou entre os canos
	private boolean passouCano = false;

	//
	private ShapeRenderer shapeRenderer;

	//Circulo de colider para o passaro
	private Circle circuloPassaro;

	//Retangulo para o colider do cano parte de cima
	private Rectangle retanguloCanoCima;

	//Retangulo para o colider do cano parte de baixo
	private Rectangle retanguloCanoBaixo;

	@Override
	public void create () {

		inicializarTexturas();
		inicializarObjetos();

	}

	private void inicializarTexturas() {

		//Criação de objetos das texturas
		batch = new SpriteBatch();
		random = new Random();

		//Textura para o passaro (Contendo 3 sprites)
		passaros = new Texture[3];
		passaros[0] = new Texture("passaro1.png");
		passaros[1] = new Texture("passaro2.png");
		passaros[2] = new Texture("passaro3.png");
		//Textura de background
		fundo = new Texture("fundo.png");

		//Textura do cano
		canoBaixo = new Texture("cano_baixo_maior.png");
		canoTopo = new Texture("cano_topo_maior.png");
	}

	private void inicializarObjetos() {


		//Obtem a referencia do tamanho da tela
		larguraDispositivo = Gdx.graphics.getWidth();
		alturaDispositivo = Gdx.graphics.getHeight();

		//Inicializa o passaro na metade ta tela
		posicaoInicialVerticalPassaro = alturaDispositivo / 2;

		//inicializa os canos no canto direito
		posicaoCanohorizontal = larguraDispositivo;
		espacoEntreCanos = 350;

		//Inicializa o texto de pontuação no jogo
		textoPontuacao = new BitmapFont();
		textoPontuacao.setColor(Color.WHITE);
		textoPontuacao.getData().setScale(10);

	}

	@Override
	public void render () {

		verificarEstadoJogo();
		validarPontos();
		desenharTexturas();
		detectarColisao();

	}

	private void detectarColisao() {

		//Posicionamento do Circulo colider do passaro
		circuloPassaro.set(50 + passaros[0].getWidth() / 2, posicaoInicialVerticalPassaro + passaros[0].getHeight() / 2, passaros[0].getWidth() / 2);

		//Posicionamento do Colider do cano topo
		retanguloCanoCima.set(posicaoCanohorizontal, alturaDispositivo / 2 - canoTopo.getHeight() - espacoEntreCanos / 2 + posicaoCanoVertical,
				canoTopo.getWidth(), canoTopo.getHeight());

		//Posicionamento do colider do cano baixo
		retanguloCanoBaixo.set(posicaoCanohorizontal, alturaDispositivo / 2 - canoBaixo.getHeight() - espacoEntreCanos / 2 + posicaoCanoVertical,
				canoBaixo.getWidth(), canoBaixo.getHeight());

		boolean colisaoCanoCima = Intersector.overlaps(circuloPassaro, retanguloCanoCima);
		boolean colisaoCanoBaixo = Intersector.overlaps(circuloPassaro, retanguloCanoBaixo);

//		if(colisaoCanoBaixo || colisaoCanoCima) {
//
//		}
	}

	private void verificarEstadoJogo() {

		//Realiza a movimentação dos canos horizontal
		posicaoCanohorizontal -= Gdx.graphics.getDeltaTime() * 200;
		if(posicaoCanohorizontal < - canoBaixo.getWidth()){
			posicaoCanohorizontal = larguraDispositivo;
			posicaoCanohorizontal = random.nextInt(400) - 200;
			passouCano = false;
		}

		//Verifica se teve toque na tela
		boolean toqueTela = Gdx.input.justTouched();

		//Realiza um salto na vertical
		if(Gdx.input.justTouched()) {
			gravidade = -25;
		}
		//Realiza a função da gravidade no passaro
		if(posicaoInicialVerticalPassaro > 0 || toqueTela)
			posicaoInicialVerticalPassaro = posicaoInicialVerticalPassaro - gravidade;

		//Realiza a movimentação da asa do passaro (animação)
		variacao += Gdx.graphics.getDeltaTime() * 10;

		//Limitando a variação do passaro
		if(variacao > 3)
			variacao = 0;

		gravidade++;
		//Realiza o aumento da variavel para a movimentação
		movimentaX++;
	}

	private void validarPontos() {
		if(posicaoCanohorizontal < 50 - passaros[0].getWidth()) {
			if(!passouCano) {
				pontos++;
				passouCano = true;
			}
		}
	}

	private void desenharTexturas() {
		//Inicio da Renderização
		batch.begin();

		//O que será desenhado e a sua posição no dispositivo
		batch.draw(fundo, 0,0, larguraDispositivo, alturaDispositivo);
		batch.draw(passaros[(int) variacao], 50, posicaoInicialVerticalPassaro);

		//Realiza o desenho dos canos
		batch.draw(canoBaixo, posicaoCanohorizontal - 100, alturaDispositivo / 2 - canoBaixo.getHeight() - espacoEntreCanos / 2 + posicaoCanoVertical);
		batch.draw(canoTopo, posicaoCanohorizontal - 100, alturaDispositivo / 2 + espacoEntreCanos / 2 + posicaoCanoVertical);

		// realiza o desenho dos pontos na tela
		textoPontuacao.draw(batch, String.valueOf(pontos), larguraDispositivo / 2, alturaDispositivo - 100);

		//fim da renderização
		batch.end();
	}

	@Override
	public void dispose () {

	}
}
