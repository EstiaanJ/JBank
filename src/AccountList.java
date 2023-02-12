import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

public class AccountList {
    private HashMap<Long,Account> accounts = new HashMap<>();
    private HashMap<String,Long> nameToID = new HashMap<>();


    private long generateID(){
        long potential = ThreadLocalRandom.current().nextLong(100000,Long.MAX_VALUE);
        boolean isUnique = true;
        while(isUnique){
            isUnique = !accounts.containsKey(potential);
            potential = ThreadLocalRandom.current().nextLong(100000,Long.MAX_VALUE);
        }
        return potential;
    }

    public void addAccount(String name, boolean overdrawAllowed, String passHash) {
        Long key = generateID();
        accounts.put(key,Account.createAccount(key,name,overdrawAllowed,passHash));
    }

    public Account get(Long key){
        return accounts.get(key);
    }

    private Long getID(String name){
        return nameToID.get(name);
    }

    public Account get(String name){
        return accounts.get(getID(name));
    }

    public void replace(Long key, Account newAccount){
        accounts.replace(key,newAccount);
    }
}
