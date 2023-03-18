package AST;

import Exception.EvalError;
import Game.Player;

import java.util.LinkedList;
import java.util.Map;

public class BlockState implements Node
{
    private LinkedList<Node> statements;

    public BlockState(LinkedList<Node> statements)
    {
        this.statements = statements;
    }

    public void evaluate(Map<String, Long> bindings, Player player)
    {
        statements.forEach(p ->
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
        if(statements.isEmpty())
        {
            s.append("{}");
        }
        else
        {
            s.append("\n{\n");
            for (Node statement : statements)
            {
                s.append("\t");
                statement.prettyPrint(s);
                s.append("\n");
            }
            s.append("}");
        }
    }
}