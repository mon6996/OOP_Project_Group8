package AST;

import Exception.EvalError;
import Game.Player;

import java.util.Map;

public class RegionCommand implements Node
{
    private String command;
    private Expr cost;

    public RegionCommand(String command, Expr cost)
    {
        this.command = command;
        this.cost = cost;
    }

    public void evaluate(Map<String, Long> bindings, Player player) throws EvalError
    {
        if(command.equals("invest"))
            player.invest(cost.eval(null, player));
        else
            player.collect(cost.eval(null, player));
    }

    public void prettyPrint(StringBuilder s)
    {
        s.append(command);
        cost.prettyPrint(s);
    }
}