package chess_robot_jma_tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.fathzer.jchess.chesslib.ChessLibMoveGenerator;
import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.Side;
import com.github.bhlangonijr.chesslib.move.Move;

class LegalMovesBothSidesTest {

	private static final String MAGIC_FEN_INIT = "8/3k3p/2R5/3PP3/8/1p2K3/7r/8 w - - 0 1";
	private static final String MAGIC_FEN_AFTER_E5E6 = "8/3k3p/2R1P3/3P4/8/1p2K3/7r/8 b - - 0 1";

	static ChessLibMoveGenerator fromFEN(String fen) {
		final Board board = new Board();
		board.loadFromFen(fen);
		return new ChessLibMoveGenerator(board);
	}

	static boolean areLegalMovesBelongingToSideToMove(Board board) {
		List<Move> lstMov = board.legalMoves();
		for (Move mov : lstMov) {
			if (board.getPiece(mov.getFrom()).getPieceSide() != board.getSideToMove()) {
				return (false);
			}
		}
		return (true);
	}

	static List<Move> getListeWhiteLegalMoves(Board board) {
		if (board.getSideToMove() == Side.WHITE) {
			return (board.legalMoves());
		} else {
			if (board.doNullMove()) {

				List<Move> lstWm = board.legalMoves();
				board.undoMove();
				return (lstWm);
			}
		}
		return (null);
	}

	static int getNbWhiteLegalMoves(Board board) {
		if (board.getSideToMove() == Side.WHITE) {
			return (board.legalMoves().size());
		} else {
			if (board.doNullMove()) {
				int nbWhiteMoves = board.legalMoves().size();

				board.undoMove();
				return (nbWhiteMoves);
			}
		}
		return (-1);
	}

	static int getNbBlackLegalMoves(Board board) {
		if (board.getSideToMove() == Side.BLACK) {
			return (board.legalMoves().size());
		} else {
			if (board.doNullMove()) {
				int nbBlackMoves = board.legalMoves().size();

				board.undoMove();
				return (nbBlackMoves);
			}
		}
		return (-1);
	}

	@Test
	void testLegalMovesBothSidesPosInit() {

		final ChessLibMoveGenerator lChessLibMoveGeneratorInit = fromFEN(MAGIC_FEN_INIT);
		Board boardInit = lChessLibMoveGeneratorInit.getBoard();

		assertTrue(areLegalMovesBelongingToSideToMove(boardInit));

	}

	@Test
	void testContentsLegalMovesBothSidesPosAfterE5E6() {

		final ChessLibMoveGenerator lChessLibMoveGeneratorInit = fromFEN(MAGIC_FEN_AFTER_E5E6);
		Board boardAfterE5E6 = lChessLibMoveGeneratorInit.getBoard();

		assertEquals(3, getNbBlackLegalMoves(boardAfterE5E6));
		assertEquals(18, getNbWhiteLegalMoves(boardAfterE5E6));

	}
}
