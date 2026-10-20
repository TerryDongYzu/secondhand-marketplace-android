package com.example.myproject

// 定義資料(Data Class)

// 使用者實體
data class User(
    val userId: String,     // 學號
    val name: String,       // 姓名
    val department: String  // 系級
)

// 物品實體
data class Item(
    val id: String,           // 物品編號
    val title: String,        // 物品名稱
    val description: String,  // 物品狀況描述
    val exchangeFor: String,  // 想交換的條件或金額
    val ownerId: String,      // 刊登者的學號
    var ownerName: String = "", // 刊登者姓名
    var ownerDept: String = "",  // 刊登者系級
    val imageUri: String? = null, // 用來存圖片的路徑，允許為空 (null)
    val imageResId: Int? = null, //存drawble裡的圖片
    var status: Int = 0,
    var buyerName: String = "",
    var buyerContact: String = "",
    var meetingLocation: String = "",
    var meetingTime: String = "",
    var buyerId: String = ""
)
// 建立資料庫
object MockDatabase {

    // 建立一個名單，裡面放兩個測試帳號
    val userList = listOf(
        User(userId = "admin", name = "管理員", department = "資管系"),
        User(userId = "123456", name = "測試員1", department = "資管系"),
        User(userId = "Test", name = "測試員2", department = "工管系")
    )
    var currentUser = userList[0]

    // 二手物品清單 (mutableListOf 之後才可以新增資料)
    val itemList = mutableListOf(
        Item(
            id = "001",
            title = "微積分原文書 (九成新)",
            description = "只翻過幾次，沒有筆記，想賣 500 元",
            exchangeFor = "500元",
            ownerId = "123456",
            imageResId = R.drawable.book1,
            status = 0,
            ownerName = "測試員",
            ownerDept = "資管系"
        ),
        Item(
            id = "002",
            title = "Android行動裝置程式設計課本",
            description = "畢業出清，功能正常",
            exchangeFor = "600元",
            ownerId = "admin", // 注意：這是你 (董承庭) 發布的物品
            imageResId = R.drawable.book2, // 這裡換成你原本 Android 課本的圖片變數
            status = 0,
            ownerName = "管理員",
            ownerDept = "資管系"
        ),
        Item(
            id = "003",
            title = "網際網路程式設計課本",
            description = "只翻過幾次，有一點點筆記",
            exchangeFor = "800元",
            ownerId = "admin",
            imageResId = R.drawable.book3,
            status = 0,
            ownerName = "管理員",
            ownerDept = "資管系"
        ),
        Item(
            id = "004",
            title = "統計學課本",
            description = "全新",
            exchangeFor = "1000元",
            ownerId = "123456",
            imageResId = R.drawable.book4,
            status = 0,
            ownerName = "測試員",
            ownerDept = "資管系"
        ),
        Item(
            id = "005",
            title = "資料庫管理",
            description = "9成新",
            exchangeFor = "600元",
            ownerId = "123456",
            imageResId = R.drawable.book5,
            status = 0,
            ownerName = "測試員",
            ownerDept = "資管系"
        )
    )
}