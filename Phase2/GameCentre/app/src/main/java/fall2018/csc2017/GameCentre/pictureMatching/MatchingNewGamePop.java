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
                for (int tmp = 0; tmp < list_diff.length; tmp++) {
                    if (parent.getItemAtPosition(position) == list_diff[tmp]) {
                        selected_difficulty = tmp * 2 + 4;
                    }
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
                for (int tmp = 0; tmp < list_theme.length; tmp++) {
                    if (parent.getItemAtPosition(position) == list_theme[tmp]) {
                        selected_theme = list_theme[tmp];
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selected_theme = list_theme[0];
            }
        });
    }
}
