package AST;

import Exception.EvalError;
import Game.Player;

import java.util.LinkedList;
import java.util.Map;

public class State implements Node
{
    private LinkedList<Node> plan;

    public State(LinkedList<Node> plan)
    {
        this.plan = plan;
    }

    public void evaluate(Map<String, Long> bindings, Player player)
    {
        plan.forEach(p ->
        {
            try
            {
                p.evaluate(bindings, player);
            }
            catch (EvalError e)
            {
                System.out.println("state error");
            }
        });
    }

    public void prettyPrint(StringBuilder s)
    {
        for (Node statement : plan)
        {
            statement.prettyPrint(s);
            s.append("\n");
        }
    }
}
