package com.digitoy.okeygame;

import java.util.*;

public class GameUtil {
    public static final Random random = new Random(System.currentTimeMillis());
    private int numberOfPlayers = Player.maxNumberOfPlayers;
    private int handTileSize = Player.Hand.handTileSize;
    private List<List<Tile>> thrownTilesList;
    private List<List<Tile>> playerTilesList;
    private List<Tile> tiles;
    private Tile indicator;
    private int turn;

    public GameUtil(int turn) {
        this.turn = turn;
        this.indicator = findIndicator();
        List<Tile> allTiles = handOutTiles(this.indicator);
        playerTilesList = new ArrayList<List<Tile>>(numberOfPlayers);
        thrownTilesList = new ArrayList<List<Tile>>(numberOfPlayers);

        for (int player = 0; player < 4; player++) {
            playerTilesList.add(new LinkedList<Tile>());
            thrownTilesList.add(new LinkedList<Tile>());
        }

        for (int i = 0; i < 14; i++) {
            for (int player = 0; player < numberOfPlayers; player++) {
                playerTilesList.get(player).add(allTiles.remove(0));
            }
        }
        playerTilesList.get(turn).add(allTiles.remove(0));
        StringBuilder sb = new StringBuilder();
        if (playerTilesList.size() > 0) {
            for (int i = 0; i < playerTilesList.size(); i++) {
                sb.append("Player " + i + " s hand : ");
                for (int j = 0; j < playerTilesList.get(i).size(); j++) {
                    sb = sb.append(playerTilesList.get(i).get(j).getTileColor().toString() + playerTilesList.get(i).get(j).getTileValue()).append(", ");

                }
                System.out.println(sb.toString());
                sb = new StringBuilder();
            }
        }
        this.tiles = allTiles;
    }

    public static final Tile findIndicator() {
        int value = random.nextInt(Tile.maxTileValue) + 1;
        int color = random.nextInt(Player.maxNumberOfPlayers);
        Tile tile = new Tile(value, Tile.Color.values()[color]);
        System.out.println("Indicator : " + tile.getTileColor() + tile.getTileValue());
        Tile joker = Tile.findJokerFromIndicator(tile);
        System.out.println("Joker : " + joker.getTileColor() + joker.getTileValue());
        return tile;
    }

    public static final List<Tile> handOutTiles(final Tile indicator) {
        List<Tile> tiles = new ArrayList<Tile>(Tile.maxTileCapacity);
        for (Tile.Color color : Tile.Color.values()) {
            for (int value = 1; value <= Tile.maxTileValue; value++) {
                tiles.add(new Tile(value, color));
                tiles.add(new Tile(value, color));
            }
        }
        tiles.remove(tiles.stream().filter(x -> (x.getTileColor().toString() + x.getTileValue()).equalsIgnoreCase(indicator.toString())).findFirst().get());
        tiles.add(Tile.findFakeJokerFromIndicator(indicator));
        tiles.add(Tile.findFakeJokerFromIndicator(indicator));
        Collections.shuffle(tiles);
        return tiles;
    }

    public static final int findPreviousPlayer(int player) {
        if (player == 0) {
            return 3;
        } else {
            return player - 1;
        }
    }

    public static final int findNextPlayer(int player) {
        return (player + 1) % 4;
    }

    public int getTurn() {
        return turn;
    }

    public void checkTileSize() {
        try {
            int tileCount = this.tiles.size();
            for (List<Tile> tile : playerTilesList) {
                tileCount = tileCount + tile.size();
                if (tile.size() > handTileSize + 1 || tile.size() < handTileSize) {
                    throw new Exception("Players have minimum 14 and maximum 15 tiles. " + playerTilesList);

                }
            }
            for (List<Tile> l : thrownTilesList) {
                tileCount = tileCount + l.size();
            }
            tileCount++;
            int expectedTileCount = Tile.maxTileCapacity;
            if (tileCount != expectedTileCount) {
                throw new Exception("There are " + tileCount + " tiles. There should be " + expectedTileCount);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Tile> getTiles() {
        return tiles;
    }

    public Tile getJoker() {
        return Tile.findJokerFromIndicator(this.indicator);
    }

    public List<List<Tile>> getPlayerTilesList() {
        return playerTilesList;
    }

    public void discardTile(int turn, Tile tile) {
        try {
            List<Tile> playerTiles = this.playerTilesList.get(turn);
            if (playerTiles.remove(tile)) {
                this.thrownTilesList.get(turn).add(tile);
            } else {
                throw new Exception("Player " + turn + " does not have a tile " + tile + ", can not thrown it.");
            }
            System.out.println("Player " + turn + " throwned tile " + tile.getTileColor() + tile.getTileValue());
            this.turn = GameUtil.findNextPlayer(turn);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Tile takeFromThrownedTiles(int turn) throws Exception {
        int previousPlayer = GameUtil.findPreviousPlayer(turn);
        if (this.thrownTilesList.get(previousPlayer).isEmpty()) {
            throw new Exception("No throwned tiles found for player " + previousPlayer);
        }
        Tile tile = this.thrownTilesList.get(previousPlayer).remove(0);
        this.playerTilesList.get(turn).add(tile);
        System.out.println("Player " + turn + " took throwned tile " + tile.getTileColor() + tile.getTileValue());
        return tile;
    }

    public Tile topThrownedPlayerTile(int playerIndex) {
        if (this.thrownTilesList.get(playerIndex).isEmpty()) {
            return null;
        }
        return this.thrownTilesList.get(playerIndex).get(0);
    }

    public Tile takeFromCenterTiles(int turn) throws Exception {
        if (this.tiles.isEmpty()) {
            throw new Exception("Player " + turn + " can not take tile. No more tiles.");
        }
        Tile tile = this.tiles.remove(0);
        this.playerTilesList.get(turn).add(tile);
        System.out.println("Player " + turn + " took from center tile " + tile.getTileColor() + tile.getTileValue());
        return tile;
    }

    public Tile topCenterTile() {
        if (this.tiles.isEmpty()) {
            return null;
        } else {
            return this.tiles.get(0);
        }
    }
}
