package com.example.rps;

import java.util.HashMap;
import java.util.Random;

public class Simulation
{
    /*
        This class Runs a simulation of a game without hurts the source game.
        It contains a data of both players(player and PC) and heuristic function.
     */
    private Player blue_player;
    private Computer computer;
    private HashMap <Integer, Piece_type> all_pieces;

    private int depth;
    private double rate;
    private boolean turn; // If turn = true - player turn. else PC turn.
    int near_arr[] = {7, 1, -7, -1};
    int diagonal_arr[] = {-8, -6, 8, 6};
    int last_PC_loc;
    int far_arr[] = {-16,-15,-14,-13,-12,-9,-2,5,2,9,12,13,14,15,16};
    int super_far_arr[] = {-24,-23,-22,-21,-20,-19,-18, -17,-10,-3, 4,11, -11,-4,3,10,17,18,19,20,21,22,23,24};

    public Simulation(Player blue_player, Computer computer, HashMap<Integer, Piece_type> all_pieces, boolean turn, boolean copy) // Copy = true if needs to create copy values of players, else false.
    {
        if (copy) // Need to create copy of blue player and computer.
        {
            this.blue_player = new Player(blue_player);
            this.computer = new Computer(computer, blue_player);
            this.all_pieces = all_pieces;
            this.turn = turn;

        }
        else
        {
            this.blue_player = blue_player;//new Player(blue_player);//
            this.computer = computer;//new Computer(computer);//
            this.all_pieces = all_pieces;
            this.turn = turn;
        }
    }

    public void doMove(int first_loc, int move_loc)
    {
        /*
            This function receives 2 locations.
            It makes the move based on the data we have and update the copy data
         */
        if (!turn) // If PC turn
        {
            if (this.all_pieces.get(move_loc).getType() == Types.empty)
            {
                Piece_type p_type = this.computer.getPieces().remove(first_loc);
                this.computer.getPieces().put(move_loc, p_type);
                all_pieces.put(first_loc,Piece_type.get_empty());
                all_pieces.put(move_loc, p_type);
            }
            else
            {
                Piece_type computer_piece =  computer.getPieces().get(first_loc);
                Piece_type player_piece =  blue_player.getPieces().get(move_loc);
                if (player_piece.getType() == Types.trap)
                {
                    clearAfterFight(move_loc, first_loc, true);
                }
                if(player_piece.getType() != computer_piece.getType())
                {
                    if (player_piece.getType() == Types.rock)
                    {
                        if (computer_piece.getType() == Types.scissors)
                        {
                            clearAfterFight(move_loc, first_loc, true);
                        }
                        if(computer_piece.getType() == Types.paper)
                        {
                            clearAfterFight(move_loc, first_loc, false);
                        }
                    }
                    if (player_piece.getType() == Types.scissors)
                    {
                        if (computer_piece.getType() == Types.paper)
                        {
                            clearAfterFight(move_loc, first_loc, true);
                        }
                        if(computer_piece.getType() == Types.rock)
                        {
                            clearAfterFight(move_loc, first_loc, false);
                        }
                    }
                    if (player_piece.getType() == Types.paper)
                    {
                        if (computer_piece.getType() == Types.rock)
                        {
                            clearAfterFight(move_loc, first_loc, true);
                        }
                        if(computer_piece.getType() == Types.scissors)
                        {
                            clearAfterFight(move_loc, first_loc, false);
                        }
                    }
                    if (player_piece.getType() == Types.king)
                    {
                        clearAfterFight(move_loc, first_loc, false);
                    }


                }
                else
                    chooseNewPieces(first_loc, move_loc);

            }
        }
        else // להוסיף כזה כשתור השחקן.
        {
            if (this.all_pieces.get(move_loc).getType() == Types.empty)
            {
                Piece_type p_type = this.blue_player.getPieces().remove(first_loc);
                this.blue_player.getPieces().put(move_loc, p_type);
                all_pieces.put(first_loc,Piece_type.get_empty());
                all_pieces.put(move_loc, p_type);
                computer.updateMove(first_loc, move_loc);

            }
            else
            {
                Piece_type computer_piece =  computer.getPieces().get(move_loc);
                Piece_type player_piece =  blue_player.getPieces().get(first_loc);
                if (computer_piece.getType() == Types.trap)
                {
                    clearAfterFight(move_loc, first_loc, false);
                }
                if(player_piece.getType() != computer_piece.getType())
                {
                    if (player_piece.getType() == Types.rock)
                    {
                        if (computer_piece.getType() == Types.scissors)
                        {
                            clearAfterFight(move_loc, first_loc, true);
                        }
                        if(computer_piece.getType() == Types.paper)
                        {
                            clearAfterFight(move_loc, first_loc, false);
                        }
                    }
                    if (player_piece.getType() == Types.scissors)
                    {
                        if (computer_piece.getType() == Types.paper)
                        {
                            clearAfterFight(move_loc, first_loc, true);
                        }
                        if(computer_piece.getType() == Types.rock)
                        {
                            clearAfterFight(move_loc, first_loc, false);
                        }
                    }
                    if (player_piece.getType() == Types.paper)
                    {
                        if (computer_piece.getType() == Types.rock)
                        {
                            clearAfterFight(move_loc, first_loc, true);
                        }
                        if(computer_piece.getType() == Types.scissors)
                        {
                            clearAfterFight(move_loc, first_loc, false);
                        }
                    }
                    if (computer_piece.getType() == Types.king)
                    {
                        clearAfterFight(move_loc, first_loc, true);
                    }
                }
                else
                    chooseNewPieces(first_loc, move_loc);

            }
        }
    }

