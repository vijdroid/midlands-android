package com.chris.thomson.midlandsriders.Adapters

//import androidx.lifecycle.AndroidViewModel
import android.app.AlertDialog
import android.app.Application
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import com.chris.thomson.midlandsriders.R
import com.daimajia.swipe.SwipeLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

//import kotlinx.coroutines.launch

class NotificationsViewAdapter(context: Context?) : RecyclerView.Adapter<NotificationsViewAdapter.NotificationViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var notifications = ArrayList<StoredNotification>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val view = inflater.inflate(R.layout.notification_item, parent, false)
        return NotificationViewHolder(view)
    }

    override fun getItemCount() = notifications.size

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        //holder.title.text = "There's a bike race in November!"
        //holder.body.text = "Everyone is welcome to attend on that day."
        val current = notifications[position]
        holder.body.text = current.body
        holder.title.text = current.title

        holder.swipe.setShowMode(SwipeLayout.ShowMode.LayDown)

        holder.txt_delete.setOnClickListener(View.OnClickListener {
            showAlert(holder.title,position)
        })
    }

    private fun showAlert(txt: TextView,position: Int) {
        val builder = AlertDialog.Builder(txt.context)
        builder.setTitle("Delete Notification")
        builder.setMessage("Are you sure you want to permanently delete?")
        builder.setPositiveButton("YES") { dialog, which ->

            val deleteNoti = DeleteData(txt.text.toString())
            val db = Room.databaseBuilder(txt.context , StoredNotificationRoomDatabase::class.java,"word_database").build()
            GlobalScope.launch {
                db.storedNotificationDao().delete_single_data(txt.text.toString())
            }

            notifications.removeAt(position)
            notifyDataSetChanged()
            Toast.makeText(txt.context, "Deleted " + "!", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }
        builder.setNegativeButton("NO") { dialog, which ->
            // Do nothing
            dialog.dismiss()
        }
        val alert = builder.create()
        alert.show()
    }

    internal fun setNotifications(notifications: ArrayList<StoredNotification>) {
        this.notifications = notifications
        notifyDataSetChanged()
    }

    inner class NotificationViewHolder internal constructor(view: View) : RecyclerView.ViewHolder(view) {
        internal var title: TextView
        internal var body: TextView
        internal var txt_delete: TextView
        internal var swipe: SwipeLayout
        internal var viewBackground : LinearLayout
        internal var viewForeground : LinearLayout

        init {
            title = view.findViewById(R.id.notification_title)
            body = view.findViewById(R.id.notification_body)
            txt_delete = view.findViewById(R.id.txt_delete)
            swipe = view.findViewById(R.id.swipe)
            viewBackground = view.findViewById(R.id.view_background);
            viewForeground = view.findViewById(R.id.view_foreground);
        }
    }
}


@Entity
class StoredNotification(
        @PrimaryKey @ColumnInfo(name = "title") val title: String,
        @ColumnInfo(name = "body") val body: String
)

@Entity
class DeleteData(
        @NonNull @ColumnInfo(name = "title") val title: String
)

@Dao
interface StoredNotificationDAO {

    @Query("SELECT * from storednotification ORDER BY title ASC")
    fun getAlphabetizedNotifications(): LiveData<List<StoredNotification>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(notification: StoredNotification)

    @Query("DELETE FROM storednotification")
    suspend fun deleteAll()

    @NonNull @Query("DELETE FROM storednotification WHERE title LIKE :title1")
    suspend fun delete_single_data(title1: String)

}

@Database(entities = arrayOf(StoredNotification::class), version = 1, exportSchema = false)
abstract class StoredNotificationRoomDatabase : RoomDatabase() {
    abstract fun storedNotificationDao(): StoredNotificationDAO

    private class StoredNotificationDatabaseCallback(
            private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            INSTANCE?.let { database ->
                scope.launch {
                    var storedNotificationDAO = database.storedNotificationDao()

                    // Delete all content here.
                   //storedNotificationDAO.deleteAll()

                    // TODO: Add your own words!
                    //var newNot = StoredNotification("Alert from Midlans!", "This is the body.")
                    //storedNotificationDAO.insert(newNot)
                }
            }
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: StoredNotificationRoomDatabase? = null

        fun getDatabase(
                context: Context,
                scope: CoroutineScope
        ): StoredNotificationRoomDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                        context.applicationContext,
                        StoredNotificationRoomDatabase::class.java,
                        "word_database"
                )
                        .addCallback(StoredNotificationDatabaseCallback(scope))
                        .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}

