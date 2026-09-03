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
        assertEquals(16, result.length());
    }

    @Test
    void testBuilderWithNullRandom() {
        NullPointerException exception = assertThrows(NullPointerException.class,
                () -> new RandomStringGenerator.Builder(null));
        assertEquals("Random instance cannot be null", exception.getMessage());
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
        assertEquals("Length parameter must be at least 1", exceptionTooSmall.getMessage());
        
        IllegalArgumentException exceptionNegative = assertThrows(IllegalArgumentException.class,
                () -> new RandomStringGenerator.Builder().length(-5));
        assertEquals("Length parameter must be at least 1", exceptionNegative.getMessage());
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
    void testCustomPoolValidation() {
        IllegalArgumentException exception1 = assertThrows(IllegalArgumentException.class,
                () -> new RandomStringGenerator.Builder().customPool((char[]) null));
        assertEquals("Alphabet pool cannot be null or empty", exception1.getMessage());

        IllegalArgumentException exception2 = assertThrows(IllegalArgumentException.class,
                () -> new RandomStringGenerator.Builder().customPool(new char[0]));
        assertEquals("Alphabet pool cannot be null or empty", exception2.getMessage());

        IllegalArgumentException exception3 = assertThrows(IllegalArgumentException.class,
                () -> new RandomStringGenerator.Builder().customPool((String) null));
        assertEquals("Alphabet pool cannot be null or empty", exception3.getMessage());

        IllegalArgumentException exception4 = assertThrows(IllegalArgumentException.class,
                () -> new RandomStringGenerator.Builder().customPool(""));
        assertEquals("Alphabet pool cannot be null or empty", exception4.getMessage());
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
    void testNullPrefixAndSuffix() {
        RandomStringGenerator generator = new RandomStringGenerator.Builder()
                .prefix(null)
                .suffix(null)
                .length(5)
                .build();
        String result = generator.generate();
        assertEquals(5, result.length());
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
    void testGenerateOverrideLengthValidation() {
        RandomStringGenerator generator = new RandomStringGenerator.Builder().build();
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> generator.generate(0));
        assertEquals("Length parameter must be at least 1", exception.getMessage());
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
        assertEquals(10, list.size());
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
        assertEquals(10, set.size());
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
    void testCollectionSizeValidation() {
        RandomStringGenerator generator = new RandomStringGenerator.Builder().build();
        
        IllegalArgumentException exception1 = assertThrows(IllegalArgumentException.class,
                () -> generator.generateImmutableList(-5));
        assertEquals("Size parameter must be at least 1", exception1.getMessage());

        IllegalArgumentException exception2 = assertThrows(IllegalArgumentException.class,
                () -> generator.generateImmutableSet(-5));
        assertEquals("Size parameter must be at least 1", exception2.getMessage());
    }

    @Test
    void testGenerateStream() {
        RandomStringGenerator generator = new RandomStringGenerator.Builder().length(5).build();
        Stream<String> stream = generator.generateStream();
        List<String> result = stream.collect(Collectors.toList());
        assertEquals(10, result.size());
    }

    @Test
    void testGenerateStreamWithSize() {
        RandomStringGenerator generator = new RandomStringGenerator.Builder().length(5).build();
        Stream<String> stream = generator.generateStream(15);
        List<String> result = stream.collect(Collectors.toList());
        assertEquals(15, result.size());
    }

    @Test
    void testGenerateStreamWithNegativeSize() {
        RandomStringGenerator generator = new RandomStringGenerator.Builder().length(5).build();
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
                () -> generator.generateStream(-5));
        assertEquals("Size parameter must be at least 1", exception.getMessage());
    }

    @Test
    void testPopulateCollectionValidation() {
        RandomStringGenerator generator = new RandomStringGenerator.Builder()
                .customPool("A")
                .length(1)
                .build(); // permutations = 1^1 = 1

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> generator.generateImmutableSet(2));
        assertTrue(exception.getMessage().contains("Cannot generate more unique strings than possible permutations"));
    }
    
    @Test
    void testPopulateCollectionValidationAllowsListDuplicates() {
        RandomStringGenerator generator = new RandomStringGenerator.Builder()
                .customPool("A")
                .length(1)
                .build(); // permutations = 1^1 = 1

        // This should NOT throw an exception because Lists allow duplicates
        List<String> list = generator.generateImmutableList(5);
        assertEquals(5, list.size());
    }

    @Test
    void testGenerateImmutableListWithOverrideLength() {
        RandomStringGenerator generator = new RandomStringGenerator.Builder().length(5).build();
        List<String> list = generator.generateImmutableList(10, 8);
        assertEquals(10, list.size());
        assertEquals(8, list.get(0).length());
    }

    @Test
    void testGenerateImmutableSetWithOverrideLength() {
        RandomStringGenerator generator = new RandomStringGenerator.Builder().length(5).build();
        Set<String> set = generator.generateImmutableSet(10, 8);
        assertEquals(10, set.size());
        assertEquals(8, set.iterator().next().length());
    }

    @Test
    void testGenerateStreamWithOverrideLength() {
        RandomStringGenerator generator = new RandomStringGenerator.Builder().length(5).build();
        Stream<String> stream = generator.generateStream(10, 8);
        List<String> list = stream.collect(Collectors.toList());
        assertEquals(10, list.size());
        assertEquals(8, list.get(0).length());
    }

    @Test
    void testEagerValidationForOverrideLength() {
        RandomStringGenerator generator = new RandomStringGenerator.Builder().build();

        IllegalArgumentException ex1 = assertThrows(IllegalArgumentException.class, 
            () -> generator.generateImmutableList(10, -1));
        assertEquals("Length parameter must be at least 1", ex1.getMessage());

        IllegalArgumentException ex2 = assertThrows(IllegalArgumentException.class, 
            () -> generator.generateImmutableSet(10, -1));
        assertEquals("Length parameter must be at least 1", ex2.getMessage());

        IllegalArgumentException ex3 = assertThrows(IllegalArgumentException.class, 
            () -> generator.generateStream(10, -1));
        assertEquals("Length parameter must be at least 1", ex3.getMessage());
    }

    @Test
    void testDelimiter() {
        RandomStringGenerator generator = new RandomStringGenerator.Builder()
                .length(5)
                .delimiter('-', 2)
                .build();
        String result = generator.generate();
        assertEquals(7, result.length());
        assertEquals('-', result.charAt(2));
        assertEquals('-', result.charAt(5));
        assertTrue(result.matches("^[a-zA-Z0-9]{2}-[a-zA-Z0-9]{2}-[a-zA-Z0-9]{1}$"));
    }

    @Test
    void testDelimiterWithIntervalOne() {
        RandomStringGenerator generator = new RandomStringGenerator.Builder()
                .length(3)
                .delimiter('-', 1)
                .build();
        String result = generator.generate();
        assertEquals(5, result.length());
        assertTrue(result.matches("^[a-zA-Z0-9]-[a-zA-Z0-9]-[a-zA-Z0-9]$"));
    }

    @Test
    void testDelimiterWithIntervalZero() {
        RandomStringGenerator generator = new RandomStringGenerator.Builder()
                .length(3)
                .delimiter('-', 0)
                .build();
        String result = generator.generate();
        assertEquals(3, result.length());
        assertFalse(result.contains("-"));
    }

    @Test
    void testDelimiterWithIntervalLargerThanLength() {
        RandomStringGenerator generator = new RandomStringGenerator.Builder()
                .length(3)
                .delimiter('-', 5)
                .build();
        String result = generator.generate();
        assertEquals(3, result.length());
        assertFalse(result.contains("-"));
    }

    @Test
    void testDelimiterValidation() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, 
            () -> new RandomStringGenerator.Builder().delimiter('-', -1));
        assertEquals("Interval parameter must be positive", ex.getMessage());
    }

    @Test
    void testExclude() {
        RandomStringGenerator generator = new RandomStringGenerator.Builder()
                .customPool("ABC")
                .exclude('C')
                .length(10)
                .build();
        String result = generator.generate();
        assertTrue(result.matches("^[AB]{10}$"));
    }

    @Test
    void testExcludeAllCharacters() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> new RandomStringGenerator.Builder().customPool("A").exclude('A'));
        assertEquals("Alphabet pool cannot be null or empty", ex.getMessage());
    }

    @Test
    void testExcludeCharactersNotInPool() {
        RandomStringGenerator generator = new RandomStringGenerator.Builder()
                .customPool("A")
                .exclude('B')
                .length(5)
                .build();
        String result = generator.generate();
        assertEquals("AAAAA", result);
    }

    @Test
    void testExcludeMultipleCalls() {
        RandomStringGenerator generator = new RandomStringGenerator.Builder()
                .customPool("ABC")
                .exclude('A')
                .exclude('B')
                .length(5)
                .build();
        String result = generator.generate();
        assertEquals("CCCCC", result);
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
