package dev.abdulrahman.randomstringgenerator;

class Validator {
    static void validateStringLength(int length) {
        if (length < 1) {
            throw new IllegalArgumentException("Length parameter must be at least 1");
        }
    }

    static void validateSequenceSize(int size) {
        if (size < 1) {
            throw new IllegalArgumentException("Size parameter must be at least 1");
        }
    }

    static void validateSetPermutations(int size, double permutations) {
        if (size > permutations) {
            throw new IllegalArgumentException("Cannot generate more unique strings than possible permutations");
        }
    }

    static void validateCustomPool(char[] pool) {
        if (pool == null || pool.length == 0) {
            throw new IllegalArgumentException("Alphabet pool cannot be null or empty");
        }
    }

    static void validateCustomPool(String pool) {
        if (pool == null || pool.isEmpty()) {
            throw new IllegalArgumentException("Alphabet pool cannot be null or empty");
        }
    }
}
