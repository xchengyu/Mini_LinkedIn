package com.example.chengyu.mini_linkedin;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.chengyu.mini_linkedin.model.BasicInfo;
import com.example.chengyu.mini_linkedin.model.Education;
import com.example.chengyu.mini_linkedin.model.Experience;
import com.example.chengyu.mini_linkedin.model.Project;
import com.example.chengyu.mini_linkedin.util.DateUtils;
import com.example.chengyu.mini_linkedin.util.ImageUtils;
import com.example.chengyu.mini_linkedin.util.ModelUtils;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
/*
 *Notification:
 *Statements and functions which ends up with '//test' means using
 *another method to avoid crash after we set up a basic info picture
 *and restart the program. This method retrieves the bitmap from
 * uri which we get from external storage and stores the bitmap in a
 * new file. After that, when we want to get the bitmap we chose, the
 * program will load the bitmap from this file.
 * Known Bug:
 * After we set up a basic info picture, close the program and then restart
 * the program, the program will crash. This is caused by google Android 6.0
 * new feature:
 * Yep, this is by design, as described on Android Developer site.
 * For security reasons, the permissions are temporary, so once the client
 * app's task stack is finished, the file is no longer accessible. You must get
 * the file data when you receive the intent answer, in the onActivityResult
 * method. Store a copy of the file data, because the file won't be available
 * anymore when onActivityResult returns.
 */
public class MainActivity extends AppCompatActivity {

    private static final int REQ_CODE_EDIT_EDUCATION = 100;
    private static final int REQ_CODE_EDIT_EXPERIENCE = 101;
    private static final int REQ_CODE_EDIT_PROJECT = 102;
    private static final int REQ_CODE_EDIT_BASIC_INFO = 103;

    private static final String MODEL_EDUCATIONS = "educations";
    private static final String MODEL_EXPERIENCES = "experiences";
    private static final String MODEL_PROJECTS = "projects";
    private static final String MODEL_BASIC_INFO = "basic_info";

