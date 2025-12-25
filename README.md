## RSA Cryptography Project

## Project Overview
This project implements the RSA cryptographic system using Java.  
The goal of the project is to demonstrate a clear understanding of number theory concepts used in RSA, including prime generation, modular arithmetic, encryption, and decryption.

The implementation avoids built-in cryptographic libraries and focuses on building each component manually to reflect the theoretical foundations of RSA.

---

## Core Functionality

### Key Generation
- Generates two distinct prime numbers using a Linear Congruential Generator (LCG)
- Filters primes using trial division and Fermat’s primality test
- Computes:
  - Modulus n = p × q
  - Euler’s totient φ(n)
  - Public exponent e such that gcd(e, φ) = 1
  - Private key d using the Extended Euclidean Algorithm

---

### Encryption
- Converts plaintext messages into integer arrays
- Applies RSA encryption formula:
  - c = m^e mod n
- Uses efficient modular exponentiation for computation

---

### Decryption
- Applies RSA decryption formula:
  - m = c^d mod n
- Converts decrypted integers back into the original plaintext message

---

## Algorithms Implemented
- Linear Congruential Generator (LCG)
- Trial Division Primality Test
- Fermat’s Primality Test
- Modular Exponentiation (Square-and-Multiply)
- Extended Euclidean Algorithm
- RSA Key Generation
- RSA Encryption and Decryption
- String to Integer Array Conversion
- Integer Array to String Conversion

---

## Technologies Used
- Java
- Discrete Mathematics concepts
- Modular arithmetic and number theory

---

## Notes
This project was developed as part of the CSC281 course to connect discrete mathematics concepts with practical cryptographic implementation and to provide a deeper understanding of how RSA works internally.
