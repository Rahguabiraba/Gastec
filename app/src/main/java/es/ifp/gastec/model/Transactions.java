package es.ifp.gastec.model;

public class Transactions {

    private int id;
    private String title;
    private float amount;
    private String type;
    private String tag;
    private String date;
    private String note;

    public Transactions(int id, String title, float amount, String type, String tag, String date, String note) {

        this.id = id;
        this.title = title;
        this.amount = amount;
        this.type = type;
        this.tag = tag;
        this.date = date;
        this.note = note;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
