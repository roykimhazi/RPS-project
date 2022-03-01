package com.example.rps;

import java.util.Dictionary;
import java.util.HashMap;

public class Player {
    enum piece_type {
        empty,
        rock,
        paper,
        Scissors,
        trap,
        king
    }
    private HashMap<Integer, piece_type> pieces;

    public HashMap<Integer, piece_type> getPieces()
    {
        return pieces;
    }

    public Player(int loc)
    {
        pieces = new HashMap<Integer, piece_type>();
        for(int i = loc; i < loc + 2; i++)
        {
            for(int j = 0; j < 7; j++)
            {
                this.pieces.put(i*7+j,piece_type.empty);
            }
        }
    }
}
