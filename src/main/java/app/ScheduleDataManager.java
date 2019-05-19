/*
*
* Yifan chen CISC181 5/19/2019
*
*/
package app;

import org.apache.poi.ss.formula.functions.FinanceLib;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Used to calculate schedule data.
 */
public class ScheduleDataManager {
    private double loanAmount;
    private double interestRate;
    private int years;
    private LocalDate firstPaymentDate;
    //-----
    private double additionalPayment;
    private double totalPayments;
    private double totalInterest;

    //region init

    public double getAdditionalPayment() {
        return additionalPayment;
    }

    public double getTotalPayments() {
        return totalPayments;
    }

    public double getTotalInterest() {
        return totalInterest;
    }

    public ScheduleDataManager(double loanAmount, double interestRate, int years, LocalDate firstPaymentDate) {
        this.loanAmount = loanAmount;
        this.interestRate = interestRate;
        this.years = years;
        this.firstPaymentDate = firstPaymentDate;
    }

    //endregion

    private ArrayList<ScheduleData> scheduleDataList = new ArrayList<>();

    public ArrayList<ScheduleData> getScheduleDataList() {
        return scheduleDataList;
    }

    /**
     * When click the calculate button, execute this function.
     * This function assumes that there is no additional payment.
     */
    public void calculate() {
        double amount = loanAmount;
        LocalDate dueDate = firstPaymentDate;
        double ratePerPeriod = interestRate / 12;// per month
        int Nper = years * 12;
        int index = 1;
        double PMT = roundNumber(Math.abs(FinanceLib.pmt(ratePerPeriod, Nper, loanAmount, 0, false)));
        while (amount > 0) {
            double interest = roundNumber(amount * ratePerPeriod);
            double allNeed = roundNumber((1 + ratePerPeriod) * amount);
            double pmt = PMT > allNeed ? allNeed : PMT;
            double principal = roundNumber(pmt - interest);
            double balance = roundNumber(amount - principal);
            ScheduleData data = new ScheduleData(index, dueDate.toString(), pmt,
                    interest, principal, balance);
            scheduleDataList.add(data);
            amount = balance;
            index++;
            dueDate = dueDate.plusMonths(1);
        }
    }

    /**
     * When additional payment in the tableview is changed, execute this function.
     * Like the excel, change the following schedule.
     * @param rowNumber The row number of the changed line in the tableview.
     */
    public void recalculate(int rowNumber) {
        ArrayList<ScheduleData> list = new ArrayList<>();
        double amount = loanAmount;
        LocalDate dueDate = firstPaymentDate;
        // Keep the line datas before the changed one
        for (int i = 0; i < rowNumber; i++) {
            ScheduleData scheduleData = scheduleDataList.get(i);
            list.add(scheduleData);
            amount = scheduleData.getBalance();
            dueDate = LocalDate.parse(scheduleData.getDueDate());
        }
        // Calculate the one whose additional payment has changed
        ScheduleData dataTemp = scheduleDataList.get(rowNumber);
        double principalTemp = dataTemp.getPayment() + dataTemp.getAdditionalPayment() - dataTemp.getInterest();
        dataTemp.setPrincipal(principalTemp);
        dataTemp.setBalance(amount - principalTemp);
        list.add(dataTemp);
        amount -= principalTemp;
        dueDate.plusMonths(1);
        // Prepare variables
        double ratePerPeriod = interestRate / 12;// per month
        int Nper = years * 12;
        int index = rowNumber + 2;
        double PMT = roundNumber(Math.abs(FinanceLib.pmt(ratePerPeriod, Nper, loanAmount, 0, false)));
        // Calculate the following schedules
        while (amount > 0) {
            double interest = roundNumber(amount * ratePerPeriod);
            double allNeed = roundNumber((1 + ratePerPeriod) * amount);
            double pmt = PMT > allNeed ? allNeed : PMT;
            double principal = roundNumber(pmt - interest);
            double balance = roundNumber(amount - principal);
            ScheduleData data = new ScheduleData(index, dueDate.toString(), pmt,
                    interest, principal, balance);
            list.add(data);
            amount = balance;
            index++;
            dueDate = dueDate.plusMonths(1);
        }
        scheduleDataList.clear();
        scheduleDataList.addAll(list);
    }

    /**
     * Keep two decimal places.
     *
     * @param f double value.
     * @return double value f with two decimal places.
     */
    private double roundNumber(double f) {
        BigDecimal bg = new BigDecimal(f);
        double f1 = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        return f1;
    }
}
