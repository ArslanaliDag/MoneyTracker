package arslanali.ru.moneytracker.pojo;

import java.io.Serializable;

public class Item implements Serializable {

    // this is POJO object
    public static final String TYPE_EXPENSE = "expense";
    public static final String TYPE_INCOME = "income";

    private String name;
    private String type;
    private int price;
    private int id;

    public Item(int price, String name, String type) {
        this.price = price;
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public int getPrice() {
        return price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
