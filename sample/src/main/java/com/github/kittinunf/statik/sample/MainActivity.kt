package com.github.kittinunf.statik.sample

import android.os.Bundle
import android.support.v4.widget.TextViewCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.Gravity
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageView
import com.github.kittinunf.statik.dsl.section
import com.github.kittinunf.statik.dsl.statik
import com.github.kittinunf.statik.dsl.textFooter
import com.github.kittinunf.statik.dsl.textHeader
import com.github.kittinunf.statik.dsl.textRow
import com.github.kittinunf.statik.dsl.twoTextRow
import com.github.kittinunf.statik.dsl.viewFooter
import com.github.kittinunf.statik.dsl.viewRow
import com.github.kittinunf.statik.sample.util.find
import com.github.kittinunf.statik.sample.util.load
import com.github.kittinunf.statik.sample.util.toast
import kotlinx.android.synthetic.main.activity_list.list
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_list)

        val r1 = textRow {
            text = "One Line"
        }

        val r2 = textRow {
            text = "One Line with Icon"
            iconRes = android.R.drawable.ic_delete
        }

        val r3 = twoTextRow {
            titleText = "Two Lines"
            summaryText = "This is two-line text"
        }

        val r4 = twoTextRow {
            titleText = "Two Lines with Icon"
            summaryText = "This is two-line text with icon"
            iconRes = android.R.drawable.ic_menu_call
        }

        val r5 = textRow {
            text = "You can customize the appearance"
            onTextSetupListener = {
                TextViewCompat.setTextAppearance(it, R.style.TextAppearance_AppCompat_Custom1)
            }
        }

        val r6 = textRow {
            text = "You can customize the appearance"
            onTextSetupListener = {
                TextViewCompat.setTextAppearance(it, R.style.TextAppearance_AppCompat_Custom4)
            }
        }

        val r7 = twoTextRow {
            var count = 0
            titleText = "Also, you can try to click this row"
            summaryText = "Click"
            iconRes = android.R.drawable.ic_input_add
            onClickListener = { _, position ->
                summaryText = "${++count} times"
                iconRes = if (count % 2 == 0) android.R.drawable.ic_input_add
                else android.R.drawable.ic_delete

                updateList(position)
            }
        }

        val r8 = textRow {
            text = "You can observe changes, click here"
            onClickListener = { _, position ->
                text = "Next random: ${Random().nextInt(10)}"
                updateList(position)
            }
            onChangedListener = {
                println(it.value)
            }
        }

        val h1 = textHeader {
            text = "Header"
            onTextSetupListener = {
                TextViewCompat.setTextAppearance(it, R.style.TextAppearance_AppCompat_Custom2)
            }
        }

        val f1 = textFooter {
            text = "Footer"
            onTextSetupListener = {
                it.gravity = Gravity.END
                TextViewCompat.setTextAppearance(it, R.style.TextAppearance_AppCompat_Custom3)
            }
            onClickListener = { _, _ ->
                println("Footer is clicked")
            }
        }

        val s1 = section {
            header(h1)
            rows(r1, r2, r3, r4, r5, r6, r7, r8)
            footer(f1)
        }

        val h2 = textHeader {
            text = "Collapsable"
            layoutRes = R.layout.widget_checkbox
            onViewSetupListener = {
                val checkBox = it.find<CheckBox>(android.R.id.checkbox)
                checkBox.setOnCheckedChangeListener { _, isChecked ->
                    toast("CheckBox is $isChecked")
                }
            }
        }

        val f2 = viewFooter {
            layoutRes = R.layout.widget_button
            onViewSetupListener = {
                it.find<Button>(android.R.id.button1).setOnClickListener {
                    toast("Button is clicked")
                }
            }
        }

        val s2 = section {
            header(h2)
            rows(r1, r1, r1, r1, r1, r1, r1, r1, r1, r1)
            footer(f2)
        }

        val r9 = viewRow {
            layoutRes = R.layout.widget_two_image_view
            onViewSetupListener = {
                val image1 = it.find<ImageView>(android.R.id.icon1)
                val image2 = it.find<ImageView>(android.R.id.icon2)

                image1.load("https://source.unsplash.com/200x200/?nature")
                image2.load("https://source.unsplash.com/200x200/?building")
            }
        }

        val s3 = section {
            rows(r9)
        }

        val adapter =
                statik {
                    sections(s1, s2, s3)
                }

        list.also {
            it.layoutManager = LinearLayoutManager(this)
            it.adapter = adapter
        }
    }

    private fun updateList(at: Int) {
        list.adapter.notifyItemChanged(at)
    }
}