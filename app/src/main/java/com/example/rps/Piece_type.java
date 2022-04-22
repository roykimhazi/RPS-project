package com.example.rps;

public class Piece_type
{
    /*
        This class is the data struct I used all over the project.
        It contains a type of piece and if the piece is expose or hidden.
        All functions returns pieces.
     */
    private Types type;
    private boolean is_exposed;

    public Piece_type(Types type, boolean is_exposed)
    {
        this.type = type;
        this.is_exposed = is_exposed;
    }
    public Piece_type(Types type)
    {
        this.type = type;
        is_exposed = false;
    }
    public Types getType()
    {
        return type;
    }

    public void setType(Types type1)
    {
        type = type1;
    }

    public boolean is_exposed()
    {
        return is_exposed;
    }

    public void expose()
    {
        this.is_exposed = true;
    }
    public void hide()
    {
        this.is_exposed = false;
    }

    public static Piece_type get_empty_h()
    {
        Piece_type t = new Piece_type(Types.empty);
        return t;
    }
    public static Piece_type get_king_h()
    {
        Piece_type t = new Piece_type(Types.king);
        return t;
    }
    public static Piece_type get_trap_h()
    {
        Piece_type t = new Piece_type(Types.trap);
        return t;
    }
    public static Piece_type get_rock_h()
    {
        Piece_type t = new Piece_type(Types.rock);
        return t;
    }
    public static Piece_type get_scissors_h()
    {
        Piece_type t = new Piece_type(Types.scissors);
        return t;
    }
    public static Piece_type get_paper_h()
    {
        Piece_type t = new Piece_type(Types.paper);
        return t;
    }
    public static Piece_type get_empty()
    {
        Piece_type t = new Piece_type(Types.empty,true);
        return t;
    }
    public static Piece_type get_king()
    {
        Piece_type t = new Piece_type(Types.king,true);
        return t;
    }
    public static Piece_type get_trap()
    {
        Piece_type t = new Piece_type(Types.trap,true);
        return t;
    }
    public static Piece_type get_rock()
    {
        Piece_type t = new Piece_type(Types.rock,true);
        return t;
    }
    public static Piece_type get_scissors()
    {
        Piece_type t = new Piece_type(Types.scissors,true);
        return t;
    }
    public static Piece_type get_paper()
    {
        Piece_type t = new Piece_type(Types.paper,true);
        return t;
    }
    public static Piece_type get_piece(Types type)
    {
        switch (type) {
            case scissors:
            {
                return get_scissors();
            }
            case paper:
            {
                return get_paper();
            }
            case rock:
            {
                return get_rock();
            }
            case trap:
            {
                return  get_trap();
            }
        }
        return get_king();
    }
}
