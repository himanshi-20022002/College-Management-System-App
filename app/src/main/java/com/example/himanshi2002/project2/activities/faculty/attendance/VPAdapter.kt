package com.example.himanshi2002.project2.activities.faculty.attendance

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class VPAdapter(fm : FragmentManager) : FragmentPagerAdapter(fm) {



    override fun getCount(): Int {
       return 2
    }

    override fun getItem(position: Int): Fragment {
        when(position){
            0 ->{return TakeAttendanceFragment()}
            1-> {return ViewAttendanceFragment()}
            else->{return TakeAttendanceFragment()}
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        when(position){
            0->{return  "Take Attendance"}
            1->{return "View Attendance"}
        }

        return super.getPageTitle(position)
    }
}