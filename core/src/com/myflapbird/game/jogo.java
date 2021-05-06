package com.myflapbird.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class jogo extends ApplicationAdapter {

	//Variaveis para obter as texturas do game
	private SpriteBatch batch;
	private Texture passaro;
	private Texture fundo;

	//Movimentação
	private int movimentaY = 0;
	private int movimentaX = 0;

	//Variaveis para obter o tamanho do celular/emulador
	private float larguraDispositivo;
	private float alturaDispositivo;

	@Override
	public void create () {
		//Creação de objetos das texturas
		batch = new SpriteBatch();
		passaro = new Texture("passaro1.png");
		fundo = new Texture("fundo.png");

		//Obtem a referencia do tamanho da tela
		larguraDispositivo = Gdx.graphics.getWidth();
		alturaDispositivo = Gdx.graphics.getHeight();

	}

	@Override
	public void render () {
		//Inicio da Renderização
		batch.begin();

		//O que será desenhado e a sua posição no dispositivo
		batch.draw(fundo, 0,0, larguraDispositivo, alturaDispositivo);
		batch.draw(passaro, 50, 50, movimentaX, movimentaY);

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
