package ar.valentinholgado.template.view.common

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject

abstract class CardAdapter<T>(
        var list: List<T> = ArrayList(),
        val eventStream: Subject<CardEvent<T>> = PublishSubject.create())
    : RecyclerView.Adapter<CardViewHolder<T>>() {

    @LayoutRes
    abstract fun getLayoutIdForPosition(position: Int): Int

    fun getViewHolderModelForPosition(position: Int): T {
        return list[position]
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: CardViewHolder<T>?, position: Int) {
        holder?.bind(getViewHolderModelForPosition(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): CardViewHolder<T> {
        val layoutInflater = LayoutInflater.from(parent?.context)
        val viewDataBinding: ViewDataBinding = DataBindingUtil.inflate(layoutInflater, viewType, parent, false)
        return CardViewHolder(viewDataBinding, eventStream)
    }

    override fun getItemViewType(position: Int): Int {
        return getLayoutIdForPosition(position)
    }

    fun outputStream(): Observable<CardEvent<T>> {
        return eventStream
    }

    fun updateList(list: List<T>) {
        // TODO Update difference only
        this.list = list
        notifyDataSetChanged()
    }
}