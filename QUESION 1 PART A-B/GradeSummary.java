package vu.gradesummary;

import java.util.Scanner;

public class GradeSummary {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        int studentCount = 1;

        // Array to count grades 1 to 9
        int[] gradeCount = new int[10];

        System.out.println("===== GRADE SUMMARY SYSTEM =====\n");

        while (studentCount <= 5) {
            System.out.print("Enter score for Student " + studentCount + " (0 - 100): ");
            int score = input.nextInt();

            // Validate score
            if (score < 0 || score > 100) {
                System.out.println("❌ Invalid score! Please enter a value between 0 and 100.\n");
                continue; // ask again without increasing student count
            }

            int grade;
            String remark;

            if (score >= 80) {
                grade = 1;
                remark = "D1";
            } else if (score >= 75) {
                grade = 2;
                remark = "D2";
            } else if (score >= 66) {
                grade = 3;
                remark = "C3";
            } else if (score >= 60) {
                grade = 4;
                remark = "C4";
            } else if (score >= 50) {
                grade = 5;
                remark = "C5";
            } else if (score >= 45) {
                grade = 6;
                remark = "C6";
            } else if (score >= 35) {
                grade = 7;
                remark = "P7";
            } else if (score >= 30) {
                grade = 8;
                remark = "P8";
            } else {
                grade = 9;
                remark = "F";
            }

            gradeCount[grade]++;

            // ✅ Individual student result display
            System.out.println("\n--- Student " + studentCount + " Result ---");
            System.out.println("Score  : " + score);
            System.out.println("Grade  : " + grade);
            System.out.println("Remark : " + remark);
            System.out.println("-------------------------------\n");

            studentCount++;
        }

        // ✅ Summary
        System.out.println("\n===== FINAL GRADE SUMMARY =====");
        for (int i = 1; i <= 9; i++) {
            System.out.println("Grade " + i + " -> " + gradeCount[i] + " student(s)");
        }

        input.close();
    }
}
