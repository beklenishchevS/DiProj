import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DiProjTelegramBot extends TelegramLongPollingBot {

    private Update update;
    private Map<Long, MessageParser> messageParserPool = new HashMap<>();

    public DiProjTelegramBot(){

    }

    public String getBotUsername() {
        return "";
    }

    public String getBotToken() {
        return "";
    }

    public void onUpdateReceived(Update update) {
        this.update = update;
        if (update.hasMessage()) {
            String text = update.getMessage().getText().toLowerCase();
            Long id = update.getMessage().getChatId();
            if (!messageParserPool.containsKey(id)){
                messageParserPool.put(id, new MessageParser(this, id));
            }
            MessageParser messageParser = messageParserPool.get(id);
            System.out.println(text);
            messageParser.putMessage(text);
        }
    }

    private void sendMessage(String text) {
        Message msg = update.getMessage();
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(msg.getChatId());
        sendMessage.setText(text);
        try {
            execute(sendMessage);
        }
        catch (TelegramApiException e)
        {
            e.printStackTrace();
        }

    }

    public void readToAnswer(String msg){
        sendMessage(msg);
    }

}
