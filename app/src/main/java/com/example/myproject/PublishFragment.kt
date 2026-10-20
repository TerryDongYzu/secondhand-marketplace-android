package com.example.myproject

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.example.myproject.databinding.FragmentPublishBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class PublishFragment : Fragment() {

    private var _binding: FragmentPublishBinding? = null
    private val binding get() = _binding!!

    // 用來暫存使用者選到的圖片路徑
    private var selectedImageUri: Uri? = null

    // 打開相簿並接收
    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        if (uri != null) {
            // 如果使用者有選照片，就把路徑存起來，並顯示在畫面的 ImageView 上
            selectedImageUri = uri
            binding.ivPreview.setImageURI(uri)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPublishBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 設定「選擇照片」按鈕
        binding.btnPickImage.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }

        binding.btnPublish.setOnClickListener {
            val titleInput = binding.etTitle.text.toString()
            val descInput = binding.etDesc.text.toString()
            val exchangeInput = binding.etExchange.text.toString()

            if (titleInput.isNotEmpty() && descInput.isNotEmpty() && exchangeInput.isNotEmpty()) {

                var permanentImageUriString: String? = null

                if (selectedImageUri != null) {
                    permanentImageUriString = copyImageToInternalStorage(selectedImageUri!!)
                }

                val newItem = Item(
                    id = System.currentTimeMillis().toString(),
                    title = titleInput,
                    description = descInput,
                    exchangeFor = exchangeInput,
                    ownerId = MockDatabase.currentUser.userId,
                    ownerName = MockDatabase.currentUser.name,
                    ownerDept = MockDatabase.currentUser.department,
                    imageUri = permanentImageUriString
                )

                MockDatabase.itemList.add(0, newItem)

                Toast.makeText(requireContext(), "刊登成功！", Toast.LENGTH_SHORT).show()

                selectedImageUri = null
                binding.ivPreview.setImageDrawable(null)

                val bottomNav = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation)
                bottomNav.selectedItemId = R.id.nav_home

            } else {
                Toast.makeText(requireContext(), "請填寫完整資訊哦", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun copyImageToInternalStorage(uri: android.net.Uri): String? {
        return try {
            val inputStream = requireContext().contentResolver.openInputStream(uri) ?: return null

            val fileName = "item_image_${System.currentTimeMillis()}.jpg"
            val newFile = java.io.File(requireContext().filesDir, fileName)
            val outputStream = java.io.FileOutputStream(newFile)

            inputStream.copyTo(outputStream)

            inputStream.close()
            outputStream.close()

            android.net.Uri.fromFile(newFile).toString()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}