package com.hilbing.runningapp.ui.fragments

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.hilbing.runningapp.R
import com.hilbing.runningapp.ui.viewmodels.MainViewModel
import com.hilbing.runningapp.utils.Constants.REQUEST_CODE_LOACTION_PERMISSION
import com.hilbing.runningapp.utils.TrackingUtility
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_run.*
import pub.devrel.easypermissions.EasyPermissions
import pub.devrel.easypermissions.AppSettingsDialog
import android.Manifest

@AndroidEntryPoint
class RunFragment: Fragment(R.layout.fragment_run), EasyPermissions.PermissionCallbacks {

    private val viewModel: MainViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requestPermissions()
        fab.setOnClickListener{
            findNavController().navigate(R.id.action_runFragment_to_trackingFragment)
        }
    }

    private fun requestPermissions(){
        if(TrackingUtility.hasLocationPermissions(requireContext())) {
            return
        }
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.Q){
            EasyPermissions.requestPermissions(
                this,
                getString(R.string.you_need_to_accept_location_permissions_to_use_this_app),
                REQUEST_CODE_LOACTION_PERMISSION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        } else {
            EasyPermissions.requestPermissions(
                this,
                getString(R.string.you_need_to_accept_location_permissions_to_use_this_app),
                REQUEST_CODE_LOACTION_PERMISSION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            )
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {

    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if(EasyPermissions.somePermissionPermanentlyDenied(this, perms)){
            AppSettingsDialog.Builder(this).build().show()
        } else {
            requestPermissions()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

}