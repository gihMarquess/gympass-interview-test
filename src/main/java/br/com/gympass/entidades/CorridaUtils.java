package br.com.gympass.entidades;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class CorridaUtils {

	public static LocalTime parserToLocalTime (String tempoVolta) {
		return LocalTime.parse("00:0" + tempoVolta, DateTimeFormatter.ofPattern("HH:mm:ss.SSS"));
	}
	public static LocalTime parserToLocalTimeHora (String tempoVolta) {
		return LocalTime.parse(tempoVolta, DateTimeFormatter.ofPattern("HH:mm:ss.SSS"));
	}
}
