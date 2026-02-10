package robot.jma.test;



import static com.github.bhlangonijr.chesslib.Square.F3;
import static com.github.bhlangonijr.chesslib.Square.F4;
import static com.github.bhlangonijr.chesslib.Square.G7;
import static com.github.bhlangonijr.chesslib.Square.H6;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

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



class TestRookVersusTwoPawnsEndgame {
	private static final String MAGIC_FEN = "R7/1K4k1/8/6p1/5p2/8/8/8 b - - 0 1";
	
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
	

	
//	static private EvaluatedMove<Move> getBest(String fen, List<Move> candidates, Supplier<Evaluator<Move, ChessLibMoveGenerator>> evaluatorBuilder) {
//		final int depth = 14;
//		final int bestMoveCount = 3;
//		final IterativeDeepeningEngine<Move, ChessLibMoveGenerator> engine = ChessLibEngine.buildEngine(evaluatorBuilder, depth);
//		engine.getDeepeningPolicy().setSize(bestMoveCount);
//		engine.getDeepeningPolicy().setDeepenOnForced(true);
//		final List<EvaluatedMove<Move>> moves = engine.getBestMoves(fromFEN(fen, BasicMoveComparator::new), candidates).getBestMoves();
//		System.out.println(moves);
//		for (EvaluatedMove<Move> move : moves) {
//			List<Move> principalVariation = move.getPrincipalVariation();
//			System.out.println(move+" -> "+principalVariation);
//		}
//		return moves.get(0);
//	}
	
	static private EvaluatedMove<Move> getBest(String fen, List<Move> candidates, Supplier<Evaluator<Move, ChessLibMoveGenerator>> evaluatorBuilder) {
		final int depth = 1;
		final int bestMoveCount = 1;
		final IterativeDeepeningEngine<Move, ChessLibMoveGenerator> engine = ChessLibEngine.buildEngine(evaluatorBuilder, depth);
		engine.getDeepeningPolicy().setSize(bestMoveCount);
		engine.getDeepeningPolicy().setDeepenOnForced(true);
		final ChessLibMoveGenerator board =fromFEN(fen, BasicMoveComparator::new);
		final List<EvaluatedMove<Move>> moves = engine.getBestMoves(board).getAccurateMoves();
//		final List<EvaluatedMove<Move>> moves = engine.getBestMoves(fromFEN(fen, BasicMoveComparator::new), candidates).getBestMoves();
		System.out.println(moves);
		for (EvaluatedMove<Move> move : moves) {
//			List<Move> principalVariation = move.getPrincipalVariation();
			List<Move> principalVariation = engine.getTranspositionTable().collectPV(board, move.getMove(), depth);
			System.out.println(move+" -> "+principalVariation);
		}
		return moves.get(0);
	}
	
	
	


	
	static private int getStaticEval(ChessLibMoveGenerator board, Supplier<Evaluator<Move, ChessLibMoveGenerator>> evaluatorBuilder) {
		Evaluator<Move, ChessLibMoveGenerator> stat = evaluatorBuilder.get();
		stat.init(board);
		return stat.evaluate(board);
	}
	
	static void testWithBestMoveSearch() {
		List<Move> candidates = Collections.singletonList(new Move(F4,F3));
		EvaluatedMove<Move> move = getBest(MAGIC_FEN, candidates, () -> new ChattyEvaluator(STATIC_EVAL_BUILDER.get()));
		// Test with static evaluator
		System.out.println("moveScoreStaticEvaluator+-> "+move.getScore());
		
		System.out.println("=====================================");
		
		List<Move> candidatesDyn = Collections.singletonList(new Move(F4,F3));
		EvaluatedMove<Move> moveDyn = getBest(MAGIC_FEN, candidatesDyn, () -> new ChattyEvaluator(DYNAMIC_EVAL_BUILDER.get()));
		// Test with static evaluator
		System.out.println("moveScoreDynamicvaluator+-> "+moveDyn.getScore());
		
		
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
		
//		final Move move = new Move(F4,F3);
		final Move move = new Move(G7,H6);
		dynamic.prepareMove(board, move);
		board.makeMove(move, MoveConfidence.LEGAL);
		dynamic.commitMove();
		
		int staticEvalPosAfterF4F3tWithStaticEvaluator = getStaticEval(board, STATIC_EVAL_BUILDER);
		int evalPosAfterE5E6tWithDynamicEvaluator = dynamic.evaluate(board);
		
		if (staticEvalPosAfterF4F3tWithStaticEvaluator != evalPosAfterE5E6tWithDynamicEvaluator) {
			System.out.println("KATASTROIKA_AFTER_F4F3");
		}
		
		testWithBestMoveSearch();
		
	}
}
