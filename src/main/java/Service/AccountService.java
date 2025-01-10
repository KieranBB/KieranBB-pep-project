package Service;

import Model.Account;
import DAO.AccountDAO;

//import java.util.List;

public class AccountService {
    private AccountDAO accountDAO;


    public AccountService(){
        accountDAO = new AccountDAO();
    }



    //Expected input: account object without an account_id
    //This method is responsible for verifying that the requested account is valid (valid pass, doesn't already exist, etc.)
    //Returns full account object if successful, otherwise returns null
    public Account registerAccount(Account account) {
        Account resAcct = null;

        String username = account.getUsername();

        //We're only checking here that the username contains non whitespace characters
        //We're still storing the username with whatever 15 trailing spaces the user wanted
        if (username.strip().length() == 0) return null;

        if (accountDAO.getAccountByUsername(username) != null) return null;
        
        if (account.getPassword().length() < 4) return null;

        resAcct = accountDAO.insertAccount(account);

        return resAcct;
    }

    //Expected input: account object without an account_id
    //Returns account object from database if username and password match, otherwise returns null
    public Account login (Account account) {

        String username = account.getUsername();
        String pass = account.getPassword();

        Account resAcct = accountDAO.getAccountByUsername(username);
        if (resAcct == null) return null;

        if (!resAcct.getPassword().equals(pass)) return null;

        return resAcct;

    }


    
    public Boolean checkUserExists(int account_id) {

        if (accountDAO.getAccountByAccountId(account_id) != null) return true;

        return false;

    }



    
}
