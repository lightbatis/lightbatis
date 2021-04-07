package titan.lightbatis.jsv.transform;

import edu.emory.mathcs.backport.java.util.Arrays;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStreamRewriter;
import org.antlr.v4.runtime.tree.*;
import titan.lightbatis.jsv.JSVParser;
import titan.lightbatis.jsv.JSVParserBaseListener;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

public class JSParserListener extends JSVParserBaseListener {
    private TokenStreamRewriter output = null;
    private Stack<String> matrixBody = new Stack<>();
    private Stack<String> matrixArgs = new Stack<>();
    private static final Set<String> paramFunctionSet = new HashSet<>(Arrays.asList(new String[]{"_greaterThan","_atLeast", "_lessThan", "_atMost"}));
    private int matrixArgIndex = 0;
    private String mainFunction = null;
    public JSParserListener(CommonTokenStream tokens, String functionName) {
        this.output = new TokenStreamRewriter(tokens);
        this.mainFunction = functionName;
        JSVParser parser = new JSVParser(tokens);
        JSVParser.ProgramContext context = parser.program();
        ParseTreeWalker walker = new ParseTreeWalker();
        walker.walk(this, context);
    }

    @Override
    public void enterProgram(JSVParser.ProgramContext ctx) {
        if (mainFunction != null) {
            output.insertBefore(ctx.getStart(), "function " + mainFunction + " () {\n");
        }
    }

    @Override
    public void exitProgram(JSVParser.ProgramContext ctx) {
        if (mainFunction != null) {
            output.insertAfter(ctx.getStop(), "\n}");
        }
    }

    @Override
    public void enterIfThenStatement(JSVParser.IfThenStatementContext ctx) {
        // 将表达式前后加一个 ()
        JSVParser.ExpressionSequenceContext exprSeqContext = ctx.expressionSequence();
        this.output.insertBefore(exprSeqContext.getStart(), '(');
        this.output.insertAfter(exprSeqContext.getStop(), ')');

        TerminalNode node = ctx.Then();
        this.output.replace(node.getSymbol(), '{');
        this.output.replace(ctx.Endif().getSymbol(), '}');
    }

    @Override
    public void enterMatrixStatement(JSVParser.MatrixStatementContext ctx) {
        Token start = ctx.getStart();
        output.insertBefore(start, "_matrix");
        matrixBody.clear();
        List<TerminalNode> nodes = ctx.Identifier();
        for (int i=0;i< nodes.size();i++) {
            TerminalNode node = nodes.get(i);
            if (i == 0) {
                output.insertBefore(node.getSymbol(),"[");
            }
            matrixArgs.push(node.getSymbol().getText());
            output.replace(node.getSymbol(), "'" + node.getSymbol().getText() + "'");
            if (i == nodes.size()-1) {
                output.insertAfter(node.getSymbol(),"]");
            }
        }
        matrixBody.push(",");
        matrixBody.push("[");
    }

    /**
     * 矩阵的每一行数据
     * @param ctx
     */
    @Override
    public void enterMatrixRow(JSVParser.MatrixRowContext ctx) {
        matrixBody.push("\n[");
        //output.replace(ctx.getStart(), "");

    }
    @Override
    public void exitMatrixRow(JSVParser.MatrixRowContext ctx) {
        matrixBody.pop();
        matrixBody.push("]");
        matrixBody.push(",");
        //matrixBody.push("\n");
        output.delete(ctx.getStart(), ctx.getStop());
        matrixArgIndex = 0;
    }
    @Override
    public void enterMatrixElement(JSVParser.MatrixElementContext ctx) {

    }

    @Override
    public void exitMatrixElement(JSVParser.MatrixElementContext ctx) {
        matrixArgIndex++;
    }