class StoredNotificationRepository(private val storedNotificationDAO: StoredNotificationDAO) {
    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    val allNotifications: LiveData<List<StoredNotification>> = storedNotificationDAO.getAlphabetizedNotifications()

    suspend fun insert(newNotification: StoredNotification) {
        storedNotificationDAO.insert(newNotification)
    }
}

// Class extends AndroidViewModel and requires application as a parameter.
class StoredNotificationViewModel(application: Application) : AndroidViewModel(application) {

    // The ViewModel maintains a reference to the repository to get data.
    private val repository: StoredNotificationRepository
    // LiveData gives us updated words when they change.
    val allNotifications: LiveData<List<StoredNotification>>

    init {
        // Gets reference to WordDao from WordRoomDatabase to construct
        // the correct WordRepository.
        val storedNotificationDao = StoredNotificationRoomDatabase.getDatabase(application, viewModelScope).storedNotificationDao()
        repository = StoredNotificationRepository(storedNotificationDao)
        allNotifications = repository.allNotifications
    }

    /**
     * The implementation of insert() in the database is completely hidden from the UI.
     * Room ensures that you're not doing any long running operations on
     * the main thread, blocking the UI, so we don't need to handle changing Dispatchers.
     * ViewModels have a coroutine scope based on their lifecycle called
     * viewModelScope which we can use here.
     */
    fun insert(newNotification: StoredNotification) = viewModelScope.launch {
        repository.insert(newNotification)
    }
}

@Entity
class Word(
        @PrimaryKey @ColumnInfo(name = "word") val word: String)

@Dao
interface WordDao {

    @Query("SELECT * from word ORDER BY word ASC")
    fun getAlphabetizedWords(): LiveData<List<Word>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(word: Word)

    @Query("DELETE FROM word")
    suspend fun deleteAll()

    @Query("DELETE FROM word")
    suspend fun delete_single_data()
}

@Database(entities = arrayOf(Word::class), version = 1, exportSchema = false)
abstract class WordRoomDatabase : RoomDatabase() {
    abstract fun wordDao(): WordDao

    private class WordDatabaseCallback(
            private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            INSTANCE?.let { database ->
                scope.launch {
                    var wordDao = database.wordDao()

                    // Delete all content here.
                    wordDao.deleteAll()

                    // Add sample words.
                    var word = Word("Hello")
                    wordDao.insert(word)
                    word = Word("World!")
                    wordDao.insert(word)

                    // TODO: Add your own words!
                    word = Word("TODO!")
                    wordDao.insert(word)
                }
            }
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: WordRoomDatabase? = null

        fun getDatabase(
                context: Context,
                scope: CoroutineScope
        ): WordRoomDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                        context.applicationContext,
                        WordRoomDatabase::class.java,
                        "word_database"
                )
                        .addCallback(WordDatabaseCallback(scope))
                        .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}

class WordRepository(private val wordDao: WordDao) {

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    val allWords: LiveData<List<Word>> = wordDao.getAlphabetizedWords()

    suspend fun insert(word: Word) {
        wordDao.insert(word)
    }
}

// Class extends AndroidViewModel and requires application as a parameter.
class WordViewModel(application: Application) : AndroidViewModel(application) {

    // The ViewModel maintains a reference to the repository to get data.
    private val repository: WordRepository
    // LiveData gives us updated words when they change.
    val allWords: LiveData<List<Word>>

    init {
        // Gets reference to WordDao from WordRoomDatabase to construct
        // the correct WordRepository.
        val wordsDao = WordRoomDatabase.getDatabase(application, viewModelScope).wordDao()
        repository = WordRepository(wordsDao)
        allWords = repository.allWords
    }

    /**
     * The implementation of insert() in the database is completely hidden from the UI.
     * Room ensures that you're not doing any long running operations on
     * the main thread, blocking the UI, so we don't need to handle changing Dispatchers.
     * ViewModels have a coroutine scope based on their lifecycle called
     * viewModelScope which we can use here.
     */
    fun insert(word: Word) = viewModelScope.launch {
        repository.insert(word)
    }
}