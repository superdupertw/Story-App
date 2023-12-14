package com.dicoding.storyapplication.view.detailstory

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.dicoding.storyapplication.R
import com.dicoding.storyapplication.databinding.ActivityDetailStoryBinding
import com.dicoding.storyapplication.utils.DateFormatter
import com.dicoding.storyapplication.view.detailmap.DetailMapActivity
import java.util.TimeZone

class DetailStoryActivity : AppCompatActivity() {
    private lateinit var detailBinding: ActivityDetailStoryBinding
    private var usernames: String? = null
    private var dates: String? = null
    private var latitude: String? = null
    private var longitude: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        detailBinding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(detailBinding.root)

        setupView()

        val photo = intent.getStringExtra(PHOTO)
        val desc = intent.getStringExtra(DESCRIPTION)
        dates = intent.getStringExtra(DATE)
        val formattedDate = DateFormatter.formatDate(dates!!, TimeZone.getDefault().id)
        usernames = intent.getStringExtra(USERNAME)
        latitude = intent.getStringExtra(LAT)
        longitude = intent.getStringExtra(LON)
        detailBinding.apply {
            Glide.with(this@DetailStoryActivity)
                .load(photo)
                .into(storyImages)
            storyName.text = usernames
            storyDesc.text = desc
            storyDate.text = formattedDate
        }

        detailBinding.fbShare.setOnClickListener {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_SUBJECT, getString(R.string.extra_subject))
                putExtra(
                    Intent.EXTRA_TEXT,
                    "Dipost oleh $usernames pada $formattedDate\n\nDengan deskripsi :\n$desc"
                )
                type = "text/plain"
            }
            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
        }
    }

    private fun setupView() {
        val actionbar = supportActionBar
        actionbar?.title = getString(R.string.detail_page)
        actionbar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        if (latitude?.toDouble() == 0.0 && longitude?.toDouble() == 0.0) {
            return false
        } else {
            val inflater = menuInflater
            inflater.inflate(R.menu.detail_menu, menu)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_maps -> {
                val moveToMap =
                    Intent(this@DetailStoryActivity, DetailMapActivity::class.java).apply {
                        putExtra(DetailMapActivity.USERNAME, usernames)
                        putExtra(DetailMapActivity.LAT, latitude)
                        putExtra(DetailMapActivity.LON, longitude)
                        putExtra(DetailMapActivity.DATE, dates)
                    }
                startActivity(moveToMap)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        const val USERNAME = "name"
        const val DESCRIPTION = "desc"
        const val LON = "lon"
        const val PHOTO = "photo"
        const val DATE = "date"
        const val LAT = "lat"
    }
}