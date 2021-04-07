// Generated from /Users/lifei/workspace/lightbatis/lightbatis-jsvisual/src/main/resources/JSVParser.g4 by ANTLR 4.8
package titan.lightbatis.jsv;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link JSVParser}.
 */
public interface JSVParserListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link JSVParser#program}.
	 * @param ctx the parse tree
	 */
	void enterProgram(JSVParser.ProgramContext ctx);
	/**
	 * Exit a parse tree produced by {@link JSVParser#program}.
	 * @param ctx the parse tree
	 */
	void exitProgram(JSVParser.ProgramContext ctx);
	/**
	 * Enter a parse tree produced by {@link JSVParser#sourceElement}.
	 * @param ctx the parse tree
	 */
	void enterSourceElement(JSVParser.SourceElementContext ctx);
	/**
	 * Exit a parse tree produced by {@link JSVParser#sourceElement}.
	 * @param ctx the parse tree
	 */
	void exitSourceElement(JSVParser.SourceElementContext ctx);
	/**
	 * Enter a parse tree produced by {@link JSVParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStatement(JSVParser.StatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link JSVParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStatement(JSVParser.StatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link JSVParser#block}.
	 * @param ctx the parse tree
	 */
	void enterBlock(JSVParser.BlockContext ctx);
	/**
	 * Exit a parse tree produced by {@link JSVParser#block}.
	 * @param ctx the parse tree
	 */
	void exitBlock(JSVParser.BlockContext ctx);
	/**
	 * Enter a parse tree produced by {@link JSVParser#statementList}.
	 * @param ctx the parse tree
	 */
	void enterStatementList(JSVParser.StatementListContext ctx);
	/**
	 * Exit a parse tree produced by {@link JSVParser#statementList}.
	 * @param ctx the parse tree
	 */
	void exitStatementList(JSVParser.StatementListContext ctx);
	/**
	 * Enter a parse tree produced by {@link JSVParser#variableStatement}.
	 * @param ctx the parse tree
	 */
	void enterVariableStatement(JSVParser.VariableStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link JSVParser#variableStatement}.
	 * @param ctx the parse tree
	 */
	void exitVariableStatement(JSVParser.VariableStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link JSVParser#variableDeclarationList}.
	 * @param ctx the parse tree
	 */
	void enterVariableDeclarationList(JSVParser.VariableDeclarationListContext ctx);
	/**
	 * Exit a parse tree produced by {@link JSVParser#variableDeclarationList}.
	 * @param ctx the parse tree
	 */
	void exitVariableDeclarationList(JSVParser.VariableDeclarationListContext ctx);
	/**
	 * Enter a parse tree produced by {@link JSVParser#variableDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterVariableDeclaration(JSVParser.VariableDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link JSVParser#variableDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitVariableDeclaration(JSVParser.VariableDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link JSVParser#emptyStatement}.
	 * @param ctx the parse tree
	 */
	void enterEmptyStatement(JSVParser.EmptyStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link JSVParser#emptyStatement}.
	 * @param ctx the parse tree
	 */
	void exitEmptyStatement(JSVParser.EmptyStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link JSVParser#expressionStatement}.
	 * @param ctx the parse tree
	 */
	void enterExpressionStatement(JSVParser.ExpressionStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link JSVParser#expressionStatement}.
	 * @param ctx the parse tree
	 */
	void exitExpressionStatement(JSVParser.ExpressionStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link JSVParser#ifThenStatement}.
	 * @param ctx the parse tree
	 */
	void enterIfThenStatement(JSVParser.IfThenStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link JSVParser#ifThenStatement}.
	 * @param ctx the parse tree
	 */
	void exitIfThenStatement(JSVParser.IfThenStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link JSVParser#matrixStatement}.
	 * @param ctx the parse tree
	 */
	void enterMatrixStatement(JSVParser.MatrixStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link JSVParser#matrixStatement}.
	 * @param ctx the parse tree
	 */
	void exitMatrixStatement(JSVParser.MatrixStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link JSVParser#matrixRow}.
	 * @param ctx the parse tree
	 */
	void enterMatrixRow(JSVParser.MatrixRowContext ctx);
	/**
	 * Exit a parse tree produced by {@link JSVParser#matrixRow}.
	 * @param ctx the parse tree
	 */
	void exitMatrixRow(JSVParser.MatrixRowContext ctx);
	/**
	 * Enter a parse tree produced by {@link JSVParser#matrixElement}.
	 * @param ctx the parse tree
	 */
	void enterMatrixElement(JSVParser.MatrixElementContext ctx);
	/**
	 * Exit a parse tree produced by {@link JSVParser#matrixElement}.
	 * @param ctx the parse tree
	 */
	void exitMatrixElement(JSVParser.MatrixElementContext ctx);
	/**
	 * Enter a parse tree produced by {@link JSVParser#matrixElementNumber}.
	 * @param ctx the parse tree
	 */
	void enterMatrixElementNumber(JSVParser.MatrixElementNumberContext ctx);
	/**
	 * Exit a parse tree produced by {@link JSVParser#matrixElementNumber}.
	 * @param ctx the parse tree
	 */
	void exitMatrixElementNumber(JSVParser.MatrixElementNumberContext ctx);
	/**
	 * Enter a parse tree produced by {@link JSVParser#matrixElementClose}.
	 * @param ctx the parse tree
	 */
	void enterMatrixElementClose(JSVParser.MatrixElementCloseContext ctx);
	/**
	 * Exit a parse tree produced by {@link JSVParser#matrixElementClose}.
	 * @param ctx the parse tree
	 */
	void exitMatrixElementClose(JSVParser.MatrixElementCloseContext ctx);
	/**
	 * Enter a parse tree produced by {@link JSVParser#matrixElementOpen}.
	 * @param ctx the parse tree
	 */
	void enterMatrixElementOpen(JSVParser.MatrixElementOpenContext ctx);
	/**
	 * Exit a parse tree produced by {@link JSVParser#matrixElementOpen}.
	 * @param ctx the parse tree
	 */
	void exitMatrixElementOpen(JSVParser.MatrixElementOpenContext ctx);
	/**
	 * Enter a parse tree produced by {@link JSVParser#matrixElementOpenClose}.
	 * @param ctx the parse tree
	 */
	void enterMatrixElementOpenClose(JSVParser.MatrixElementOpenCloseContext ctx);
	/**
	 * Exit a parse tree produced by {@link JSVParser#matrixElementOpenClose}.
	 * @param ctx the parse tree
	 */
	void exitMatrixElementOpenClose(JSVParser.MatrixElementOpenCloseContext ctx);
	/**
	 * Enter a parse tree produced by {@link JSVParser#matrixElementCloseOpen}.
	 * @param ctx the parse tree
	 */
	void enterMatrixElementCloseOpen(JSVParser.MatrixElementCloseOpenContext ctx);
	/**
	 * Exit a parse tree produced by {@link JSVParser#matrixElementCloseOpen}.
	 * @param ctx the parse tree
	 */
	void exitMatrixElementCloseOpen(JSVParser.MatrixElementCloseOpenContext ctx);
	/**
	 * Enter a parse tree produced by {@link JSVParser#matrixElementGreaterThan}.
	 * @param ctx the parse tree
	 */
	void enterMatrixElementGreaterThan(JSVParser.MatrixElementGreaterThanContext ctx);
	/**
	 * Exit a parse tree produced by {@link JSVParser#matrixElementGreaterThan}.
	 * @param ctx the parse tree
	 */
	void exitMatrixElementGreaterThan(JSVParser.MatrixElementGreaterThanContext ctx);
	/**
	 * Enter a parse tree produced by {@link JSVParser#matrixElementAtLeast}.
	 * @param ctx the parse tree
	 */
	void enterMatrixElementAtLeast(JSVParser.MatrixElementAtLeastContext ctx);
	/**
	 * Exit a parse tree produced by {@link JSVParser#matrixElementAtLeast}.
	 * @param ctx the parse tree
	 */
	void exitMatrixElementAtLeast(JSVParser.MatrixElementAtLeastContext ctx);
	/**
	 * Enter a parse tree produced by {@link JSVParser#matrixElementLessThan}.
	 * @param ctx the parse tree
	 */
	void enterMatrixElementLessThan(JSVParser.MatrixElementLessThanContext ctx);
	/**
	 * Exit a parse tree produced by {@link JSVParser#matrixElementLessThan}.
	 * @param ctx the parse tree
	 */
	void exitMatrixElementLessThan(JSVParser.MatrixElementLessThanContext ctx);
	/**
	 * Enter a parse tree produced by {@link JSVParser#matrixElementAtMost}.
	 * @param ctx the parse tree
	 */
	void enterMatrixElementAtMost(JSVParser.MatrixElementAtMostContext ctx);
	/**
	 * Exit a parse tree produced by {@link JSVParser#matrixElementAtMost}.
	 * @param ctx the parse tree
	 */
	void exitMatrixElementAtMost(JSVParser.MatrixElementAtMostContext ctx);
	/**
	 * Enter a parse tree produced by {@link JSVParser#varModifier}.
	 * @param ctx the parse tree
	 */
	void enterVarModifier(JSVParser.VarModifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link JSVParser#varModifier}.
	 * @param ctx the parse tree
	 */
	void exitVarModifier(JSVParser.VarModifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link JSVParser#returnStatement}.
	 * @param ctx the parse tree
	 */
	void enterReturnStatement(JSVParser.ReturnStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link JSVParser#returnStatement}.
	 * @param ctx the parse tree
	 */
	void exitReturnStatement(JSVParser.ReturnStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link JSVParser#formalParameterList}.
	 * @param ctx the parse tree
	 */
	void enterFormalParameterList(JSVParser.FormalParameterListContext ctx);
	/**
	 * Exit a parse tree produced by {@link JSVParser#formalParameterList}.
	 * @param ctx the parse tree
	 */
	void exitFormalParameterList(JSVParser.FormalParameterListContext ctx);
	/**
	 * Enter a parse tree produced by {@link JSVParser#formalParameterArg}.
	 * @param ctx the parse tree
	 */
	void enterFormalParameterArg(JSVParser.FormalParameterArgContext ctx);
	/**
	 * Exit a parse tree produced by {@link JSVParser#formalParameterArg}.
	 * @param ctx the parse tree
	 */
	void exitFormalParameterArg(JSVParser.FormalParameterArgContext ctx);
	/**
	 * Enter a parse tree produced by {@link JSVParser#lastFormalParameterArg}.
	 * @param ctx the parse tree
	 */
	void enterLastFormalParameterArg(JSVParser.LastFormalParameterArgContext ctx);
	/**
	 * Exit a parse tree produced by {@link JSVParser#lastFormalParameterArg}.
	 * @param ctx the parse tree
	 */
	void exitLastFormalParameterArg(JSVParser.LastFormalParameterArgContext ctx);
	/**
	 * Enter a parse tree produced by {@link JSVParser#sourceElements}.
	 * @param ctx the parse tree
	 */
	void enterSourceElements(JSVParser.SourceElementsContext ctx);
	/**
	 * Exit a parse tree produced by {@link JSVParser#sourceElements}.
	 * @param ctx the parse tree
	 */
	void exitSourceElements(JSVParser.SourceElementsContext ctx);
	/**
	 * Enter a parse tree produced by {@link JSVParser#arrayLiteral}.
	 * @param ctx the parse tree
	 */
	void enterArrayLiteral(JSVParser.ArrayLiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link JSVParser#arrayLiteral}.
	 * @param ctx the parse tree
	 */
	void exitArrayLiteral(JSVParser.ArrayLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link JSVParser#elementList}.
	 * @param ctx the parse tree
	 */
	void enterElementList(JSVParser.ElementListContext ctx);
	/**
	 * Exit a parse tree produced by {@link JSVParser#elementList}.
	 * @param ctx the parse tree
	 */
	void exitElementList(JSVParser.ElementListContext ctx);
	/**
	 * Enter a parse tree produced by {@link JSVParser#arrayElement}.
	 * @param ctx the parse tree
	 */
	void enterArrayElement(JSVParser.ArrayElementContext ctx);
	/**
	 * Exit a parse tree produced by {@link JSVParser#arrayElement}.
	 * @param ctx the parse tree
	 */
	void exitArrayElement(JSVParser.ArrayElementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code PropertyExpressionAssignment}
	 * labeled alternative in {@link JSVParser#propertyAssignment}.
	 * @param ctx the parse tree
	 */
	void enterPropertyExpressionAssignment(JSVParser.PropertyExpressionAssignmentContext ctx);
	/**
	 * Exit a parse tree produced by the {@code PropertyExpressionAssignment}
	 * labeled alternative in {@link JSVParser#propertyAssignment}.
	 * @param ctx the parse tree
	 */
	void exitPropertyExpressionAssignment(JSVParser.PropertyExpressionAssignmentContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ComputedPropertyExpressionAssignment}
	 * labeled alternative in {@link JSVParser#propertyAssignment}.
	 * @param ctx the parse tree
	 */
	void enterComputedPropertyExpressionAssignment(JSVParser.ComputedPropertyExpressionAssignmentContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ComputedPropertyExpressionAssignment}
	 * labeled alternative in {@link JSVParser#propertyAssignment}.
	 * @param ctx the parse tree
	 */
	void exitComputedPropertyExpressionAssignment(JSVParser.ComputedPropertyExpressionAssignmentContext ctx);
	/**
	 * Enter a parse tree produced by the {@code PropertyShorthand}
	 * labeled alternative in {@link JSVParser#propertyAssignment}.
	 * @param ctx the parse tree
	 */
	void enterPropertyShorthand(JSVParser.PropertyShorthandContext ctx);
	/**
	 * Exit a parse tree produced by the {@code PropertyShorthand}
	 * labeled alternative in {@link JSVParser#propertyAssignment}.
	 * @param ctx the parse tree
	 */
	void exitPropertyShorthand(JSVParser.PropertyShorthandContext ctx);
	/**
	 * Enter a parse tree produced by {@link JSVParser#propertyName}.
	 * @param ctx the parse tree
	 */
	void enterPropertyName(JSVParser.PropertyNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link JSVParser#propertyName}.
	 * @param ctx the parse tree
	 */
	void exitPropertyName(JSVParser.PropertyNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link JSVParser#arguments}.
	 * @param ctx the parse tree
	 */
	void enterArguments(JSVParser.ArgumentsContext ctx);
	/**
	 * Exit a parse tree produced by {@link JSVParser#arguments}.
	 * @param ctx the parse tree
	 */
	void exitArguments(JSVParser.ArgumentsContext ctx);
	/**
	 * Enter a parse tree produced by {@link JSVParser#argument}.
	 * @param ctx the parse tree
	 */
	void enterArgument(JSVParser.ArgumentContext ctx);
	/**
	 * Exit a parse tree produced by {@link JSVParser#argument}.
	 * @param ctx the parse tree
	 */
	void exitArgument(JSVParser.ArgumentContext ctx);
	/**
	 * Enter a parse tree produced by {@link JSVParser#expressionSequence}.
	 * @param ctx the parse tree
	 */
	void enterExpressionSequence(JSVParser.ExpressionSequenceContext ctx);
	/**
	 * Exit a parse tree produced by {@link JSVParser#expressionSequence}.
	 * @param ctx the parse tree
	 */
	void exitExpressionSequence(JSVParser.ExpressionSequenceContext ctx);
	/**
	 * Enter a parse tree produced by the {@code TemplateStringExpression}
	 * labeled alternative in {@link JSVParser#singleExpression}.
	 * @param ctx the parse tree
	 */
	void enterTemplateStringExpression(JSVParser.TemplateStringExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code TemplateStringExpression}
	 * labeled alternative in {@link JSVParser#singleExpression}.
	 * @param ctx the parse tree
	 */
	void exitTemplateStringExpression(JSVParser.TemplateStringExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code TernaryExpression}
	 * labeled alternative in {@link JSVParser#singleExpression}.
	 * @param ctx the parse tree
	 */
	void enterTernaryExpression(JSVParser.TernaryExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code TernaryExpression}
	 * labeled alternative in {@link JSVParser#singleExpression}.
	 * @param ctx the parse tree
	 */
	void exitTernaryExpression(JSVParser.TernaryExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code LogicalAndExpression}
	 * labeled alternative in {@link JSVParser#singleExpression}.
	 * @param ctx the parse tree
	 */
	void enterLogicalAndExpression(JSVParser.LogicalAndExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code LogicalAndExpression}
	 * labeled alternative in {@link JSVParser#singleExpression}.
	 * @param ctx the parse tree
	 */
	void exitLogicalAndExpression(JSVParser.LogicalAndExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code PowerExpression}
	 * labeled alternative in {@link JSVParser#singleExpression}.
	 * @param ctx the parse tree
	 */
	void enterPowerExpression(JSVParser.PowerExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code PowerExpression}
	 * labeled alternative in {@link JSVParser#singleExpression}.
	 * @param ctx the parse tree
	 */
	void exitPowerExpression(JSVParser.PowerExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code PreIncrementExpression}
	 * labeled alternative in {@link JSVParser#singleExpression}.
	 * @param ctx the parse tree
	 */
	void enterPreIncrementExpression(JSVParser.PreIncrementExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code PreIncrementExpression}
	 * labeled alternative in {@link JSVParser#singleExpression}.
	 * @param ctx the parse tree
	 */
	void exitPreIncrementExpression(JSVParser.PreIncrementExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ObjectLiteralExpression}
	 * labeled alternative in {@link JSVParser#singleExpression}.
	 * @param ctx the parse tree
	 */
	void enterObjectLiteralExpression(JSVParser.ObjectLiteralExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ObjectLiteralExpression}
	 * labeled alternative in {@link JSVParser#singleExpression}.
	 * @param ctx the parse tree
	 */
	void exitObjectLiteralExpression(JSVParser.ObjectLiteralExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code MetaExpression}
	 * labeled alternative in {@link JSVParser#singleExpression}.
	 * @param ctx the parse tree
	 */
	void enterMetaExpression(JSVParser.MetaExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code MetaExpression}
	 * labeled alternative in {@link JSVParser#singleExpression}.
	 * @param ctx the parse tree
	 */
	void exitMetaExpression(JSVParser.MetaExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code InExpression}
	 * labeled alternative in {@link JSVParser#singleExpression}.
	 * @param ctx the parse tree
	 */
	void enterInExpression(JSVParser.InExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code InExpression}
	 * labeled alternative in {@link JSVParser#singleExpression}.
	 * @param ctx the parse tree
	 */
	void exitInExpression(JSVParser.InExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code LogicalOrExpression}
	 * labeled alternative in {@link JSVParser#singleExpression}.
	 * @param ctx the parse tree
	 */
	void enterLogicalOrExpression(JSVParser.LogicalOrExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code LogicalOrExpression}
	 * labeled alternative in {@link JSVParser#singleExpression}.
	 * @param ctx the parse tree
	 */
	void exitLogicalOrExpression(JSVParser.LogicalOrExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code NotExpression}
	 * labeled alternative in {@link JSVParser#singleExpression}.
	 * @param ctx the parse tree
	 */
	void enterNotExpression(JSVParser.NotExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code NotExpression}
	 * labeled alternative in {@link JSVParser#singleExpression}.
	 * @param ctx the parse tree
	 */
	void exitNotExpression(JSVParser.NotExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code PreDecreaseExpression}
	 * labeled alternative in {@link JSVParser#singleExpression}.
	 * @param ctx the parse tree
	 */
	void enterPreDecreaseExpression(JSVParser.PreDecreaseExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code PreDecreaseExpression}
	 * labeled alternative in {@link JSVParser#singleExpression}.
	 * @param ctx the parse tree
	 */
	void exitPreDecreaseExpression(JSVParser.PreDecreaseExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ArgumentsExpression}
	 * labeled alternative in {@link JSVParser#singleExpression}.
	 * @param ctx the parse tree
	 */
	void enterArgumentsExpression(JSVParser.ArgumentsExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ArgumentsExpression}
	 * labeled alternative in {@link JSVParser#singleExpression}.
	 * @param ctx the parse tree
	 */
	void exitArgumentsExpression(JSVParser.ArgumentsExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code AwaitExpression}
	 * labeled alternative in {@link JSVParser#singleExpression}.
	 * @param ctx the parse tree
	 */
	void enterAwaitExpression(JSVParser.AwaitExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code AwaitExpression}
	 * labeled alternative in {@link JSVParser#singleExpression}.
	 * @param ctx the parse tree
	 */
	void exitAwaitExpression(JSVParser.AwaitExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ThisExpression}
	 * labeled alternative in {@link JSVParser#singleExpression}.
	 * @param ctx the parse tree
	 */
	void enterThisExpression(JSVParser.ThisExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ThisExpression}
	 * labeled alternative in {@link JSVParser#singleExpression}.
	 * @param ctx the parse tree
	 */
	void exitThisExpression(JSVParser.ThisExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code UnaryMinusExpression}
	 * labeled alternative in {@link JSVParser#singleExpression}.
	 * @param ctx the parse tree
	 */
	void enterUnaryMinusExpression(JSVParser.UnaryMinusExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code UnaryMinusExpression}
	 * labeled alternative in {@link JSVParser#singleExpression}.
	 * @param ctx the parse tree
	 */
	void exitUnaryMinusExpression(JSVParser.UnaryMinusExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code AssignmentExpression}
	 * labeled alternative in {@link JSVParser#singleExpression}.
	 * @param ctx the parse tree
	 */
	void enterAssignmentExpression(JSVParser.AssignmentExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code AssignmentExpression}
	 * labeled alternative in {@link JSVParser#singleExpression}.
	 * @param ctx the parse tree
	 */
	void exitAssignmentExpression(JSVParser.AssignmentExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code PostDecreaseExpression}
	 * labeled alternative in {@link JSVParser#singleExpression}.
	 * @param ctx the parse tree
	 */
	void enterPostDecreaseExpression(JSVParser.PostDecreaseExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code PostDecreaseExpression}
	 * labeled alternative in {@link JSVParser#singleExpression}.
	 * @param ctx the parse tree
	 */
	void exitPostDecreaseExpression(JSVParser.PostDecreaseExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code TypeofExpression}
	 * labeled alternative in {@link JSVParser#singleExpression}.
	 * @param ctx the parse tree
	 */
	void enterTypeofExpression(JSVParser.TypeofExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code TypeofExpression}
	 * labeled alternative in {@link JSVParser#singleExpression}.
	 * @param ctx the parse tree
	 */
	void exitTypeofExpression(JSVParser.TypeofExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code InstanceofExpression}
	 * labeled alternative in {@link JSVParser#singleExpression}.
	 * @param ctx the parse tree
	 */
	void enterInstanceofExpression(JSVParser.InstanceofExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code InstanceofExpression}
	 * labeled alternative in {@link JSVParser#singleExpression}.
	 * @param ctx the parse tree
	 */
	void exitInstanceofExpression(JSVParser.InstanceofExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code UnaryPlusExpression}
	 * labeled alternative in {@link JSVParser#singleExpression}.
	 * @param ctx the parse tree
	 */
	void enterUnaryPlusExpression(JSVParser.UnaryPlusExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code UnaryPlusExpression}
	 * labeled alternative in {@link JSVParser#singleExpression}.
	 * @param ctx the parse tree
	 */
	void exitUnaryPlusExpression(JSVParser.UnaryPlusExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code DeleteExpression}
	 * labeled alternative in {@link JSVParser#singleExpression}.
	 * @param ctx the parse tree
	 */
	void enterDeleteExpression(JSVParser.DeleteExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code DeleteExpression}
	 * labeled alternative in {@link JSVParser#singleExpression}.
	 * @param ctx the parse tree
	 */
	void exitDeleteExpression(JSVParser.DeleteExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ImportExpression}
	 * labeled alternative in {@link JSVParser#singleExpression}.
	 * @param ctx the parse tree
	 */
	void enterImportExpression(JSVParser.ImportExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ImportExpression}
	 * labeled alternative in {@link JSVParser#singleExpression}.
	 * @param ctx the parse tree
	 */
	void exitImportExpression(JSVParser.ImportExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code EqualityExpression}
	 * labeled alternative in {@link JSVParser#singleExpression}.
	 * @param ctx the parse tree
	 */
	void enterEqualityExpression(JSVParser.EqualityExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code EqualityExpression}
	 * labeled alternative in {@link JSVParser#singleExpression}.
	 * @param ctx the parse tree
	 */
	void exitEqualityExpression(JSVParser.EqualityExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code BitXOrExpression}
	 * labeled alternative in {@link JSVParser#singleExpression}.
	 * @param ctx the parse tree
	 */
	void enterBitXOrExpression(JSVParser.BitXOrExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code BitXOrExpression}
	 * labeled alternative in {@link JSVParser#singleExpression}.
	 * @param ctx the parse tree
	 */
	void exitBitXOrExpression(JSVParser.BitXOrExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code SuperExpression}
	 * labeled alternative in {@link JSVParser#singleExpression}.
	 * @param ctx the parse tree
	 */
	void enterSuperExpression(JSVParser.SuperExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code SuperExpression}
	 * labeled alternative in {@link JSVParser#singleExpression}.
	 * @param ctx the parse tree
	 */
	void exitSuperExpression(JSVParser.SuperExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code MultiplicativeExpression}
	 * labeled alternative in {@link JSVParser#singleExpression}.
	 * @param ctx the parse tree
	 */
	void enterMultiplicativeExpression(JSVParser.MultiplicativeExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code MultiplicativeExpression}
	 * labeled alternative in {@link JSVParser#singleExpression}.
	 * @param ctx the parse tree
	 */
	void exitMultiplicativeExpression(JSVParser.MultiplicativeExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code BitShiftExpression}
	 * labeled alternative in {@link JSVParser#singleExpression}.
	 * @param ctx the parse tree
	 */
	void enterBitShiftExpression(JSVParser.BitShiftExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code BitShiftExpression}
	 * labeled alternative in {@link JSVParser#singleExpression}.
	 * @param ctx the parse tree
	 */
	void exitBitShiftExpression(JSVParser.BitShiftExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ParenthesizedExpression}
	 * labeled alternative in {@link JSVParser#singleExpression}.
	 * @param ctx the parse tree
	 */
	void enterParenthesizedExpression(JSVParser.ParenthesizedExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ParenthesizedExpression}
	 * labeled alternative in {@link JSVParser#singleExpression}.
	 * @param ctx the parse tree
	 */
	void exitParenthesizedExpression(JSVParser.ParenthesizedExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code AdditiveExpression}
	 * labeled alternative in {@link JSVParser#singleExpression}.
	 * @param ctx the parse tree
	 */
	void enterAdditiveExpression(JSVParser.AdditiveExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code AdditiveExpression}
	 * labeled alternative in {@link JSVParser#singleExpression}.
	 * @param ctx the parse tree
	 */
	void exitAdditiveExpression(JSVParser.AdditiveExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code RelationalExpression}
	 * labeled alternative in {@link JSVParser#singleExpression}.
	 * @param ctx the parse tree
	 */
	void enterRelationalExpression(JSVParser.RelationalExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code RelationalExpression}
	 * labeled alternative in {@link JSVParser#singleExpression}.
	 * @param ctx the parse tree
	 */
	void exitRelationalExpression(JSVParser.RelationalExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code PostIncrementExpression}
	 * labeled alternative in {@link JSVParser#singleExpression}.
	 * @param ctx the parse tree
	 */
	void enterPostIncrementExpression(JSVParser.PostIncrementExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code PostIncrementExpression}
	 * labeled alternative in {@link JSVParser#singleExpression}.
	 * @param ctx the parse tree
	 */
	void exitPostIncrementExpression(JSVParser.PostIncrementExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code BitNotExpression}
	 * labeled alternative in {@link JSVParser#singleExpression}.
	 * @param ctx the parse tree
	 */
	void enterBitNotExpression(JSVParser.BitNotExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code BitNotExpression}
	 * labeled alternative in {@link JSVParser#singleExpression}.
	 * @param ctx the parse tree
	 */
	void exitBitNotExpression(JSVParser.BitNotExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code NewExpression}
	 * labeled alternative in {@link JSVParser#singleExpression}.
	 * @param ctx the parse tree
	 */
	void enterNewExpression(JSVParser.NewExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code NewExpression}
	 * labeled alternative in {@link JSVParser#singleExpression}.
	 * @param ctx the parse tree
	 */
	void exitNewExpression(JSVParser.NewExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code LiteralExpression}
	 * labeled alternative in {@link JSVParser#singleExpression}.
	 * @param ctx the parse tree
	 */
	void enterLiteralExpression(JSVParser.LiteralExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code LiteralExpression}
	 * labeled alternative in {@link JSVParser#singleExpression}.
	 * @param ctx the parse tree
	 */
	void exitLiteralExpression(JSVParser.LiteralExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ArrayLiteralExpression}
	 * labeled alternative in {@link JSVParser#singleExpression}.
	 * @param ctx the parse tree
	 */
	void enterArrayLiteralExpression(JSVParser.ArrayLiteralExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ArrayLiteralExpression}
	 * labeled alternative in {@link JSVParser#singleExpression}.
	 * @param ctx the parse tree
	 */
	void exitArrayLiteralExpression(JSVParser.ArrayLiteralExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code MemberDotExpression}
	 * labeled alternative in {@link JSVParser#singleExpression}.
	 * @param ctx the parse tree
	 */
	void enterMemberDotExpression(JSVParser.MemberDotExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code MemberDotExpression}
	 * labeled alternative in {@link JSVParser#singleExpression}.
	 * @param ctx the parse tree
	 */
	void exitMemberDotExpression(JSVParser.MemberDotExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code MemberIndexExpression}
	 * labeled alternative in {@link JSVParser#singleExpression}.
	 * @param ctx the parse tree
	 */
	void enterMemberIndexExpression(JSVParser.MemberIndexExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code MemberIndexExpression}
	 * labeled alternative in {@link JSVParser#singleExpression}.
	 * @param ctx the parse tree
	 */
	void exitMemberIndexExpression(JSVParser.MemberIndexExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code IdentifierExpression}
	 * labeled alternative in {@link JSVParser#singleExpression}.
	 * @param ctx the parse tree
	 */
	void enterIdentifierExpression(JSVParser.IdentifierExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code IdentifierExpression}
	 * labeled alternative in {@link JSVParser#singleExpression}.
	 * @param ctx the parse tree
	 */
	void exitIdentifierExpression(JSVParser.IdentifierExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code BitAndExpression}
	 * labeled alternative in {@link JSVParser#singleExpression}.
	 * @param ctx the parse tree
	 */
	void enterBitAndExpression(JSVParser.BitAndExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code BitAndExpression}
	 * labeled alternative in {@link JSVParser#singleExpression}.
	 * @param ctx the parse tree
	 */
	void exitBitAndExpression(JSVParser.BitAndExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code BitOrExpression}
	 * labeled alternative in {@link JSVParser#singleExpression}.
	 * @param ctx the parse tree
	 */
	void enterBitOrExpression(JSVParser.BitOrExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code BitOrExpression}
	 * labeled alternative in {@link JSVParser#singleExpression}.
	 * @param ctx the parse tree
	 */
	void exitBitOrExpression(JSVParser.BitOrExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code AssignmentOperatorExpression}
	 * labeled alternative in {@link JSVParser#singleExpression}.
	 * @param ctx the parse tree
	 */
	void enterAssignmentOperatorExpression(JSVParser.AssignmentOperatorExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code AssignmentOperatorExpression}
	 * labeled alternative in {@link JSVParser#singleExpression}.
	 * @param ctx the parse tree
	 */
	void exitAssignmentOperatorExpression(JSVParser.AssignmentOperatorExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code VoidExpression}
	 * labeled alternative in {@link JSVParser#singleExpression}.
	 * @param ctx the parse tree
	 */
	void enterVoidExpression(JSVParser.VoidExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code VoidExpression}
	 * labeled alternative in {@link JSVParser#singleExpression}.
	 * @param ctx the parse tree
	 */
	void exitVoidExpression(JSVParser.VoidExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CoalesceExpression}
	 * labeled alternative in {@link JSVParser#singleExpression}.
	 * @param ctx the parse tree
	 */
	void enterCoalesceExpression(JSVParser.CoalesceExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CoalesceExpression}
	 * labeled alternative in {@link JSVParser#singleExpression}.
	 * @param ctx the parse tree
	 */
	void exitCoalesceExpression(JSVParser.CoalesceExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link JSVParser#assignable}.
	 * @param ctx the parse tree
	 */
	void enterAssignable(JSVParser.AssignableContext ctx);
	/**
	 * Exit a parse tree produced by {@link JSVParser#assignable}.
	 * @param ctx the parse tree
	 */
	void exitAssignable(JSVParser.AssignableContext ctx);
	/**
	 * Enter a parse tree produced by {@link JSVParser#objectLiteral}.
	 * @param ctx the parse tree
	 */
	void enterObjectLiteral(JSVParser.ObjectLiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link JSVParser#objectLiteral}.
	 * @param ctx the parse tree
	 */
	void exitObjectLiteral(JSVParser.ObjectLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link JSVParser#assignmentOperator}.
	 * @param ctx the parse tree
	 */
	void enterAssignmentOperator(JSVParser.AssignmentOperatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link JSVParser#assignmentOperator}.
	 * @param ctx the parse tree
	 */
	void exitAssignmentOperator(JSVParser.AssignmentOperatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link JSVParser#literal}.
	 * @param ctx the parse tree
	 */
	void enterLiteral(JSVParser.LiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link JSVParser#literal}.
	 * @param ctx the parse tree
	 */
	void exitLiteral(JSVParser.LiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link JSVParser#numericLiteral}.
	 * @param ctx the parse tree
	 */
	void enterNumericLiteral(JSVParser.NumericLiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link JSVParser#numericLiteral}.
	 * @param ctx the parse tree
	 */
	void exitNumericLiteral(JSVParser.NumericLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link JSVParser#bigintLiteral}.
	 * @param ctx the parse tree
	 */
	void enterBigintLiteral(JSVParser.BigintLiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link JSVParser#bigintLiteral}.
	 * @param ctx the parse tree
	 */
	void exitBigintLiteral(JSVParser.BigintLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link JSVParser#getter}.
	 * @param ctx the parse tree
	 */
	void enterGetter(JSVParser.GetterContext ctx);
	/**
	 * Exit a parse tree produced by {@link JSVParser#getter}.
	 * @param ctx the parse tree
	 */
	void exitGetter(JSVParser.GetterContext ctx);
	/**
	 * Enter a parse tree produced by {@link JSVParser#setter}.
	 * @param ctx the parse tree
	 */
	void enterSetter(JSVParser.SetterContext ctx);
	/**
	 * Exit a parse tree produced by {@link JSVParser#setter}.
	 * @param ctx the parse tree
	 */
	void exitSetter(JSVParser.SetterContext ctx);
	/**
	 * Enter a parse tree produced by {@link JSVParser#identifierName}.
	 * @param ctx the parse tree
	 */
	void enterIdentifierName(JSVParser.IdentifierNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link JSVParser#identifierName}.
	 * @param ctx the parse tree
	 */
	void exitIdentifierName(JSVParser.IdentifierNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link JSVParser#identifier}.
	 * @param ctx the parse tree
	 */
	void enterIdentifier(JSVParser.IdentifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link JSVParser#identifier}.
	 * @param ctx the parse tree
	 */
	void exitIdentifier(JSVParser.IdentifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link JSVParser#reservedWord}.
	 * @param ctx the parse tree
	 */
	void enterReservedWord(JSVParser.ReservedWordContext ctx);
	/**
	 * Exit a parse tree produced by {@link JSVParser#reservedWord}.
	 * @param ctx the parse tree
	 */
	void exitReservedWord(JSVParser.ReservedWordContext ctx);
	/**
	 * Enter a parse tree produced by {@link JSVParser#keyword}.
	 * @param ctx the parse tree
	 */
	void enterKeyword(JSVParser.KeywordContext ctx);
	/**
	 * Exit a parse tree produced by {@link JSVParser#keyword}.
	 * @param ctx the parse tree
	 */
	void exitKeyword(JSVParser.KeywordContext ctx);
	/**
	 * Enter a parse tree produced by {@link JSVParser#let}.
	 * @param ctx the parse tree
	 */
	void enterLet(JSVParser.LetContext ctx);
	/**
	 * Exit a parse tree produced by {@link JSVParser#let}.
	 * @param ctx the parse tree
	 */
	void exitLet(JSVParser.LetContext ctx);
	/**
	 * Enter a parse tree produced by {@link JSVParser#eos}.
	 * @param ctx the parse tree
	 */
	void enterEos(JSVParser.EosContext ctx);
	/**
	 * Exit a parse tree produced by {@link JSVParser#eos}.
	 * @param ctx the parse tree
	 */
	void exitEos(JSVParser.EosContext ctx);
}