    public void chooseNewPieces(int first_loc, int move_loc)
    {
        /*
            This function receives two moves and in case there was a tie, choose new weapons to both sides.
         */
        Random rand = new Random();
        int chosen = -1;
        boolean flag = false;

        if (first_loc == last_PC_loc && !turn)
        {
            chosen = rand.nextInt(3);
        }
        else
        {
            if (computer.getCount_paper() > computer.getCount_rock())
            {
                if (computer.getCount_paper() > computer.getCount_scissors())
                {
                    chosen = 0;
                    flag = true;
                }
            }
            if (computer.getCount_scissors() > computer.getCount_paper())
            {
                if (computer.getCount_scissors() > computer.getCount_rock())
                {
                    chosen = 0;
                    flag = true;
                }
            }
            if (computer.getCount_rock() > computer.getCount_scissors())
            {
                if (computer.getCount_rock() > computer.getCount_paper())
                {
                    chosen = 0;
                    flag = true;
                }
            }
            if (!flag)
                chosen = rand.nextInt(3);
        }
        if (!turn)
            last_PC_loc = first_loc;
        // Choose new weapon to player.
        // Choose new weapon to PC.
        switch (chosen) // Choose new weapon to PC.
        {
            case 0:
            {
                updateAfterTie(first_loc, move_loc, Piece_type.get_scissors(), false, false);
                break;
            }
            case 1:
            {
                updateAfterTie(first_loc, move_loc, Piece_type.get_rock(), false, false);
                break;
            }
            case 2:
            {
                updateAfterTie(first_loc, move_loc,Piece_type.get_paper(), false, false);
                break;
            }
        }
        chosen = rand.nextInt(3);
        switch (chosen) // Choose new weapon to player.
        {
            case 0:
            {
                updateAfterTie(first_loc, move_loc, Piece_type.get_scissors(), true, true);
                break;
            }
            case 1:
            {
                updateAfterTie(first_loc, move_loc, Piece_type.get_rock(), true, true);
                break;
            }
            case 2:
            {
                updateAfterTie(first_loc, move_loc, Piece_type.get_paper(), true, true);
                break;
            }
        }
    }

    private void updateAfterTie(int first_loc, int move_loc, Piece_type p_type , boolean is_player /* True if player piece, false if PC piece */, boolean start_fight /* If true, start fight again with new pieces */)
    {
        if (turn) // If player move.
        {
            if (!is_player) // If PC piece.
                computer.getPieces().put(move_loc, p_type);
            else
                blue_player.getPieces().put(first_loc,p_type);
        }
        else
        {
            if (!is_player) // If PC piece.
                computer.getPieces().put(first_loc, p_type);
            else
                blue_player.getPieces().put(move_loc,p_type);
        }
        if (start_fight)
            doMove(first_loc, move_loc);
    }

