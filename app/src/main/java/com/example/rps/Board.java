package com.example.rps;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.HashMap;


/**
 * This class builds the layout of the board, show it and controls it.
 */
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
        convert.put(Types.rock,"\uD83E\uDEA8");    //🪨,🪨
        convert.put(Types.paper,"\uD83D\uDCC3");
        convert.put(Types.scissors,"✂");
        convert.put(Types.king,"⛿");
        convert.put(Types.trap,"\uD83E\uDEA4");//🪤
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
        }

        PlaceButtonsInLayout();
    }
    public Button[][] getButtons()
    {
        return this.buttons;
    }

    /**
     * This function set for each button on click listener.
     */
    public void setListener() {
        for (Button[] rowButtons : this.buttons)
        {
            for (Button button : rowButtons)
            {
                button.setOnClickListener((View.OnClickListener) this.context);
            }
        }
    }

    /**
     * This function allows to the player to click on the buttons.
     * @param isClickable
     */
    public void setClickable(boolean isClickable)
    {
        for (Button[] rowButtons : this.buttons)
        {
            for (Button button : rowButtons)
            {
                button.setEnabled(isClickable);
            }
        }
    }

    /**
     * This function places the buttons in the layout.
     */
    private void PlaceButtonsInLayout()
    {
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

    /**
     * This function adds two buttons to the layout.
     * @return Linear layout of the buttons.
     */
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

    /**
     * This function removes two buttons from the board.
     * @param two_buttons
     */
    public void removeTwoButtons(LinearLayout two_buttons)
    {
        this.linearLayout.removeViewInLayout(two_buttons);
    }

    /**
     * The program uses DisplayMetrics to get width of the user screen.
     * By that it adjusts the board to a comfortable size.
     * @param button
     */
    private void setButtonParameters(Button button)
    {
        LinearLayout.LayoutParams buttonParameters;
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) this.context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        if (row < column)
        {
            buttonParameters = new LinearLayout.LayoutParams(width/column-8 ,width/column-8);
        }
        else
        {
            buttonParameters = new LinearLayout.LayoutParams(width/row-8 ,width/row-8);
        }
        buttonParameters.setMargins(5, 1, 5, 1);
        button.setLayoutParams(buttonParameters);
    }

    /**
     * This function shows pieces on the board, based on type.
     * @param loc
     * @param p
     */
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

    /**
     * This function marks a button on the board.
     * @param i
     * @param j
     */
    public void moveAble(int i, int j)
    {
        buttons[i][j].setText("⌾");
        buttons[i][j].setTextColor(-2446);
    }

    /**
     * This function updates the visual on the button based on parameters.
     * @param i
     * @param j
     * @param type
     * @param is_player
     * @param is_exposed
     */
    public void updateButton(int i, int j, Types type, boolean is_player, boolean is_exposed)
    {
        if (is_player == false) {
            if (((i * column) + j) % 2 == 1)
                buttons[i][j].setBackgroundColor(0xFFB38282);
            else
                buttons[i][j].setBackgroundColor(0xFFDC4646);
            if (!is_exposed)
                buttons[i][j].setText("?");
            else
                buttons[i][j].setText(convert.get(type));

        }
        else
        {
            if (((i * column) + j) % 2 == 1)
                buttons[i][j].setBackgroundColor(-5254155);
            else
                buttons[i][j].setBackgroundColor(-8144144);
            buttons[i][j].setText(convert.get(type));
        }
        buttons[i][j].setTextColor(-16777216);
    }

    /**
     * This function clears the button.
     * @param i
     * @param j
     */
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
