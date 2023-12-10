package test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.netislepafree.morpion_solitaire.model.Game;
import org.netislepafree.morpion_solitaire.model.grid.Line;
import org.netislepafree.morpion_solitaire.model.grid.Mode;
import java.util.List;
import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

public class GameTest {

    private Game game;

    @BeforeEach
    public void setUp() {
        game = new Game();
        game.grid.init();
    }
    
    @Test
    public void testValidLine() {
        int x =  19;
        int y =  23;
    	List<Line> possibleLines = game.grid.findLines(x, y);
        assertTrue("Le mouvement valide devrait produire au moins une ligne possible", !possibleLines.isEmpty());
    }
    
    @Test
    public void testInvalidLine() {
        int x = 1;
        int y = 1;
        List<Line> possibleLines = game.grid.findLines(x, y);
        assertTrue("Le mouvement invalide ne devrait produire aucune ligne possible", possibleLines.isEmpty());
    }  
    
    @Test
    public void testResetMove() {
        game.playerMove(19, 23);
        game.resetMove();
        List<Line> possibleLines = game.grid.findLines(19, 23);
        assertTrue("Le mouvement valide devrait produire au moins une ligne possible", !possibleLines.isEmpty());  
    }
    
    @Test
    public void testSetMode() {
        game.setGameMode(Mode._5T);
        assertEquals(Mode._5T, game.grid.getMode(), "Le mode de jeu devrait être Mode._5T après l'avoir défini.");
    }
 
}

