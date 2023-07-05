package cs50.caleb.receiptocr3;

public class History {

    private int id;
    private String userName;
    private String merchantName;
    private String date;
    private String time;
    private double spent;
    private String jsonString;

    public History(int id, String userName, String merchantName, String date, String time, double spent, String jsonString) {
        this.id = id;
        this.userName = userName;
        this.merchantName = merchantName;
        this.date = date;
        this.time = time;
        this.spent = spent;
        this.jsonString = jsonString;
    }

    public int getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public Double getSpent() {
        return spent;
    }

    public String getJsonString() {
        return jsonString;
    }

}
