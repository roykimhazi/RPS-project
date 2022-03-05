package com.example.rps;

import java.util.Dictionary;
import java.util.HashMap;

public class Player {
    enum piece_type {
        empty,
        rock,
        paper,
        scissors,
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
    public void resetPieces(int loc)
    {
        for (int i = loc; i < 14 + loc; i++)
        {
            if (pieces.get(i) != piece_type.king && pieces.get(i) != piece_type.trap)
                pieces.put(i, piece_type.empty);
        }
    }

    public void spread_pieces(int loc)
    {
        for (int i = 0; i < 12; i++)
        {
            boolean foundPlace = false;
            if(i<4)
            {
                while (!foundPlace)
                {
                    int x = (int) (Math.random() * (14));
                    if (pieces.get(loc + x) == piece_type.empty)
                    {
                        pieces.put(loc + x, piece_type.paper);
                        foundPlace = true;
                    }
                }
            }
            else{
                if(i<8)
                {
                    while (!foundPlace)
                    {
                        int x = (int) (Math.random() * (14));
                        if (pieces.get(loc + x) == piece_type.empty)
                        {
                            pieces.put(loc + x, piece_type.rock);
                            foundPlace = true;
                        }
                    }
                }
                else
                {
                    while (!foundPlace)
                    {
                        int x = (int) (Math.random() * (14));
                        if (pieces.get(loc + x) == piece_type.empty)
                        {
                            pieces.put(loc + x, piece_type.scissors);
                            foundPlace = true;
                        }
                    }
                }
            }
        }
    }
}
