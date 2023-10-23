package org.netislepafree.morpion_solitaire;

import org.netislepafree.morpion_solitaire.grid.Mode;
import javafx.scene.input.MouseEvent;

public class UserInterface {
    private String computerOptions;
    private Mode gameModeOptions;
    private Game game;

    public void mousePressed(MouseEvent me) {
        System.out.printf("(%f,%f)", me.getX(), me.getY());
        double tolerance = 0.2;
        double tempX = (me.getX() - 25) / 25;
        double tempY = (me.getY() - 25) / 25;
        double frX = tempX - (int) tempX;
        double frY = tempY - (int) tempY;
        if ((frX > tolerance && frX < 1 - tolerance) || (frY > tolerance && frY < 1 - tolerance)) {
            System.out.println("INVALID");
            return;
        }
        int gridX = (int) Math.round(tempX);
        int gridY = (int) Math.round(tempY);

        game.playerMove(gridX, gridY);
    }

}
