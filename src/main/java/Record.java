import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Record {

    private final Timestamp date;
    private Double sugar;
    private Double XE;
    private Double shortIns;
    private Double longIns;
    private Double coefficient;
    private final Long userID;

    public Record(Long userID){
        Date javaDate = new Date();
        date = new Timestamp(javaDate.getTime());
        this.userID = userID;
    }

    public Record(Long userID, Timestamp date, Double sugar, Double XE, Double shortIns, Double longIns, Double coefficient){
        this.userID = userID;
        this.date = date;
        this.sugar = sugar;
        this.XE = XE;
        this.shortIns = shortIns;
        this.longIns = longIns;
        this.coefficient = coefficient;
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

    public Double getXE() {
        return XE;
    }

    public Double getShortIns() {
        return shortIns;
    }

    public Double getLongIns() {
        return longIns;
    }

    public Double getCoefficient() {
        return coefficient;
    }

    public void setCoefficient(Double coefficient) {
        this.coefficient = coefficient;
    }

    public void setLongIns(Double longIns) {
        this.longIns = longIns;
    }

    public void setShortIns(Double shortIns) {
        this.shortIns = shortIns;
    }

    public void setSugar(Double sugar) {
        this.sugar = sugar;
    }

    public void setXE(Double XE) {
        this.XE = XE;
    }

    public Long getUserID() {
        return userID;
    }

    /**
     * @return Array where:
     * 2 - sugar
     * 3 - XE
     * 4 - shortIns
     * 5 - longIns
     * 6 - coefficient
     */
    public Double[] getRecordAsArray(){
        Double[] result = new Double[7];
        result[2] = sugar;
        result[3] = XE;
        result[4] = shortIns;
        result[5] = longIns;
        result[6] = coefficient;
        return result;
    }

    public Boolean isEmpty(){
        return sugar == null &&
                XE == null &&
                shortIns == null &&
                longIns == null &&
                coefficient == null;
    }

    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String time = sdf.format(date);
        String sugar = this.sugar != 0 ? String.valueOf(this.sugar) : "";
        String xe =  this.XE != 0 ? String.valueOf(this.XE) : "";
        String shortInsulin = this.shortIns != 0 ? String.valueOf(this.shortIns) : "";
        String longInsulin = this.longIns != 0 ? String.valueOf(this.longIns) : "";
        String coefficient = this.coefficient != 0 ? String.valueOf(this.coefficient) : "";

        String result = time + "\n";
        if (!"".equals(sugar)){
            result += sugar + " ммоль/л" + "\n";
        }
        if (!"".equals(xe)) {
            result += xe + "XE" + "\n";
        }
        if (!"".equals(coefficient)){
            result += "Коэффициент " + coefficient + "\n";
        }
        if (!"".equals(shortInsulin)){
            result += shortInsulin + "ед. короткого"+ "\n";
        }
        if (!"".equals(longInsulin)){
            result += longInsulin + "ед. длинного"+ "\n";
        }

        return result+"\n\n";
    }
}
