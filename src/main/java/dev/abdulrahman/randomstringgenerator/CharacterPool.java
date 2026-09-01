package dev.abdulrahman.randomstringgenerator;

/**
 * Defines standard character alphabets used for generating random strings.
 */
public enum CharacterPool {
    /** A-Z (26 characters) */
    UPPERCASE,
    
    /** a-z (26 characters) */
    LOWERCASE,
    
    /** A-Z, a-z (52 characters) */
    MIXEDCASE,
    
    /** 0-9, A-F (16 characters) */
    HEXADECIMAL,
    
    /** A-Z, 0-9 (36 characters) */
    ALPHANUMERIC_UPPERCASE,
    
    /** a-z, 0-9 (36 characters) */
    ALPHANUMERIC_LOWERCASE,
    
    /** A-Z, a-z, 0-9 (62 characters) */
    ALPHANUMERIC_MIXEDCASE,
    
    /** A-Z, a-z, 0-9, -, _ (64 characters). Safe for URLs without encoding. */
    URL_SAFE,
    
    /** Alphanumeric mixed case excluding visually ambiguous characters: 0, O, I, l (58 characters). */
    BASE58
}
