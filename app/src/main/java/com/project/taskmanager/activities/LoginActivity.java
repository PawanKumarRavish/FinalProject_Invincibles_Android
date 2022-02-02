package com.project.taskmanager.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.project.taskmanager.R;
import com.project.taskmanager.SharedPreference;
import com.project.taskmanager.db.DatabaseClient;
import com.project.taskmanager.db.entities.TotalBalance;
import com.project.taskmanager.interfaces.Tags;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class  LoginActivity extends WalletActivity implements View.OnClickListener {


    ArrayList<TotalBalance> parentList;

    String userPin;

    @BindView(R.id.code1_et)
    EditText mCode1;

    @BindView(R.id.code2_et)
    EditText mCode2;

    @BindView(R.id.code3_et)
    EditText mCode3;

    @BindView(R.id.code4_et)
    EditText mCode4;

    @BindView(R.id.buttonOne)
    Button mButtonOne;

    @BindView(R.id.buttonTwo)
    Button mButtonTwo;

    @BindView(R.id.buttonThree)
    Button mButtonThree;

    @BindView(R.id.buttonFour)
    Button mButtonFour;

    @BindView(R.id.buttonFive)
    Button mButtonFive;

    @BindView(R.id.buttonSix)
    Button mButtonSix;

    @BindView(R.id.buttonSeven)
    Button mButtonSeven;

    @BindView(R.id.buttonEight)
    Button mButtonEight;

    @BindView(R.id.buttonNine)
    Button mButtonNine;

    @BindView(R.id.buttonDelete)
    ImageButton mButtonDelete;

    @BindView(R.id.buttonZero)
    Button mButtonZero;

    @BindView(R.id.buttonOkay)
    ImageButton mButtonOkay;

    @BindView(R.id.show_tv)
    TextView mShowTv;

    @BindView(R.id.hide_tv)
    TextView mHideTv;

    @BindView(R.id.showLayout)
    LinearLayout mShowLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        parentList = new ArrayList<>();


        boolean isLogin = SharedPreference.isLogin();
        if (isLogin) {
            Intent i = new Intent(LoginActivity.this, ConfirmPinActivity.class);
            startActivity(i);
            finish();
        }


        mButtonOne.setOnClickListener(this);
        mButtonTwo.setOnClickListener(this);
        mButtonThree.setOnClickListener(this);
        mButtonFour.setOnClickListener(this);
        mButtonFive.setOnClickListener(this);
        mButtonSix.setOnClickListener(this);
        mButtonSeven.setOnClickListener(this);
        mButtonEight.setOnClickListener(this);
        mButtonNine.setOnClickListener(this);
        mButtonZero.setOnClickListener(this);
        mButtonDelete.setOnClickListener(this);
        mButtonOkay.setOnClickListener(this);


        mShowTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHideTv.setVisibility(View.VISIBLE);
                mShowTv.setVisibility(View.GONE);
                mCode1.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                mCode2.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                mCode3.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                mCode4.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }
        });

        mHideTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mShowTv.setVisibility(View.VISIBLE);
                mHideTv.setVisibility(View.GONE);
                mCode1.setTransformationMethod(PasswordTransformationMethod.getInstance());
                mCode2.setTransformationMethod(PasswordTransformationMethod.getInstance());
                mCode3.setTransformationMethod(PasswordTransformationMethod.getInstance());
                mCode4.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        });

    }

    private void prepareDefaultTotalBalance() {
        TotalBalance totalBalance = new TotalBalance(Tags.CASH, 0);
        parentList.add(totalBalance);

        totalBalance = new TotalBalance(Tags.BANK, 0);
        parentList.add(totalBalance);
    }

    private void saveDefaultTotalBalance() {

        class SaveDefaultTotalBalance extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {

                //adding to database
                DatabaseClient.getInstance(LoginActivity.this).getAppDatabase()
                        .totalBalanceDao()
                        .insertList(parentList);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Log.e("List", parentList + "");
            }
        }

        SaveDefaultTotalBalance saveDefaultTotalBalance = new SaveDefaultTotalBalance();
        saveDefaultTotalBalance.execute();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.buttonOne:
                if (mCode1.getText().toString().length() == 0) {
                    setOtp(mCode1, "1");
                } else if (mCode2.getText().toString().length() == 0) {
                    setOtp(mCode2, "1");
                } else if (mCode3.getText().toString().length() == 0) {
                    setOtp(mCode3, "1");
                } else if (mCode4.getText().toString().length() == 0) {
                    setOtp(mCode4, "1");
                }
                break;

            case R.id.buttonTwo:
                if (mCode1.getText().toString().length() == 0) {
                    setOtp(mCode1, "2");
                } else if (mCode2.getText().toString().length() == 0) {
                    setOtp(mCode2, "2");
                } else if (mCode3.getText().toString().length() == 0) {
                    setOtp(mCode3, "2");
                } else if (mCode4.getText().toString().length() == 0) {
                    setOtp(mCode4, "2");
                }
                break;

            case R.id.buttonThree:
                if (mCode1.getText().toString().length() == 0) {
                    setOtp(mCode1, "3");
                } else if (mCode2.getText().toString().length() == 0) {
                    setOtp(mCode2, "3");
                } else if (mCode3.getText().toString().length() == 0) {
                    setOtp(mCode3, "3");
                } else if (mCode4.getText().toString().length() == 0) {
                    setOtp(mCode4, "3");
                }
                break;

            case R.id.buttonFour:
                if (mCode1.getText().toString().length() == 0) {
                    setOtp(mCode1, "4");
                } else if (mCode2.getText().toString().length() == 0) {
                    setOtp(mCode2, "4");
                } else if (mCode3.getText().toString().length() == 0) {
                    setOtp(mCode3, "4");
                } else if (mCode4.getText().toString().length() == 0) {
                    setOtp(mCode4, "4");
                }
                break;

            case R.id.buttonFive:
                if (mCode1.getText().toString().length() == 0) {
                    setOtp(mCode1, "5");
                } else if (mCode2.getText().toString().length() == 0) {
                    setOtp(mCode2, "5");
                } else if (mCode3.getText().toString().length() == 0) {
                    setOtp(mCode3, "5");
                } else if (mCode4.getText().toString().length() == 0) {
                    setOtp(mCode4, "5");
                }
                break;

            case R.id.buttonSix:
                if (mCode1.getText().toString().length() == 0) {
                    setOtp(mCode1, "6");
                } else if (mCode2.getText().toString().length() == 0) {
                    setOtp(mCode2, "6");
                } else if (mCode3.getText().toString().length() == 0) {
                    setOtp(mCode3, "6");
                } else if (mCode4.getText().toString().length() == 0) {
                    setOtp(mCode4, "6");
                }
                break;

            case R.id.buttonSeven:
                if (mCode1.getText().toString().length() == 0) {
                    setOtp(mCode1, "7");
                } else if (mCode2.getText().toString().length() == 0) {
                    setOtp(mCode2, "7");
                } else if (mCode3.getText().toString().length() == 0) {
                    setOtp(mCode3, "7");
                } else if (mCode4.getText().toString().length() == 0) {
                    setOtp(mCode4, "7");
                }
                break;

            case R.id.buttonEight:
                if (mCode1.getText().toString().length() == 0) {
                    setOtp(mCode1, "8");
                } else if (mCode2.getText().toString().length() == 0) {
                    setOtp(mCode2, "8");
                } else if (mCode3.getText().toString().length() == 0) {
                    setOtp(mCode3, "8");
                } else if (mCode4.getText().toString().length() == 0) {
                    setOtp(mCode4, "8");
                }
                break;

            case R.id.buttonNine:
                if (mCode1.getText().toString().length() == 0) {
                    setOtp(mCode1, "9");
                } else if (mCode2.getText().toString().length() == 0) {
                    setOtp(mCode2, "9");
                } else if (mCode3.getText().toString().length() == 0) {
                    setOtp(mCode3, "9");
                } else if (mCode4.getText().toString().length() == 0) {
                    setOtp(mCode4, "9");
                }
                break;

            case R.id.buttonZero:
                if (mCode1.getText().toString().length() == 0) {
                    setOtp(mCode1, "0");
                } else if (mCode2.getText().toString().length() == 0) {
                    setOtp(mCode2, "0");
                } else if (mCode3.getText().toString().length() == 0) {
                    setOtp(mCode3, "0");
                } else if (mCode4.getText().toString().length() == 0) {
                    setOtp(mCode4, "0");
                }
                break;

            case R.id.buttonDelete:
                if (mCode4.getText().toString().length() != 0) {
                    mCode4.setText("");
                } else if (mCode3.getText().toString().length() != 0) {
                    mCode3.setText("");
                } else if (mCode2.getText().toString().length() != 0) {
                    mCode2.setText("");
                } else if (mCode1.getText().toString().length() != 0) {
                    mCode1.setText("");
                }
                break;

            case R.id.buttonOkay:

                if (mCode1.length() == 0 || mCode2.length() == 0 || mCode3.length() == 0 || mCode4.length() == 0) {
                    Toast.makeText(LoginActivity.this, "Please enter PIN.", Toast.LENGTH_SHORT).show();
                } else {

                    if (!SharedPreference.isCount()) {
                        // Saving the default total balance
                        prepareDefaultTotalBalance();
                        saveDefaultTotalBalance();
                        // Saving the default total balance
                        // for saving in total balance dao for only one time (Unique constraint error)
                        SharedPreference.setCount();

                    }


                    userPin = mCode1.getText().toString().trim() + mCode2.getText().toString().trim() + mCode3.getText().toString().trim() + mCode4.getText().toString().trim();
                    SharedPreference.setUserPin(userPin);
                    Intent intent = new Intent(LoginActivity.this, ConfirmPinActivity.class);
                    intent.putExtra("USER_PIN", userPin);
                    startActivity(intent);
                }

                break;
        }

    }


    private void setOtp(TextView editText, String value) {
        editText.setText(value);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
