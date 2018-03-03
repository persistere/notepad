package caramelo.com.br.notepad.ui.notedetail

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import caramelo.com.br.notepad.R
import caramelo.com.br.notepad.model.Note
import caramelo.com.br.notepad.onTextChange

import kotlinx.android.synthetic.main.activity_note_detail.*
import kotlinx.android.synthetic.main.content_note_detail.*

class NoteDetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_NOTE_ID = "extra_note_id"
    }

    private val model by lazy {
        ViewModelProviders.of(this).get(NoteDetailViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_detail)
        setSupportActionBar(toolbar)

        bindView()

        model.intent = intent
        model.isLoading.observe(this, loadingObserver)
        model.note?.observe(this, noteObserver)
    }

    private fun bindView() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        noteTitleEditText.onTextChange {
            model.note?.value?.title = it
        }

        noteContentEditText.onTextChange {
            model.note?.value?.content = it
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onPause() {
        super.onPause()
        model.save()
    }

    private val loadingObserver = Observer<Boolean> {
        if (it == true) {
            loading.visibility = View.VISIBLE
            noteTitleCardView.visibility = View.GONE
            noteContentCardView.visibility = View.GONE
        } else {
            loading.visibility = View.GONE
            noteTitleCardView.visibility = View.VISIBLE
            noteContentCardView.visibility = View.VISIBLE
        }
    }

    private val noteObserver = Observer<Note> {
        it?.let { data ->
            noteTitleEditText.setText(data.title)
            noteContentEditText.setText(data.content)
        }
    }
}