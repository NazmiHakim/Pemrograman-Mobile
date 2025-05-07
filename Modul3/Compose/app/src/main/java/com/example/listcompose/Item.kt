package com.example.listcompose

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

object ItemData {
    val items = listOf(
        Item(0, "Lookism", "PTJ", "Action, Fighting, Comedy, Romance", "Status: OnGoing", R.drawable.lookism, "https://lookism.fandom.com/wiki/Lookism", "\n" + "Daniel is an unattractive loner who wakes up in a different body. Now tall, handsome, and cooler than ever in his new form, Daniel aims to achieve everything he couldn't before. How far will he go to keep his body... and his secrets?"),
        Item(1, "Bones", "This, Picture: Mu Mu-min", "Action, Fighting", "Status: OnGoing", R.drawable.bones, "https://en.namu.wiki/w/%EB%B3%B8%EC%A6%88(%EC%9B%B9%ED%88%B0)", "\n" + "The protagonist, Kang Jihyoung, wanted to become a hunter for his mother.\n" + "\n" + "However, his bullies put his mother into a vegetative state. He tried to take revenge but was thoroughly crushed and trapped...\n" + "\n" + "The boy who lost everything became a beast and began his hunt."),
        Item(2, "WEE!!!", "Amoeba UwU", "Slice of Life, Romance", "OnGoing", R.drawable.wee, "https://webtoon.fandom.com/id/wiki/WEE!!!", "\n" + "Amu and her gang's wacky daily life as high school teenagers."),
        Item(3, "Hero Has Returned", "FUNGBACK", "Action, Fighting, Fantasy", "OnGoing", R.drawable.herohasreturned, "https://hero-has-returned.fandom.com/wiki/Hero_Has_Returned_Wiki", "\n" + "After saving one world, warrior Minsu Kim returns to Earth only to find that he’s lost everything... so he decides to destroy the world he used to call home. In his rampage, he kills teenager Jeongsu Park and his family. But instead of dying normally, Jeongsu is transported to another world, where he finds that he's now a warrior himself. After killing the demon king of this alternate world and honing his skills as a warrior, Jeongsu returns to earth, hell-bent on revenge against Minsu. As the two warriors fight with the fate of their country in the balance, who will emerge victorious?"),
        Item(4, "Study Group", "Blue String", "Action, Fighting, Comedy", "OnGoing", R.drawable.studygroup, "https://study-group.fandom.com/wiki/Study_Group_Wiki", "\n" + "Yoon Ga Min, a 17 year old student, goes to a school known to harbor future criminals. Stuck in a carnivorous environment where only the strongest survive, he struggles to get good grades to enter a 4 year university. But, will he be able to study well enough to enter university, or will he get involved in the unending fights around the school?")
    )
}