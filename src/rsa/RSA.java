package rsa;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.math.BigInteger;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Class where everything is put all together.
 * Generating keys, encryption and decryption
 * of the messages.
 */
public class RSA {

    /**
     * Creates a scanner object for System Input.
     */
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws FileNotFoundException {

        int option = startEngine();

        switch (option){
            case 1:
                generateKeyPair();
                break;
            case 2:
                encryptMessage();
                break;
            case 3:
                decryptMessage();
                break;
        }
    }

    /**
     * Asks for what user want to do, like
     * Generating Keys or Encrypt Message or Decrypt Message
     * @return option for what the user wants to do.
     */
    public static int startEngine(){

        int option;

        while(true) {
            System.out.println("\nWhat do you want to do?");
            System.out.println("1. Create Key Pairs");
            System.out.println("2. Encrypt Message");
            System.out.println("3. Decrypt Message");
            System.out.print(">>> Choose Option := ");
            option = scanner.nextInt();

            if(option > 0 && option < 4)
                break;

            System.err.println("[-] Choose correct option.\n");
        }

        return option;
    }

    /**
     * Calls the GenerateKeys class for generating the keys.
     * Display the keys on the screen in the form PK(n,e)
     * and SK(n,d). Also ask if user want to store the keys as
     * files. System will create two separate files PublicKey.txt
     * and PrivateKey.txt (if choose yes to save).
     *
     * Values stores in the from:
     *          PublicKey.txt = n,e
     *          PrivateKey.txt = n,d
     * @throws FileNotFoundException if creating files failed.
     */
    public static void generateKeyPair() throws FileNotFoundException {

        int numOfBits = chooseBits();

        System.out.println("\n[+] Generating Key Pairs...\n");
        GenerateKeys generateKeys = new GenerateKeys(numOfBits);
        System.out.println("[+] PK (n,e) = PK(" + generateKeys.getN() + ", " + generateKeys.getE() + ")");
        System.out.println("[+] SK (n,d) = SK(" + generateKeys.getN() + ", " + generateKeys.getD() + ")");

        while (true){

            System.out.print("\n>>> Do you want to save it in file? (y/n) := ");
            char option = scanner.next().toLowerCase().charAt(0);
            if (option == 'y' || option == 'n') {
                if(option == 'y') {
                    PrintStream out1 = new PrintStream("PublicKey.txt");
                    out1.println(generateKeys.getN() + "," + generateKeys.getE());
                    PrintStream out2 = new PrintStream("PrivateKey.txt");
                    out2.println(generateKeys.getN() + "," + generateKeys.getD());
                    System.out.println("[+] Files created for both the keys.");
                }
                break;
            }

            System.err.println("[-] Choose correct option.\n");
        }


    }

    /**
     * Asks for which RSA Algorithm user wants
     * in the form of Number of Bits.
     * @return number of bits user wants for the keys.
     */
    public static int chooseBits(){

        while(true) {

            System.out.println("\nWhich RSA Algorithm? ");
            System.out.println("1. RSA-50");
            System.out.println("2. RSA-100");
            System.out.println("3. RSA-128");
            System.out.println("4. RSA-192");
            System.out.println("5. RSA-256");
            System.out.println("6. RSA-512 (Recommended)");
            System.out.println("7. RSA-1024 (Best but slower)");
            System.out.print(">>> Choose Option := ");
            int option = scanner.nextInt();

            switch (option) {
                case 1:
                    return 50;
                case 2:
                    return 100;
                case 3:
                    return 128;
                case 4:
                    return 192;
                case 5:
                    return 256;
                case 6:
                    return 512;
                case 7:
                    return 1024;
                default:
                    System.err.println("[-] Choose correct option.\n");
            }
        }
    }

    /**
     * Asks for a message for encryption. Checks the
     * message length with n and then Encrypts the
     * message using FME. Displays encrypted message
     * on the screen and also ask if you want to save
     * the message in a file. File is saved with the name
     * EncryptedMessages.txt
     * @throws FileNotFoundException if creating file failed.
     */
    public static void encryptMessage() throws FileNotFoundException {

        BigInteger[] values = getValues("Public");
        BigInteger eMessage, m;

        while(true) {
            System.out.print("\n>>> Type Message to Encrypt: ");
            m = scanner.nextBigInteger();

            eMessage = FME.test(m, values[1], values[0]);

            if(values[0].compareTo(m) > 0)
                break;

            System.err.println("[-] Message is to long to encrypt.\n");
        }

        System.out.println("\n[+] Encrypted Message: " + eMessage);

        while(true){
            System.out.print("\n>>> Write Encrypted Message to File? (y/n) := ");
            char option = scanner.next().toLowerCase().charAt(0);
            if(option == 'y' || option == 'n') {
                if(option == 'y'){
                    PrintStream out = new PrintStream("EncryptedMessage.txt");
                    out.println(eMessage);
                    System.out.println("\n[+] File Created with name EncryptedMessage.txt");
                }
                break;
            }
            System.err.println("\n[-] Choose correct option.\n");
        }
    }

    /**
     * Asks for an Encrypted Message to Decrypt.
     * Decrypts the encrypted message using FME.
     * Displays the original message on the screen.
     */
    public static void decryptMessage(){

        BigInteger[] values = getValues("Private");

        System.out.print("\n>>> Type Message to Decrypt: ");
        BigInteger c = scanner.nextBigInteger();

        System.out.println("\n[+] Original Message: " + FME.test(c, values[1], values[0]));
    }

    /**
     * Common method used by encrypting and decrypting methods.
     * When asked user to import keys or not. If choose yes and
     * didn't find the file or choose no then asks to enter the
     * e,d,n depends on what user what to do.
     * @param keyType tells the method whether its encryption or
     *                decryption
     * @return BigInteger Array with [n, (e or d)]
     */
    public static BigInteger[] getValues(String keyType){

        char option = importFile(keyType);
        char exponent;
        String fileName = keyType + "Key.txt";

        if(keyType.equals("Public"))
            exponent = 'E';
        else
            exponent = 'D';

        BigInteger[] values = new BigInteger[2];

        if (option == 'y') {
            try {

                Scanner file = new Scanner(new File(fileName));
                Scanner line = new Scanner(file.nextLine()).useDelimiter(",");
                values[0] = line.nextBigInteger();
                values[1] = line.nextBigInteger();
                System.out.println("\n[+] Keys Imported.");

            } catch (FileNotFoundException ex) {
                System.err.println("[-] File not Found.");
                System.err.println("[-] File name should be " + fileName +". Add it Manually!!\n");
                option = 'n';
            } catch (InputMismatchException ex){
                System.err.println("[-] Format of file is inappropriate.");
                System.err.println("[-] Required (N," + exponent + ") format. Add it Manually!!\n");
                option = 'n';
            }
        }

        if(option == 'n'){
            System.out.print("\n>>> Enter N := ");
            values[0] = scanner.nextBigInteger();

            System.out.print(">>> Enter " + exponent + " := ");
            values[1] = scanner.nextBigInteger();
        }

        return values;
    }

    /**
     * Asks if user wants to import the keys which was saved using
     * this program or in the same format used by this program.
     * @param keyType tells the method whether to ask for public
     *                or private key.
     * @return {@code y} if user want to import the key
     *         {@code n} if user doesn't want to import the keys.
     */
    public static char importFile(String keyType){

        while (true) {

            System.out.print("\n>>> Import " + keyType + " Key from File? (y/n) := ");
            char option = scanner.next().toLowerCase().charAt(0);
            if(option == 'y' || option == 'n')
                return option;

            System.err.println("\n[-] Choose correct option.\n");
        }
    }
}