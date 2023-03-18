package Parser;

import Tokenizer.Tokenizer;
import AST.*;
import AST.Number;
import Exception.*;

import java.util.LinkedList;

public class PlanParser implements Parser
{
    private Tokenizer tkz;

    public PlanParser(Tokenizer tkz)
    {
        this.tkz = tkz;
    }

    private boolean isIdentifier(String s)
    {
        // Check that the string is not a reserved word
        switch (s)
        {
            case "collect", "done", "down", "downleft", "downright", "else", "if", "invest", "move", "nearby", "opponent", "relocate", "shoot", "then", "up", "upleft", "upright", "while" ->
            {
                return false;
            }
            default -> {}
        }

        // Check that the string starts with a letter
        char firstChar = s.charAt(0);
        return Character.isLetter(firstChar);
    }

    private boolean isSpecialVar(String s)
    {
        // Check that the string is special variables
        return switch (s)
                {
                    case "rows", "cols", "currow", "curcol", "budget", "deposit", "int", "maxdeposit", "random" -> true;
                    default -> false;
                };
    }

    public Node parse() throws SyntaxError, LexicalError, EvalError
    {
        Node n = parsePlan();
        if (tkz.hasNextToken())                         // if true have next word so convert tokenizer not all
            throw new SyntaxError("leftover token");
        return n;
    }

    // Plan -> Statement+
    private Node parsePlan() throws SyntaxError, LexicalError, EvalError
    {
        if(!tkz.hasNextToken())
            throw new SyntaxError("You must have construction plan !");

        LinkedList<Node> plan = new LinkedList<>();
        while(tkz.hasNextToken())
        {
            plan.add(parseStatement());
        }
        return new State(plan);
    }

    // Statement -> Command | BlockStatement | IfStatement | WhileStatement
    private Node parseStatement() throws SyntaxError, LexicalError, EvalError
    {
        if(tkz.peek("{"))
        {
            return parseBlock();
        }
        else if (tkz.peek("if"))
        {
            return parseIf();
        }
        else if (tkz.peek("while"))
        {
            return parseWhile();
        }
        else
        {
            return parseCommand();
        }
    }

    // Command -> AssignmentStatement | ActionCommand
    private Node parseCommand() throws SyntaxError, LexicalError
    {
        if(isIdentifier(tkz.peek()))
        {
            return parseAssignment();
        }
        else
        {
            return parseAction();
        }
    }

    // ActionCommand -> done | relocate | MoveCommand | AST.RegionCommand | AttackCommand
    private Node parseAction() throws LexicalError, SyntaxError
    {
        if(tkz.peek("done"))
        {
            tkz.consume();
            return new Done();
        }
        else if (tkz.peek("relocate"))
        {
            tkz.consume();
            return new Relocate();
        }
        else if (tkz.peek("move"))
        {
            tkz.consume();
            return parseMove();
        }
        else if (tkz.peek("invest") || tkz.peek("collect"))
        {
            return parseRegion();
        }
        else if (tkz.peek("shoot"))
        {
            tkz.consume();
            return parseAttack();
        }
        else
        {
            throw new SyntaxError("Unknown command: " + tkz);
        }
    }

    // AssignmentStatement -> <identifier> = Expression
    private Node parseAssignment() throws SyntaxError, LexicalError
    {
        String var = tkz.consume();
        if(isSpecialVar(var))
        {
            return new Identifier(var);
        }
        else
        {
            tkz.consume("=");
            return new Assignment(var, "=", parseE());
        }

    }

    // MoveCommand -> move Direction
    private Node parseMove() throws LexicalError, SyntaxError
    {
        return new Move(parseDir());
    }

    // AST.RegionCommand -> invest Expression | collect Expression
    private Node parseRegion() throws LexicalError, SyntaxError
    {
        return new RegionCommand(tkz.consume(), parseE());
    }

    // AttackCommand -> shoot Direction Expression
    private Node parseAttack() throws LexicalError, SyntaxError
    {
        return new Attack(parseDir(), parseE());
    }

    // Direction -> up | down | upleft | upright | downleft | downright
    private Expr parseDir() throws LexicalError, SyntaxError
    {
        String direction = tkz.consume();
        return switch (direction)
        {
            case "up" -> Direction.up;
            case "down" -> Direction.down;
            case "upleft" -> Direction.upleft;
            case "upright" -> Direction.upright;
            case "downleft" -> Direction.downleft;
            case "downright" -> Direction.downright;
            default -> throw new SyntaxError("Unknown direction: " + direction);
        };
    }

    // BlockStatement -> { Statement* }
    private Node parseBlock() throws SyntaxError, LexicalError, EvalError
    {
        LinkedList<Node> statements = new LinkedList<>();
        tkz.consume("{");
        while(!tkz.peek("}"))
        {
            statements.add(parseStatement());
        }
        tkz.consume("}");
        return new BlockState(statements);
    }

    // IfStatement -> if ( Expression ) then Statement else Statement
    private Node parseIf() throws SyntaxError, LexicalError, EvalError
    {
        tkz.consume("if");
        tkz.consume("(");
        Expr condition = parseE();
        tkz.consume(")");
        tkz.consume("then");
        Node thenStatement = parseStatement();
        tkz.consume("else");
        Node elseStatement = parseStatement();
        return new IfState(condition, thenStatement, elseStatement);
    }

    // WhileStatement -> while ( Expression ) Statement
    private Node parseWhile() throws SyntaxError, LexicalError, EvalError
    {
        tkz.consume("while");
        tkz.consume("(");
        Expr condition = parseE();
        tkz.consume(")");
        Node body = parseStatement();
        return new WhileState(condition, body);
    }

    // Expression -> Expression + Term | Expression - Term | Term
    private Expr parseE() throws LexicalError, SyntaxError
    {
        Expr e = parseT();
        while (tkz.hasNextToken() && (tkz.peek("+") || tkz.peek("-")))
        {
            e = new BinaryArithExpr(e, tkz.consume(), parseT());
        }
        return e;
    }

    // Term -> Term * Factor | Term / Factor | Term % Factor | Factor
    private Expr parseT() throws LexicalError, SyntaxError
    {
        Expr e = parseF();
        while ( tkz.hasNextToken() && (tkz.peek("*") || tkz.peek("/") || tkz.peek("%")) )
        {
            e = new BinaryArithExpr(e, tkz.consume(), parseF());
        }
        return e;
    }

    // Factor -> Power ^ Factor | Power
    private Expr parseF() throws LexicalError, SyntaxError
    {
        Expr e = parseP();
        if (tkz.peek("^"))
        {
            e = new BinaryArithExpr(e, tkz.consume(), parseF());
        }
        return e;
    }

    // Power -> <number> | <identifier> | ( Expression ) | InfoExpression
    // InfoExpression -> opponent | nearby Direction
    private Expr parseP() throws LexicalError, SyntaxError
    {
        if (tkz.peek().matches(".*[0-9].*"))
        {
            return new Number(Long.parseLong(tkz.consume()));
        }
        else if(tkz.peek("opponent"))
        {
            return new InfoExpr(tkz.consume(), null);
        }
        else if (tkz.peek("nearby"))
        {
            return new InfoExpr(tkz.consume(), parseDir());
        }
        else if (tkz.peek().matches(".*[A-Za-z].*") && isIdentifier(tkz.peek()))
        {
            return new Identifier(tkz.consume());
        }
        else if (tkz.peek("("))
        {
            tkz.consume();
            Expr e = parseE();
            tkz.consume(")");
            return e;
        }
        else
        {
            throw new SyntaxError("Unexpected token: " + tkz.peek());
        }
    }
}
