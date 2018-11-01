package com.example.demo.services;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.ws.rs.core.MediaType;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import br.com.gympass.entidades.CorridaUtils;
import br.com.gympass.entidades.Piloto;

@RestController
public class Corrida {
	public final String PATTERN_LOG = "(\\d+:\\d+:\\d+.\\d+)\\s+(\\d+)\\s+–\\s+([A-z].[A-z]+)\\s+(\\d)\\s+(\\d+:\\d+.\\d+)\\s+(\\d+,\\d+)";
	
	@RequestMapping(path= {"/corrida"}, produces=MediaType.APPLICATION_JSON)
	public List<Piloto> uploadFile(@RequestParam MultipartFile file) {
		Map<Long, Piloto> pilotos = new HashMap<Long, Piloto>();
		try {
			for (String linha : new String(file.getBytes()).split(System.lineSeparator())) {
				Pattern pattern = Pattern.compile(PATTERN_LOG);
				Matcher matcher = pattern.matcher(linha);
				Piloto piloto = null;
				if (matcher.matches()) {
					Long idPiloto = Long.valueOf(matcher.group(2));
					if (!pilotos.containsKey(idPiloto)) {
						piloto = criaPilotoString(matcher);
						pilotos.put(idPiloto, piloto);
					} else {
						piloto = pilotos.get(idPiloto);
						calculaTempoTotal(matcher, piloto);
						defineMelhorVoltaPiloto(matcher, piloto);
						piloto.adicionarVelocidadeMediaVolta(Double.valueOf(matcher.group(6).replace(",", ".")));
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		final List<Piloto> pilotosList = new ArrayList<Piloto>();
		
		pilotosToList(pilotos, pilotosList);
		calculaPosicaoPiloto(pilotosList);
		
		calculaDiferencaPrimeiroLugar(pilotosList);
		
		return ordenaResultado(pilotosList);
	}

	private void calculaDiferencaPrimeiroLugar(final List<Piloto> pilotosList) {
		Piloto pilotoPrimeiroLugar = pilotosList.stream().filter(piloto -> piloto.getPosicao() == 1).findFirst().orElse(null);
		for(Piloto piloto : pilotosList) {
			if(!(piloto.getPosicao() == 1)) {
				piloto.setDiferencaPrimeiroLugar(LocalTime.ofNanoOfDay(1000 * 1000 * pilotoPrimeiroLugar.getTotalTempo().until(piloto.getTotalTempo(), ChronoUnit.MILLIS)));
			}
		}
	}

	private Piloto criaPilotoString(Matcher matcher) {
		Piloto piloto;
		piloto = new Piloto(Long.valueOf(matcher.group(2)), matcher.group(3),
				Double.valueOf(matcher.group(6).replace(",", ".")), matcher.group(5));
		return piloto;
	}

	private void defineMelhorVoltaPiloto(Matcher matcher, Piloto piloto) {
		if(piloto.getMelhorVolta() == null) {
			piloto.setMelhorVolta(CorridaUtils.parserToLocalTime(matcher.group(5)));
		}else {
			if(piloto.getMelhorVolta().compareTo(CorridaUtils.parserToLocalTime(matcher.group(5))) > 0){
				piloto.setMelhorVolta(CorridaUtils.parserToLocalTime(matcher.group(5)));
			}
		}
	}

	private void calculaTempoTotal(Matcher matcher, Piloto piloto) {
		piloto.setTotalTempo(piloto.getTotalTempo().plusNanos(CorridaUtils.parserToLocalTime(matcher.group(5)).toNanoOfDay()));
	}

	private List<Piloto> ordenaResultado(final List<Piloto> pilotosList) {
		return pilotosList.stream().sorted(Comparator.comparing(Piloto::getTotalTempo).thenComparing(Piloto::getQuantidadeVoltas)).collect(Collectors.toList());
	}

	private void pilotosToList(Map<Long, Piloto> pilotos, final List<Piloto> pilotosList) {
		if(pilotos.size() > 0) {
			pilotos.forEach((id, piloto)-> {
				piloto.calcularVelocidadeMediaVolta();
				pilotosList.add(piloto);
			});
		}
	}

	private void calculaPosicaoPiloto(final List<Piloto> pilotosList) {
		for(int x = 0; x < pilotosList.size(); x++) {
			ordenaResultado(pilotosList).get(x).setPosicao(x+1);
		
		}
	}

}
