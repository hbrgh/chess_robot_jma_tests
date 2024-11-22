package robot.jma.test;

import java.util.List;
import java.util.function.Function;

import com.fathzer.games.ai.evaluation.EvaluatedMove;
import com.fathzer.games.ai.iterativedeepening.IterativeDeepeningEngine;
import com.fathzer.games.util.SelectiveComparator;
import com.fathzer.jchess.chesslib.ChessLibMoveGenerator;
import com.fathzer.jchess.chesslib.ai.BasicMoveComparator;
import com.fathzer.jchess.chesslib.ai.eval.hbpg2.Hb2SimplifiedEvaluator;
import com.fathzer.jchess.chesslib.uci.ChessLibEngine;
import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.move.Move;

public class TestPassedPawnsDepth1 {
	
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

	public static void main(String[] args) {
		
	
		
		
		
		final int depth = 1;
		final int bestMoveCount = 3;

		final String fen = "8/3k3p/2R5/3PP3/8/1p2K3/7r/8 w - - 0 1";
		Board internal = new Board();
		internal.loadFromFen(fen); 
		System.out.println(internal.toStringFromWhiteViewPoint());
		


		final IterativeDeepeningEngine<Move, ChessLibMoveGenerator> engine = ChessLibEngine.buildEngine(Hb2SimplifiedEvaluator::new, depth);
		engine.getDeepeningPolicy().setSize(bestMoveCount);
		engine.getDeepeningPolicy().setDeepenOnForced(true);
		final List<EvaluatedMove<Move>> moves = engine.getBestMoves(fromFEN(fen, BasicMoveComparator::new)).getBestMoves();
		System.out.println(moves);
		for (EvaluatedMove<Move> move : moves) {

			List<Move> principalVariation = move.getPrincipalVariation();
			System.out.println(move+" -> "+principalVariation);
		}

	}

}
