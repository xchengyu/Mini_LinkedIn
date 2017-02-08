package com.example.chengyu.mini_linkedin;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.chengyu.mini_linkedin.model.Experience;
import com.example.chengyu.mini_linkedin.model.Project;
import com.example.chengyu.mini_linkedin.util.DateUtils;

import java.util.Arrays;

/**
 * Created by Chengyu on 8/24/2016.
 */
public class ProjectEditActivity extends EditBaseActivity<Project> {
    public static final String KEY_PROJECT_ID = "project_id";
    public static final String KEY_PROJECT = "project";
    private static final String ACTIVITY_TITLE = "Edit Project";
    @Override
    protected int getLayoutId() {
        return R.layout.activity_project_info_edit;
    }

    @Override
    protected String getSpecificTitle() {
        return ACTIVITY_TITLE;
    }

    @Override
    protected Project initializeData() {
        return getIntent().getParcelableExtra(KEY_PROJECT);
    }

    @Override
    protected void setupUIForEdit(@NonNull final Project data) {
        ((EditText) findViewById(R.id.project_edit_name))
                .setText(data.project_name);
        ((EditText) findViewById(R.id.project_edit_details))
                .setText(TextUtils.join("\n", data.details));
        ((EditText) findViewById(R.id.project_edit_start_date))
                .setText(DateUtils.dateToString(data.startDate));
        ((EditText) findViewById(R.id.project_edit_end_date))
                .setText(DateUtils.dateToString(data.endDate));
        ((TextView) findViewById(R.id.project_edit_delete)).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent resultIntent = new Intent(view.getContext(), MainActivity.class);
                resultIntent.putExtra(KEY_PROJECT_ID, data.id);
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        });
    }

    @Override
    protected void setupUIForCreate() {
        findViewById(R.id.project_edit_delete).setVisibility(View.GONE);
    }

    @Override
    protected void saveAndExit(@Nullable Project data) {
        if (data == null) {
            data = new Project();
        }
        data.project_name = ((EditText) findViewById(R.id.project_edit_name))
                .getText().toString();
        data.startDate = DateUtils.StringTodate(((EditText) findViewById(R.id.project_edit_start_date))
                .getText().toString());
        data.endDate = DateUtils.StringTodate(((EditText) findViewById(R.id.project_edit_end_date))
                .getText().toString());
        data.details = Arrays.asList(TextUtils.split(
                ((EditText) findViewById(R.id.project_edit_details)).getText().toString(), "\n"));
        Intent resultIntent = new Intent();
        resultIntent.putExtra(KEY_PROJECT, data);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }
}
