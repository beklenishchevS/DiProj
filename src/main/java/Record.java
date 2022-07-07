import java.sql.Timestamp;
import java.util.Date;

public class Record {

    private Timestamp date;
    private Double sugar;

    public Record(Double sugar){
        Date javaDate = new Date();
        date = new Timestamp(javaDate.getTime());
        this.sugar = sugar;
    }


    public boolean save(){
        try {
            Saver.save(this);
            return true;
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public Double getSugar() {
        return sugar;
    }

    public Timestamp getDate() {
        return date;
    }
}
