package com.danielrodriguez.topofreddit.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.danielrodriguez.topofreddit.R
import com.danielrodriguez.topofreddit.dummy.DummyContent
import com.danielrodriguez.topofreddit.topOfRedditApplication
import kotlinx.android.synthetic.main.fragment_item_list.*
import kotlinx.android.synthetic.main.item_list_content.view.*
import javax.inject.Inject

class ItemListFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: ItemListViewModel

    private val isTablet: Boolean get() = arguments?.getBoolean(TABLET) ?: false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_item_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        activity?.topOfRedditApplication?.appComponent?.inject(this)

        viewModel = ViewModelProvider(this, viewModelFactory)[ItemListViewModel::class.java]

        item_list.adapter = RedditPostAdapter(activity!!, viewModel.items, isTablet)
    }

    override fun onResume() {
        super.onResume()

        (activity as AppCompatActivity).supportActionBar?.title = getString(viewModel.title)
    }

    companion object {
        private const val TABLET = "tablet"

        @JvmStatic
        fun newInstance(isTablet: Boolean) =
            ItemListFragment().apply {
                arguments = Bundle().apply {
                    putBoolean(TABLET, isTablet)
                }
            }
    }
}