    private BasicInfo basicInfo;
    private List<Education> educations;
    private List<Experience> experiences;
    private List<Project> projects;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.main_activity_icon);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        loadData();
        setupUI();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        if (item.getItemId() == R.id.toPdf) {
//            //Toast.makeText(this, "haha", Toast.LENGTH_SHORT).show();
//            // create a new document
//            PdfDocument document = null;
//            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
//                document = new PdfDocument();
//                // crate a page description
//                PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(100, 100, 1).create();
//
//                // start a page
//                PdfDocument.Page page = document.startPage(pageInfo);
//
//                // draw something on the page
//                View content = findViewById(R.id.rootView);
//                content.draw(page.getCanvas());
//
//                // finish the page
//                document.finishPage(page);
//
//                // add more pages
//
//                // write the document content
//                try {
//                    document.writeTo(new FileOutputStream("pdftest.txt"));
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//                // close the document
//                document.close();
//            }
//
//
//            return true;
//        }
        return super.onOptionsItemSelected(item);
    }

    private void loadData() {
        BasicInfo savedBasicInfo = ModelUtils.read(this,
                MODEL_BASIC_INFO,
                new TypeToken<BasicInfo>() {
                });
        this.basicInfo = savedBasicInfo == null ? new BasicInfo() : savedBasicInfo;

        List<Education> savedEducations = ModelUtils.read(this,
                MODEL_EDUCATIONS,
                new TypeToken<List<Education>>() {
                });
        this.educations = savedEducations == null ? new ArrayList<Education>() : savedEducations;

        List<Experience> savedExperiences = ModelUtils.read(this,
                MODEL_EXPERIENCES,
                new TypeToken<List<Experience>>() {
                });
        this.experiences = savedExperiences == null ? new ArrayList<Experience>() : savedExperiences;

        List<Project> savedProjects = ModelUtils.read(this,
                MODEL_PROJECTS,
                new TypeToken<List<Project>>() {
                });
        this.projects = savedProjects == null ? new ArrayList<Project>() : savedProjects;
    }

    private void setupUI() {
        setContentView(R.layout.activity_main);
        ImageButton addEducation = (ImageButton) findViewById(R.id.add_education_button);
        addEducation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EducationEditActivity.class);
                startActivityForResult(intent, REQ_CODE_EDIT_EDUCATION);
            }
        });
        ImageButton addExperience = (ImageButton) findViewById(R.id.add_experience_button);
        addExperience.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ExperienceEditActivity.class);
                startActivityForResult(intent, REQ_CODE_EDIT_EXPERIENCE);
            }
        });
        ImageButton addProject = (ImageButton) findViewById(R.id.add_project_button);
        addProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ProjectEditActivity.class);
                startActivityForResult(intent, REQ_CODE_EDIT_PROJECT);
            }
        });
        setupBasicInfo();
        setupEducations();
        setupExperiences();
        setupProjects();
    }

    private void setupBasicInfo() {
        ((TextView) findViewById(R.id.personal_info_name)).setText(TextUtils.isEmpty(basicInfo.name) ?
                "Your name" : basicInfo.name);
        ((TextView) findViewById(R.id.personal_info_email)).setText(TextUtils.isEmpty(basicInfo.email) ?
                "Your name" : basicInfo.email);
        ImageView userPicture = (ImageView) findViewById(R.id.user_picture);

        if (basicInfo.imageUri != null) {
            ImageUtils.loadImage(this, basicInfo.imageUri, userPicture);
        } else {
            userPicture.setImageResource(R.drawable.user_ghost);
        }

//        if (basicInfo.path != null && !basicInfo.path.equals("")) {
//            ImageUtils.loadImage(getApplicationContext(), basicInfo.path, userPicture);
//        } else {
//            userPicture.setImageResource(R.drawable.user_ghost);
//        }//test
        findViewById(R.id.edit_basic_info).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, BasicInfoEditActivity.class);
                intent.putExtra(BasicInfoEditActivity.KEY_BASIC_INFO, basicInfo);
                startActivityForResult(intent, REQ_CODE_EDIT_BASIC_INFO);
            }
        });
    }

    private void setupEducations() {
        LinearLayout educationList = (LinearLayout) findViewById(R.id.education_list);
        educationList.removeAllViews();
        for (Education education : educations) {
            View educationView = getLayoutInflater().inflate(R.layout.education_item, null);
            setupEducation(educationView, education);
            educationList.addView(educationView);
        }
    }

    private void setupEducation(View educationView, final Education education) {
        String dateString = DateUtils.dateToString(education.startDate) + "~" +
                DateUtils.dateToString(education.endDate);
        TextView education_school = (TextView) educationView.findViewById(R.id.education_school);
        education_school.setText(education.school + " " + education.major + " " + "(" + dateString + ")");
        TextView education_courses = (TextView) educationView.findViewById(R.id.education_courses);
        education_courses.setText(formatItem(education.courses));
        final ImageButton editEducation = (ImageButton) educationView.findViewById(R.id.edit_education_btn);
        editEducation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EducationEditActivity.class);
                intent.putExtra(EducationEditActivity.KEY_EDUCATION, education);
                startActivityForResult(intent, REQ_CODE_EDIT_EDUCATION);
            }
        });
    }

    private void setupExperiences() {
        LinearLayout experienceList = (LinearLayout) findViewById(R.id.experience_list);
        experienceList.removeAllViews();
        for (Experience experience : experiences) {
            View experienceView = getLayoutInflater().inflate(R.layout.experience_item, null);
            setupExperience(experienceView, experience);
            experienceList.addView(experienceView);
        }
    }

    private void setupExperience(View experienceView, final Experience experience) {
        String dateString = DateUtils.dateToString(experience.startDate) + "~" +
                DateUtils.dateToString(experience.endDate);
        TextView experience_company = (TextView) experienceView.findViewById(R.id.experience_company);
        experience_company.setText(experience.company + " " + experience.title + " " + "(" + dateString + ")");
        TextView experience_details = (TextView) experienceView.findViewById(R.id.experience_details);
        experience_details.setText(formatItem(experience.details));
        final ImageButton editExperience = (ImageButton) experienceView.findViewById(R.id.edit_experience_btn);
        editExperience.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ExperienceEditActivity.class);
                intent.putExtra(ExperienceEditActivity.KEY_EXPERIENCE, experience);
                startActivityForResult(intent, REQ_CODE_EDIT_EXPERIENCE);
            }
        });

    }

    private void setupProjects() {
        LinearLayout projectList = (LinearLayout) findViewById(R.id.project_list);
        projectList.removeAllViews();
        for (Project project : projects) {
            View projectView = getLayoutInflater().inflate(R.layout.project_item, null);
            setupProject(projectView, project);
            projectList.addView(projectView);
        }
    }

    private void setupProject(View projectView, final Project project) {
        String dateString = DateUtils.dateToString(project.startDate) + "~" +
                DateUtils.dateToString(project.endDate);
        TextView project_name = (TextView) projectView.findViewById(R.id.project_name);
        project_name.setText(project.project_name + " " + "(" + dateString + ")");
        TextView project_details = (TextView) projectView.findViewById(R.id.project_details);
        project_details.setText(formatItem(project.details));
        final ImageButton editProject = (ImageButton) projectView.findViewById(R.id.edit_project_btn);
        editProject.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ProjectEditActivity.class);
                intent.putExtra(ProjectEditActivity.KEY_PROJECT, project);
                startActivityForResult(intent, REQ_CODE_EDIT_PROJECT);
            }
        });
    }


    private String formatItem(List<String> items) {
        StringBuilder sb = new StringBuilder();
        for (String item : items) {
            sb.append(' ').append('-').append(' ').append(item).append('\n');
        }
        if (sb.length() > 0) {
            sb.substring(0, sb.length() - 1);
        }
        return sb.toString();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQ_CODE_EDIT_BASIC_INFO:
                    BasicInfo basicInfoString = data.getParcelableExtra(BasicInfoEditActivity.KEY_BASIC_INFO);
                    updateBasicInfo(basicInfoString);
                    break;
                case REQ_CODE_EDIT_EDUCATION:
                    String educationID = data.getStringExtra(EducationEditActivity.KEY_EDUCATION_ID);
                    if (educationID != null) {
                        deleteEducation(educationID);
                    } else {
                        Education education = data.getParcelableExtra(EducationEditActivity.KEY_EDUCATION);
                        updateEducation(education);
                    }
                    break;
                case REQ_CODE_EDIT_EXPERIENCE :
                    String experienceID = data.getStringExtra(ExperienceEditActivity.KEY_EXPERIENCE_ID);
                    if (experienceID != null) {
                        deleteExperience(experienceID);
                    } else {
                        Experience experience = data.getParcelableExtra(ExperienceEditActivity.KEY_EXPERIENCE);
                        updateExperience(experience);
                    }
                    break;
                case REQ_CODE_EDIT_PROJECT :
                    String projectID = data.getStringExtra(ProjectEditActivity.KEY_PROJECT_ID);
                    if (projectID != null) {
                        deleteProject(projectID);
                    } else {
                        Project project = data.getParcelableExtra(ProjectEditActivity.KEY_PROJECT);
                        updateProject(project);
                    }
                    break;
            }
        }
    }

    private void updateBasicInfo(BasicInfo basicInfoString) {
        ModelUtils.save(this, MODEL_BASIC_INFO, basicInfoString);
        this.basicInfo = basicInfoString;
        setupBasicInfo();
    }

    private void updateEducation(Education education) {
        boolean found = false;
        for (int i = 0; i < educations.size(); i++) {
            Education cur = educations.get(i);
            if (TextUtils.equals(cur.id, education.id)) {
                found = true;
                educations.set(i, education);
                break;
            }
        }
        if (!found) {
            educations.add(education);
        }
        ModelUtils.save(MainActivity.this, MODEL_EDUCATIONS, educations);
        setupEducations();
        return;
    }

    private void updateExperience(Experience experience) {
        boolean found = false;
        for (int i = 0; i < experiences.size(); i++) {
            Experience cur = experiences.get(i);
            if (TextUtils.equals(cur.id, experience.id)) {
                found = true;
                experiences.set(i, experience);
                break;
            }
        }
        if (!found) {
            experiences.add(experience);
        }
        ModelUtils.save(MainActivity.this, MODEL_EXPERIENCES, experiences);
        setupExperiences();
        return;
    }

    private void updateProject(Project project) {
        boolean found = false;
        for (int i = 0; i < projects.size(); i++) {
            Project cur = projects.get(i);
            if (TextUtils.equals(cur.id, project.id)) {
                found = true;
                projects.set(i, project);
                break;
            }
        }
        if (!found) {
            projects.add(project);
        }
        ModelUtils.save(MainActivity.this, MODEL_PROJECTS, projects);
        setupProjects();
        return;
    }

    private void deleteEducation(@NonNull String educationID) {
        for (int i = 0; i < educations.size(); i++) {
            Education cur = educations.get(i);
            if (TextUtils.equals(cur.id, educationID)) {
                educations.remove(i);
            }
        }
        ModelUtils.save(MainActivity.this, MODEL_EDUCATIONS, educations);
        setupEducations();
        return;
    }

    private void deleteExperience(String experienceID) {
        for (int i = 0; i < experiences.size(); i++) {
            Experience cur = experiences.get(i);
            if (TextUtils.equals(cur.id, experienceID)) {
                experiences.remove(i);
            }
        }
        ModelUtils.save(MainActivity.this, MODEL_EXPERIENCES, experiences);
        setupExperiences();
        return;
    }

    private void deleteProject(String projectID) {
        for (int i = 0; i < projects.size(); i++) {
            Project cur = projects.get(i);
            if (TextUtils.equals(cur.id, projectID)) {
                projects.remove(i);
            }
        }
        ModelUtils.save(MainActivity.this, MODEL_PROJECTS, projects);
        setupProjects();
        return;
    }
}
