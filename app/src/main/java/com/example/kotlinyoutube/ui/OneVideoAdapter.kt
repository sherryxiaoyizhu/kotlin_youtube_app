package com.example.kotlinyoutube.ui

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinyoutube.R
import com.example.kotlinyoutube.ui.OneVideoActivity.Companion.videoUrl
import com.example.kotlinyoutube.api.OneVideo
import com.example.kotlinyoutube.ui.OneVideoActivity.Companion.videoId
import com.google.android.youtube.player.YouTubePlayer
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_one_video.view.*
import kotlinx.android.synthetic.main.row_main.view.*
import kotlinx.android.synthetic.main.row_one_video.view.*

class OneVideoAdapter(private val oneVideo: List<OneVideo>,
                      private val viewModel: MainViewModel)
    : RecyclerView.Adapter<OneVideoAdapter.VH>() {

    companion object {
        const val WEB_URL_KEY = "WEB_URL"
        const val VIDEO_ID_KEY = "VIDEO_ID"
    }

    override fun getItemCount(): Int {
        return 1 // fetch one video detail
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val customView = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_one_video, parent, false)
        return VH(customView, viewModel)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(oneVideo[holder.adapterPosition])
    }

    // set up view holder
    inner class VH(val view: View, val viewModel: MainViewModel): RecyclerView.ViewHolder(view) {

        init {
            view.videoDetailImageView.setOnLongClickListener {
                val intent = Intent(view.context, WebViewActivity::class.java).apply {
                    putExtra(WEB_URL_KEY, videoUrl)
                }
                view.context.startActivity(intent)
                true
            }
//            view.videoDetailImageView.setOnLongClickListener {
//                Log.d("XXX", "videoDetailImageView clicked 1...")
//                val intent = Intent(view.context, YouTubeMediaPlayer::class.java).apply {
//                    putExtra(VIDEO_ID_KEY, videoId)
//                }
//                Log.d("XXX", "videoDetailImageView clicked 2...")
//                view.context.startActivity(intent)
//                true
//            }
        }

        fun bind(item: OneVideo) {
            // fetch data
            val thumbnailUrl = item.snippet.thumbnails.high.url
            val viewCount = item.statistics.viewCount
            val publishedDate = item.snippet.publishedAt.substringBefore('T')
            val likesCount = item.statistics.likeCount
            val dislikesCount = item.statistics.dislikeCount
            val commentsCount = item.statistics.commentCount
            val description = item.snippet.description

            // display video detail thumbnail
            val thumbnailImageView = view.videoDetailImageView
            Picasso.with(view.context).load(thumbnailUrl).into(
                thumbnailImageView
            )

            // display video details: views, published time, number of likes, comments, description
            val views = viewModel.getThousands(viewCount)
            val time = viewModel.getTimeAgo(viewModel.stringToDate(publishedDate))
            view.viewCount_publishedAt_TV.text = "$views views • $time"
            view.likeCountTV.text = viewModel.getShortScale(likesCount)
            view.dislikeCountTV.text = viewModel.getShortScale(dislikesCount)
            view.commentsCount.text = viewModel.getShortScale(commentsCount)
            view.oneVideoDescriptionTV.text = description
        }
    }
}
