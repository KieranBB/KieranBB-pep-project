package Service;

import Model.Message;
import DAO.MessageDAO;

import java.util.List;


public class MessageService {
    private MessageDAO messageDAO;


    public MessageService() {
        messageDAO = new MessageDAO();
    }


    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }


    public Message createMessage(Message message) {
        


        String message_text = message.getMessage_text();
        if (message_text.strip().length() == 0) return null;
        if (message_text.length() > 255) return null; // changed to > instead of >= to be consistent with update message length limit

        Message resMsg = messageDAO.insertMessage(message);

        return resMsg;

    }


    public Message getMessageFromMessageId(int message_id) {

        return messageDAO.getMessageFromMessageId(message_id);

    }


    public Message deleteMessage(int message_id) {

        Message resMsg = messageDAO.getMessageFromMessageId(message_id);

        if (resMsg == null) return null;

        messageDAO.deleteMessage(message_id);

        return resMsg;
    }


    //We don't update epoch time. In theory front end could give us new epoch time, or we could
    //generate it here, but the preferred behavior is ambiguous, so we leave original epoch time.
    public Message updateMessage(int message_id, String message_text) {

        Message resMsg  = null;

        if (messageDAO.getMessageFromMessageId(message_id) == null) return null;

        if (message_text.strip().length() == 0) return null;
        if (message_text.length() > 255) return null;

        Boolean updSuccess = messageDAO.updateMessage(message_id, message_text);

        if (updSuccess) resMsg = messageDAO.getMessageFromMessageId(message_id);

        return resMsg;


    }


    public List<Message> getMessagesFromAccountId(int account_id) {

        return messageDAO.getMessagesFromAccountId(account_id);

    }





    
}
