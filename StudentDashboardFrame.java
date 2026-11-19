import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class StudentDashboardFrame extends JFrame {

    private JTextField txtId, txtName, txtEmail, txtPhone, txtDob, txtCourseId;
    private JComboBox<String> cmbGender;
    private JTable tblStudents;
    private DefaultTableModel tableModel;

    public StudentDashboardFrame() {
        if (!SessionManager.isLoggedIn()) {
            JOptionPane.showMessageDialog(null, "Please login first!");
            new NewJFrame().setVisible(true);
            dispose();
            return;
        }

        setTitle("Student Management System - Dashboard");
        setSize(900, 560);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // TOP BAR
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(44, 62, 80));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        JLabel lblWelcome = new JLabel("STUDENT MANAGEMENT PORTAL  |  Logged in as: " + SessionManager.getCurrentUser());
        lblWelcome.setForeground(Color.WHITE);
        lblWelcome.setFont(new Font("Arial", Font.BOLD, 14));

        JButton btnLogout = new JButton("Logout");
        btnLogout.setBackground(new Color(231, 76, 60));
        btnLogout.setForeground(Color.WHITE);
        btnLogout.addActionListener(e -> logout());

        topPanel.add(lblWelcome, BorderLayout.WEST);
        topPanel.add(btnLogout, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        // LEFT FORM
        JPanel formPanel = new JPanel(new GridLayout(8, 2, 8, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder("Student Details"));
        formPanel.setPreferredSize(new Dimension(330, 0));

        txtId = new JTextField();
        txtId.setEditable(false);
        txtId.setBackground(new Color(235, 235, 235));

        txtName = new JTextField();
        txtEmail = new JTextField();
        txtPhone = new JTextField();
        cmbGender = new JComboBox<>(new String[]{"Male", "Female", "Other"});
        txtDob = new JTextField("2005-01-01");
        txtCourseId = new JTextField();

        formPanel.add(new JLabel("Student ID (Auto):")); formPanel.add(txtId);
        formPanel.add(new JLabel("Full Name:*"));        formPanel.add(txtName);
        formPanel.add(new JLabel("Email:*"));            formPanel.add(txtEmail);
        formPanel.add(new JLabel("Phone (10 digits):*")); formPanel.add(txtPhone);
        formPanel.add(new JLabel("Gender:"));            formPanel.add(cmbGender);
        formPanel.add(new JLabel("DOB (YYYY-MM-DD):*")); formPanel.add(txtDob);
        formPanel.add(new JLabel("Course ID (Number):*")); formPanel.add(txtCourseId);

        JPanel btnPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        JButton btnAdd = new JButton("Add");
        JButton btnUpdate = new JButton("Update");
        JButton btnDelete = new JButton("Delete");
        JButton btnClear = new JButton("Clear");

        btnPanel.add(btnAdd);
        btnPanel.add(btnUpdate);
        btnPanel.add(btnDelete);
        btnPanel.add(btnClear);

        JPanel leftWrapper = new JPanel(new BorderLayout(5, 10));
        leftWrapper.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 5));
        leftWrapper.add(formPanel, BorderLayout.CENTER);
        leftWrapper.add(btnPanel, BorderLayout.SOUTH);
        add(leftWrapper, BorderLayout.WEST);

        // TABLE
        String[] columns = {"ID", "Name", "Email", "Phone", "Gender", "DOB", "Course ID"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblStudents = new JTable(tableModel);
        tblStudents.setRowHeight(24);
        JScrollPane scrollPane = new JScrollPane(tblStudents);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Registered Students (Click a row to Edit/Delete)"));
        add(scrollPane, BorderLayout.CENTER);

        tblStudents.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tblStudents.getSelectedRow();
                if (row >= 0) {
                    txtId.setText(tableModel.getValueAt(row, 0).toString());
                    txtName.setText(tableModel.getValueAt(row, 1).toString());
                    txtEmail.setText(tableModel.getValueAt(row, 2).toString());
                    txtPhone.setText(tableModel.getValueAt(row, 3).toString());
                    cmbGender.setSelectedItem(tableModel.getValueAt(row, 4).toString());
                    txtDob.setText(tableModel.getValueAt(row, 5).toString());
                    txtCourseId.setText(tableModel.getValueAt(row, 6).toString());
                }
            }
        });

        btnAdd.addActionListener(e -> addRecord());
        btnUpdate.addActionListener(e -> updateRecord());
        btnDelete.addActionListener(e -> deleteRecord());
        btnClear.addActionListener(e -> clearForm());

        loadTableData();
    }

    private boolean validateInputs() {
        String name = txtName.getText().trim();
        String email = txtEmail.getText().trim();
        String phone = txtPhone.getText().trim();
        String dob = txtDob.getText().trim();
        String courseStr = txtCourseId.getText().trim();

        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || dob.isEmpty() || courseStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields marked with * are required!", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (!email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
            JOptionPane.showMessageDialog(this, "Please enter a valid email address!", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (!phone.matches("\\d{10}")) {
            JOptionPane.showMessageDialog(this, "Phone number must be exactly 10 digits!", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (!dob.matches("\\d{4}-\\d{2}-\\d{2}")) {
            JOptionPane.showMessageDialog(this, "DOB must be in YYYY-MM-DD format!", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        try {
            Integer.parseInt(courseStr);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Course ID must be a valid integer!", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    private void addRecord() {
        if (!validateInputs()) return;
        boolean ok = StudentCRUD.addStudent(
                txtName.getText().trim(),
                txtEmail.getText().trim(),
                txtPhone.getText().trim(),
                cmbGender.getSelectedItem().toString(),
                txtDob.getText().trim(),
                Integer.parseInt(txtCourseId.getText().trim())
        );
        if (ok) {
            JOptionPane.showMessageDialog(this, "Student Added Successfully!");
            clearForm();
            loadTableData();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to add student. Check XAMPP MySQL or duplicate email.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateRecord() {
        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Select a student row from the table first!", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!validateInputs()) return;
        boolean ok = StudentCRUD.updateStudent(
                Integer.parseInt(txtId.getText()),
                txtName.getText().trim(),
                txtEmail.getText().trim(),
                txtPhone.getText().trim(),
                cmbGender.getSelectedItem().toString(),
                txtDob.getText().trim(),
                Integer.parseInt(txtCourseId.getText().trim())
        );
        if (ok) {
            JOptionPane.showMessageDialog(this, "Student Updated Successfully!");
            clearForm();
            loadTableData();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to update student.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteRecord() {
        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Select a student row from the table first!", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this student?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            boolean ok = StudentCRUD.deleteStudent(Integer.parseInt(txtId.getText()));
            if (ok) {
                JOptionPane.showMessageDialog(this, "Student Deleted Successfully!");
                clearForm();
                loadTableData();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete student.", "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void loadTableData() {
        tableModel.setRowCount(0);
        for (Object[] row : StudentCRUD.getAllStudents()) {
            tableModel.addRow(row);
        }
    }

    private void clearForm() {
        txtId.setText("");
        txtName.setText("");
        txtEmail.setText("");
        txtPhone.setText("");
        cmbGender.setSelectedIndex(0);
        txtDob.setText("2005-01-01");
        txtCourseId.setText("");
        tblStudents.clearSelection();
    }

    private void logout() {
        SessionManager.logout();
        JOptionPane.showMessageDialog(this, "Logged out successfully!");
        new NewJFrame().setVisible(true);
        dispose();
    }
}