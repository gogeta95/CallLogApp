package com.example.calllogapp.ui.main

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.PermissionChecker
import androidx.core.content.PermissionChecker.PERMISSION_GRANTED
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.calllogapp.R
import com.example.calllogapp.model.CallRecord
import com.example.calllogapp.service.ServerService
import com.example.calllogapp.ui.main.list.CallDiffCallback
import com.example.calllogapp.ui.main.list.CallLogRecyclerAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val text: TextView by lazy { findViewById(R.id.text) }

    private val btnService: Button by lazy { findViewById(R.id.btn_service) }

    private val recyclerView: RecyclerView by lazy { findViewById(R.id.recycler_View) }

    private val viewModel: MainActivityViewModel by viewModel()
    private val adapter by lazy { CallLogRecyclerAdapter(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupUI()

        viewModel.updateServiceStatus(ServerService.running)

        observeViewModel()

        btnService.setOnClickListener {
            handleServiceButton()
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.onResume()
    }

    private fun setupUI() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    private fun observeViewModel() {
        viewModel.observeUIModel().observe(this) { model ->
            updateServiceStatus(model)
            updateUi(model)
        }
    }

    private fun updateServiceStatus(model: UIModel) {
        if (model.startService == null)
            return

        val intent = Intent(this, ServerService::class.java)

        if (model.startService) {
            startServerService(intent)
        } else {
            stopServerService(intent)
        }
    }

    private fun updateUi(model: UIModel) {
        if (model.serverRunning && model.serverUrl != null) {
            text.text = getString(R.string.server_running_on_s, model.serverUrl)
            btnService.text = getString(R.string.stop_service)
        } else {
            text.text = getString(R.string.server_stopped)
            btnService.text = getString(R.string.start_service)
        }

        adapter.setData(model.records)
    }

    private fun handleServiceButton() {
        checkPermissions {
            viewModel.onServiceButtonClicked(ServerService.running)
        }
    }

    private fun startServerService(intent: Intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent)
        } else {
            startService(intent)
        }
    }

    private fun stopServerService(intent: Intent) {
        stopService(intent)
    }

    private fun checkPermissions(block: () -> Unit) {
        val permissions = arrayOf(
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_CALL_LOG,
            Manifest.permission.READ_CONTACTS
        )

        if (permissions.all {
                PermissionChecker.checkSelfPermission(this, it) == PERMISSION_GRANTED
            }) {
            block()
        } else {
            requestPermissions(permissions, PERMISSIONS_REQ_CODE)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode != PERMISSIONS_REQ_CODE)
            return

        if (grantResults.all { it == PERMISSION_GRANTED }) {
            viewModel.onServiceButtonClicked(ServerService.running)
        } else {
            Toast.makeText(this, getString(R.string.accept_perms), Toast.LENGTH_LONG).show()
        }
    }

    companion object {
        private const val PERMISSIONS_REQ_CODE = 5
    }

    private fun CallLogRecyclerAdapter.setData(records: List<CallRecord>) {
        val diffCallback = CallDiffCallback(this.items ?: emptyList(), records)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.items.clear()
        this.items.addAll(records)
        diffResult.dispatchUpdatesTo(this)
    }
}