package com.ph41626.and103_assignment.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.ph41626.and103_assignment.Fragment.CartFragment;
import com.ph41626.and103_assignment.Fragment.HomeFragment;
import com.ph41626.and103_assignment.Fragment.ProfileFragment;
import com.ph41626.and103_assignment.Fragment.SearchFragment;

public class ViewPagerMainBottomNavigationAdapter extends FragmentStateAdapter {

    public ViewPagerMainBottomNavigationAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0: return new HomeFragment();
            case 1: return new SearchFragment();
            case 2: return new CartFragment();
            case 3: return new ProfileFragment();
            default:break;
        }
        return new HomeFragment();
    }
    @Override
    public int getItemCount() {
        return 4;
    }
}
