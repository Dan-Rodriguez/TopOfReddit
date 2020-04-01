package com.danielrodriguez.topofreddit.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.danielrodriguez.topofreddit.R
import com.danielrodriguez.topofreddit.topOfRedditApplication
import kotlinx.android.synthetic.main.fragment_item_list.*
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

        val adapter = RedditPostAdapter(activity!!, isTablet)
        item_list.adapter = adapter

        viewModel.posts.observe(this, Observer {
            adapter.submitList(it)
        })

        item_list.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val linearLayoutManager: LinearLayoutManager =
                    item_list.layoutManager as LinearLayoutManager

                val totalItemCount = linearLayoutManager.itemCount
                val lastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition()

                if (viewModel.isLoading.value == false && viewModel.hasMore) {
                    if (lastVisibleItemPosition == totalItemCount - 1) {
                        viewModel.loadMore()
                    }
                }
            }
        })
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
