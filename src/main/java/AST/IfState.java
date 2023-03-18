package AST;

import Exception.EvalError;
import Game.Player;

import java.util.Map;

public class IfState implements Node
{
    private Expr condition;
    private Node thenStatement;
    private Node elseStatement;

    public IfState(Expr condition, Node thenStatement, Node elseStatement)
    {
        this.condition = condition;
        this.thenStatement = thenStatement;
        this.elseStatement = elseStatement;
    }

    public void evaluate(Map<String, Long> bindings, Player player) throws EvalError
    {
        if(condition.eval(bindings, player) > 0)
        {
            thenStatement.evaluate(bindings, player);
        }
        else
        {
            elseStatement.evaluate(bindings, player);
        }
    }

    public void prettyPrint(StringBuilder s)
    {
        s.append("if (");
        condition.prettyPrint(s);
        s.append(") ");
        s.append("then ");
        thenStatement.prettyPrint(s);
        if (elseStatement != null) {
            s.append("\n\telse ");
            elseStatement.prettyPrint(s);
        }
    }
}