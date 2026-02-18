package robot.jma.infra.test;

import com.fathzer.jchess.chesslib.ai.eval.hb.HbPestoState;

/** The state of a simplified evaluator.
 */
public class HbComplPestoStateTest {
	

	public static void main(String[] args) {
		HbPestoState lHbPestoState = new HbPestoState();
		System.out.println(lHbPestoState.hbComplPs.mgPassedPawnsBonus);
		System.out.println(lHbPestoState.hbComplPs.egPassedPawnsBonus);
		(lHbPestoState.hbComplPs.mgPassedPawnsBonus)++;
		System.out.println(lHbPestoState.hbComplPs.mgPassedPawnsBonus);
		System.out.println("STALINE");
		HbPestoState HbPestoState2 = new HbPestoState();
		lHbPestoState.copyTo(HbPestoState2);
		System.out.println(HbPestoState2.hbComplPs.mgPassedPawnsBonus);
		System.out.println(HbPestoState2.hbComplPs.egPassedPawnsBonus);

	}
}