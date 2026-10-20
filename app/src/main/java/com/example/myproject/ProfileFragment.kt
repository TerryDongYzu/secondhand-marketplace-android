package com.example.myproject

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myproject.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: ItemAdapter
    private var isShowingPublished = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 設定 RecyclerView
        binding.recyclerViewMyItems.layoutManager = LinearLayoutManager(requireContext())
        adapter = ItemAdapter(emptyList())
        binding.recyclerViewMyItems.adapter = adapter

        // 設定兩個按鈕的點擊事件
        binding.btnTabPublished.setOnClickListener {
            isShowingPublished = true
            updateListAndTabUI() // 更新畫面
        }

        binding.btnTabPurchased.setOnClickListener {
            isShowingPublished = false
            updateListAndTabUI() // 更新畫面
        }

        // 登出按鈕邏輯
        binding.btnLogout.setOnClickListener {
            android.app.AlertDialog.Builder(requireContext())
                .setTitle("提示")
                .setMessage("確定要登出嗎？")
                .setPositiveButton("確定") { _, _ ->
                    val intent = android.content.Intent(requireActivity(), LoginActivity::class.java)
                    startActivity(intent)
                    requireActivity().finish()
                }
                .setNegativeButton("取消", null)
                .show()
        }
    }

    override fun onResume() {
        super.onResume()

        // 更新個人資料文字
        binding.tvProfileName.text = MockDatabase.currentUser.name
        binding.tvProfileDept.text = "系級:${MockDatabase.currentUser.department}"
        binding.tvProfileId.text = "ID:${MockDatabase.currentUser.userId}"

        // 每次回到畫面，都重新整理清單
        updateListAndTabUI()
    }

    // 切換：根據 isShowingPublished 來決定要顯示什麼資料跟按鈕顏色
    private fun updateListAndTabUI() {
        if (isShowingPublished) {
            // 變成我的刊登
            binding.btnTabPublished.setBackgroundColor(android.graphics.Color.parseColor("#FF5722")) // 橘色
            binding.btnTabPurchased.setBackgroundColor(android.graphics.Color.parseColor("#AAAAAA")) // 灰色

            // 自己賣的東西
            val myItems = MockDatabase.itemList.filter { it.ownerId == MockDatabase.currentUser.userId }
            adapter.updateData(myItems)
        } else {
            // 變成我的預訂
            binding.btnTabPublished.setBackgroundColor(android.graphics.Color.parseColor("#AAAAAA")) // 灰色
            binding.btnTabPurchased.setBackgroundColor(android.graphics.Color.parseColor("#FF5722")) // 橘色

            // 自己買的東西
            val myPurchases = MockDatabase.itemList.filter { it.buyerId == MockDatabase.currentUser.userId }
            adapter.updateData(myPurchases)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}