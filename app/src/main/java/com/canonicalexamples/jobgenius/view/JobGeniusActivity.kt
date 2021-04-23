package com.canonicalexamples.jobgenius.view

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.canonicalexamples.jobgenius.R

class JobGeniusActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_job_genius)
        setSupportActionBar(findViewById(R.id.toolbar))

    }

//    // On backPressed is called when ever the back button is clicked
//    override fun onBackPressed() {
//        println(this.intent)
//
//
//        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
//
//        // Set the message show for the Alert time
//        builder.setMessage("Do you want to exit ?")
//
//        // Set Alert Title
//        //builder.setTitle("Alert !")
//
//        // Set Cancelable false so if the user clicks outside the dialog it will be closed
//        builder.setCancelable(false)
//
//        // When the user click yes button the app will be closed
//        builder.setPositiveButton("Yes") { dialog , which ->
//            run {
//                finish()
//                exitProcess(0)
//            }
//        }
//
//        // When the user clicks "no" button the dialog is canceled
//        builder.setNegativeButton("No" , DialogInterface.OnClickListener { dialog , which -> dialog.cancel()})
//
//        // Create the Alert dialog
//        val alertDialog: AlertDialog = builder.create()
//
//        // Show the Alert Dialog box
//        alertDialog.show()
//    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            else -> super.onOptionsItemSelected(item)
        }
    }
}
