package AST;

import Exception.EvalError;
import Game.Player;

import java.util.Map;

public class Assignment implements Node
{
    private String var, op;
    private Expr num;

    public Assignment(String name, String op, Expr num)
    {
        this.var = name;
        this.op = op;
        this.num = num;
    }

    public void evaluate(Map<String, Long> bindings, Player player) throws EvalError
    {
        if(bindings.containsKey(var))
        {
            bindings.replace(var, num.eval(bindings, player));
        }
        else
        {
            bindings.put(var, num.eval(bindings, player));
        }
    }

    public void prettyPrint(StringBuilder s)
    {
        s.append(var).append(op);
        num.prettyPrint(s);
    }
}
