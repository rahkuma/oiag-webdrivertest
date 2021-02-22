package com.tibco.automation.oiag.common.utils;

import java.util.Random;

public abstract class RandomStringGenerator {

	public enum RandomizerTypes {
		DIGITS_ONLY, LETTERS_ONLY, MIXED
	}

	private static final String LETTERS = "qweTYUIOrtyAZERuioFDSpzxJHGcvbnKQWXmasCVBNdfghjklPML";
	private static final int LETTERS_LENGTH = LETTERS.length();
	private static final String NUMBERS = "1357924680187635209";
	private static final int NUMBERS_LENGTH = NUMBERS.length();

	public static String get(int length) {
		return get(new Random(System.currentTimeMillis()), length, RandomizerTypes.MIXED);
	}

	public static String get(int length, RandomizerTypes type) {
		return get(new Random(System.currentTimeMillis()), length, type);
	}

	public static String get(Random random, int length, RandomizerTypes type) {

		random.setSeed(System.currentTimeMillis());
		if (length <= 0) {
			throw new IllegalArgumentException("length has to be bigger than zero");
		}

		StringBuilder generated_str = new StringBuilder("");

		for (int i = 0; i < length; i++) {

			// characters
			if ((RandomizerTypes.LETTERS_ONLY == type)) {
				int ele1 = random.nextInt(LETTERS_LENGTH);
				char c = LETTERS.charAt(ele1);
				if (random.nextDouble() > 0.5D) {
					c = Character.toUpperCase(c);
				}
				generated_str.append(c);
			}
			// Mixed
			else if ((RandomizerTypes.MIXED == type)) {
				int ele1 = random.nextInt(LETTERS_LENGTH + NUMBERS_LENGTH);
				generated_str.append((LETTERS + NUMBERS).charAt(ele1));

			} else {
				int ele = random.nextInt(NUMBERS_LENGTH);
				generated_str.append(NUMBERS.charAt(ele));
			}
		}
		return generated_str.toString();
	}
	
	public static String getIntegerString(int from, int to) {
		Random random = new Random(System.currentTimeMillis());
		return String.valueOf(random.nextInt(to) % (to - from + 1) + from);
	}
}
