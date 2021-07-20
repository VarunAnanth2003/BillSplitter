import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.TreeMap;

public class Main {
    private static ArrayList<Integer> diffArr = new ArrayList<>();
    private static ArrayList<String> names = new ArrayList<>();
    private static int[] foundComboArr = null;
    //Name, Payment, Bill
    //Positive diff means need to pay x more
    //Negative diff means needs to be reimbursed |x| more
    public static void main(String[] args) {
        TreeMap<String, int[]> payMap = new TreeMap<>();
        payMap.put("Sanaa", new int[] {25, 20});
        payMap.put("Varun", new int[] {15, 5});
        payMap.put("Megan", new int[] {0, 10});
        payMap.put("Vicki", new int[] {15, 20});

        Iterator<String> nameitr = payMap.keySet().iterator();
        while(nameitr.hasNext()) {
           names.add(nameitr.next());
        }
        int sumP = 0;
        int sumB = 0;
        for(int[] x : payMap.values()) {
            sumP+=x[0];
            sumB+=x[1];
        }
        if(sumP < sumB) {
            System.out.println("You cannot pay this bill with the money in the pool");
            System.out.println("Bill amount: " + sumB);
            System.out.println("Amount in Pool: " + sumP);
        } else {
            for(int[] x : payMap.values()) {
                diffArr.add(x[1]-x[0]);
            }
            printDiffs();
            comboChecker();
        }   
    }

    private static void comboChecker() {
        boolean foundCombo = true;
        while(foundCombo) {
            listStatus();
            foundComboArr = null;
            foundCombo = false;
            for (int i = 1; i < diffArr.size(); i++) {
                int leastIndex = identifyLeast();
                try {
                    comboFinder(0, 0, new int[i], diffArr.get(leastIndex));
                } catch (FoundCombinationException e) {
                    //e.printStackTrace();
                    
                }
                if(foundComboArr != null) {
                    foundCombo = true;
                    if(foundComboArr.length > 1) {
                        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                    }
                    for(int x : foundComboArr) {
                        System.out.println(names.get(x) + " needs to pay $" + diffArr.get(x) + " to " + names.get(leastIndex));
                        diffArr.set(x, 0);
                    }
                    diffArr.set(leastIndex, 0);
                    break;
                }
            }
        }
        if(listStatus()) {
            System.out.println("All transactions complete!");
        } else {
            splitChecker();
        }  
    }

    private static void splitChecker() {
        while(!listStatus()) {
            boolean foundSplit = false;
            int bigOwe = identifyGreatest();
            for (int i = 0; i < diffArr.size(); i++) {
                if(diffArr.get(i) < 0 && -1*diffArr.get(i) <= diffArr.get(bigOwe)) {
                    System.out.println(names.get(bigOwe) + " needs to pay $" + -1*diffArr.get(i) + " to " + names.get(i));
                    diffArr.set(bigOwe, diffArr.get(bigOwe) + diffArr.get(i));
                    diffArr.set(i, 0);
                    foundSplit = true;
                }
            }
            if(!foundSplit) {
                comboChecker();
            }
        }
    }

    private static boolean listStatus() {
        for (int i = 0; i < diffArr.size(); i++) {
            if(diffArr.get(i) == 0) {
                //System.out.println(names.get(i) + "'s payment is done");
                names.set(i, "");
            }
        }
        diffArr.removeAll(Arrays.asList(0));
        names.removeAll(Arrays.asList(""));
        if(diffArr.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    private static int identifyLeast() {
        int leastInt = Integer.MAX_VALUE;
        int ret_val = -1;
        for (int i = 0; i < diffArr.size(); i++) {
            if(diffArr.get(i) < leastInt) {
                leastInt = diffArr.get(i);
                ret_val = i;
            }
        }
        return ret_val;
    }

    private static int identifyGreatest() {
        int greatestInt = Integer.MIN_VALUE;
        int ret_val = -1;
        for (int i = 0; i < diffArr.size(); i++) {
            if(diffArr.get(i) > greatestInt) {
                greatestInt = diffArr.get(i);
                ret_val = i;
            }
        }
        return ret_val;
    }

    private static int[] comboFinder(int index, int sumLevel, int[] num, int sumToFind) throws FoundCombinationException {
        if(sumLevel == num.length) {
            int sum = 0;
            for(int x : num) {
                sum+=diffArr.get(x)<0?Integer.MAX_VALUE:diffArr.get(x);
            }
            if(sum == sumToFind*-1) {
                foundComboArr = num;
                throw new FoundCombinationException("Found Combo");
            }
        } else {
            for (int i = 0; i < diffArr.size(); i++) {
                if(index == 0 || !numExists(num, i)) {
                    num[index] = i;
                    comboFinder(index+1, sumLevel+1, num, sumToFind);
                }
            }
        }
        return null;
    }

    private static boolean numExists(int[] num, int x) {
        for(int a : num) {
            if(a == x) {
                return true;
            }
        }
        return false;
    }

    private static void printDiffs() {
        for (int i = 0; i < diffArr.size(); i++) {
            System.out.println(names.get(i) + ": " + diffArr.get(i));
        }
    }
}
