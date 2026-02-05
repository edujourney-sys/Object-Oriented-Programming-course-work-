package vu.studentregistrationapp;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.io.*;
import java.time.LocalDate;
import java.time.Period;
import java.time.YearMonth;
import java.util.regex.Pattern;
import java.util.Scanner;

public class StudentRegistrationApp extends JFrame {
    private JTextField tfFirstName, tfLastName, tfEmail, tfConfirmEmail;
    private JPasswordField pfPassword, pfConfirmPassword;
    private JComboBox<String> cbYear, cbMonth, cbDay;
    private JRadioButton rbMale, rbFemale, rbCivil, rbCSE, rbElec, rbEC, rbMech;
    private JTextArea outputArea;
    private ButtonGroup genderGroup, deptGroup;

    public StudentRegistrationApp() {
        setTitle("Student Registration System");
        setSize(950, 750);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // ===== THEME COLORS =====
        Color bgColor = new Color(245, 245, 245); 
        Color navyBlue = new Color(0, 0, 128);
        Color fieldWhite = Color.WHITE;
        Color midGrey = new Color(180, 180, 180);

        JPanel contentPane = new JPanel(new BorderLayout());
        Border outerBorder = new MatteBorder(40, 15, 8, 15, navyBlue);
        Border innerBorder = new MatteBorder(6, 6, 6, 6, Color.WHITE);
        contentPane.setBorder(new CompoundBorder(outerBorder, innerBorder));
        contentPane.setBackground(bgColor);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(bgColor);
        GridBagConstraints gbc = new GridBagConstraints();

        // Title
        JLabel title = new JLabel("NEW STUDENT REGISTRATION FORM");
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setForeground(navyBlue);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 3;
        gbc.insets = new Insets(0, 0, 30, 0);
        mainPanel.add(title, gbc);

        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;

        // Form Rows
        addFormRow(mainPanel, gbc, "Student First Name", tfFirstName = createTextField(fieldWhite), 1);
        addFormRow(mainPanel, gbc, "Student Last Name", tfLastName = createTextField(fieldWhite), 2);
        addFormRow(mainPanel, gbc, "Email Address", tfEmail = createTextField(fieldWhite), 3);
        addFormRow(mainPanel, gbc, "Confirm Email Address", tfConfirmEmail = createTextField(fieldWhite), 4);
        addFormRow(mainPanel, gbc, "Password", pfPassword = createPasswordField(fieldWhite), 5);
        addFormRow(mainPanel, gbc, "Confirm Password", pfConfirmPassword = createPasswordField(fieldWhite), 6);

        // Date of Birth Row
        gbc.gridx = 0; gbc.gridy = 7; gbc.insets = new Insets(5, 0, 5, 10);
        mainPanel.add(new JLabel("Date of Birth"), gbc);

        JPanel dobPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        dobPanel.setOpaque(false);
        cbYear = createComboBox(new String[]{"Select Year"});
        for(int i=2026; i>=1960; i--) cbYear.addItem(String.valueOf(i));
        cbMonth = createComboBox(new String[]{"Select Month", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"});
        cbDay = createComboBox(new String[]{"Select Day"});

        // Logic for auto-updating days
        cbMonth.addActionListener(e -> updateDays());
        cbYear.addActionListener(e -> updateDays());

        dobPanel.add(cbYear); dobPanel.add(cbMonth); dobPanel.add(cbDay);
        gbc.gridx = 1; mainPanel.add(dobPanel, gbc);

        addFormRow(mainPanel, gbc, "Gender", createGenderPanel(), 8);
        addFormRow(mainPanel, gbc, "Department", createDeptPanel(), 9);

        // Output Header
        gbc.gridx = 2; gbc.gridy = 8;
        JLabel lblBelow = new JLabel("Your Data is Below:");
        lblBelow.setFont(new Font("Arial", Font.BOLD, 18));
        lblBelow.setForeground(navyBlue);
        mainPanel.add(lblBelow, gbc);

        // Output Area
        outputArea = new JTextArea(10, 25);
        outputArea.setEditable(false);
        outputArea.setBackground(Color.WHITE);
        outputArea.setBorder(new CompoundBorder(new LineBorder(midGrey, 2), new EmptyBorder(6, 6, 6, 6)));
        gbc.gridy = 9; gbc.gridheight = 2; gbc.fill = GridBagConstraints.BOTH;
        mainPanel.add(new JScrollPane(outputArea), gbc);

        // Buttons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        btnPanel.setOpaque(false);
        JButton btnSubmit = new JButton("Submit");
        JButton btnCancel = new JButton("Cancel");
        styleButton(btnSubmit, midGrey);
        styleButton(btnCancel, new Color(210, 210, 210));
        btnPanel.add(btnSubmit); btnPanel.add(Box.createHorizontalStrut(10)); btnPanel.add(btnCancel);

        gbc.gridx = 1; gbc.gridy = 11; gbc.gridheight = 1; gbc.fill = GridBagConstraints.NONE;
        mainPanel.add(btnPanel, gbc);

        contentPane.add(mainPanel, BorderLayout.CENTER);
        setContentPane(contentPane);

        btnSubmit.addActionListener(e -> handleSubmit());
        btnCancel.addActionListener(e -> resetForm());
    }

    private void updateDays() {
        if (cbYear.getSelectedIndex() <= 0 || cbMonth.getSelectedIndex() <= 0) return;
        int year = Integer.parseInt((String)cbYear.getSelectedItem());
        int month = cbMonth.getSelectedIndex();
        int daysInMonth = YearMonth.of(year, month).lengthOfMonth();

        cbDay.removeAllItems();
        cbDay.addItem("Select Day");
        for (int i = 1; i <= daysInMonth; i++) cbDay.addItem(String.format("%02d", i));
    }

    private void handleSubmit() {
        StringBuilder err = new StringBuilder();
        String fName = tfFirstName.getText().trim();
        String lName = tfLastName.getText().trim();
        String email = tfEmail.getText().trim();
        String cEmail = tfConfirmEmail.getText().trim();
        String pass = new String(pfPassword.getPassword());
        String cPass = new String(pfConfirmPassword.getPassword());

        if(fName.isEmpty() || lName.isEmpty()) err.append("- Names are required.\n");
        if(!email.matches("^[\\w.-]+@[\\w.-]+\\.[a-z]{2,6}$")) err.append("- Invalid Email.\n");
        if(!email.equals(cEmail)) err.append("- Emails do not match.\n");
        if(pass.length() < 8 || !pass.matches(".*[a-zA-Z].*") || !pass.matches(".*\\d.*")) 
            err.append("- Password must be 8-20 chars with 1 letter and 1 digit.\n");
        if(!pass.equals(cPass)) err.append("- Passwords do not match.\n");

        if(cbDay.getSelectedIndex() <= 0) {
            err.append("- Incomplete Date of Birth.\n");
        } else {
            LocalDate dob = LocalDate.of(Integer.parseInt((String)cbYear.getSelectedItem()), cbMonth.getSelectedIndex(), cbDay.getSelectedIndex());
            int age = Period.between(dob, LocalDate.now()).getYears();
            if(age < 16 || age > 60) err.append("- Age must be 16-60.\n");
        }

        if(genderGroup.getSelection() == null) err.append("- Select Gender.\n");
        if(deptGroup.getSelection() == null) err.append("- Select Department.\n");

        if(err.length() > 0) {
            JOptionPane.showMessageDialog(this, err.toString(), "Validation Errors", JOptionPane.ERROR_MESSAGE);
        } else {
            processValidSubmit(fName, lName, email);
        }
    }

    private void processValidSubmit(String fName, String lName, String email) {
        int currentYear = LocalDate.now().getYear();
        int count = 1;
        File file = new File("students.csv");
        if(file.exists()) {
            try (Scanner sc = new Scanner(file)) {
                while(sc.hasNextLine()) {
                    String line = sc.nextLine();
                    if(line.contains("ID: " + currentYear)) count++;
                }
            } catch (Exception e) {}
        }

        String id = currentYear + "-" + String.format("%05d", count);
        String gender = rbMale.isSelected() ? "M" : "F";
        String dept = "";
        if(rbCivil.isSelected()) dept = "Civil";
        else if(rbCSE.isSelected()) dept = "Computer Science and Engineering";
        else if(rbElec.isSelected()) dept = "Electrical";
        else if(rbEC.isSelected()) dept = "Electronics and Communication";
        else if(rbMech.isSelected()) dept = "Mechanical";

        String dobStr = cbYear.getSelectedItem() + "-" + cbMonth.getSelectedItem() + "-" + cbDay.getSelectedItem();
        String record = String.format("ID: %s | %s %s | %s | %s | %s | %s", id, fName, lName, gender, dept, dobStr, email);
        
        outputArea.setText(record);

        try (PrintWriter pw = new PrintWriter(new FileWriter(file, true))) {
            pw.println(record);
            JOptionPane.showMessageDialog(this, "Success! Student data saved successfully.", "Registration Successful", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "File Error: " + ex.getMessage());
        }
    }

    // UI Helper methods (Your original style)
    private JTextField createTextField(Color bg) {
        JTextField tf = new JTextField(22); tf.setBackground(bg);
        tf.setBorder(new LineBorder(new Color(170, 170, 170), 1)); return tf;
    }
    private JPasswordField createPasswordField(Color bg) {
        JPasswordField pf = new JPasswordField(22); pf.setBackground(bg);
        pf.setBorder(new LineBorder(new Color(170, 170, 170), 1)); return pf;
    }
    private JComboBox<String> createComboBox(String[] items) {
        JComboBox<String> cb = new JComboBox<>(items); cb.setBackground(Color.WHITE); return cb;
    }
    private void styleButton(JButton btn, Color bg) {
        btn.setBackground(bg); btn.setFocusPainted(false);
        btn.setBorder(new LineBorder(new Color(140, 140, 140), 1));
    }
    private void addFormRow(JPanel p, GridBagConstraints gbc, String lab, Component c, int y) {
        gbc.gridx = 0; gbc.gridy = y; gbc.insets = new Insets(5, 0, 5, 10);
        p.add(new JLabel(lab), gbc); gbc.gridx = 1; p.add(c, gbc);
    }
    private JPanel createGenderPanel() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0)); p.setOpaque(false);
        rbMale = new JRadioButton("Male"); rbFemale = new JRadioButton("Female");
        genderGroup = new ButtonGroup(); genderGroup.add(rbMale); genderGroup.add(rbFemale);
        rbMale.setOpaque(false); rbFemale.setOpaque(false);
        p.add(rbMale); p.add(rbFemale); return p;
    }
    private JPanel createDeptPanel() {
        JPanel p = new JPanel(new GridLayout(5, 1, 0, 2)); p.setOpaque(false);
        rbCivil = new JRadioButton("Civil");
        rbCSE = new JRadioButton("Computer Science and Engineering");
        rbElec = new JRadioButton("Electrical");
        rbEC = new JRadioButton("Electronics and Communication");
        rbMech = new JRadioButton("Mechanical");
        JRadioButton[] rbs = {rbCivil, rbCSE, rbElec, rbEC, rbMech};
        deptGroup = new ButtonGroup();
        for(JRadioButton r : rbs) { r.setOpaque(false); deptGroup.add(r); p.add(r); }
        return p;
    }
    private void resetForm() {
        tfFirstName.setText(""); tfLastName.setText(""); tfEmail.setText(""); tfConfirmEmail.setText("");
        pfPassword.setText(""); pfConfirmPassword.setText("");
        genderGroup.clearSelection(); deptGroup.clearSelection();
        cbYear.setSelectedIndex(0); cbMonth.setSelectedIndex(0); cbDay.setSelectedIndex(0);
        outputArea.setText("");
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new StudentRegistrationApp().setVisible(true));
    }
}