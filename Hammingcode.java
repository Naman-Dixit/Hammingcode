import java.util.Scanner;

public class HammingCode {
    // Method to calculate the number of redundant bits needed
    private static int calculateRedundantBits(int m) {
        int r = 0;
        while (Math.pow(2, r) < (m + r + 1)) {
            r++;
        }
        return r;
    }

    // Method to position redundant bits in the data
    private static int[] insertRedundantBits(int[] data, int r) {
        int[] hammingCode = new int[data.length + r];
        int j = 0;

        for (int i = 0; i < hammingCode.length; i++) {
            if (Math.pow(2, j) - 1 == i) {
                // Position for redundant bit
                hammingCode[i] = 0;
                j++;
            } else {
                hammingCode[i] = data[i - j];
            }
        }
        return hammingCode;
    }

    // Method to calculate parity bits
    private static void calculateParityBits(int[] hammingCode, int r) {
        for (int i = 0; i < r; i++) {
            int parityBitPosition = (int) Math.pow(2, i) - 1;
            int parity = 0;

            for (int j = parityBitPosition; j < hammingCode.length; j += (parityBitPosition + 1) * 2) {
                for (int k = j; k < j + parityBitPosition + 1 && k < hammingCode.length; k++) {
                    parity ^= hammingCode[k];
                }
            }

            hammingCode[parityBitPosition] = parity;
        }
    }

    // Method to detect and correct errors
    private static int detectAndCorrectError(int[] hammingCode, int r) {
        int errorPosition = 0;

        for (int i = 0; i < r; i++) {
            int parityBitPosition = (int) Math.pow(2, i) - 1;
            int parity = 0;

            for (int j = parityBitPosition; j < hammingCode.length; j += (parityBitPosition + 1) * 2) {
                for (int k = j; k < j + parityBitPosition + 1 && k < hammingCode.length; k++) {
                    parity ^= hammingCode[k];
                }
            }

            if (parity != 0) {
                errorPosition += parityBitPosition + 1;
            }
        }

        if (errorPosition != 0) {
            hammingCode[errorPosition - 1] ^= 1; // Correct the error
        }

        return errorPosition;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the number of data bits: ");
        int m = scanner.nextInt();

        int[] data = new int[m];
        System.out.print("Enter the data bits (space-separated): ");
        for (int i = 0; i < m; i++) {
            data[i] = scanner.nextInt();
        }

        int r = calculateRedundantBits(m);
        int[] hammingCode = insertRedundantBits(data, r);

        calculateParityBits(hammingCode, r);

        System.out.println("Hamming code after adding parity bits: ");
        for (int bit : hammingCode) {
            System.out.print(bit + " ");
        }
        System.out.println();

        System.out.print("Introduce a 1-based error position (or 0 for no error): ");
        int errorPosition = scanner.nextInt();
        if (errorPosition != 0) {
            hammingCode[errorPosition - 1] ^= 1; // Introduce an error
        }

        System.out.println("Received Hamming code: ");
        for (int bit : hammingCode) {
            System.out.print(bit + " ");
        }
        System.out.println();

        int detectedErrorPosition = detectAndCorrectError(hammingCode, r);
        if (detectedErrorPosition != 0) {
            System.out.println("Error detected and corrected at position: " + detectedErrorPosition);
        } else {
            System.out.println("No error detected.");
        }

        System.out.println("Corrected Hamming code: ");
        for (int bit : hammingCode) {
            System.out.print(bit + " ");
        }
        System.out.println();

        scanner.close();
    }
}
