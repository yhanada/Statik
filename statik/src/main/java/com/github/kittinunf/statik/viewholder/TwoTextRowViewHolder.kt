package com.github.kittinunf.statik.viewholder

import android.view.View
import android.support.v4.widget.TextViewCompat
import android.widget.ImageView
import android.widget.TextView
import com.github.kittinunf.statik.R
import com.github.kittinunf.statik.representable.TwoTextRowRepresentable

class TwoTextRowViewHolder(view: View) : StatikViewHolder(view), BindableViewHolder<TwoTextRowRepresentable> {

    override fun bind(item: TwoTextRowRepresentable) {
        item.onViewSetupListener?.invoke(itemView)

        //primary
        val titleTextView = itemView.findViewById<TextView>(R.id.statik_row_text_primary)
        titleTextView.text = item.titleText

        //summary
        val summaryTextView = itemView.findViewById<TextView>(R.id.statik_row_text_secondary)
        if (item.summaryText == null) {
            summaryTextView.visibility = View.GONE
        } else {
            summaryTextView.apply {
                visibility = View.VISIBLE
                text = item.summaryText
            }
        }

        val titleTextSetup = item.onTitleTextSetupListener
        if (titleTextSetup == null) {
            TextViewCompat.setTextAppearance(titleTextView, R.style.Widget_Statik_Base_PrimaryTextItem)
        } else {
            titleTextSetup(titleTextView)
        }

        val summaryTextSetup = item.onSummaryTextSetupListener
        if (summaryTextSetup == null) {
            TextViewCompat.setTextAppearance(summaryTextView, R.style.Widget_Statik_Base_SecondaryTextItem)
        } else {
            summaryTextSetup(summaryTextView)
        }

        //image
        val imageViewSetup = item.onImageSetupListener
        val iconImageView = itemView.findViewById<ImageView>(R.id.statik_row_icon)
        if (imageViewSetup == null) {
            val iconRes = item.iconRes
            if (iconRes == null) {
                iconImageView.visibility = View.GONE
            } else {
                iconImageView.apply {
                    visibility = View.VISIBLE
                    setImageResource(iconRes)
                }
            }
        } else {
            imageViewSetup(iconImageView)
        }

        //click
        item.onClickListener?.let { listener ->
            itemView.setOnClickListener { view ->
                listener.invoke(view)
            }
        }
    }
}
