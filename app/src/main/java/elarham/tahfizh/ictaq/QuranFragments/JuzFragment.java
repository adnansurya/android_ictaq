package elarham.tahfizh.ictaq.QuranFragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import elarham.tahfizh.ictaq.R;

public class JuzFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,@Nullable  ViewGroup container, @Nullable  Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_juz, container, false);
    }
}
