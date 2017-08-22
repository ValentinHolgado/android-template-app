package ar.valentinholgado.template.view.common

import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import ar.valentinholgado.template.BR
import io.reactivex.Observer

class CardViewHolder<T>(val binding: ViewDataBinding,
                           val eventStream: Observer<CardEvent<T>>) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.setVariable(BR.eventHandler, object : CardEventHandler<T> {
            override fun onItemClick(cardContent: T) {
                eventStream.onNext(CardEvent(cardContent))
            }
        })
    }

    fun bind(viewHolderModel: T) {
        binding.setVariable(BR.viewHolderModel, viewHolderModel)
        binding.executePendingBindings()
    }
}