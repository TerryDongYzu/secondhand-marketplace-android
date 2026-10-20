package com.example.myproject
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myproject.MainActivity
import com.example.myproject.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 設定登入按鈕
        binding.btnLogin.setOnClickListener {
            val username = binding.etUsername.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            // 1. 去我們剛剛建好的 userList 裡面，尋找有沒有符合這個學號的人
            val matchedUser = MockDatabase.userList.find { it.userId == username }

            // 2. 判斷是否有找到人，且密碼是否正確 (為了測試方便，密碼統一 1234)
            if (matchedUser != null && password == "1234") {
                MockDatabase.currentUser = matchedUser
                android.widget.Toast.makeText(this, "登入成功！歡迎 ${matchedUser.name}", android.widget.Toast.LENGTH_SHORT).show()

                // 跳轉到主畫面
                val intent = android.content.Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish() // 關閉登入頁面

            } else {
                android.widget.Toast.makeText(this, "帳號或密碼錯誤！", android.widget.Toast.LENGTH_LONG).show()
            }
        }
        val tvShowTestAccounts = findViewById<android.widget.TextView>(R.id.tv_show_test_accounts)

        tvShowTestAccounts.setOnClickListener {
            val accountInfo = """
                【管理員】
                帳號: admin | 密碼: 1234
                
                【測試員 1】
                帳號: 123456 | 密碼: 1234
                
                【測試員 2】
                帳號: Test | 密碼: 1234
            """.trimIndent()

            android.app.AlertDialog.Builder(this)
                .setTitle("💡 測試帳號清單")
                .setMessage(accountInfo)
                .setPositiveButton("我知道了", null)
                .show()
        }
        // 在 onCreate 的最下面加上這行
        binding.ivLogo.alpha = 0f
        binding.ivLogo.animate().alpha(1f).setDuration(1500).start()
    }
}