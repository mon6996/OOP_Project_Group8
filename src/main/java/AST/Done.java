package AST;

import Exception.EvalError;
import Game.Player;

import java.util.Map;

public class Done implements Node
{
    public void evaluate(Map<String, Long> bindings, Player player) throws EvalError
    {
        player.done();
    }

    public void prettyPrint(StringBuilder s)
    {
        s.append("done");
    }
}