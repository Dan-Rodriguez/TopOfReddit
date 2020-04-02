package com.danielrodriguez.topofreddit.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.danielrodriguez.topofreddit.R
import com.danielrodriguez.topofreddit.domain.model.RedditPost
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_list_content.view.*

class RedditPostAdapter(
    private val parentActivity: FragmentActivity,
    private val twoPane: Boolean
): RecyclerView.Adapter<RedditPostAdapter.ViewHolder>() {

    private var viewModel: ItemListViewModel? = null

    private val onClickListener: View.OnClickListener

    init {
        onClickListener = View.OnClickListener { v ->
            val item = v.tag as RedditPost
            if (twoPane) {
                val fragment = ItemDetailFragment.newInstance(item, twoPane)
                parentActivity.supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.item_detail_container, fragment)
                    .commit()
            } else {

                val fragment = ItemDetailFragment.newInstance(item, twoPane)
                parentActivity.supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_list_content, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        viewModel?.getPostAt(position)?.let { item ->
            with(holder) {
                with(itemView) {
                    alpha = 1f
                    tag = item
                    setOnClickListener {
                        viewItem(holder)
                        viewModel?.viewAt(holder.adapterPosition)
                        onClickListener.onClick(holder.itemView)
                    }
                }
                authorTextView.text = item.author
                titleTextView.text = item.title
                dismissButton.setOnClickListener {
                    deleteItem(holder)
                }
                commentCountTextView.text = parentActivity.resources.getQuantityString(R.plurals.x_comments, item.numberOfComments, item.numberOfComments)
                viewedView.alpha = if (item.viewed) 0f else 1f

                val quantity = if (item.hoursAgo == 1L) 1 else 0
                timestampTextView.text = parentActivity.resources.getQuantityString(R.plurals.x_hours_ago, quantity, item.hoursAgo)

                if (item.thumbnail.isNotEmpty()) {
                    Picasso.get().load(item.thumbnail).into(imageView)
                }
            }
        }
    }

    private fun viewItem(holder: ViewHolder) {
        viewModel?.getPostAt(holder.adapterPosition)?.let { item ->
            val animation = AnimationUtils.loadAnimation(
                holder.itemView.context,
                android.R.anim.fade_out
            )
            animation.duration = holder.itemView.context.resources.getInteger(R.integer.animation_duration).toLong()
            animation.onAnimationEnd {
                holder.viewedView.alpha = 0f
            }
            holder.viewedView.startAnimation(animation)
        }
    }

    private fun deleteItem(holder: ViewHolder) {

        val animation = AnimationUtils.loadAnimation(
            parentActivity,
            android.R.anim.slide_out_right
        )
        animation.duration = parentActivity.resources.getInteger(R.integer.animation_duration).toLong()
        animation.onAnimationEnd {
            holder.itemView.alpha = 0f

            val position = holder.adapterPosition
            viewModel?.removePostAt(position)
            notifyItemRemoved(position)
        }
        holder.itemView.startAnimation(animation)
    }

    override fun getItemCount() = viewModel?.postsCount ?: 0

    fun submitList(viewModel: ItemListViewModel) {
        this.viewModel = viewModel
        notifyDataSetChanged()
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val authorTextView: TextView = view.author
        val titleTextView: TextView = view.title
        val dismissButton: TextView = view.dismiss_button
        val commentCountTextView: TextView = view.comment_count
        val viewedView: View = view.viewed
        val timestampTextView: TextView = view.timestamp
        val imageView: ImageView = view.image
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