    private void clearAfterFight(int move_loc, int first_loc, boolean winner) // True if player won, else false.
    {
        /*
            This function receives two locations of pieces and a winner.
            It updates the copy data according to the winne.
         */
        if (turn) // If player turn.
        {
            if (winner) // If player won PC = true, else false. // Player attacks and win.
            {
                computer.getPieces().remove(move_loc);

                Piece_type piece_type = blue_player.getPieces().remove(first_loc);
                computer.reportAGuess(first_loc, piece_type);
                blue_player.getPieces().put(move_loc, piece_type);
                computer.updateMove(first_loc, move_loc);
                //blue_player.getPieces().get(move_loc).expose();
                all_pieces.put(first_loc,Piece_type.get_empty());
                all_pieces.put(move_loc, piece_type);
            }
            else // Player attacks and lose.
            {
                //computer.getPieces().get(move_loc).expose();
                //System.out.println(" " + move_loc + computer.getPieces().get(move_loc).getType());
                computer.reportAGuess(first_loc, blue_player.getPieces().remove(first_loc));
                computer.removeFromGuess(first_loc);
                all_pieces.put(first_loc,Piece_type.get_empty());
            }
        }

        else // If PC turn.
        {
            if (winner) // If player won PC = true, else false. // PC attacks and lose.
            {
                //blue_player.getPieces().get(move_loc).expose();
                computer.reportAGuess(move_loc,blue_player.getPieces().get(move_loc));
                computer.getPieces().remove(first_loc);
                all_pieces.put(first_loc,Piece_type.get_empty());
            }
            else // PC attacks and win.
            {
                computer.reportAGuess(move_loc, blue_player.getPieces().remove(move_loc));
                computer.removeFromGuess(move_loc);
                computer.getPieces().put(move_loc, computer.getPieces().remove(first_loc));
                //computer.getPieces().get(move_loc).expose();
                all_pieces.put(first_loc,Piece_type.get_empty());
                all_pieces.put(move_loc, computer.getPieces().get(move_loc));
            }
        }
    }

    public double getRate()
    {
        return HeuristicFunction();
    }

    public double HeuristicFunction()
    {
        /*
            This function rate t board based on both players data.
            It determinants the rahete according to some strategies.
            The first, if flag has been eaten then return max score.
            The second, is the amount of pieces for each side, if the piece is hidden the value of the piece is bigger.
            The third, for PC checks pieces distance from the guess of enemy flag.
            The fourth, checks how strong is the defence on the flag.
            The fifth, checks for enemy pieces that threat the flag.
            The sixth, checks domination on the board.
         */
        int winner = is_over(); // First strategy.
        if (winner != 0)
        {
            if (winner == 1)
                return 10000000;
            return -10000000;
        }
        double score = 0;
        int king_loc = 0;
        if (!turn) // PC turn.
        {
            // Second strategy.
            for (int key : computer.getPieces().keySet())
            {
                if (computer.getPieces().get(key).getType() == Types.king)
                    king_loc = key;
                if (computer.getPieces().get(key).is_exposed() == false)
                    score += 0.2;
                score += 3;
            }
            for (int key : blue_player.getPieces().keySet())
            {
                if (blue_player.getPieces().get(key).is_exposed() == false)
                    score -= 0.2;
                score -= 3;
            }
            score += distanceFromKing(); // Third strategy.

        }
        else // Player turn.
            {
                // Second strategy.
                for (int key : blue_player.getPieces().keySet())
                {
                    if (blue_player.getPieces().get(key).getType() == Types.king)
                        king_loc = key;
                    if (blue_player.getPieces().get(key).is_exposed() == false)
                        score += 0.2;
                    score += 2;
                }
                for (int key : computer.getPieces().keySet())
                {
                    if (computer.getPieces().get(key).is_exposed() == false)
                        score -= 0.2;
                    score -= 2;
                }

            }
        score += kingSafe(king_loc); // Reduce the score from that function, fourth strategy.
        score += kingHostiles(king_loc); // Check plus, diagonal and bigger, fifth strategy.
        score += domination(); // Sixth strategy.


        return score;
    }

