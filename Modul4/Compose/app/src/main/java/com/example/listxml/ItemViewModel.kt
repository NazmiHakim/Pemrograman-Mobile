package com.example.listxml

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class ItemViewModel(private val param: String) : ViewModel() {

    private val _itemList = MutableStateFlow<List<Item>>(emptyList())
    val itemList: StateFlow<List<Item>> = _itemList

    private val _selectedItem = MutableStateFlow<Item?>(null)

    init {
        viewModelScope.launch {
            val items = getDummyItems()
            _itemList.value = items
            Timber.d("ItemViewModel: $param - Loaded ${items.size} items")
        }
    }

    fun selectItem(item: Item) {
        _selectedItem.value = item
        Timber.d("ItemViewModel: Selected item: ${item.title}")
    }

    private fun getDummyItems(): List<Item> {
        return listOf(
            Item(R.drawable.lookism, "Lookism", "Author 1", "https://lookism.fandom.com/wiki/Lookism"),
            Item(R.drawable.bones, "Bones", "Author 2", "https://en.namu.wiki/w/%EB%B3%B8%EC%A6%88(%EC%9B%B9%ED%88%B0)"),
            Item(R.drawable.wee, "Wee", "Author 3", "https://webtoon.fandom.com/id/wiki/WEE!!!"),
            Item(R.drawable.herohasreturned, "Hero Has Returned", "Author 4", "https://hero-has-returned.fandom.com/wiki/Hero_Has_Returned_Wiki"),
            Item(R.drawable.studygroup, "Study Group", "Author 5", "https://study-group.fandom.com/wiki/Study_Group_Wiki")
        )
    }
}