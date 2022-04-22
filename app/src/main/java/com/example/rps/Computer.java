package com.example.rps;

import android.os.Build;
import android.util.Pair;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class Computer extends Player {
    /*
        This class represent the PC side, it extends from Player.
        It contains data of the pieces, guesses of the player pieces etc.
     */
    private int count_scissors;
    private int count_rock;
    private int count_paper;
    private int last_loc = 0;

    protected HashMap<Integer, Piece_type> guess;

    public Computer (Computer computer, Player player)
    {
        super(player, computer);
        this.count_scissors = computer.count_scissors;
        this.count_rock = computer.count_rock;
        this.count_paper = computer.count_paper;
        guess = new HashMap<Integer, Piece_type>();
        for (int key : computer.guess.keySet())
        {
            this.guess.put(key,computer.guess.get(key));
        }
    }
    public Computer(int loc, Board board , GameActivity ga)
    {
        super(loc, ga);
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

    public int getCount_scissors()
    {
        return count_scissors;
    }
    public int getCount_rock()
    {
        return count_rock;
    }
    public int getCount_paper()
    {
        return count_paper;
    }
    private void initComputerPieces(Board board)
    {
        /*
            This function is initializing the PC pieces on the board.
         */
        for (int i = 0; i < 2; i++)
        {
            for (int j = 0; j < 7; j++)
            {
                board.getButtons()[i][j].setText("?");
                //board.showPieces(0,this);
            }
        }
    }

    private void initGuess()
    {
        /*
            This function is initializing all player pieces(by guessing).
         */
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
        /*
            This function update all Guesses.
         */
        for (int loc : blue_pieces.keySet())
        {
            if(blue_pieces.get(loc).is_exposed() == true)
                guess.put(loc,blue_pieces.get(loc));
        }
    }


    public void reportAGuess(int loc, Piece_type p_type)
    {
        /*
            This function receives a location and piece type of checked guess(a guess that 100% expose).
            If the guess at location is this type expose it, otherwise replace to type.
         */
        if (guess.get(loc).getType() != p_type.getType())
            replacePiecesSpecific(loc, p_type);
        guess.get(loc).expose();
    }
    public void removeFromGuess(int loc)
    {
        /*
            This function receives a location of a piece and removes it from the guesses HashMap.
         */
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
        /*
            This function receives 2 locations and change guess location if moved.
         */
        if(guess.get(first_loc).getType() == Types.trap || guess.get(first_loc).getType() == Types.king) // If guess is trap or king, replace their guess because they can't move.
        {
            replacePieces(first_loc);
        }
        guess.put(moved_loc, guess.remove(first_loc));
    }

    public void replacePieces(int loc)
    {
        /*
            This function receives a location of guess and replace king/trap guess with weapon piece.
         */
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
    {
        /*
            This function receives a location and type, and replace piece in guess.
         */
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
                break;
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
                break;
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
                break;
            }
            case scissors:
            {
                for (int replace_loc : guess.keySet() )
                {
                    if(replace_loc != loc && guess.get(replace_loc).getType() == Types.scissors && guess.get(replace_loc).is_exposed() == false)
                    {
                        guess.put(replace_loc, guess.get(loc));
                        guess.put(loc, p_type);
                        break;
                    }
                }
                break;
            }
        }
    }

    public List<Pair<Integer,Integer>> getPossibleMoves(HashMap <Integer, Piece_type> all_pieces, boolean turn)
    {
        List<Pair<Integer,Integer>> moves_list = new ArrayList<Pair<Integer,Integer>>(); // This list contains all possible moves.
        if (turn) // Player turn
        {
            for (int rand_loc : gameActivity.blue_player.getPieces().keySet())
            {
                if (gameActivity.blue_player.getPieces().get(rand_loc).getType() == Types.king || gameActivity.blue_player.getPieces().get(rand_loc).getType() == Types.trap )
                    continue;
                if (all_pieces.get(rand_loc + 7) != null && (rand_loc + 7) % 7 == rand_loc % 7 && gameActivity.blue_player.getPieces().get(rand_loc + 7) == null)
                {
                    moves_list.add(new Pair<>(rand_loc, rand_loc + 7));
//                whichMove(all_pieces, rand_loc, rand_loc + 7, board, tv_text);
//                break;
                }
                if (all_pieces.get(rand_loc + 1) != null && (rand_loc + 1) / 7 == rand_loc / 7 && gameActivity.blue_player.getPieces().get(rand_loc + 1) == null)
                {
                    moves_list.add(new Pair<>(rand_loc, rand_loc + 1));
//                whichMove(all_pieces, rand_loc, rand_loc + 1, board, tv_text);
//                break;
                }
                if (all_pieces.get(rand_loc - 7) != null && (rand_loc - 7) % 7 == rand_loc % 7 && gameActivity.blue_player.getPieces().get(rand_loc - 7) == null)
                {
                    moves_list.add(new Pair<>(rand_loc, rand_loc - 7));
//                whichMove(all_pieces, rand_loc, rand_loc - 7, board, tv_text);
//                break;
                }
                if (all_pieces.get(rand_loc - 1) != null && (rand_loc - 1) / 7 == rand_loc / 7 && gameActivity.blue_player.getPieces().get(rand_loc - 1) == null)
                {
                    moves_list.add(new Pair<>(rand_loc, rand_loc - 1));
//                whichMove(all_pieces, rand_loc, rand_loc - 1, board, tv_text);
//                break;
                }
            }
        }
        else // PC turn.
        {
            for (int rand_loc : this.getPieces().keySet())
            {
                if (this.getPieces().get(rand_loc).getType() == Types.king || this.getPieces().get(rand_loc).getType() == Types.trap )
                    continue;
                if (all_pieces.get(rand_loc + 7) != null && (rand_loc + 7) % 7 == rand_loc % 7 && this.getPieces().get(rand_loc + 7) == null)
                {
                    moves_list.add(new Pair<>(rand_loc, rand_loc + 7));
//                whichMove(all_pieces, rand_loc, rand_loc + 7, board, tv_text);
//                break;
                }
                if (all_pieces.get(rand_loc + 1) != null && (rand_loc + 1) / 7 == rand_loc / 7 && this.getPieces().get(rand_loc + 1) == null)
                {
                    moves_list.add(new Pair<>(rand_loc, rand_loc + 1));
//                whichMove(all_pieces, rand_loc, rand_loc + 1, board, tv_text);
//                break;
                }
                if (all_pieces.get(rand_loc - 7) != null && (rand_loc - 7) % 7 == rand_loc % 7 && this.getPieces().get(rand_loc - 7) == null)
                {
                    moves_list.add(new Pair<>(rand_loc, rand_loc - 7));
//                whichMove(all_pieces, rand_loc, rand_loc - 7, board, tv_text);
//                break;
                }
                if (all_pieces.get(rand_loc - 1) != null && (rand_loc - 1) / 7 == rand_loc / 7 && this.getPieces().get(rand_loc - 1) == null)
                {
                    moves_list.add(new Pair<>(rand_loc, rand_loc - 1));
//                whichMove(all_pieces, rand_loc, rand_loc - 1, board, tv_text);
//                break;
                }
            }
        }
        return moves_list;
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void makeMove(HashMap <Integer, Piece_type> all_pieces, Board board, TextView tv_text, boolean turn) // True if player turn, else false.
    {
        /*
            This function makes a move by using MCTS and Heuristic function.
         */
        Pair<Integer,Integer> chosen_move;
//        chosen_move = MCTS_Node.StartMCTS(gameActivity.blue_player, this);
//        whichMove(all_pieces, chosen_move.first, chosen_move.second, board, tv_text );
        List<Pair<Integer,Integer>> moves_list = getPossibleMoves(all_pieces, turn); // This list contains all possible moves.
        if (!moves_list.isEmpty())
        {
            int i = chooseBestMove(moves_list, turn);
            whichMove(all_pieces, moves_list.get(i).first, moves_list.get(i).second,board,tv_text );
        }
        //whichMove(all_pieces, first_loc, rand_loc, board);
    }

    public List<Pair<Integer,Integer>> get_all_moves_sorted(boolean turn)
    {
        List<Pair<Integer,Integer>> moves = getPossibleMoves(gameActivity.all_pieces, turn); // This list contains all possible moves.
        sort_move_list(moves, turn);
        return moves;
    }


    public Pair<Pair<Integer,Integer>,Double> getBestMove(boolean turn)
    {
        double best_rate;
        List<Pair<Integer,Integer>> moves = getPossibleMoves(gameActivity.all_pieces, turn); // This list contains all possible moves.
        best_rate = sort_move_list(moves, turn);
        return new Pair<>(moves.get(0),best_rate);
    }


    public double sort_move_list(List<Pair<Integer,Integer>> moves_list, boolean turn)
    {

        Simulation new_simulation;
        Simulation simulation = new Simulation(gameActivity.blue_player, this, gameActivity.all_pieces, 0, turn, false, 0 , 0);
        double rate_of_current_pos = simulation.getRate();
        double rate_of_moved_loc;
        double [] rates = new double[moves_list.size()];
        double temp;
        Pair<Integer,Integer> temp_p;
        for (int i = 0; i < moves_list.size(); i++)
        {
            new_simulation = new Simulation(gameActivity.blue_player, this, gameActivity.getCopy_of_All_pieces(),0, turn, true, moves_list.get(i).first , moves_list.get(i).second);
            new_simulation.doMove(moves_list.get(i).first , moves_list.get(i).second);
            rate_of_moved_loc = new_simulation.getRate();
            rate_of_current_pos = rate_of_moved_loc - rate_of_current_pos;
            rates[i] = rate_of_current_pos;
        }
        for (int i = 0; i < moves_list.size() - 1; i++)
        {
            for (int j = i; j < moves_list.size(); j++)
            {
                if (rates[i] < rates[j])
                {
                    temp = rates[j];
                    rates[j] = rates[i];
                    rates[i] = temp;
                    temp_p = moves_list.get(j);
                    moves_list.set(j, moves_list.get(i));
                    moves_list.set(i, temp_p);
                }
            }
        }
        for (int i = 0; i < moves_list.size() - 1; i++)
            System.out.println("Move number - " + i + " at location : " + moves_list.get(i).first + " to: " + moves_list.get(i).second + " rate = " + rates[i]);
        return rates[0];
    }

    private int chooseBestMove(List<Pair<Integer,Integer>> moves_list, boolean turn)
    {
        /*
            This function receives list of moves and turn(True if player turn, false if PC turn).
            This function returns the best move based on the biggest difference of all moves.
         */
        Simulation new_simulation;
        Simulation simulation = new Simulation(gameActivity.blue_player, this, gameActivity.getCopy_of_All_pieces(),0, true, false, 0 , 0);
        double rate_of_current_pos = simulation.getRate();
        double best_move_rate = -999999999;
        int index_of_best_move = 0;
        double rate_of_moved_loc;
        System.out.println("________________________");
        for (int i = 0; i < moves_list.size(); i++)
        {
            new_simulation = new Simulation(gameActivity.blue_player, this, gameActivity.getCopy_of_All_pieces(),0, false, true, moves_list.get(i).first , moves_list.get(i).second);
            new_simulation.doMove(moves_list.get(i).first , moves_list.get(i).second);
            rate_of_moved_loc = new_simulation.getRate();
            if (rate_of_moved_loc - rate_of_current_pos > best_move_rate)
            {
                best_move_rate = rate_of_moved_loc - rate_of_current_pos;
                index_of_best_move = i;
            }
            System.out.println("Move number - " + i + " at location : " + moves_list.get(i).first + " to: " + moves_list.get(i).second + " rate = " + rate_of_moved_loc);
        }
        return index_of_best_move;
    }

    public void whichMove(HashMap <Integer, Piece_type> all_pieces, int first_loc, int move_loc, Board board, TextView tv_text)
    {
        /*
            This function make a move or start a fight.
         */
        if (all_pieces.get(move_loc).getType() == Types.empty)
        {
            Piece_type p_type = this.getPieces().remove(first_loc);
            this.getPieces().put(move_loc, p_type);
            all_pieces.put(first_loc, Piece_type.get_empty());
            all_pieces.put(move_loc, p_type);
            board.updateButton(move_loc / 7, move_loc % 7, p_type.getType(), false, this.getPieces().get(move_loc).is_exposed());//update on board
            board.clearButton(first_loc / 7, first_loc % 7);
        }
        else
        {
            Piece_type computer_piece = all_pieces.get(first_loc);
            Piece_type player_piece = all_pieces.get(move_loc);
            if (player_piece.getType() == Types.trap)
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
                        // להפוך את הנשק לגלוי
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
                        // להפוך את הנשק לגלוי
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
                        // להפוך את הנשק לגלוי
                    }
                    if(computer_piece.getType() == Types.scissors)
                    {
                        clearAfterFight(move_loc, first_loc, false);
                    }
                }
                if (player_piece.getType() == Types.king)
                {
                    clearAfterFight(move_loc, first_loc, false);
                    gameActivity.create_lose_dialog();
                }
//                if(computer_piece.getType() == Types.trap)
//                {
//                    clearAfterFight(move_loc, first_loc, true, tv_text);
//                }

            }
            else
            {
                // לעשות את הבחירה מחדש של המחשב
                gameActivity.create_tie_dialog(computer_piece, move_loc, first_loc);
                //whichMove(all_pieces, first_loc, move_loc, board, tv_text);
                //startFight(player_loc, computer_loc);
                // לזמן קרב שוב עם החדשים
            }
        }
    }

    public void clearAfterFight(int player_loc, int computer_loc, boolean winner) // true if player won, false if computer won.
    {
        /*
            This function updates data after moving a piece.
         */
        if (gameActivity.blue_player.getPieces().get(player_loc).getType() == Types.trap) {
            gameActivity.tv_text.setTextColor(0xff0000ff);
            gameActivity.tv_text.setText("Red player fell into blue " + Types.trap.toString());
            //blue_player.getPieces().remove(player_loc);
            this.getPieces().remove(computer_loc);
            reportAGuess(player_loc, gameActivity.blue_player.getPieces().get(player_loc));
            gameActivity.all_pieces.put(computer_loc, Piece_type.get_empty());
            gameActivity.board.clearButton(computer_loc / 7, computer_loc % 7);// remove moved piece from last location
        } else {
            if (winner) // If player won fight.
            {
                Piece_type p_type = gameActivity.blue_player.getPieces().get(player_loc);
                gameActivity.blue_player.getPieces().get(player_loc).expose();

                gameActivity.tv_text.setTextColor(0xff0000ff);
                gameActivity.tv_text.setText("Blue won with " + p_type.getType().toString());

                gameActivity.all_pieces.put(computer_loc, Piece_type.get_empty());
                //blue_player.getPieces().remove(player_loc);
                this.getPieces().remove(computer_loc);
                reportAGuess(player_loc, p_type);
                gameActivity.board.updateButton(player_loc / 7, player_loc % 7, p_type.getType(), true, true);
                gameActivity.board.clearButton(computer_loc / 7, computer_loc % 7);// remove moved piece from last location
            }
            else // If PC won fight.
            {
                Piece_type p_type = this.getPieces().remove(computer_loc);
                gameActivity.tv_text.setTextColor(0xffff0000);
                gameActivity.tv_text.setText("Red won with " + p_type.getType().toString());
                this.getPieces().put(player_loc, p_type);
                this.getPieces().get(player_loc).expose();
                gameActivity.all_pieces.put(computer_loc, Piece_type.get_empty());
                gameActivity.all_pieces.put(player_loc, p_type);
                gameActivity.all_pieces.get(player_loc).expose();
                reportAGuess(player_loc, gameActivity.blue_player.getPieces().remove(player_loc));
                removeFromGuess(player_loc);
                gameActivity.board.updateButton(player_loc / 7, player_loc % 7, p_type.getType(), false, true);//update on board
                gameActivity.board.clearButton(computer_loc / 7, computer_loc % 7);// remove moved piece from last location
            }
        }
    }


    public void makeMoveMCTS(Pair<Integer,Integer> move, boolean turn)
    {
        Simulation simulation = new Simulation(gameActivity.blue_player, this, gameActivity.all_pieces,0, turn, false, move.first , move.second);
        simulation.doMove(move.first,move.second);
    }

    public void chooseNewPiece(int computer_loc)
    {
        /*
            This function picks a new weapon to piece if tie occurred.
         */
        boolean flag = false;
        Random rand = new Random();
        int chosen = -1;
        if (computer_loc == last_loc)
        {
            chosen = rand.nextInt(3);
        }
        else
        {
            if (count_paper > count_rock)
            {
                if (count_paper > count_scissors)
                {
                    chosen = 0;
                    flag = true;
                }
            }
            if (count_scissors > count_paper)
            {
                if (count_scissors > count_rock)
                {
                    chosen = 0;
                    flag = true;
                }
            }
            if (count_rock > count_scissors)
            {
                if (count_rock > count_paper)
                {
                    chosen = 0;
                    flag = true;
                }
            }
            if (!flag)
                chosen = rand.nextInt(3);
        }
        last_loc = computer_loc;
        switch (chosen)
        {
            case 0:
            {
                updateAfterTie(computer_loc, Piece_type.get_scissors());
                break;
            }
            case 1:
            {
                updateAfterTie(computer_loc, Piece_type.get_rock());
                break;
            }
            case 2:
            {
                updateAfterTie(computer_loc, Piece_type.get_paper());
                break;
            }
        }
    }

    private void updateAfterTie(int computer_loc, Piece_type p_type)
    {
        /*
            This function updates the PC piece after a tie.
         */
        this.getPieces().put(computer_loc, p_type);
        gameActivity.all_pieces.put(computer_loc, p_type);
        gameActivity.board.updateButton(computer_loc / 7, computer_loc % 7, p_type.getType(), false, true);//update on board
    }
    public void updateGuessAfterTie(int player_loc, Piece_type p_type, Types chosen)
    {
        /*
            This function updates guess after tie.
         */
        switch (p_type.getType())
        {
            case paper:
            {
                count_paper -= 1;
                break;
            }
            case scissors:
            {
                count_scissors -= 1;
                break;
            }
            case rock:
            {
                count_rock -= 1;
                break;
            }
        }
        switch (chosen)
        {
            case rock:
            {
                count_rock ++;
                break;
            }
            case scissors:
            {
                count_scissors ++;
                break;
            }
            case paper:
            {
                count_paper ++;
                break;
            }
        }
    }
}
