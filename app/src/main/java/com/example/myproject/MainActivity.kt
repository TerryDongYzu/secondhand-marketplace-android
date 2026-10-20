package com.example.myproject

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.myproject.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 預設一開啟主畫面，先載入首頁
        if (savedInstanceState == null) {
            replaceFragment(HomeFragment())
        }

        // 設定底部導覽列
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> replaceFragment(HomeFragment())
                R.id.nav_publish -> replaceFragment(PublishFragment())
                R.id.nav_profile -> replaceFragment(ProfileFragment())
            }
            true
        }
    }

    // 切換 Fragment與動畫
    private fun replaceFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()

        // 過場動畫
        transaction.setCustomAnimations(R.anim.slide_in, R.anim.fade_out)

        transaction.replace(R.id.fragment_container, fragment)
        transaction.commit()
    }
}