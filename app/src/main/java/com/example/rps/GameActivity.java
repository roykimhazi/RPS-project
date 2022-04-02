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
    Dialog d_lose, d_win;
    TextView tvbombsleft_d, tv_score_d;
    Button homebtn_lose, homebtn_win, btn_flag;
    boolean is_flag = false, is_first = true, is_sec = true, first_move;
    TextView tv_text;
    String userName;
    Player red_player, blue_player;
    Computer computer;
    HashMap <Integer, Player.piece_type> all_pieces;
    Pair<Integer,Integer> first_click_pair, second_click_pair;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        //column = Integer.valueOf(getIntent().getExtras().getString("column"));
        //row = Integer.valueOf(getIntent().getExtras().getString("row"));
        all_pieces = new HashMap<Integer,Player.piece_type>();
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
                    all_pieces.put(i, Player.piece_type.empty);
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
                    blue_player.getPieces().put(loc, Player.piece_type.king);
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
                        blue_player.getPieces().put(loc, Player.piece_type.trap);
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
            if(blue_player.getPieces().get(loc) == Player.piece_type.trap)
            {
                Toast.makeText(this, "You can't move your trap", Toast.LENGTH_SHORT).show();
                return false;
            }
            if(blue_player.getPieces().get(loc) == Player.piece_type.king)
            {
                Toast.makeText(this, "You can't move your flag", Toast.LENGTH_SHORT).show();
                return false;
            }
            if(blue_player.getPieces().get(loc) == null)
            {
                Toast.makeText(this, "You can move your pieces only", Toast.LENGTH_SHORT).show();
                return false;
            }
            first_click_loc = loc;
            first_click_pair = pair;
            if(pair.second != 0 && all_pieces.get(loc - 1) == Player.piece_type.empty)//left
            {
                board.moveAble(pair.first ,pair.second - 1);
            }
            if(pair.second != 6 && all_pieces.get(loc + 1) == Player.piece_type.empty)//right
            {
                board.moveAble(pair.first ,pair.second + 1);
            }
            if(pair.first != 0 && all_pieces.get(loc - 7) == Player.piece_type.empty)//above
            {
                board.moveAble(pair.first - 1 ,pair.second);
            }
            if(pair.first != 5 && all_pieces.get(loc + 7) == Player.piece_type.empty)//under
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
        if(all_pieces.get(loc) == Player.piece_type.empty && board.getButtons()[pair.first][pair.second].getText() == "⌾")
        {
            Player.piece_type type = blue_player.getPieces().remove(first_click_loc);
            blue_player.getPieces().put(loc, type);
            all_pieces.put(first_click_loc, Player.piece_type.empty);
            all_pieces.put(loc, type);
            board.updateButton(pair.first, pair.second, type);//update on board
            board.clearButton(first_click_pair.first, first_click_pair.second);// remove moved piece from last location
        }
        else
            if(blue_player.getPieces().get(loc) != null)
                Toast.makeText(this, "Not legal move", Toast.LENGTH_SHORT).show();
            else
                if(computer.getPieces().get(loc) != Player.piece_type.empty && (loc == first_click_loc + 1 || loc == first_click_loc - 1 || loc == first_click_loc + 7 || loc == first_click_loc - 7))
                {
                    startFight(first_click_loc, loc);
                }
                else
                    Toast.makeText(this, "Not legal move", Toast.LENGTH_SHORT).show();
        clearButtons(first_click_pair);
    }

    public void startFight(int player_loc, int computer_loc)
    {
        // לבדוק אם מישהו מהם מנצח רק לפי מה שיש להם עכשיו
        // אם יש מנצח לקבוע ניצחון ולהזיז את החייל
        // אחרת לעשות עוד פעם ועוד פעם
        Player.piece_type player_weapon = all_pieces.get(player_loc);
        Player.piece_type computer_weapon = all_pieces.get(computer_loc);
        if(player_weapon != computer_weapon)
        {
            if(computer_weapon == Player.piece_type.trap)
            {
                // לעשות את האכילה של המלכודת ואת הצגתה.
                clearAfterFight(player_loc, computer_loc,true);
            }
            if (player_weapon == Player.piece_type.rock)
            {
                if (computer_weapon == Player.piece_type.scissors)
                {
                    clearAfterFight(player_loc, computer_loc, true);
                    // להפוך את הנשק לגלוי
                }
                if(computer_weapon == Player.piece_type.paper)
                {
                    clearAfterFight(player_loc, computer_loc, false);
                }
            }
            if (player_weapon == Player.piece_type.scissors)
            {
                if (computer_weapon == Player.piece_type.paper)
                {
                    clearAfterFight(player_loc, computer_loc, true);
                    // להפוך את הנשק לגלוי
                }
                if(computer_weapon == Player.piece_type.rock)
                {
                    clearAfterFight(player_loc, computer_loc, false);
                }
            }
            if (player_weapon == Player.piece_type.paper)
            {
                if (computer_weapon == Player.piece_type.rock)
                {
                    clearAfterFight(player_loc, computer_loc, true);
                    // להפוך את הנשק לגלוי
                }
                if(computer_weapon == Player.piece_type.scissors)
                {
                    clearAfterFight(player_loc, computer_loc, false);
                }
            }
            if (computer_weapon == Player.piece_type.king)
            {
                clearAfterFight(player_loc, computer_loc, true);
                win();
            }
            if(computer_weapon == Player.piece_type.trap)
            {
                clearAfterFight(player_loc, computer_loc, false);
            }

        }
//        else
//        {
//            create dialog for choosing again
//        }
    }

    public void clearAfterFight(int player_loc, int computer_loc, boolean winner) // true if player won, false if computer won.
    {
        if (computer.getPieces().get(computer_loc) == Player.piece_type.trap)
        {
            blue_player.getPieces().remove(player_loc);
            all_pieces.put(player_loc, Player.piece_type.empty);
            board.clearButton(first_click_pair.first, first_click_pair.second);// remove moved piece from last location
        }
        else
            {
                if(winner)
                {
                    Player.piece_type type = blue_player.getPieces().remove(player_loc);
                    blue_player.getPieces().put(computer_loc, type);
                    all_pieces.put(player_loc, Player.piece_type.empty);
                    all_pieces.put(computer_loc, type);
                    computer.getPieces().remove(computer_loc);
                    board.updateButton(second_click_pair.first, second_click_pair.second, type);//update on board
                    board.clearButton(first_click_pair.first, first_click_pair.second);// remove moved piece from last location
                }
                else
                {
                    Player.piece_type type = computer.getPieces().remove(computer_loc);
                    computer.getPieces().put(player_loc, type);
                    all_pieces.put(computer_loc, Player.piece_type.empty);
                    all_pieces.put(player_loc, type);
                    blue_player.getPieces().remove(player_loc);
                    board.updateButton(first_click_pair.first, first_click_pair.second, type);//update on board
                    board.clearButton(second_click_pair.first, second_click_pair.second);// remove moved piece from last location
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

    private void win()
    {
        /*
         *This function checks if the player marked all of the bombs with flags
         */

        d_win = new Dialog(this);
//        d_win.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        d_win.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        d_win.setContentView(R.layout.dialog_win);
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
