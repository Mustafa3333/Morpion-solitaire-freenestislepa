package test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.netislepafree.morpion_solitaire.App;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The type App test.
 */
public class AppTest {
    private static App app;

    /**
     * Sets up.
     */
    @BeforeEach
    public void setUp() {
        app = new App(); // Instantiate the App object before each test
    }

    /**
     * Test valid login.
     */
    @Test
    public void testValidLogin() {
        assertTrue(app.checkCredentials("m", ""));
    }

    /**
     * Test invalid login.
     */
    @Test
    public void testInvalidLogin() {
        assertFalse(app.checkCredentials("nonexistentUser", "invalidPassword"));

    }
}