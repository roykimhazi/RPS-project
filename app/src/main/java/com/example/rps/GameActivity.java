package com.example.rps;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
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
    boolean is_flag = false, is_first = true, is_sec = true, first_move;
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
//        turn = rand.nextInt(2);
        turn = 1;
        first_move = true;
        row = 6;
        column = 7;
        red_player = new Player(0);
        blue_player = new Player(4);
        linearLayoutBoard = (LinearLayout) findViewById(R.id.board);
        board = new Board(this, linearLayoutBoard, column, row);
        //bh = new BoardHandler(column, row);
        computer = new Computer(0, board);
        //System.out.println(bh.toString());
        //btn_flag.setOnClickListener(this);
        tv_text = (TextView)findViewById(R.id.tvtext);
        userName = getIntent().getExtras().getString("userName");

    }

    @Override
    protected void onStart() {
        super.onStart();
        board.setClickable(true);
        board.setListener();

        Toast.makeText(this, "Place your flag", Toast.LENGTH_SHORT).show();
    }

    @SuppressLint("ResourceType")
    @Override
    public void onClick(View v)
    {
        /*
         This function checks which button is pressed and how to operate
         */
        if(v.getId() == 1)
        {
            blue_player.resetPieces(column * 4);
            blue_player.spread_pieces(column * 4);
            board.showPieces(column * 4, blue_player);
        }
        else
            if (v.getId() == 2)
            {
                board.removeTwoButtons(two_buttons);
                if (turn == 1)
                {
                    tv_text.setTextColor(0xff0000ff);
                    tv_text.setText("Blue start");
                }
                else {
                    tv_text.setText("Red start");
                    tv_text.setTextColor(0xffff0000);
                }
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
            }
        else{
            if (is_first)
            {
                Pair<Integer, Integer> pair = getButtonPos(board, (Button) v);
                int loc = pair.first * column + pair.second;
                if (loc > 27)
                {
                    blue_player.getPieces().put(loc, Piece_type.get_king_h());
                    //blue_player.getPieces().get(loc).hide();
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
                    Pair<Integer, Integer> pair = getButtonPos(board, (Button) v);
                    int loc = pair.first * column + pair.second;
                    if (loc > 27 &&  ((Button) v).getText().toString().equals("")) {
                        blue_player.getPieces().put(loc, Piece_type.get_trap_h());
                        //blue_player.getPieces().get(loc).hide();
                        ((Button) v).setText("T");
                        is_sec = false;
                        blue_player.spread_pieces(column * 4);

                        board.showPieces(column * 4, blue_player);
                        two_buttons = board.addTwoButtons();
                    }
                    else
                        Toast.makeText(this, "You can't place there! \n try again", Toast.LENGTH_SHORT).show();
                }
                else
                {

                    if(v == re_scissors)
                    {
                        int loc = first_click_pair.first * column + first_click_pair.second;
                        chosen = Types.scissors;
                        update_player_after_tie(loc);
                    }
                    if(v == re_rock)
                    {
                        int loc = first_click_pair.first * column + first_click_pair.second;
                        chosen = Types.rock;
                        update_player_after_tie(loc);
                    }
                    if(v == re_paper)
                    {
                        int loc = first_click_pair.first * column + first_click_pair.second;
                        chosen = Types.paper;
                        update_player_after_tie(loc);
                        //d_tie.dismiss();
                    }
                    if (v == homebtn_lose)
                    {
                        Intent intent = new Intent(this, MainActivity.class);
                        startActivity(intent);
                    }
                    else
                    {
                        if(!first_move)
                        {
                            second_move((Button) v);
                            first_move = true;
                        }
                        else if(first_move)
                                first_move = !first_move((Button) v);

                    }
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
        // לבדוק האם לחץ על חייל שלו
        // לסמן לאן החייל יכול לזוז
        // לקלוט את הלחיצה הבאה
        // לבדוק האם הלחיצה הבאה תקינה
        // במידה והמשבצת ריקה לעבור, במידה ויש שם חייל לעשות קרב
        // לבנות משחק
        Pair<Integer, Integer> pair = getButtonPos(board, (Button) b);
        int loc = pair.first * column + pair.second;
        if (turn == 1)//blue
        {
            if(blue_player.getPieces().get(loc) == null) // must move your pieces.
            {
                Toast.makeText(this, "You can move your pieces only", Toast.LENGTH_SHORT).show();
                return false;
            }
            if(blue_player.getPieces().get(loc).getType() == Types.trap) // traps can't move.
            {
                Toast.makeText(this, "You can't move your trap", Toast.LENGTH_SHORT).show();
                return false;
            }
            if(blue_player.getPieces().get(loc).getType() == Types.king) // flags can't move.
            {
                Toast.makeText(this, "You can't move your flag", Toast.LENGTH_SHORT).show();
                return false;
            }
            first_click_loc = loc;
            first_click_pair = pair;
            // mark all movable places.
            if(pair.second != 0 && all_pieces.get(loc - 1).getType() == Types.empty) // left
            {
                board.moveAble(pair.first ,pair.second - 1);
            }
            if(pair.second != 6 && all_pieces.get(loc + 1).getType() == Types.empty) // right
            {
                board.moveAble(pair.first ,pair.second + 1);
            }
            if(pair.first != 0 && all_pieces.get(loc - 7).getType() == Types.empty) // above
            {
                board.moveAble(pair.first - 1 ,pair.second);
            }
            if(pair.first != 5 && all_pieces.get(loc + 7).getType() == Types.empty) // under
            {
                board.moveAble(pair.first + 1,pair.second);
            }
            return true;
        }
        else
            Toast.makeText(this, "It is not your turn", Toast.LENGTH_SHORT).show();
            return false;
    }

    public void second_move(Button b)
    {
        Pair<Integer, Integer> pair = getButtonPos(board, (Button) b);
        int loc = pair.first * column + pair.second;
        second_click_pair = pair;
        // normal move
        // cancel  points
        // start fight
        if(all_pieces.get(loc).getType() == Types.empty && board.getButtons()[pair.first][pair.second].getText() == "⌾")// move to empty cell.
        {
            Piece_type p_type = blue_player.getPieces().remove(first_click_loc);
            blue_player.getPieces().put(loc, p_type);
            all_pieces.put(first_click_loc, Piece_type.get_empty());
            all_pieces.put(loc, p_type);
            board.updateButton(pair.first, pair.second, p_type.getType(), true);//update on board
            board.clearButton(first_click_pair.first, first_click_pair.second);// remove moved piece from last location
            computer.updateMove(first_click_loc, loc);
        }
        else
            if(blue_player.getPieces().get(loc) != null)
                Toast.makeText(this, "Not legal move", Toast.LENGTH_SHORT).show();
            else
                // start fight
                if(computer.getPieces().get(loc) != null && (loc == first_click_loc + 1 || loc == first_click_loc - 1 || loc == first_click_loc + 7 || loc == first_click_loc - 7))
                {
                    startFight(first_click_loc, loc);
                }
                else
                    Toast.makeText(this, "Not legal move", Toast.LENGTH_SHORT).show();
        clearButtons(first_click_pair);
        // להעביר את התור לשחקן השני

    }

    public void startFight(int player_loc, int computer_loc)
    {
        // לבדוק אם מישהו מהם מנצח רק לפי מה שיש להם עכשיו
        // אם יש מנצח לקבוע ניצחון ולהזיז את החייל
        // אחרת לעשות עוד פעם ועוד פעם
        Piece_type player_piece = all_pieces.get(player_loc);
        Piece_type computer_piece = all_pieces.get(computer_loc);
        if(player_piece.getType() != computer_piece.getType())
        {
            if (player_piece.getType() == Types.rock)
            {
                if (computer_piece.getType() == Types.scissors)
                {
                    clearAfterFight(player_loc, computer_loc, true);
                    // להפוך את הנשק לגלוי
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
                    // להפוך את הנשק לגלוי
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
                    // להפוך את הנשק לגלוי
                }
                if(computer_piece.getType() == Types.scissors)
                {
                    clearAfterFight(player_loc, computer_loc, false);
                }
            }
            if (computer_piece.getType() == Types.king)
            {
                clearAfterFight(player_loc, computer_loc, true);
                win();
            }
            if(computer_piece.getType() == Types.trap)
            {
                clearAfterFight(player_loc, computer_loc, true);
            }

        }
        else
        {
            // לעשות את הבחירה מחדש של המחשב
            create_tie_dialog(player_piece);
            //startFight(player_loc, computer_loc);
            // לזמן קרב שוב עם החדשים
        }
    }
    public void update_player_after_tie(int player_loc)
    {
        blue_player.getPieces().put(player_loc, Piece_type.get_piece(chosen));
        //blue_player.getPieces().get(player_loc).expose();
        all_pieces.put(player_loc, Piece_type.get_piece(chosen));
        //all_pieces.get(player_loc).expose();
        board.updateButton(first_click_pair.first, first_click_pair.second, chosen, true);//update on board
        d_tie.dismiss();
        startFight(player_loc,second_click_pair.first * column + second_click_pair.second );
    }
    public void clearAfterFight(int player_loc, int computer_loc, boolean winner) // true if player won, false if computer won.
    {
        if (computer.getPieces().get(computer_loc).getType() == Types.trap)
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
                if(winner)//player won fight
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
                    board.updateButton(second_click_pair.first, second_click_pair.second, p_type.getType(), true);//update on board
                    board.clearButton(first_click_pair.first, first_click_pair.second);// remove moved piece from last location
                }
                else // pc won fight
                {
                    Piece_type p_type = computer.getPieces().get(computer_loc);
                    computer.getPieces().get(computer_loc).expose();
                    tv_text.setTextColor(0xffff0000);
                    tv_text.setText("Red won with " + p_type.getType().toString());
                    all_pieces.put(player_loc, Piece_type.get_empty());
                    //blue_player.getPieces().remove(player_loc);
                    computer.reportAGuess(player_loc, blue_player.getPieces().remove(player_loc));
                    computer.removeFromGuess(player_loc);
                    board.updateButton(second_click_pair.first, second_click_pair.second, p_type.getType(), false);
                    board.clearButton(first_click_pair.first, first_click_pair.second);// remove moved piece from last location
                }
        }
    }

    public void clearButtons(Pair last_pair)
    {   
        if((int)last_pair.second != 0 && board.getButtons()[(int)last_pair.first][(int)last_pair.second - 1].getText() == "⌾")//left
        {
            board.clearButton((int)last_pair.first ,(int)last_pair.second - 1);
        }
        if((int)last_pair.second != 6 && board.getButtons()[(int)last_pair.first][(int)last_pair.second + 1].getText() == "⌾")//left
        {
            board.clearButton((int)last_pair.first ,(int)last_pair.second + 1);
        }
        if((int)last_pair.first != 0 && board.getButtons()[(int)last_pair.first - 1][(int)last_pair.second].getText() == "⌾")//left
        {
            board.clearButton((int)last_pair.first - 1 ,(int)last_pair.second);
        }
        if((int)last_pair.first != 5 && board.getButtons()[(int)last_pair.first + 1][(int)last_pair.second].getText() == "⌾")//left
        {
            board.clearButton((int)last_pair.first + 1, (int)last_pair.second);
        }
    }

    public Pair<Integer, Integer> getButtonPos(Board board, Button button)
    {
        /*
         *This function returns the values of the square on the board that pressed
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

    private void create_lose_dialog()
        /*
         *This function create the lose dialog 
         */
    {
        d_lose = new Dialog(this);
        d_lose.setContentView(R.layout.dialog_gameover);
        homebtn_lose = (Button) d_lose.findViewById(R.id.dialogreturn);
        homebtn_lose.setOnClickListener(this);
        tvbombsleft_d = (TextView)d_lose.findViewById(R.id.tvbombsleft_d);
        //tvbombsleft_d.setText("Bombs left - " + tv_bombs.getText().toString());
        tv_score_d = (TextView)d_lose.findViewById(R.id.tv_score_d);
        //tv_score_d.setText("Score - " + tv_score.getText().toString());
        d_lose.setCancelable(false);
        d_lose.show();
        updateData();
    }

    public void create_tie_dialog(Piece_type p_type)
    {
        d_tie = new Dialog(this);
        d_tie.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d_tie.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        d_tie.setContentView(R.layout.dialog_tie);
        re_scissors = (ImageButton) d_tie.findViewById(R.id.scissors);
        re_scissors.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                chosen = Types.scissors;
                update_player_after_tie(first_click_loc);
                //d_tie.dismiss();
            }
        });
        re_rock = (ImageButton) d_tie.findViewById(R.id.rock);
        re_rock.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                chosen = Types.rock;
                update_player_after_tie(first_click_loc);
                //d_tie.dismiss();
            }
        });
        re_paper = (ImageButton) d_tie.findViewById(R.id.paper);
        re_paper.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                chosen = Types.paper;
                update_player_after_tie(first_click_loc);
                //d_tie.dismiss();
            }
        });
        tv_type_chose = (TextView)d_tie.findViewById(R.id.tv_type_chose);
        tv_type_chose.setText(tv_type_chose.getText().toString() + p_type.getType().toString());
        d_tie.setCancelable(false);
        d_tie.show();
    }
    private void win()
    {
        /*
         *This function checks if the player marked all of the bombs with flags
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
