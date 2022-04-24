package tamhoang.ldpro4.ui.custom.keyboard;

import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.widget.EditText;

/**
 * @author:Hanet Electronics
 * @Skype: chukimmuoi
 * @Mobile : +84 167 367 2505
 * @Email: muoick@hanet.com
 * @Website: http://hanet.com/
 * @Project: SuperTabletPlayXOne
 * Created by CHUKIMMUOI on 6/7/2016.
 */

public class KeyboardNumberListener implements KeyboardView.OnKeyboardActionListener {
    public static final String TAG = KeyboardNumberListener.class.getSimpleName();
    private EditText mEditText;

    public KeyboardNumberListener(EditText mEditText) {
        this.mEditText = mEditText;
    }

    @Override
    public void onPress(int primaryCode) {

    }

    @Override
    public void onRelease(int primaryCode) {

    }

    @Override
    public void onKey(int primaryCode, int[] keyCodes) {
        String value = mEditText.getText().toString().trim();
        if(value.isEmpty()){
            value = "0";
        }
        long count = Long.parseLong(value);
        switch (primaryCode){
            case Keyboard.KEYCODE_DELETE:
                deleteSelection(mEditText);
                break;
            case 55006:
                if(mEditText != null) mEditText.getText().clear();
                break;
            default:
                if(count < Integer.MAX_VALUE) {
                    char code = (char) primaryCode;
                    code = Character.toUpperCase(code);
                    updateSelection(code, mEditText);
                }
                break;
        }
    }

    @Override
    public void onText(CharSequence text) {

    }

    @Override
    public void swipeLeft() {

    }

    @Override
    public void swipeRight() {

    }

    @Override
    public void swipeDown() {

    }

    @Override
    public void swipeUp() {

    }

    private void updateSelection(char letter, EditText editText) {
        int selectionIndex = editText.getSelectionStart();
        String text = editText.getText().toString();

        String headText = text.substring(0, selectionIndex);
        String tailText = headText.length() == text.length() ? "" : text.substring(selectionIndex, text.length());

        headText += letter;
        editText.setText(headText + tailText);
        editText.setSelection(selectionIndex + 1);
    }

    private void deleteSelection(EditText editText) {
        int selectionIndex = editText.getSelectionStart();
        String text = editText.getText().toString();

        String headText = text.substring(0, selectionIndex);
        String tailText = headText.length() == text.length() ? "" : text.substring(selectionIndex, text.length());

        if (headText.length() > 0) {
            headText = headText.substring(0, headText.length() - 1);
        }
        editText.setText(headText + tailText);
        editText.setSelection(Math.max(selectionIndex - 1, 0));
    }
}
