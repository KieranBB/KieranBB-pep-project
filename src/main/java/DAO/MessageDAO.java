package DAO;

import Util.ConnectionUtil;
import Model.Message;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MessageDAO {


    public List<Message> getAllMessages() {
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try{

            String sql = "select * from message;";
            PreparedStatement pStatement = connection.prepareStatement(sql);
            ResultSet rs = pStatement.executeQuery();
            while(rs.next()){
                Message message = new Message(
                        rs.getInt("message_id"),
                        rs.getInt("posted_by"),
                        rs.getString("message_text"),
                        rs.getLong("time_posted_epoch")
                    );
                messages.add(message);
            }

        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }

        return messages;

    }


    //Assume inherit epoch time from initial json object taken by controller
    //Returns Message object if successful, otherwise returns null
    public Message insertMessage(Message message) {
        Connection connection = ConnectionUtil.getConnection();

        try {

            String sql = "insert into message (posted_by, message_text, time_posted_epoch) values (?, ?, ?);";
            PreparedStatement pStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            pStatement.setInt(1,message.getPosted_by());
            pStatement.setString(2, message.getMessage_text());
            pStatement.setLong(3, message.getTime_posted_epoch());

            pStatement.executeUpdate();

            ResultSet pkResultSet = pStatement.getGeneratedKeys();
            if (pkResultSet.next()) {
                int generated_message_id = pkResultSet.getInt(1);
                return new Message(
                    generated_message_id,
                    message.getPosted_by(),
                    message.getMessage_text(),
                    message.getTime_posted_epoch());
            }


        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }


        return null;
    }



    public Message getMessageFromMessageId(int message_id){
        Connection connection = ConnectionUtil.getConnection();

        try {

            String sql = "select * from message where message_id = ?;";
            PreparedStatement pStatement = connection.prepareStatement(sql);

            pStatement.setInt(1,message_id);

            ResultSet rs = pStatement.executeQuery();

            while(rs.next()) {
                Message message = new Message(
                    rs.getInt("message_id"),
                    rs.getInt("posted_by"),
                    rs.getString("message_text"),
                    rs.getLong("time_posted_epoch"));
                return message;
            }
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;

    }

    
    public void deleteMessage(int message_id){
        Connection connection = ConnectionUtil.getConnection();

        try {

            String sql = "delete from message where message_id = ?;";
            PreparedStatement pStatement = connection.prepareStatement(sql);

            pStatement.setInt(1,message_id);

            int rAffected = pStatement.executeUpdate();

            if (rAffected > 1) System.out.println("Oops");
            //if (rAffected != 0) return true;

        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }

        //return false;
    }


    //return true if successful
    public Boolean updateMessage(int message_id, String message_text){
        Connection connection = ConnectionUtil.getConnection();

        try {

            String sql = "update message set message_text = ? where message_id = ?;";
            PreparedStatement pStatement = connection.prepareStatement(sql);

            pStatement.setString(1,message_text);
            pStatement.setInt(2, message_id);

            pStatement.executeUpdate();

            int rAffected = pStatement.executeUpdate();
            if (rAffected != 0) return true;


        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }

        return false;
    }


    public List<Message> getMessagesFromAccountId(int account_id) {
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try{

            String sql = "select * from message where posted_by = ?;";
            PreparedStatement pStatement = connection.prepareStatement(sql);

            pStatement.setInt(1, account_id);

            ResultSet rs = pStatement.executeQuery();
            while(rs.next()){
                Message message = new Message(
                        rs.getInt("message_id"),
                        rs.getInt("posted_by"),
                        rs.getString("message_text"),
                        rs.getLong("time_posted_epoch")
                    );
                messages.add(message);
            }

        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }

        return messages;



    }






}
