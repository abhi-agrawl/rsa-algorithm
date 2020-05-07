package rsa;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * Class for obtaining a probable random
 * BigInteger prime number using the MR Test.
 */
public class CreateRandom{

    private BigInteger value;

    /**
     * Creates a random BigInteger probable prime
     * number using SecureRandom class.
     * @param numOfBits Tells how many bits of number it should be.
     */
    public CreateRandom(int numOfBits) {
        do {

            this.value = new BigInteger(numOfBits, new SecureRandom());
            boolean checkEven = this.value.mod(BigInteger.TWO).equals(BigInteger.ZERO);
            if (checkEven)
                this.value = this.value.add(BigInteger.ONE);

        } while (isComposite(this.value));

    }

    /**
     * Use Miller-Rabin test for primality test.
     * Checks with 3 randomly generated number.
     * @param value randomly created BigInteger.
     * @return {@code true} if value is Composite,
     *         {@code false} if value is may be prime.
     */
    private boolean isComposite(BigInteger value){

        int s = 0;
        BigInteger d = value.subtract(BigInteger.ONE);

        do{
            d = d.divide(BigInteger.TWO);
            s++;
        }while(d.mod(BigInteger.TWO).equals(BigInteger.ZERO));

        BigInteger a, remainder;

        for(int i=0;i<3;i++){

            boolean isThisComposite = true;

            do{
                a = new BigInteger(value.bitLength(), new SecureRandom());
            }while(a.compareTo(BigInteger.ONE) > 0 && a.compareTo(value) < 0) ;

            remainder = FME.test(a, d, value);

            if(remainder.equals(BigInteger.ONE) || remainder.subtract(value).equals(BigInteger.ONE.negate())) isThisComposite = false;

            for(int j=1;j < s && isThisComposite; j++){

                remainder = remainder.multiply(remainder).mod(value);
                if(remainder.subtract(value).equals(BigInteger.ONE.negate())) isThisComposite = false;
            }

            if(isThisComposite)
                return true;
        }
        return false;
    }

    public BigInteger getValue() {
        return value;
    }
}

