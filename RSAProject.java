package DMath;
public class RSAProject {


	public int[] LCG(int seed, int quantity) {
		final int A = 1664525;
		final int C = 1013904223;
		final int M = (int) Math.pow(2, 32);

		int[] result = new int[quantity];
		int current = seed;

		for (int i = 0; i < quantity; i++) {
			current = (A * current + C) % M;
			result[i] = current;
		}
		return result;
	}

	public int[] String_to_intArray(String s) {
		s = s.toLowerCase();
		int[] arr = new int[s.length()];
		for (int i = 0; i < s.length(); i++) {
			char ch = s.charAt(i);
			arr[i] = (ch - 'a') + 1;
		}
		return arr;
	}

	public String IntArray_to_String(int[] arr) {
		String result ="";
		for (int n : arr) {
			char ch = (char) ('a' + (n - 1));
			result= result+ch;
		}
		return result;
	}


	public boolean trialDivisionUpToBound(long n, int bound) {
		if (n < 2) return false;
		for (long i = 2; i <= bound; i++) {
			if (i * i > n) break;
			if (n % i == 0) return false;
		}
		return true;
	}

	public boolean fermatTest(long n, int[] bases) {
		if (n <= 2) return n == 2;
		for (int a : bases) {
			long result = modularExponentiation(a, n - 1, n);
			if (result != 1) return false;
		}
		return true;
	}

	public boolean isProbablePrime(long n) {
		int bound = (int)Math.min(Math.sqrt(n), 10000);
		if (!trialDivisionUpToBound(n, bound)) return false;
		int[] bases = {2, 3, 5};
		if (!fermatTest(n, bases)) return false;
		return true;
	}


	public long modularExponentiation(long base, long exponent, long modulus) {
		long result = 1;
		base = base % modulus;

		while (exponent > 0) {
			if (exponent % 2 == 1) {
				result = (result * base) % modulus;
			}
			exponent = exponent >> 1;
		base = (base * base) % modulus;
		}
		return result;
	}

	public long extendedEuclideanAlgorithm(long e, long m) {
		long[] coeffs = extendedGCD(e, m);
		long gcd = coeffs[0];
		long x = coeffs[1];

		if (gcd != 1) {
			throw new ArithmeticException("Modular inverse does not exist");
		}
		return (x % m + m) % m;
	}

	private long[] extendedGCD(long a, long b) {
		if (b == 0) {
			return new long[]{a, 1, 0};
		}
		long[] vals = extendedGCD(b, a % b);
		long gcd = vals[0];
		long x = vals[2];
		long y = vals[1] - (a / b) * vals[2];
		return new long[]{gcd, x, y};
	}


	public KeyPair generateKeys() {
		int seed1 = 1111;
		int seed2 = 2222;
		long p = getPrimeFromLCG(seed1);
		long q = getPrimeFromLCG(seed2);

		while (q == p) {
			seed2 += 100;     
			q = getPrimeFromLCG(seed2);
		}

		long n = p * q;
		long phi = (p - 1) * (q - 1);
		long e = 3;

		while (gcd(e, phi) != 1) {
			e++;
		}

		long d = extendedEuclideanAlgorithm(e, phi);
		d = d % phi;
		if (d < 0) {
			d += phi;
		}

		return new KeyPair(n, e, d);
	}
	
	private long getPrimeFromLCG(int seed) {
		int[] nums = LCG(seed, 50);

		for (int value : nums) {
			long candidate = Math.abs((long) value);

			candidate = candidate % 1000;  //0-999

			if (candidate < 100) {
				candidate += 100;          
			}            
			if (candidate % 2 == 0) candidate++;

			if (isProbablePrime(candidate)) {
				return candidate;
			}
		}

		long candidate = 101;
		while (!isProbablePrime(candidate)) {
			candidate += 2;
		}
		return candidate;
	}


	private long gcd(long e, long phi) {
		while (phi != 0) {
			long temp = phi;
			phi = e % phi;
			e = temp;
		}
		return e;
	}

	public long[] encrypt(String message, int e, int n) {
		int[] index = String_to_intArray(message);
		long[] encryptedMessage = new long[index.length];
		for (int i = 0; i < index.length; i++) {
			System.out.println("RSA calling modularExponentiation for m = " + index[i]);
			long c = modularExponentiation(index[i], e, n);
			encryptedMessage[i] = c;
		}
		return encryptedMessage;
	}

