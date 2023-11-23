package org.netislepafree.morpion_solitaire;

import org.netislepafree.morpion_solitaire.grid.Grid;
import org.netislepafree.morpion_solitaire.grid.Line;
import org.netislepafree.morpion_solitaire.grid.Mode;
import org.netislepafree.morpion_solitaire.view.GridView;

import java.util.List;

public class Game {
    private String playerName;
    public Grid grid;
    private GridView gridView;
    private boolean computerMode=false;

    public Game() {
        grid = new Grid(50, 50, Mode._5D);
        grid.init();
    }

    public void playerMove(int x, int y){
        if (computerMode) return;
        List<Line> possibleLines = grid.findLines(x, y);
        if (possibleLines.size() == 0) {
            System.out.println("Invalid Move");
        } else if (possibleLines.size() == 1) {
            makeMove(possibleLines.get(0));
        }
    }
    private void makeMove(Line line) {
        grid.addLine(line);
    }

    public void setGameMode(Mode mode) {
        this.grid.setMode(mode);
    }

    // @Todo
    public void updateGameState(){
    };

}
