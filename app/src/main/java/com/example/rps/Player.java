package com.example.rps;

import java.util.Dictionary;
import java.util.HashMap;

public class Player
{
    /*
        This class represent the Player side.
         It contains data of the pieces.
    */
    private HashMap<Integer, Piece_type> pieces;
    protected GameActivity gameActivity;

    public Player(Player player)
    {
        pieces = new HashMap<Integer, Piece_type>();
        for (int key : player.getPieces().keySet())
        {
            this.pieces.put(key,player.getPieces().get(key));
        }
        this.gameActivity = new GameActivity();
        gameActivity.all_pieces = player.gameActivity.getCopy_of_All_pieces();
        gameActivity.blue_player = this;
    }

    public Player(Player player, Computer computer)
    {
        pieces = new HashMap<Integer, Piece_type>();
        for (int key : computer.getPieces().keySet())
        {
            this.pieces.put(key,computer.getPieces().get(key));
        }
        this.gameActivity = new GameActivity();
        gameActivity.all_pieces = computer.gameActivity.getCopy_of_All_pieces();
        gameActivity.blue_player = new Player(player);
    }
    public Player(int loc, GameActivity ga)
    {
        gameActivity = ga;
        pieces = new HashMap<Integer, Piece_type>();
        for(int i = loc; i < loc + 2; i++)
        {
            for(int j = 0; j < 7; j++)
            {
                this.pieces.put(i*7+j, Piece_type.get_empty_h());
            }
        }
    }
    public HashMap<Integer, Piece_type> getPieces()
    {
        return pieces;
    }
    public void resetPieces(int loc)
    {
        /*
            This function reset all pieces to empty.
         */
        for (int i = loc; i < 14 + loc; i++)
        {
            if (pieces.get(i).getType() != Types.king && pieces.get(i).getType() != Types.trap)
                pieces.put(i, Piece_type.get_empty_h());
        }
    }

    public void spread_pieces(int loc)
    {
        /*
           This function spreads 12 weapon pieces randomly.
        */
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
            else
                {
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
