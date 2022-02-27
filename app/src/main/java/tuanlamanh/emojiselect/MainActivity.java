package tuanlamanh.emojiselect;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    ArrayList<String> listEmojis;
    String target;
    int wrong = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        runGame();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(getApplicationContext(), "Restarting game...", Toast.LENGTH_SHORT).show();
        wrong = 0;
        runGame();
    }

    private void runGame()
    {
        String[] list = getResources().getStringArray(R.array.arr_emoji);
        Collections.shuffle(Arrays.asList(list));
        listEmojis = new ArrayList<>(Arrays.asList(list));
        randomEmoji();
        ArrayAdapter<String> myAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.item, list);

        GridView mainGridView = findViewById(R.id.mainGridView);
        mainGridView.setAdapter(myAdapter);
        mainGridView.setOnItemClickListener((parent, view, position, id) -> {
            TextView v = (TextView) view;
            String pickedEmoji = v.getText().toString();
            if (pickedEmoji.equals(target)) {
                v.setText("");
                listEmojis.remove(pickedEmoji);
                if (listEmojis.size() == 0) {
                    runActivityResult(true);
                    return;
                }
                randomEmoji();
            }
            else {
                wrong++;
                if(wrong >3){
                    runActivityResult(false);
                }
                else {
                    Toast.makeText(getApplicationContext(),
                            "Failed! Remaining: "+(3-wrong)+" lives",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void randomEmoji()
    {
        TextView targetItem = findViewById(R.id.targetEmoji);

        Random rand = new Random();
        int r = rand.nextInt(listEmojis.size());
        target= listEmojis.get(r);
        targetItem.setText(target);
    }

    private void runActivityResult(boolean isWin)
    {
        Intent intent = new Intent(getApplicationContext(), ResultActivity.class);
        intent.putExtra("isWin", isWin);
        startActivity(intent);
    }
}
