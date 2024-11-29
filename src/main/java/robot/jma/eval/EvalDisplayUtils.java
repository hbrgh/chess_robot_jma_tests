package robot.jma.eval;

import java.util.function.Supplier;
import java.util.stream.IntStream;

import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.File;
import com.github.bhlangonijr.chesslib.Piece;
import com.github.bhlangonijr.chesslib.Rank;
import com.github.bhlangonijr.chesslib.Side;
import com.github.bhlangonijr.chesslib.Square;

public class EvalDisplayUtils {
	
	private static IntStream zeroToSeven() {
		return IntStream.iterate(0, i -> i + 1).limit(8);
	}

	private static IntStream sevenToZero() {
		return IntStream.iterate(7, i -> i - 1).limit(8);
	}



	public static String genFenSymbolForDisplay(Piece piece) {
		return (piece == Piece.NONE ? " " : piece.getFenSymbol());
	}

	/**
	 * Freely inspired from https://github.com/Luecx/Chess.git
	 * Returns a string representation of the board from the given player's point of
	 * view.
	 *
	 * @param side The side whose home rank should be at the bottom of the resulting
	 *             representation.
	 * @return A string representation of the board from the given player's point of
	 *         view.
	 * @since 1.4.0
	 */
	public static String toStringFromViewPointHb(Board board, Side side) {
		StringBuilder sb = new StringBuilder();

		final Supplier<IntStream> rankIterator = side == Side.WHITE ? EvalDisplayUtils::sevenToZero
				: EvalDisplayUtils::zeroToSeven;
		final Supplier<IntStream> fileIterator = side == Side.WHITE ? EvalDisplayUtils::zeroToSeven
				: EvalDisplayUtils::sevenToZero;

		sb.append(" ┌───┬───┬───┬───┬───┬───┬───┬───┐\n");

		rankIterator.get().forEach(i -> {
			Rank r = Rank.allRanks[i];
			fileIterator.get().forEach(n -> {
				File f = File.allFiles[n];
				if (!File.NONE.equals(f) && !Rank.NONE.equals(r)) {
					Square sq = Square.encode(r, f);
					Piece piece = board.getPiece(sq);
					sb.append(" | ");
					sb.append(genFenSymbolForDisplay(piece));

				}

			});
			if (r != Rank.RANK_1) {
				sb.append(" |\n ├───┼───┼───┼───┼───┼───┼───┼───┤\n");
			} else {
				sb.append(" |\n └───┴───┴───┴───┴───┴───┴───┴───┘\n");
			}

		});

		return sb.toString();
	}

	public static String toStringHb(Board board) {
		return toStringFromViewPointHb(board, Side.WHITE) + "Side to move: " + board.getSideToMove();
	}

}
