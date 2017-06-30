package arslanali.ru.moneytracker.pojo;

public class Item {

    // this is POJO object
    public static final String TYPE_EXPENSE = "expense";
    public static final String TYPE_INCOME = "income";

    public final String name;
    public final String type;
    public final int price;
    int id;

    public Item(String name, int price, String type) {
        this.name = name;
        this.price = price;
        this.type = type;
    }
}
