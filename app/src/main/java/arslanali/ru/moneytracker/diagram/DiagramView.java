package arslanali.ru.moneytracker.diagram;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;

import arslanali.ru.moneytracker.R;

public class DiagramView extends View {

    private Paint expensePaint = new Paint();
    private Paint incomePaint = new Paint();

    private long expense;
    private long income;

    public DiagramView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        expensePaint.setColor(getResources().getColor(R.color.colorExpense));
        incomePaint.setColor(getResources().getColor(R.color.colorItem));

        if (isInEditMode()) {
            expense = 35000L;
            income = 45000;
        }
    }

    public void updateDiagram(long expense, long income) {
        this.expense = expense;
        this.income = income;
        invalidate();
    }

    // manage my view
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    // painting my view
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            drawPieDiagram(canvas);
        else
            drawRectDiagram(canvas);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void drawPieDiagram(Canvas canvas) {
        if (expense + income == 0) {
            return;
        }

        float expenseAngle = 360.f * expense / (expense + income);
        float incomeAngle = 360.f * income / (expense + income);
        int space = 10; // space between income and expense
        int size = Math.min(getWidth(), getHeight()) - space * 2;
        int xMargin = (getWidth() - size) / 2;
        int yMargin = (getHeight() - size) / 2;

        canvas.drawArc(xMargin - space, yMargin, getWidth() - xMargin - space, getHeight() - yMargin, 180 - expenseAngle / 2, expenseAngle, true, expensePaint);
        canvas.drawArc(xMargin + space, yMargin, getWidth() - xMargin + space, getHeight() - yMargin, 360 - incomeAngle / 2, incomeAngle, true, incomePaint);
    }

    private void drawRectDiagram(Canvas canvas) {
        if (expense + income == 0)
            return;

        long max = Math.max(expense, income), expensesHeight = getHeight() * expense / max, incomeHeight = getHeight() * income / max;
        int w = getWidth() / 4;

        canvas.drawRect(w / 2, getHeight() - expensesHeight, w * 3 / 2, getHeight(), expensePaint);
        canvas.drawRect(5 * w / 2, getHeight() - incomeHeight, w * 7 / 2, getHeight(), incomePaint);
    }
}

