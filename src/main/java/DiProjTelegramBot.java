import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

public class DiProjTelegramBot extends TelegramLongPollingBot {

    private Update update;

    public DiProjTelegramBot(){

    }

    public String getBotUsername() {
        return "DiProjBot";
    }

    public String getBotToken() {
        return "5469647346:AAFhghCJDWoGmXs_VZdxpNHPO0hDWQND2Zs";
    }

    public void onUpdateReceived(Update update) {
        this.update = update;
        String text = update.getMessage().getText().toLowerCase();
        System.out.println(text);
        Record record = new Record(Double.parseDouble(text));
        if (record.save()){
            sendMessage("OK");
        } else {
            sendMessage("Wrong");
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

}
