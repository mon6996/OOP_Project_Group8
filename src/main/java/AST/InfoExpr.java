package AST;

import Exception.EvalError;
import Game.Player;

import java.util.Map;

public class InfoExpr implements Expr
{
    private String command;
    private Expr dir;

    public InfoExpr(String command, Expr dir)
    {
        this.command = command;
        this.dir = dir;
    }

    public Long eval(Map<String, Long> bindings, Player player) throws EvalError
    {
        if(command.equals("opponent"))
            return player.opponent();
        else
            return player.nearby(dir.eval(null, player));
    }

    public void prettyPrint(StringBuilder s)
    {
        s.append(command);
        if(dir != null)
        {
            dir.prettyPrint(s);
        }
    }
}