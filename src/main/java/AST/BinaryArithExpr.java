package AST;

import Exception.EvalError;
import Game.Player;

import java.util.Map;

public class BinaryArithExpr implements Expr
{
    private Expr left, right;
    private String op;

    public BinaryArithExpr(Expr left, String op, Expr right)
    {
        this.left = left;
        this.op = op;
        this.right = right;
    }

    public Long eval(Map<String, Long> bindings, Player player) throws EvalError
    {
        long lv = left.eval(bindings, player);
        long rv = right.eval(bindings, player);
        if (op.equals("+")) return lv + rv;
        if (op.equals("-")) return lv - rv;
        if (op.equals("*")) return lv * rv;
        if (op.equals("/")) return lv / rv;
        if (op.equals("%")) return lv % rv;
        if (op.equals("^")) return (long) Math.pow(lv, rv);
        throw new EvalError("unknown op: " + op);
    }

    public void prettyPrint(StringBuilder s)
    {
        s.append("(");
        left.prettyPrint(s);
        s.append(op);
        right.prettyPrint(s);
        s.append(")");
    }
}