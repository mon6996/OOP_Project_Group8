package Parser;

import AST.Node;
import Exception.*;

public interface Parser
{
    Node parse() throws SyntaxError, LexicalError, EvalError;
}
