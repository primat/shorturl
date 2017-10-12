package ca.primat.shorturl.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * The Base62Service class handles conversion of decimal numbers to base 62 and vice versa.
 */
@Service
public class Base62Service {

    // The following properties are used as a cache, for rapid conversion between decimal and non-decimal digits
    private final String DIGITS_STRING = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private final char[] DIGITS_ARRAY = DIGITS_STRING.toCharArray();
    private final Map<Character, Integer> DIGITS_MAP;
    private final int BASE = DIGITS_ARRAY.length;

    /**
     * Constructor. Initialize DIGITS_MAP.
     */
    public Base62Service() {
        DIGITS_MAP = new HashMap<Character, Integer>();
        for (int i = 0; i < DIGITS_STRING.length(); i++) {
            DIGITS_MAP.put(DIGITS_ARRAY[i], i);
        }
    }

    /**
     * Returns the base 62 encoded string of a (Long) decimal number.
     * @param value The decimal value to encode
     * @return @String the base 62 string of an integer
     */
    public String encode(long value) {

        // Do not proceed on bad input
        if (value < 0) {
            throw new IllegalArgumentException();
        }

        // Keep dividing by BASE and taking the remainder. Using the remainder as the index
        // in DIGITS_ARRAY, build each digit in resulting number
        final StringBuilder sb = new StringBuilder(1);

        do {
            char c = DIGITS_ARRAY[(int)(value % BASE)];
            sb.insert(0, c);
            value /= BASE;
        } while (value > 0);

        return sb.toString();
    }

    /**
     * Returns the decimal value of a base 62 number, where the number is encoded as a string.
     * @param encodedNumber The base 62 number to decode
     * @return The decimal value of encodedNumber
     */
    public long decode(String encodedNumber) {

        // Loop through all digits in the base 62 number and convert each one to its
        // decimal value. Sum them all up for the final result.
        long result = 0;
        long power = 1;

        for (int i = encodedNumber.length() - 1; i >= 0; i--) {

            // Get the decimal value of the character we're currently looking at
            Integer decimalValue = DIGITS_MAP.get(encodedNumber.charAt(i));

            // Handle invalid characters in the encoded string
            if (decimalValue == null) {
                throw new IllegalArgumentException();
            }

            result += decimalValue * power;
            power *= BASE;
        }

        return result;
    }
}
