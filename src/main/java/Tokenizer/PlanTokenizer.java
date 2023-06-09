package Tokenizer;

import Exception.*;

import java.util.NoSuchElementException;

public class PlanTokenizer implements Tokenizer
{
    private String src, next;
    private int pos;

    public PlanTokenizer(String src) throws LexicalError
    {
        this.src = src;
        pos = 0;
        computeNext();
    }

    public boolean hasNextToken()
    {
        return next != null;                    // if have next token(!=null) is true but if not return false
    }

    public String peek()
    {
        if (!hasNextToken()) throw new NoSuchElementException("no more tokens");
        return next;
    }

    public String consume() throws LexicalError
    {
        if (!hasNextToken()) throw new NoSuchElementException("no more tokens");
        String result = next;
        computeNext();
        return result;
    }

    private void computeNext() throws LexicalError
    {
        StringBuilder s = new StringBuilder();
        while (pos < src.length() && Character.isWhitespace(src.charAt(pos)))
            pos++;
        if (pos < src.length() && src.charAt(pos) == '#') {
            // Skip the rest of the line after "#" character
            while (pos < src.length() && src.charAt(pos) != '\n') {
                pos++;
            }
            // Skip the newline character after the "#" character
            if (pos < src.length() && src.charAt(pos) == '\n') {
                pos++;
            }
            // Compute the next token after the "#" line
            computeNext();
        }
        else if (pos < src.length()) {
            char c = src.charAt(pos);
            if ('\n' == c) {
                pos++;
            }
            else if (Character.isDigit(c)) {
                s.append(c);
                for (pos++; pos < src.length() && Character.isDigit(src.charAt(pos)); pos++)
                    s.append(src.charAt(pos));
            }
            else if (Character.isLetter(c)) {
                s.append(c);
                for (pos++; pos < src.length() && Character.isLetter(src.charAt(pos)); pos++)
                {
                    s.append(src.charAt(pos));
                }
            }
            else if (c == '+' || c == '-' || c == '*' || c == '/' || c == '%' || c == '^' ||
                    c == '=' || c == '(' || c == ')' || c == '{' || c == '}') {
                s.append(c);
                pos++;
            }
            else {
                throw new LexicalError("unknown character: " + c);
            }
            next = s.toString();
        }
        else {
            next = null;
        }
    }

    public boolean peek(String s)
    {
        if (!hasNextToken())
            return false;
        return peek().equals(s);
    }

    public void consume(String s) throws SyntaxError, LexicalError
    {
        if (peek(s))
            consume();
        else
            throw new SyntaxError(s + " expected");
    }
}

