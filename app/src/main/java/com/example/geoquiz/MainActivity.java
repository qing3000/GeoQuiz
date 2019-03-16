package com.example.geoquiz;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
{
    private static final String TAG = "QuizActivity";
    private static final String KEY_INDEX = "index";
    private static final int REQUEST_CODE_CHEAT = 0;

    private Button mTrueButton;
    private Button mFalseButton;
    private ImageButton mNextButton;
    private ImageButton mPreviousButton;
    private Button mCheatButton;
    private TextView mQuestionTextView;
    private Question[] mQuestionBank = new Question[]
    {
        new Question(R.string.question_australia, true),
        new Question(R.string.question_oceans, true),
        new Question(R.string.question_mideast, false),
        new Question(R.string.question_africa, false),
        new Question(R.string.question_americas, true),
        new Question(R.string.question_asia, true),
    };

    private int mCurrentIndex = 0;
    private boolean mIsCheater;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate(Bundle) called");
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null)
        {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
            Log.i(TAG, String.format("currentIndex=%d", mCurrentIndex));
        }

        // Set up question.
        mQuestionTextView = findViewById(R.id.question_text_view);
        mQuestionTextView.setOnClickListener(new View.OnClickListener()
        {
            @Override public void onClick(View v)
            {
                updateQuestion(1);
            }
        });

        // Hook up the buttons
        mTrueButton = findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(new View.OnClickListener()
        {
            @Override public void onClick(View v)
            {
                checkAnswer(true);
            }
        });

        mFalseButton = findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener()
        {
            @Override public void onClick(View v)
            {
                checkAnswer(false);
            }
        });

        // Do something when user click "Next".
        mNextButton = findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                updateQuestion(1);
            }
        });

        mPreviousButton = findViewById(R.id.previous_button);
        mPreviousButton.setOnClickListener(new View.OnClickListener()
        {
            @Override public void onClick(View v)
            {
                updateQuestion(-1);
            }
        });

        mCheatButton = findViewById(R.id.cheat_button);
        mCheatButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
                Intent intent = CheatActivity.newIntent(MainActivity.this, answerIsTrue);
                startActivityForResult(intent, REQUEST_CODE_CHEAT);
            }
        });
        mIsCheater = false;
        updateQuestion(0);
    }

    @Override public void onStart()
    {
        super.onStart();
        Log.d(TAG, "onStart() called");
    }

    @Override public void onResume()
    {
        super.onResume();
        Log.d(TAG, "onResume() called");
    }

    @Override public void onPause()
    {
        super.onPause();
        Log.d(TAG, "onPause() called");
    }

    @Override public void onSaveInstanceState(Bundle savedInstanceState)
    {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, String.format("onSaveInstanceState(), currentIndex=%d", mCurrentIndex));
        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
    }

    @Override public void onStop()
    {
        super.onStop();
        Log.d(TAG, "onStop() called");
    }

    @Override public void onDestroy()
    {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (resultCode == Activity.RESULT_OK)
        {
            if (requestCode == REQUEST_CODE_CHEAT)
            {
                if (data != null)
                {
                    mIsCheater = CheatActivity.wasAnswerShown(data);
                }
            }
        }
    }

    private void updateQuestion(int step)
    {
        Log.i(TAG, String.format("OldIndex=%d", mCurrentIndex));
        mCurrentIndex = mCurrentIndex + step;
        if (mCurrentIndex < 0)
        {
            mCurrentIndex = mQuestionBank.length - 1;
        }
        else
        {
            mCurrentIndex = mCurrentIndex % mQuestionBank.length;
        }

        Log.i(TAG, String.format("NewIndex=%d", mCurrentIndex));

        int question = mQuestionBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);
        mIsCheater = false;
    }

    private void checkAnswer(boolean userPressedTrue)
    {
        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
        int messageResId;
        if (mIsCheater)
        {
            messageResId = R.string.judgment_toast;
        }
        else
        {
            messageResId = userPressedTrue == answerIsTrue ? R.string.correct_toast : R.string.incorrect_toast;
        }
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();
    }
}
