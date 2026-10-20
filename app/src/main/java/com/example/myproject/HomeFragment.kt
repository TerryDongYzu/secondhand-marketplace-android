package com.example.myproject

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myproject.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 1. 設定 RecyclerView 的排列方式
        binding.recyclerViewItems.layoutManager = LinearLayoutManager(requireContext())
        // 2. 把 MockDatabase 的資料交給 Adapter
        val adapter = ItemAdapter(MockDatabase.itemList)
        // 3. 把 Adapter 裝到 RecyclerView 上
        binding.recyclerViewItems.adapter = adapter

        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val keyword = s.toString().trim()

                if (keyword.isEmpty()) {
                    adapter.updateData(MockDatabase.itemList)
                } else {
                    val filteredList = MockDatabase.itemList.filter { item ->
                        item.title.contains(keyword, ignoreCase = true) ||
                                item.description.contains(keyword, ignoreCase = true)
                    }
                    adapter.updateData(filteredList)
                }
            }
        })
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}