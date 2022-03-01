package com.example.rps;

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

public class GameActivity extends AppCompatActivity implements View.OnClickListener {
    LinearLayout linearLayoutBoard;
    Board board;
    int column, row;
    BoardHandler bh;
    Dialog d_lose, d_win;
    TextView tvbombsleft_d, tv_score_d;
    Button homebtn_lose, homebtn_win, btn_flag;
    boolean is_flag = false, is_first = true, is_sec = true;
    TextView tv_bombs, tv_score;
    String userName;
    Player red_player, blue_player;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        //column = Integer.valueOf(getIntent().getExtras().getString("column"));
        //row = Integer.valueOf(getIntent().getExtras().getString("row"));
        row = 7;
        column = 6;
        red_player = new Player(0);
        blue_player = new Player(5);
        linearLayoutBoard = (LinearLayout) findViewById(R.id.board);
        board = new Board(this, linearLayoutBoard, column, row);
        bh = new BoardHandler(column, row);
        System.out.println(bh.toString());
        btn_flag = (Button) findViewById(R.id.btn_flag);
        btn_flag.setOnClickListener(this);
        tv_bombs = (TextView)findViewById(R.id.tvbombsleft);
        tv_score = (TextView)findViewById(R.id.tvscore);
        userName = getIntent().getExtras().getString("userName");


    }

    @Override
    protected void onStart() {
        super.onStart();
        board.setClickable(true);
        board.setListener();
        Toast.makeText(this, "Place your flag", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View v)
    {
        /*
         This function checks which button is pressed and how to operate
         */
        if (is_first)
        {
            Pair<Integer, Integer> pair = getButtonPos(board, (Button) v);
            int loc = pair.first + (pair.second + 1) * row;
            if (loc > 34 && loc < 49)
            {
                blue_player.getPieces().put(loc, Player.piece_type.king);
                ((Button) v).setText("⛿");
                is_first = false;
                Toast.makeText(this, "Place your trap", Toast.LENGTH_LONG).show();
            }
            else
                Toast.makeText(this, "You can't place there! \n try again", Toast.LENGTH_SHORT).show();
        }
        else{
            if (is_sec)
            {
                Pair<Integer, Integer> pair = getButtonPos(board, (Button) v);
                int loc = pair.first + (pair.second + 1) * row;
                if (loc > 34 && loc < 49 &&  ((Button) v).getText().toString().equals("")) {
                    blue_player.getPieces().put(loc, Player.piece_type.trap);
                    ((Button) v).setText("T");
                    is_sec = false;
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

                else if (v == btn_flag)
                {
                    is_flag = !is_flag;
                    if (is_flag)
                    {
                        v.setBackgroundResource(R.drawable.ic_flag);
                    }
                    else
                    {
                        v.setBackgroundResource(R.drawable.ic_btn_mines);
                    }
                }
                else
                {
                    Pair<Integer, Integer> pair = getButtonPos(board, (Button) v);
                    int x = pair.first;
                    int y = pair.second;
                    if ( is_flag && ((Button) v).getText().toString().equals("⛿"))
                    {
                        ((Button) v).setText("");
                        tv_bombs.setText(String.valueOf(Integer.parseInt(tv_bombs.getText().toString())+1));
                    }
                    else if (is_flag && ((Button) v).getText().toString().equals(""))
                    {
                        ((Button) v).setText("⛿");
                        tv_bombs.setText(String.valueOf(Integer.parseInt(tv_bombs.getText().toString())-1));
                        if (tv_bombs.getText().toString().equals("0"))
                            is_win();
                    }
                    else
                    {
                        if (((Button) v).getText().toString().equals("⛿"))
                        {
                            Toast.makeText(this, "You can't do this ", Toast.LENGTH_SHORT).show();
                        }
                        else if (bh.getIntBoard()[y][x] != -1)
                        {
                            if (((Button) v).getText().toString().equals(""))
                            {
                                tv_score.setText(String.valueOf(Integer.parseInt(tv_score.getText().toString()) + 10));
                                ((Button) v).setText(String.valueOf(bh.getIntBoard()[y][x]));
                            }
                        }
                        else
                        {
                            create_lose_dialog();
                        }
                    }
                }
            }
        }

    }



    public Pair<Integer, Integer> getButtonPos(Board board, Button button)
    {
        /*
         *This function returns the values of the square on the board that pressed
         */
        int x, y;
        for (y = 0; y < column; y++)
        {
            for (x = 0; x < row; x++)
            {
                if (board.getButtons()[y][x] == button)
                {
                    return new Pair<>(x, y);
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
        tvbombsleft_d.setText("Bombs left - " + tv_bombs.getText().toString());
        tv_score_d = (TextView)d_lose.findViewById(R.id.tv_score_d);
        tv_score_d.setText("Score - " + tv_score.getText().toString());
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
            tv_score_d.setText("Score - " + tv_score.getText().toString());
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
            update.put("score", (tv_score.getText().toString()));
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
