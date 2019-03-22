package com.example.githubsearchdemo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;

/**
 * Custom Array Adapter for {@link OrgRepoData}
 */
public class RepositoryAdapter extends ArrayAdapter<OrgRepoData> {

    RepositoryAdapter(Context context, ArrayList<OrgRepoData> arrayList) {
        super(context, 0, arrayList);
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, @NonNull final ViewGroup parent) {
        OrgRepoData repository = getItem(position);
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_list_view, parent, false);
        }

        TextView repoName = convertView.findViewById(R.id.repoName);
        TextView repoDescription = convertView.findViewById(R.id.repoDescription);
        TextView repoStars = convertView.findViewById(R.id.repoStars);
        final String star = " ✰";

        repoName.setText(repository.getName());
        repoDescription.setText(repository.getDescription());
        repoStars.setText(repository.getStars() + star);
        return convertView;
    }
}
