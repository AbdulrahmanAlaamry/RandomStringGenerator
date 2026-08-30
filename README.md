# RandomStringGenerator

[![Java Version](https://img.shields.io/badge/Java-17%2B-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://openjdk.org/)
[![License](https://img.shields.io/badge/License-MIT-blue.svg?style=for-the-badge)](LICENSE)
[![Build Status](https://img.shields.io/badge/Build-Passing-brightgreen?style=for-the-badge)](#)

A zero-dependency, fluent Java utility designed for generating customizable random strings, unique identifiers, and mock data using `SecureRandom` by default.

---

## Features

- **Fluent Builder Pattern:** Clean and expressive object construction.
- **Cryptographically Secure:** Defaults to `SecureRandom` with support for custom PRNG instances.
- **Built-in Character Pools:** Pre-configured sets including URL-Safe, Base58, Hexadecimal, and Alphanumeric.
- **Batch & Stream Output:** Generate single strings, unique immutable sets, immutable lists, or Java Streams.
- **Prefixes & Suffixes:** Direct formatting integration for custom token formats.

---

## Quick Start

### 1. Basic Generation
```java
RandomStringGenerator generator = new RandomStringGenerator.Builder()
        .length(8)
        .build();

String token = generator.generate(); // e.g., "aB9xK2Lm"
```

### 2. Custom Pools & Formatting
```java
RandomStringGenerator orderIdGen = new RandomStringGenerator.Builder()
        .pool(CharacterPool.HEXADECIMAL)
        .length(6)
        .prefix("ORD-")
        .suffix("-2026")
        .build();

String orderId = orderIdGen.generate(); // e.g., "ORD-4F9A12-2026"
```

### 3. Custom Character Sets
```java
RandomStringGenerator pinGen = new RandomStringGenerator.Builder()
        .customPool("0123456789")
        .length(4)
        .build();

String pin = pinGen.generate(); // e.g., "4921"
```

### 4. Bulk Generation & Streams
```java
RandomStringGenerator generator = new RandomStringGenerator.Builder()
        .pool(CharacterPool.BASE58)
        .length(10)
        .build();

// Generate an unmodifiable set of 5 unique tokens
Set<String> uniqueTokens = generator.generateImmutableSet(5);

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

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