	public String decrypt(long[] ciphertext, int d, int n) {
		int[] decoded = new int[ciphertext.length];
		for (int i = 0; i < ciphertext.length; i++) {
			long m = modularExponentiation(ciphertext[i], d, n);
			decoded[i] = (int) m;
		}
		return IntArray_to_String(decoded);
	}

	// ===== KeyPair Class

	public static final class KeyPair {
		public final long n, e, d;
		
		public KeyPair(long n, long e, long d) {
			this.n = n; this.e = e; this.d = d;
		}
	}

	public static void main(String[] args) {
	    RSAProject rsa = new RSAProject();

	    System.out.println("===== SAMPLE RUN OF ALL METHODS =====\n");

	    // 1. LCG
	    System.out.println("1. LCG GENERATOR:");
	    int[] lcgValues = rsa.LCG(5, 5);
	    System.out.print("LCG(5, 5) output: ");
	    for (int v : lcgValues) {
	        System.out.print(v + " ");
	    }
	    System.out.println("\n");

	    // 2. trialDivisionUpToBound
	    System.out.println("2. TRIAL DIVISION:");
	    long testNum = 97;
	    int bound = (int)Math.min(Math.sqrt(testNum), 10000);
	    boolean trial = rsa.trialDivisionUpToBound(testNum, bound);
	    System.out.println("trialDivisionUpToBound(97) → " + trial + "\n");

	    // 3. fermatTest 
	    System.out.println("3. FERMAT TEST:");
	    int[] bases = {2, 3, 5};
	    boolean fermat = rsa.fermatTest(testNum, bases);
	    System.out.println("fermatTest(97, {2,3,5}) → " + fermat + "\n");

	    // 4. isProbablePrime 
	    System.out.println("4. isProbablePrime:");
	    boolean probablePrime = rsa.isProbablePrime(testNum);
	    System.out.println("isProbablePrime(97) → " + probablePrime + "\n");

	    // 5. modularExponentiation 
	    System.out.println("5. MODULAR EXPONENTIATION:");
	    long modExp = rsa.modularExponentiation(7, 128, 19);
	    System.out.println("modularExponentiation(7,128,19) → " + modExp + "\n");

	    // 6. extendedEuclideanAlgorithm 
	    System.out.println("6. EXTENDED EUCLIDEAN ALGORITHM:");
	    long inverse = rsa.extendedEuclideanAlgorithm(7, 40);
	    System.out.println("extendedEuclideanAlgorithm(7,40) → " + inverse + "\n");

	    // 7. RSA KEY GENERATION 
	    System.out.println("7. RSA KEY GENERATION:");
	    KeyPair keys = rsa.generateKeys();
	    System.out.println("Public Key (n,e): (" + keys.n + ", " + keys.e + ")");
	    System.out.println("Private Key d: " + keys.d + "\n");

	    // 8. String_to_intArray 
	    System.out.println("8. STRING → INT ARRAY:");
	    String message = "hello";
	    int[] intArray = rsa.String_to_intArray(message);
	    System.out.print("String_to_intArray(\"hello\") → ");
	    for (int v : intArray) {
	        System.out.print(v + " ");
	    }
	    System.out.println("\n");

	    // 9. IntArray_to_String 
	    System.out.println("9. INT ARRAY → STRING:");
	    String rebuilt = rsa.IntArray_to_String(intArray);
	    System.out.print("IntArray_to_String([ ");
	    for (int v : intArray) System.out.print(v + " ");
	    System.out.println("]) → \"" + rebuilt + "\"\n");

	    // 10. ENCRYPTION 
	    System.out.println("10. ENCRYPTION:");
	    long[] ciphertext = rsa.encrypt(message, (int)keys.e, (int)keys.n);
	    System.out.println("Encrypting message: \"" + message + "\"");

	    System.out.print("Ciphertext values: ");
	    for (long c : ciphertext) {
	        System.out.print(c + " ");
	    }
	    System.out.println("\n");

	    // 11. DECRYPTION 
	    System.out.println("11. DECRYPTION:");
	    System.out.print("Decrypting ciphertext: ");
	    for (long c : ciphertext) {
	        System.out.print(c + " ");
	    }
	    System.out.println();

	    String decrypted = rsa.decrypt(ciphertext, (int)keys.d, (int)keys.n);
	    System.out.println("Decrypted message → \"" + decrypted + "\"\n");

	}


	}
