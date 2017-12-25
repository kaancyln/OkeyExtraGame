package com.digitoy.okeygame;


public class OkeyExtraGame {
    private int numberOfPlayers = Player.maxNumberOfPlayers;

    public static void main(String[] args) {
        OkeyExtraGame game = new OkeyExtraGame();
        try {
            game.startGame();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startGame() throws Exception {
        System.out.println("Started.");
        Player[] players = new Player[numberOfPlayers];
        for (int i = 0; i < numberOfPlayers; i++) {
            players[i] = new Player(i, new OkeyExtraBot());
        }
        GameUtil gameUtil = new GameUtil(0);
        while (true) {
            int turn = gameUtil.getTurn();
            Player player = players[turn];
            player.play(gameUtil);
            gameUtil.checkTileSize();
            if (gameUtil.getTiles().isEmpty()) {
                break;
            }
        }
        System.out.println("Finished.");
    }
}
