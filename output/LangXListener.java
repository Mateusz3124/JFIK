// Generated from LangX.g4 by ANTLR 4.13.2
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link LangXParser}.
 */
public interface LangXListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link LangXParser#prog}.
	 * @param ctx the parse tree
	 */
	void enterProg(LangXParser.ProgContext ctx);
	/**
	 * Exit a parse tree produced by {@link LangXParser#prog}.
	 * @param ctx the parse tree
	 */
	void exitProg(LangXParser.ProgContext ctx);
	/**
	 * Enter a parse tree produced by {@link LangXParser#struct}.
	 * @param ctx the parse tree
	 */
	void enterStruct(LangXParser.StructContext ctx);
	/**
	 * Exit a parse tree produced by {@link LangXParser#struct}.
	 * @param ctx the parse tree
	 */
	void exitStruct(LangXParser.StructContext ctx);
	/**
	 * Enter a parse tree produced by {@link LangXParser#structBlock}.
	 * @param ctx the parse tree
	 */
	void enterStructBlock(LangXParser.StructBlockContext ctx);
	/**
	 * Exit a parse tree produced by {@link LangXParser#structBlock}.
	 * @param ctx the parse tree
	 */
	void exitStructBlock(LangXParser.StructBlockContext ctx);
	/**
	 * Enter a parse tree produced by {@link LangXParser#structVal}.
	 * @param ctx the parse tree
	 */
	void enterStructVal(LangXParser.StructValContext ctx);
	/**
	 * Exit a parse tree produced by {@link LangXParser#structVal}.
	 * @param ctx the parse tree
	 */
	void exitStructVal(LangXParser.StructValContext ctx);
	/**
	 * Enter a parse tree produced by {@link LangXParser#function}.
	 * @param ctx the parse tree
	 */
	void enterFunction(LangXParser.FunctionContext ctx);
	/**
	 * Exit a parse tree produced by {@link LangXParser#function}.
	 * @param ctx the parse tree
	 */
	void exitFunction(LangXParser.FunctionContext ctx);
	/**
	 * Enter a parse tree produced by {@link LangXParser#fparam}.
	 * @param ctx the parse tree
	 */
	void enterFparam(LangXParser.FparamContext ctx);
	/**
	 * Exit a parse tree produced by {@link LangXParser#fparam}.
	 * @param ctx the parse tree
	 */
	void exitFparam(LangXParser.FparamContext ctx);
	/**
	 * Enter a parse tree produced by {@link LangXParser#fblock}.
	 * @param ctx the parse tree
	 */
	void enterFblock(LangXParser.FblockContext ctx);
	/**
	 * Exit a parse tree produced by {@link LangXParser#fblock}.
	 * @param ctx the parse tree
	 */
	void exitFblock(LangXParser.FblockContext ctx);
	/**
	 * Enter a parse tree produced by {@link LangXParser#return}.
	 * @param ctx the parse tree
	 */
	void enterReturn(LangXParser.ReturnContext ctx);
	/**
	 * Exit a parse tree produced by {@link LangXParser#return}.
	 * @param ctx the parse tree
	 */
	void exitReturn(LangXParser.ReturnContext ctx);
	/**
	 * Enter a parse tree produced by the {@code assignType}
	 * labeled alternative in {@link LangXParser#stat}.
	 * @param ctx the parse tree
	 */
	void enterAssignType(LangXParser.AssignTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code assignType}
	 * labeled alternative in {@link LangXParser#stat}.
	 * @param ctx the parse tree
	 */
	void exitAssignType(LangXParser.AssignTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code assignStructKey}
	 * labeled alternative in {@link LangXParser#stat}.
	 * @param ctx the parse tree
	 */
	void enterAssignStructKey(LangXParser.AssignStructKeyContext ctx);
	/**
	 * Exit a parse tree produced by the {@code assignStructKey}
	 * labeled alternative in {@link LangXParser#stat}.
	 * @param ctx the parse tree
	 */
	void exitAssignStructKey(LangXParser.AssignStructKeyContext ctx);
	/**
	 * Enter a parse tree produced by the {@code assignStruct}
	 * labeled alternative in {@link LangXParser#stat}.
	 * @param ctx the parse tree
	 */
	void enterAssignStruct(LangXParser.AssignStructContext ctx);
	/**
	 * Exit a parse tree produced by the {@code assignStruct}
	 * labeled alternative in {@link LangXParser#stat}.
	 * @param ctx the parse tree
	 */
	void exitAssignStruct(LangXParser.AssignStructContext ctx);
	/**
	 * Enter a parse tree produced by the {@code assignTypedStruct}
	 * labeled alternative in {@link LangXParser#stat}.
	 * @param ctx the parse tree
	 */
	void enterAssignTypedStruct(LangXParser.AssignTypedStructContext ctx);
	/**
	 * Exit a parse tree produced by the {@code assignTypedStruct}
	 * labeled alternative in {@link LangXParser#stat}.
	 * @param ctx the parse tree
	 */
	void exitAssignTypedStruct(LangXParser.AssignTypedStructContext ctx);
	/**
	 * Enter a parse tree produced by the {@code assignTyped}
	 * labeled alternative in {@link LangXParser#stat}.
	 * @param ctx the parse tree
	 */
	void enterAssignTyped(LangXParser.AssignTypedContext ctx);
	/**
	 * Exit a parse tree produced by the {@code assignTyped}
	 * labeled alternative in {@link LangXParser#stat}.
	 * @param ctx the parse tree
	 */
	void exitAssignTyped(LangXParser.AssignTypedContext ctx);
	/**
	 * Enter a parse tree produced by the {@code assignTypeGlobal}
	 * labeled alternative in {@link LangXParser#stat}.
	 * @param ctx the parse tree
	 */
	void enterAssignTypeGlobal(LangXParser.AssignTypeGlobalContext ctx);
	/**
	 * Exit a parse tree produced by the {@code assignTypeGlobal}
	 * labeled alternative in {@link LangXParser#stat}.
	 * @param ctx the parse tree
	 */
	void exitAssignTypeGlobal(LangXParser.AssignTypeGlobalContext ctx);
	/**
	 * Enter a parse tree produced by the {@code assignTypedGlobal}
	 * labeled alternative in {@link LangXParser#stat}.
	 * @param ctx the parse tree
	 */
	void enterAssignTypedGlobal(LangXParser.AssignTypedGlobalContext ctx);
	/**
	 * Exit a parse tree produced by the {@code assignTypedGlobal}
	 * labeled alternative in {@link LangXParser#stat}.
	 * @param ctx the parse tree
	 */
	void exitAssignTypedGlobal(LangXParser.AssignTypedGlobalContext ctx);
	/**
	 * Enter a parse tree produced by the {@code assign}
	 * labeled alternative in {@link LangXParser#stat}.
	 * @param ctx the parse tree
	 */
	void enterAssign(LangXParser.AssignContext ctx);
	/**
	 * Exit a parse tree produced by the {@code assign}
	 * labeled alternative in {@link LangXParser#stat}.
	 * @param ctx the parse tree
	 */
	void exitAssign(LangXParser.AssignContext ctx);
	/**
	 * Enter a parse tree produced by the {@code print}
	 * labeled alternative in {@link LangXParser#stat}.
	 * @param ctx the parse tree
	 */
	void enterPrint(LangXParser.PrintContext ctx);
	/**
	 * Exit a parse tree produced by the {@code print}
	 * labeled alternative in {@link LangXParser#stat}.
	 * @param ctx the parse tree
	 */
	void exitPrint(LangXParser.PrintContext ctx);
	/**
	 * Enter a parse tree produced by the {@code read}
	 * labeled alternative in {@link LangXParser#stat}.
	 * @param ctx the parse tree
	 */
	void enterRead(LangXParser.ReadContext ctx);
	/**
	 * Exit a parse tree produced by the {@code read}
	 * labeled alternative in {@link LangXParser#stat}.
	 * @param ctx the parse tree
	 */
	void exitRead(LangXParser.ReadContext ctx);
	/**
	 * Enter a parse tree produced by the {@code callSingle}
	 * labeled alternative in {@link LangXParser#stat}.
	 * @param ctx the parse tree
	 */
	void enterCallSingle(LangXParser.CallSingleContext ctx);
	/**
	 * Exit a parse tree produced by the {@code callSingle}
	 * labeled alternative in {@link LangXParser#stat}.
	 * @param ctx the parse tree
	 */
	void exitCallSingle(LangXParser.CallSingleContext ctx);
	/**
	 * Enter a parse tree produced by the {@code if}
	 * labeled alternative in {@link LangXParser#stat}.
	 * @param ctx the parse tree
	 */
	void enterIf(LangXParser.IfContext ctx);
	/**
	 * Exit a parse tree produced by the {@code if}
	 * labeled alternative in {@link LangXParser#stat}.
	 * @param ctx the parse tree
	 */
	void exitIf(LangXParser.IfContext ctx);
	/**
	 * Enter a parse tree produced by the {@code for}
	 * labeled alternative in {@link LangXParser#stat}.
	 * @param ctx the parse tree
	 */
	void enterFor(LangXParser.ForContext ctx);
	/**
	 * Exit a parse tree produced by the {@code for}
	 * labeled alternative in {@link LangXParser#stat}.
	 * @param ctx the parse tree
	 */
	void exitFor(LangXParser.ForContext ctx);
	/**
	 * Enter a parse tree produced by {@link LangXParser#blockif}.
	 * @param ctx the parse tree
	 */
	void enterBlockif(LangXParser.BlockifContext ctx);
	/**
	 * Exit a parse tree produced by {@link LangXParser#blockif}.
	 * @param ctx the parse tree
	 */
	void exitBlockif(LangXParser.BlockifContext ctx);
	/**
	 * Enter a parse tree produced by {@link LangXParser#blockfor}.
	 * @param ctx the parse tree
	 */
	void enterBlockfor(LangXParser.BlockforContext ctx);
	/**
	 * Exit a parse tree produced by {@link LangXParser#blockfor}.
	 * @param ctx the parse tree
	 */
	void exitBlockfor(LangXParser.BlockforContext ctx);
	/**
	 * Enter a parse tree produced by {@link LangXParser#equal}.
	 * @param ctx the parse tree
	 */
	void enterEqual(LangXParser.EqualContext ctx);
	/**
	 * Exit a parse tree produced by {@link LangXParser#equal}.
	 * @param ctx the parse tree
	 */
	void exitEqual(LangXParser.EqualContext ctx);
	/**
	 * Enter a parse tree produced by the {@code single0}
	 * labeled alternative in {@link LangXParser#expr0}.
	 * @param ctx the parse tree
	 */
	void enterSingle0(LangXParser.Single0Context ctx);
	/**
	 * Exit a parse tree produced by the {@code single0}
	 * labeled alternative in {@link LangXParser#expr0}.
	 * @param ctx the parse tree
	 */
	void exitSingle0(LangXParser.Single0Context ctx);
	/**
	 * Enter a parse tree produced by the {@code add}
	 * labeled alternative in {@link LangXParser#expr0}.
	 * @param ctx the parse tree
	 */
	void enterAdd(LangXParser.AddContext ctx);
	/**
	 * Exit a parse tree produced by the {@code add}
	 * labeled alternative in {@link LangXParser#expr0}.
	 * @param ctx the parse tree
	 */
	void exitAdd(LangXParser.AddContext ctx);
	/**
	 * Enter a parse tree produced by the {@code single1}
	 * labeled alternative in {@link LangXParser#expr1}.
	 * @param ctx the parse tree
	 */
	void enterSingle1(LangXParser.Single1Context ctx);
	/**
	 * Exit a parse tree produced by the {@code single1}
	 * labeled alternative in {@link LangXParser#expr1}.
	 * @param ctx the parse tree
	 */
	void exitSingle1(LangXParser.Single1Context ctx);
	/**
	 * Enter a parse tree produced by the {@code sub}
	 * labeled alternative in {@link LangXParser#expr1}.
	 * @param ctx the parse tree
	 */
	void enterSub(LangXParser.SubContext ctx);
	/**
	 * Exit a parse tree produced by the {@code sub}
	 * labeled alternative in {@link LangXParser#expr1}.
	 * @param ctx the parse tree
	 */
	void exitSub(LangXParser.SubContext ctx);
	/**
	 * Enter a parse tree produced by the {@code single2}
	 * labeled alternative in {@link LangXParser#expr2}.
	 * @param ctx the parse tree
	 */
	void enterSingle2(LangXParser.Single2Context ctx);
	/**
	 * Exit a parse tree produced by the {@code single2}
	 * labeled alternative in {@link LangXParser#expr2}.
	 * @param ctx the parse tree
	 */
	void exitSingle2(LangXParser.Single2Context ctx);
	/**
	 * Enter a parse tree produced by the {@code mult}
	 * labeled alternative in {@link LangXParser#expr2}.
	 * @param ctx the parse tree
	 */
	void enterMult(LangXParser.MultContext ctx);
	/**
	 * Exit a parse tree produced by the {@code mult}
	 * labeled alternative in {@link LangXParser#expr2}.
	 * @param ctx the parse tree
	 */
	void exitMult(LangXParser.MultContext ctx);
	/**
	 * Enter a parse tree produced by the {@code div}
	 * labeled alternative in {@link LangXParser#expr3}.
	 * @param ctx the parse tree
	 */
	void enterDiv(LangXParser.DivContext ctx);
	/**
	 * Exit a parse tree produced by the {@code div}
	 * labeled alternative in {@link LangXParser#expr3}.
	 * @param ctx the parse tree
	 */
	void exitDiv(LangXParser.DivContext ctx);
	/**
	 * Enter a parse tree produced by the {@code single3}
	 * labeled alternative in {@link LangXParser#expr3}.
	 * @param ctx the parse tree
	 */
	void enterSingle3(LangXParser.Single3Context ctx);
	/**
	 * Exit a parse tree produced by the {@code single3}
	 * labeled alternative in {@link LangXParser#expr3}.
	 * @param ctx the parse tree
	 */
	void exitSingle3(LangXParser.Single3Context ctx);
	/**
	 * Enter a parse tree produced by the {@code int}
	 * labeled alternative in {@link LangXParser#expr4}.
	 * @param ctx the parse tree
	 */
	void enterInt(LangXParser.IntContext ctx);
	/**
	 * Exit a parse tree produced by the {@code int}
	 * labeled alternative in {@link LangXParser#expr4}.
	 * @param ctx the parse tree
	 */
	void exitInt(LangXParser.IntContext ctx);
	/**
	 * Enter a parse tree produced by the {@code int64}
	 * labeled alternative in {@link LangXParser#expr4}.
	 * @param ctx the parse tree
	 */
	void enterInt64(LangXParser.Int64Context ctx);
	/**
	 * Exit a parse tree produced by the {@code int64}
	 * labeled alternative in {@link LangXParser#expr4}.
	 * @param ctx the parse tree
	 */
	void exitInt64(LangXParser.Int64Context ctx);
	/**
	 * Enter a parse tree produced by the {@code real}
	 * labeled alternative in {@link LangXParser#expr4}.
	 * @param ctx the parse tree
	 */
	void enterReal(LangXParser.RealContext ctx);
	/**
	 * Exit a parse tree produced by the {@code real}
	 * labeled alternative in {@link LangXParser#expr4}.
	 * @param ctx the parse tree
	 */
	void exitReal(LangXParser.RealContext ctx);
	/**
	 * Enter a parse tree produced by the {@code float32}
	 * labeled alternative in {@link LangXParser#expr4}.
	 * @param ctx the parse tree
	 */
	void enterFloat32(LangXParser.Float32Context ctx);
	/**
	 * Exit a parse tree produced by the {@code float32}
	 * labeled alternative in {@link LangXParser#expr4}.
	 * @param ctx the parse tree
	 */
	void exitFloat32(LangXParser.Float32Context ctx);
	/**
	 * Enter a parse tree produced by the {@code float64}
	 * labeled alternative in {@link LangXParser#expr4}.
	 * @param ctx the parse tree
	 */
	void enterFloat64(LangXParser.Float64Context ctx);
	/**
	 * Exit a parse tree produced by the {@code float64}
	 * labeled alternative in {@link LangXParser#expr4}.
	 * @param ctx the parse tree
	 */
	void exitFloat64(LangXParser.Float64Context ctx);
	/**
	 * Enter a parse tree produced by the {@code valueof}
	 * labeled alternative in {@link LangXParser#expr4}.
	 * @param ctx the parse tree
	 */
	void enterValueof(LangXParser.ValueofContext ctx);
	/**
	 * Exit a parse tree produced by the {@code valueof}
	 * labeled alternative in {@link LangXParser#expr4}.
	 * @param ctx the parse tree
	 */
	void exitValueof(LangXParser.ValueofContext ctx);
	/**
	 * Enter a parse tree produced by the {@code toint}
	 * labeled alternative in {@link LangXParser#expr4}.
	 * @param ctx the parse tree
	 */
	void enterToint(LangXParser.TointContext ctx);
	/**
	 * Exit a parse tree produced by the {@code toint}
	 * labeled alternative in {@link LangXParser#expr4}.
	 * @param ctx the parse tree
	 */
	void exitToint(LangXParser.TointContext ctx);
	/**
	 * Enter a parse tree produced by the {@code toint64}
	 * labeled alternative in {@link LangXParser#expr4}.
	 * @param ctx the parse tree
	 */
	void enterToint64(LangXParser.Toint64Context ctx);
	/**
	 * Exit a parse tree produced by the {@code toint64}
	 * labeled alternative in {@link LangXParser#expr4}.
	 * @param ctx the parse tree
	 */
	void exitToint64(LangXParser.Toint64Context ctx);
	/**
	 * Enter a parse tree produced by the {@code toreal}
	 * labeled alternative in {@link LangXParser#expr4}.
	 * @param ctx the parse tree
	 */
	void enterToreal(LangXParser.TorealContext ctx);
	/**
	 * Exit a parse tree produced by the {@code toreal}
	 * labeled alternative in {@link LangXParser#expr4}.
	 * @param ctx the parse tree
	 */
	void exitToreal(LangXParser.TorealContext ctx);
	/**
	 * Enter a parse tree produced by the {@code tofloat32}
	 * labeled alternative in {@link LangXParser#expr4}.
	 * @param ctx the parse tree
	 */
	void enterTofloat32(LangXParser.Tofloat32Context ctx);
	/**
	 * Exit a parse tree produced by the {@code tofloat32}
	 * labeled alternative in {@link LangXParser#expr4}.
	 * @param ctx the parse tree
	 */
	void exitTofloat32(LangXParser.Tofloat32Context ctx);
	/**
	 * Enter a parse tree produced by the {@code tofloat64}
	 * labeled alternative in {@link LangXParser#expr4}.
	 * @param ctx the parse tree
	 */
	void enterTofloat64(LangXParser.Tofloat64Context ctx);
	/**
	 * Exit a parse tree produced by the {@code tofloat64}
	 * labeled alternative in {@link LangXParser#expr4}.
	 * @param ctx the parse tree
	 */
	void exitTofloat64(LangXParser.Tofloat64Context ctx);
	/**
	 * Enter a parse tree produced by the {@code par}
	 * labeled alternative in {@link LangXParser#expr4}.
	 * @param ctx the parse tree
	 */
	void enterPar(LangXParser.ParContext ctx);
	/**
	 * Exit a parse tree produced by the {@code par}
	 * labeled alternative in {@link LangXParser#expr4}.
	 * @param ctx the parse tree
	 */
	void exitPar(LangXParser.ParContext ctx);
	/**
	 * Enter a parse tree produced by the {@code call}
	 * labeled alternative in {@link LangXParser#expr4}.
	 * @param ctx the parse tree
	 */
	void enterCall(LangXParser.CallContext ctx);
	/**
	 * Exit a parse tree produced by the {@code call}
	 * labeled alternative in {@link LangXParser#expr4}.
	 * @param ctx the parse tree
	 */
	void exitCall(LangXParser.CallContext ctx);
	/**
	 * Enter a parse tree produced by the {@code idStat}
	 * labeled alternative in {@link LangXParser#valueOfID}.
	 * @param ctx the parse tree
	 */
	void enterIdStat(LangXParser.IdStatContext ctx);
	/**
	 * Exit a parse tree produced by the {@code idStat}
	 * labeled alternative in {@link LangXParser#valueOfID}.
	 * @param ctx the parse tree
	 */
	void exitIdStat(LangXParser.IdStatContext ctx);
	/**
	 * Enter a parse tree produced by the {@code structValue}
	 * labeled alternative in {@link LangXParser#valueOfID}.
	 * @param ctx the parse tree
	 */
	void enterStructValue(LangXParser.StructValueContext ctx);
	/**
	 * Exit a parse tree produced by the {@code structValue}
	 * labeled alternative in {@link LangXParser#valueOfID}.
	 * @param ctx the parse tree
	 */
	void exitStructValue(LangXParser.StructValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link LangXParser#type}.
	 * @param ctx the parse tree
	 */
	void enterType(LangXParser.TypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link LangXParser#type}.
	 * @param ctx the parse tree
	 */
	void exitType(LangXParser.TypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link LangXParser#funcType}.
	 * @param ctx the parse tree
	 */
	void enterFuncType(LangXParser.FuncTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link LangXParser#funcType}.
	 * @param ctx the parse tree
	 */
	void exitFuncType(LangXParser.FuncTypeContext ctx);
}