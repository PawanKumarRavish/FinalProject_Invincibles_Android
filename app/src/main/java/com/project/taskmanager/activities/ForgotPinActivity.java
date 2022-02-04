package com.project.taskmanager.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.project.taskmanager.R;
import com.project.taskmanager.SharedPreference;
import com.project.taskmanager.db.DatabaseClient;
import com.project.taskmanager.db.entities.SecurityQuestions;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ForgotPinActivity extends AppCompatActivity {

    @BindView(R.id.question_tv)
    TextView mQuestionTv;

    @BindView(R.id.answer_et)
    EditText mAnswerEt;

    @BindView(R.id.save_btn)
    Button mSaveBtn;

    List<SecurityQuestions> securityQuestions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pin);
        ButterKnife.bind(this);

        getSecurityQuesAndAnswer();

        mSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mAnswerEt.getText().toString().trim().isEmpty()){
                    Toast.makeText(ForgotPinActivity.this, "Please write your security answer", Toast.LENGTH_LONG).show();
                }
                else{
                    if(mAnswerEt.getText().toString().trim().equalsIgnoreCase(securityQuestions.get(0).getAnswer())){
                        SharedPreference.resetLogin();
                        Intent intent=new Intent(ForgotPinActivity.this,LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else{
                        Toast.makeText(ForgotPinActivity.this, "Your answer is incorrect. Please try again", Toast.LENGTH_LONG).show();
                        mAnswerEt.setText("");
                    }

                }
            }
        });
    }

    private void getSecurityQuesAndAnswer() {

        class GetSecurityQuesAndAnswer extends AsyncTask<Void, Void, List<SecurityQuestions>> {

            @Override
            protected List<SecurityQuestions> doInBackground(Void... voids) {
                  securityQuestions = DatabaseClient
                        .getInstance(ForgotPinActivity.this)
                        .getAppDatabase()
                        .securityQuestionDao()
                        .getAll();
                return securityQuestions;
            }

            @Override
            protected void onPostExecute(List<SecurityQuestions> securityQuestions) {
                super.onPostExecute(securityQuestions);

                Log.e("List",securityQuestions+"");

                mQuestionTv.setText(securityQuestions.get(0).getQuestion());

            }
        }

        GetSecurityQuesAndAnswer gb = new GetSecurityQuesAndAnswer();
        gb.execute();

    }
}
