package com.delitx.univlabmusicplayer.repositories.audio

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import com.delitx.univlabmusicplayer.model.AudioElement
import com.delitx.univlabmusicplayer.model.AudioMetadata
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

class AudioRepositoryImpl @Inject constructor(
    @ApplicationContext
    private val appContext: Context,
) : AudioRepository {

    override suspend fun getAudioList(): List<AudioElement> {
        return getAudios()
    }

    private fun getAudios(): List<AudioElement> {
        return try {
            val selection = MediaStore.Audio.Media.IS_MUSIC + " != 0"
            val cursor = appContext.contentResolver.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                audioProjection,
                selection,
                null,
                MediaStore.Audio.Media.DEFAULT_SORT_ORDER,
            )
            val audioList = mutableListOf<AudioElement>()
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    val audioElement = parseCursorEntry(cursor)
                    if (audioElement != null) {
                        audioList.add(audioElement)
                    }
                } while (cursor.moveToNext())
            }
            cursor?.close()
            audioList
        } catch (e: SecurityException) {
            emptyList()
        }
    }

    @SuppressLint("Range")
    private fun parseCursorEntry(cursor: Cursor): AudioElement? {
        val title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)) ?: "Unknown"
        val album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM)) ?: "Unknown"
        val artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)) ?: "Unknown"
        val path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA))
        val id = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media._ID)).toString()
        val duration = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION))
        val albumId = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)).toString()
        val uri = Uri.parse("content://media/external/audio/albumart")
        val albumImageUri = Uri.withAppendedPath(uri, albumId)
        val audioElement = AudioElement(
            id = id,
            name = title,
            path = path,
            metadata = AudioMetadata(
                authorName = artist,
                albumTitle = album,
                duration = (duration / 1000).seconds,
                albumImage = albumImageUri,
            ),
        )
        val file = File(audioElement.path)
        return if (file.exists()) audioElement else null
    }

    private val audioProjection = arrayOf(
        MediaStore.Audio.Media._ID,
        MediaStore.Audio.Media.TITLE,
        MediaStore.Audio.Media.ALBUM,
        MediaStore.Audio.Media.ARTIST,
        MediaStore.Audio.Media.DURATION,
        MediaStore.Audio.Media.DATE_ADDED,
        MediaStore.Audio.Media.DATA,
        MediaStore.Audio.Media.ALBUM_ID,
    )
}
