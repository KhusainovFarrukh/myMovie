package academy.android.mymovie.adapter

import academy.android.mymovie.R
import academy.android.mymovie.callback.ActorCallback
import academy.android.mymovie.clickinterface.ActorClickInterface
import academy.android.mymovie.data.Actor
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class ActorAdapter(
    private val actorClickInterface: ActorClickInterface,
    private val imageUrl: String
) :
    ListAdapter<Actor, ActorAdapter.ActorViewHolder>(ActorCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActorViewHolder =
        ActorViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.view_holder_actor, parent, false)
        )

    override fun onBindViewHolder(holder: ActorViewHolder, position: Int) {
        holder.onBindActor(getItem(position))
        holder.itemView.apply {
            setOnClickListener { actorClickInterface.onActorClick(getItem(position).id) }
        }
    }

    inner class ActorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val txvActor: TextView = itemView.findViewById(R.id.txv_actor)
        private val imvActor: ImageView = itemView.findViewById(R.id.imv_actor)

        fun onBindActor(actor: Actor) {
            if (actor.profileUrl == null) {
                imvActor.setImageDrawable(
                    ContextCompat.getDrawable(
                        itemView.context,
                        R.drawable.sample_placeholder
                    )
                )
            } else {
                Glide.with(itemView.context)
                    .load(Uri.parse(imageUrl + actor.profileUrl))
                    .apply(imageOption)
                    .into(imvActor)
            }
            txvActor.text = actor.name
        }
    }

    companion object {
        private val imageOption = RequestOptions()
            .placeholder(R.drawable.sample_placeholder)
            .fallback(R.drawable.sample_placeholder)
    }
}