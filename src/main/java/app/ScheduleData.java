/*
*
* Yifan chen CISC181 5/19/2019
*
*/
package app;

import javafx.beans.property.*;

/**
 * A line of data in table view.
 */
public class ScheduleData {

    private final IntegerProperty paymentIndex = new SimpleIntegerProperty();
    private final StringProperty dueDate = new SimpleStringProperty();
    private final DoubleProperty payment = new SimpleDoubleProperty();
    private final DoubleProperty additionalPayment = new SimpleDoubleProperty();
    private final DoubleProperty interest = new SimpleDoubleProperty();
    private final DoubleProperty principal = new SimpleDoubleProperty();
    private final DoubleProperty balance = new SimpleDoubleProperty();

    //region getter and setter
    public int getPaymentIndex() {
        return paymentIndex.get();
    }

    public IntegerProperty paymentIndexProperty() {
        return paymentIndex;
    }

    public void setPaymentIndex(int paymentIndex) {
        this.paymentIndex.set(paymentIndex);
    }

    public String getDueDate() {
        return dueDate.get();
    }

    public StringProperty dueDateProperty() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate.set(dueDate);
    }

    public double getPayment() {
        return payment.get();
    }

    public DoubleProperty paymentProperty() {
        return payment;
    }

    public void setPayment(double payment) {
        this.payment.set(payment);
    }

    public double getAdditionalPayment() {
        return additionalPayment.get();
    }

    public DoubleProperty additionalPaymentProperty() {
        return additionalPayment;
    }

    public void setAdditionalPayment(double additionalPayment) {
        this.additionalPayment.set(additionalPayment);
    }

    public double getInterest() {
        return interest.get();
    }

    public DoubleProperty interestProperty() {
        return interest;
    }

    public void setInterest(double interest) {
        this.interest.set(interest);
    }

    public double getPrincipal() {
        return principal.get();
    }

    public DoubleProperty principalProperty() {
        return principal;
    }

    public void setPrincipal(double principal) {
        this.principal.set(principal);
    }

    public double getBalance() {
        return balance.get();
    }

    public DoubleProperty balanceProperty() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance.set(balance);
    }
    //endregion

    public ScheduleData(int index, String date, double payment,
                        double interest, double principal, double balance){
        this.paymentIndex.set(index);
        this.dueDate.set(date);
        this.payment.set(payment);
        this.interest.set(interest);
        this.principal.set(principal);
        this.balance.set(balance);
    }
}
