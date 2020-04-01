package com.danielrodriguez.topofreddit.presentation

import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.danielrodriguez.topofreddit.R
import kotlinx.android.synthetic.main.activity_item_list.*
import kotlinx.android.synthetic.main.item_list.*

class ItemListActivity : AppCompatActivity() {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private var twoPane: Boolean
        get() = (item_detail_container != null)
        set(_) {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_list)

        setSupportActionBar(toolbar)
        toolbar.title = title

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .add(
                    R.id.fragment_container,
                    ItemListFragment.newInstance(
                        twoPane
                    )
                )
                .commit()

        } else {
            supportFragmentManager
                .beginTransaction()
                .replace(
                    R.id.fragment_container,
                    ItemListFragment.newInstance(
                        twoPane
                    )
                )
                .commit()
        }
    }
}

val Int.isLandscape: Boolean get() = (this == Configuration.ORIENTATION_LANDSCAPE)
