package com.example.myproject

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myproject.databinding.ActivityItemDetailBinding

class ItemDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityItemDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityItemDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 設定返回按鈕
        binding.tvBack.setOnClickListener {
            finish()
            overridePendingTransition(android.R.anim.fade_in, R.anim.fade_out)
        }

        val clickedItemId = intent.getStringExtra("ITEM_ID")

        if (clickedItemId != null) {
            val item = MockDatabase.itemList.find { it.id == clickedItemId }

            if (item != null) {
                binding.tvDetailTitle.text = item.title
                binding.tvDetailExchange.text = "價格：${item.exchangeFor}"
                binding.tvDetailDesc.text = item.description

                binding.tvDetailOwner.text = "賣家：${item.ownerName} | ${item.ownerDept} (ID:${item.ownerId})"

                // 處理詳細頁面的圖片顯示
                if (item.imageUri != null) {
                    try {
                        binding.ivDetailImage.setImageURI(android.net.Uri.parse(item.imageUri))
                    } catch (e: Exception) {
                        // 防閃退處理
                        binding.ivDetailImage.setImageResource(android.R.drawable.ic_menu_gallery)
                    }
                } else if (item.imageResId != null) {
                    binding.ivDetailImage.setImageResource(item.imageResId)
                } else {
                    binding.ivDetailImage.setImageResource(android.R.drawable.ic_menu_gallery)
                }

                if (item.ownerId == MockDatabase.currentUser.userId) {

                    if (item.status == 1) {
                        binding.btnBuy.text = "有人想面交！點擊查看詳情"
                        binding.btnBuy.isEnabled = true
                        binding.btnBuy.setBackgroundColor(android.graphics.Color.parseColor("#4CAF50"))
                        binding.btnBuy.setOnClickListener {
                            val orderDetails = """
                                👤 買家姓名/學號：${item.buyerName}
                                📱 聯絡方式：${if(item.buyerContact.isEmpty()) "未提供" else item.buyerContact}
                                📍 面交地點：${item.meetingLocation}
                                ⏰ 面交時間：${item.meetingTime}      
                                您是否同意與這位買家進行面交？
                            """.trimIndent()

                            android.app.AlertDialog.Builder(this)
                                .setTitle("📝 審核面交請求")
                                .setMessage(orderDetails)
                                .setPositiveButton("同意交易") { _, _ ->
                                    item.status = 2
                                    android.widget.Toast.makeText(this, "已同意交易！物品成功售出 🎉", android.widget.Toast.LENGTH_LONG).show()
                                    finish()
                                    overridePendingTransition(android.R.anim.fade_in, R.anim.fade_out)
                                }
                                .setNegativeButton("拒絕請求") { _, _ ->
                                    item.status = 0
                                    item.buyerName = ""
                                    item.buyerContact = ""
                                    item.meetingLocation = ""
                                    item.meetingTime = ""
                                    item.buyerId = ""
                                    android.widget.Toast.makeText(this, "已婉拒請求，物品已重新上架！", android.widget.Toast.LENGTH_SHORT).show()
                                    finish()
                                    overridePendingTransition(android.R.anim.fade_in, R.anim.fade_out)
                                }
                                .setNeutralButton("先關閉", null) // 給賣家一個不做任何決定、單純關閉視窗的退路
                                .show()
                        }
                    }
                    else if (item.status == 2) {
                        binding.btnBuy.visibility = android.view.View.GONE // 隱藏按鈕
                        binding.tvTransactionRecord.visibility = android.view.View.VISIBLE // 顯示收據

                        binding.tvTransactionRecord.text = """
                            📜 【二手物品面交確認紀錄表】
                            ==========================
                            👤 買家：${item.buyerName} (ID:${item.buyerId})
                            📱 聯絡方式：${item.buyerContact}
                            📍 面交地點：${item.meetingLocation}
                            ⏰ 面交時間：${item.meetingTime}
                            狀態評定：🎉 雙方已確認面交，交易成功！
                        """.trimIndent()
                    }
                    else {
                        // 如果還沒人買 (0)
                        binding.btnBuy.text = "這是我發布的物品"
                        binding.btnBuy.isEnabled = false
                        binding.btnBuy.setBackgroundColor(android.graphics.Color.LTGRAY)
                    }
                }
                // 2. 判斷物品狀態 (買家/路人視角)
                else when (item.status) {
                    1 -> {
                        binding.btnBuy.text = "已被預訂，等待賣家確認中"
                        binding.btnBuy.isEnabled = false
                        binding.btnBuy.setBackgroundColor(android.graphics.Color.parseColor("#FF9800"))
                    }
                    2 -> {
                        if (item.buyerId == MockDatabase.currentUser.userId) {
                            binding.btnBuy.visibility = android.view.View.GONE // 隱藏按鈕
                            binding.tvTransactionRecord.visibility = android.view.View.VISIBLE // 顯示收據

                            binding.tvTransactionRecord.text = """
                                📜 【二手物品面交確認紀錄表】
                                ==========================
                                👑 賣家：${item.ownerName} ${item.ownerDept}
                                📍 面交地點：${item.meetingLocation}
                                ⏰ 面交時間：${item.meetingTime}
                                狀態評定：🎉 您已成功購買此物品！
                            """.trimIndent()
                        } else {
                            // 如果是路人點進來（防呆機制，雖然首頁已經鎖了，但多一層保護）
                            binding.btnBuy.text = "已售出"
                            binding.btnBuy.isEnabled = false
                            binding.btnBuy.setBackgroundColor(android.graphics.Color.LTGRAY)
                        }
                    }
                    0 -> {
                        binding.btnBuy.text = "立即購買"
                        binding.btnBuy.isEnabled = true
                        binding.btnBuy.setBackgroundColor(android.graphics.Color.parseColor("#FF5722")) // 恢復原本的按鈕顏色

                        // 正常的購買點擊事件
                        binding.btnBuy.setOnClickListener {

                            val dialogView = layoutInflater.inflate(R.layout.dialog_checkout, null)
                            val etBuyerInfo = dialogView.findViewById<android.widget.EditText>(R.id.et_buyer_info)
                            val etContact = dialogView.findViewById<android.widget.EditText>(R.id.et_contact)
                            val tvMeetingTime = dialogView.findViewById<android.widget.TextView>(R.id.tv_meeting_time)
                            val spinnerLocation = dialogView.findViewById<android.widget.Spinner>(R.id.spinner_meeting_location)

                            // 設定預設的校園面交地點清單
                            val locations = arrayOf("請選擇面交地點", "圖書館正門", "元智一館郵局旁", "學校正門警衛室", "宿宿交誼廳", "紅磚道(二館三館中間)")

                            // 清單放入 Spinner
                            val adapter = android.widget.ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, locations)
                            spinnerLocation.adapter = adapter

                            // 設定時間選擇器
                            tvMeetingTime.setOnClickListener {
                                val calendar = java.util.Calendar.getInstance()
                                android.app.DatePickerDialog(this, { _, year, month, day ->
                                    android.app.TimePickerDialog(this, { _, hour, minute ->
                                        val formattedTime = String.format("%04d/%02d/%02d %02d:%02d", year, month + 1, day, hour, minute)
                                        tvMeetingTime.text = formattedTime
                                    }, calendar.get(java.util.Calendar.HOUR_OF_DAY), calendar.get(java.util.Calendar.MINUTE), true).show()
                                }, calendar.get(java.util.Calendar.YEAR), calendar.get(java.util.Calendar.MONTH), calendar.get(java.util.Calendar.DAY_OF_MONTH)).show()
                            }

                            // 建立並顯示彈出視窗
                            android.app.AlertDialog.Builder(this)
                                .setView(dialogView)
                                .setCancelable(false)
                                .setPositiveButton("確認送出請求") { _, _ ->

                                    val buyer = etBuyerInfo.text.toString()
                                    val time = tvMeetingTime.text.toString()
                                    val location = spinnerLocation.selectedItem.toString()
                                    val contact = etContact.text.toString()

                                    if (buyer.isNotEmpty() &&
                                        location != "請選擇面交地點" &&
                                        time != "點擊選擇面交日期與時間" && time.isNotEmpty()) {

                                        item.status = 1
                                        item.buyerName = buyer
                                        item.buyerContact = contact
                                        item.meetingLocation = location
                                        item.meetingTime = time
                                        item.buyerId = MockDatabase.currentUser.userId //記錄買家 ID

                                        android.widget.Toast.makeText(this, "已發送！將於 $time 在 $location 面交，請靜候賣家同意", android.widget.Toast.LENGTH_LONG).show()
                                        finish()
                                        overridePendingTransition(android.R.anim.fade_in, R.anim.fade_out)
                                    } else {
                                        android.widget.Toast.makeText(this, "請確保所有資訊與地點都已經選擇喔！", android.widget.Toast.LENGTH_SHORT).show()
                                    }
                                }
                                .setNegativeButton("取消", null)
                                .show()
                        }
                    }
                }
            }
        } else {
            Toast.makeText(this, "找不到物品資料", Toast.LENGTH_SHORT).show()
        }
    }
}