import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;

public class Main extends JFrame {

    // Hardcoded 2D Array para sa existing Tricycle data natin (walang file handling, primitives lang)
    // Format: {Type, BodyNo, PlateNo, BaseFare, HasSticker, ExtraParam(Seats o kaya ZoneFee)}
    private String[][] initialTricycleData = {
            {"REGULAR", "TODA-001", "ABC-1234", "15.0", "true", "4"},
            {"REGULAR", "TODA-002", "XYZ-5678", "15.0", "false", "5"},
            {"SPECIAL", "TODA-003", "DEF-9012", "20.0", "true", "40.0"},
            {"SPECIAL", "TODA-004", "GHI-3456", "25.0", "true", "50.0"}
    };

    // Hardcoded 2D Array para sa existing TODA Drivers
    // Format: {LicenseNo, FullName, YearsExp, HasValidLicense}
    private String[][] initialDriverData = {
            {"N01-24-100231", "Dellomas, Carl Aksel D", "5", "true"},
            {"N02-21-445120", "Cabigao, Clark Kent C", "0", "true"},
            {"N03-19-882104", "Agpalasin, Felicity Ann G", "8", "true"},
            {"N04-18-339102", "Magtalas, Itchmond Sean R", "4", "false"},
            {"N05-22-774190", "Serfa Juan, Erica Gwyneth T", "3", "true"},
            {"N06-20-619283", "Tiongson, Jake Arwin G", "6", "true"}
    };

    // Simpleng object arrays lang gamit natin (hindi ArrayList) para primitive approach talaga
    private Tricycle[] tricycleList = new Tricycle[50];
    private int tricycleCount = 0;

    private Driver[] driverList = new Driver[50];
    private int driverCount = 0;

    // Swing components para sa Tab 1 (Tricycle)
    private JTextField txtBodyNo, txtPlateNo, txtBaseFare, txtExtraField;
    private JComboBox<String> cmbType;
    private JCheckBox chkSticker;
    private JLabel lblExtraField, lblTotalCount;
    private DefaultTableModel tricycleTableModel;
    private JTable tricycleTable;

    // Swing components para sa Tab 2 (Drivers & Inspection)
    private JTextField txtLicenseNo, txtFullName, txtExperience;
    private JCheckBox chkValidLicense;
    private DefaultTableModel driverTableModel;
    private JTable driverTable;
    private JTextArea txtInspectionLog;

    public Main() {
        setTitle("Municipal Tricycle TODA Franchise & Dispatch System");
        setSize(900, 620);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Layout Manager 1: BorderLayout para sa main window
        setLayout(new BorderLayout());

        // I-load muna yung laman ng 2D array papunta sa object arrays natin
        loadHardcodedData();

        // TabbedPane para malinis tignan yung hati ng Tricycle at Drivers
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("1. Tricycle Units (Polymorphism)", createTricycleTab());
        tabbedPane.addTab("2. TODA Drivers & Inspection (Interface)", createDriverTab());

        add(tabbedPane, BorderLayout.CENTER);

        // I-display agad sa JTable yung na-load na data
        refreshTricycleTable();
        refreshDriverTable();
    }

    // Method na nagbabasa nung 2D array tapos ginagawang objects
    private void loadHardcodedData() {
        for (int i = 0; i < initialTricycleData.length; i++) {
            String type = initialTricycleData[i][0];
            String bodyNo = initialTricycleData[i][1];
            String plateNo = initialTricycleData[i][2];
            double fare = Double.parseDouble(initialTricycleData[i][3]);
            boolean sticker = Boolean.parseBoolean(initialTricycleData[i][4]);

            // Kung REGULAR yung nasa column 0, RegularTricycle ang gagawin. Pag hindi, SpecialTricycle.
            if (type.equals("REGULAR")) {
                int seats = Integer.parseInt(initialTricycleData[i][5]);
                tricycleList[tricycleCount++] = new RegularTricycle(bodyNo, plateNo, fare, sticker, seats);
            } else {
                double zoneFee = Double.parseDouble(initialTricycleData[i][5]);
                tricycleList[tricycleCount++] = new SpecialTricycle(bodyNo, plateNo, fare, sticker, zoneFee);
            }
        }

        for (int i = 0; i < initialDriverData.length; i++) {
            String lic = initialDriverData[i][0];
            String name = initialDriverData[i][1];
            int exp = Integer.parseInt(initialDriverData[i][2]);
            boolean valid = Boolean.parseBoolean(initialDriverData[i][3]);
            driverList[driverCount++] = new Driver(lic, name, exp, valid);
        }
    }

