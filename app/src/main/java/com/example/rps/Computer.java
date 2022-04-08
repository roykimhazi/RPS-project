package com.example.rps;

public class Computer extends Player {

    public Computer(int loc, Board board)
    {
        super(loc);
        boolean foundPlace = false;
        while (!foundPlace)
        {
            int x = (int) (Math.random() * (14));
            if (x < 7)
            {
                getPieces().put(loc + x, Player.piece_type.king);
                foundPlace = true;
            }
        }
        foundPlace = false;
        while (!foundPlace)
        {
            int x = (int) (Math.random() * (14));
            if (getPieces().get(loc + x) == piece_type.empty)
            {
                getPieces().put(loc + x, Player.piece_type.trap);
                foundPlace = true;
            }
        }
        spread_pieces(0);
        initComputerPieces(board);
    }

    private void initComputerPieces(Board board)
    {
        for (int i = 0; i < 2; i++)
        {
            for (int j = 0; j < 7; j++)
            {
                board.getButtons()[i][j].setText("?");
                //board.showPieces(0,this);
            }
        }
    }
}
