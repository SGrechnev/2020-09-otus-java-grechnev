package ru.otus.atm;

class ATMHelper {
    int gcdByEuclidsAlgorithm(int n1, int n2) {
        if (n2 == 0) {
            if (n1 == 0) {
                throw new IllegalArgumentException("gcd(0,0) is undefined");
            }
            return n1;
        }
        return gcdByEuclidsAlgorithm(n2, n1 % n2);
    }

    int gcdByEuclidsAlgorithm(int[] numbers) {
        int resultGcd = 0;
        for (int number : numbers) {
            resultGcd = gcdByEuclidsAlgorithm(number, resultGcd);
        }
        return resultGcd;
    }
}
