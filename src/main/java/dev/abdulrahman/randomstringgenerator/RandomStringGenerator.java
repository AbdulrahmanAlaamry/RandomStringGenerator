package dev.abdulrahman.randomstringgenerator;

import java.security.SecureRandom;
import java.util.*;
import java.util.function.IntFunction;
import java.util.stream.Stream;

/**
 * A robust, highly configurable utility for generating random strings.
 * <p>
 * This class uses a Builder pattern to allow configuration of string length,
 * character pools (alphabets), prefixes, suffixes, and the underlying
 * source of randomness.
 * <p>
 * Instances of this class are immutable and thread-safe, assuming the provided
 * {@link Random} instance is also thread-safe (like {@link SecureRandom}).
 */
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

    /**
     * Builder for constructing {@link RandomStringGenerator} instances.
     */
    public static class Builder {
        private int length;
        private char[] alphabet;
        private final Random random;
        private String prefix, suffix;

        /**
         * Creates a new Builder using a cryptographically strong {@link SecureRandom} instance.
         */
        public Builder() {
            this(new SecureRandom());
        }

        /**
         * Creates a new Builder with a custom {@link Random} instance.
         *
         * @param random the random number generator to use
         * @throws NullPointerException if random is null
         */
        public Builder(Random random) {
            this.length = 16;
            this.alphabet = getAlphabetForCase(CharacterPool.ALPHANUMERIC_MIXEDCASE);
            this.random = Objects.requireNonNull(random, "Random instance cannot be null");
            this.prefix = "";
            this.suffix = "";
        }

        /**
         * Sets the default length of the generated strings.
         *
         * @param length the length of the string (excluding prefix and suffix)
         * @return this builder
         * @throws IllegalArgumentException if length is less than 1
         */
        public Builder length(int length) {
            Validator.validateStringLength(length);
            this.length = length;
            return this;
        }

        /**
         * Sets the alphabet pool to one of the standard {@link CharacterPool} presets.
         *
         * @param characterPool the preset character pool to use
         * @return this builder
         */
        public Builder pool(CharacterPool characterPool) {
            this.alphabet = getAlphabetForCase(characterPool);
            return this;
        }

        /**
         * Sets a custom alphabet pool using a character array.
         *
         * @param pool the character array to draw characters from
         * @return this builder
         * @throws IllegalArgumentException if the pool is null or empty
         */
        public Builder customPool(char[] pool) {
            Validator.validateCustomPool(pool);
            this.alphabet = pool;
            return this;
        }

        /**
         * Sets a custom alphabet pool using a string.
         *
         * @param pool the string containing the characters to draw from
         * @return this builder
         * @throws IllegalArgumentException if the pool is null or empty
         */
        public Builder customPool(String pool) {
            Validator.validateCustomPool(pool);
            this.alphabet = pool.toCharArray();
            return this;
        }

        /**
         * Sets a constant prefix to be prepended to all generated strings.
         *
         * @param prefix the prefix string (null is treated as empty string)
         * @return this builder
         */
        public Builder prefix(String prefix) {
            this.prefix = prefix == null ? "" : prefix;
            return this;
        }

        /**
         * Sets a constant suffix to be appended to all generated strings.
         *
         * @param suffix the suffix string (null is treated as empty string)
         * @return this builder
         */
        public Builder suffix(String suffix) {
            this.suffix = suffix == null ? "" : suffix;
            return this;
        }

        /**
         * Builds and returns a new {@link RandomStringGenerator} instance.
         *
         * @return a configured RandomStringGenerator
         */
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

    /**
     * Generates a single random string using the configured length.
     *
     * @return the generated string
     */
    public String generate() {
        return generate(length);
    }

    /**
     * Generates a single random string overriding the default length.
     *
     * @param overrideLength the length of the random portion of the string
     * @return the generated string
     * @throws IllegalArgumentException if overrideLength is less than 1
     */
    public String generate(int overrideLength) {
        Validator.validateStringLength(overrideLength);
        StringBuilder sb = new StringBuilder(prefix.length() + overrideLength + suffix.length());
        sb.append(prefix);
        for (int i = 0; i < overrideLength; i++) {
            sb.append(randomChar());
        }
        sb.append(suffix);
        return sb.toString();
    }

    /**
     * Generates an immutable list of 10 random strings.
     *
     * @return an unmodifiable list of generated strings
     */
    public List<String> generateImmutableList() {
        return generateImmutableList(10);
    }

    /**
     * Generates an immutable list of random strings.
     *
     * @param size the number of strings to generate
     * @return an unmodifiable list of generated strings
     * @throws IllegalArgumentException if size is less than 1
     */
    public List<String> generateImmutableList(int size) {
        return List.copyOf(
                generateCollection(
                        ArrayList::new,
                        size
                )
        );
    }

    /**
     * Generates an immutable set of 10 unique random strings.
     *
     * @return an unmodifiable set of unique generated strings
     */
    public Set<String> generateImmutableSet() {
        return generateImmutableSet(10);
    }

    /**
     * Generates an immutable set of unique random strings.
     *
     * @param size the number of unique strings to generate
     * @return an unmodifiable set of unique generated strings
     * @throws IllegalArgumentException if size is less than 1, or if the requested
     *                                  size exceeds the total number of possible unique permutations
     */
    public Set<String> generateImmutableSet(int size) {
        Validator.validateSetPermutations(size, permutations);
        return Set.copyOf(
                generateCollection(
                        HashSet::new,
                        size
                )
        );
    }

    /**
     * Creates a stream of 10 randomly generated strings.
     *
     * @return a stream containing 10 generated strings
     */
    public Stream<String> generateStream() {
        return generateStream(10);
    }

    /**
     * Creates a stream of randomly generated strings.
     *
     * @param size the number of strings the stream will emit
     * @return a stream containing the specified number of generated strings
     */
    public Stream<String> generateStream(int size) {
        Validator.validateSequenceSize(size);
        return Stream.generate(this::generate).limit(size);
    }

    private <T extends Collection<String>> T generateCollection(IntFunction<T> collectionFactory, int size) {
        Validator.validateSequenceSize(size);
        T collection = collectionFactory.apply(size); // Instantiates the ArrayList or HashSet safely
        while (collection.size() != size) {
            collection.add(generate());
        }
        return collection;
    }

    @Deprecated(forRemoval = true)
    private void populateCollection(Collection<String> collection, int size) {
        Validator.validateSequenceSize(size);
        while (collection.size() != size) {
            collection.add(generate());
        }
    }
}
