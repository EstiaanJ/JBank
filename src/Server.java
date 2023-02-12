import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static spark.Spark.*;
import java.math.BigDecimal;

public class Server {
    private static Logger logger = LoggerFactory.getLogger(Server.class);
    public static void main(String[] args) {
        AccountList accountList = new AccountList();

        port(44658);

        post("/test", (req, res) -> {
            return "ya!";
        });

        post("/create-account", (req, res) -> {
            String accName = req.queryParams("acc-name");
            String passHash = req.queryParams("pass-hash");
            accountList.addAccount(accName,false,passHash);
            return "Account added";
        });

        post("/deposit", (req, res) -> {
            String accName = req.queryParams("acc-name");
            logger.info("Deposit request on account name: " + accName);
            try {
                Account account = accountList.get(accName);
                logger.debug("Account found with name: " + accName + " and ID: " + account.ID());

                BigDecimal amount = new BigDecimal(req.queryParams("amount"));
                logger.debug("Deposit amount: " + amount);

                if (authenticate(account, req.queryParams("pass-hash"))) {
                    logger.warn("Authentication failed for account: " + accName + " with ID: " + account.ID());
                    return "Authentication failed";
                } else {
                    logger.info("Authentication successful for account: " + accName + " with ID: " + account.ID());
                    account = account.credit(amount);
                    logger.debug("Proposed new balance: " + account.balance());

                    accountList.replace(account.ID(), account);
                    logger.info("Deposit successful for account: " + accName + " with ID: " + account.ID() + " and new balance: " + account.balance());
                    return "Deposit successful! New balance: " + account.balance();
                }
            } catch (NullPointerException e) {
                logger.warn("Account not found with name: " + accName, e);
                return "Account not found";
            } catch (Exception e) {
                logger.error("An error occurred while processing the deposit request", e);
                return "An error occurred while processing the deposit request";
            }
        });

        post("/withdraw", (req, res) -> {
            String accName = req.queryParams("acc-name");
            logger.info("Withdraw request on account name: " + accName);

            Account account = accountList.get(accName);
            logger.debug("Account found with name: " + accName + " and ID: " + account.ID());

            BigDecimal amount = new BigDecimal(req.queryParams("amount"));
            logger.debug("Withdrawal amount: " + amount);

            if(!authenticate(account,req.queryParams("pass-hash"))){
                logger.warn("Authentication failed for account: " + accName + " with ID: " + account.ID());
                return "Authentication failed";
            } else {
                logger.info("Authentication successful for account: " + accName + " with ID: " + account.ID());
                account = account.debit(amount);
                logger.debug("Proposed new balance: " + account.balance());

                accountList.replace(account.ID(),account);
                logger.info("Withdrawal successful for account: " + accName + " with ID: " + account.ID() + " and new balance: " + account.balance());
                return "Withdrawal successful! New balance: " + account.balance();
            }
        });

    }

    public static boolean authenticate(Account account, String passHash){
        return account.passHash().equals(passHash);
    }

}
