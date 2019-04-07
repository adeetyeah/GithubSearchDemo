package com.example.githubsearchdemo;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;

/**
 * Custom Adapter for {@link OrgRepoData}
 */
public class RepositoryAdapter extends RecyclerView.Adapter<RepositoryAdapter.MyViewHolder> {

    public interface RepositoryListenerInterface {
        void onRepositoryClick(int position);
    }

    private ArrayList<OrgRepoData> repoList;
    private RepositoryListenerInterface repositoryListenerInterface;


    RepositoryAdapter(ArrayList<OrgRepoData> arrayList, RepositoryListenerInterface repoListenerInterface) {
        this.repoList = arrayList;
        this.repositoryListenerInterface = repoListenerInterface;
    }

    static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView repoName;
        TextView repoDescription;
        TextView repoStars;
        RepositoryListenerInterface repoListenerInterface;

        MyViewHolder(@NonNull final View itemView, final RepositoryListenerInterface repoListenerInterface) {
            super(itemView);
            this.repoName = itemView.findViewById(R.id.repoName);
            this.repoDescription = itemView.findViewById(R.id.repoDescription);
            this.repoStars = itemView.findViewById(R.id.repoStars);

            this.repoListenerInterface = repoListenerInterface;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(final View view) {
            repoListenerInterface.onRepositoryClick(getAdapterPosition());
        }
    }

    @NonNull
    @Override
    public RepositoryAdapter.MyViewHolder onCreateViewHolder(@NonNull final ViewGroup viewGroup, final int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list_view, viewGroup, false);
        return new MyViewHolder(view, repositoryListenerInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull final RepositoryAdapter.MyViewHolder myViewHolder, final int i) {
        final TextView repoName = myViewHolder.repoName;
        final TextView repoDescription = myViewHolder.repoDescription;
        final TextView repoStars = myViewHolder.repoStars;
        final String star = " ✰";

        repoName.setText(repoList.get(i).getName());
        repoDescription.setText(repoList.get(i).getDescription());
        repoStars.setText(repoList.get(i).getStars() + star);
    }

    @Override
    public int getItemCount() {
        return repoList.size();
    }
}
