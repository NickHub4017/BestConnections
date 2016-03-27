package Classes;

import android.content.Context;
import android.view.LayoutInflater;
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
import DBHelper.Quiz;

/**
 * Created by Pasan Eramusugoda on 5/9/2015.
 */
public class JournalQuizListAdapter extends ArrayAdapter<Quiz> {

    public JournalQuizListAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public JournalQuizListAdapter(Context context, int resource, List<Quiz> items) {
        super(context, resource, items);
    }

    boolean isEditing = false;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.layout_journal_results_list_item, null);
        }

        Quiz quiz = getItem(position);

        if (quiz != null) {
            TextView textViewQuiz = (TextView) v.findViewById(R.id.textViewQuiz);
            TextView textViewValue = (TextView) v.findViewById(R.id.textViewValue);

            if (textViewQuiz != null) {
                textViewQuiz.setText(quiz.getQuiz());
            }

            if (textViewValue != null) {
                textViewValue.setText(Integer.toString(quiz.getValue()));
            }
        }

        return v;
    }
}
