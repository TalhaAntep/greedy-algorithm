import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Please enter the number of judges: ");
        int judgeNumber = scanner.nextInt();

        System.out.print("Please enter the cost of problem type changing: ");
        int changingCost = scanner.nextInt();

        List<Integer> problemTypes = readInputFile("input.txt");

        int totalCost = calculateCost(locateProblems(judgeNumber, problemTypes),changingCost);

        System.out.println("Total cost: " + totalCost);
    }

    //time complexity O(n)
    private static List<Integer> readInputFile(String fileName) {
        ArrayList<Integer> problemTypes = new ArrayList<>();
        try {
            File file = new File(fileName);
            Scanner fileScanner = new Scanner(file);
            System.out.println("The input file is read.");
            fileScanner.nextLine();

            System.out.println("The problem types are listed:");
            while (fileScanner.hasNextLine()) {
                int problemType = Integer.parseInt(fileScanner.nextLine().split(" ")[1]); //parse int foreach problem type
                problemTypes.add(problemType);
                System.out.print("Type " + problemType + ", ");
            }
            fileScanner.close();
            System.out.println();
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }
        return problemTypes;
    }


    //time complexity O(n*m)
    private static List<List<Integer>> locateProblems(int judgeNumber, List<Integer> problemTypes) {
        List<List<Integer>> judgeProblems = new ArrayList<>();
        for (int i = 0; i < judgeNumber; i++) {
            judgeProblems.add(new ArrayList<>());
        }

        List<Integer> lastProblemType = new ArrayList<>();
        for (int i = 0; i < judgeNumber; i++) {
            lastProblemType.add(-1);
        }

        for (int i = 0; i < problemTypes.size(); i++) {
            int problemType = problemTypes.get(i);
            boolean placed = false;

            // control the after problem types
            for (int j = 0; j < judgeNumber; j++) {
                if (lastProblemType.get(j) == problemType) {
                    judgeProblems.get(j).add(problemType);
                    lastProblemType.set(j, problemType);
                    placed = true;
                    break;
                }
            }

            if (!placed) {
                // find the least repeating problem type
                int minIndex = -1;
                int minFutureCount = 5000;

                for (int j = 0; j < judgeNumber; j++) {
                    int futureCount = 0;
                    for (int k = i + 1; k < problemTypes.size(); k++) {
                        if (problemTypes.get(k) == lastProblemType.get(j)) {
                            futureCount++;
                        }
                    }

                    if (futureCount < minFutureCount) {
                        minFutureCount = futureCount;
                        minIndex = j;
                    }
                }

                // add problem type
                judgeProblems.get(minIndex).add(problemType);
                lastProblemType.set(minIndex, problemType);
            }
        }

        return judgeProblems;
    }



    //time complexity O(n)
    private static int calculateCost(List<List<Integer>> judgeProblems, int costOfChange) {
        int totalCost = 0;
        int previousType=0;
        for (List<Integer> problems : judgeProblems) {
            for (int problem : problems) {
                if (totalCost==0){  //add cost that first problem type
                    previousType=problem;
                    totalCost++;
                }
                else {
                    if (previousType != problem){ //add cost when change the problem type
                        totalCost++;
                        previousType=problem;
                    }
                }
            }
        }
        totalCost=totalCost*costOfChange;
        return totalCost;
    }
}