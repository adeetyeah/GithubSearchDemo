package com.example.githubsearchdemo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import static com.example.githubsearchdemo.GithubSearchConstants.GITHUB_ORGS_API;
import static com.example.githubsearchdemo.GithubSearchConstants.REPOS;
import static com.example.githubsearchdemo.GithubSearchConstants.REPO_URL;

/**
 * Activity that lets you search a github orgs and provides the 3 most popular repositories in the org.
 */
public class GithubSearchActivity extends AppCompatActivity {

    private static String TAG = GithubSearchActivity.class.getSimpleName();
    private static ListView listView;
    private static TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        final EditText orgName = findViewById(R.id.orgName);
        final Button getRepos = findViewById(R.id.getRepos);
        listView = findViewById(R.id.listView);

        textView = findViewById(R.id.textView);
        textView.setVisibility(View.GONE);

        getRepos.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("CheckResult")
            @Override
            public void onClick(View view) {
                final String organization = orgName.getText().toString().toLowerCase();
                textView.setVisibility(View.GONE);
                textView.setText(null);
                textView.setText(R.string.text_view);
                textView.append(organization);
                Log.d(TAG, "Getting repositories for " + organization);

                //getRepositoriesUsingAsyncTask(organization);

                getRepositoriesUsingRxAndroid(organization);

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> adapterView, final View view, final int i, final long l) {
                final OrgRepoData repository = (OrgRepoData) adapterView.getAdapter().getItem(i);
                final Intent webViewIntent = new Intent(GithubSearchActivity.this, WebViewActivity.class);
                webViewIntent.putExtra(REPO_URL, repository.getGithubUrl());
                Log.d(TAG, "Starting activity with " + webViewIntent + " extras - " + webViewIntent.getExtras());
                startActivity(webViewIntent);
            }
        });
    }

    /**
     * Async class to get an organization's repository details in the background. Once the details are fetched,
     * the main thread is updated with the 3 most popular repositories for the organization.
     */
    private static class GetOrganizationRepositoryDetails extends AsyncTask<String, Void, ArrayList> {

        private WeakReference<GithubSearchActivity> githubSearchActivityWeakReference;

        // only retain a weak reference to the activity
        GetOrganizationRepositoryDetails(GithubSearchActivity context) {
            githubSearchActivityWeakReference = new WeakReference<>(context);
        }

        @Override
        protected ArrayList doInBackground(final String... strings) {
            final String repoUrl = strings[0];
            return GithubSearchUtils.getAndFilterRepositoriesFromOrganization(repoUrl);
        }

        @Override
        protected void onPostExecute(final ArrayList repositoryList) {
            super.onPostExecute(repositoryList);

            GithubSearchActivity githubSearchActivity = githubSearchActivityWeakReference.get();
            if (githubSearchActivity == null || githubSearchActivity.isFinishing()) return;

            displayFetchedData(githubSearchActivity, repositoryList);
        }
    }



    private void getRepositoriesUsingAsyncTask(final String organization) {
        new GetOrganizationRepositoryDetails(GithubSearchActivity.this).execute(GITHUB_ORGS_API + organization + REPOS);
    }

    private void getRepositoriesUsingRxAndroid(final String organization) {
        GithubSearchUtils.getObservableRepositories(GITHUB_ORGS_API + organization + REPOS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<ArrayList>() {
                    @Override
                    public void onSubscribe(final Disposable d) {
                        Log.d(TAG, "onSubscribe");
                    }

                    @Override
                    public void onNext(final ArrayList arrayList) {
                        Log.d(TAG, "onNext");

                        displayFetchedData(GithubSearchActivity.this, arrayList);
                    }

                    @Override
                    public void onError(final Throwable e) {
                        Log.d(TAG, "onError - " + e.toString());

                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete()");

                    }
                });
    }

    /**
     * Displays the result present in the {@link ArrayList<OrgRepoData>} in the list view, using the {@link RepositoryAdapter}
     * @param context context of the activity
     * @param repoList {@link ArrayList<OrgRepoData>} object
     */
    private static void displayFetchedData(final Context context, final ArrayList repoList) {
        if(repoList!=null || repoList.size() != 0) {
            Toast.makeText(context, R.string.toast_done, Toast.LENGTH_SHORT).show();
            final RepositoryAdapter repositoryAdapter = new RepositoryAdapter(context, repoList);
            listView.setAdapter(repositoryAdapter);
            textView.setVisibility(View.VISIBLE);
            repositoryAdapter.notifyDataSetChanged();
        }
        else {
            Log.e(TAG, "Error in fetching data");
        }
    }
}
