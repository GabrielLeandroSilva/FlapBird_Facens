package com.myflapbird.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
	
	private Random random;

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
		espacoEntreCanos = 150;

	}

	@Override
	public void render () {

		verificarEstadoJogo();
		desenharTexturas();

	}

	private void verificarEstadoJogo() {

		//Realiza a movimentação dos canos horizontal
		posicaoCanohorizontal -= Gdx.graphics.getDeltaTime() * 200;
		if(posicaoCanohorizontal < - canoBaixo.getWidth()){
			posicaoCanohorizontal = larguraDispositivo;
			posicaoCanohorizontal = random.nextInt(400) - 200;
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

	private void desenharTexturas() {
		//Inicio da Renderização
		batch.begin();

		//O que será desenhado e a sua posição no dispositivo
		batch.draw(fundo, 0,0, larguraDispositivo, alturaDispositivo);
		batch.draw(passaros[(int) variacao], 0, posicaoInicialVerticalPassaro);

		//Realiza o desenho dos canos
		batch.draw(canoBaixo, posicaoCanohorizontal - 100, alturaDispositivo / 2 - canoBaixo.getHeight() - espacoEntreCanos / 2 + posicaoCanoVertical);
		batch.draw(canoTopo, posicaoCanohorizontal - 100, alturaDispositivo / 2 + espacoEntreCanos / 2 + posicaoCanoVertical);

		//fim da renderização
		batch.end();
	}

	@Override
	public void dispose () {

	}
}
