package AST;

import Exception.EvalError;
import Game.Player;

import java.util.Map;

public class Number implements Expr
{
    private Long val;

    public Number(Long val)
    {
        this.val = val;
    }

    public Long eval(Map<String, Long> bindings, Player player) throws EvalError
    {
        return val;
    }

    public void prettyPrint(StringBuilder s)
    {
        s.append(val);
    }
}