package com.danielrodriguez.topofreddit.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.danielrodriguez.topofreddit.R
import com.danielrodriguez.topofreddit.dummy.DummyContent
import kotlinx.android.synthetic.main.item_detail.view.*

class ItemDetailFragment : Fragment() {

    private val isTablet: Boolean get() = arguments?.getBoolean(TABLET) ?: false

    /**
     * The dummy content this fragment is presenting.
     */
    private var item: DummyContent.DummyItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)

        arguments?.let {
            if (it.containsKey(ARG_ITEM_ID)) {
                // Load the dummy content specified by the fragment
                // arguments. In a real-world scenario, use a Loader
                // to load content from a content provider.
                item = DummyContent.ITEM_MAP[it.getString(ARG_ITEM_ID)]
                (activity as AppCompatActivity).supportActionBar?.title = item?.content
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.item_detail, container, false)

        // Show the dummy content as text in a TextView.
        item?.let {
            rootView.item_detail.text = it.details
        }

        return rootView
    }

    override fun onResume() {
        super.onResume()

        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(!isTablet)
    }

    override fun onPause() {
        super.onPause()

        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }

    override fun onOptionsItemSelected(item: MenuItem) =
        when (item.itemId) {
            android.R.id.home -> {
                fragmentManager?.popBackStack()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    companion object {
        private const val ARG_ITEM_ID = "item_id"
        private const val TABLET = "tablet"

        @JvmStatic
        fun newInstance(itemId: String, isTablet: Boolean) =
            ItemDetailFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_ITEM_ID, itemId)
                    putBoolean(TABLET, isTablet)
                }
            }
    }
}
