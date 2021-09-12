package com.mobiquity.packer;

import com.mobiquity.classes.Package;
import com.mobiquity.classes.*;
import com.mobiquity.exception.APIException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;
import java.util.stream.Collectors;

public class Packer {

    private Packer() {
    }

    public static String pack(String filePath) throws APIException, IOException {

        File file = new File(filePath); //create new file instance

        if (file.exists()) {
            FileReader fr = new FileReader(file); //read the file
            BufferedReader br = new BufferedReader(fr); //create character input stream
            List<String[]> lines = br.lines().map(line -> line.split(":")).collect(Collectors.toList()); //create list of array of strings by package size and items
             /* for (int i = 0; i < lines.size(); i++) {
                String[] strings = lines.get(i);
                for (int j = 0; j < strings.length; j++) {
                  System.out.print(strings[j] + " ");
                }
                System.out.println();
              } */
            StringBuffer result = new StringBuffer(); //create string buffer
            fr.close(); //close the stream

            for (String[] packageData : lines) {
                int limit = Integer.parseInt(packageData[0].trim()); //get package limit as first element of list

                if (limit < Constants.MAX_PACKAGE_LIMIT) {                          //check max package limit
                    Package optimizedPackage = solveProblem(packageData, limit,2);  //send for solution - solType = 1 => DP : BnB

                    result.append("\n");

                    if (optimizedPackage.getItems().size() > 0) {                   //get result and print
                        optimizedPackage.getItems().forEach(item -> result.append(item.getIndex()).append(","));
                    } else {
                        result.append("-");
                    }

                } else {
                    throw new APIException("Package limit should be less than 100.");  //throw limit exception
                }
            }
            return result.toString();
        }
        throw new APIException("No file found in: " + filePath); //wrong path exception
    }

    private static Package solveProblem(String[] packageData, Integer limit, int solType) throws APIException {
        String itemData = packageData[1]; //get the items
        String[] items = itemData.trim().split(" "); // split items
        Item[] fileItems = new Item[items.length];

        for (int i = 0; i < fileItems.length; i++) {
            String[] splitItemData = items[i].replace("€", "").replace("(", "").replace(")", "").split(",");
            //        System.out.println(Arrays.toString(itemData));
            try {
                Integer index = Integer.parseInt(splitItemData[0]);
                double weight = Double.parseDouble(splitItemData[1]);
                double cost = Double.parseDouble(splitItemData[2]);

                Item item = new Item(index, weight, cost);
                if (item.isValid()) {
                    fileItems[i] = item;
                }
            } catch (NumberFormatException e) {
                throw new APIException("Wrong formatted cost or weight!");
            }
        }
        if (solType == 1)
            return getOptimizedPackageDP(fileItems, limit);   //for DP approach
        else
            return getOptimizedPackageBNB(fileItems, limit);  //for BnB approach
    }

    public static Package getOptimizedPackageDP(Item[] items, int limit) throws APIException { //Dynamic Programming approach
        int numberOfItems = items.length;

        if (numberOfItems > Constants.MAX_ITEMS_AMOUNT)
            throw new APIException("There are more than 15 items!");  //Max items exception

        int[][] m = new int[numberOfItems + 1][limit + 1];

        for (int i = 0; i <= limit; i++) {
            m[0][i] = 0;
        }

        for (int i = 1; i <= numberOfItems; i++) {
            for (int j = 0; j <= limit; j++) {
                int weightInt = (int) items[i - 1].getWeight();  //get current item's weight
                int costInt = (int) items[i - 1].getCost();      //get current item's cost

                if (weightInt > j) {                                //check if item's weight fits package
                    m[i][j] = m[i - 1][j];
                } else {
                    m[i][j] = Math.max(m[i - 1][j], m[i - 1][j - weightInt] + costInt);  //get max
                }
            }
        }


        int mx = m[numberOfItems][limit];
        int lm = limit;
        List<Item> itemsSolution = new ArrayList<>();

        for (int i = numberOfItems; i > 0 && mx > 0; i--) {
            if (mx != m[i - 1][lm]) {
                itemsSolution.add(items[i - 1]);
                mx -= (int) items[i - 1].getCost();
                lm -= (int) items[i - 1].getWeight();
            }
        }

        return new Package(itemsSolution, limit);
    }

