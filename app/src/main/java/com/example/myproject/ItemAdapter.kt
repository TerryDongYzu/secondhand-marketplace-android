package com.example.myproject

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myproject.databinding.ItemSecondHandBinding

// 傳入資料清單
class ItemAdapter(private var itemList: List<Item>) : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    class ItemViewHolder(val binding: ItemSecondHandBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ItemSecondHandBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val currentItem = itemList[position]
        holder.binding.tvItemTitle.text = currentItem.title
        holder.binding.tvItemDesc.text = currentItem.description
        holder.binding.tvItemExchange.text = "價格：${currentItem.exchangeFor}"

        // 處理圖片顯示
        if (currentItem.imageUri != null) {
            try {
                // 嘗試讀取手機裡的真實圖片
                holder.binding.ivItemImage.setImageURI(android.net.Uri.parse(currentItem.imageUri))
            } catch (e: Exception) {
                holder.binding.ivItemImage.setImageResource(android.R.drawable.ic_menu_gallery)
            }
        } else if (currentItem.imageResId != null) {
            holder.binding.ivItemImage.setImageResource(currentItem.imageResId)
        } else {
            holder.binding.ivItemImage.setImageResource(android.R.drawable.ic_menu_gallery)
        }

        when (currentItem.status) {
            1 -> {
                // 【狀態 1：等待賣家確認中】
                holder.itemView.alpha = 0.7f
                holder.binding.tvItemExchange.text = "🤝 交易確認中"
                holder.binding.tvItemExchange.setTextColor(android.graphics.Color.parseColor("#FF9800"))

                // 雙方或其他人都能點進去（由詳細頁面內部做身分防呆）
                holder.binding.root.setOnClickListener {
                    val context = holder.itemView.context
                    val intent = android.content.Intent(context, ItemDetailActivity::class.java)
                    intent.putExtra("ITEM_ID", currentItem.id)
                    context.startActivity(intent)
                }
            }
            2 -> {
                // 狀態 2：交易完成
                holder.itemView.alpha = 0.5f

                // 動態切換顯示字樣
                when {
                    currentItem.buyerId == MockDatabase.currentUser.userId -> {
                        holder.binding.tvItemExchange.text = "🎉 購買成功"
                        holder.binding.tvItemExchange.setTextColor(android.graphics.Color.parseColor("#4CAF50"))
                    }
                    currentItem.ownerId == MockDatabase.currentUser.userId -> {
                        holder.binding.tvItemExchange.text = "💰 已賣出"
                        holder.binding.tvItemExchange.setTextColor(android.graphics.Color.GRAY)
                    }
                    else -> {
                        holder.binding.tvItemExchange.text = "已售出"
                        holder.binding.tvItemExchange.setTextColor(android.graphics.Color.GRAY)
                    }
                }

                // 權限防護：只有買家與賣家可以點進詳細頁面瀏覽紀錄
                if (currentItem.buyerId == MockDatabase.currentUser.userId || currentItem.ownerId == MockDatabase.currentUser.userId) {
                    holder.binding.root.setOnClickListener {
                        val context = holder.itemView.context
                        val intent = android.content.Intent(context, ItemDetailActivity::class.java)
                        intent.putExtra("ITEM_ID", currentItem.id)
                        context.startActivity(intent)
                    }
                } else {
                    holder.binding.root.setOnClickListener(null) // 路人限制點擊
                }
            }
            else -> {
                // 狀態 0：正常可購買
                holder.itemView.alpha = 1.0f
                holder.binding.tvItemExchange.text = "價錢：${currentItem.exchangeFor}"
                holder.binding.tvItemExchange.setTextColor(android.graphics.Color.parseColor("#FF5722"))

                holder.binding.root.setOnClickListener {
                    val context = holder.itemView.context
                    val intent = android.content.Intent(context, ItemDetailActivity::class.java)
                    intent.putExtra("ITEM_ID", currentItem.id)
                    context.startActivity(intent)
                }
            }
        }
    }
    // 用來更新列表資料的函式
    fun updateData(newList: List<Item>) {
        itemList = newList
        notifyDataSetChanged() // 告訴 RecyclerView 資料變了，請重新整理畫面！
    }
    // 系統總共有幾筆資料
    override fun getItemCount(): Int {
        return itemList.size
    }
}