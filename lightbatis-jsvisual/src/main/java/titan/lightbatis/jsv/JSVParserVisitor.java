// Generated from /Users/lifei/workspace/lightbatis/lightbatis-jsvisual/src/main/resources/JSVParser.g4 by ANTLR 4.8
package titan.lightbatis.jsv;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link JSVParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface JSVParserVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link JSVParser#program}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProgram(JSVParser.ProgramContext ctx);
	/**
	 * Visit a parse tree produced by {@link JSVParser#sourceElement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSourceElement(JSVParser.SourceElementContext ctx);
	/**
	 * Visit a parse tree produced by {@link JSVParser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatement(JSVParser.StatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link JSVParser#block}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlock(JSVParser.BlockContext ctx);
	/**
	 * Visit a parse tree produced by {@link JSVParser#statementList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatementList(JSVParser.StatementListContext ctx);
	/**
	 * Visit a parse tree produced by {@link JSVParser#variableStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariableStatement(JSVParser.VariableStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link JSVParser#variableDeclarationList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariableDeclarationList(JSVParser.VariableDeclarationListContext ctx);
	/**
	 * Visit a parse tree produced by {@link JSVParser#variableDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariableDeclaration(JSVParser.VariableDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link JSVParser#emptyStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEmptyStatement(JSVParser.EmptyStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link JSVParser#expressionStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressionStatement(JSVParser.ExpressionStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link JSVParser#ifThenStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIfThenStatement(JSVParser.IfThenStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link JSVParser#matrixStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMatrixStatement(JSVParser.MatrixStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link JSVParser#matrixRow}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMatrixRow(JSVParser.MatrixRowContext ctx);
	/**
	 * Visit a parse tree produced by {@link JSVParser#matrixElement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMatrixElement(JSVParser.MatrixElementContext ctx);
	/**
	 * Visit a parse tree produced by {@link JSVParser#matrixElementNumber}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMatrixElementNumber(JSVParser.MatrixElementNumberContext ctx);
	/**
	 * Visit a parse tree produced by {@link JSVParser#matrixElementClose}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMatrixElementClose(JSVParser.MatrixElementCloseContext ctx);
	/**
	 * Visit a parse tree produced by {@link JSVParser#matrixElementOpen}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMatrixElementOpen(JSVParser.MatrixElementOpenContext ctx);
	/**
	 * Visit a parse tree produced by {@link JSVParser#matrixElementOpenClose}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMatrixElementOpenClose(JSVParser.MatrixElementOpenCloseContext ctx);
	/**
	 * Visit a parse tree produced by {@link JSVParser#matrixElementCloseOpen}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMatrixElementCloseOpen(JSVParser.MatrixElementCloseOpenContext ctx);
	/**
	 * Visit a parse tree produced by {@link JSVParser#matrixElementGreaterThan}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMatrixElementGreaterThan(JSVParser.MatrixElementGreaterThanContext ctx);
	/**
	 * Visit a parse tree produced by {@link JSVParser#matrixElementAtLeast}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMatrixElementAtLeast(JSVParser.MatrixElementAtLeastContext ctx);
	/**
	 * Visit a parse tree produced by {@link JSVParser#matrixElementLessThan}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMatrixElementLessThan(JSVParser.MatrixElementLessThanContext ctx);
	/**
	 * Visit a parse tree produced by {@link JSVParser#matrixElementAtMost}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMatrixElementAtMost(JSVParser.MatrixElementAtMostContext ctx);
	/**
	 * Visit a parse tree produced by {@link JSVParser#varModifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVarModifier(JSVParser.VarModifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link JSVParser#returnStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReturnStatement(JSVParser.ReturnStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link JSVParser#formalParameterList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFormalParameterList(JSVParser.FormalParameterListContext ctx);
	/**
	 * Visit a parse tree produced by {@link JSVParser#formalParameterArg}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFormalParameterArg(JSVParser.FormalParameterArgContext ctx);
	/**
	 * Visit a parse tree produced by {@link JSVParser#lastFormalParameterArg}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLastFormalParameterArg(JSVParser.LastFormalParameterArgContext ctx);
	/**
	 * Visit a parse tree produced by {@link JSVParser#sourceElements}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSourceElements(JSVParser.SourceElementsContext ctx);
	/**
	 * Visit a parse tree produced by {@link JSVParser#arrayLiteral}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArrayLiteral(JSVParser.ArrayLiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link JSVParser#elementList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitElementList(JSVParser.ElementListContext ctx);
	/**
	 * Visit a parse tree produced by {@link JSVParser#arrayElement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArrayElement(JSVParser.ArrayElementContext ctx);
	/**
	 * Visit a parse tree produced by the {@code PropertyExpressionAssignment}
	 * labeled alternative in {@link JSVParser#propertyAssignment}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPropertyExpressionAssignment(JSVParser.PropertyExpressionAssignmentContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ComputedPropertyExpressionAssignment}
	 * labeled alternative in {@link JSVParser#propertyAssignment}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComputedPropertyExpressionAssignment(JSVParser.ComputedPropertyExpressionAssignmentContext ctx);
	/**
	 * Visit a parse tree produced by the {@code PropertyShorthand}
	 * labeled alternative in {@link JSVParser#propertyAssignment}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPropertyShorthand(JSVParser.PropertyShorthandContext ctx);
	/**
	 * Visit a parse tree produced by {@link JSVParser#propertyName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPropertyName(JSVParser.PropertyNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link JSVParser#arguments}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArguments(JSVParser.ArgumentsContext ctx);
	/**
	 * Visit a parse tree produced by {@link JSVParser#argument}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArgument(JSVParser.ArgumentContext ctx);
	/**
	 * Visit a parse tree produced by {@link JSVParser#expressionSequence}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressionSequence(JSVParser.ExpressionSequenceContext ctx);
	/**
	 * Visit a parse tree produced by the {@code TemplateStringExpression}
	 * labeled alternative in {@link JSVParser#singleExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTemplateStringExpression(JSVParser.TemplateStringExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code TernaryExpression}
	 * labeled alternative in {@link JSVParser#singleExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTernaryExpression(JSVParser.TernaryExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code LogicalAndExpression}
	 * labeled alternative in {@link JSVParser#singleExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLogicalAndExpression(JSVParser.LogicalAndExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code PowerExpression}
	 * labeled alternative in {@link JSVParser#singleExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPowerExpression(JSVParser.PowerExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code PreIncrementExpression}
	 * labeled alternative in {@link JSVParser#singleExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPreIncrementExpression(JSVParser.PreIncrementExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ObjectLiteralExpression}
	 * labeled alternative in {@link JSVParser#singleExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitObjectLiteralExpression(JSVParser.ObjectLiteralExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code MetaExpression}
	 * labeled alternative in {@link JSVParser#singleExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMetaExpression(JSVParser.MetaExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code InExpression}
	 * labeled alternative in {@link JSVParser#singleExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInExpression(JSVParser.InExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code LogicalOrExpression}
	 * labeled alternative in {@link JSVParser#singleExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLogicalOrExpression(JSVParser.LogicalOrExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code NotExpression}
	 * labeled alternative in {@link JSVParser#singleExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNotExpression(JSVParser.NotExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code PreDecreaseExpression}
	 * labeled alternative in {@link JSVParser#singleExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPreDecreaseExpression(JSVParser.PreDecreaseExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ArgumentsExpression}
	 * labeled alternative in {@link JSVParser#singleExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArgumentsExpression(JSVParser.ArgumentsExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code AwaitExpression}
	 * labeled alternative in {@link JSVParser#singleExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAwaitExpression(JSVParser.AwaitExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ThisExpression}
	 * labeled alternative in {@link JSVParser#singleExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitThisExpression(JSVParser.ThisExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code UnaryMinusExpression}
	 * labeled alternative in {@link JSVParser#singleExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnaryMinusExpression(JSVParser.UnaryMinusExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code AssignmentExpression}
	 * labeled alternative in {@link JSVParser#singleExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignmentExpression(JSVParser.AssignmentExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code PostDecreaseExpression}
	 * labeled alternative in {@link JSVParser#singleExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPostDecreaseExpression(JSVParser.PostDecreaseExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code TypeofExpression}
	 * labeled alternative in {@link JSVParser#singleExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeofExpression(JSVParser.TypeofExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code InstanceofExpression}
	 * labeled alternative in {@link JSVParser#singleExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInstanceofExpression(JSVParser.InstanceofExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code UnaryPlusExpression}
	 * labeled alternative in {@link JSVParser#singleExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnaryPlusExpression(JSVParser.UnaryPlusExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code DeleteExpression}
	 * labeled alternative in {@link JSVParser#singleExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeleteExpression(JSVParser.DeleteExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ImportExpression}
	 * labeled alternative in {@link JSVParser#singleExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitImportExpression(JSVParser.ImportExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code EqualityExpression}
	 * labeled alternative in {@link JSVParser#singleExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEqualityExpression(JSVParser.EqualityExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code BitXOrExpression}
	 * labeled alternative in {@link JSVParser#singleExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBitXOrExpression(JSVParser.BitXOrExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code SuperExpression}
	 * labeled alternative in {@link JSVParser#singleExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSuperExpression(JSVParser.SuperExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code MultiplicativeExpression}
	 * labeled alternative in {@link JSVParser#singleExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMultiplicativeExpression(JSVParser.MultiplicativeExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code BitShiftExpression}
	 * labeled alternative in {@link JSVParser#singleExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBitShiftExpression(JSVParser.BitShiftExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ParenthesizedExpression}
	 * labeled alternative in {@link JSVParser#singleExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParenthesizedExpression(JSVParser.ParenthesizedExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code AdditiveExpression}
	 * labeled alternative in {@link JSVParser#singleExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAdditiveExpression(JSVParser.AdditiveExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code RelationalExpression}
	 * labeled alternative in {@link JSVParser#singleExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRelationalExpression(JSVParser.RelationalExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code PostIncrementExpression}
	 * labeled alternative in {@link JSVParser#singleExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPostIncrementExpression(JSVParser.PostIncrementExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code BitNotExpression}
	 * labeled alternative in {@link JSVParser#singleExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBitNotExpression(JSVParser.BitNotExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code NewExpression}
	 * labeled alternative in {@link JSVParser#singleExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNewExpression(JSVParser.NewExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code LiteralExpression}
	 * labeled alternative in {@link JSVParser#singleExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLiteralExpression(JSVParser.LiteralExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ArrayLiteralExpression}
	 * labeled alternative in {@link JSVParser#singleExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArrayLiteralExpression(JSVParser.ArrayLiteralExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code MemberDotExpression}
	 * labeled alternative in {@link JSVParser#singleExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMemberDotExpression(JSVParser.MemberDotExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code MemberIndexExpression}
	 * labeled alternative in {@link JSVParser#singleExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMemberIndexExpression(JSVParser.MemberIndexExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code IdentifierExpression}
	 * labeled alternative in {@link JSVParser#singleExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdentifierExpression(JSVParser.IdentifierExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code BitAndExpression}
	 * labeled alternative in {@link JSVParser#singleExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBitAndExpression(JSVParser.BitAndExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code BitOrExpression}
	 * labeled alternative in {@link JSVParser#singleExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBitOrExpression(JSVParser.BitOrExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code AssignmentOperatorExpression}
	 * labeled alternative in {@link JSVParser#singleExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignmentOperatorExpression(JSVParser.AssignmentOperatorExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code VoidExpression}
	 * labeled alternative in {@link JSVParser#singleExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVoidExpression(JSVParser.VoidExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code CoalesceExpression}
	 * labeled alternative in {@link JSVParser#singleExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCoalesceExpression(JSVParser.CoalesceExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link JSVParser#assignable}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignable(JSVParser.AssignableContext ctx);
	/**
	 * Visit a parse tree produced by {@link JSVParser#objectLiteral}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitObjectLiteral(JSVParser.ObjectLiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link JSVParser#assignmentOperator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignmentOperator(JSVParser.AssignmentOperatorContext ctx);
	/**
	 * Visit a parse tree produced by {@link JSVParser#literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLiteral(JSVParser.LiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link JSVParser#numericLiteral}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNumericLiteral(JSVParser.NumericLiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link JSVParser#bigintLiteral}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBigintLiteral(JSVParser.BigintLiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link JSVParser#getter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGetter(JSVParser.GetterContext ctx);
	/**
	 * Visit a parse tree produced by {@link JSVParser#setter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSetter(JSVParser.SetterContext ctx);
	/**
	 * Visit a parse tree produced by {@link JSVParser#identifierName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdentifierName(JSVParser.IdentifierNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link JSVParser#identifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdentifier(JSVParser.IdentifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link JSVParser#reservedWord}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReservedWord(JSVParser.ReservedWordContext ctx);
	/**
	 * Visit a parse tree produced by {@link JSVParser#keyword}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitKeyword(JSVParser.KeywordContext ctx);
	/**
	 * Visit a parse tree produced by {@link JSVParser#let}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLet(JSVParser.LetContext ctx);
	/**
	 * Visit a parse tree produced by {@link JSVParser#eos}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEos(JSVParser.EosContext ctx);
}