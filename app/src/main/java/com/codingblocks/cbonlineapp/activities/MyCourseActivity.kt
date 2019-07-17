package com.codingblocks.cbonlineapp.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.codingblocks.cbonlineapp.BuildConfig
import com.codingblocks.cbonlineapp.R
import com.codingblocks.cbonlineapp.adapters.TabLayoutAdapter
import com.codingblocks.cbonlineapp.extensions.observer
import com.codingblocks.cbonlineapp.fragments.AnnouncementsFragment
import com.codingblocks.cbonlineapp.fragments.CourseContentFragment
import com.codingblocks.cbonlineapp.fragments.DoubtsFragment
import com.codingblocks.cbonlineapp.fragments.OverviewFragment
import com.codingblocks.cbonlineapp.fragments.LeaderboardFragment
import com.codingblocks.cbonlineapp.util.COURSE_ID
import com.codingblocks.cbonlineapp.util.COURSE_NAME
import com.codingblocks.cbonlineapp.util.RUN_ATTEMPT_ID
import com.codingblocks.cbonlineapp.util.RUN_ID
import com.codingblocks.cbonlineapp.util.MediaUtils
import com.codingblocks.cbonlineapp.viewmodels.MyCourseViewModel
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerSupportFragment
import kotlinx.android.synthetic.main.activity_my_course.htab_tabs
import kotlinx.android.synthetic.main.activity_my_course.htab_viewpager
import kotlinx.android.synthetic.main.activity_my_course.toolbar
import org.koin.androidx.viewmodel.ext.android.viewModel

class MyCourseActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener {

    private val viewModel by viewModel<MyCourseViewModel>()

    private lateinit var youtubePlayerInit: YouTubePlayer.OnInitializedListener
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_course)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        viewModel.courseId = intent.getStringExtra(COURSE_ID)
        title = intent.getStringExtra(COURSE_NAME)
        viewModel.attemptId = intent.getStringExtra(RUN_ATTEMPT_ID) ?: ""
        viewModel.runId = intent.getStringExtra(RUN_ID) ?: ""

        if (viewModel.attemptId.isEmpty()) {
            viewModel.attemptId = viewModel.getRunAttempt(viewModel.runId)
        }
        if (savedInstanceState == null) {

            viewModel.updatehit(viewModel.attemptId)
            viewModel.fetchCourse(viewModel.attemptId)
            viewModel.getPromoVideo(viewModel.courseId)
            setupViewPager(viewModel.attemptId, viewModel.courseId)
        }
    }

    override fun onStart() {
        super.onStart()

        viewModel.promoVideo.observer(this) {
            youtubePlayerInit = object : YouTubePlayer.OnInitializedListener {
                override fun onInitializationFailure(
                    p0: YouTubePlayer.Provider?,
                    p1: YouTubeInitializationResult?
                ) {
                }

                override fun onInitializationSuccess(
                    p0: YouTubePlayer.Provider?,
                    youtubePlayerInstance: YouTubePlayer?,
                    p2: Boolean
                ) {
                    if (!p2) {
                        youtubePlayerInstance?.cueVideo(MediaUtils.getYotubeVideoId(it))
                    }
                }
            }
            val youTubePlayerSupportFragment =
                supportFragmentManager.findFragmentById(R.id.displayYoutubeVideo) as YouTubePlayerSupportFragment?

            youTubePlayerSupportFragment!!.initialize(BuildConfig.YOUTUBE_KEY, youtubePlayerInit)
        }
    }

    private fun setupViewPager(crUid: String, crCourseId: String) {
        val adapter = TabLayoutAdapter(supportFragmentManager)
        adapter.add(OverviewFragment.newInstance(viewModel.attemptId, crUid), "Overview")
        adapter.add(AnnouncementsFragment.newInstance(viewModel.courseId, viewModel.attemptId), "About")
        adapter.add(CourseContentFragment.newInstance(viewModel.attemptId), "Course Content")
        adapter.add(DoubtsFragment.newInstance(viewModel.attemptId, crCourseId), "Doubts")
        adapter.add(LeaderboardFragment.newInstance(viewModel.runId), "Leaderboard")

        htab_viewpager.adapter = adapter
        htab_tabs.setupWithViewPager(htab_viewpager)
        htab_tabs.getTabAt(0)?.setIcon(R.drawable.ic_menu)
        htab_tabs.getTabAt(1)?.setIcon(R.drawable.ic_announcement)
        htab_tabs.getTabAt(2)?.setIcon(R.drawable.ic_docs)
        htab_tabs.getTabAt(3)?.setIcon(R.drawable.ic_announcement)

        htab_tabs.getTabAt(4)?.setIcon(R.drawable.ic_leaderboard)
        htab_tabs.getTabAt(2)?.select()
        htab_viewpager.offscreenPageLimit = 4
    }

    override fun onRefresh() {
        viewModel.fetchCourse(viewModel.attemptId)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        viewModel.updatehit(viewModel.attemptId)
        viewModel.fetchCourse(viewModel.attemptId)
        viewModel.getPromoVideo(viewModel.courseId)
        setupViewPager(viewModel.attemptId, viewModel.courseId)
    }
}
