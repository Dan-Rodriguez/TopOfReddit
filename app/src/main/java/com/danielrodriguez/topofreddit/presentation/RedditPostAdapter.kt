package com.danielrodriguez.topofreddit.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.danielrodriguez.topofreddit.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_list_content.view.*

class RedditPostAdapter: RecyclerView.Adapter<RedditPostAdapter.ViewHolder>() {

    interface RedditPostAdapterListener {
        fun onPostDeleteButtonClicked(holder: ViewHolder, post: RedditPostViewModel)
        fun onPostClicked(holder: ViewHolder, post: RedditPostViewModel)
    }

    private var posts: List<RedditPostViewModel> = mutableListOf()

    var redditPostAdapterListener: RedditPostAdapterListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_list_content, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(posts[position])
    }

    override fun getItemCount() = posts.size

    fun submitList(list: List<RedditPostViewModel>) {
        this.posts = list
        notifyDataSetChanged()
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val authorTextView: TextView = view.author
        private val titleTextView: TextView = view.title
        private val dismissButton: TextView = view.dismiss_button
        private val commentCountTextView: TextView = view.comment_count
        val viewedView: View = view.viewed
        private val timestampTextView: TextView = view.timestamp
        private val imageView: ImageView = view.image

        private val post: RedditPostViewModel get() = posts[this.adapterPosition]

        init {
            itemView.setOnClickListener {
                redditPostAdapterListener?.onPostClicked(this, post)
            }
        }

        fun bind(viewModel: RedditPostViewModel) {
            authorTextView.text = viewModel.author
            titleTextView.text = viewModel.title
            commentCountTextView.text = viewModel.numberOfComments
            viewedView.alpha = 1f
            viewedView.alpha = viewModel.viewedAlpha
            timestampTextView.text = viewModel.timeStamp

            dismissButton.setOnClickListener {
                redditPostAdapterListener?.onPostDeleteButtonClicked(this, post)
            }

            if (viewModel.thumbnail.isNotEmpty()) {
                Picasso.get().load(viewModel.thumbnail).into(imageView)
            } else {
                imageView.setImageResource(viewModel.thumbnailPlaceholder)
            }
        }
    }
}

fun Animation.onAnimationEnd(callback: (Animation?) -> Unit) {
    this.setAnimationListener(object: Animation.AnimationListener {

        override fun onAnimationEnd(animation: Animation?) {
            callback(animation)
        }

        override fun onAnimationRepeat(animation: Animation?) {}

        override fun onAnimationStart(animation: Animation?) {}
    })
}