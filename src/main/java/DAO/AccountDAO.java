package DAO;

import Model.Account;
import Util.ConnectionUtil;

import java.sql.*;
//import java.util.ArrayList;
//import java.util.List;

public class AccountDAO {


    



    //Expected input: account object without account_id
    //Most input verification assumed to be done in service object (valid password, etc.)
    //Returns copy of account object from table if successful, otherwise returns null
    public Account insertAccount(Account account){
        Connection connection = ConnectionUtil.getConnection();
        try {

            String sql = "insert into account (username, password) values (?, ?);";
            PreparedStatement pStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            pStatement.setString(1, account.getUsername());
            pStatement.setString(2, account.getPassword());

            pStatement.executeUpdate();
            ResultSet pkResultSet = pStatement.getGeneratedKeys();
            if(pkResultSet.next()){
                int generated_account_id = pkResultSet.getInt(1);
                return new Account(generated_account_id, account.getUsername(), account.getPassword());
            }

        } catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }


    
    //Expected input: username as string
    //Returns associated account object if it exists
    public Account getAccountByUsername(String username){
        Connection connection = ConnectionUtil.getConnection();
        try{
            String sql = "select * from account where username=?;";
            PreparedStatement pStatement = connection.prepareStatement(sql);

            pStatement.setString(1,username);

            ResultSet rs = pStatement.executeQuery();
            

            while(rs.next()){
                Account account = new Account(rs.getInt("account_id"),
                    rs.getString("username"),
                    rs.getString("password"));
                return account;
            }

        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;

    }



    public Account getAccountByAccountId(int account_id) {
        Connection connection = ConnectionUtil.getConnection();
        try{
            String sql = "select * from account where account_id=?;";
            PreparedStatement pStatement = connection.prepareStatement(sql);

            pStatement.setInt(1,account_id);

            ResultSet rs = pStatement.executeQuery();

            while(rs.next()){
                Account account = new Account(rs.getInt("account_id"),
                    rs.getString("username"),
                    rs.getString("password"));
                return account;
            }

        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;

    }


    
}
