package com.myflapbird.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class jogo extends ApplicationAdapter {

	//Variaveis para obter as texturas do game
	private SpriteBatch batch;
	private Texture[] passaros;
	private Texture fundo;

	//Movimentação
	private int movimentaY = 0;
	private int movimentaX = 0;

	//Variaveis para obter o tamanho do celular/emulador
	private float larguraDispositivo;
	private float alturaDispositivo;

	//Variavel para obter a mudamça de sprite do passaro
	private float variacao = 0;
	//Variavel para simular a gravidade no jogo
	private float gravidade = 0;
	//Variavel de posicionamento inicial do passaro
	private float posicaoInicialVerticalPassaro = 0;

	@Override
	public void create () {
		//Creação de objetos das texturas
		batch = new SpriteBatch();

		//Textura para o passaro (Contendo 3 sprites)
		passaros = new Texture[3];
		passaros[0] = new Texture("passaro1.png");
		passaros[1] = new Texture("passaro2.png");
		passaros[2] = new Texture("passaro3.png");
		//Textura de background
		fundo = new Texture("fundo.png");

		//Obtem a referencia do tamanho da tela
		larguraDispositivo = Gdx.graphics.getWidth();
		alturaDispositivo = Gdx.graphics.getHeight();

		//Inicializa o passaro na metade ta tela
		posicaoInicialVerticalPassaro = alturaDispositivo / 2;

	}

	@Override
	public void render () {
		//Inicio da Renderização
		batch.begin();

		//Limitando a variação do passaro
		if(variacao > 3)
			variacao = 0;

		//Verifica se teve toque na tela
		boolean toqueTela = Gdx.input.justTouched();

		//Realiza um salto na vertical
		if(Gdx.input.justTouched()) {
			gravidade = -25;
		}

		//Realiza a função da gravidade no passaro
		if(posicaoInicialVerticalPassaro > 0 || toqueTela)
			posicaoInicialVerticalPassaro = posicaoInicialVerticalPassaro - gravidade;

		//O que será desenhado e a sua posição no dispositivo
		batch.draw(fundo, 0,0, larguraDispositivo, alturaDispositivo);
		batch.draw(passaros[(int) variacao], 30, posicaoInicialVerticalPassaro);

		//Realiza a movimentação da asa do passaro (animação)
		variacao += Gdx.graphics.getDeltaTime() * 10;
		gravidade++;

		//Realiza o aumento da variavel para a movimentação
		movimentaY++;
		movimentaX++;

		//fim da renderização
		batch.end();
	}
	
	@Override
	public void dispose () {

	}
}
