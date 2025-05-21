package com.example.listcompose

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class ItemViewModel(private val context: Context, private val param: String) : ViewModel() {

    private val _itemList = MutableStateFlow<List<Item>>(emptyList())
    val itemList: StateFlow<List<Item>> = _itemList

    init {
        viewModelScope.launch {
            val items = getItems(context)
            _itemList.value = items
            Timber.d("ItemViewModel: $param - Loaded ${items.size} items")
        }
    }
}