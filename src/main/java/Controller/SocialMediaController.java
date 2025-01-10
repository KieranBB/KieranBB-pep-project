package Controller;

import io.javalin.Javalin;
import io.javalin.http.Context;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;

import java.util.List;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    AccountService accountService;
    MessageService messageService;

    public SocialMediaController() {
        this.accountService = new AccountService();
        this.messageService = new MessageService();
    }

    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("register", this::registerHandler);
        app.post("login", this::loginHandler);
        app.post("messages", this::createMessageHandler);
        app.get("messages", this::getAllMessagesHandler);
        app.get("messages/{message_id}", this::getMessageHandler);
        app.delete("messages/{message_id}", this::deleteMessageHandler);
        app.patch("messages/{message_id}", this::updateMessageHandler);
        app.get("accounts/{account_id}/messages", this::getMessagesFromUserHandler);

        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     *
    private void exampleHandler(Context context) {
        context.json("sample text");
    }
    */


    private void registerHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account addedAccount = accountService.registerAccount(account);
        if (addedAccount != null) {
            ctx.json(mapper.writeValueAsString(addedAccount));
        } else {
            ctx.status(400);
        }


    }


    private void loginHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account rsltAccount = accountService.login(account);
        if (rsltAccount != null) {
            ctx.json(mapper.writeValueAsString(rsltAccount));
        } else {
            ctx.status(401);
        }

    }


    private void createMessageHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
        Message rsltMessage = null;

        if (accountService.checkUserExists(message.getPosted_by())) {
            rsltMessage = messageService.createMessage(message);
        }
        if (rsltMessage != null) {
            ctx.json(mapper.writeValueAsString(rsltMessage));
        } else {
            ctx.status(400);
        }


    }

    private void getAllMessagesHandler(Context ctx) {
        List<Message> messages = messageService.getAllMessages();

        ctx.json(messages);

    }


    private void getMessageHandler(Context ctx) {
        int message_id = Integer.parseInt(ctx.pathParam("message_id"));
        Message message = messageService.getMessageFromMessageId(message_id);
        if (message != null) ctx.json(message);


    }


    private void deleteMessageHandler(Context ctx) {

        int message_id = Integer.parseInt(ctx.pathParam("message_id"));
        Message message = messageService.deleteMessage(message_id);
        if (message != null) ctx.json(message);
        

    }

    
    private void updateMessageHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        int message_id = Integer.parseInt(ctx.pathParam("message_id"));
        String message_text = mapper.readValue(ctx.body(), Message.class).getMessage_text();

        Message message = messageService.updateMessage(message_id, message_text);

        if (message != null) {
            ctx.json(mapper.writeValueAsString(message));
        } else {
            ctx.status(400);
        }
        
    }


    private void getMessagesFromUserHandler(Context ctx) {
        
        int account_id = Integer.parseInt(ctx.pathParam("account_id"));

        List<Message> messages = messageService.getMessagesFromAccountId(account_id);

        ctx.json(messages);
        

    }





}