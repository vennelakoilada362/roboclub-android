package amu.roboclub.ui.fragments;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import amu.roboclub.R;
import amu.roboclub.models.Contribution;
import amu.roboclub.ui.viewholder.ContributionHolder;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ContributionFragment extends Fragment {
    public ContributionFragment() {
        // Required empty public constructor
    }

    public static ContributionFragment newInstance() {
        return new ContributionFragment();
    }

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_contribution, container, false);

        ButterKnife.bind(this, root);

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setReverseLayout(true);
        llm.setStackFromEnd(true);
        recyclerView.setLayoutManager(llm);

        final Snackbar snackbar = Snackbar.make(recyclerView, "Loading Contributors", Snackbar.LENGTH_INDEFINITE);
        snackbar.show();

        DatabaseReference contributionReference = FirebaseDatabase.getInstance().getReference("contribution");
        FirebaseRecyclerAdapter contributionAdapter = new FirebaseRecyclerAdapter<Contribution, ContributionHolder>
                (Contribution.class, R.layout.item_contribution, ContributionHolder.class, contributionReference) {

            @Override
            protected void populateViewHolder(ContributionHolder holder, Contribution contribution, int position) {
                if (snackbar.isShown())
                    snackbar.dismiss();
                holder.contributor.setText(contribution.contributor);
                holder.purpose.setText(contribution.purpose);
                holder.remark.setText(contribution.remark);
                holder.amount.setText(contribution.amount);
            }
        };

        recyclerView.setAdapter(contributionAdapter);

        return root;
    }
}
