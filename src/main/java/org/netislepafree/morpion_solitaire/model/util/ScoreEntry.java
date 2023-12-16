package org.netislepafree.morpion_solitaire.model.util;


/**
 * The type Score entry.
 */
public record ScoreEntry(String username, double score) {
    /**
     * Gets username.
     *
     * @return the username
     */
    @Override
    public String username() {
        return username;
    }

    /**
     * Gets score.
     *
     * @return the score
     */
    @Override
    public double score() {
        return score;
    }

    /**
     * Instantiates a new Score entry.
     *
     * @param username the username
     * @param score    the score
     */
    public ScoreEntry {
    }
}
