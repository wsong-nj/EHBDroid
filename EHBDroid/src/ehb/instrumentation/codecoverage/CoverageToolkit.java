package ehb.instrumentation.codecoverage;

import com.app.test.AppDir;

import android.util.Log;

/**
 * Note: This class is going to instrumented into apk.
 */
public class CoverageToolkit {

	public static int[][][] bbb = new int[AppDir.CLASSCOUNT][][];
	public static int[][][] lll = new int[AppDir.CLASSCOUNT][][];

	public static int calculateLines() {
		int reachLines = 0;
		for (int i = 0; i < bbb.length; i++) {
			for (int j = 0; j < bbb[i].length; j++) {
				for (int k = 0; k < bbb[i][j].length; k++) {
					int total = bbb[i][j][k] == 0 ? 0 : lll[i][j][k];
					reachLines = reachLines + total;
				}
			}
		}
		return reachLines;
	}

	public static void initbbblllij(int i, int j) {
		bbb[i - 1] = new int[j][];
		lll[i - 1] = new int[j][];
	}

	public static void initbbblllijk(int i, int j, int k) {
		bbb[i - 1][j - 1] = new int[k];
		lll[i - 1][j - 1] = new int[k];
	}

	public static void instrumentData(int i, int j, int k, int length) {
		bbb[i - 1][j - 1][k - 1] = 1;
		lll[i - 1][j - 1][k - 1] = length;
	}

	public static void printResult() {
		int calculateLines = calculateLines();
		Log.e("COUNT", "Reaching Lines: "+calculateLines);
	}

}
