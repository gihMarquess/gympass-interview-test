package br.com.gympass.entidades;

import java.time.LocalTime;

public class Piloto {

	private long id;
	private String nome;
	private LocalTime melhorVolta;
	private Double velocidadeMediaVolta;
	private LocalTime totalTempo;
	private int quantidadeVoltas;
	private int posicao;
	private LocalTime diferencaPrimeiroLugar;
	
	
	public LocalTime getDiferencaPrimeiroLugar() {
		return diferencaPrimeiroLugar;
	}

	public void setDiferencaPrimeiroLugar(LocalTime diferencaPrimeiroLugar) {
		this.diferencaPrimeiroLugar = diferencaPrimeiroLugar;
	}

	public int getQuantidadeVoltas() {
		return quantidadeVoltas;
	}

	public void setQuantidadeVoltas(int quantidadeVoltas) {
		this.quantidadeVoltas = quantidadeVoltas;
	}

	public int getPosicao() {
		return posicao;
	}

	public void setPosicao(int posicao) {
		this.posicao = posicao;
	}

	public Piloto(long id, String nome, Double velocidadeMediaVolta, String totalTempo) {
		super();
		this.id = id;
		this.nome = nome;
		this.melhorVolta = null;
		this.velocidadeMediaVolta = velocidadeMediaVolta;
		this.totalTempo = CorridaUtils.parserToLocalTime(totalTempo);
		this.quantidadeVoltas = 1;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Double getVelocidadeMediaVolta() {
		return velocidadeMediaVolta;
	}

	public void setVelocidadeMediaVolta(Double velocidadeMediaVolta) {
		this.velocidadeMediaVolta = velocidadeMediaVolta;
	}
	
	public void adicionarVelocidadeMediaVolta(Double velocidadeMediaVolta) {
		this.quantidadeVoltas++;
		this.velocidadeMediaVolta = this.velocidadeMediaVolta + velocidadeMediaVolta;
	}
	
	public void calcularVelocidadeMediaVolta() {
		this.velocidadeMediaVolta = this.velocidadeMediaVolta / this.quantidadeVoltas;
	}

	public LocalTime getTotalTempo() {
		return totalTempo;
	}

	public void setTotalTempo(LocalTime totalTempo) {
		this.totalTempo = totalTempo;
	}

	public LocalTime getMelhorVolta() {
		return melhorVolta;
	}

	public void setMelhorVolta(LocalTime melhorVolta) {
		this.melhorVolta = melhorVolta;
	}

}
