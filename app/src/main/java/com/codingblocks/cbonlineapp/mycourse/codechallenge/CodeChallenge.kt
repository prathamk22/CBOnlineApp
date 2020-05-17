package com.codingblocks.cbonlineapp.mycourse.codechallenge

import android.os.Bundle
import com.codingblocks.cbonlineapp.R
import com.codingblocks.cbonlineapp.baseclasses.BaseCBActivity
import com.codingblocks.cbonlineapp.util.CODE_ID
import com.codingblocks.cbonlineapp.util.CONTENT_ID
import com.codingblocks.cbonlineapp.util.CONTEST_ID
import com.codingblocks.cbonlineapp.util.SECTION_ID
import org.jetbrains.anko.AnkoLogger
import org.koin.androidx.viewmodel.ext.android.stateViewModel

class CodeChallenge : BaseCBActivity(), AnkoLogger {

    private val vm: CodeChallengeViewModel by stateViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_code_challenge)
        if (savedInstanceState == null) {
            vm.contentId = intent.getStringExtra(CONTENT_ID)
            vm.sectionId = intent.getStringExtra(SECTION_ID)
            vm.contestId = intent.getStringExtra(CONTEST_ID)
            vm.codeId = intent.getStringExtra(CODE_ID)
        }
        vm.fetchSections()
    }
}
