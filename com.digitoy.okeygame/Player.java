package com.digitoy.okeygame;

import java.util.*;

public class Player {
    public static int maxNumberOfPlayers = 4;
    private int playerIndex;
    private IOkeyExtra okeyExtra;

    public Player(int playerIndex, IOkeyExtra logic) {
        this.playerIndex = playerIndex;
        this.okeyExtra = logic;
    }

    public void play(GameUtil gameUtil) throws Exception {
        List<Tile> playerTiles = gameUtil.getPlayerTilesList().get(this.playerIndex);
        final Tile joker = gameUtil.getJoker();
        Comparator handComparator = new Comparator(joker);
        if (playerTiles.size() == 15) {
            Hand hand = this.okeyExtra.play(joker, playerTiles);
            discardTile(gameUtil, hand);
        } else if (playerTiles.size() == 14) {
            int previousPlayer = gameUtil.findPreviousPlayer(this.playerIndex);
            Tile topDiscardedTile = gameUtil.topThrownedPlayerTile(previousPlayer);
            Tile topCenterTile = gameUtil.topCenterTile();
            if (topCenterTile == null && topDiscardedTile == null) {
                throw new Exception("Player " + playerIndex + " can not take from center tiles and throwened tiles. They are empty.");
            }
            if (topDiscardedTile == null && topCenterTile != null) {
                gameUtil.takeFromCenterTiles(this.playerIndex);
                Hand hand = this.okeyExtra.play(joker, playerTiles);
                discardTile(gameUtil, hand);
            } else if (topCenterTile == null && topDiscardedTile != null) {
                gameUtil.takeFromThrownedTiles(previousPlayer);
                Hand hand = this.okeyExtra.play(joker, playerTiles);
                discardTile(gameUtil, hand);
            } else {
                List<Tile> tmpTiles = new LinkedList<Tile>(playerTiles);
                tmpTiles.add(topCenterTile);
                Hand handDrawCenter = this.okeyExtra.play(joker, tmpTiles);
                tmpTiles = new LinkedList<Tile>(playerTiles);
                tmpTiles.add(topDiscardedTile);
                Hand handDrawDiscarded = this.okeyExtra.play(joker, tmpTiles);

                int compareResult = handComparator.compare(handDrawCenter, handDrawDiscarded);
                if (compareResult > 0) {
                    gameUtil.takeFromThrownedTiles(this.playerIndex);
                } else if (compareResult < 0) {
                    gameUtil.takeFromCenterTiles(this.playerIndex);
                } else {
                    //both hands have equal scores. Randomly select one.
                    if (Math.random() > 0.1f) {
                        gameUtil.takeFromThrownedTiles(this.playerIndex);
                    } else {
                        gameUtil.takeFromCenterTiles(this.playerIndex);
                    }
                }
                Hand hand = this.okeyExtra.play(joker, playerTiles);
                discardTile(gameUtil, hand);
            }
        } else {
            throw new Exception("Players have minimum 14 and maximum 15 tiles. Player " + this.playerIndex + " has " + playerTiles.size() + " tiles");
        }
    }

    private void discardTile(GameUtil gameUtil, Hand hand) throws Exception {
        List<Tile> freeTiles = hand.getFreeTilesList();
        if (freeTiles.isEmpty()) {
            gameUtil.discardTile(this.playerIndex, (Tile) ((List) (hand.getSeriesList().toArray()[0])).get(0));
        } else {
            if (freeTiles.size() == 1) {
                System.out.println("Winner player is : Player" + gameUtil.getTurn() + " with hand : " + hand);
                throw new Exception("Game is finished");
            } else {
                gameUtil.discardTile(this.playerIndex, freeTiles.get(0));
                hand.getFreeTilesList().remove(freeTiles.get(0));
            }
        }
        System.out.println("Hand player " + (gameUtil.getTurn()) + " " + hand);
    }

    public static class Hand {

        public static int handTileSize = 14;
        private List<List<Tile>> seriesList = new LinkedList<List<Tile>>();
        private List<Pairs> pairsList = new LinkedList<Pairs>();
        private List<Tile> freeTilesList = new ArrayList<Tile>();
        private Tile joker;

        public List<Pairs> getPairsList() {
            return pairsList;
        }

        public List<Tile> getFreeTilesList() {
            return freeTilesList;
        }

        public List<List<Tile>> getSeriesList() {
            return seriesList;
        }

        public int findNumberOfJokers(Tile joker) {
            int result = Collections.frequency(this.freeTilesList, joker);
            for (List<Tile> tiles : this.seriesList) {
                result = result + Collections.frequency(tiles, joker);
            }
            return result;
        }

        public void setFreeTilesList(List<Tile> freeTilesList) {
            this.freeTilesList = freeTilesList;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("Hand : ");
            for(List<Tile> serie :  seriesList) {
                sb.append(serie).append(", ");
            }

            if (pairsList.size() > 1) {
                sb.append(pairsList.toString());
            }
            if (freeTilesList.size() > 0) {
                sb.append("------ Free Tile :");
                for (int i = 0; i < freeTilesList.size(); i++) {
                    sb.append(freeTilesList.get(i).getTileColor().toString() + freeTilesList.get(i).getTileValue()).append(",");
                }
            }

            return sb.toString();
        }

        public void addSerieList(List<Tile> serieList) {
            this.seriesList.add(serieList);
        }

        public int numberOfTiles() {
            int sum = freeTilesList.size();
            for (List<Tile> serie : seriesList) {
                sum += serie.size();
            }
            return sum;
        }

        public boolean contains(Tile tile) {
            for (Tile freeTile : freeTilesList) {
                if (tile.equals(freeTile)) {
                    return true;
                }
            }
            for (List<Tile> serie : seriesList) {
                for (Tile aTile : serie) {
                    if (tile.equals(aTile)) {
                        return true;
                    }
                }
            }
            for (Pairs pair : pairsList) {
                if (tile.equals(pair.getPair1()) || tile.equals(pair.getPair2())) {
                    return true;
                }
            }
            return false;
        }

        public static class Pairs {
            private Tile pair1;
            private Tile pair2;

            public Pairs(Tile pair1, Tile pair2) {
                this.pair1 = pair1;
                this.pair2 = pair2;
            }

            public Tile getPair1() {
                return pair1;
            }

            public Tile getPair2() {
                return pair2;
            }

            @Override
            public String toString() {
                return "<" + pair1 + ", " + pair2 + ">";
            }

        }
    }
}
