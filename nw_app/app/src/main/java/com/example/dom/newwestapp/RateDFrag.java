package com.example.dom.newwestapp;


import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import static android.R.attr.rating;

/**
 * A simple {@link Fragment} subclass.
 */
public class RateDFrag extends DialogFragment {
    RatingBar ratingBar;
    Button getRating;
    EditText commentText;
    int markerId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rate_d, container, false);
        getDialog().setTitle("Rate Intersection");
        ratingBar = (RatingBar) view.findViewById(R.id.rating_bar);
        commentText = (EditText) view.findViewById(R.id.editText);
        getRating = (Button) view.findViewById(R.id.get_rating);
        getRating.setOnClickListener(new OnGetRatingClickListener());
        markerId = getArguments().getInt("id");
        return view;
    }

    private class OnGetRatingClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            //RatingBar$getRating() returns float value, you should cast(convert) it to string to display in a view
            String rating = String.valueOf(ratingBar.getRating());
            String comment = commentText.getText().toString();
            IntersectionDao dao = new IntersectionDao(getActivity());
            dao.updateRatingById(markerId, rating, comment);
            Intersection i = dao.findIntersectionById(markerId);
            System.out.println("Rating: " + i.getRating());
            System.out.println("Comment: " + i.getComments());
            dao.close();
            dismiss();
        }
    }

    public static RateDFrag getInstance(int id) {
        RateDFrag dialog = new RateDFrag();
        dialog.setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialog);
        Bundle args = new Bundle();
        args.putInt("id", id);
        dialog.setArguments(args);
        return dialog;
    }
}