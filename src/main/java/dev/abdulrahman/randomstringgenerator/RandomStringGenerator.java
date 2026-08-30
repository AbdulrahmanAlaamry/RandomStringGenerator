package dev.abdulrahman.randomstringgenerator;

import java.security.SecureRandom;
import java.util.*;
import java.util.stream.Stream;

public class RandomStringGenerator {
    private final int length;
    private final char[] alphabet;
    private final Random random;
    private final String prefix, suffix;
    private final double permutations;

    private RandomStringGenerator(Builder builder) {
        this.length = builder.length;
        this.alphabet = builder.alphabet;
        this.random = builder.random;
        this.prefix = builder.prefix;
        this.suffix = builder.suffix;
        this.permutations = Math.pow(alphabet.length, this.length);
    }

    public static class Builder {
        private int length;
        private char[] alphabet;
        private final Random random;
        private String prefix, suffix;

        public Builder() {
            this(new SecureRandom());
        }

        public Builder(Random random) {
            this.length = 5; // Default length of 5
            this.alphabet = getAlphabetForCase(CharacterPool.ALPHANUMERIC_MIXEDCASE); // Mixed-case alphanumeric alphabet by default
            this.random = random;
            this.prefix = "";
            this.suffix = "";
        }

        public Builder length(int length) {
            if (length < 1 || length > 16) {
                throw new IllegalArgumentException("Size must be between 1 and 16");
            }
            this.length = length;
            return this;
        }

        public Builder pool(CharacterPool characterPool) {
            this.alphabet = getAlphabetForCase(characterPool);
            return this;
        }

        public Builder customPool(char[] pool) {
            this.alphabet = pool;
            return this;
        }

        public Builder customPool(String pool) {
            this.alphabet = pool.toCharArray();
            return this;
        }

        public Builder prefix(String prefix) {
            this.prefix = prefix;
            return this;
        }

        public Builder suffix(String suffix) {
            this.suffix = suffix;
            return this;
        }

        public RandomStringGenerator build() {
            return new RandomStringGenerator(this);
        }
    }

    private static char[] getAlphabetForCase(CharacterPool characterPool) {
        return switch (characterPool) {
            case UPPERCASE -> "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
            case LOWERCASE -> "abcdefghijklmnopqrstuvwxyz".toCharArray();
            case MIXEDCASE -> "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".toCharArray();
            case HEXADECIMAL -> "0123456789ABCDEF".toCharArray();
            case ALPHANUMERIC_UPPERCASE -> "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
            case ALPHANUMERIC_LOWERCASE -> "abcdefghijklmnopqrstuvwxyz0123456789".toCharArray();
            case ALPHANUMERIC_MIXEDCASE -> "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789".toCharArray();
            case URL_SAFE -> "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_".toCharArray();
            case BASE58 -> "ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz123456789".toCharArray();
        };
    }

    private char randomChar() {
        int randomIndex = random.nextInt(alphabet.length);
        return alphabet[randomIndex];
    }

    public String generate() {
        return generate(length);
    }

    public String generate(int overrideLength) {
        StringBuilder sb = new StringBuilder(prefix.length() + overrideLength + suffix.length());
        sb.append(prefix);
        for (int i = 0; i < overrideLength; i++) {
            sb.append(randomChar());
        }
        sb.append(suffix);
        return sb.toString();
    }

    public List<String> generateImmutableList() {
        return generateImmutableList(8);
    }

    public List<String> generateImmutableList(int size) {
        Collection<String> list = new ArrayList<>(size);
        populateCollection(list, size);
        return List.copyOf(list);
    }

    public Set<String> generateImmutableSet() {
        return generateImmutableSet(8);
    }

    public Set<String> generateImmutableSet(int size) {
        Collection<String> set = new HashSet<>(size);
        populateCollection(set, size);
        return Set.copyOf(set);
    }

    public Stream<String> generateStream() {
        return generateStream(8);
    }

    public Stream<String> generateStream(int size) {
        return Stream.generate(this::generate).limit(size);
    }

    private void populateCollection(Collection<String> collection, int size) {
        if (size < 1 || size > permutations) {
            throw new IllegalArgumentException("Size parameter must be between 1 " +
                    "and the number of possible permutations");
        }

        while (collection.size() != size) {
            collection.add(generate());
        }
    }
}
