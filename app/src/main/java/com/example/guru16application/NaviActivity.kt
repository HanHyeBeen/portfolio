package com.example.guru16application

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import com.example.guru16application.databinding.FragmentHomeBinding
import com.google.android.material.navigation.NavigationBarView
import com.google.android.material.navigation.NavigationView


private const val TAG_HOME = "home_fragment"
private const val TAG_CLOTHING = "home_fragment"
private const val TAG_FOOD = "food_fragment"
private const val TAG_SHELTER = "shelter_fragment"

class NaviActivity : AppCompatActivity(R.layout.activity_navi) {

    private lateinit var binding : FragmentHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(savedInstanceState == null){
            val bundle = bundleOf("some_int" to 0)
            supportFragmentManager.commit {
                setReorderingAllowed(true)
            }
        }

        binding = FragmentHomeBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_navi)

        setFragment(TAG_HOME, HomeFragment())

        NavigationBarView.OnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.homeFragment -> setFragment(TAG_HOME, HomeFragment())
                R.id.clothingFragment -> setFragment(TAG_CLOTHING, ClothingFragment())
                R.id.foodFragment -> setFragment(TAG_FOOD, FoodFragment())
                R.id.shelterFragment -> setFragment(TAG_SHELTER, ShelterFragment())
            }
            true
        }
    }
    private fun setFragment(tag: String, fragment: Fragment) {
        val manager : FragmentManager = supportFragmentManager
        val fragTransaction = manager.beginTransaction()

        val home = manager.findFragmentByTag(TAG_HOME)
        val clothing = manager.findFragmentByTag(TAG_CLOTHING)
        val food = manager.findFragmentByTag(TAG_FOOD)
        val shelter = manager.findFragmentByTag(TAG_SHELTER)

        if(home != null) fragTransaction.hide(home)
        if(clothing != null) fragTransaction.hide(clothing)
        if(food != null) fragTransaction.hide(food)
        if(shelter != null) fragTransaction.hide(shelter)

        if(tag == TAG_HOME)
            if(home != null) fragTransaction.show(home)

        if(tag == TAG_CLOTHING)
            if(clothing != null) fragTransaction.show(clothing)
        else if(tag == TAG_FOOD)
            if(food != null) fragTransaction.show(food)
        else if(tag == TAG_SHELTER)
            if(shelter != null) fragTransaction.show(shelter)
    }

}