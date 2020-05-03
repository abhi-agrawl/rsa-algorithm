package rsa;

import java.math.BigInteger;

/**
 * Class with the implementation of
 * Fast Modular Exponentiation.
 */
public class FME {

    /**
     * Returns the mod using FME test.
     * @param a is the base of BigInteger type
     * @param d is the exponent of BigInteger type
     * @param value is modulo (m) of BigInteger type
     * @return a^d (mod m)
     */
    static BigInteger test(BigInteger a, BigInteger d, BigInteger value){

        BigInteger finalNumber = BigInteger.ONE;
        BigInteger remainder = a.mod(value);

        String binaryOfd = getBinary(d);

        int len = binaryOfd.length() - 1;

        for(int i=0; i <= len; i++){

            if(binaryOfd.charAt(len - i) == '1')
                finalNumber = finalNumber.multiply(remainder);

            remainder = remainder.multiply(remainder).mod(value);
        }
        return finalNumber.mod(value);
    }

    /**
     * Converts decimal number to binary number.
     * @param number a decimal number which will be converted.
     * @return Binary Number in the form of String
     */
    private static String getBinary(BigInteger number){

        String binaryNumber = "";

        while(!number.equals(BigInteger.ZERO)){
            binaryNumber = number.mod(BigInteger.TWO) + binaryNumber;
            number = number.divide(BigInteger.TWO);
        }
        return binaryNumber;
    }
}
