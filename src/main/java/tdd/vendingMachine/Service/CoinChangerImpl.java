package tdd.vendingMachine.Service;

import java.util.*;

/**
 * Simple Dynamic-Programming based implementation.
 * Complexity O(nstep * N),
 * nstep : number of allowed steps between 0 and required sum
 * M : number of avilable coins
 *
 * This implementation allows for a preference on coins in change given, so as to
 * minimize the number of events (over time) when change cannot be paid. In order to use this
 * functionality, preferred `penality` array should be set.
 *
 * Default implementation selects least coins to give the change.
 *
 */
public class CoinChangerImpl implements CoinChanger {
    int dmoney;

    public CoinChangerImpl(int dmoney) {
        this.dmoney = dmoney;
    }

    @Override
    public List<Integer> distribute(Map<Integer, Integer> avilableCoins, int sum) {
        if (sum % dmoney != 0)
            throw new RuntimeException(SrvError.SUM_NOT_DIVISIBLE_BY_DMONEY.toString());

        double MAXCOST = 1e10;
        int LARGESTCOIN = 10001;
        int nstep = sum / dmoney + 1;
        double[] cost = new double[nstep];
        int[] prev = new int[nstep];
        //`sum` at (nstep-1)
        for (int i = 0; i < nstep; i++) {
            cost[i] = 1e10;
            prev[i] = -1;
        }

        //larger penalty --> coins of this type will less likely be used in change
        double[] penalty = new double[LARGESTCOIN];
        for (int i = 0; i < penalty.length; i++) {
            penalty[i] = 1.0;
//            if (i<20) penalty[i] = 0.1;   //Way to set preference: would prefere to pay in <20 coins
        }

        List<Integer> cc = new ArrayList<>();
        for (int nom : avilableCoins.keySet()) {
            if (nom% dmoney !=0)
                throw new RuntimeException(SrvError.NOMINALS_NOT_DIVISIBLE_BY_DMONEY.toString());
            for (int i = 0; i < avilableCoins.get(nom); i++) {
                cc.add(nom);
            }
        }


        int ncoin = cc.size();
        int[] coin = new int[ncoin];
        int j = 0;
        for(int i : cc) coin[j++]=i;

        //Dynamic Programming algo:
        cost[0] = 0;
        prev[0] = 0;
        for (int co = 0; co < ncoin; co++) {
            int dstep = coin[co]/dmoney ;
            for (int st = nstep-1; st >= 0; st--) {
                if ((st - dstep)<0) break;
                double possible = cost[st-dstep] + penalty[coin[co]];
                if (cost[st] > possible ) {
                    cost[st] = possible;
                    prev[st] = st-dstep;
                }
            }
        }
        if (cost[nstep-1]==MAXCOST) return null;    //cannot give change
        List<Integer> coinsUsed = new ArrayList<>();
        int at = nstep-1;
        while(at!=0) {
            int p = prev[at];
            coinsUsed.add((at - p)*dmoney);
            at = p;
        }
        Collections.sort(coinsUsed);
        return coinsUsed;
    }
}
