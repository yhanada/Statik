package com.github.kittinunf.statik.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.github.kittinunf.statik.model.Section
import com.github.kittinunf.statik.representable.AnyRepresentable
import com.github.kittinunf.statik.representable.ItemRepresentable
import com.github.kittinunf.statik.viewholder.BindableViewHolder
import com.github.kittinunf.statik.viewholder.StatikViewHolder
import kotlin.properties.Delegates

typealias OnSectionUpdateListener = (List<Section>) -> Unit

class StatikAdapter(private val typeFactory: TypeFactory = defaultTypeFactory) : RecyclerView.Adapter<StatikViewHolder>() {

    var sections by Delegates.observable(emptyList<Section>()) { _, _, value ->
        //calculate items
        update()
        onSectionUpdate?.invoke(value)
    }

    var onSectionUpdate: OnSectionUpdateListener? = null

    private var items = emptyList<ItemRepresentable>()

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatikViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return typeFactory.viewHolder(viewType, view)
    }

    @Suppress("unchecked_cast")
    override fun onBindViewHolder(viewHolder: StatikViewHolder, position: Int) {
        (viewHolder as BindableViewHolder<ItemRepresentable>).bind(items[position])
    }

    override fun getItemViewType(position: Int): Int = items[position].type(typeFactory)

    override fun getItemCount(): Int = items.size

    override fun getItemId(position: Int): Long = items[position].stableId

    private fun createRepresentable(currentSection: Section, previousSectionCount: Int): List<ItemRepresentable> {
        val items = mutableListOf<ItemRepresentable>()

        if (currentSection.header != null) {
            items.add(currentSection.header)
        }

        items.addAll(currentSection.rows)

        if (currentSection.footer != null) {
            items.add(currentSection.footer)
        }

        items.withIndex()
                .onEach { (index, item) ->
                    (item as? AnyRepresentable)?.apply {
                        section = currentSection
                        position = index + previousSectionCount
                    }
                }

        return items
    }

    fun update() {
        items = sections.withIndex()
                .flatMap { (index, section) ->
                    createRepresentable(section,
                            sections.subList(0, index)
                                    .fold(0) { acc, each -> acc + calculateSectionSize(each) })
                }

        notifyDataSetChanged()
    }

    private fun calculateSectionSize(section: Section): Int =
            (if (section.header != null) 1 else 0) + section.rows.size + (if (section.footer != null) 1 else 0)

    override fun onViewRecycled(holder: StatikViewHolder) {
        super.onViewRecycled(holder)
        holder.onViewRecycled()
    }
}
