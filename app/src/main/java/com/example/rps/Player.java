package com.example.rps;

import java.util.HashMap;

/**
 * This class represent the Player side.
 * It contains data of the pieces.
 */
public class Player
{
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
    public Player(int loc, GameActivity ga, boolean all_expose)
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
        if (all_expose)
        {
            for (int key : this.getPieces().keySet())
            {
                this.getPieces().get(key).expose();
            }
        }
    }
    public HashMap<Integer, Piece_type> getPieces()
    {
        return pieces;
    }

    /**
     * This function reset all pieces to empty.
     * @param loc
     * @param all_expose
     */
    public void resetPieces(int loc , boolean all_expose)
    {
        for (int i = loc; i < 14 + loc; i++)
        {
            if (pieces.get(i).getType() != Types.king && pieces.get(i).getType() != Types.trap)
                pieces.put(i, Piece_type.get_empty_h());
        }
        if (all_expose)
        {
            for (int key : this.getPieces().keySet())
            {
                this.getPieces().get(key).expose();
            }
        }
    }

    /**
     * This function spreads 12 weapon pieces randomly.
     * @param loc
     * @param all_expose
     */
    public void spread_pieces(int loc, boolean all_expose)
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
                        if (all_expose)
                            pieces.put(loc + x, Piece_type.get_rock());
                        else
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
                            if (all_expose)
                                pieces.put(loc + x, Piece_type.get_paper());
                            else
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
                            if (all_expose)
                                pieces.put(loc + x, Piece_type.get_scissors());
                            else
                                pieces.put(loc + x, Piece_type.get_scissors_h());
                            foundPlace = true;
                        }
                    }
                }
            }
        }
    }
}
