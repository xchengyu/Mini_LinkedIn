package com.example.chengyu.mini_linkedin;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.chengyu.mini_linkedin.model.Education;
import com.example.chengyu.mini_linkedin.util.DateUtils;
import com.example.chengyu.mini_linkedin.util.ModelUtils;

import java.util.Arrays;

/**
 * Created by Chengyu on 8/24/2016.
 */
public class EducationEditActivity extends EditBaseActivity<Education> {
    public static final String KEY_EDUCATION_ID = "education_id";
    public static final String KEY_EDUCATION = "education";
    private static final String ACTIVITY_TITLE = "Edit Education";
    @Override
    protected int getLayoutId() {
        return R.layout.activity_education_info_edit;
    }

    @Override
    protected String getSpecificTitle() {
        return ACTIVITY_TITLE;
    }

    @Override
    protected Education initializeData() {
        return getIntent().getParcelableExtra(KEY_EDUCATION);
    }

    @Override
    protected void setupUIForEdit(@NonNull final Education data) {
        ((EditText) findViewById(R.id.education_edit_school))
                .setText(data.school);
        ((EditText) findViewById(R.id.education_edit_major))
                .setText(data.major);
        ((EditText) findViewById(R.id.education_edit_courses))
                .setText(TextUtils.join("\n", data.courses));

        ((EditText) findViewById(R.id.education_edit_start_date))
                .setText(DateUtils.dateToString(data.startDate));
        ((EditText) findViewById(R.id.education_edit_end_date))
                .setText(DateUtils.dateToString(data.endDate));
        ((TextView) findViewById(R.id.education_edit_delete)).setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent resultIntent = new Intent(view.getContext(), MainActivity.class);
                resultIntent.putExtra(KEY_EDUCATION_ID, data.id);
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        });
    }

    @Override
    protected void setupUIForCreate() {
        findViewById(R.id.education_edit_delete).setVisibility(View.GONE);
    }

    @Override
    protected void saveAndExit(@Nullable Education data) {
        if (data == null) {
            data = new Education();
        }
        data.school = ((EditText) findViewById(R.id.education_edit_school))
                .getText().toString();
        data.major = ((EditText) findViewById(R.id.education_edit_major))
                .getText().toString();
        data.startDate = DateUtils.StringTodate(((EditText) findViewById(R.id.education_edit_start_date))
                .getText().toString());
        data.endDate = DateUtils.StringTodate(((EditText) findViewById(R.id.education_edit_end_date))
                .getText().toString());
        data.courses = Arrays.asList(TextUtils.split(
                ((EditText) findViewById(R.id.education_edit_courses)).getText().toString(), "\n"));
        Intent resultIntent = new Intent();
        resultIntent.putExtra(KEY_EDUCATION, data);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }
}
