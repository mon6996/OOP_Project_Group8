package AST;

import Exception.EvalError;
import Game.Player;

import java.util.Map;

public enum Direction implements Expr
{
    up, upleft, upright, down, downleft, downright;

    public Long eval(Map<String, Long> bindings, Player player) throws EvalError
    {
        switch (this)
        {
            case up -> {return 1L;}
            case upright -> {return 2L;}
            case downright -> {return 3L;}
            case down -> {return 4L;}
            case downleft -> {return 5L;}
            case upleft -> {return 6L;}
            default -> {return 0L;}
        }
    }
    public void prettyPrint(StringBuilder s)
    {
        s.append(" ").append(Direction.this);
    }
}


