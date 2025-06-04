package com.example.FishyFinder.ui.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.FishyFinder.ui.view.information.AirLautFragment
import com.example.FishyFinder.ui.view.information.AirTawarFragment

class InformationPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> AirTawarFragment()
            1 -> AirLautFragment()
            else -> AirTawarFragment()
        }
    }
}
