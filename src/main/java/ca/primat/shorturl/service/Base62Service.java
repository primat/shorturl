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

    /*
     * The highest ID possible. Since a short URL cannot contain more than 10 alphanumeric characters,
     * we are limited to 62^10-1 (or 839,299,365,868,340,223) items maximum.
     */
    //public static final long MAX_ID = 839299365868340223L; // Corresponds to ten consecutive Z's

    /**
     * Returns the base 62 encoded string of a (Long) decimal number.
     * @param value The decimal value to encode
     * @return @String the base 62 string of an integer
     */
    String encode(long value) {
        final StringBuilder sb = new StringBuilder(1);

        // Keep dividing by 62 and taking the remainder. using the remainder as the index in BASE62_DIGITS,
        // we get each digits in the encoded string
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

            // Find the offset between the ascii value of value.charAt(i) and it's position in BASE62_DIGITS.

            int asciiValue = value.charAt(i);
            int offset;
            if (asciiValue > 90) { // value.charAt(i) is lowercase alpha
                // ascii(a) = 97, BASE62_DIGITS[10] = 'a', offset = 10 - 97 = -87
                offset = -87;
            }
            else if (asciiValue > 57) { // value.charAt(i) is uppercase alpha
                // ascii(A) = 65, BASE62_DIGITS[36] = 'A', offset = 36 - 65 = -29
                offset = -29;
            }
            else { // value.charAt(i) is a decimal number
                // ascii(0) = 48, BASE62_DIGITS[0] = '0', offset = 0 - 48 = -48
                offset = -48;
            }

            result += (asciiValue + offset) * power;
            power *= 62;
        }

        return result;
    }
}
