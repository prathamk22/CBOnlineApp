package com.codingblocks.cbonlineapp.mycourse.codechallenge

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import com.codingblocks.cbonlineapp.baseclasses.BaseCBViewModel
import com.codingblocks.cbonlineapp.util.*
import com.codingblocks.cbonlineapp.util.extensions.runIO
import com.codingblocks.onlineapi.ResultWrapper
import com.codingblocks.onlineapi.fetchError

class CodeChallengeViewModel(
    handle: SavedStateHandle,
    private val repo: CodeChallengeRepository,
    val prefs: PreferenceHelper
) : BaseCBViewModel() {

    var sectionId by savedStateValue<String>(handle, SECTION_ID)
    var contentId by savedStateValue<String>(handle, CONTENT_ID)
    var contestId by savedStateValue<String>(handle, CONTEST_ID)
    var codeId by savedStateValue<String>(handle, CODE_ID)

    fun fetchSections() {
        runIO {
            when (val response = repo.fetchCodeChallenge(codeId!!.toInt(),contestId?:"")) {
                is ResultWrapper.GenericError -> {
                    Log.e("codeChallenge 1", response.code.toString() +" "+ response.error)
                    setError(response.error)
                }
                is ResultWrapper.Success -> {
                    if (response.value.isSuccessful)
                        response.value.body()?.let { codeChallenge ->
//                            Log.e("name",codeChallenge.included.size.toString())
                            Log.e("name",codeChallenge.toString())

                        }
                    else {
                        Log.e("codeChallenge 2", response.value.toString())
                        setError(fetchError(response.value.code()))
                    }
                }
            }
        }
    }

}
