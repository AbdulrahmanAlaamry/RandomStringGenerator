# RandomStringGenerator

[![Java Version](https://img.shields.io/badge/Java-17%2B-007396?style=flat&logo=openjdk&logoColor=white)](https://openjdk.org/)
[![License: MIT](https://img.shields.io/badge/License-MIT-blue.svg?style=flat)](LICENSE)
[![Build Status](https://img.shields.io/github/actions/workflow/status/AbdulrahmanAlaamry/RandomStringGenerator/ci.yml?branch=master&style=flat&label=Build)](https://github.com/AbdulrahmanAlaamry/RandomStringGenerator/actions/workflows/ci.yml)
[![](https://jitpack.io/v/AbdulrahmanAlaamry/RandomStringGenerator.svg)](https://jitpack.io/#AbdulrahmanAlaamry/RandomStringGenerator)

A zero-dependency, fluent Java utility designed for generating customizable random strings, unique identifiers, formatted license keys, and mock data using `SecureRandom` by default.

---

## Features

- **Fluent Builder Pattern:** Clean, chainable API with fail-fast input and null validation.
- **Cryptographically Secure:** Defaults to `SecureRandom` with support for custom PRNG instances.
- **Built-in Character Pools:** Pre-configured sets including URL-Safe, Base58, Hexadecimal, and Alphanumeric.
- **Character Exclusion:** Strip confusing or unwanted characters from standard pools without writing custom alphabets.
- **Delimiters & Chunking:** Automatically segment generated strings at regular intervals for license and product keys.
- **Batch & Stream Output:** Generate single strings, unique immutable sets, immutable lists, or Java Streams with on-the-fly length overrides.
- **Prefixes & Suffixes:** Seamless prefix and suffix attachment with automatic null-safety.

---

## Quick Start

### 1. Basic Generation
```java
// Defaults to a 16-character mixed-case alphanumeric string
RandomStringGenerator generator = new RandomStringGenerator.Builder().build();

String token = generator.generate(); // e.g., "aB9xK2LmP4qR8vTw"
```

### 2. Delimiters & Formatting (License Keys)
```java
RandomStringGenerator keyGen = new RandomStringGenerator.Builder()
        .pool(CharacterPool.ALPHANUMERIC_UPPERCASE)
        .length(16)
        .delimiter('-', 4)
        .prefix("KEY-")
        .build();

String key = keyGen.generate(); // e.g., "KEY-ABCD-1234-XYZ9-8765"
```

### 3. Character Exclusion
```java
// Strip visually confusing characters from an existing pool
RandomStringGenerator cleanGen = new RandomStringGenerator.Builder()
        .pool(CharacterPool.ALPHANUMERIC_UPPERCASE)
        .exclude('O', '0', 'I', '1') // Must be called after defining the pool
        .length(8)
        .build();

String cleanCode = cleanGen.generate(); // e.g., "8K7X2B5V"
```

### 4. Custom Character Sets
```java
RandomStringGenerator pinGen = new RandomStringGenerator.Builder()
        .customPool("0123456789")
        .length(4)
        .build();

String pin = pinGen.generate(); // e.g., "4921"
```

### 5. Bulk Generation & Sequence Overrides
```java
RandomStringGenerator generator = new RandomStringGenerator.Builder()
        .pool(CharacterPool.BASE58)
        .length(6)
        .build();

// Generate an unmodifiable set of unique tokens using default length (6)
Set<String> uniqueTokens = generator.generateImmutableSet(5);

// Override configured length on-the-fly (generate 3 tokens of length 12)
List<String> longTokens = generator.generateImmutableList(3, 12);

// Stream generation
List<String> sortedKeys = generator.generateStream(5)
        .filter(s -> !Character.isDigit(s.charAt(0)))
        .sorted()
        .toList();
```

---

## Supported Character Pools

| CharacterPool Enum | Character Set | Use Case |
| :--- | :--- | :--- |
| `UPPERCASE` | `A-Z` | Voucher codes, Serial numbers |
| `LOWERCASE` | `a-z` | Readable slugs |
| `MIXEDCASE` | `A-Z`, `a-z` | Case-sensitive identifiers |
| `HEXADECIMAL` | `0-9`, `A-F` | Hashes, Color codes, Byte IDs |
| `ALPHANUMERIC_UPPERCASE` | `A-Z`, `0-9` | License keys, Gift codes |
| `ALPHANUMERIC_LOWERCASE` | `a-z`, `0-9` | Database keys, URL slugs |
| `ALPHANUMERIC_MIXEDCASE` | `A-Z`, `a-z`, `0-9` | Session tokens, General IDs |
| `URL_SAFE` | `A-Z`, `a-z`, `0-9`, `-_` | Web tokens, Filenames |
| `BASE58` | Bitcoin Base58 Alphabet | High readability (no `0`, `O`, `I`, `l`) |

---

## Installation

Please click on the JitPack badge located at the top of the README for an in-depth installation guide.

---

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
