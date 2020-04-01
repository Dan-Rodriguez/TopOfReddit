package com.danielrodriguez.topofreddit.presentation

import androidx.lifecycle.ViewModel
import com.danielrodriguez.topofreddit.R
import com.danielrodriguez.topofreddit.dummy.DummyContent
import javax.inject.Inject

class ItemListViewModel @Inject constructor(): ViewModel() {
    val items: List<DummyContent.DummyItem> = DummyContent.ITEMS
    val title: Int = R.string.app_name
}