    @Override
    public void exitMatrixStatement(JSVParser.MatrixStatementContext ctx) {
        matrixBody.pop();
        matrixBody.push("\n");
        matrixBody.push("]");
        StringBuffer buffer = new StringBuffer();
        matrixBody.stream().forEach( str -> {
            buffer.append(str);
        });
        //output.insertBefore(ctx.getStop(), buffer.toString());
        TerminalNode closedNode = ctx.CloseParen();
        output.insertBefore(closedNode.getSymbol(), buffer.toString());
        matrixArgs.clear();
        matrixBody.clear();
    }

    @Override
    public void enterMatrixElementNumber(JSVParser.MatrixElementNumberContext ctx) {
        matrixBody.push(ctx.getText());
        matrixBody.push(",");
    }

    @Override
    public void enterMatrixElementClose(JSVParser.MatrixElementCloseContext ctx) {
        processMatrixElementArgs("_rangeClose", ctx);
    }
    @Override
    public void enterMatrixElementCloseOpen(JSVParser.MatrixElementCloseOpenContext ctx) {
        processMatrixElementArgs("_rangeCloseOpen", ctx);
    }

    @Override
    public void enterMatrixElementOpen(JSVParser.MatrixElementOpenContext ctx) {
        processMatrixElementArgs("_rangeOpen", ctx);
    }

    @Override
    public void enterMatrixElementOpenClose(JSVParser.MatrixElementOpenCloseContext ctx) {
        processMatrixElementArgs("_rangeOpenClose", ctx);
    }

    @Override
    public void enterMatrixElementGreaterThan(JSVParser.MatrixElementGreaterThanContext ctx) {
        processMatrixElementArgs("_greaterThan", ctx);
    }

    @Override
    public void enterMatrixElementAtLeast(JSVParser.MatrixElementAtLeastContext ctx) {
        processMatrixElementArgs("_atLeast", ctx);
    }

    @Override
    public void enterMatrixElementLessThan(JSVParser.MatrixElementLessThanContext ctx) {
        processMatrixElementArgs("_lessThan", ctx);
    }

    @Override
    public void enterMatrixElementAtMost(JSVParser.MatrixElementAtMostContext ctx) {
        processMatrixElementArgs("_atMost", ctx);
    }

    private void processMatrixElementArgs(String rangeType, ParserRuleContext ctx) {
        matrixBody.push(rangeType);
        matrixBody.push("(");
        ParseTreeWalker.DEFAULT.walk(new RemoveOpenBracketAndCloseParen(matrixBody, rangeType), ctx);
        String varName = matrixArgs.get(matrixArgIndex);
        matrixBody.push(",");
        matrixBody.push(varName);
        matrixBody.push(")");
        matrixBody.push(",");
    }

    public String parse() {
        return this.output.getText();
    }


    private class RemoveOpenBracketAndCloseParen implements ParseTreeListener {

        private Stack<String> content = null;
        private String rangeType = null;
        RemoveOpenBracketAndCloseParen(Stack<String> content, String rangeType) {
            this.content = content;
            this.rangeType = rangeType;
        }
        @Override
        public void visitTerminal(TerminalNode node) {
            Token token = node.getSymbol();
            if (token.getType() == JSVParser.OpenBracket ||
                    token.getType() == JSVParser.CloseParen ||
                    token.getType() == JSVParser.OpenParen ||
                    token.getType() == JSVParser.CloseBracket)  {

            } else {
                String word = token.getText();
                // 去掉区间表达式里的 +- 符号， 转换成相对应的函数了。
                if (!(word.equals("+") || word.equals("-"))) {
                    //转换成 +- 函数的操作，由于少了一个参数， 所以删除一个 , 分隔符。
                    if (paramFunctionSet.contains(rangeType) && (token.getType() == JSVParser.Comma)) {
                        return;
                    }
                    content.push(token.getText());
                }
            }
        }

        @Override
        public void visitErrorNode(ErrorNode node) {

        }

        @Override
        public void enterEveryRule(ParserRuleContext ctx) {
        }

        @Override
        public void exitEveryRule(ParserRuleContext ctx) {

        }
    }
}
