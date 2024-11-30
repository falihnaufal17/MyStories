package com.dicoding.mystories

import com.dicoding.mystories.data.response.ListStoryItem

object DataDummy {
    fun generateDummyQuoteResponse(): List<ListStoryItem> {
        val items: MutableList<ListStoryItem> = arrayListOf()
        for (i in 0..100) {
            val quote = ListStoryItem(
                i.toString(),
                null,
                "Name $i",
                "Desc $i",
                9.0,
                "id-$i",
                9.0
            )
            items.add(quote)
        }
        return items
    }
}