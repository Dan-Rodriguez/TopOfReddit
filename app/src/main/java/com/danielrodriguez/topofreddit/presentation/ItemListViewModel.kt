package com.danielrodriguez.topofreddit.presentation

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.danielrodriguez.topofreddit.R
import com.danielrodriguez.topofreddit.dummy.DummyContent
import java.util.ArrayList
import javax.inject.Inject

class ItemListViewModel @Inject constructor(): ViewModel() {


    private val ITEMS: MutableList<DummyContent.DummyItem> = ArrayList()


    val hasMore: Boolean get() = ITEMS.size < 50

    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _posts = MutableLiveData<List<DummyContent.DummyItem>>()
    val posts: LiveData<List<DummyContent.DummyItem>> = _posts

    val title: Int = R.string.app_name

    init {
        for (i in 1..25) {
            ITEMS.add(createDummyItem(i))
        }

        _posts.value = ITEMS
    }

    private fun createDummyItem(position: Int): DummyContent.DummyItem {
        return DummyContent.DummyItem(
            position.toString(),
            "Item " + position,
            makeDetails(position)
        )
    }

    private fun makeDetails(position: Int): String {
        val builder = StringBuilder()
        builder.append("Details about Item: ").append(position)
        builder.append("\nMore details information here. $position")
        return builder.toString()
    }

    fun loadMore() {

        _isLoading.value = true

        Thread {
            Thread.sleep(3000)

            Handler(Looper.getMainLooper()).post {
                val start = ITEMS.size
                val end = ITEMS.size + 25

                for (i in start..end) {
                    ITEMS.add(createDummyItem(i))
                }

                _posts.value = ITEMS

                _isLoading.value = false
            }
        }
        .start()
    }
}