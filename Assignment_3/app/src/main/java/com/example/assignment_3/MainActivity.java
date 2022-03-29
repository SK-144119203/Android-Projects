package com.example.assignment_3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity
                        implements FragmentDialog.DialogClickListener{

    private static int totalNumberOfCorrectAnswers, totalNumberOfQuestionsAttempts, numberOfCorrectAnswers;
    private int selectedNumberOfQuestion = 8, progressCounter = 0, colorNumber, qNumber;
    String str, question;

    ProgressBar bar;
    FileStorageManager storageManager;

    FragmentManager fm = getSupportFragmentManager();

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString("Question", question);
        outState.putInt("QuestionNumber", qNumber);
        outState.putInt("QuestionColorNumber", colorNumber);
        outState.putInt("ProgressCounter", progressCounter);
        outState.putInt("NumberOfQuestions", selectedNumberOfQuestion);
        outState.putInt("TotalNumberOfAnswers", totalNumberOfCorrectAnswers);
        outState.putInt("TotalNumberOfQuestions", totalNumberOfQuestionsAttempts);
        outState.putInt("NumberOfAnswers", numberOfCorrectAnswers);
        outState.putSerializable("StorageManager", (Serializable) storageManager);
        outState.putSerializable("ProgressBar", (Serializable) bar);
        outState.putSerializable("FragmentManager", (Serializable) fm);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        question = savedInstanceState.getString("Question");
        qNumber =savedInstanceState.getInt("QuestionNumber");
        colorNumber = savedInstanceState.getInt("QuestionColorNumber");
        progressCounter = savedInstanceState.getInt("ProgressCounter");
        selectedNumberOfQuestion = savedInstanceState.getInt("NumberOfQuestions");
        totalNumberOfCorrectAnswers = savedInstanceState.getInt("TotalNumberOfAnswers");
        totalNumberOfQuestionsAttempts = savedInstanceState.getInt("TotalNumberOfQuestions");
        numberOfCorrectAnswers = savedInstanceState.getInt("NumberOfAnswers");
        storageManager = (FileStorageManager) savedInstanceState.getSerializable("StorageManager");
        bar = (ProgressBar) savedInstanceState.getSerializable("ProgressBar");
        fm = (FragmentManager) savedInstanceState.getSerializable("FragmentManager");
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        storageManager = new FileStorageManager();

        ArrayList<Integer> tempList;
        tempList = storageManager.readAllValuesFromFile(MainActivity.this);
        if(!tempList.isEmpty()){
            totalNumberOfCorrectAnswers = tempList.get(0);
            totalNumberOfQuestionsAttempts = tempList.get(1);
            tempList.clear();
        }else{
            totalNumberOfCorrectAnswers = 0;
            totalNumberOfQuestionsAttempts = 0;
        }

        bar = findViewById(R.id.quizBar);
        bar.setMax(selectedNumberOfQuestion);
        bar.setProgress(progressCounter);

        displayQuestion();
    }

    private void displayQuestion() {
        generateQuestion();
        QuestionFragment dialog = QuestionFragment.newInstance(question, colorNumber);
        fm.beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fm.beginTransaction().add(R.id.questionContainer, dialog).commit();
    }

    private void generateQuestion() {
        int tempQNumber = qNumber;
        while(tempQNumber == qNumber){
            qNumber = new Random().nextInt(9-1)+1;
        }
        str = "question" + qNumber;
        colorNumber = Color.argb(255, new Random().nextInt(256), new Random().nextInt(256), new Random().nextInt(256));
        question = getString(getApplicationContext().getResources().getIdentifier(str, "string", getPackageName()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.subMenuAverage:
                storageManager.readAllValuesFromFile(MainActivity.this);
                ArrayList<Integer> tempList;
                tempList = storageManager.readAllValuesFromFile(MainActivity.this);
                totalNumberOfCorrectAnswers = tempList.get(0);
                totalNumberOfQuestionsAttempts = tempList.get(1);
                tempList.clear();

                AlertDialog.Builder avgBuilder = new AlertDialog.Builder(MainActivity.this);
                avgBuilder.setMessage(getString(R.string.displayAverageMessage) + totalNumberOfCorrectAnswers + "/" + totalNumberOfQuestionsAttempts);
                avgBuilder.setPositiveButton(
                        getString(R.string.DialogFragmentButtonSAVE), (dialogInterface, i) -> {
                            Toast.makeText(getApplicationContext(), R.string.saveAverageMessage, Toast.LENGTH_SHORT).show();
                            dialogInterface.dismiss();
                        });
                avgBuilder.setNegativeButton(
                        getString(R.string.DialogFragmentButtonOK), (dialogInterface, i) -> dialogInterface.dismiss());

                AlertDialog avgAlertDialog = avgBuilder.create();
                avgAlertDialog.show();
                avgAlertDialog.setCancelable(false);
                break;

            case R.id.subMenuNumberOfQuestions:
                FragmentDialog dialog = FragmentDialog.newInstance(getString(R.string.DialogFragmentTextView));
                dialog.show(fm, FragmentDialog.Tag);
                dialog.listener = this;

                dialog.setCancelable(false);
                break;

            case R.id.subMenuReset:
                totalNumberOfCorrectAnswers = 0;
                totalNumberOfQuestionsAttempts = 0;
                storageManager.writeResultToFile(MainActivity.this, totalNumberOfCorrectAnswers, totalNumberOfQuestionsAttempts);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void dialogListenerWithQuestion(String nQuestion) {
        selectedNumberOfQuestion = Integer.parseInt(nQuestion);
        bar.setMax(selectedNumberOfQuestion);
        numberOfCorrectAnswers = 0;
        progressCounter = 0;
        bar.setProgress(progressCounter);
        Toast.makeText(this, nQuestion+ " " + getString(R.string.numberOfQuestionsSelected), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void dialogListenerWithCancel() {
        Toast.makeText(this, R.string.numberOfQuestionsNotSelected, Toast.LENGTH_SHORT).show();
    }

    @SuppressLint("NonConstantResourceId")
    public void btnClick(View view) {

        String result = getCorrectAnswer();
        switch (view.getId()){
            case R.id.btnTrue:
                if(result.equals(getString(R.string.correctAnswer))){
                    ++numberOfCorrectAnswers;
                }
                break;
            case R.id.btnFalse:
                if(result.equals(getString(R.string.inCorrectAnswer))){
                    ++numberOfCorrectAnswers;
                }
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + view.getId());
        }
        Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
        progressCounter++;
        bar.setProgress(progressCounter);

        if(progressCounter == selectedNumberOfQuestion){
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle(R.string.finalResult);
            builder.setMessage(getString(R.string.finalResultMessage1) + numberOfCorrectAnswers + getString(R.string.finalResultMessage2) + selectedNumberOfQuestion);
            builder.setPositiveButton(
                    getString(R.string.DialogFragmentButtonSAVE), (dialogInterface, i) -> {
                        Toast.makeText(getApplicationContext(), R.string.saveAverageMessage, Toast.LENGTH_SHORT).show();
                        totalNumberOfCorrectAnswers += numberOfCorrectAnswers;
                        totalNumberOfQuestionsAttempts += selectedNumberOfQuestion;
                        storageManager.writeResultToFile(MainActivity.this, totalNumberOfCorrectAnswers, totalNumberOfQuestionsAttempts);
                        numberOfCorrectAnswers = 0;
                        progressCounter = 0;
                        bar.setProgress(progressCounter);
                        dialogInterface.dismiss();
                    });
            builder.setNegativeButton(
                    getString(R.string.DialogFragmentButtonCancel), (dialogInterface, i) -> {
                        numberOfCorrectAnswers = 0;
                        progressCounter = 0;
                        bar.setProgress(progressCounter);
                        dialogInterface.dismiss();
                    });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();
            alertDialog.setCancelable(false);
        }

        displayQuestion();
    }

    private String getCorrectAnswer() {
        return getString(getApplicationContext().getResources().getIdentifier("answer" + qNumber, "string", getPackageName()));
    }
}