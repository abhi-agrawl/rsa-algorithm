package rsa;

import java.math.BigInteger;

/**
 * Class for generating public and
 * private key value pairs.
 */
public class GenerateKeys {

    private BigInteger n;
    private BigInteger phiN;
    private int e;
    private BigInteger d;

    /**
     * Creates all the necessary values for RSA Algorithm
     * and then checks by encrypting decrypting the message
     * m which is 2.
     * @param numOfBits Tells how many bits of number it should be.
     */
    public GenerateKeys(int numOfBits) {

        while (true) {

            BigInteger p = new CreateRandom(numOfBits).getValue();
            BigInteger q = new CreateRandom(numOfBits).getValue();

            this.n = p.multiply(q);
            this.phiN = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
            calculateEncryptionExponent();
            calculateDecryptionExponent();

            BigInteger m = BigInteger.valueOf(2);
            if (m.equals(FME.test(FME.test(m, BigInteger.valueOf(e), n), d, n)))
                break;
        }
    }

    /**
     * Calculates the Encryption Exponent e.
     * e is an odd number starting from 3
     * Checks the gcd of e and totient(n) = 1
     */
    private void calculateEncryptionExponent(){

        this.e = 3;

        while (true){
            boolean isCoPrime = calculateGCD(BigInteger.valueOf(this.e), phiN).equals(BigInteger.ONE);

            if(isCoPrime) {
                break;
            }
            else
                this.e += 2;
        }
    }

    /**
     * Calculates GCD for encryptionExponent and
     * the totient(n) using Euclidean Algorithm
     * @param a is the encryptionExponent
     * @param phiN is the totient(n)
     * @return gcd of two values
     */
    public static BigInteger calculateGCD(BigInteger a, BigInteger phiN){

        if(phiN.equals(BigInteger.ZERO))
            return a;
        return calculateGCD(phiN, a.mod(phiN));
    }

    /**
     * Calculates the Decryption Exponent d.
     * Uses Extended Euclidean Algorithm
     * where a = Encryption Exponent(e) and
     * b = totient(n) and return the final value of X.
     */
    private void calculateDecryptionExponent(){

        BigInteger r1 = new BigInteger(this.phiN.toString());
        BigInteger r2 = BigInteger.valueOf(this.e);

        BigInteger quotient;

        BigInteger x1 = BigInteger.ZERO;
        BigInteger x2 = BigInteger.ONE;

        int i;

        for(i=1; !r2.equals(BigInteger.ZERO); i++ ){

            quotient = r1.divide(r2);

            BigInteger tempX = x2;
            x2 = x2.multiply(quotient).add(x1);
            x1 = tempX;

            BigInteger tempR = r2;
            r2 = r1.mod(r2);
            r1 = tempR;

        }

        BigInteger invMod = BigInteger.valueOf(i % 2 == 0 ? 1 : -1).multiply(x1);
        this.d = invMod.compareTo(BigInteger.ZERO) > 0 ? invMod : this.phiN.add(invMod);
    }

    public BigInteger getN() {
        return n;
    }

    public int getE() {
        return e;
    }

    public BigInteger getD() {
        return d;
    }
}
