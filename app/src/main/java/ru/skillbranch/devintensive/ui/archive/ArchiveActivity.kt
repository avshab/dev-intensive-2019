package ru.skillbranch.devintensive.ui.archive

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_archive.*
import kotlinx.android.synthetic.main.activity_main.fab
import kotlinx.android.synthetic.main.activity_main.toolbar
import ru.skillbranch.devintensive.R
import ru.skillbranch.devintensive.ui.adapters.ArchiveAdapter
import ru.skillbranch.devintensive.ui.adapters.ArchiveTouchHelperCallback
import ru.skillbranch.devintensive.viewmodels.ArchiveViewModel

class ArchiveActivity : AppCompatActivity() {

private lateinit var adapter: ArchiveAdapter
private lateinit var viewModel: ArchiveViewModel

override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_archive)
    initToolbar()
    initViews()
    initViewModel()
    Log.d("M_ProfileActivity", "onCreate")
}

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this).get(ArchiveViewModel::class.java)
        viewModel.getChatData().observe(this, Observer { adapter.updateData(it) })
    }

    private fun initViews() {
        adapter = ArchiveAdapter {
            Snackbar.make(rv_archive_list, "Click on ${it.title}", Snackbar.LENGTH_LONG).show()
        }

        val divider = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        val touchCallback = ArchiveTouchHelperCallback(adapter) {
            val id = it.id
            viewModel.restoreFromArhive(it.id)

            val snackbar = Snackbar.make(rv_archive_list, "Восстановить чат с ${it.title} из архива?", Snackbar.LENGTH_LONG)
            snackbar.view.setBackgroundColor(resources.getColor(R.color.color_snack_bar, theme))
            snackbar.setAction("Отмена", {
                viewModel.addToArchive(id)
                snackbar.dismiss()
            })
            snackbar.show()
        }

        val touchHelper = ItemTouchHelper(touchCallback)
        touchHelper.attachToRecyclerView(rv_archive_list)

        with(rv_archive_list){
            adapter = adapter
            layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this@ArchiveActivity)
            addItemDecoration(divider)
        }

        fab.setOnClickListener {

        }
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if(item?.itemId == android.R.id.home) {
            finish()
            overridePendingTransition(R.anim.idle, R.anim.bottom_down)
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)
        val searchItem = menu?.findItem(R.id.action_search)
        val searchView = searchItem?.actionView as SearchView
        searchView.queryHint = "Введите имя пользователя"
        searchView.setOnQueryTextListener((object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.handleSearchQuery(query)
                return true
            }

            override fun onQueryTextChange(query: String?): Boolean {
                viewModel.handleSearchQuery(query)
                return true
            }

        }))

        return super.onCreateOptionsMenu(menu)
    }
}