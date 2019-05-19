/*
*
* Yifan chen CISC181 5/19/2019
*
*/
package app.controller;

import app.ScheduleData;
import app.ScheduleDataManager;
import app.StudentCalc;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.DoubleStringConverter;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class LoanCalcViewController implements Initializable {

    private StudentCalc SC = null;

    @FXML
    private TextField LoanAmount;
    @FXML
    private TextField InterestRate;
    @FXML
    private TextField NbrOfYears;
    @FXML
    private Label lblTotalPayments;
    @FXML
    private Label lblTotalInterest;
    @FXML
    private Label lblAdditionalPayment;
    @FXML
    private Label lblErrorMsg;
    @FXML
    private DatePicker PaymentStartDate;
    @FXML
    private TableView<ScheduleData> tableView;
    @FXML
    public TableColumn<ScheduleData, Integer> indexColumn;
    @FXML
    public TableColumn<ScheduleData, String> dateColumn;
    @FXML
    public TableColumn<ScheduleData, Double> paymentColumn;
    @FXML
    public TableColumn<ScheduleData, Double> additionalPaymentColumn;
    @FXML
    public TableColumn<ScheduleData, Double> interestColumn;
    @FXML
    public TableColumn<ScheduleData, Double> principalColumn;
    @FXML
    public TableColumn<ScheduleData, Double> balanceColumn;

    private ObservableList<ScheduleData> scheduleDataList = FXCollections.observableArrayList();

    private ScheduleDataManager scheduleDataManager;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        indexColumn.setCellValueFactory(new PropertyValueFactory<>("paymentIndex"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
        paymentColumn.setCellValueFactory(new PropertyValueFactory<>("payment"));
        additionalPaymentColumn.setCellValueFactory(new PropertyValueFactory<>("additionalPayment"));
        interestColumn.setCellValueFactory(new PropertyValueFactory<>("interest"));
        principalColumn.setCellValueFactory(new PropertyValueFactory<>("principal"));
        balanceColumn.setCellValueFactory(new PropertyValueFactory<>("balance"));
        tableView.setEditable(true);
        additionalPaymentColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        additionalPaymentColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<ScheduleData, Double>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<ScheduleData, Double> event) {
                System.out.println("row num = " + event.getTablePosition().getRow());
                (event.getTableView().getItems().get(event.getTablePosition().getRow())).setAdditionalPayment(event.getNewValue());
                scheduleDataManager.recalculate(event.getTablePosition().getRow());
                List<ScheduleData> dataList = scheduleDataManager.getScheduleDataList();
                scheduleDataList.clear();
                scheduleDataList.addAll(dataList);
                tableView.setItems(scheduleDataList);
                updateStatistics(dataList);
            }
        });
    }

    public void setMainApp(StudentCalc sc) {
        this.SC = sc;
    }

    /**
     * btnCalcLoan - Fire this event when the button clicks
     *
     * @param event
     * @version 1.0
     */
    @FXML
    private void btnCalcLoan(ActionEvent event) {
        try {
            scheduleDataList.clear();
            double loanAmount = Double.parseDouble(LoanAmount.getText());
            double interestRate = Double.parseDouble(InterestRate.getText());
            int years = Integer.parseInt(NbrOfYears.getText());
            LocalDate startDate = PaymentStartDate.getValue();
            scheduleDataManager = new ScheduleDataManager(loanAmount, interestRate, years, startDate);
            scheduleDataManager.calculate();
            List<ScheduleData> dataList = scheduleDataManager.getScheduleDataList();
            scheduleDataList.addAll(dataList);
            tableView.setItems(scheduleDataList);
            updateStatistics(dataList);
            lblErrorMsg.setText("Calculate successfully.");
        } catch (NumberFormatException ne) {
            lblErrorMsg.setText("Value conversion error, please check.");
        }
    }

    /**
     * Update statistics in the view.
     * @param dataList Datas.
     */
    private void updateStatistics(List<ScheduleData> dataList) {
        double totalPayment = dataList.stream().mapToDouble(data -> data.getInterest() + data.getPrincipal()).sum();
        lblTotalPayments.setText(String.format("%.2f", totalPayment));
        double totalInterest = dataList.stream().mapToDouble(data -> data.getInterest()).sum();
        lblTotalInterest.setText(String.format("%.2f", totalInterest));
        double totalAdditionalPayment = dataList.stream().mapToDouble(data -> data.getAdditionalPayment()).sum();
        lblAdditionalPayment.setText(String.format("%.2f", totalAdditionalPayment));
    }

}
