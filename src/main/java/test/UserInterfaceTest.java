package test;

import org.junit.jupiter.api.Test;
import org.netislepafree.morpion_solitaire.model.Game;
import org.netislepafree.morpion_solitaire.model.grid.Line;
import org.netislepafree.morpion_solitaire.model.grid.Mode;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * The type User interface test.
 */
public class UserInterfaceTest {
    /**
     * The Game.
     */
    final Game game = new Game("test");

    /**
     * Test reset move.
     */
    @Test
    public void testResetMove() {
        game.playerMove(9, 13);
        game.resetMove();
        List<Line> possibleLines = game.grid.findLines(9, 13);
        assertFalse(possibleLines.isEmpty(), "Le mouvement valide devrait produire au moins une ligne possible");
    }

    /**
     * Test set mode.
     */
    @Test
    public void testSetMode() {
        game.setGameMode(Mode._5T);
        assertEquals(Mode._5T, game.grid.getMode(), "Le mode de jeu devrait être Mode._5T après l'avoir défini.");
    }


}
