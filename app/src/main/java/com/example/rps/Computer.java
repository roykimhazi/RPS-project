package com.example.rps;

import java.util.HashMap;

public class Computer extends Player {

    private int count_scissors;
    private int count_rock;
    private int count_paper;

    private HashMap<Integer, Piece_type> guess;

    public Computer(int loc, Board board)
    {
        super(loc);
        count_paper = 4;
        count_rock = 4;
        count_scissors = 4;
        guess = new HashMap<Integer, Piece_type>();
        boolean foundPlace = false;
        while (!foundPlace)
        {
            int x = (int) (Math.random() * (14));
            if (x < 7)
            {
                getPieces().put(loc + x, Piece_type.get_king_h());
                //getPieces().get(loc + x).hide();
                foundPlace = true;
            }
        }
        foundPlace = false;
        while (!foundPlace)
        {
            int x = (int) (Math.random() * (14));
            if (getPieces().get(loc + x).getType() == Types.empty)
            {
                getPieces().put(loc + x, Piece_type.get_trap_h());
                //getPieces().get(loc + x).hide();
                foundPlace = true;
            }
        }
        spread_pieces(0);
        initComputerPieces(board);
        for(int i = loc; i < loc + 2; i++)
        {
            for(int j = 0; j < 7; j++)
            {
                guess.put(i*7+j + 28, Piece_type.get_empty_h());
                //guess.get(i*7+j + 28).hide();
            }
        }
        initGuess();
    }

    private void initComputerPieces(Board board)
    {
        for (int i = 0; i < 2; i++)
        {
            for (int j = 0; j < 7; j++)
            {
                //board.getButtons()[i][j].setText("?");
                board.showPieces(0,this);
            }
        }
    }

    private void initGuess()
    {
        boolean foundPlace = false;

        int x = (int) (Math.random() * (7) + 35);
        guess.put(x, Piece_type.get_king_h());

        while (!foundPlace)
        {
            x = (int) (Math.random() * (14)+ 28);
            if (guess.get(x).getType() == Types.empty)
            {
                guess.put(x, Piece_type.get_trap_h());
                foundPlace = true;
            }
        }

        for (int i = 0; i < 12; i++)
        {
            foundPlace = false;
            if (i < 4) {
                while (!foundPlace) {
                    x = (int) (Math.random() * (14)+ 28);
                    if (guess.get(x).getType() == Types.empty)
                    {
                        guess.put(x, Piece_type.get_paper_h());
                        foundPlace = true;
                    }
                }
            }
            else
                {
                if (i < 8) {
                    while (!foundPlace)
                    {
                        x = (int) (Math.random() * (14)+ 28);
                        if (guess.get(x).getType() == Types.empty)
                        {
                            guess.put(x, Piece_type.get_rock_h());
                            foundPlace = true;
                        }
                    }
                }
                else
                    {
                    while (!foundPlace)
                    {
                        x = (int) (Math.random() * (14)+ 28);
                        if (guess.get(x).getType() == Types.empty)
                        {
                            guess.put(x, Piece_type.get_scissors_h());
                            foundPlace = true;
                        }
                    }
                }
            }
        }
    }

    public void updateGuesses(HashMap<Integer, Piece_type> blue_pieces)
    {
        for (int loc : blue_pieces.keySet())
        {
            if(blue_pieces.get(loc).is_exposed() == true)
                guess.put(loc,blue_pieces.get(loc));
        }
    }


    public void reportAGuess(int loc, Piece_type p_type)
    {
        // this function receive a location and type of checked guess, if guess at loc = type expose it. else replace to type.
        if (guess.get(loc).getType() != p_type.getType())
            replacePiecesSpecific(loc, p_type);
        guess.get(loc).expose();
    }
    public void removeFromGuess(int loc)
    {
        Types type;
        if (guess.get(loc).getType() == Types.trap || guess.get(loc).getType() == Types.king)
        {
            replacePieces(loc);
            type = guess.get(loc).getType();
        }
        else
            type = guess.remove(loc).getType();

        if(type == Types.scissors)
            count_scissors -= 1;
        if(type == Types.rock)
            count_rock -= 1;
        if(type == Types.paper)
            count_paper -= 1;
    }

    public void updateMove(int first_loc, int moved_loc)
    {
        // change guess location if moved.
        if(guess.get(first_loc).getType() == Types.trap || guess.get(first_loc).getType() == Types.king) // if guess is trap or king, replace their guess because they can't move.
        {
            replacePieces(first_loc);
        }
        guess.put(moved_loc, guess.remove(first_loc));
    }

    public void replacePieces(int loc)
    {
        // replace king/trap guess with piece guess
        for (int replace_loc : guess.keySet())
        {
            if(replace_loc != loc && guess.get(replace_loc).getType() != Types.trap && guess.get(replace_loc).getType() != Types.king && guess.get(replace_loc).is_exposed() == false)
            {
                Piece_type type = guess.remove(replace_loc);
                guess.put(replace_loc, guess.get(loc));
                guess.put(loc, type);
                break;
            }
        }
    }
    public void replacePiecesSpecific(int loc, Piece_type p_type)
    {// this function get a location and type, and replace piece.
        switch (p_type.getType())
        {
            case king:
            {
                // Declare computer win
            }
            case trap:
            {
                for (int replace_loc : guess.keySet())
                {
                    if(replace_loc != loc && guess.get(replace_loc).getType() == Types.trap && guess.get(replace_loc).is_exposed() == false)// try later this : guess.get(replace_loc).Piece_type.get_trap()
                    {
                        guess.put(replace_loc, guess.get(loc));
                        guess.put(loc, p_type);
                        break;
                    }
                }
            }
            case rock:
            {
                for (int replace_loc : guess.keySet())
                {
                    if(replace_loc != loc && guess.get(replace_loc).getType() == Types.rock && guess.get(replace_loc).is_exposed() == false)
                    {
                        guess.put(replace_loc, guess.get(loc));
                        guess.put(loc, p_type);
                        break;
                    }
                }
            }
            case paper:
            {
                for (int replace_loc : guess.keySet())
                {
                    if(replace_loc != loc && guess.get(replace_loc).getType() == Types.paper && guess.get(replace_loc).is_exposed() == false)
                    {
                        guess.put(replace_loc, guess.get(loc));
                        guess.put(loc, p_type);
                        break;
                    }
                }
            }
            case scissors:
            {
                for (int replace_loc : guess.keySet())
                {
                    if(replace_loc != loc && guess.get(replace_loc).getType() == Types.scissors && guess.get(replace_loc).is_exposed() == false)
                    {
                        guess.put(replace_loc, guess.get(loc));
                        guess.put(loc, p_type);
                        break;
                    }
                }
            }
        }
    }

}
