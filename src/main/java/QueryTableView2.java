import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.sql.Connection;
import java.util.Vector;

public class QueryTableView2 extends JPanel {
    private final ConnectionProvider connectionProvider;
    private final JTextArea queryText = new JTextArea();
    private final JLabel errorText = new JLabel();
    private final JTable resultsTable = new JTable();
    private ReadOnlyQuery2 currentQuery;
    private static String currentModel;
    public final static String[] models = new String[] {"Prius","Camry", "Outback", "Legacy", "F-150"};



    public QueryTableView2(ReadOnlyQuery2[] queries, ConnectionProvider connectionProvider) {
        this.connectionProvider = connectionProvider;
        currentQuery = queries[0];

        queryText.setText(currentQuery.description);
        queryText.setEditable(false);

        this.setLayout(new BorderLayout());
        this.add(BorderLayout.NORTH, buildQuerySelectionArea(queries));
        this.add(BorderLayout.CENTER, buildResultsArea());
    }

    private JPanel buildQuerySelectionArea(ReadOnlyQuery2[] queries) {
        JPanel root = new JPanel();
        root.setLayout(new BorderLayout());

        JPanel topPane = new JPanel();

//        String[] models = new String[] {"Prius","Camry", "Outback", "Legacy", "F-150"};
        JComboBox<String> modelSelector = new JComboBox<>(models);
        modelSelector.addItemListener(this::onQuerySelected2);

        JComboBox<ReadOnlyQuery2> querySelector2 = new JComboBox<>(queries);
        querySelector2.setRenderer(new QueryListCellRenderer2());
        querySelector2.addItemListener(this::onQuerySelected);

        topPane.add(modelSelector);
        topPane.add(querySelector2);
        topPane.add(queryText);

        root.add(BorderLayout.CENTER, topPane);

        JButton runButton = new JButton("Run");
        runButton.addActionListener(this::runQuery);
        root.add(BorderLayout.SOUTH, runButton);

        return root;
    }


    private JPanel buildResultsArea() {
        JPanel resultsArea = new JPanel();
        resultsArea.setLayout(new BorderLayout());
        resultsArea.add(BorderLayout.NORTH, errorText);
        resultsArea.add(BorderLayout.CENTER, new JScrollPane(resultsTable));
        resultsTable.setFillsViewportHeight(true);
        return resultsArea;
    }

    private void onQuerySelected(ItemEvent event) {
        ReadOnlyQuery2 query = (ReadOnlyQuery2) event.getItem();
        currentQuery = query;
        queryText.setText(query.description);
        clearTable();
        clearError();
    }

    private void onQuerySelected2(ItemEvent event) {
        String model = (String) event.getItem();
        System.out.print("String model from QTV2 is: " + model);
        currentModel = model;
        clearTable();
        clearError();
    }

    public static String getCurrentModel() {
        return currentModel;
    }


    private void runQuery(ActionEvent event) {
        try {
            Connection connection = connectionProvider.getConnection();
            ReadOnlyQueryResult result = currentQuery.run(connection);
            clearError();
            DefaultTableModel tableModel = (DefaultTableModel) resultsTable.getModel();
            tableModel.setDataVector(result.data, result.columnNames);
            resultsTable.setVisible(true);
        } catch (Exception ex) {
            ex.printStackTrace();
            clearTable();
            errorText.setText(ex.getMessage());
            errorText.setVisible(true);
        }
    }

    private void clearTable() {
        ((DefaultTableModel) resultsTable.getModel()).setDataVector(new Vector<>(), new Vector<>());
        resultsTable.setVisible(false);
    }

    private void clearError() {
        errorText.setText("");
        errorText.setVisible(false);
    }

    private static class QueryListCellRenderer implements ListCellRenderer<ReadOnlyQuery2> {
        @Override
        public Component getListCellRendererComponent(JList<? extends ReadOnlyQuery2> list, ReadOnlyQuery2 value, int index, boolean isSelected, boolean cellHasFocus) {
            return new JLabel(value.label1);
        }
    }

    private static class QueryListCellRenderer2 implements ListCellRenderer<ReadOnlyQuery2> {
        @Override
        public Component getListCellRendererComponent(JList<? extends ReadOnlyQuery2> list, ReadOnlyQuery2 value, int index, boolean isSelected, boolean cellHasFocus) {
            return new JLabel(value.label2);
        }
    }
}
