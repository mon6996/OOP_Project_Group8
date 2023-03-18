package AST;

import Exception.EvalError;
import Game.Player;

import java.util.Map;

public class WhileState implements Node
{
    private Expr condition;
    private Node body;

    public WhileState(Expr condition, Node body)
    {
        this.condition = condition;
        this.body = body;
    }

    public void evaluate(Map<String, Long> bindings, Player player) throws EvalError
    {
        for (int counter = 0; counter < 10000 && condition.eval(bindings, player) > 0; counter++)
        {
            body.evaluate(bindings, player);
        }
    }

    public void prettyPrint(StringBuilder s)
    {
        s.append("while (");
        condition.prettyPrint(s);
        s.append(") ");
        body.prettyPrint(s);
    }
}