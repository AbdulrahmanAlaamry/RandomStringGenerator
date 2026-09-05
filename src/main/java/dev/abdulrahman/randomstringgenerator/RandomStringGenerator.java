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
 * <b>Terminology:</b>
 * <ul>
 *     <li><b>Length:</b> Refers to the number of characters in a generated String.</li>
 *     <li><b>Sequence:</b> An umbrella term used in this API to describe any grouped output of generated strings (e.g., List, Set, or Stream).</li>
 *     <li><b>Size:</b> Refers to the number of elements in a generated sequence.</li>
 * </ul>
 * <p>
 * Instances of this class are immutable and thread-safe, assuming the provided
 * {@link Random} instance is also thread-safe (like {@link SecureRandom}).
 */
public class RandomStringGenerator {
    /** The default number of characters in a generated string. */
    private static final int DEFAULT_STRING_LENGTH = 16;
    
    /** The default number of strings generated when requesting a sequence. */
    private static final int DEFAULT_SEQUENCE_SIZE = 10;

    private final int length, interval;
    private final char[] alphabet;
    private final Random random;
    private final String prefix, suffix;
    private final char delimiter;

    private RandomStringGenerator(Builder builder) {
        this.length = builder.length;
        this.alphabet = builder.alphabet;
        this.random = builder.random;
        this.prefix = builder.prefix;
        this.suffix = builder.suffix;
        this.interval = builder.interval;
        this.delimiter = builder.separator;
    }

