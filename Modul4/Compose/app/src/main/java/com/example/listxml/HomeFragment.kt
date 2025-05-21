package com.example.listxml

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.listxml.databinding.FragmentHomeBinding
import kotlinx.coroutines.launch
import timber.log.Timber

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: ItemAdapter

    private val viewModel: ItemViewModel by viewModels {
        ItemViewModelFactory("ParamDariHome")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = ItemAdapter(
            emptyList(),
            onOpenClick = { url ->
                Timber.d("HomeFragment: Open URL $url")
                val intent = Intent(Intent.ACTION_VIEW, url.toUri())
                startActivity(intent)
            },
            onDetailClick = { item ->
                viewModel.selectItem(item)
                Timber.d("HomeFragment: Navigate to detail ${item.title}")
                val action = HomeFragmentDirections.actionHomeToDetail(
                    item.title,
                    item.author,
                    item.imageResId
                )
                findNavController().navigate(action)
            }
        )

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.itemList.collect { items ->
                adapter.updateItems(items)
                Timber.d("HomeFragment: Received ${items.size} items")
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}