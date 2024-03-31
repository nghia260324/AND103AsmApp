package com.ph41626.and103_assignment.Fragment;

import static com.ph41626.and103_assignment.Services.Services.convertLocalhostToIpAddress;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ph41626.and103_assignment.Adapter.RecyclerViewSettingsAdapter;
import com.ph41626.and103_assignment.Activity.MainActivity;
import com.ph41626.and103_assignment.Model.Setting;
import com.ph41626.and103_assignment.Model.User;
import com.ph41626.and103_assignment.Model.ViewModel;
import com.ph41626.and103_assignment.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    private ArrayList<Setting> listSettings;
    private RecyclerView rcv_settings;
    private RecyclerViewSettingsAdapter settingsAdapter;
    private ImageView img_avatar;
    private TextView tv_email,tv_name;
    private ViewModel viewModel;
    private MainActivity mainActivity;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        initUI(view);
        DataListSettings();
        FillRecyclerViewSetting();

        UpdateUserWhenDataChanges();
        return view;
    }
    private void UpdateUserWhenDataChanges() {
        viewModel.getChangeUser().observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                tv_name.setText(user.getName());
                tv_email.setText(user.getEmail());
                Glide.with(getContext()).load(convertLocalhostToIpAddress(user.getAvatar())).error(R.drawable.img_home_1).placeholder(R.drawable.img_home_1).into(img_avatar);
            }
        });
    }
    private void FillRecyclerViewSetting() {
        settingsAdapter = new RecyclerViewSettingsAdapter(getContext(),listSettings,mainActivity);
        rcv_settings.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        rcv_settings.setAdapter(settingsAdapter);
    }
    private void DataListSettings() {
        listSettings.add(new Setting(R.drawable.baseline_account_circle,"User Information"));
        listSettings.add(new Setting(R.drawable.baseline_manage_accounts,"Inventory Management"));
        listSettings.add(new Setting(R.drawable.baseline_shopping_cart,"My Cart"));
        listSettings.add(new Setting(R.drawable.baseline_login,"Logout"));
    }
    private void initUI(View view) {
        mainActivity = (MainActivity) getActivity();
        viewModel = new ViewModelProvider(requireActivity()).get(ViewModel.class);
        rcv_settings = view.findViewById(R.id.rcv_settings);
        listSettings = new ArrayList<>();

        img_avatar = view.findViewById(R.id.img_avatar);
        tv_email = view.findViewById(R.id.tv_email);
        tv_name = view.findViewById(R.id.tv_name);
    }
}