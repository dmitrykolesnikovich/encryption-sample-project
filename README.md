This repository is based on following article https://medium.com/mindorks/how-to-pass-large-data-between-server-and-client-android-securely-345fed551651

# How to pass large data between server and client (android) securely?

### Using RSA and AES (Hybrid) encryption technique

Most of the times, we pass sensitive data from our Android app to our server. Such sensitive data could be a personal message one user is sending to another, could be an information about the user, could be user’s SMS, so on and so forth. While we should always explicitly inform users before transferring such sensitive data, but we should also make sure we transfer this data as securely as possible.
It shouldn’t surprize you that companies have faced huge financial losses due to poor security, and it has lead to shut down of companies as well. Another surprize is that implementing this isn’t complicated at all! With little knowledge you can create a rock solid security for the client — server communication.

### ABC of Encryption

Consider you have a sensitive information say user’s email ID. You want to securely transfer it to your server. How will you do it? Hmm.. one way could be to encrypt it using a passcode. Client will use this passcode to encrypt user’s email ID and send to the server. Server will, in turn, use the same passcode to decrypt the data received from the client. If during the transmission of the data someone gets access to the transmitting data, he/ she wouldn’t be able to make sense of it as it is encrypted. Also, since the intruder doesn’t have access to the passcode, he/ she wouldn’t be able to decrypt the data.
This kind of encryption technique is called symmetric encryption. It is fast and can encrypt large texts of data. The drawback includes safe distribution of the key. Also, you need to ship the key in your app — which is a highly unrecommended practice. The most popular Symmetric Algorithms are DES, Triple-DES, AES, Blowfish, RC2, RC4(ARCFOUR), RC5, RC6.
Hmm, this is an issue. You can’t ship the passcode in your app. If the intruder gets access to your passcode then he/ she can easily get all the sensitive data that will be transmitted from your app.
What to do now? What if there was an encryption technique where the algorithm would generate a key pair for you comprising of 2 keys. 1 public key and 1 private key. Public key is visible publicly and anyone can use that key to encrypt sensitive data. But now the decryption of that data can only be done by private key which only you have access to. Wow! That means you can safely ship public key in your app and keep the private key on server. This is just wonderful!
This kind of encryption technique is called asymmetric encryption. It is comparatively very slow and can encrypt only very small texts of data at a time (128 bytes to be exact!). The most popular Asymmetric Algorithms (aka Public Key Algorithms) are RSA, Diffie-Hellman, ElGamal, DSS.
So now it is clear that you can’t use symmetric encryption because you can’t ship the passcode in your app. Also, it is impractical to use asymmetric encryption because 99% of the times the data that you’d want to transfer would be of more than 128 bytes in size! What to do?

### Hybrid solution to the rescue!

Consider this:
Using an asymmetric encryption (say RSA), the server generates a key pair consisting of a public key and a private key.
Server saves these keys in a secure location.
We take public key and ship it in our app (client).
When we want to transfer some sensitive data to server (at runtime), we generate a passcode (aka secret key) using a symmetric encryption (say AES).
Using this secret key we encrypt our large texts of data quickly.
Now we use the public key to encrypt our secret key.
We send this encrypted data and encrypted secret key combination to server (using any commonly used way to send combination of data, like JSON)
Server receives this combination, extracts encrypted data and encrypted secret key from it.
Server uses private key to decrypt the encrypted secret key.
Server uses decrypted secret key (or simply called secret key) to decrypt the encrypted data. Hence it gets the large texts of data which was sent by the client securely.
This technique is called Hybrid Cryptography.

### Show me the code!

1. First of all server needs to generates a key pair using RSA. This will be done via the following class: (Note that this code needs to be run only 1 time as you’ll use the same key pair going forward)

`GenerateRsaKeyPair.java`

2. On client side, to generate secret key at runtime, encrypt sensitive string using secret key, and encrypt secret key using public key, this class will help you:

`ClientEncrypt.java`

3. Upon receiving the encrypted text and encrypted secret key on server side, the server will decrypt the encrypted secret key using private key. Afterwards, the server will retrieve the original text transmitted by the client by decrypting the encrypted text using secret key (decrypted). The following class will help you in the same:

`ServerDecrypt.java`

# Encryption for browser

- AES https://www.npmjs.com/package/crypto-js
- RSA https://github.com/wwwtyro/cryptico

# Encryption for iOS

https://github.com/dmitrykolesnikovich/encryption-sample-project-ios

