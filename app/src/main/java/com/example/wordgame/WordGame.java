package com.example.wordgame;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wordgame.Adapter.MyAdapter;

import java.util.ArrayList;
import java.util.Collections;

public class WordGame extends AppCompatActivity implements View.OnClickListener {

    private String[] words = {"Apple", "Banana", "softwarica", "coventry", "android", "assignment"};
    private RecyclerView recyclerView;
    TextView textView;
    private EditText editTextWord;
    SharedPreferences sharedPreferences;
    private int level = 0;
    Button btnOk, btnClear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_game);
        recyclerView = findViewById(R.id.rVChars);
        textView = findViewById(R.id.level);
        editTextWord = findViewById(R.id.txtbox);
        btnOk = findViewById(R.id.btnOK);
        btnClear = findViewById(R.id.btnCancel);

        btnOk.setOnClickListener(this);
        btnClear.setOnClickListener(this);

        SharedPreferences savedata = getSharedPreferences("Game", Context.MODE_PRIVATE);
        if (savedata.getInt("Level", 0) == 0) {
            showWord(level);
        } else {
            level = savedata.getInt("Level", 0);
            showWord(level);
        }
    }

    private Character[] shuffleWords(int level) {
        char[] word = words[level].toCharArray();


        ArrayList<Character> chars = new ArrayList<>(word.length);
        for (char c : word) {
            chars.add(c);
        }

        Collections.shuffle(chars);
        Character[] shuffledWord = new Character[chars.size()];

        for (int i = 0; i < shuffledWord.length; i++) {
            shuffledWord[i] = chars.get(i);
        }
        return shuffledWord;
    }

    private void showWord(int i){
        MyAdapter myAdapter = new MyAdapter(WordGame.this, shuffleWords(i), editTextWord);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 3);
        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(layoutManager);
        textView.setText("GAME LEVEL : " + (i+1));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnOK:
                String usr_word = editTextWord.getText().toString();
                if (level + 1 < words.length) {
                    if (usr_word.equals(words[level])) {
                        level++;
                        showWord(level);
                        sharedPreferences = getSharedPreferences("Game", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putInt("Level", level);
                        editor.commit();
                        editTextWord.setText("");
                        Toast.makeText(WordGame.this, "Next Level", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(WordGame.this, "Wrong Word", Toast.LENGTH_SHORT).show();
                        editTextWord.setText("");
                        showWord(level);
                    }
                } else {
                    level = 0;
                    sharedPreferences = getSharedPreferences("Game", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("Level", level);
                    showWord(level);
                    Toast.makeText(WordGame.this, "Game Over", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.btnCancel:
                editTextWord.setText("");
                showWord(level);
                break;
        }

    }
}
