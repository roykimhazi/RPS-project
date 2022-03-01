package com.example.rps;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Button;
import android.widget.LinearLayout;

public class Board
{
    private Context context;
    private LinearLayout linearLayout;
    private Button[][] buttons;
    private int row;
    private int column;

    public Board(Context context, LinearLayout linearLayout, int column, int row)
    {
        this.column = column;
        this.row = row;
        this.context = context;
        this.linearLayout = linearLayout;
        this.buttons = new Button[column][row];
        int index = 1;
        for (int i = 0; i < column; i++)
        {
            for (int j = 0; j < row; j++)
            {
                this.buttons [i][j] = new Button(context);
                setButtonParameters(this.buttons [i][j]);
                if(index%2 ==0 )
                {
                    this.buttons[i][j].setBackgroundColor(-16711936 );
                }
                else
                    this.buttons[i][j].setBackgroundColor(0XFF03DAC5 );
                index++;
            }
          //  index++;
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
}