    private double kingHostiles(int king_loc)
    {
        /*
            This function checks for enemies pieces near flag.
            And returns score.
         */
        double value = 0;
        if (turn) // Player turn
        {
            for (int i : near_arr)
            {
                if (computer.getPieces().get(king_loc + i) != null)
                    value -= 8;
            }

            for (int i : diagonal_arr)
            {
                if (computer.getPieces().get(king_loc + i) != null)
                {
                    value -= 4;
                }
            }
            for (int i : far_arr)
            {
                if (computer.getPieces().get(king_loc + i) != null)
                {
                    value -= 2;
                }
            }
        }
        else // PC turn.
        {
            for (int i : near_arr)
            {
                if (blue_player.getPieces().get(king_loc + i) != null)
                   value -= 8;
            }

            for (int i : diagonal_arr)
            {
                if (blue_player.getPieces().get(king_loc + i) != null)
                    value -= 4;
            }
            for (int i : far_arr)
            {
                if (blue_player.getPieces().get(king_loc + i) != null)
                    value -= 2;
            }
        }
        return value;
    }

    private double distanceFromKing()
    {
        /*
            This function checks pieces distance from guess flag.
            Return score based on distance.
         */
        int king_guess_loc = 0;
        for (int key : computer.guess.keySet())
        {
            if (computer.guess.get(key).getType() == Types.king)
                king_guess_loc = key;
        }
        for (int i : near_arr)
        {
            if (computer.getPieces().get(king_guess_loc + i) != null)
            {
                return 4;
            }
        }
        for (int i : diagonal_arr)
        {
            if (computer.getPieces().get(king_guess_loc + i) != null)
            {
                return 3.5;
            }
        }
        for (int i : far_arr)
        {
            if (computer.getPieces().get(king_guess_loc + i) != null)
            {
                return 3;
            }
        }
        for(int i :super_far_arr)
        {
            if (computer.getPieces().get(king_guess_loc + i) != null)
            {
                return 2.5;
            }
        }
        return 0;
    }

    private double domination()
    {
        /*
            This function checks if the pieces spread on the board smartly, rate it and return.
         */
        double value = 0;
        Piece_type p_type;
        if (turn) // Player turn
        {

            for (int key: computer.getPieces().keySet())
            {
                if (computer.getPieces().get(key).is_exposed() == true && computer.getPieces().get(key).getType() != Types.trap)
                {
                    p_type = computer.getPieces().get(key);
                    for (int i : near_arr)
                    {
                        if (blue_player.getPieces().get(key + i) != null)
                        {
                            value += winner(p_type, blue_player.getPieces().get(key + i));
                        }
                    }
                    for (int i : diagonal_arr)
                    {
                        if (blue_player.getPieces().get(key + i) != null)
                        {
                            value += winner(p_type, blue_player.getPieces().get(key + i));
                        }
                    }
                }
            }

        }
        else
        {
            for (int key: blue_player.getPieces().keySet())
            {
                if (blue_player.getPieces().get(key).is_exposed() == true && blue_player.getPieces().get(key).getType() != Types.trap)
                {
                    p_type = blue_player.getPieces().get(key);
                    for (int i : near_arr)
                    {
                        if (computer.getPieces().get(key + i) != null)
                        {
                            value += winner(p_type, computer.getPieces().get(key + i));
                        }
                    }
                    for (int i : diagonal_arr)
                    {
                        if (computer.getPieces().get(key + i) != null)
                        {
                            value += winner(p_type, computer.getPieces().get(key + i));
                        }
                    }
                }
            }
        }
        return value;
    }

