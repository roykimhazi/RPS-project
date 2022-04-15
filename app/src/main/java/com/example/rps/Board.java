package com.example.rps;

import android.app.Activity;
import android.content.Context;
import android.nfc.cardemulation.HostApduService;
import android.util.DisplayMetrics;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.HashMap;



public class Board
{
    HashMap<Types, String> convert = new HashMap<>();
    private Context context;
    private LinearLayout linearLayout;
    private Button[][] buttons;
    private int row;
    private int column;


    public Board(Context context, LinearLayout linearLayout, int column, int row)
    {
        convert.put(Types.rock,"R");    //🪨,🪨
        convert.put(Types.paper,"\uD83D\uDCC3");
        convert.put(Types.scissors,"✂");
        convert.put(Types.king,"⛿");
        convert.put(Types.trap,"T");//🪤
        convert.put(Types.empty,"");
        this.column = column;
        this.row = row;
        this.context = context;
        this.linearLayout = linearLayout;
        this.buttons = new Button[row][column];
        int index = 1;
        for (int i = 0; i < row; i++)
        {
            for (int j = 0; j < column; j++)
            {
                this.buttons [i][j] = new Button(context);
                setButtonParameters(this.buttons [i][j]);
                this.buttons[i][j].setTextSize(32);
                if(index%2 ==0 )
                {
                    // 11523061 this.buttons[i][j].setBackgroundColor(-16711936 );
                    if (i < 2)
                        this.buttons[i][j].setBackgroundColor(0xFFB38282);// red background color
                    else {
                        if (i < 4)
                            this.buttons[i][j].setBackgroundColor(0xFF95F385);// natural background color
                        else
                            this.buttons[i][j].setBackgroundColor(-5254155);// blue background color
                    }
                }
                else
                    //this.buttons[i][j].setBackgroundColor(0XFF03DAC5 );
                    if (i < 2)
                        this.buttons[i][j].setBackgroundColor(0xFFDC4646);// red background color
                    else
                        {
                        if (i < 4)
                            this.buttons[i][j].setBackgroundColor(0xFF46FF39);// natural background color
                        else
                            this.buttons[i][j].setBackgroundColor(-8144144);// blue background color
                    }
                    index++;
            }
            //index++;
        }

        PlaceButtonsInLayout();
    }

    public Context getContext()
    {
        return this.context;
    }

    public void setContext(Context context)
    {
        this.context = context;
    }

    public LinearLayout getLinearLayout()
    {
        return this.linearLayout;
    }

    public void setLinearLayout(LinearLayout linearLayout)
    {
        this.linearLayout = linearLayout;
    }

    public Button[][] getButtons()
    {
        return this.buttons;
    }

    public void setButtons(Button[][] buttons)
    {
        this.buttons = buttons;
    }

    public void setListener() {
        for (Button[] rowButtons : this.buttons)
        {
            for (Button button : rowButtons)
            {
                button.setOnClickListener((View.OnClickListener) this.context);
            }
        }
    }

    public void setClickable(boolean isClickable)
    {
        /*
         *This function allows to the player to click on the buttons
         */
        for (Button[] rowButtons : this.buttons)
        {
            for (Button button : rowButtons)
            {
                button.setEnabled(isClickable);
            }
        }
    }

    private void PlaceButtonsInLayout()
    {
        /*
         *This function places the buttons in the layout
         */
        for (Button[] rowButtons : this.buttons)
        {
            LinearLayout rowLinearLayout = new LinearLayout(this.context);
            LinearLayout.LayoutParams layoutParameters = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParameters.setMargins(1, 5, 1, 5);
            rowLinearLayout.setLayoutParams(layoutParameters);
            for (Button button : rowButtons)
            {
                rowLinearLayout.addView(button);
            }
            this.linearLayout.addView(rowLinearLayout);
        }
    }

    public LinearLayout addTwoButtons()
    {
        LinearLayout two_buttons = new LinearLayout(this.context);
        Button b,b2;
        b = new Button(context);
        b.setText("Shuffle pieces?");
        b.setTextSize(20);
        b.setId(1);
        b.setOnClickListener((View.OnClickListener) this.context);
        two_buttons.addView(b);
        b2 = new Button(context);
        b2.setText("Stay with these");
        b2.setTextSize(20);
        b2.setId(2);
        b2.setOnClickListener((View.OnClickListener) this.context);
        two_buttons.addView(b2);
        two_buttons.setHorizontalGravity(17);
        this.linearLayout.addView(two_buttons);
        return two_buttons;
    }
    public void removeTwoButtons(LinearLayout two_buttons)
    {
        this.linearLayout.removeViewInLayout(two_buttons);
    }
    private void setButtonParameters(Button button)
    {
        /*
        The program uses DisplayMetrics to get width of the user screen.
        By that it adjusts the board to a comfortable size.
         */
        LinearLayout.LayoutParams buttonParameters;
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) this.context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        if (row < column)
        {
            buttonParameters = new LinearLayout.LayoutParams(width/column-8 ,width/column-8); // 95 was the default size.
        }
        else
        {
            buttonParameters = new LinearLayout.LayoutParams(width/row-8 ,width/row-8); // 95 was the default size.
        }
        buttonParameters.setMargins(5, 1, 5, 1);
        button.setLayoutParams(buttonParameters);
    }
    public void showPieces(int loc, Player p)
    {
        int i_loc = loc;
        for (int i = i_loc / column; i < i_loc / column + 2; i++)
        {
            for(int j = 0; j < 7; j++)
            {
                buttons[i][j].setText(convert.get(p.getPieces().get(loc++).getType()));
            }
        }
    }
    public void moveAble(int i, int j)
    {
        buttons[i][j].setText("⌾");
        buttons[i][j].setTextColor(-2446);
        //buttons[i][j].setBackgroundColor(-2446);
    }
    public void updateButton(int i, int j, Types type, boolean is_player)
    {
        if (is_player == false) {
            if (((i * column) + j) % 2 == 1)
                buttons[i][j].setBackgroundColor(0xFFB38282);
            else
                buttons[i][j].setBackgroundColor(0xFFDC4646);
        }
        else
        {
            if (((i * column) + j) % 2 == 1)// pair.first * column + pair.second;
                buttons[i][j].setBackgroundColor(-5254155);
            else
                buttons[i][j].setBackgroundColor(-8144144);
        }
        buttons[i][j].setText(convert.get(type));
        buttons[i][j].setTextColor(-16777216);
    }
    public void clearButton(int i, int j)
    {
        if (((i * column) + j) % 2 == 1)
            buttons[i][j].setBackgroundColor(0xFF95F385);
        else
            buttons[i][j].setBackgroundColor(0xFF46FF39);
        buttons[i][j].setText("");
        buttons[i][j].setTextColor(-16777216);
    }
}
