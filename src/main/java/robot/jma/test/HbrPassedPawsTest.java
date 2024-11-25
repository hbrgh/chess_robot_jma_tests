package robot.jma.test;



import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;



import static com.github.bhlangonijr.chesslib.Square.*;

import com.fathzer.games.MoveGenerator.MoveConfidence;
import com.fathzer.games.ai.evaluation.EvaluatedMove;
import com.fathzer.games.ai.evaluation.Evaluator;
import com.fathzer.games.ai.iterativedeepening.IterativeDeepeningEngine;
import com.fathzer.games.util.SelectiveComparator;
import com.fathzer.jchess.chesslib.ChessLibMoveGenerator;
import com.fathzer.jchess.chesslib.ai.BasicMoveComparator;
import com.fathzer.jchess.chesslib.ai.eval.hbpg2.Hb2MyFirstEvaluator;
import com.fathzer.jchess.chesslib.ai.eval.hbpg2.Hb2SimplifiedEvaluator;
import com.fathzer.jchess.chesslib.uci.ChessLibEngine;
import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.move.Move;

class HbrPassedPawsTest {
	private static final String MAGIC_FEN = "8/3k3p/2R5/3PP3/8/1p2K3/7r/8 w - - 0 1";
	
	private static final Supplier<Evaluator<Move, ChessLibMoveGenerator>> STATIC_EVAL_BUILDER = Hb2MyFirstEvaluator::new;
	private static final Supplier<Evaluator<Move, ChessLibMoveGenerator>> DYNAMIC_EVAL_BUILDER = Hb2SimplifiedEvaluator::new;
	
	private static class ChattyEvaluator implements Evaluator<Move, ChessLibMoveGenerator> {
		private Evaluator<Move, ChessLibMoveGenerator> internal;

		private ChattyEvaluator(Evaluator<Move, ChessLibMoveGenerator> internal) {
			super();
			this.internal = internal;
		}

		@Override
		public Evaluator<Move, ChessLibMoveGenerator> fork() {
			System.out.println(this+" forked");
			var result = new ChattyEvaluator(internal);
			this.internal = internal.fork();
			return result;
		}

		@Override
		public void init(ChessLibMoveGenerator board) {
			System.out.println(this+" inited with "+board);
			internal.init(board);
		}

		@Override
		public void prepareMove(ChessLibMoveGenerator board, Move move) {
			System.out.println(this+" prepareMove "+move);
			internal.prepareMove(board, move);
		}

		@Override
		public void commitMove() {
			System.out.println(this+" commitMove");
			internal.commitMove();
		}

		@Override
		public void unmakeMove() {
			System.out.println(this+" unmakeMove");
			internal.unmakeMove();
		}

		@Override
		public int evaluate(ChessLibMoveGenerator board) {
			final int result = internal.evaluate(board);
			System.out.println(this+" evaluate "+board+" to "+result);
			return result;
		}
		
	}
	
	static ChessLibMoveGenerator fromFEN(String fen) {
		final Board board = new Board();
		board.loadFromFen(fen);
		return new ChessLibMoveGenerator(board);
	}
	
	static ChessLibMoveGenerator fromFEN(String fen, Function<ChessLibMoveGenerator, SelectiveComparator<Move>> moveComparatorBuilder) {
		final ChessLibMoveGenerator mv = fromFEN(fen);
		mv.setMoveComparatorBuilder(moveComparatorBuilder);
		return mv;
	}
	

	
	private EvaluatedMove<Move> getBest(String fen, List<Move> candidates, Supplier<Evaluator<Move, ChessLibMoveGenerator>> evaluatorBuilder) {
		final int depth = 1;
		final int bestMoveCount = 1;
		final IterativeDeepeningEngine<Move, ChessLibMoveGenerator> engine = ChessLibEngine.buildEngine(evaluatorBuilder, depth);
		engine.getDeepeningPolicy().setSize(bestMoveCount);
		engine.getDeepeningPolicy().setDeepenOnForced(true);
		final List<EvaluatedMove<Move>> moves = engine.getBestMoves(fromFEN(fen, BasicMoveComparator::new), candidates).getBestMoves();
		System.out.println(moves);
		for (EvaluatedMove<Move> move : moves) {
			List<Move> principalVariation = move.getPrincipalVariation();
			System.out.println(move+" -> "+principalVariation);
		}
		return moves.get(0);
	}
	
	


	
	static private int getStaticEval(ChessLibMoveGenerator board, Supplier<Evaluator<Move, ChessLibMoveGenerator>> evaluatorBuilder) {
		Evaluator<Move, ChessLibMoveGenerator> stat = evaluatorBuilder.get();
		stat.init(board);
		return stat.evaluate(board);
	}
	
	public static void main(String[] args) {
		
		// check if dynamic evaluator gives the right evaluation after the first move 
		ChessLibMoveGenerator board = fromFEN(MAGIC_FEN);
		Evaluator<Move, ChessLibMoveGenerator> dynamic = DYNAMIC_EVAL_BUILDER.get();
		dynamic.init(board);
//		int staticEvalPosDepartWithStaticEvaluator = getStaticEval(board, STATIC_EVAL_BUILDER);
//		int evalPosDepartWithDynamicEvaluator = dynamic.evaluate(board);
//		
//		if (staticEvalPosDepartWithStaticEvaluator != evalPosDepartWithDynamicEvaluator) {
//			System.out.println("KATASTROIKA");
//		}
		
		final Move move = new Move(E5,E6);
		dynamic.prepareMove(board, move);
		board.makeMove(move, MoveConfidence.LEGAL);
		dynamic.commitMove();
		
		int staticEvalPosAfterE5E6tWithStaticEvaluator = getStaticEval(board, STATIC_EVAL_BUILDER);
		int evalPosAfterE5E6tWithDynamicEvaluator = dynamic.evaluate(board);
		
		if (staticEvalPosAfterE5E6tWithStaticEvaluator != evalPosAfterE5E6tWithDynamicEvaluator) {
			System.out.println("KATASTROIKA_AFTER_E5E6");
		}
		
	}
}
