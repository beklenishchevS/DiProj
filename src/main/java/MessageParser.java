import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

public class MessageParser {
    private final static String INITIAL_RESPONSE = ConstantResponses.INITIAL_RESPONSE;
    private final Long userId;

    private final DiProjTelegramBot owner;

    private final Map<String, Wrap> funcs = new HashMap<>();
    private final GetterDB getterDB;

    private Record record;

    public MessageParser(DiProjTelegramBot owner, Long userId){
        this.owner = owner;
        setStartFunc();
        setHelpFunc();
        setOKFunc();
        setNewFunc();
        setAdding();
        setWrong();
        setGetter();
        this.userId = userId;
        getterDB = new GetterDB(userId);
        record = new Record(userId);
    }
    private void setWrong(){
        funcs.put("ERROR", message -> owner.readToAnswer(ConstantResponses.WRONG));
    }

    private void setStartFunc(){
        funcs.put("/start", message -> {
            record = new Record(userId);
            owner.readToAnswer(INITIAL_RESPONSE);
        });
    }

    private void setHelpFunc(){
        funcs.put("/help", message -> owner.readToAnswer(INITIAL_RESPONSE));
    }

    private void setOKFunc(){
        Wrap wrap = message -> {
            if (record == null){
                return;
            }
            boolean isSaved = record.save();
            if (isSaved){
                owner.readToAnswer(ConstantResponses.SAVED);
            } else {
                owner.readToAnswer(ConstantResponses.NOT_SAVED);
            }
            record = new Record(userId);
        };
        funcs.put("ok", wrap);
        funcs.put("ок", wrap);
    }

    private void setNewFunc(){
        Wrap wrap = message -> {
            record = new Record(userId);
            owner.readToAnswer("Делаю новую запись!");
        };

        funcs.put("/new", wrap);
        funcs.put("новая", wrap);
    }

    private void setAdding(){
        Wrap wrapSugar = message -> {
            record.setSugar(parseValue(message));
            owner.readToAnswer(ConstantResponses.ANYTHING_ELSE);
        };
        funcs.put("сахар", wrapSugar);


        Wrap wrapXE = message -> {
            record.setXE(parseValue(message));
            if (record.getCoefficient() != null){
                double shortInsulin = record.getXE() * record.getCoefficient();
                owner.readToAnswer("Я посчитал, что необходимо поставить "
                        + shortInsulin
                        + " короткого без учета сахара.");
            }
            owner.readToAnswer(ConstantResponses.ANYTHING_ELSE);
        };
        funcs.put("съел", wrapXE);
        funcs.put("хе", wrapXE);
        funcs.put("съела", wrapXE);


        Wrap wrapLong = message -> {
            record.setLongIns(parseValue(message));
            owner.readToAnswer(ConstantResponses.ANYTHING_ELSE);
        };
        funcs.put("длинный", wrapLong);


        Wrap wrapCoefficient = message -> {
            record.setCoefficient(parseValue(message));
            if (record.getXE() != null) {
                double shortInsulin = record.getXE() * record.getCoefficient();
                owner.readToAnswer("Я посчитал, что необходимо поставить "
                        + shortInsulin
                        + " короткого без учета сахара.");
            }
            owner.readToAnswer(ConstantResponses.ANYTHING_ELSE);
        };
        funcs.put("коэффициент", wrapCoefficient);
        funcs.put("коэф", wrapCoefficient);

        Wrap wrapShort = message -> {
            record.setShortIns(parseValue(message));
            owner.readToAnswer(ConstantResponses.ANYTHING_ELSE);
        };
        funcs.put("короткий", wrapShort);
    }

    private void setGetter(){
        Wrap wrap = message -> {
            Timestamp timestamp = Timestamp.valueOf(castRussianDateFormatToCorrect(message));
            Record[] records = {};
            try {
                records = getterDB.getRecords(timestamp);
            } catch (Exception e){
                e.printStackTrace();
            }
            owner.readToAnswer("Результаты за " + message + ":\n\n");
            for (Record record: records){
                owner.readToAnswer(record.toString());
            }
        };

        funcs.put("результат", wrap);
    }


    public void putMessage(String message){
        if (message.split(" ").length == 1){
            if (funcs.containsKey(message)){
                funcs.get(message).exec(message);
            } else {
                setErrorResponse();
            }
            return;
        }
        String[] values = message.split(" ");
        String parameter = values[0];
        String value = values[1];
        if (funcs.containsKey(parameter)){
            funcs.get(parameter).exec(value);
            return;
        }
        setErrorResponse();
    }

    private void setErrorResponse(){
        funcs.get("ERROR").exec(null);
    }

    private Double parseValue(String value){
        try {
            return Double.parseDouble(value);
        } catch (Exception e){
            String[] parts = value.split(",");
            if (parts.length == 2){
                return Double.parseDouble(parts[0]+"."+parts[1]);
            }
        }
        return null;
    }

    private String castRussianDateFormatToCorrect(String date){
        String[] parts = date.split("\\.");
        if (parts[2].length() == 2){
            parts[2] = "20" + parts[2];
        }
        return parts[2]+"-"+parts[1]+"-"+parts[0] + " 00:00:00";
    }

}
