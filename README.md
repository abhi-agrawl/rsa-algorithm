# RSA Algorithm

 - [x] Generating Key value pair with a multiple options for number of bits to choose
from. 
- [x] Option to store Public and Private Key in .txt file.
- [x] Encrypts a message (also gives option to import key stored by the same program)
- [x] Option to store Encrypted Message in .txt file.
- [X] Decrypts a message (also gives option to import key stored by the same program)

## Implementation

#### I. Key Generation

> Using [CreateRandom Class](src/rsa/CreateRandom.java) 
  - Get two different large prime numbers ***p*** and ***q*** 
    - Using *Miller-Rabin Test*

> Using [GenerateKeys Class](src/rsa/GenerateKeys.java)
 - Let ***n*** = *p* * *q*
 - Let ***φ(n)*** = *(p-1)* * *(q-1)*
 - Choose a small, odd integer number ***e***, where 1 < e < φ(n) 
 and *e* is a co-prime to ***φ(n)***. 
    - Check *calculateEncryptionExponent()* method
 - Calculate the integer number ***d***, where *e*d ≡ 1 mod φ(n) and 1 <
   d < φ(n).  
   - Using *Extended Euclidean Algorithm* 
   - Check *calculateDecryptionExponent()* method
 
 - [x] The public key of the RSA is the pair PK = (n, e)
 - [x] The secret key of the RSA is the pair SK = (n, e) 
 
 #### II. Encryption
 
 > Using [FME Class](src/rsa/FME.java) 
- To encrypt the message **m** (where m < n) using the public key ***PK = (n,
   e)*** following formula is used: **(Fast Modular Exponent)**
   
   ```
   c := EncPK(m) = m^e (mod n).
   ```
  
#### III. Decryption
> Using [FME Class](src/rsa/FME.java)
- To decrypt the secret message ***c*** using the secret key ***d***
 following formula is used: **(Fast Modular Exponent)**
  
  ```
  m := DecSK(c) = c^d (mod n).
  ```