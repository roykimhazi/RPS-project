package com.example.rps;

import java.util.Dictionary;
import java.util.HashMap;

public class Player {
    private HashMap<Integer, Piece_type> pieces;

    public HashMap<Integer, Piece_type> getPieces()
    {
        return pieces;
    }

    public Player(int loc)
    {
        pieces = new HashMap<Integer, Piece_type>();
        for(int i = loc; i < loc + 2; i++)
        {
            for(int j = 0; j < 7; j++)
            {
                this.pieces.put(i*7+j, Piece_type.get_empty_h());
            }
        }
    }
    public void resetPieces(int loc)
    {
        for (int i = loc; i < 14 + loc; i++)
        {
            if (pieces.get(i).getType() != Types.king && pieces.get(i).getType() != Types.trap)
                pieces.put(i, Piece_type.get_empty_h());
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
                    if (pieces.get(loc + x).getType() == Types.empty)
                    {
                        pieces.put(loc + x, Piece_type.get_rock_h());
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
                        if (pieces.get(loc + x).getType() == Types.empty)
                        {
                            pieces.put(loc + x, Piece_type.get_paper_h());
                            foundPlace = true;
                        }
                    }
                }
                else
                {
                    while (!foundPlace)
                    {
                        int x = (int) (Math.random() * (14));
                        if (pieces.get(loc + x).getType() == Types.empty)
                        {
                            pieces.put(loc + x, Piece_type.get_scissors_h());
                            foundPlace = true;
                        }
                    }
                }
            }
        }
    }
}
