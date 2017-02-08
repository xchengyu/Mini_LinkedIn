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
import com.example.chengyu.mini_linkedin.model.Experience;
import com.example.chengyu.mini_linkedin.util.DateUtils;

import java.util.Arrays;

/**
 * Created by Chengyu on 8/24/2016.
 */
public class ExperienceEditActivity extends EditBaseActivity<Experience> {
    public static final String KEY_EXPERIENCE_ID = "experience_id";
    public static final String KEY_EXPERIENCE = "experience";
    private static final String ACTIVITY_TITLE = "Edit Experience";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_experience_info_edit;
    }

    @Override
    protected String getSpecificTitle() {
        return ACTIVITY_TITLE;
    }

    @Override
    protected Experience initializeData() {
        return getIntent().getParcelableExtra(KEY_EXPERIENCE);
    }

    @Override
    protected void setupUIForEdit(@NonNull final Experience data) {
        ((EditText) findViewById(R.id.experience_edit_company))
                .setText(data.company);
        ((EditText) findViewById(R.id.experience_edit_title))
                .setText(data.title);
        ((EditText) findViewById(R.id.experience_edit_details))
                .setText(TextUtils.join("\n", data.details));

        ((EditText) findViewById(R.id.experience_edit_start_date))
                .setText(DateUtils.dateToString(data.startDate));
        ((EditText) findViewById(R.id.experience_edit_end_date))
                .setText(DateUtils.dateToString(data.endDate));
        ((TextView) findViewById(R.id.experience_edit_delete)).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent resultIntent = new Intent(view.getContext(), MainActivity.class);
                resultIntent.putExtra(KEY_EXPERIENCE_ID, data.id);
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        });
    }

    @Override
    protected void setupUIForCreate() {
        findViewById(R.id.experience_edit_delete).setVisibility(View.GONE);
    }

    @Override
    protected void saveAndExit(@Nullable Experience data) {
        if (data == null) {
            data = new Experience();
        }
        data.company = ((EditText) findViewById(R.id.experience_edit_company))
                .getText().toString();
        data.title = ((EditText) findViewById(R.id.experience_edit_title))
                .getText().toString();
        data.startDate = DateUtils.StringTodate(((EditText) findViewById(R.id.experience_edit_start_date))
                .getText().toString());
        data.endDate = DateUtils.StringTodate(((EditText) findViewById(R.id.experience_edit_end_date))
                .getText().toString());
        data.details = Arrays.asList(TextUtils.split(
                ((EditText) findViewById(R.id.experience_edit_details)).getText().toString(), "\n"));
        Intent resultIntent = new Intent();
        resultIntent.putExtra(KEY_EXPERIENCE, data);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }
}
