package com.example.rps;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
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
    int column, row, turn;
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


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        //column = Integer.valueOf(getIntent().getExtras().getString("column"));
        //row = Integer.valueOf(getIntent().getExtras().getString("row"));
        all_pieces = new HashMap<Integer,Player.piece_type>();
        Random rand = new Random();
        turn = rand.nextInt(2);
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
                        if(first_move)
                            first_move = !first_move((Button) v);
                        if(!first_move)
                            second_move((Button) v);
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
            if(blue_player.getPieces().get(loc) == null)
            {
                Toast.makeText(this, "You can move your pieces only", Toast.LENGTH_SHORT).show();
                return false;
            }
            //if() לצבוע את המשבצות שניתן ללכת אליהן
        }
        else
            Toast.makeText(this, "It is not your turn", Toast.LENGTH_SHORT).show();
            return false;
    }

    public void second_move(Button b)
    {

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

    private void is_win()
    {
        /*
         *This function checks if the player marked all of the bombs with flags
         */
        int count = 0;
        for (int y = 0; y < column; y++)
        {
            for (int x = 0; x < row; x++)
            {
                if (bh.getIntBoard()[y][x] == -1 && board.getButtons()[y][x].getText().toString().equals("⛿"))
                    count++;
            }
        }
        if(count == 25)
        {
            d_win = new Dialog(this);
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
        else
            Toast.makeText(this, "You have a mistake ", Toast.LENGTH_SHORT).show();
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
