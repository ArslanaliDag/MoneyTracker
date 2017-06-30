package arslanali.ru.moneytracker.pojo;

public class Item {

    public static final String TYPE_EXPENSE = "expense";
    public static final String TYPE_INCOME = "income";

    public final String name;
    public final int price;

    public Item(String name, int price) {
        this.name = name;
        this.price = price;
    }
}
