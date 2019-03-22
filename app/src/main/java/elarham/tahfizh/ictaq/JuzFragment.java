package elarham.tahfizh.ictaq;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class JuzFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,@Nullable  ViewGroup container, @Nullable  Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_juz, container, false);
    }
}
