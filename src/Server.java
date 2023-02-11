import static spark.Spark.*;
import java.math.BigDecimal;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Server {

    public static void main(String[] args) {
        AccountList accountList = new AccountList();

        //port(25565);

        post("/test", (req, res) -> {
            return "ya!";
        });

        post("/add-account", (req, res) -> {
            String accName = req.queryParams("acc-name");
            accountList.addAccount(accName,false);
            return "Account added";
        });

        post("/deposit", (req, res) -> {
            BigDecimal amount = new BigDecimal(req.queryParams("amount"));
            System.out.println("Deposit Amount: " + amount.doubleValue());
            Long ID = Long.parseLong(req.queryParams("ID"));
            System.out.println(ID);
            Account account = accountList.get(ID);
            account = account.credit(amount);
            accountList.replace(ID,account);
            return "Deposit successful! New balance: " + account.balance();
        });

        post("/credit", (req, res) -> {
            BigDecimal amount = new BigDecimal(req.queryParams("amount"));
            Long ID = Long.parseLong(req.queryParams("ID"));
            Account account = accountList.get(ID);
            account = account.debit(amount);
            accountList.replace(ID,account);
            return "Credit successful! New balance: " + account.balance();
        });
    }
}
