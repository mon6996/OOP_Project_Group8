package AST;

import Exception.EvalError;
import Game.*;

import java.util.Map;

public class Move implements Node
{
    private Expr dir;

    public Move(Expr dir)
    {
        this.dir = dir;
    }

    public void evaluate(Map<String, Long> bindings, Player player) throws EvalError
    {
        player.move(dir.eval(null, player));
    }

    public void prettyPrint(StringBuilder s)
    {
        s.append("move");
        dir.prettyPrint(s);
    }
}
