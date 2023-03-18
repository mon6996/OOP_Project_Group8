package AST;

import Exception.EvalError;
import Game.Player;

import java.util.Map;

public interface Node
{
    default void evaluate(Map<String, Long> bindings, Player player) throws EvalError {}
    void prettyPrint(StringBuilder s);
}
