package test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.netislepafree.morpion_solitaire.model.Game;
import org.netislepafree.morpion_solitaire.model.grid.Line;
import org.netislepafree.morpion_solitaire.model.grid.Mode;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class GameTest {

    private Game game;

    @BeforeEach
    public void setUp() {
        game = new Game("test");
        game.grid.init();
    }
    
    @Test
    public void testValidLine() {
        int x =  9;
        int y =  13;
    	List<Line> possibleLines = game.grid.findLines(x, y);
        assertFalse(possibleLines.isEmpty(), "Le mouvement valide devrait produire au moins une ligne possible");
    }
    
    @Test
    public void testInvalidLine() {
        int x = 1;
        int y = 1;
        List<Line> possibleLines = game.grid.findLines(x, y);
        assertTrue(possibleLines.isEmpty(), "Le mouvement invalide ne devrait produire aucune ligne possible");
    }  
    
    @Test
    public void testResetMove() {
        game.playerMove(9, 13);
        game.resetMove();
        List<Line> possibleLines = game.grid.findLines(9, 13);
        assertFalse(possibleLines.isEmpty(), "Le mouvement valide devrait produire au moins une ligne possible");
    }
    
    @Test
    public void testSetMode() {
        game.setGameMode(Mode._5T);
        assertEquals(Mode._5T, game.grid.getMode(), "Le mode de jeu devrait être Mode._5T après l'avoir défini.");
    }
 
}

