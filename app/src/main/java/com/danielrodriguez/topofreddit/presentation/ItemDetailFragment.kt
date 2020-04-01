package com.danielrodriguez.topofreddit.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.danielrodriguez.topofreddit.R
import com.danielrodriguez.topofreddit.domain.model.RedditPost
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_detail.*

class ItemDetailFragment : Fragment() {

    private val isTablet: Boolean get() = arguments?.getBoolean(TABLET) ?: false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.item_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            if (it.containsKey(POST)) {
                it.getParcelable<RedditPost>(POST)?.let { post ->
                    author.text = post.author
                    title.text = post.title

                    if (post.thumbnail.isNotEmpty()) {
                        Picasso.get().load(post.thumbnail).into(image)
                    }

                    (activity as AppCompatActivity).supportActionBar?.title = post.author

                } ?: run {
                    throw IllegalArgumentException(getString(R.string.not_a_post))
                }

            } else {
                throw IllegalArgumentException(getString(R.string.no_post_provided))
            }
        }
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
        private const val POST = "post"
        private const val TABLET = "tablet"

        @JvmStatic
        fun newInstance(post: RedditPost, isTablet: Boolean) =
            ItemDetailFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(POST, post)
                    putBoolean(TABLET, isTablet)
                }
            }
    }
}
