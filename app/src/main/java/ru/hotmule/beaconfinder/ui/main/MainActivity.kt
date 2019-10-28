package ru.hotmule.beaconfinder.ui.main

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresApi
import org.altbeacon.beacon.BeaconManager
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.findbeacon.R
import com.example.findbeacon.databinding.ActivityMainBinding
import org.koin.android.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    companion object {
        private const val REQUEST_FINE_LOCATION_PERMISSION = 1
        private const val REQUEST_ENABLE_BLUETOOTH = 2
    }

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    private val viewModel by viewModel<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        navController = findNavController(R.id.nav_host_fragment)

        NavigationUI.setupWithNavController(binding.toolbar, navController)

        if (bluetoothVerified()) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                checkPermissions()
            else
                viewModel.permissionGranted()
        }
    }

    override fun onSupportNavigateUp() = navController.navigateUp()

    private fun bluetoothVerified(): Boolean {

        try {
            if (!BeaconManager.getInstanceForApplication(this).checkAvailability()) {
                val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BLUETOOTH)
            } else
                return true

        } catch (e: RuntimeException) {
            val builder = AlertDialog.Builder(this)
            builder.setTitle(R.string.ble_not_available_title)
            builder.setMessage(R.string.ble_not_available_message)
            builder.setPositiveButton(android.R.string.ok, null)
            builder.setOnDismissListener {
                finish();
            }
            builder.show()
        }

        return false
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun checkPermissions() {

        if (checkSelfPermission(
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_FINE_LOCATION_PERMISSION)

        } else
            viewModel.permissionGranted()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_ENABLE_BLUETOOTH) {
            when (resultCode) {

                Activity.RESULT_OK -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                        checkPermissions()
                }

                Activity.RESULT_CANCELED -> finish()
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>,
                                            grantResults: IntArray) {

        if (requestCode == REQUEST_FINE_LOCATION_PERMISSION) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED)
                finish()
            else
                viewModel.permissionGranted()
        }
    }
}
