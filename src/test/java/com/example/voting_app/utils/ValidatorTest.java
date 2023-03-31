package com.example.voting_app.utils;

import jdk.jfr.Name;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ValidatorTest {


    @Test
    public void testThatPasswordIsValid(){
      assertTrue(Validator.isPasswordValid("JenniferMusah#1234"));
    }

    @Test
    @Name("Test that password is not valid when it is does not contain a symbols")
    public void testThatPasswordIsNotValidWhenItsDoesNotContainASymbol(){
        assertFalse(Validator.isPasswordValid("jenny78456asj"));
    }
    @Test
    @Name("Test that password is not valid when it does not contain capital letter")
    public void testThatPasswordIsNotValidWhenItDoesNotContainACapitalLetters(){
        assertFalse(Validator.isPasswordValid("jennymusag234#"));
    }
    @Test
    @Name("Test that password is not valid when i does not contain at least a small letter")
    public void testThatPasswordIsNotValidWhenItDoesNotContainACapitalLetter(){
        assertFalse(Validator.isPasswordValid("JENNYMUSAH#1244"));
    }
     @Test
    @Name("Test that password is not valid when i does not contain at least a number")
    public void testThatPasswordIsNotValidWhenItDoesNotContainANumber(){
        assertFalse(Validator.isPasswordValid("Jennymusah###"));
    }

    @Test
    public void testThatEmailValid(){
        assertTrue(Validator.isEmailAddressValid("jennymusah99@gmail.com"));
    }

    @Test
    @Name("test that password is invalid when it does not contain @")
    public void testThatPasswordIsInValidWhenItDoesNotContainAt(){
        assertFalse(Validator.isEmailAddressValid("jennymusahgmail.com"));
    }

    @Test
    @Name("test that password is invalid when it does not contain .")
    public void testThatPasswordIsInValidWhenItDoesNotContainDot(){
        assertFalse(Validator.isEmailAddressValid("jennymusah@gmail,com"));
    }
     @Test
    @Name("test that password is valid when it contains capital letter")
    public void testThatPasswordIsValidWhenItContainsCapitalLetter(){
        assertTrue(Validator.isEmailAddressValid("Jennymusah@gmail.com"));
    }

    @Test
    @Name("Test that date format returns true when date format is valid")
    public void testThatDateFormatValidationReturnTrueWhenValid(){
        assertTrue(Validator.isDateValid("03/03/2023"));
    }
     @Test
    @Name("Test that date format returns false when date format is invalid")
    public void testThatDateFormatValidationReturnFalseWhenInValid() {
         assertFalse(Validator.isDateValid("/03/2023"));
     }

}