    private double winner(Piece_type p_enemy_type, Piece_type p_your_type)
    {
        /*
            This function receives 2 weapons and return score of the fight.
         */
        switch (p_enemy_type.getType())
        {
            case rock:
            {
                if (p_your_type.getType() == Types.scissors)
                    return -0.1;
                if (p_your_type.getType() == Types.paper)
                {
                    if (p_your_type.is_exposed() == false)
                        return 1;
                    return 0.5;
                }
                else
                    return 0.2;
            }
            case paper:
            {
                if (p_your_type.getType() == Types.rock)
                    return -0.1;
                if (p_your_type.getType() == Types.scissors)
                {
                    if (p_your_type.is_exposed() == false)
                        return 1;
                    return 0.5;
                }
                else
                    return 0.2;
            }
            case scissors:
            {
                if (p_your_type.getType() == Types.paper)
                    return -0.1;
                if (p_your_type.getType() == Types.rock)
                {
                    if (p_your_type.is_exposed() == false)
                        return 1;
                    return 0.5;
                }
                else
                    return 0.2;
            }
        }
        return 0;
    }
    private double kingSafe(int king_loc)
    {
        /*
            This function receives flag location and return score based on his protection level.
         */
        double value = 0;
        int c_scissors = 0, c_rock = 0, c_paper = 0;
        if (turn) // Player turn
        {
            for (int i : near_arr) {
                if (blue_player.getPieces().get(king_loc + i) != null) {
                    if (blue_player.getPieces().get(king_loc + i).getType() == Types.trap)
                        value += 2;
                    if (blue_player.getPieces().get(king_loc + i).getType() == Types.rock)
                        if (c_rock < 2) {
                            c_rock++;
                            value++;
                        }
                    if (blue_player.getPieces().get(king_loc + i).getType() == Types.scissors)
                        if (c_scissors < 2) {
                            c_scissors++;
                            value++;
                        }
                    if (blue_player.getPieces().get(king_loc + i).getType() == Types.paper)
                        if (c_paper < 2) {
                            c_paper++;
                            value++;
                        }
                }
            }

            for (int i : diagonal_arr) {
                if (blue_player.getPieces().get(king_loc + i) != null) {
                    if (blue_player.getPieces().get(king_loc + i).getType() == Types.trap)
                        value++;
                    if (blue_player.getPieces().get(king_loc + i).getType() == Types.rock)
                        if (c_rock < 2) {
                            c_rock++;
                            value++;
                        }
                    if (blue_player.getPieces().get(king_loc + i).getType() == Types.scissors)
                        if (c_scissors < 2) {
                            c_scissors++;
                            value++;
                        }
                    if (blue_player.getPieces().get(king_loc + i).getType() == Types.paper)
                        if (c_paper < 2) {
                            c_paper++;
                            value++;
                        }
                }

            }
        }
        else // PC turn.
            {
                for (int i : near_arr) {
                    if (computer.getPieces().get(king_loc + i) != null) {
                        if (computer.getPieces().get(king_loc + i).getType() == Types.trap)
                            value += 2;
                        if (computer.getPieces().get(king_loc + i).getType() == Types.rock)
                            if (c_rock < 2) {
                                c_rock++;
                                value++;
                            }
                        if (computer.getPieces().get(king_loc + i).getType() == Types.scissors)
                            if (c_scissors < 2) {
                                c_scissors++;
                                value++;
                            }
                        if (computer.getPieces().get(king_loc + i).getType() == Types.paper)
                            if (c_paper < 2) {
                                c_paper++;
                                value++;
                            }
                    }
                }

                for (int i : diagonal_arr)
                {
                    if (computer.getPieces().get(king_loc + i) != null)
                    {
                        if (computer.getPieces().get(king_loc + i).getType() == Types.trap)
                            value++;
                        if (computer.getPieces().get(king_loc + i).getType() == Types.rock)
                            if (c_rock < 2)
                            {
                                c_rock++;
                                value++;
                            }
                        if (computer.getPieces().get(king_loc + i).getType() == Types.scissors)
                            if (c_scissors < 2)
                            {
                                c_scissors++;
                                value++;
                            }
                        if (computer.getPieces().get(king_loc + i).getType() == Types.paper)
                            if (c_paper < 2)
                            {
                                c_paper++;
                                value++;
                            }
                    }

                }
            }
        return value;
    }

    private int is_over()
    {
        /*
            This function returns 1 if player won, -1 if PC won and 0 if nobody won.
         */
        int value = 1;
        if (turn) // Player turn.
        {
            for (Piece_type piece_type : computer.getPieces().values())
            {
                if (piece_type.getType() == Types.king)
                {
                    value = -1;
                }
            }
            if(value == 1)
                return value;
            for (Piece_type piece_type : blue_player.getPieces().values())
            {
                if (piece_type.getType() == Types.king)
                {
                    value = 0;
                }
            }
        }

        else // PC turn.
        {
            for (Piece_type piece_type : blue_player.getPieces().values())
            {
                if (piece_type.getType() == Types.king)
                {
                    value = -1;
                }
            }
            if(value == 1)
                return value;
            for (Piece_type piece_type : computer.getPieces().values())
            {
                if (piece_type != null && piece_type.getType() == Types.king)
                {
                    value = 0;
                }
            }
        }

        return value;
    }
}
