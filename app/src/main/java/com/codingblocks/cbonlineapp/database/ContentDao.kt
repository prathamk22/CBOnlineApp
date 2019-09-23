package com.codingblocks.cbonlineapp.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.codingblocks.cbonlineapp.database.models.ContentModel

@Dao
abstract class ContentDao : BaseDao<ContentModel> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract override fun insert(obj: ContentModel)

    @Query("SElECT * FROM ContentModel ")
    abstract fun getContent(): LiveData<List<ContentModel>>

    @Query("SElECT * FROM ContentModel where attempt_id = :attempt_id ")
    abstract fun getCourseContents(attempt_id: String): LiveData<List<ContentModel>>

    @Query("SElECT * FROM ContentModel where attempt_id = :attempt_id AND ccid = :id")
    abstract fun getContentWithId(attempt_id: String, id: String): ContentModel

    @Query("SElECT * FROM ContentModel where isDownloaded = :progress ORDER BY date")
    abstract fun getDownloads(progress: String): List<ContentModel>

    @Query("UPDATE ContentModel SET isDownloaded = :downloadprogress WHERE lectureContentId = :contentid")
    abstract fun updateContent(contentid: String, downloadprogress: String)
//
//    @Query("UPDATE ContentModel SET isDownloaded = :downloadprogress WHERE lectureId = :videoId AND section_id = :section")
//    abstract fun updateContentWithVideoId(section: String, videoId: String, downloadprogress: String)

    @Query("UPDATE ContentModel SET progress = 'DONE' AND progressId = :progressId WHERE ccid = :id AND attempt_id = :attemptId")
    abstract fun updateProgress(id: String, attemptId: String, progressId: String)

//    @Query("SELECT * FROM ContentModel WHERE section_id = :sectionId AND attempt_id =:attemptId AND `order` = ((SELECT `order` FROM ContentModel where ccid = :uid) + 1 ) LIMIT 1")
//    abstract fun getNextItem(sectionId: String, attemptId: String, uid: String): LiveData<ContentModel>
}
