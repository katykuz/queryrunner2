import javax.swing.*;

public class MainWindow {
    private final JFrame window;

    public MainWindow(ConnectionProvider connectionProvider) {
        // TODO: customize window with your project or team's name, add styling as you like
        window = new JFrame("Tire Database - Group 2");
        window.setSize(800, 600);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JTabbedPane tabs = new JTabbedPane();
        // TODO: replace with your own queries
        ReadOnlyQuery[] queries = new ReadOnlyQuery[] {
                new ReadOnlyQuery("Display Customers", "SELECT * FROM customer",
                        "Selects all customers"),
                new ReadOnlyQuery("Display Employees", "SELECT * FROM employee",
                        "Selects all Employees"),
                new ReadOnlyQuery("Most Popular Tires",
                        "SELECT tire.model as 'Tire Model', COUNT(orders.id) " +
                                "AS `Number of Orders` \n" +
                                "FROM orders \n" +
                                "join tire on orders.tire_id = tire.id\n" +
                                "GROUP BY tire.model\n" +
                                "ORDER BY `Number of Orders` DESC\n" +
                                "LIMIT 5;",
                        "Shows the most commonly purchased Tires"),
                new ReadOnlyQuery("Tires in Stock By Model Name",
                        "select tire.model as 'Tire Model', " +
                                "sum(tire_quantity) as 'Number in stock' from " +
                                "inventory join tire on tire.id = " +
                                "inventory.tire_id group by tire.model",
                        "List of tire models and number of each " +
                                "in Stock")
        };
        QueryTableView queryTableView = new QueryTableView(queries, connectionProvider);
        tabs.add("All Queries", queryTableView);

        /////////////////////// REVENUE PROCEDURE //////////////////////////

        ReadOnlyQuery[] procedure_revenue = new ReadOnlyQuery[] {
                new ReadOnlyQuery("Year to Date",
                        "CALL `tb_cpsc5021_22_group2`." +
                            "`annual_rev_tally`(current_timestamp())",
                        "Annual Revenue up to today"),
                new ReadOnlyQuery("Quarter 1",
                        "CALL `tb_cpsc5021_22_group2`." +
                                "`annual_rev_q1`()",
                        "Quarter 1 Revenue"),
                new ReadOnlyQuery("Quarter 2",
                        "CALL `tb_cpsc5021_22_group2`." +
                                "`annual_rev_q2`()",
                        "Quarter 2 Revenue"),
                new ReadOnlyQuery("Quarter 3",
                        "CALL `tb_cpsc5021_22_group2`." +
                                "`annual_rev_tally`('2022-07-01','2022-09-30')",
                        "Quarter 3 Revenue"),
                new ReadOnlyQuery("Quarter 4",
                        "CALL `tb_cpsc5021_22_group2`." +
                                "`annual_rev_tally`('2022-10-01','current_timestamp()')",
                        "Quarter 4 Revenue")

        };

        QueryTableView queryTableView_Revenue = new QueryTableView(procedure_revenue, connectionProvider);
        tabs.add("Revenue Report", queryTableView_Revenue);


        /////////////////// TIRES BY COST AND CAR MODEL  /////////////////////////

        String currentModel = QueryTableView2.models[0];
        String currentModel1 = QueryTableView2.models[1];
        //System.out.print("currentModel string is" + currentModel);

        ReadOnlyQuery2[] procedure_tiresByCostByModel = new ReadOnlyQuery2[] {
                new ReadOnlyQuery2("p", "250",
                        "CALL `tb_cpsc5021_22_group2`." +
                                "`winter_tires_by_cost&car`(250, '" + currentModel + "')",
                        "Tires under $250 for a Prius"),
                new ReadOnlyQuery2("Prius", "500",
                        "CALL `tb_cpsc5021_22_group2`." +
                                "`winter_tires_by_cost&car`(500, '" + QueryTableView2.models[1] + "')",
                        "Tires under $500 for a Prius"),
        };

        QueryTableView2 queryTableView_tireByCostByModel = new QueryTableView2(procedure_tiresByCostByModel, connectionProvider);
        tabs.add("Tires for Car Model by Cost", queryTableView_tireByCostByModel);



        //tb_cpsc5021_22_group2.winter_tires_by_cost&car(500, 'Prius');

        ReadOnlyQuery[] update_cost = new ReadOnlyQuery[] {
                new ReadOnlyQuery("Winter Tires 10% OFF",
                        "update tire\n" +
                                "set cost = ((0.9)*tire.cost)\n" +
                                "where season_id = '1';",
                        "Annual Winter Tire Sale")};
                QueryTableView queryTableView_Update = new QueryTableView(update_cost, connectionProvider);
        tabs.add("Set Tires on Sale", queryTableView_Update);



        window.setContentPane(tabs);
    }

    public void show() {
        window.setVisible(true);
    }
}
