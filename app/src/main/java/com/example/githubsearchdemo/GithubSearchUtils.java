package com.example.githubsearchdemo;

import android.util.Log;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import static com.example.githubsearchdemo.GithubSearchConstants.REPO_DESCRIPTION;
import static com.example.githubsearchdemo.GithubSearchConstants.REPO_NAME;
import static com.example.githubsearchdemo.GithubSearchConstants.REPO_STARS;
import static com.example.githubsearchdemo.GithubSearchConstants.REPO_URL;

/**
 * Utils class for {@link GithubSearchActivity}
 */
class GithubSearchUtils {

    private static String TAG = GithubSearchUtils.class.getSimpleName();

    /**
     * Returns an observable object containing all the filtered repositories.
     * @param url requestUrl for an organization
     * @return {@link Observable<ArrayList>} object
     */
    static Observable<ArrayList> getObservableRepositories(final String url) {
        return Observable.defer(new Callable<ObservableSource<? extends ArrayList>>() {
            @Override
            public ObservableSource<? extends ArrayList> call() {
                return Observable.just(getAndFilterRepositoriesFromOrganization(url));
            }
        });
    }

    /**
     * Gets all repositories for an organization and sorts it based on the number of stars.
     * @param url requestUrl for an organization
     * @return {@link ArrayList<OrgRepoData>} object
     */
    static ArrayList getAndFilterRepositoriesFromOrganization(final String url) {
        final String jsonResponse = makeServiceCall(url);

        Map<OrgRepoData, Integer> map = new HashMap<>();

        if(jsonResponse != null) {
            try {
                final JSONArray jsonArray = new JSONArray(jsonResponse);
                for (int i = 0; i < jsonArray.length(); i++) {
                    final JSONObject repoObject = jsonArray.getJSONObject(i);
                    final String name = repoObject.get(REPO_NAME).toString();
                    final String github_url = repoObject.get(REPO_URL).toString();
                    final String description = repoObject.get(REPO_DESCRIPTION).toString();
                    final int stars = Integer.parseInt(repoObject.get(REPO_STARS).toString());

                    OrgRepoData repositoryObject = new OrgRepoData(name, description, github_url, stars);
                    map.put(repositoryObject, stars);
                }
            }
            catch(JSONException e) {
                e.printStackTrace();
            }
        }
        // Sorting map to display repos with most stars
        map = sortByComparator(map, false);

        return new ArrayList<>(map.keySet());
    }

    /**
     * This method makes a service call to fetch the response for an org
     * @param reqUrl url for a particular organization
     * @return {@link String} response
     */
    private static String makeServiceCall(String reqUrl) {
        String response = null;
        try {
            final URL url = new URL(reqUrl);
            final InputStream in = url.openStream();
            response = convertStreamToString(in);

        } catch (MalformedURLException e) {
            Log.e(TAG, "MalformedURLException: " + e.getMessage());
        } catch (ProtocolException e) {
            Log.e(TAG, "ProtocolException: " + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "IOException: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
        return response;
    }

    /**
     * Converts an input stream to a string
     * @param inputStream inputStream
     * @return {@link String} response
     */
    private static String convertStreamToString(InputStream inputStream){
        final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        final StringBuilder sb = new StringBuilder();

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    /**
     * Sorts a given map based on a parameter, in ascending or descending order.
     * @param unsortedMap {@link Map} object of type {@link OrgRepoData} and {@link Integer}
     * @param ascendingOrder boolean value; true if ascending, false otherwise
     * @return sorted {@link Map} object of type {@link OrgRepoData} and {@link Integer}
     */
    private static Map<OrgRepoData, Integer> sortByComparator(final Map<OrgRepoData, Integer> unsortedMap, final boolean ascendingOrder) {
        final List<Map.Entry<OrgRepoData, Integer>> list = new LinkedList<>(unsortedMap.entrySet());
        // Sorting the list based on values
        Collections.sort(list, new Comparator<Map.Entry<OrgRepoData, Integer>>() {
            @Override
            public int compare(final Map.Entry<OrgRepoData, Integer> entry1, final Map.Entry<OrgRepoData, Integer> entry2) {
                if(ascendingOrder) {
                    return entry1.getValue().compareTo(entry2.getValue());
                }
                else {
                    return entry2.getValue().compareTo(entry1.getValue());

                }
            }
        });

        // Maintaining insertion order with the help of LinkedList
        final Map<OrgRepoData, Integer> sortedMap = new LinkedHashMap<>();
        for(Map.Entry<OrgRepoData, Integer> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }
        return sortedMap;
    }
}
