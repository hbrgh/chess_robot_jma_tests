package robot.jma.eval;

import java.util.function.Supplier;

import com.fathzer.games.ai.evaluation.Evaluator;
import com.fathzer.jchess.chesslib.ChessLibMoveGenerator;
import com.fathzer.jchess.chesslib.ai.eval.hb.HbPestoEvaluator;
import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.move.Move;

public class EvalPostionFen2 {

	private static final String MAGIC_FEN = "8/3k3p/2R5/3PP3/8/1p2K3/7r/8 w - - 0 1";

	private static final Supplier<Evaluator<Move, ChessLibMoveGenerator>> STATIC_EVAL_BUILDER = HbPestoEvaluator::new;

	static private int getStaticEval(ChessLibMoveGenerator board,
			Supplier<Evaluator<Move, ChessLibMoveGenerator>> evaluatorBuilder) {
		Evaluator<Move, ChessLibMoveGenerator> stat = evaluatorBuilder.get();
		stat.init(board);
		return stat.evaluate(board);
	}

	static ChessLibMoveGenerator fromFEN(String fen) {
		final Board board = new Board();
		board.loadFromFen(fen);
		return new ChessLibMoveGenerator(board);
	}

	public static void evalPostWithDetails(String fen) {
		ChessLibMoveGenerator board = fromFEN(MAGIC_FEN);
		
		System.out.println(EvalDisplayUtils.toStringHb(board.getBoard()));
		int scoreEvalPosStatic = getStaticEval(board, STATIC_EVAL_BUILDER);
		System.out.println("scoreEvalPosStatic = " + scoreEvalPosStatic);
	}

	public static void main(String[] args) {
	
		evalPostWithDetails(MAGIC_FEN);
	

	}

}
