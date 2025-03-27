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
