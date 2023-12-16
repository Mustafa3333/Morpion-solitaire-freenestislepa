package test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.netislepafree.morpion_solitaire.model.Game;
import org.netislepafree.morpion_solitaire.model.grid.Line;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The type Game test.
 */
public class GameTest {

    private Game game;

    /**
     * Sets up.
     */
    @BeforeEach
    public void setUp() {
        game = new Game("test");
        game.grid.init();
    }

    /**
     * Test valid line.
     */
    @Test
    public void testValidLine() {
        int x =  9;
        int y =  13;
    	List<Line> possibleLines = game.grid.findLines(x, y);
        assertFalse(possibleLines.isEmpty(), "Le mouvement valide devrait produire au moins une ligne possible");
    }

    /**
     * Test invalid line.
     */
    @Test
    public void testInvalidLine() {
        int x = 1;
        int y = 1;
        List<Line> possibleLines = game.grid.findLines(x, y);
        assertTrue(possibleLines.isEmpty(), "Le mouvement invalide ne devrait produire aucune ligne possible");
    }  

}

