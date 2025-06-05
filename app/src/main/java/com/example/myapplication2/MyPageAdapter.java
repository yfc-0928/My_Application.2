package com.example.myapplication2;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class MyPageAdapter extends FragmentStateAdapter {
    public MyPageAdapter(@NonNull ViewPagerActivity fa){
        super(fa);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position){
        if(position==0){
            //return new FirstFragment();
            return FirstFragment.newInstance();
        }else if(position==1){
            return new SecondFragment();
        }else{
            return new ThirdFragment();
        }
    }

    @Override
    public int getItemCount(){
        return 3;
    }
}
