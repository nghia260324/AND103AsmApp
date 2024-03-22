package com.ph41626.and103_assignment.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.ph41626.and103_assignment.Fragment.CategoryFragment;
import com.ph41626.and103_assignment.Fragment.DistributorFragment;
import com.ph41626.and103_assignment.Fragment.ProductFragment;

public class ViewPagerInventoryBottomNavigationAdapter extends FragmentStateAdapter {

    public ViewPagerInventoryBottomNavigationAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0: return new ProductFragment();
            case 1: return new CategoryFragment();
            case 2: return new DistributorFragment();
            default:break;
        }
        return new ProductFragment();
    }
    @Override
    public int getItemCount() {
        return 3;
    }
}
