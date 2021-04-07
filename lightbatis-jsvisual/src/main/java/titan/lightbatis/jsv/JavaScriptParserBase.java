package titan.lightbatis.jsv;

import org.antlr.v4.runtime.*;

/**
 * All parser methods that used in grammar (p, prev, notLineTerminator, etc.)
 * should start with lower case char similar to parser rules.
 */
public abstract class JavaScriptParserBase extends Parser
{
    public JavaScriptParserBase(TokenStream input) {
        super(input);
    }

    /**
     * Short form for prev(String str)
     */
    protected boolean p(String str) {
        return prev(str);
    }

    /**
     * Whether the previous token value equals to @param str
     */
    protected boolean prev(String str) {
        return _input.LT(-1).getText().equals(str);
    }

    /**
     * Short form for next(String str)
     */
    protected boolean n(String str) {
        return next(str);
    }

    /**
     * Whether the next token value equals to @param str
     */
    protected boolean next(String str) {
        return _input.LT(1).getText().equals(str);
    }

    protected boolean notLineTerminator() {
        return !here(JSVParser.LineTerminator);
    }

    protected boolean notOpenBraceAndNotFunction() {
        int nextTokenType = _input.LT(1).getType();
        return nextTokenType != JSVParser.OpenBrace && nextTokenType != JSVParser.Function;
    }

    protected boolean closeBrace() {
        return _input.LT(1).getType() == JSVParser.CloseBrace;
    }
    
    /**
     * Returns {@code true} iff on the current index of the parser's
     * token stream a token of the given {@code type} exists on the
     * {@code HIDDEN} channel.
     *
     * @param type
     *         the type of the token on the {@code HIDDEN} channel
     *         to check.
     *
     * @return {@code true} iff on the current index of the parser's
     * token stream a token of the given {@code type} exists on the
     * {@code HIDDEN} channel.
     */
    private boolean here(final int type) {

        // Get the token ahead of the current index.
        int possibleIndexEosToken = this.getCurrentToken().getTokenIndex() - 1;
        Token ahead = _input.get(possibleIndexEosToken);

        // Check if the token resides on the HIDDEN channel and if it's of the
        // provided type.
        return (ahead.getChannel() == Lexer.HIDDEN) && (ahead.getType() == type);
    }
    
    /**
     * Returns {@code true} iff on the current index of the parser's
     * token stream a token exists on the {@code HIDDEN} channel which
     * either is a line terminator, or is a multi line comment that
     * contains a line terminator.
     *
     * @return {@code true} iff on the current index of the parser's
     * token stream a token exists on the {@code HIDDEN} channel which
     * either is a line terminator, or is a multi line comment that
     * contains a line terminator.
     */
    protected boolean lineTerminatorAhead() {

        // Get the token ahead of the current index.
        int possibleIndexEosToken = this.getCurrentToken().getTokenIndex() - 1;
        Token ahead = _input.get(possibleIndexEosToken);

        if (ahead.getChannel() != Lexer.HIDDEN) {
            // We're only interested in tokens on the HIDDEN channel.
            return false;
        }

        if (ahead.getType() == JSVParser.LineTerminator) {
            // There is definitely a line terminator ahead.
            return true;
        }

        if (ahead.getType() == JSVParser.WhiteSpaces) {
            // Get the token ahead of the current whitespaces.
            possibleIndexEosToken = this.getCurrentToken().getTokenIndex() - 2;
            ahead = _input.get(possibleIndexEosToken);
        }

        // Get the token's text and type.
        String text = ahead.getText();
        int type = ahead.getType();

        // Check if the token is, or contains a line terminator.
        return (type == JSVParser.MultiLineComment && (text.contains("\r") || text.contains("\n"))) ||
                (type == JSVParser.LineTerminator);
    }

    /**
     * 行首
     * @return
     */
    protected  boolean lineAhead(){
        int possibleIndexEosToken = this.getCurrentToken().getTokenIndex();
        Token ahead = _input.get(possibleIndexEosToken);
        //System.out.println("Current token = " + ahead + " index " + possibleIndexEosToken  + String.format(" Token startIndex = %d, endIdnex =%d, tokenIndex = %d, line pos = %d, text=%s, type= %d", ahead.getStartIndex(), ahead.getStopIndex(), ahead.getTokenIndex(), ahead.getCharPositionInLine(), ahead.getText(), ahead.getType()));
//        String text = ahead.getText();
//        int type = ahead.getType();
        return (ahead.getCharPositionInLine() ==0);
    }


}