    public static Package getOptimizedPackageBNB(Item[] items, int limit) throws APIException { //Branch and Bound approach

        int numberOfItems = items.length;

        if (numberOfItems > Constants.MAX_ITEMS_AMOUNT)
            throw new APIException("There are more than 15 items!");  //Max items exception

        Item[] originalArray = new Item[numberOfItems];        // take a copy of original items and sort by ratio
        System.arraycopy(items, 0, originalArray, 0, numberOfItems);
        Arrays.sort(items, new SortByRatio()); //sort by ratio and get most effective item

        Node current, left, right;
        current = new Node();
        left = new Node();
        right = new Node();

        double minLB = 0;
        double finalLB = Integer.MAX_VALUE;
        current.setTotalValue(0.0);
        current.setTotalValue(0.0);
        current.setUpperBound(0.0);
        current.setLowerBound(0.0);
        current.setLevel(0);
        current.setFlag(false);


        PriorityQueue<Node> pq = new PriorityQueue<Node>(new SortByLb());        // Create a priority queue for lb

        pq.add(current); //dummy node

        boolean[] currPath = new boolean[numberOfItems + 1]; //Bool array that store if the element is included or not
        boolean[] finalPath = new boolean[numberOfItems + 1]; //Bool array to get result at the last level

        while (!pq.isEmpty()) {
            current = pq.poll();
            if (current.getUpperBound() > minLB
                    || current.getUpperBound() >= finalLB) {
                continue; //if current node's best case is worse than
            }

            if (current.getLevel() != 0)
                currPath[current.getLevel() - 1] = current.isFlag();
            if (current.getLevel() == numberOfItems) {
                if (current.getLowerBound() < finalLB) {
                    // Reached last level
                    for (int i = 0; i < numberOfItems; i++) {
                        finalPath[items[i].getIndex() - 1] = currPath[i];
                    }
                    finalLB = current.getLowerBound();
                }
                continue;
            }

            int level = current.getLevel();

            assign(right, upperBound(current.getTotalValue(), current.getTotalWeight(), level + 1, items, limit), lowerBound(current.getTotalValue(), current.getTotalWeight(), level + 1,
                    items, limit), level + 1, false, current.getTotalValue(), current.getTotalWeight());

            if (current.getTotalWeight() + items[current.getLevel()].getWeight() <= limit) { //check limit for total weight plus current item's weight

                left.setUpperBound(upperBound(current.getTotalValue() - items[level].getCost(), current.getTotalWeight() + items[level].getWeight(), level + 1, items, limit));
                left.setLowerBound(lowerBound(current.getTotalValue() - items[level].getCost(), current.getTotalWeight() + items[level].getWeight(), level + 1, items, limit));
                assign(left, left.getUpperBound(), left.getLowerBound(), level + 1, true, current.getTotalValue() - items[level].getCost(), current.getTotalWeight() + items[level].getWeight());
            }
            else {

                left.setUpperBound(1.0);
                left.setLowerBound(1.0);
            }


            minLB = Math.min(minLB, left.getLowerBound());            // Update minLB
            minLB = Math.min(minLB, right.getLowerBound());

            if (minLB >= left.getUpperBound())
                pq.add(new Node(left));
            if (minLB >= right.getUpperBound())
                pq.add(new Node(right));
        }

        //create return Item object
        List<Item> itemsSolution = new ArrayList<>();
        for (int i = 0; i < numberOfItems; i++) {
            if (finalPath[i]) {
                itemsSolution.add(originalArray[i]);
            }

        }

        return new Package(itemsSolution, limit);
    }

    public static double lowerBound(double tv, double tw, int idx, Item[] items, int limit) {
        double value = tv;
        double weight = tw;
        for (int i = idx; i < items.length; i++) {
            if (weight + items[i].getWeight() <= limit) {
                weight += items[i].getWeight();
                value -= items[i].getCost();
            } else {
                break;
            }
        }
        return value;
    }

    public static double upperBound(double tv, double tw, int idx, Item[] items, int limit) {
        double value = tv;
        double weight = tw;
        for (int i = idx; i < items.length; i++) {
            if (weight + items[i].getWeight() <= limit) {
                weight += items[i].getWeight();
                value -= items[i].getCost();
            } else {
                value -= (limit - weight) / items[i].getWeight() * items[i].getCost();
                break;
            }
        }
        return value;
    }

    public static void assign(Node a, double ub, double lb, int level, boolean flag, double tv, double tw) {
        a.setUpperBound(ub);
        a.setLowerBound(lb);
        a.setLevel(level);
        a.setFlag(flag);
        a.setTotalValue(tv);
        a.setTotalWeight(tw);
    }


}
