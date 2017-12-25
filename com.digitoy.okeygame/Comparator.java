package com.digitoy.okeygame;


public class Comparator implements java.util.Comparator<Player.Hand> {

    private final Tile joker;

    public Comparator(Tile joker) {
        this.joker = joker;
    }

    @Override
    public int compare(Player.Hand hand1, Player.Hand hand2) {
        int h1FreeTiles = hand1.getFreeTilesList().size();
        int h2FreeTiles = hand2.getFreeTilesList().size();
        if (h1FreeTiles < h2FreeTiles) {
            return -1;
        } else if (h1FreeTiles > h2FreeTiles) {
            return 1;
        } else {
            int h1JokerCount = hand1.findNumberOfJokers(this.joker);
            int h2JokerCount = hand2.findNumberOfJokers(this.joker);
            if (h1JokerCount > h2JokerCount) {
                System.out.println("h1 *  : " + hand1);
                System.out.println("h2 : " + hand2);
                return -1;
            } else if (h1JokerCount < h2JokerCount) {
                System.out.println("h1 : " + hand1);
                System.out.println("h2 * : " + hand2);
                return 1;
            }

        }
        return 0;
    }
}
