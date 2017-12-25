package com.digitoy.okeygame;

import java.util.List;

public interface IOkeyExtra {

    Player.Hand play(Tile joker, List<Tile> tiles);

}