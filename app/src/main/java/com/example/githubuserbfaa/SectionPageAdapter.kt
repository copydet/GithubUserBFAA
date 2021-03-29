package com.example.githubuserbfaa

import android.content.Context
import androidx.annotation.Nullable
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class SectionsPagerAdapter(private val mContext: Context, fm: FragmentManager): FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    var setUsername: String? = null

    @StringRes
    private val TAB_TITLES = intArrayOf(R.string.follower_user, R.string.following_user)

    override fun getCount(): Int {
        return 2
    }

    override fun getItem(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = FollowerFragment.newInstance(setUsername.toString())
            1 -> fragment = FollowingFragment.newInstance(setUsername.toString())
        }
        return fragment as Fragment
    }

    @Nullable
    override fun getPageTitle(position: Int): CharSequence? {
        return mContext.resources.getString(TAB_TITLES[position])
    }

    fun  setUsername(usernametab: String){
        this.setUsername = usernametab
    }
}