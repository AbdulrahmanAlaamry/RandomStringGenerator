package dev.abdulrahman.randomstringgenerator;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class RandomStringGeneratorTest {

    @Test
    void testDefaultBuilder() {
        RandomStringGenerator generator = new RandomStringGenerator.Builder().build();
        String result = generator.generate();
        assertNotNull(result);
        assertEquals(5, result.length());
    }

    @Test
    void testBuilderLength() {
        RandomStringGenerator generator = new RandomStringGenerator.Builder().length(10).build();
        String result = generator.generate();
        assertEquals(10, result.length());
    }

    @Test
    void testBuilderLengthValidation() {
        IllegalArgumentException exceptionTooSmall = assertThrows(IllegalArgumentException.class,
                () -> new RandomStringGenerator.Builder().length(0));
        assertEquals("Size must be between 1 and 16", exceptionTooSmall.getMessage());

        IllegalArgumentException exceptionTooLarge = assertThrows(IllegalArgumentException.class,
                () -> new RandomStringGenerator.Builder().length(17));
        assertEquals("Size must be between 1 and 16", exceptionTooLarge.getMessage());
    }

    @ParameterizedTest
    @EnumSource(CharacterPool.class)
    void testAllCharacterPools(CharacterPool pool) {
        RandomStringGenerator generator = new RandomStringGenerator.Builder()
                .pool(pool)
                .length(10)
                .build();
        String result = generator.generate();
        assertEquals(10, result.length());
        assertFalse(result.isEmpty());
    }

    @Test
    void testCustomPoolCharArray() {
        char[] pool = {'a', 'b', 'c'};
        RandomStringGenerator generator = new RandomStringGenerator.Builder()
                .customPool(pool)
                .length(10)
                .build();
        String result = generator.generate();
        assertTrue(result.matches("^[abc]{10}$"));
    }

    @Test
    void testCustomPoolString() {
        String pool = "xyz";
        RandomStringGenerator generator = new RandomStringGenerator.Builder()
                .customPool(pool)
                .length(10)
                .build();
        String result = generator.generate();
        assertTrue(result.matches("^[xyz]{10}$"));
    }

    @Test
    void testPrefixAndSuffix() {
        RandomStringGenerator generator = new RandomStringGenerator.Builder()
                .prefix("PRE-")
                .suffix("-SUF")
                .length(5)
                .build();
        String result = generator.generate();
        assertTrue(result.startsWith("PRE-"));
        assertTrue(result.endsWith("-SUF"));
        assertEquals(4 + 5 + 4, result.length());
    }

    @Test
    void testGenerateOverrideLength() {
        RandomStringGenerator generator = new RandomStringGenerator.Builder()
                .length(5)
                .build();
        String result = generator.generate(10);
        assertEquals(10, result.length());
    }
    
    @Test
    void testGenerateOverrideLengthWithPrefixSuffix() {
        RandomStringGenerator generator = new RandomStringGenerator.Builder()
                .prefix("A")
                .suffix("Z")
                .length(5)
                .build();
        String result = generator.generate(10);
        assertEquals(1 + 10 + 1, result.length());
        assertTrue(result.startsWith("A"));
        assertTrue(result.endsWith("Z"));
    }

    @Test
    void testGenerateImmutableList() {
        RandomStringGenerator generator = new RandomStringGenerator.Builder().length(5).build();
        List<String> list = generator.generateImmutableList();
        assertEquals(8, list.size());
        assertThrows(UnsupportedOperationException.class, () -> list.add("test"));
    }

    @Test
    void testGenerateImmutableListWithSize() {
        RandomStringGenerator generator = new RandomStringGenerator.Builder().length(5).build();
        List<String> list = generator.generateImmutableList(12);
        assertEquals(12, list.size());
        assertThrows(UnsupportedOperationException.class, () -> list.add("test"));
    }

    @Test
    void testGenerateImmutableSet() {
        RandomStringGenerator generator = new RandomStringGenerator.Builder().length(5).build();
        Set<String> set = generator.generateImmutableSet();
        assertEquals(8, set.size());
        assertThrows(UnsupportedOperationException.class, () -> set.add("test"));
    }

    @Test
    void testGenerateImmutableSetWithSize() {
        RandomStringGenerator generator = new RandomStringGenerator.Builder().length(5).build();
        Set<String> set = generator.generateImmutableSet(15);
        assertEquals(15, set.size());
        assertThrows(UnsupportedOperationException.class, () -> set.add("test"));
    }

    @Test
    void testGenerateStream() {
        RandomStringGenerator generator = new RandomStringGenerator.Builder().length(5).build();
        Stream<String> stream = generator.generateStream();
        List<String> result = stream.limit(8).collect(Collectors.toList());
        assertEquals(8, result.size());
    }

    @Test
    void testGenerateStreamWithSize() {
        RandomStringGenerator generator = new RandomStringGenerator.Builder().length(5).build();
        Stream<String> stream = generator.generateStream(10);
        List<String> result = stream.collect(Collectors.toList());
        assertEquals(10, result.size());
    }

    @Test
    void testPopulateCollectionValidation() {
        RandomStringGenerator generator = new RandomStringGenerator.Builder()
                .customPool("A")
                .length(1)
                .build(); // permutations = 1^1 = 1

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> generator.generateImmutableList(2));
        assertTrue(exception.getMessage().contains("Size parameter must be between 1 and the number of possible permutations"));
    }

    @Test
    void testDeterminismWithSeededRandom() {
        Random random1 = new Random(12345);
        RandomStringGenerator generator1 = new RandomStringGenerator.Builder(random1).length(10).build();
        String result1 = generator1.generate();

        Random random2 = new Random(12345);
        RandomStringGenerator generator2 = new RandomStringGenerator.Builder(random2).length(10).build();
        String result2 = generator2.generate();

        assertEquals(result1, result2);
    }
}
