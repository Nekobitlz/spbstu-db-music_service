package ru.spbstu.commons.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.spbstu.commons.DummyViewHolder

open class BaseAdapter :
    ListAdapter<BaseAdapterItem<RecyclerView.ViewHolder>, RecyclerView.ViewHolder>(DIFF_UTIL) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return currentList.find { it.viewType == viewType }?.createViewHolder(parent)
            ?: DummyViewHolder(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        item.bind(holder)
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).viewType
    }

    fun indexOf(headerTypesItem: BaseAdapterItem<out RecyclerView.ViewHolder>): Int {
        return currentList.indexOf(headerTypesItem)
    }

    companion object {
        private val DIFF_UTIL =
            object : DiffUtil.ItemCallback<BaseAdapterItem<RecyclerView.ViewHolder>>() {

                override fun areItemsTheSame(
                    old: BaseAdapterItem<RecyclerView.ViewHolder>,
                    new: BaseAdapterItem<RecyclerView.ViewHolder>
                ): Boolean {
                    return old.viewType == new.viewType
                }

                override fun areContentsTheSame(
                    old: BaseAdapterItem<RecyclerView.ViewHolder>,
                    new: BaseAdapterItem<RecyclerView.ViewHolder>
                ): Boolean {
                    return old == new
                }
            }
    }
}