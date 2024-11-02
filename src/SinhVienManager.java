import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class SinhVienManager extends JFrame {
    private JTextField tfMaSV, tfHoTen, tfLop, tfGPA;
    private JButton btnHienThi, btnThem, btnCapNhat, btnXoa, btnReset;
    private JTable table;
    private DefaultTableModel model;
    private Connection conn;

    public SinhVienManager() {


        // Init
        setTitle("Quản lý Sinh Viên");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        // Text Field
        JLabel lblMaSV = new JLabel("Mã SV:");
        lblMaSV.setBounds(20, 20, 100, 25);
        add(lblMaSV);

        tfMaSV = new JTextField();
        tfMaSV.setBounds(120, 20, 150, 25);
        add(tfMaSV);

        JLabel lblHoTen = new JLabel("Họ Tên:");
        lblHoTen.setBounds(20, 60, 100, 25);
        add(lblHoTen);

        tfHoTen = new JTextField();
        tfHoTen.setBounds(120, 60, 150, 25);
        add(tfHoTen);

        JLabel lblLop = new JLabel("Lớp:");
        lblLop.setBounds(20, 100, 100, 25);
        add(lblLop);

        tfLop = new JTextField();
        tfLop.setBounds(120, 100, 150, 25);
        add(tfLop);

        JLabel lblGPA = new JLabel("GPA:");
        lblGPA.setBounds(20, 140, 100, 25);
        add(lblGPA);

        tfGPA = new JTextField();
        tfGPA.setBounds(120, 140, 150, 25);
        add(tfGPA);

        // Create Table
        model = new DefaultTableModel();
        model.addColumn("Mã SV");
        model.addColumn("Họ Tên");
        model.addColumn("Lớp");
        model.addColumn("GPA");

        table = new JTable(model);
        JScrollPane sp = new JScrollPane(table);
        sp.setBounds(20, 180, 550, 200);
        add(sp);

        // Create Buttons - đặt các nút ở phía dưới
        btnHienThi = new JButton("Hiển thị");
        btnHienThi.setBounds(50, 400, 100, 25);
        add(btnHienThi);

        btnThem = new JButton("Thêm");
        btnThem.setBounds(160, 400, 100, 25);
        add(btnThem);

        btnCapNhat = new JButton("Cập Nhật");
        btnCapNhat.setBounds(270, 400, 100, 25);
        add(btnCapNhat);

        btnXoa = new JButton("Xoá");
        btnXoa.setBounds(380, 400, 100, 25);
        add(btnXoa);

        btnReset = new JButton("Reset");
        btnReset.setBounds(490, 400, 100, 25);
        add(btnReset);

        // ConnectDB
        connectToDatabase();

        // FunctionBtn
        btnHienThi.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayData();
            }
        });

        btnThem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                insertData();
            }
        });

        btnCapNhat.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateData();
            }
        });

        btnXoa.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteData();
            }
        });

        btnReset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetForm();
            }
        });

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                tfMaSV.setText(model.getValueAt(table.getSelectedRow(), 0).toString());
                tfHoTen.setText(model.getValueAt(table.getSelectedRow(), 1).toString());
                tfLop.setText(model.getValueAt(table.getSelectedRow(), 2).toString());
                tfGPA.setText(model.getValueAt(table.getSelectedRow(), 3).toString());
            }
        });
    }

    private void connectToDatabase() {
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:students.db");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void displayData() {
        try {
            model.setRowCount(0);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM SinhVien");
            while (rs.next()) {
                model.addRow(new Object[]{rs.getString("maSV"), rs.getString("hoTen"), rs.getString("lop"), rs.getDouble("gpa")});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void insertData() {
        String maSV = tfMaSV.getText();
        String hoTen = tfHoTen.getText();
        String lop = tfLop.getText();
        String gpaText = tfGPA.getText();

        if (maSV.isEmpty() || hoTen.isEmpty() || lop.isEmpty() || gpaText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng điền đầy đủ thông tin");
            return;
        }

        try {
            double gpa = Double.parseDouble(gpaText);
            String sql = "INSERT INTO SinhVien(maSV, hoTen, lop, gpa) VALUES (?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, maSV);
            pstmt.setString(2, hoTen);
            pstmt.setString(3, lop);
            pstmt.setDouble(4, gpa);
            pstmt.executeUpdate();
            displayData();
            resetForm();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void resetForm() {
        tfMaSV.setText("");
        tfHoTen.setText("");
        tfLop.setText("");
        tfGPA.setText("");
    }

    private void updateData() {
        String maSV = tfMaSV.getText();
        String hoTen = tfHoTen.getText();
        String lop = tfLop.getText();
        String gpaText = tfGPA.getText();

        if (maSV.isEmpty() || hoTen.isEmpty() || lop.isEmpty() || gpaText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng điền đầy đủ thông tin để cập nhật");
            return;
        }

        try {
            double gpa = Double.parseDouble(gpaText);
            String sql = "UPDATE SinhVien SET hoTen = ?, lop = ?, gpa = ? WHERE maSV = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, hoTen);
            pstmt.setString(2, lop);
            pstmt.setDouble(3, gpa);
            pstmt.setString(4, maSV);
            int rowsUpdated = pstmt.executeUpdate();
            if (rowsUpdated > 0) {
                JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
                displayData();
                resetForm();
            } else {
                JOptionPane.showMessageDialog(this, "Không tìm thấy sinh viên để cập nhật.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteData() {
        String maSV = tfMaSV.getText();

        if (maSV.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sinh viên để xóa");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa sinh viên này?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                String sql = "DELETE FROM SinhVien WHERE maSV = ?";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, maSV);
                int rowsDeleted = pstmt.executeUpdate();
                if (rowsDeleted > 0) {
                    JOptionPane.showMessageDialog(this, "Xóa thành công!");
                    displayData();
                    resetForm();
                } else {
                    JOptionPane.showMessageDialog(this, "Không tìm thấy sinh viên để xóa.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}