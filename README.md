# 元智校園二手交易平台

> 本專案為校園二手物品交易系統之 Android 客戶端，在Android Studio 中使用 **Kotlin** 開發。

---

## 核心介面展示

| 1. 系統登入 | 2. 市集首頁與檢索 | 3. 刊登二手物品 | 4. 個人中心與管理 |
| :---: | :---: | :---: | :---: |
| <img src="https://github.com/user-attachments/assets/71d4dcd3-ea1e-4ae3-b6d3-db7df86d0df7" width="220" alt="系統登入" /> | <img src="https://github.com/user-attachments/assets/ef5d6b1a-a71a-464b-a524-a531ecb6daa8" width="220" alt="市集首頁" /> | <img src="https://github.com/user-attachments/assets/9e9edfe2-5961-498f-b7f7-46d691cf460f" width="220" alt="刊登商品" /> | <img src="https://github.com/user-attachments/assets/a5391e66-b7c6-44c9-baa8-3093fc20176e" width="220" alt="個人檔案" />|
| 內建測試帳號與一鍵指引 | 關鍵字搜尋 | 完整商品資訊與照片選取 | 刊登中與預訂狀態追蹤 |
---

## 系統核心功能

* **身分驗證**：
  * 校園學號登入機制。
  * 提供測試帳號提示快捷機制，方便評審與開發除錯。
* **市集首頁與即時檢索**：
  * 二手書籍與各類物品清單。
  * 支援關鍵字動態搜尋，快速篩選物品名稱與描述。
* **商品發布**：
  * 支援商品標題、詳細屋況說明、自訂價格與實體照片選取。
  * 輸入驗證防呆機制，確保商品資料完整性。
* **個人中心與狀態追蹤**：
  * 個人資料與系級身分顯示。
  * 分頁切換「我的刊登」與「我的預訂」，掌握物品流轉狀態。

---

## 技術棧與架構設計 (Tech Stack & Architecture)

* **開發語言**：Kotlin
* **架構模式**：Single Activity + Multi-Fragments 架構 (`BottomNavigationView` 導航)
* **UI 元件**：Material Design Components, `ConstraintLayout`, `RecyclerView`, `CardView`
* **資料處理**：本機 Mock 資料持久層 (`MockData.kt`)、適配器模式 (`ItemAdapter`)
* **目標環境**：Android 14 (API 34) / 支援多數主流 Android 裝置

---

## 專案目錄結構

```text
app/src/main/
├── java/com/example/myproject/
│   ├── MainActivity.kt          # 核心主容器 (管理底部導航與 Fragment 切換)
│   ├── LoginActivity.kt         # 登入與身分驗證
│   ├── ItemDetailActivity.kt    # 商品詳細資訊頁面
│   ├── HomeFragment.kt          # 市集首頁展示與檢索
│   ├── PublishFragment.kt       # 商品刊登表單
│   ├── ProfileFragment.kt       # 個人交易中心
│   ├── ItemAdapter.kt           # RecyclerView 商品列表適配器
│   └── MockData.kt              # 測試資料實體模型
└── res/
    ├── layout/                  # 各畫面 XML 佈局配置
    ├── drawable/                # 圖示、向量圖與背景圓角樣式
    └── menu/                    # 底部導航選單 (bottom_nav_menu.xml)
