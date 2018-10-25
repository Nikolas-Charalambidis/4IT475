package cz.vse.fis.ws.polyalphabetic;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import cz.vse.fis.validation.annotation.Alphabetic;
import cz.vse.fis.validation.annotation.Numeric;

@Component
@Validated
public class PolybiusSquare {
	
	private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWYZ";
	private static final int size = 5;

	public final String encrypt(@Alphabetic final String value, @Alphabetic final String key) throws InvalidParameterException {
		
		StringBuilder sb = new StringBuilder();
		
		String[][] square = this.constructSquare(key);
		String[] array = value.toUpperCase().replaceAll("X", "Y").replaceAll(" ", "").split("");
		
		
		for (String string: array) {
			String number = "";
			for (int i=0; i<square.length; i++) {
				String[] subArray = square[i];
				for (int j=0; j<subArray.length; j++) {
					String letter = subArray[j];
					if (string.equals(letter)) {
						int ii = i+1;
						int jj = j+1;
						number = new StringBuilder(String.valueOf(ii)).append(jj).toString();
						break;
					}
				}
			}
			sb.append(number).append(" ");
		}
		
		return sb.toString().trim();
	}

	public final String decrypt(@Numeric final String value, @Alphabetic final String key) throws InvalidParameterException {

		String[][] square = this.constructSquare(key);
		return Arrays.asList(value.split(" "))
					 .stream()
					 .map(s -> {
						 int i = Integer.parseInt(s.charAt(0) + "") - 1;
						 int j = Integer.parseInt(s.charAt(1) + "") - 1;
						 return square[i][j];
					 }).collect(Collectors.joining(""));
	}
	
	private String[][] constructSquare(String key) {
		
		String[][] square = new String[5][5];
		int inner = 0;
		int outer = 0;
		
		List<String> keyDuplicateLetters = Arrays.asList(key.toUpperCase().replace(" ", "").split(""));		
		List<String> letters = new ArrayList<>(new LinkedHashSet<>(keyDuplicateLetters));
		List<String> remainingLetters = new ArrayList<>(Arrays.asList(ALPHABET.split("")));
		remainingLetters.removeAll(letters);
		letters.addAll(remainingLetters);

		Iterator<String> iterator = letters.iterator();
		while (iterator.hasNext()) {
			if (outer < size) {
				if (inner < size) { 
					square[outer][inner] = iterator.next();
					inner++;
				} else {
					inner = 0;
					outer++;
				}
			} else {
				break;
			}
		}
		
		return square;
	}
}