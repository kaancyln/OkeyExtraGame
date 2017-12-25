package com.digitoy.okeygame;

public class Tile implements Comparable<Tile> {
    public static int maxTileValue = 13;
    public static int maxTileCapacity = 106;

    @Override
    public int compareTo(Tile o) {
        return 0;
    }

    public enum Color {
        YELLOW,
        BLUE,
        BLACK,
        RED
    }

    private Color tileColor;
    private int tileValue;
    private boolean joker;
    private boolean fakeJoker;

    public Tile(int value, Color color) {
        this.tileValue = value;
        this.tileColor = color;
        this.joker = false;
        this.fakeJoker = false;
    }

    public static Tile joker(int value, Color color) {
        Tile tile = new Tile(value, color);
        tile.joker = true;
        tile.fakeJoker = false;
        return tile;
    }

    public static Tile fakeJoker(int value, Color color) {
        Tile tile = new Tile(value, color);
        tile.joker = false;
        tile.fakeJoker = true;
        return tile;
    }

    public static Tile findFakeJokerFromIndicator(Tile indicator) {
        int value = indicator.getTileValue() + 1;
        if (value > 13) {
            value = 1;
        }
        Color color = indicator.getTileColor();
        return fakeJoker(value, color);
    }

    public static Tile findJokerFromIndicator(Tile indicator) {
        int value = indicator.getTileValue() + 1;
        if (value > 13) {
            value = 1;
        }
        Color color = indicator.getTileColor();
        return joker(value, color);
    }

    @Override
    public String toString() {
        String prefix = "";
        if (this.isFakeJoker()) {
            prefix = "J";
        } else if (this.isJoker()) {
            prefix = "*";
        }
        switch (this.tileColor) {
            case BLACK:
                return prefix + "BLACK" + this.tileValue;
            case RED:
                return prefix + "RED" + this.tileValue;
            case BLUE:
                return prefix + "BLUE" + this.tileValue;
            case YELLOW:
                return prefix + "YELLOW" + this.tileValue;
            default:
                return "NA";
        }
    }

    public int getTileValue() {
        return tileValue;
    }

    public Color getTileColor() {
        return tileColor;
    }

    public boolean isJoker() {
        return this.joker;
    }

    public boolean isFakeJoker() {
        return this.fakeJoker;
    }

}
