package bitman.ay27.blockade.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Proudly to user Intellij IDEA.
 * Created by ay27 on 15/1/29.
 */
public class RandomKeyboard extends FrameLayout {
    private static final int[] ids = new int[]{R.id.key_btn_1, R.id.key_btn_2, R.id.key_btn_3, R.id.key_btn_4, R.id.key_btn_5, R.id.key_btn_6, R.id.key_btn_7, R.id.key_btn_8, R.id.key_btn_9, R.id.key_btn_0, R.id.key_btn_cancel, R.id.key_btn_back};
    private static final int KEYBOARD_NUMBER_SUM = 10;
    private ArrayList<NumberClickListener> listeners;
    private OnClickListener numberBtnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            for (NumberClickListener listener : listeners) {
                listener.onClick(v, ((Button) v).getText().toString());
            }
        }
    };
    private List<Button> btns;

    public RandomKeyboard(Context context) {
        super(context);
        init(null);
    }

    public RandomKeyboard(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public RandomKeyboard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private static int[] getRandomSequence(int total) {
        int[] sequence = new int[total];
        int[] output = new int[total];

        for (int i = 0; i < total; i++) {
            sequence[i] = i;
        }

        Random random = new Random();
        int end = total - 1;

        for (int i = 0; i < total; i++) {
            int num = random.nextInt(end + 1);
            output[i] = sequence[num];
            sequence[num] = sequence[end];
            end--;
        }

        return output;
    }

    private void init(AttributeSet attrs) {
        View keyboard = LayoutInflater.from(getContext()).inflate(R.layout.random_keyboard, null);
        listeners = new ArrayList<NumberClickListener>();
        btns = new ArrayList<Button>();
        findViews(keyboard);
        this.setEnabled(true);
        keyboard.setEnabled(true);
        addView(keyboard);
    }

    private void findViews(View view) {
        for (int id : ids) {
            Button button = (Button) view.findViewById(id);
            button.setOnClickListener(numberBtnClickListener);
            btns.add(button);
        }
    }

    public void randomIt() {
        int[] randomSequence = getRandomSequence(KEYBOARD_NUMBER_SUM);
        for (int i = 0; i < KEYBOARD_NUMBER_SUM; i++) {
            btns.get(i).setText(""+randomSequence[i]);
        }
    }

    public void registerListener(NumberClickListener listener) {
        listeners.add(listener);
    }

    public void unregisterListener(NumberClickListener listener) {
        listeners.remove(listener);
    }

    public static interface NumberClickListener {
        public void onClick(View v, String value);
    }

}
