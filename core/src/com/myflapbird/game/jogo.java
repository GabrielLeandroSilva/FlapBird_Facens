package com.myflapbird.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Sound;
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
	private Texture gameover;

	//Movimentação
	private int movimentaY = 0;
	private int movimentaX = 0;

	//Pontuação do jogo
	private int pontos = 0;
	//Estado do jogo
	private int estadoJogo = 0;

	//Record do jogador
	private int pontuacaoMaxima = 0;

	//Variaveis para obter o tamanho do celular/emulador
	private float larguraDispositivo;
	private float alturaDispositivo;

	//Variavel de identificação da posicao do passaro
	private float posicaoHorizontalPassaro = 0;

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
	BitmapFont textoReiniciar;
	BitmapFont textoMelhorPontuacao;

	//Variaveis de som para o jogo
	Sound somVoando;
	Sound somColisao;
	Sound somPontuacao;


	//variavel para indicar se o passaro passou entre os canos
	private boolean passouCano = false;
	private ShapeRenderer shapeRenderer;
	//Circulo de colider para o passaro
	private Circle circuloPassaro;
	//Retangulo para o colider do cano parte de cima
	private Rectangle retanguloCanoCima;
	//Retangulo para o colider do cano parte de baixo
	private Rectangle retanguloCanoBaixo;

	//Realiza uma interface (persistencia de dados)
	Preferences preferences;

	@Override
	public void create () {

		inicializarTexturas();
		inicializarObjetos();

	}

	private void inicializarTexturas() {


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
		//Textura do texto do game over
		gameover = new Texture("game_over.png");

	}

	private void inicializarObjetos() {

		//Criação de objetos das texturas
		batch = new SpriteBatch();
		random = new Random();

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

		//Inicializa o texto de melhor pontuação no jogo
		textoMelhorPontuacao = new BitmapFont();
		textoMelhorPontuacao.setColor(Color.GREEN);
		textoMelhorPontuacao.getData().setScale(2);

		//Inicializa o texto de Reinicia
		textoReiniciar = new BitmapFont();
		textoReiniciar.setColor(Color.RED);
		textoReiniciar.getData().setScale(2);

		//Inicializa os coliders
		shapeRenderer = new ShapeRenderer();
		circuloPassaro = new Circle();
		retanguloCanoCima = new Rectangle();
		retanguloCanoBaixo = new Rectangle();

		//Inicializa o audio de colisão
		somColisao = Gdx.audio.newSound(Gdx.files.internal("som_batida.wav"));
		//Inicializa o audio de voo do passaro
		somVoando = Gdx.audio.newSound(Gdx.files.internal("som_asa.wav"));
		//Inicializa o audio de ganho de pontos
		somPontuacao = Gdx.audio.newSound(Gdx.files.internal("som_pontos.wav"));
		//Inicializa interface
		preferences = Gdx.app.getPreferences("flapbird");
		pontuacaoMaxima = preferences.getInteger("pontuacaoMaxima", 0);

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
		retanguloCanoCima.set(posicaoCanohorizontal, alturaDispositivo / 2 + espacoEntreCanos / 2 + posicaoCanoVertical,
				canoTopo.getWidth(), canoTopo.getHeight());

		//Posicionamento do colider do cano baixo
		retanguloCanoBaixo.set(posicaoCanohorizontal, alturaDispositivo / 2 - canoBaixo.getHeight() - espacoEntreCanos / 2 + posicaoCanoVertical,
				canoBaixo.getWidth(), canoBaixo.getHeight());

		//Boleana para detectar a colisao nos canos
		boolean colisaoCanoCima = Intersector.overlaps(circuloPassaro, retanguloCanoCima);
		boolean colisaoCanoBaixo = Intersector.overlaps(circuloPassaro, retanguloCanoBaixo);

		//Verifica se houve colisao nos canos e muda estado do jogo
		if(colisaoCanoBaixo || colisaoCanoCima) {
			if(estadoJogo == 1) {
				somColisao.play();
				estadoJogo = 2;
			}
		}
	}

	private void verificarEstadoJogo() {

		//Verifica se teve toque na tela
		boolean toqueTela = Gdx.input.justTouched();

		if(estadoJogo == 0) {
			//Realiza um salto na vertical
			if(Gdx.input.justTouched()) {
				gravidade = -15;
				estadoJogo = 1;
				somVoando.play();
			}
		} else if (estadoJogo == 1) {
			if(Gdx.input.justTouched()) {
				gravidade = -15;
				somVoando.play();
			}

			//Realiza a movimentação dos canos horizontal
			posicaoCanohorizontal -= Gdx.graphics.getDeltaTime() * 200;
			if(posicaoCanohorizontal < - canoBaixo.getWidth()){
				posicaoCanohorizontal = larguraDispositivo;
				posicaoCanoVertical = random.nextInt(400) - 200;
				passouCano = false;
			}

			//Realiza a função da gravidade no passaro
			if(posicaoInicialVerticalPassaro > 0 || toqueTela)
				posicaoInicialVerticalPassaro = posicaoInicialVerticalPassaro - gravidade;

			gravidade++;

		} else if (estadoJogo == 2) {
			//Metodo para salvar pontuação record
			if(pontos > pontuacaoMaxima) {
				pontuacaoMaxima = pontos;
				preferences.putInteger("pontuaçãoMaxima", pontuacaoMaxima);
			}

			posicaoHorizontalPassaro -= Gdx.graphics.getDeltaTime() * 500;

			//Realiza o reset das variaveis para o reinicio do jogo
			if(toqueTela) {
				estadoJogo = 0;
				pontos = 0;
				gravidade = 0;
				posicaoHorizontalPassaro = 0;
				posicaoInicialVerticalPassaro = alturaDispositivo / 2;
				posicaoCanohorizontal = larguraDispositivo;

			}
		}

		//Realiza o aumento da variavel para a movimentação
		//movimentaX++;
	}

	private void validarPontos() {
		if(posicaoCanohorizontal < 50 - passaros[0].getWidth()) {
			if(!passouCano) {
				pontos++;
				passouCano = true;
				somPontuacao.play();
			}
		}
		//Realiza a movimentação da asa do passaro (animação)
		variacao += Gdx.graphics.getDeltaTime() * 10;

		//Limitando a variação do passaro
		if(variacao > 3)
			variacao = 0;
	}

	private void desenharTexturas() {
		//Inicio da Renderização
		batch.begin();

		//O que será desenhado e a sua posição no dispositivo
		batch.draw(fundo, 0,0, larguraDispositivo, alturaDispositivo);
		batch.draw(passaros[(int) variacao], 50 + posicaoHorizontalPassaro, posicaoInicialVerticalPassaro);

		//Realiza o desenho dos canos
		batch.draw(canoBaixo, posicaoCanohorizontal, alturaDispositivo / 2 - canoBaixo.getHeight() - espacoEntreCanos / 2 + posicaoCanoVertical);
		batch.draw(canoTopo, posicaoCanohorizontal, alturaDispositivo / 2 + espacoEntreCanos / 2 + posicaoCanoVertical);

		// realiza o desenho dos pontos na tela
		textoPontuacao.draw(batch, String.valueOf(pontos), larguraDispositivo / 2, alturaDispositivo - 100);

		//Realiza o desenho quando de game over
		if(estadoJogo == 2) {
			batch.draw(gameover, larguraDispositivo / 2 - gameover.getWidth() / 2, alturaDispositivo / 2);
			textoReiniciar.draw(batch, "Toque na tela para Reiniciar!", larguraDispositivo / 2 - 250, alturaDispositivo / 2 - gameover.getHeight() / 2);
			textoMelhorPontuacao.draw(batch, "Sua melhor pontuação é: " + pontuacaoMaxima + " Pontos!", larguraDispositivo / 2 -200, alturaDispositivo / 2 - gameover.getHeight() * 2);
		}

		//fim da renderização
		batch.end();
	}

	@Override
	public void dispose () {

	}
}
