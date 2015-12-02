package com.erikmafo.dailyselfie;

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Listener} interface
 * to handle interaction events.
 * Use the {@link SelfieGalleryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SelfieGalleryFragment extends Fragment {


    private static final String ARG_SELFIES = "SELFIES";

    private ImageAdapter mAdapter;
    private GridView mGridView;
    private Listener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param selfies selfies to display in the gallary.
     * @return A new instance of fragment SelfieGalleryFragment.
     */
    public static SelfieGalleryFragment newInstance(Selfie[] selfies) {
        SelfieGalleryFragment fragment = new SelfieGalleryFragment();
        Bundle args = new Bundle();
        args.putParcelableArray(ARG_SELFIES, selfies);
        fragment.setArguments(args);
        return fragment;
    }

    public SelfieGalleryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Selfie[] selfies = {};
        if (getArguments() != null) {
            selfies = (Selfie[]) getArguments().get(ARG_SELFIES);
        }
        mAdapter = new ImageAdapter(getActivity(), new ArrayList<>(Arrays.asList(selfies)));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_selfie_gallery, container, false);
        mGridView = (GridView) view.findViewById(R.id.selfie_gallery_gridview);

        mGridView.setAdapter(mAdapter);

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mListener != null) {
                    Selfie selfie = mAdapter.getItem(position);
                    mListener.onSelfieItemClick(selfie);
                }
            }
        });

        return view;
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
    }

    public void add(Selfie selfie) {
        mAdapter.add(selfie);
        mAdapter.notifyDataSetChanged();
    }

    public List<Selfie> getSelfieList() {
        return mAdapter.getList();
    }

    public void remove(Selfie selfie) {
        mAdapter.remove(selfie);
        mAdapter.notifyDataSetChanged();
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
        public void onSelfieItemClick(Selfie selfieItem);
    }

}
