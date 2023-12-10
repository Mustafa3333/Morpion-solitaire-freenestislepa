package test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.Before;
import org.netislepafree.morpion_solitaire.model.Game;
import org.netislepafree.morpion_solitaire.model.grid.Grid;
import org.netislepafree.morpion_solitaire.model.grid.Line;
import org.netislepafree.morpion_solitaire.model.grid.Point;

import java.util.List;

import static org.junit.Assert.*;

public class GameTest {

    private Game game;

    @BeforeEach
    public void setUp() {
        game = new Game();
        // Initialisation du jeu avec un état qui permet un mouvement valide
        game.grid.init(); // Initialise le plateau avec la configuration de départ
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
	
}

