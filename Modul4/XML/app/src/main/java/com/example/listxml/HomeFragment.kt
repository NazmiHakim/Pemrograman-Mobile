package com.example.listxml

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.listxml.databinding.FragmentHomeBinding

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
        val itemList = listOf(
            Item(R.drawable.lookism, "Lookism", "PTJ", "https://example.com"),
            Item(R.drawable.bones, "Bones", "This, Picture : Mu Mu-min", "https://example.com"),
            Item(R.drawable.wee, "WEE!!!", "Amoeba UwU", "https://example.com"),
            Item(R.drawable.herohasreturned, "Hero Has Returned", "FUNGBACK", "https://example.com"),
            Item(R.drawable.studygroup, "Study Group", "BlueString", "https://example.com")
        )

        val adapter = ItemAdapter(itemList,
            onOpenClick = { url ->
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(intent)
            },
            onDetailClick = { item ->
                val action = HomeFragmentDirections.actionHomeToDetail(item.title, item.author, item.imageResId)
                findNavController().navigate(action)
            })

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}