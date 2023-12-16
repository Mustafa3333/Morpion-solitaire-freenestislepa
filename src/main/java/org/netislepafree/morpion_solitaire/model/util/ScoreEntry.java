package org.netislepafree.morpion_solitaire.model.util;


/**
 * The type Score entry.
 */
public class ScoreEntry {
    /**
     * Gets username.
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Gets score.
     *
     * @return the score
     */
    public double getScore() {
        return score;
    }

    private final String username;
    private final double score;

    /**
     * Instantiates a new Score entry.
     *
     * @param username the username
     * @param score    the score
     */
    public ScoreEntry(String username, double score) {
        this.score = score;
        this.username = username;
    }
}
