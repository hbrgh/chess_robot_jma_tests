package robot.jma.test;

import java.util.function.Function;

import com.fathzer.games.util.SelectiveComparator;
import com.fathzer.jchess.chesslib.ChessLibMoveGenerator;
import com.fathzer.jchess.chesslib.ai.eval.hbpg2.Hb2MyFirstEvaluator;
import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.move.Move;

public class TestPassedPawnsStaticPositionKingToE8afterE6 {
	
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
		
	
		
		

//		final String fen = "8/3k3p/2R5/3P4/1p2P3/4K3/7r/8 w - - 0 1";
		final String fenAfterE5E6_Kd7e8 = "4k3/7p/2R1P3/3P4/8/1p2K3/7r/8 w - - 1 2";
		Board internal = new Board();
		internal.loadFromFen("8/3k3p/2R5/3PP3/8/1p2K3/7r/8 w - - 0 1"); 
		System.out.println(internal.toStringFromWhiteViewPoint());
		
		Hb2MyFirstEvaluator.evaluatePositionAsWhite(fenAfterE5E6_Kd7e8);
		
//		final String fen = "r2k1r2/pp1b2pp/1b2Pn2/2p5/Q1B2Bq1/2P5/P5PP/3R1RK1 w - - 0 1"; // pas de pions doublés
//		final String fen = "5k2/p1p2ppp/4b3/4p1P1/1P2P3/p2P1B2/1b2NP1P/6K1 w - - 0 1";
	


	}

}
