package amu.roboclub.ui;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import amu.roboclub.R;
import amu.roboclub.models.Project;
import amu.roboclub.ui.fragments.ProjectFragment;

public class ProjectDetailActivity extends AppCompatActivity {

    private Project project;



    private static float toPx(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }

    private static String getSmallImage(String url) {
        return url.replaceFirst("upload/", "upload/c_thumb,w_150,h_150/");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        project = (Project) getIntent().getSerializableExtra("project");

        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setTitle(project.name);
        }

        populateUI();
    }

    private void showImages() {
        if (project.images == null || project.images.size() == 0)
            return;


        final LinearLayout imageList = (LinearLayout) findViewById(R.id.gallery_list);

        for (final String imageUrl : project.images) {
            final ImageView im = new ImageView(this);

            final int pad = (int) toPx(5, this);
            im.setPadding(pad, pad, pad, pad);
            im.setLayoutParams(
                    new ViewGroup.LayoutParams(
                            (int) toPx(100, this),
                            (int) toPx(100, this)
                    )
            );

            final Context context = this;

            im.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, "Loading...", Toast.LENGTH_SHORT).show();

                    ImageView bigImage = new ImageView(getApplicationContext());
                    bigImage.setPadding(pad, pad, pad, pad);
                    Picasso.with(getApplicationContext())
                            .load(imageUrl)
                            .into(bigImage);

                    AlertDialog.Builder alert = new AlertDialog.Builder(context);
                    alert.setView(bigImage);
                    alert.show();
                }
            });

            Picasso.with(this)
                    .load(getSmallImage(imageUrl))
                    .into(im, new Callback() {
                        @Override
                        public void onSuccess() {
                            imageList.addView(im);
                            findViewById(R.id.gallery_card).setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onError() {
                        }
                    });
        }

    }

    private void showYoutube() {
        if (project.youtube == null)
            return;


        ImageView youtube = (ImageView) findViewById(R.id.youtube_thumb);
        Picasso.with(this)
                .load("https://img.youtube.com/vi/" + project.youtube + "/hqdefault.jpg")
                .into(youtube, new Callback() {
                    @Override
                    public void onSuccess() {
                        findViewById(R.id.youtube_card).setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onError() {

                    }
                });

        youtube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://youtu.be/" + project.youtube)));
            }
        });

    }

    private void showDocuments() {
        
    }

    private void populateUI() {
        if (project == null)
            return;

        ImageView image = (ImageView) findViewById(R.id.image);
        TextView team = (TextView) findViewById(R.id.team);
        TextView description = (TextView) findViewById(R.id.description);


        ProjectFragment.setImage(this, image, project.image);

        if (project.team != null)
            team.setText(project.team);
        else
            team.setText("---");

        if (project.description != null)
            description.setText(project.description);

        showYoutube();
        showImages();
        showDocuments();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
        }
        return super.onOptionsItemSelected(item);
    }

}
