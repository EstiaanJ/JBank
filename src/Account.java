import java.math.BigDecimal;
import java.util.ArrayDeque;


//Debits are withdrawls
//Credits are payments into the account
public record Account(String name, long ID, ArrayDeque<BigDecimal> credits, ArrayDeque<BigDecimal> debits, boolean negBalanceAllowed, String passHash) {

    private boolean debitNotOverdrawCheck(BigDecimal amount){
        BigDecimal preBalance = this.balance();
        BigDecimal postBalance = preBalance.subtract(amount);
        return !(postBalance.signum() < 0 );
    }

    private Account unsafeDebit(BigDecimal amount){


        debits.addLast(amount);
        return this.clone();
    }

    public Account debit(BigDecimal amount){
        if(amount.signum() > 0){
            if(negBalanceAllowed){
                debits.addLast(amount);
            } else if(debitNotOverdrawCheck(amount)){
                debits.addLast(amount);
            }
        }
        return this.clone();
    }

    public Account debit(double amount){
        return this.debit(new BigDecimal(amount));
    }

    public Account credit(BigDecimal amount){
        if(amount.signum() > 0) {
            credits.addLast(amount);
        }
        return this.clone();
    }

    public Account credit(double amount){
        return this.credit(new BigDecimal(amount));
    }

    private double sumOfCreditsFast(){
        double sum = 0;
        for(BigDecimal credit: credits){
            sum += credit.doubleValue();
        }
        return sum;
    }
    private double sumOfDebitsFast() {
        double sum = 0;
        for (BigDecimal debit: debits) {
            sum += debit.doubleValue();
        }
        return sum;
    }

    private BigDecimal sumOfCredit() {
        BigDecimal sum = BigDecimal.ZERO;
        for (BigDecimal credit: credits) {
            sum = sum.add(credit);
        }
        return sum;
    }

    private BigDecimal sumOfDebits() {
        BigDecimal sum = BigDecimal.ZERO;
        for (BigDecimal debit: debits) {
            sum = sum.add(debit);
        }
        return sum;
    }


    public BigDecimal balance(){
        return sumOfCredit().subtract(sumOfDebits());
    }

    public double balanceFloatPoint(){
        return balance().doubleValue();
    }

    public double quickBalance(){
        return sumOfCreditsFast() - sumOfDebitsFast();
    }

    public Account clone(){
        return new Account(this.name, this.ID, credits.clone(), debits.clone(), this.negBalanceAllowed, this.passHash);
    }


    public static Account createAccount(long ID, String name, boolean overdrawAllowed, String passHash) {
        return new Account(name, ID, new ArrayDeque<BigDecimal>(), new ArrayDeque<BigDecimal>(), overdrawAllowed, passHash);
    }
}
