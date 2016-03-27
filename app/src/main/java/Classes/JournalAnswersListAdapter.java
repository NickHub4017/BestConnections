package Classes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.syncbridge.bestconnections.R;

import java.util.List;

import DBHelper.Question;

/**
 * Created by Pasan Eramusugoda on 5/9/2015.
 */
public class JournalAnswersListAdapter extends ArrayAdapter<Question> {

    public JournalAnswersListAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public JournalAnswersListAdapter(Context context, int resource, List<Question> items) {
        super(context, resource, items);
    }

    boolean isEditing = false;

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.layout_journal_answers_list_item, null);
        }

        Question question = getItem(position);

        if (question != null) {
            TextView textViewQuestion = (TextView) v.findViewById(R.id.textViewQuestion);
            final EditText editTextAnswer = (EditText) v.findViewById(R.id.editTextAnswer);
            ImageView imageViewEdit = (ImageView) v.findViewById(R.id.imageViewEdit);

            if (textViewQuestion != null) {
                textViewQuestion.setText(question.getQuestion());
            }

            if (editTextAnswer != null) {
                editTextAnswer.setText(question.getAnswer());
                editTextAnswer.requestFocusFromTouch();
                editTextAnswer.setOnTouchListener(new View.OnTouchListener() {
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    /* get the index of the touched list item */
                           // mNoteId = index;
                        }
                        return false;
                    }
                });
            }

            imageViewEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!isEditing) {
                        isEditing = true;
                        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        //imm.showSoftInput(editTextAnswer, InputMethodManager.SHOW_FORCED);
                    } else {
                        isEditing = false;
                        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//                        if (editTextAnswer != null) {
//                            imm.hideSoftInputFromWindow(editTextAnswer.getWindowToken(), 0);
//                        }
                    }
                }
            });
        }

        return v;
    }
}
