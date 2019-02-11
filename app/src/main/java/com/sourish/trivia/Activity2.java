package com.sourish.trivia;

import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.os.CountDownTimer;
import android.widget.Toast;
import android.content.Intent;
import android.provider.ContactsContract;
import android.app.AlertDialog;
import android.content.DialogInterface;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class Activity2 extends AppCompatActivity {
    private static ArrayList<String> memberNames = new ArrayList<>(Arrays.asList("Aayush Tyagi", "Abhinav Koppu", "Aditya Yadav", "Ajay Merchia",
            "Alice Zhao", "Amy Shen", "Anand Chandra", "Andres Medrano", "Angela Dong", "Anika Bagga", "Anmol Parande",
            "Austin Davis", "Ayush Kumar", "Brandon David", "Candice Ye", "Carol Wang", "Cody Hsieh", "Daniel Andrews",
            "Daniel Jing", "Eric Kong", "Ethan Wong", "Fang Shuo", "Izzie Lau", "Jaiveer Singh", "Japjot Singh", "Jeffery Zhang",
            "Joey Hejna", "Julie Deng", "Justin Kim", "Kaden Dippe", "Kanyes Thaker", "Kayli Jiang", "Kiana Go", "Leon Kwak",
            "Levi Walsh", "Louie Mcconnell", "Max Miranda", "Michelle Mao", "Mohit Katyal", "Mudabbir Khan", "Natasha Wong",
            "Nikhar Arora", "Noah Pepper", "Paul Shao", "Radhika Dhomse", "Sai Yandapalli", "Saman Virai", "Sarah Tang",
            "Sharie Wang", "Shiv Kushwah", "Shomil Jain", "Shreya Reddy", "Shubha Jagannatha", "Shubham Gupta", "Srujay Korlakunta",
            "Stephen Jayakar", "Suyash Gupta", "Tiger Chen", "Vaibhav Gattani", "Victor Sun", "Vidya Ravikumar", "Vineeth Yeevani",
            "Wilbur Shi", "William Lu", "Will Oakley", "Xin Yi Chen", "Young Lin"));
    private int counter;
    private Button[] choices = new Button[4];
    private String[] answers = new String[4];
    private String correctAnswer = "Button";

    private Button quit;

    private ImageView photo;

    private TextView scoreDisp;
    private int score;

    private CountDownTimer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);

        photo = findViewById(R.id.memberimage);
        scoreDisp = findViewById(R.id.score);
        choices[0] = findViewById(R.id.choice1);
        choices[1] = findViewById(R.id.choice2);
        choices[2] = findViewById(R.id.choice3);
        choices[3] = findViewById(R.id.choice4);
        quit = findViewById(R.id.quit);
        counter = 0;

        // Set onClickListener for quit button.
        quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(Activity2.this)
                        .setTitle("Exiting Game")
                        .setMessage("Are you sure you want to quit?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setNegativeButton("No", null)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        }).show();
            }
        });

        // Set onClickListeners for each choice button
        for (int i = 0; i < choices.length; i++) {
            final int finalI = i;
            choices[finalI].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (choices[finalI].getText() == correctAnswer) {
                        timer.cancel();
                        Toast.makeText(Activity2.this, "Correct", Toast.LENGTH_SHORT).show();
                        increaseScore();
                        counter++;
                        doAction();
                    } else {
                        timer.cancel();
                        Toast.makeText(Activity2.this, "Incorrect", Toast.LENGTH_SHORT).show();
                        counter++;
                        doAction();
                    }
                }
            });
        }

        Collections.shuffle(memberNames);
        doAction();
    }

    private void doAction() {
        timer = new CountDownTimer(6000, 1000) {
            TextView timerText = findViewById(R.id.timeleft);
            public void onTick(long millisUntilFinished) {
                timerText.setText("Time Left: " + millisUntilFinished / 1000 + "s");
            }

            public void onFinish() {
                timerText.setText("No Time Left");
                Toast.makeText(Activity2.this, "Incorrect", Toast.LENGTH_SHORT).show();
                counter++;
                doAction();
            }
        }.start();

        //Populate buttons and images with data
        if (counter == memberNames.size()) {
            counter = 0;
            Collections.shuffle(memberNames);
        }
        populate();

        /*//Add onClickListeners to buttons
        for (int i = 0; i < choices.length; i++) {
            if (choices[i].getText() == correctAnswer) {
                choices[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        timer.cancel();
                        Toast.makeText(Activity2.this, "Correct", Toast.LENGTH_SHORT).show();
                        increaseScore();
                        counter++;
                        doAction();
                    }
                });
            } else {
                choices[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        timer.cancel();
                        Toast.makeText(Activity2.this, "Incorrect", Toast.LENGTH_SHORT).show();
                        counter++;
                        doAction();
                    }
                });
            }
        }*/

        //Add "add contact" functionality
        final Intent intent = new Intent(ContactsContract.Intents.Insert.ACTION);
        intent.setType(ContactsContract.RawContacts.CONTENT_TYPE);
        intent.putExtra(ContactsContract.Intents.Insert.NAME, "Joe Bob");
        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timer.cancel();
                startActivity(intent);
                doAction();
            }
        });
    }

    private void populate() {
        correctAnswer = memberNames.get(counter);
        answers = new String[4];

        //generate random choices
        int x = (int)(Math.random() * memberNames.size());
        for (int i = 0; i < answers.length - 1; i++) {
            while (x == counter || containedIn(memberNames.get(x), answers)) {
                x = (int)(Math.random() * memberNames.size());
            }
            answers[i] = memberNames.get(x);
        }
        answers[3] = correctAnswer;

        //shuffle choices
        shuffleArray(answers);

        //set buttons to choices
        for (int i = 0; i < answers.length; i++) {
            choices[i].setText(answers[i]);
        }

        //display image
        displayPicture(correctAnswer);
    }

    private boolean containedIn(String name, String[] arr) {
        for (int i = 0; i < arr.length; i++) {
            if (name == arr[i]) {
                return true;
            }
        }
        return false;
    }

    private void increaseScore() {
        score++;
        scoreDisp.setText("Score: " + score);
    }


    private void displayPicture(String member) {
        String name = member.toLowerCase().replace(" ", "");
        int id = getResources().getIdentifier(name, "drawable", getPackageName());
        Drawable drawable = getResources().getDrawable(id);
        photo.setBackground(drawable);
    }

    private static void shuffleArray(String[] a) {
        int n = a.length;
        Random random = new Random();
        random.nextInt();
        for (int i = 0; i < n; i++) {
            int change = i + random.nextInt(n - i);
            swap(a, i, change);
        }
    }

    private static void swap(String[] a, int i, int change) {
        String helper = a[i];
        a[i] = a[change];
        a[change] = helper;
    }
}
