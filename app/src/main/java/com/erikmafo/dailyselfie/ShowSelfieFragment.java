package com.erikmafo.dailyselfie;

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Listener} interface
 * to handle interaction events.
 * Use the {@link ShowSelfieFragment#newInstance} factory method toS
 * create an instance of this fragment.
 */
public class ShowSelfieFragment extends Fragment {


    private static final String ARG_SELFIE = "ARG_SELFIE";

    private Selfie mSelfie;
    private ImageView mImageView;
    private ImageButton mDeleteButton;
    private Listener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param selfie selfie to be displayed .
     * @return A new instance of fragment ShowSelfieFragment.
     */
    public static ShowSelfieFragment newInstance(Selfie selfie) {
        ShowSelfieFragment fragment = new ShowSelfieFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_SELFIE, selfie);
        fragment.setArguments(args);
        return fragment;
    }

    public ShowSelfieFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mSelfie = (Selfie) getArguments().get(ARG_SELFIE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_show_selfie, container, false);
        mImageView = (ImageView) view.findViewById(R.id.selfie_image);
        if (mSelfie != null) {
            mSelfie.loadOnTo(mImageView, 800, 800);
        }

        mDeleteButton = (ImageButton) view.findViewById(R.id.deleteButton);

        mDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDeleteButtonPressed();
            }
        });

        return view;


    }

    public void onDeleteButtonPressed() {
        if (mListener != null) {
            mListener.onDeleteButtonClicked(mSelfie);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (Listener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement Listener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        mSelfie = null;
        mImageView = null;
        mDeleteButton = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface Listener {
        public void onDeleteButtonClicked(Selfie selfie);
    }

}
