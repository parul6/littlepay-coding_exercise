package org.littlepay.util;

import java.util.Optional;

public class Utility {

	public static String normalizedKey(String stop1, String stop2) {
		// Normalize the key so the order of stops doesn't matter
		return Optional.ofNullable(stop1).orElse("UNKNOWN") // If stop1 is null, use "UNKNOWN"
				.compareTo(Optional.ofNullable(stop2).orElse("UNKNOWN")) < 0  // Compare with stop2, which is also null-safe
				? stop1 + "-" + stop2
				: stop2 + "-" + stop1;
	}

}