    // Setup para sa Unang Tab
    private JPanel createTricycleTab() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));

        // Layout Manager 2: GridLayout (4 rows, 4 columns) para sa mga textfields
        JPanel formPanel = new JPanel(new GridLayout(4, 4, 8, 8));
        formPanel.setBorder(BorderFactory.createTitledBorder("Register New Tricycle Unit"));

        formPanel.add(new JLabel("Dispatch Type:"));
        cmbType = new JComboBox<>(new String[]{"Regular Line (Shared)", "Special Trip (Arkila)"});
        formPanel.add(cmbType);

        formPanel.add(new JLabel("Body Number (e.g., TODA-005):"));
        txtBodyNo = new JTextField();
        formPanel.add(txtBodyNo);

        formPanel.add(new JLabel("Plate Number:"));
        txtPlateNo = new JTextField();
        formPanel.add(txtPlateNo);

        formPanel.add(new JLabel("Base Fare (PHP):"));
        txtBaseFare = new JTextField("15.0");
        formPanel.add(txtBaseFare);

        lblExtraField = new JLabel("Max Seats (1-6):");
        formPanel.add(lblExtraField);
        txtExtraField = new JTextField("4");
        formPanel.add(txtExtraField);

        formPanel.add(new JLabel("Franchise Status:"));
        chkSticker = new JCheckBox("Has Valid Sticker", true);
        formPanel.add(chkSticker);

        lblTotalCount = new JLabel("Total Units Registered: " + Tricycle.getTotalRegistered());
        formPanel.add(lblTotalCount);
        formPanel.add(new JLabel("")); // pang-fill lang ng grid cell

        mainPanel.add(formPanel, BorderLayout.NORTH);

        // Table sa gitna
        String[] columns = {"Body No.", "Plate No.", "Dispatch Type", "Base Fare", "Trip Collection", "Compliance Status"};
        tricycleTableModel = new DefaultTableModel(columns, 0);
        tricycleTable = new JTable(tricycleTableModel);
        mainPanel.add(new JScrollPane(tricycleTable), BorderLayout.CENTER);

        // Layout Manager 3: FlowLayout para sa mga buttons sa baba
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        JButton btnAdd = new JButton("Add Tricycle");
        JButton btnUpdateFare = new JButton("Update Selected Base Fare");
        JButton btnDelete = new JButton("Delete Selected");

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnUpdateFare);
        buttonPanel.add(btnDelete);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Event 1: Pag nagpalit ng type sa dropdown, magpapalit din yung label (Max Seats vs Zone Fee)
        cmbType.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (cmbType.getSelectedIndex() == 0) {
                    lblExtraField.setText("Max Seats (1-6):");
                    txtExtraField.setText("4");
                } else {
                    lblExtraField.setText("Zone Fee (0-1000 PHP):");
                    txtExtraField.setText("30.0");
                }
            }
        });

        // Event 2: Pag-click ng Add Tricycle
        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (tricycleCount >= tricycleList.length) {
                    JOptionPane.showMessageDialog(Main.this, "Array is full!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                try {
                    String body = txtBodyNo.getText();
                    String plate = txtPlateNo.getText();
                    double fare = Double.parseDouble(txtBaseFare.getText());
                    boolean sticker = chkSticker.isSelected();

                    Tricycle newUnit;
                    if (cmbType.getSelectedIndex() == 0) {
                        int seats = Integer.parseInt(txtExtraField.getText());
                        newUnit = new RegularTricycle(body, plate, fare, sticker, seats);
                    } else {
                        double fee = Double.parseDouble(txtExtraField.getText());
                        newUnit = new SpecialTricycle(body, plate, fare, sticker, fee);
                    }

                    tricycleList[tricycleCount++] = newUnit;
                    refreshTricycleTable();

                    txtBodyNo.setText("");
                    txtPlateNo.setText("");
                    JOptionPane.showMessageDialog(Main.this,
                            "Tricycle Added!\nComputed Trip Collection: PHP " + newUnit.calculateTripCollection(),
                            "Success", JOptionPane.INFORMATION_MESSAGE);

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(Main.this,
                            "Please enter valid numbers for Base Fare and Seats/Zone Fee.",
                            "Input Error", JOptionPane.ERROR_MESSAGE);
                } catch (IllegalArgumentException ex) {
                    // Dito nasasalo yung validation errors galing sa setters natin
                    JOptionPane.showMessageDialog(Main.this,
                            ex.getMessage(),
                            "Validation Error", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        // Event 3: Pag-update ng Base Fare gamit ang JOptionPane.showInputDialog
        btnUpdateFare.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = tricycleTable.getSelectedRow();
                if (row == -1) {
                    JOptionPane.showMessageDialog(Main.this, "Select a tricycle row from the table first.", "Warning", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                Tricycle selected = tricycleList[row];
                String input = JOptionPane.showInputDialog(Main.this,
                        "Enter new Base Fare for " + selected.getBodyNumber() + ":",
                        selected.getBaseFare());

                if (input != null) {
                    try {
                        double newFare = Double.parseDouble(input);
                        selected.setBaseFare(newFare);
                        refreshTricycleTable();
                        JOptionPane.showMessageDialog(Main.this, "Base fare updated!", "Updated", JOptionPane.INFORMATION_MESSAGE);
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(Main.this, "Invalid number format.", "Error", JOptionPane.ERROR_MESSAGE);
                    } catch (IllegalArgumentException ex) {
                        JOptionPane.showMessageDialog(Main.this, ex.getMessage(), "Validation Error", JOptionPane.WARNING_MESSAGE);
                    }
                }
            }
        });

        // Event 4: Pag-delete ng record na may kasamang confirmation dialog
        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = tricycleTable.getSelectedRow();
                if (row == -1) {
                    JOptionPane.showMessageDialog(Main.this, "Select a tricycle row to delete.", "Warning", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                int confirm = JOptionPane.showConfirmDialog(Main.this,
                        "Are you sure you want to delete unit " + tricycleList[row].getBodyNumber() + "?",
                        "Confirm Deletion", JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    // Dahil array gamit natin, i-usod lang natin pakaliwa yung mga elements
                    for (int i = row; i < tricycleCount - 1; i++) {
                        tricycleList[i] = tricycleList[i + 1];
                    }
                    tricycleList[tricycleCount - 1] = null;
                    tricycleCount--;
                    Tricycle.setTotalRegistered(Tricycle.getTotalRegistered() - 1);
                    refreshTricycleTable();
                }
            }
        });

        return mainPanel;
    }

    // Setup para sa Pangalawang Tab
    private JPanel createDriverTab() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));

        JPanel formPanel = new JPanel(new GridLayout(3, 4, 8, 8));
        formPanel.setBorder(BorderFactory.createTitledBorder("Register TODA Driver"));

        formPanel.add(new JLabel("License No. (min 5 chars):"));
        txtLicenseNo = new JTextField();
        formPanel.add(txtLicenseNo);

        formPanel.add(new JLabel("Driver Full Name:"));
        txtFullName = new JTextField();
        formPanel.add(txtFullName);

        formPanel.add(new JLabel("Years Experience (0-60):"));
        txtExperience = new JTextField("2");
        formPanel.add(txtExperience);

        formPanel.add(new JLabel("License Status:"));
        chkValidLicense = new JCheckBox("Has Unexpired License", true);
        formPanel.add(chkValidLicense);

        JButton btnAddDriver = new JButton("Add Driver");
        JButton btnRunInspection = new JButton("Run Full TODA Inspection (Interface)");
        formPanel.add(btnAddDriver);
        formPanel.add(btnRunInspection);

        mainPanel.add(formPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridLayout(2, 1, 5, 5));

        String[] driverCols = {"License No.", "Full Name", "Experience (Yrs)", "Valid License", "Inspection Result"};
        driverTableModel = new DefaultTableModel(driverCols, 0);
        driverTable = new JTable(driverTableModel);
        centerPanel.add(new JScrollPane(driverTable));

        txtInspectionLog = new JTextArea();
        txtInspectionLog.setEditable(false);
        txtInspectionLog.setBorder(BorderFactory.createTitledBorder("Combined Inspection Report (Tricycles & Drivers implementing Inspectable)"));
        centerPanel.add(new JScrollPane(txtInspectionLog));

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // Event 5: Pag-add ng bagong Driver
        btnAddDriver.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (driverCount >= driverList.length) {
                    JOptionPane.showMessageDialog(Main.this, "Driver array is full!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                try {
                    String lic = txtLicenseNo.getText();
                    String name = txtFullName.getText();
                    int exp = Integer.parseInt(txtExperience.getText());
                    boolean valid = chkValidLicense.isSelected();

                    Driver d = new Driver(lic, name, exp, valid);
                    driverList[driverCount++] = d;
                    refreshDriverTable();

                    txtLicenseNo.setText("");
                    txtFullName.setText("");
                    JOptionPane.showMessageDialog(Main.this, "Driver registered successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(Main.this, "Experience must be a valid whole number.", "Input Error", JOptionPane.ERROR_MESSAGE);
                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(Main.this, ex.getMessage(), "Validation Error", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        // Event 6: Pagsasama nung Tricycle at Driver sa iisang Inspectable[] array
        btnRunInspection.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StringBuilder sb = new StringBuilder();
                sb.append("=== MUNICIPAL TODA COMPLIANCE AUDIT ===\n\n");

                int passedCount = 0;
                int totalItems = tricycleCount + driverCount;

                // Dahil parehong Inspectable yung Tricycle at Driver, pwede sila sa iisang array
                Inspectable[] allInspectables = new Inspectable[totalItems];
                int index = 0;
                for (int i = 0; i < tricycleCount; i++) {
                    allInspectables[index++] = tricycleList[i];
                }
                for (int i = 0; i < driverCount; i++) {
                    allInspectables[index++] = driverList[i];
                }

                for (int i = 0; i < allInspectables.length; i++) {
                    Inspectable item = allInspectables[i];
                    if (item.isCompliant()) {
                        passedCount++;
                    }
                    if (item instanceof Tricycle) {
                        Tricycle t = (Tricycle) item;
                        sb.append("[VEHICLE] ").append(t.getBodyNumber()).append(" -> ").append(item.checkCompliance()).append("\n");
                    } else if (item instanceof Driver) {
                        Driver d = (Driver) item;
                        sb.append("[DRIVER]  ").append(d.getFullName()).append(" -> ").append(item.checkCompliance()).append("\n");
                    }
                }

                sb.append("\nSUMMARY: ").append(passedCount).append(" out of ").append(totalItems).append(" passed inspection.");
                txtInspectionLog.setText(sb.toString());
            }
        });

        return mainPanel;
    }

    // Dito makikita yung Polymorphism sa JTable:
    // Kahit Tricycle[] lang yung array natin, alam ni Java kung pang-Regular o pang-Special yung computation.
    private void refreshTricycleTable() {
        tricycleTableModel.setRowCount(0);
        for (int i = 0; i < tricycleCount; i++) {
            Tricycle t = tricycleList[i];
            Object[] row = {
                    t.getBodyNumber(),
                    t.getPlateNumber(),
                    t.getDispatchType(),
                    "PHP " + t.getBaseFare(),
                    "PHP " + t.calculateTripCollection(),
                    t.checkCompliance()
            };
            tricycleTableModel.addRow(row);
        }
        lblTotalCount.setText("Total Units Registered: " + Tricycle.getTotalRegistered());
    }

    private void refreshDriverTable() {
        driverTableModel.setRowCount(0);
        for (int i = 0; i < driverCount; i++) {
            Driver d = driverList[i];
            Object[] row = {
                    d.getLicenseNo(),
                    d.getFullName(),
                    d.getYearsExperience(),
                    d.getHasValidLicense() ? "Yes" : "No",
                    d.checkCompliance()
            };
            driverTableModel.addRow(row);
        }
    }

    void main() {
        new Main().setVisible(true);
    }
}