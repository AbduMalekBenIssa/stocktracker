package stocktracker.tests;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


import stocktracker.model.*;
import java.util.List;


/**
 * JUnit tests for User class
 *
 * @author Omar Almishri, AbduMalek Ben Issa
 * @version 2.0
 * @Tutorial T04
 */
public class UserTest {
    private User user;

    @BeforeEach
    public void setUp() {
        user = new User("Test User", 10000.0);
    }

    @Test
    public void testGetName() {
        assertEquals("Test User", user.getName(), "Name should match constructor value");
    }

    @Test
    public void testGetBalance() {
        assertEquals(10000.0, user.getBalance(), 0.01, "Balance should match constructor value");
    }

    @Test
    public void testDeposit_validAmount() {
        boolean result = user.deposit(500.0);

        assertTrue(result, "Deposit should return true for valid amount");
        assertEquals(10500.0, user.getBalance(), 0.01, "Balance should increase by deposit amount");
    }
    @Test
    public void testDeposit_invalidAmount() {
        boolean result = user.deposit(-100.0);

        assertFalse(result, "Deposit should return false for negative amount");
        assertEquals(10000.0, user.getBalance(), 0.01, "Balance should remain unchanged");
    }

}
