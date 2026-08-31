# RandomStringGenerator

[![Java Version](https://img.shields.io/badge/Java-17%2B-007396?style=flat-square&logo=openjdk&logoColor=white)](https://openjdk.org/)
[![License: MIT](https://img.shields.io/badge/License-MIT-blue.svg?style=flat-square)](LICENSE)
[![Build Status](https://img.shields.io/github/actions/workflow/status/AbdulrahmanAlaamry/RandomStringGenerator/ci.yml?branch=master&style=flat-square&label=Build)](https://github.com/AbdulrahmanAlaamry/RandomStringGenerator/actions/workflows/ci.yml)

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
        .length(4)
        .build();

// Generate an unmodifiable set of unique tokens
Set<String> uniqueTokens = generator.generateImmutableSet(3); // e.g., ["7kM9", "B3xT", "8wLc"]

// Stream generation: assembling a formatted multi-segment license key
String licenseKey = generator.generateStream(4)
        .collect(Collectors.joining("-")); // e.g., "7kM9-B3xT-8wLc-2vPq"
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
