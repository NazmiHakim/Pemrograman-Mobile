package com.example.listcompose

import android.content.Context

data class Item(
    val id: Int,
    val title: String,
    val author: String,
    val info1: String,
    val info2: String,
    val imageResId: Int,
    val url: String,
    val description: String
)

fun getItems(context: Context): List<Item> {
    val titles = context.resources.getStringArray(R.array.item_titles)
    val authors = context.resources.getStringArray(R.array.item_authors)
    val infos1 = context.resources.getStringArray(R.array.item_info1)
    val infos2 = context.resources.getStringArray(R.array.item_info2)
    val urls = context.resources.getStringArray(R.array.item_urls)
    val descriptions = context.resources.getStringArray(R.array.item_descriptions)
    
    val imageIds = listOf(
        R.drawable.lookism,
        R.drawable.bones,
        R.drawable.wee,
        R.drawable.herohasreturned,
        R.drawable.studygroup
    )

    return titles.indices.map { index ->
        Item(
            id = index,
            title = titles[index],
            author = authors[index],
            info1 = infos1[index],
            info2 = infos2[index],
            imageResId = imageIds[index],
            url = urls[index],
            description = descriptions[index]
        )
    }
}