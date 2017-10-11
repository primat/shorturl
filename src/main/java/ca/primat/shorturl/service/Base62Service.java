package ca.primat.shorturl.service;

import org.springframework.stereotype.Service;

/**
 * The Base62Service class handles conversion of decimal numbers to base 62 and vice versa.
 */
@Service
public class Base62Service {

    /**
     * The set of digits used in the base 62 representation of a number.
     */
    private final char[] BASE62_DIGITS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

    /**
     * Returns the base 62 encoded string of a (Long) decimal number.
     * @param value The decimal value to encode
     * @return @String the base 62 string of an integer
     */
    String encode(long value) {
        final StringBuilder sb = new StringBuilder(1);

        // Keep dividing by 62 and taking the remainder. Using the remainder as the index
        // in BASE62_DIGITS, build each digit in the encoded string
        do {
            sb.insert(0, BASE62_DIGITS[(int)(value % 62)]);
            value /= 62;
        } while (value > 0);

        return sb.toString();
    }

    /**
     * Returns the decimal value of a base 62 encoded string.
     * @param value The base 62 number to decode
     * @return @Long the decimal value of a base 62 encoded string.
     */
    public long decode(String value) {
        long result = 0;
        long power = 1;

        // Loop through all digits in the base62 encoded string and convert
        // each one to the appropriate decimal value. Sum them all up for the final result.

        for (int i = value.length() - 1; i >= 0; i--) {

            // Look at each characters in the string, starting from the last, that is, value.charAt(i)
            // Find the offset between the ascii value of the character and its position in BASE62_DIGITS.
            // asciiValue+offset is the value of the character in base 10

            int asciiValue = value.charAt(i);
            int offset;
            if (asciiValue > 90) { // The character is lowercase alpha
                offset = -87;
            }
            else if (asciiValue > 57) { // The character is uppercase alpha
                offset = -29;
            }
            else { // The character  is a decimal number
                offset = -48;
            }

            result += (asciiValue + offset) * power;
            power *= 62;
        }

        return result;
    }
}