    /**
     * Builder for constructing {@link RandomStringGenerator} instances.
     */
    public static class Builder {
        private int length, interval;
        private char[] alphabet;
        private final Random random;
        private String prefix, suffix;
        private char separator;

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
            this.length = DEFAULT_STRING_LENGTH;
            this.alphabet = getAlphabetForCase(CharacterPool.ALPHANUMERIC_MIXEDCASE);
            this.random = Objects.requireNonNull(random, "Random instance cannot be null");
            this.prefix = "";
            this.suffix = "";
            this.separator = '\0';
            this.interval = 0;
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
         * @throws NullPointerException if characterPool is null
         */
        public Builder pool(CharacterPool characterPool) {
            Objects.requireNonNull(characterPool, "Character pool cannot be null");
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
         * Sets a delimiter to be inserted at regular intervals within the generated string.
         *
         * @param separator the character to insert as a delimiter
         * @param interval  the number of characters between each delimiter
         * @return this builder
         * @throws IllegalArgumentException if the interval is negative
         */
        public Builder delimiter(char separator, int interval) {
            Validator.validateDelimiterInterval(interval);
            this.interval = interval;
            this.separator = separator;
            return this;
        }

        /**
         * Excludes specific characters from the currently configured alphabet pool.
         * <p>
         * <b>Note:</b> Because this method filters the currently configured alphabet, 
         * you must call this method <i>after</i> calling {@link #pool(CharacterPool)} 
         * or {@link #customPool(String)}. If you call this method before setting a pool, 
         * the exclusion will be overwritten.
         *
         * @param characters the characters to exclude
         * @return this builder
         * @throws IllegalArgumentException if excluding the characters results in an empty pool
         */
        public Builder exclude(char... characters) {
            if (characters == null || characters.length == 0) return this;

            String excludeStr = new String(characters);
            StringBuilder updatedAlphabet = new StringBuilder();

            for (char c : this.alphabet) {
                if (excludeStr.indexOf(c) == -1) {
                    updatedAlphabet.append(c);
                }
            }

            char[] newAlphabet = updatedAlphabet.toString().toCharArray();
            Validator.validateCustomPool(newAlphabet);
            
            this.alphabet = newAlphabet;
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
        int capacity = computeStringBuilderCapacity(overrideLength);
        StringBuilder sb = new StringBuilder(capacity);
        sb.append(prefix);
        for (int i = 0; i < overrideLength; i++) {
            if (isValidInterval(i) && isAtInterval(i)) {
                sb.append(delimiter);
            }
            sb.append(randomChar());
        }
        sb.append(suffix);
        return sb.toString();
    }

    private int computeStringBuilderCapacity(int overrideLength) {
        int delimiterCount = (interval > 0) ? (overrideLength - 1) / interval : 0;
        return prefix.length() + overrideLength + suffix.length() + delimiterCount;
    }

    private boolean isValidInterval(int iteration) {
        return iteration != 0 && interval != 0;
    }

    private boolean isAtInterval(int iteration) {
        return iteration % interval == 0;
    }

    /**
     * Generates an immutable list of 10 random strings.
     *
     * @return an unmodifiable list of generated strings
     */
    public List<String> generateImmutableList() {
        return generateImmutableList(DEFAULT_SEQUENCE_SIZE);
    }

    /**
     * Generates an immutable list of random strings.
     *
     * @param size the number of strings to generate
     * @return an unmodifiable list of generated strings
     * @throws IllegalArgumentException if size is less than 1
     */
    public List<String> generateImmutableList(int size) {
        return generateImmutableList(size, length);
    }

    /**
     * Generates an immutable list of random strings overriding the default string length.
     *
     * @param size           the number of strings to generate
     * @param overrideLength the length of the random portion of each string
     * @return an unmodifiable list of generated strings
     * @throws IllegalArgumentException if size or overrideLength is less than 1
     */
    public List<String> generateImmutableList(int size, int overrideLength) {
        Validator.validateStringLength(overrideLength);
        return List.copyOf(
                generateCollection(
                        ArrayList::new,
                        size,
                        overrideLength
                )
        );
    }

    /**
     * Generates an immutable set of 10 unique random strings.
     *
     * @return an unmodifiable set of unique generated strings
     */
    public Set<String> generateImmutableSet() {
        return generateImmutableSet(DEFAULT_SEQUENCE_SIZE);
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
        return generateImmutableSet(size, length);
    }

    /**
     * Generates an immutable set of unique random strings overriding the default string length.
     *
     * @param size           the number of unique strings to generate
     * @param overrideLength the length of the random portion of each string
     * @return an unmodifiable set of unique generated strings
     * @throws IllegalArgumentException if size or overrideLength is less than 1, or if the requested
     *                                  size exceeds the total number of possible unique permutations
     */
    public Set<String> generateImmutableSet(int size, int overrideLength) {
        Validator.validateStringLength(overrideLength);
        double overridePermutations = Math.pow(alphabet.length, overrideLength);
        Validator.validateSetPermutations(size, overridePermutations);
        return Set.copyOf(
                generateCollection(
                        HashSet::new,
                        size,
                        overrideLength
                )
        );
    }

    /**
     * Creates a stream of 10 randomly generated strings.
     *
     * @return a stream containing 10 generated strings
     */
    public Stream<String> generateStream() {
        return generateStream(DEFAULT_SEQUENCE_SIZE);
    }

    /**
     * Creates a stream of randomly generated strings.
     *
     * @param size the number of strings the stream will emit
     * @return a stream containing the specified number of generated strings
     */
    public Stream<String> generateStream(int size) {
        return generateStream(size, length);
    }

    /**
     * Creates a stream of randomly generated strings overriding the default string length.
     *
     * @param size           the number of strings the stream will emit
     * @param overrideLength the length of the random portion of each string
     * @return a stream containing the specified number of generated strings
     * @throws IllegalArgumentException if size or overrideLength is less than 1
     */
    public Stream<String> generateStream(int size, int overrideLength) {
        Validator.validateSequenceSize(size);
        Validator.validateStringLength(overrideLength);
        return Stream.generate(() -> generate(overrideLength)).limit(size);
    }

    private <T extends Collection<String>> T generateCollection(IntFunction<T> collectionFactory,
                                                                int size, int overrideLength) {
        Validator.validateSequenceSize(size);
        T collection = collectionFactory.apply(size); // Instantiates the ArrayList or HashSet safely
        while (collection.size() != size) {
            collection.add(generate(overrideLength));
        }
        return collection;
    }
}
