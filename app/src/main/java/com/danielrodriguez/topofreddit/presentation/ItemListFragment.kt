package com.danielrodriguez.topofreddit.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.danielrodriguez.topofreddit.R
import com.danielrodriguez.topofreddit.topOfRedditApplication
import kotlinx.android.synthetic.main.fragment_item_list.*
import javax.inject.Inject

class ItemListFragment : Fragment(), RedditPostAdapter.RedditPostAdapterListener {

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

        val adapter = RedditPostAdapter().also { it.redditPostAdapterListener = this }

        setupRecyclerView(adapter)

        viewModel = ViewModelProvider(this, viewModelFactory)[ItemListViewModel::class.java]

        setupViewModelObservers(adapter)

        swipeToRefresh.setOnRefreshListener {
            viewModel.refresh()
        }
    }

    private fun setupViewModelObservers(adapter: RedditPostAdapter) {
        with(viewModel) {
            posts.observe(this@ItemListFragment, Observer {
                adapter.submitList(viewModel.reddistPostViewModels)
            })

            isLoading.observe(this@ItemListFragment, Observer {
                swipeToRefresh.isRefreshing = it
            })
        }
    }

    private fun setupRecyclerView(newAdapter: RedditPostAdapter) {
        with(item_list) {
            adapter = newAdapter
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    if (isLastRow()) {
                        viewModel.loadMore()
                    }
                }
            })
        }
    }

    private fun isLastRow() : Boolean {
        val linearLayoutManager: LinearLayoutManager =
            item_list.layoutManager as LinearLayoutManager

        val totalItemCount = linearLayoutManager.itemCount
        val lastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition()

        return viewModel.isLoading.value == false && viewModel.hasMore
                && lastVisibleItemPosition == totalItemCount - 1
    }

    override fun onResume() {
        super.onResume()

        (activity as AppCompatActivity).supportActionBar?.title = getString(viewModel.title)
    }

    override fun onPostDeleteButtonClicked(
        holder: RedditPostAdapter.ViewHolder,
        post: RedditPostViewModel
    ) {
        val animation = AnimationUtils.loadAnimation(
            context,
            R.anim.slide_out_left
        )
        animation.duration = resources.getInteger(R.integer.animation_duration).toLong()
        animation.onAnimationEnd { viewModel.deletePost(post) }
        holder.itemView.startAnimation(animation)
    }

    override fun onPostClicked(holder: RedditPostAdapter.ViewHolder, post: RedditPostViewModel) {
        viewPost(holder, post)

        if (isTablet) {
            val fragment = ItemDetailFragment.newInstance(post.post, isTablet)
            activity?.supportFragmentManager
                ?.beginTransaction()
                ?.replace(R.id.item_detail_container, fragment)
                ?.commit()
        } else {

            val fragment = ItemDetailFragment.newInstance(post.post, isTablet)
            activity?.supportFragmentManager
                ?.beginTransaction()
                ?.replace(R.id.fragment_container, fragment)
                ?.addToBackStack(null)
                ?.commit()
        }
    }

    // TODO - Don't expose 'viewedView' maybe?
    private fun viewPost(holder: RedditPostAdapter.ViewHolder, post: RedditPostViewModel) {
        val animation = AnimationUtils.loadAnimation(
            holder.itemView.context,
            android.R.anim.fade_out
        )
        animation.duration = holder.itemView.context.resources.getInteger(R.integer.animation_duration).toLong()
        animation.onAnimationEnd { holder.viewedView.alpha = 0f }
        holder.viewedView.startAnimation(animation)
        viewModel.viewedPost(post)
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
