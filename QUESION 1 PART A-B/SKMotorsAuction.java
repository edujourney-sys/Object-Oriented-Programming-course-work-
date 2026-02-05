package vu.skmotorsauction;

import java.util.Scanner;

public class SKMotorsAuction {
    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);

        System.out.println("=================================");
        System.out.println("      SK MOTORS AUCTION SYSTEM    ");
        System.out.println("=================================\n");

        // ===== 1. VEHICLE DETAILS =====
        System.out.println("----- VEHICLE DETAILS -----");

        System.out.print("Enter Vehicle Registration Number: ");
        String regNumber = input.nextLine();

        System.out.print("Enter Vehicle Cost: ");
        double vehicleCost = input.nextDouble();

        System.out.println();

        // ===== 2. BIDDERS DETAILS =====
        System.out.println("----- BIDDERS DETAILS -----");

        double highestBid = 0;
        int highestBidder = 0;

        for (int i = 1; i <= 3; i++) {
            System.out.print("Enter Bid Amount for Bidder " + i + ": ");
            double bid = input.nextDouble();

            if (bid > highestBid) {
                highestBid = bid;
                highestBidder = i;
            }
        }

        System.out.println();

        // ===== 3. FINANCIAL DETAILS =====
        System.out.println("----- FINANCIAL DETAILS -----");

        System.out.print("Enter Total Deposits Made: ");
        double deposits = input.nextDouble();

        System.out.print("Enter Total Expenses Incurred: ");
        double expenses = input.nextDouble();

        System.out.println();

        // ===== 4. CALCULATIONS =====
        double balance = highestBid - deposits;
        double profitLoss = highestBid - (vehicleCost + expenses);

        // ===== 5. VEHICLE AUCTION SUMMARY =====
        System.out.println("=================================");
        System.out.println("        VEHICLE AUCTION SUMMARY   ");
        System.out.println("=================================");

        System.out.println("Vehicle Registration Number : " + regNumber);
        System.out.println("Vehicle Cost               : " + vehicleCost);
        System.out.println("Total Expenses             : " + expenses);

        System.out.println("\nHighest Bidder             : Bidder " + highestBidder);
        System.out.println("Highest Bid Amount         : " + highestBid);
        System.out.println("Total Deposits             : " + deposits);
        System.out.println("Balance to be Cleared      : " + balance);

        System.out.println();

        if (profitLoss > 0) {
            System.out.println("Profit Made                : " + profitLoss);
        } else if (profitLoss < 0) {
            System.out.println("Loss Made                  : " + Math.abs(profitLoss));
        } else {
            System.out.println("Result                     : No Profit, No Loss");
        }

        System.out.println("=================================");

        input.close();
    }
}
