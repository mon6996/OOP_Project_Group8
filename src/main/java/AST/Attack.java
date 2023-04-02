package AST;

import Exception.EvalError;
import Game.Player;

import java.util.Map;

public class Attack implements Node
{
    private Expr dir;
    private Expr cost;

    public Attack(Expr dir, Expr cost)
    {
        this.dir = dir;
        this.cost = cost;
    }

    public void evaluate(Map<String, Long> bindings, Player player) throws EvalError
    {
        player.shoot(dir.eval(bindings, player), cost.eval(bindings, player));
    }

    public void prettyPrint(StringBuilder s)
    {
        s.append("shoot ");
        cost.prettyPrint(s);
        dir.prettyPrint(s);
    }
}