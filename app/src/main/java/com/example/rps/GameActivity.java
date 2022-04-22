package com.example.rps;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Random;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {
    LinearLayout linearLayoutBoard, two_buttons;
    Board board;
    int column, row, turn, first_click_loc;
    BoardHandler bh;
    Dialog d_lose, d_win, d_tie;
    TextView tvbombsleft_d, tv_score_d;
    Button homebtn_lose, homebtn_win, btn_flag;
    ImageButton re_scissors, re_rock, re_paper;
    boolean second_move = false, is_first = true, is_sec = true, first_move;
    TextView tv_text, tv_type_chose;
    String userName;
    Player red_player, blue_player;
    Computer computer;
    HashMap <Integer, Piece_type> all_pieces;
    Pair<Integer,Integer> first_click_pair, second_click_pair;
    Types chosen;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        //column = Integer.valueOf(getIntent().getExtras().getString("column"));
        //row = Integer.valueOf(getIntent().getExtras().getString("row"));
        all_pieces = new HashMap<Integer,Piece_type>();
        Random rand = new Random();
        turn = rand.nextInt(2);
        //turn = 1;
        first_move = true;
        row = 6;
        column = 7;
        red_player = new Player(0, this);
        blue_player = new Player(4, this);
        linearLayoutBoard = (LinearLayout) findViewById(R.id.board);
        board = new Board(this, linearLayoutBoard, column, row);
        //bh = new BoardHandler(column, row);
        computer = new Computer(0, board, this);
        //System.out.println(bh.toString());
        //btn_flag.setOnClickListener(this);
        tv_text = (TextView)findViewById(R.id.tvtext);
        userName = getIntent().getExtras().getString("userName");

    }

    @Override
    protected void onStart()
    {
        // This function is auto active.
        // Initialize the board.
        super.onStart();
        board.setClickable(true);
        board.setListener();
        Toast.makeText(this, "Place your flag", Toast.LENGTH_SHORT).show();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("ResourceType")
    @Override
    public void onClick(View v)
    {
        /*
         This function checks which button was pressed and operate accordingly.
         This function receives the view that pressed.
         */
        if(v.getId() == 1)
        {
            // Spread pieces again randomly.
            blue_player.resetPieces(column * 4);
            blue_player.spread_pieces(column * 4);
            board.showPieces(column * 4, blue_player);
        }
        else
            if (v.getId() == 2)
            {
                // Start clean the activity from the buttons and start the game.
                board.removeTwoButtons(two_buttons);
                for(int i = 0; i < 14; i++)
                {
                    all_pieces.put(i,computer.getPieces().get(i));
                }
                for(int i = 14; i < 28; i++)
                {
                    all_pieces.put(i, Piece_type.get_empty());
                }
                for(int i = 28; i < 42 ; i++)
                {
                    all_pieces.put(i,blue_player.getPieces().get(i));
                }
                if (turn == 1)
                {
                    tv_text.setTextColor(0xff0000ff);
                    tv_text.setText("Blue start");
                }
                else {
                    tv_text.setText("Red start");
                    tv_text.setTextColor(0xffff0000);
                    computer.makeMove(all_pieces, board, tv_text, false);
                    turn = 1;
                }
            }
        else
            {
            if (is_first)
            {
                // If this is the first click place the flag(king).
                Pair<Integer, Integer> pair = getButtonPos(board, (Button) v);
                int loc = pair.first * column + pair.second;
                if (loc > 27)
                {
                    blue_player.getPieces().put(loc, Piece_type.get_king_h());
                    // blue_player.getPieces().get(loc).hide();
                    ((Button) v).setText("⛿");
                    is_first = false;
                    Toast.makeText(this, "Place your trap", Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(this, "You can't place there! \n try again", Toast.LENGTH_SHORT).show();
            }
            else
                {
                if (is_sec)
                {
                    // If this is the second click place trap.
                    Pair<Integer, Integer> pair = getButtonPos(board, (Button) v);
                    int loc = pair.first * column + pair.second;
                    if (loc > 27 &&  ((Button) v).getText().toString().equals("")) {
                        blue_player.getPieces().put(loc, Piece_type.get_trap_h());
                        //blue_player.getPieces().get(loc).hide();
                        ((Button) v).setText("T");
                        is_sec = false;
                        blue_player.spread_pieces(column * 4); // spread player pieces randomly.
                        board.showPieces(column * 4, blue_player);
                        two_buttons = board.addTwoButtons(); // add two buttons to layout.
                    }
                    else
                        Toast.makeText(this, "You can't place there! \n try again", Toast.LENGTH_SHORT).show();
                }
                else
                {

//                    if(v == re_scissors)
//                    {
//                        int loc = first_click_pair.first * column + first_click_pair.second;
//                        chosen = Types.scissors;
//                        update_player_after_tie(loc, loc);
//                    }
//                    if(v == re_rock)
//                    {
//                        int loc = first_click_pair.first * column + first_click_pair.second;
//                        chosen = Types.rock;
//                        update_player_after_tie(loc, loc);
//                    }
//                    if(v == re_paper)
//                    {
//                        int loc = first_click_pair.first * column + first_click_pair.second;
//                        chosen = Types.paper;
//                        update_player_after_tie(loc, loc);
//                        //d_tie.dismiss();
             //       }
                    if (v == homebtn_lose)
                    {
                        Intent intent = new Intent(this, MainActivity.class);
                        startActivity(intent);
                    }
                    //else


                    /*
                     To make a move the player must press two buttons.
                     The first - the piece you would like to move.
                     The second - where would you like the piece to go.
                    */
                    if(!first_move)
                    {
                        second_move = second_move((Button) v);
                        if (second_move)
                        {
//                                turn = 0;
//                                computer.makeMove(all_pieces, board, tv_text);
                        }
                        first_move = true;
                    }
                    else if(first_move)
                        turn = 1;
                    first_move = !first_move((Button) v);
                }
            }
        }
    }
//else
//                            {
//                                create_lose_dialog();
//                            }


    public boolean first_move(Button b)
    {
        /*
            This function receives a button and checks if the button is valid to press,
            Mark the places that the piece can go.
            Return true or false if valid move or not.
        */
        Pair<Integer, Integer> pair = getButtonPos(board, (Button) b);
        int loc = pair.first * column + pair.second;
        if (turn == 1) // Player turn.
        {
            if(blue_player.getPieces().get(loc) == null) // Must move your pieces.
            {
                Toast.makeText(this, "You can move your pieces only", Toast.LENGTH_SHORT).show();
                return false;
            }
            if(blue_player.getPieces().get(loc).getType() == Types.trap) // Traps can't move.
            {
                Toast.makeText(this, "You can't move your trap", Toast.LENGTH_SHORT).show();
                return false;
            }
            if(blue_player.getPieces().get(loc).getType() == Types.king) // Flags can't move.
            {
                Toast.makeText(this, "You can't move your flag", Toast.LENGTH_SHORT).show();
                return false;
            }
            first_click_loc = loc;
            first_click_pair = pair;
            // mark all movable places.
            if(pair.second != 0 && all_pieces.get(loc - 1).getType() == Types.empty) // Mark left
            {
                board.moveAble(pair.first ,pair.second - 1);
            }
            if(pair.second != 6 && all_pieces.get(loc + 1).getType() == Types.empty) // Mark right
            {
                board.moveAble(pair.first ,pair.second + 1);
            }
            if(pair.first != 0 && all_pieces.get(loc - 7).getType() == Types.empty) // Mark above
            {
                board.moveAble(pair.first - 1 ,pair.second);
            }
            if(pair.first != 5 && all_pieces.get(loc + 7).getType() == Types.empty) // Mark under
            {
                board.moveAble(pair.first + 1,pair.second);
            }
            return true;
        }
        else
            Toast.makeText(this, "It is not your turn", Toast.LENGTH_SHORT).show();
            return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public boolean second_move(Button b)
    {
        /*
            This function receives a button, checks if the move is valid and move the piece.
            If the piece move towards empty place it will move, otherwise start fight with the pc piece.
            After that, the computer play it's move.
         */
        Pair<Integer, Integer> pair = getButtonPos(board, (Button) b);
        int loc = pair.first * column + pair.second; // Get the location of player second press.
        second_click_pair = pair;
        if(all_pieces.get(loc).getType() == Types.empty && board.getButtons()[pair.first][pair.second].getText() == "⌾") // Move to empty cell.
        {
            Piece_type p_type = blue_player.getPieces().remove(first_click_loc);
            blue_player.getPieces().put(loc, p_type);
            all_pieces.put(first_click_loc, Piece_type.get_empty());
            all_pieces.put(loc, p_type);
            board.updateButton(pair.first, pair.second, p_type.getType(), true, true); // Update on board
            board.clearButton(first_click_pair.first, first_click_pair.second); // Remove moved piece from last location
            computer.updateMove(first_click_loc, loc);
            turn = 0;
            computer.makeMove(all_pieces, board, tv_text, false); // Computer move.
        }
        else
            if(blue_player.getPieces().get(loc) != null) // Prevent unwanted crushes
            {
                Toast.makeText(this, "Not legal move", Toast.LENGTH_SHORT).show();
                clearButtons(first_click_pair);
                return false;
            }
            else
                if(computer.getPieces().get(loc) != null && (loc == first_click_loc + 1 || loc == first_click_loc - 1 || loc == first_click_loc + 7 || loc == first_click_loc - 7))
                {
                    startFight(first_click_loc, loc); // Start fight.
                }
                else
                    {
                    Toast.makeText(this, "Not legal move", Toast.LENGTH_SHORT).show(); // Unknown move.
                    clearButtons(first_click_pair);
                    return false;
                }

        clearButtons(first_click_pair);
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void startFight(int player_loc, int computer_loc)
    {
        /*
            This function receives player piece location and computer piece location.
            And calculate the result, if player won or PC won.
         */
        Piece_type player_piece = all_pieces.get(player_loc);
        Piece_type computer_piece = all_pieces.get(computer_loc);
        if(player_piece.getType() != computer_piece.getType()) // If the types of the pieces is different.
        {
            if (player_piece.getType() == Types.rock)
            {
                if (computer_piece.getType() == Types.scissors)
                {
                    clearAfterFight(player_loc, computer_loc, true);
                }
                if(computer_piece.getType() == Types.paper)
                {
                    clearAfterFight(player_loc, computer_loc, false);
                }
            }

            if (player_piece.getType() == Types.scissors)
            {
                if (computer_piece.getType() == Types.paper)
                {
                    clearAfterFight(player_loc, computer_loc, true);
                }
                if(computer_piece.getType() == Types.rock)
                {
                    clearAfterFight(player_loc, computer_loc, false);
                }
            }

            if (player_piece.getType() == Types.paper)
            {
                if (computer_piece.getType() == Types.rock)
                {
                    clearAfterFight(player_loc, computer_loc, true);
                }
                if(computer_piece.getType() == Types.scissors)
                {
                    clearAfterFight(player_loc, computer_loc, false);
                }
            }

            if (computer_piece.getType() == Types.king) // Player won.
            {
                clearAfterFight(player_loc, computer_loc, true);
                win();
            }
            if(computer_piece.getType() == Types.trap) // Player fell into PC trap.
            {
                clearAfterFight(player_loc, computer_loc, true);
            }

        }
        else
        {
            create_tie_dialog(player_piece, first_click_loc, computer_loc); // If tie, call func.
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void update_player_after_tie(int player_loc, int computer_loc)
    {
        /*
            This function receives player piece location and computer piece location.
            It updates the types of the pieces and start fight all over again.
         */
        Piece_type p_type = blue_player.getPieces().get(player_loc);
        blue_player.getPieces().put(player_loc, Piece_type.get_piece(chosen));
        //blue_player.getPieces().get(player_loc).expose();
        all_pieces.put(player_loc, Piece_type.get_piece(chosen));
        //all_pieces.get(player_loc).expose();
        board.updateButton(player_loc / 7, player_loc % 7, chosen, true, true);//update on board
        computer.updateGuessAfterTie(player_loc, p_type, chosen);
        d_tie.dismiss();
        computer.chooseNewPiece(computer_loc);
        if(turn == 1)
            startFight(player_loc, second_click_pair.first * column + second_click_pair.second);
        else
            computer.whichMove(all_pieces, computer_loc, player_loc, board, tv_text);
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void clearAfterFight(int player_loc, int computer_loc, boolean winner) // true if player won, false if computer won.
    {
        /*
           This function receive player piece location, computer piece location and who won the fight.
           The function updates the data of both players according to the winner.
        */
        if (computer.getPieces().get(computer_loc) != null && computer.getPieces().get(computer_loc).getType() == Types.trap)
        {
            tv_text.setTextColor(0xffff0000);
            tv_text.setText("Your player fell into red " + Types.trap.toString());
            //blue_player.getPieces().remove(player_loc);
            Piece_type p_type = blue_player.getPieces().remove(player_loc);
            computer.reportAGuess(player_loc, p_type);
            computer.removeFromGuess(player_loc);
            all_pieces.put(player_loc, Piece_type.get_empty());
            board.clearButton(first_click_pair.first, first_click_pair.second);// remove moved piece from last location
        }
        else
            {
                if(winner)  // If player won the fight.
                {
                    Piece_type p_type = blue_player.getPieces().remove(player_loc);
                    tv_text.setTextColor(0xff0000ff);
                    tv_text.setText("Blue won with " + p_type.getType().toString());
                    blue_player.getPieces().put(computer_loc, p_type);
                    blue_player.getPieces().get(computer_loc).expose();
                    all_pieces.put(player_loc, Piece_type.get_empty());
                    all_pieces.put(computer_loc, p_type);
                    all_pieces.get(computer_loc).expose();
                    computer.reportAGuess(player_loc, p_type);
                    computer.updateMove(player_loc, computer_loc);
                    computer.getPieces().remove(computer_loc);
                    board.updateButton(second_click_pair.first, second_click_pair.second, p_type.getType(), true, true);//update on board
                    board.clearButton(first_click_pair.first, first_click_pair.second);// remove moved piece from last location
                }
                else // If PC won the fight.
                {
                    Piece_type p_type = computer.getPieces().get(computer_loc);
                    computer.getPieces().get(computer_loc).expose();
                    tv_text.setTextColor(0xffff0000);
                    tv_text.setText("Red won with " + p_type.getType().toString());
                    all_pieces.put(player_loc, Piece_type.get_empty());
                    //blue_player.getPieces().remove(player_loc);
                    computer.reportAGuess(player_loc, blue_player.getPieces().remove(player_loc));
                    computer.removeFromGuess(player_loc);
                    board.updateButton(second_click_pair.first, second_click_pair.second, p_type.getType(), false, true);
                    board.clearButton(first_click_pair.first, first_click_pair.second);// remove moved piece from last location
                }
        }
        turn = 0;
        computer.makeMove(all_pieces, board, tv_text, false); // Start PC move.
    }

    public void clearButtons(Pair last_pair)
    {
        /*
            This function receives a Pair of last clicked location and removes all unwanted marks from the board.
         */
        if((int)last_pair.second != 0 && board.getButtons()[(int)last_pair.first][(int)last_pair.second - 1].getText() == "⌾") // Clear left
        {
            board.clearButton((int)last_pair.first ,(int)last_pair.second - 1);
        }
        if((int)last_pair.second != 6 && board.getButtons()[(int)last_pair.first][(int)last_pair.second + 1].getText() == "⌾") // Clear left
        {
            board.clearButton((int)last_pair.first ,(int)last_pair.second + 1);
        }
        if((int)last_pair.first != 0 && board.getButtons()[(int)last_pair.first - 1][(int)last_pair.second].getText() == "⌾") // Clear left
        {
            board.clearButton((int)last_pair.first - 1 ,(int)last_pair.second);
        }
        if((int)last_pair.first != 5 && board.getButtons()[(int)last_pair.first + 1][(int)last_pair.second].getText() == "⌾") // Clear left
        {
            board.clearButton((int)last_pair.first + 1, (int)last_pair.second);
        }
    }

    public Pair<Integer, Integer> getButtonPos(Board board, Button button)
    {
        /*
            This function receives the board and a button that pressed.
            Returns the values of the button on the board that pressed.
         */
        int x, y;
        for (y = 0; y < row; y++)
        {
            for (x = 0; x < column; x++)
            {
                if (board.getButtons()[y][x] == button)
                {
                    return new Pair<>(y, x);
                }
            }
        }
        return null;
    }

    protected void create_lose_dialog()
        /*
            This function creates lose dialog.
         */
    {
        d_lose = new Dialog(this);
        d_lose.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        d_lose.setContentView(R.layout.dialog_gameover);
        homebtn_lose = (Button) d_lose.findViewById(R.id.dialogreturn);
        homebtn_lose.setOnClickListener(this);
        //tvbombsleft_d.setText("Bombs left - " + tv_bombs.getText().toString());
        tv_score_d = (TextView)d_lose.findViewById(R.id.tv_score_d);
        //tv_score_d.setText("Score - " + tv_score.getText().toString());
        d_lose.setCancelable(false);
        d_lose.show();
        //updateData();
    }

    public void create_tie_dialog(Piece_type p_type, int player_loc, int computer_loc)
    {
        /*
            This function creates tie dialog.
         */
        d_tie = new Dialog(this);
        d_tie.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d_tie.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        d_tie.setContentView(R.layout.dialog_tie);
        re_scissors = (ImageButton) d_tie.findViewById(R.id.scissors);
        re_scissors.setOnClickListener(new View.OnClickListener()
        {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v)
            {
                chosen = Types.scissors;
                update_player_after_tie(player_loc, computer_loc);
                //d_tie.dismiss();
            }
        });
        re_rock = (ImageButton) d_tie.findViewById(R.id.rock);
        re_rock.setOnClickListener(new View.OnClickListener()
        {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v)
            {
                chosen = Types.rock;
                update_player_after_tie(player_loc, computer_loc);
                //d_tie.dismiss();
            }
        });
        re_paper = (ImageButton) d_tie.findViewById(R.id.paper);
        re_paper.setOnClickListener(new View.OnClickListener()
        {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v)
            {
                chosen = Types.paper;
                update_player_after_tie(player_loc, computer_loc);
                //d_tie.dismiss();
            }
        });
        tv_type_chose = (TextView)d_tie.findViewById(R.id.tv_type_chose);
        tv_type_chose.setText(tv_type_chose.getText().toString() + p_type.getType().toString());
        if(turn == 1)
        {
            tv_type_chose.setTextColor(Color.WHITE);
        }
        else
            tv_type_chose.setTextColor(Color.RED);
        d_tie.setCancelable(false);
        d_tie.show();
    }
    private void win()
    {
        /*
            This function creates win dialog.
         */

        d_win = new Dialog(this);
        d_win.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d_win.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        d_win.setContentView(R.layout.dialog_win);//dialog_win
        homebtn_win = (Button) d_win.findViewById(R.id.dialog_win_return);
        homebtn_win.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(com.example.rps.GameActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        tv_score_d = (TextView)d_win.findViewById(R.id.tv_score_d);
        //tv_score_d.setText("Score - " + tv_score.getText().toString());
        d_win.setCancelable(false);
        d_win.show();
        updateData();

    }

    public HashMap<Integer,Piece_type>getCopy_of_All_pieces()
    {
        HashMap<Integer,Piece_type> copy = new HashMap<>();
        for (int key : all_pieces.keySet())
        {
            copy.put(key, all_pieces.get(key));
        }
        return copy;
    }
    public void updateData()
        /*
        *This function creates the connection from the client to the server and transports the name and score.
        */
    {
        JSONObject update = new JSONObject();
        try
        {
            update.put("username", userName);
            //update.put("score", (tv_score.getText().toString()));
            update.put("request", "update");
            Client client = new Client(update);
            JSONObject received = client.execute().get();
            System.out.println(received);
        }
        catch (Exception e)
        {
            System.out.println(e.toString());
        }
    }

}
