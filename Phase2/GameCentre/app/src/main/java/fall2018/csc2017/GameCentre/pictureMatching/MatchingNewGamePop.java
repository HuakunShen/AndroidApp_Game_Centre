package fall2018.csc2017.GameCentre.pictureMatching;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import fall2018.csc2017.GameCentre.R;

public class MatchingNewGamePop extends AppCompatActivity {

    private int selected_difficulty;
    private String[] list_diff = new String[]{"Easy(4x4)", "Normal(6x6)", "Hard(8x8)"};

    private String selected_theme;
    private String[] list_theme = new String[]{"Number", "Animal", "Emoji"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matching_new_game_pop);
        addDiffSpinnerListener();
        addThemeSpinnerListener();
    }

    private void addDiffSpinnerListener() {
        Spinner select_diff = findViewById(R.id.match_diff_select);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, list_diff);
        select_diff.setAdapter(arrayAdapter);

        select_diff.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position) == list_diff[0]) {
                    selected_difficulty = 4;
                } else if (parent.getItemAtPosition(position) == list_diff[1]) {
                    selected_difficulty = 6;
                } else if (parent.getItemAtPosition(position) == list_diff[2]) {
                    selected_difficulty = 8;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selected_difficulty = 4;
            }
        });
    }

    private void addThemeSpinnerListener() {
        final Spinner select_theme = findViewById(R.id.match_theme_select);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, list_theme);
        select_theme.setAdapter(arrayAdapter);

        select_theme.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position) == list_theme[0]) {
                    selected_theme = list_theme[0];
                } else if (parent.getItemAtPosition(position) == list_diff[1]) {
                    selected_theme = list_theme[1];
                } else if (parent.getItemAtPosition(position) == list_diff[2]) {
                    selected_theme = list_theme[2];
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selected_difficulty = 4;
            }
        });
    }
}
